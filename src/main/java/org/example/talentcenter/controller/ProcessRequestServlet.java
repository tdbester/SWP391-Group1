package org.example.talentcenter.controller;

import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.talentcenter.service.NotificationService;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "ProcessRequestServlet", value = "/ProcessRequest")
public class ProcessRequestServlet extends HttpServlet {
    private RequestDAO requestDAO = new RequestDAO();
    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }
        ArrayList<Request> requestTypes = requestDAO.getStudentRequestType();
        request.setAttribute("requestTypes", requestTypes);
        try {
            if ("list".equals(action) || action == null) {
                int page = 1;
                int recordsPerPage = 10;
                try {
                    String pageParam = request.getParameter("page");
                    if (pageParam != null) page = Integer.parseInt(pageParam);
                } catch (NumberFormatException ignored) {
                }

                int offset = (page - 1) * recordsPerPage;
                ArrayList<Request> requestList = requestDAO.getAllRequestWithPaging(offset, recordsPerPage);
                int totalRecords = requestDAO.getTotalRequestCount();
                int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

                request.setAttribute("requestList", requestList);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);
            } else if (action.equals("search")) {
                String keyword = request.getParameter("keyword");
                if (keyword == null || keyword.trim().isEmpty()) {
                    response.sendRedirect("ProcessRequest?action=list");
                    return;
                }
                ArrayList<Request> requestList = requestDAO.searchRequests(keyword.trim());
                request.setAttribute("requestList", requestList);
                request.setAttribute("keyword", keyword);
                request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);

            } else if (action.equals("filterByType")) {
                String typeFilter = request.getParameter("typeFilter");
                if (typeFilter == null || typeFilter.trim().isEmpty()) {
                    response.sendRedirect("ProcessRequest?action=list");
                    return;
                }
                ArrayList<Request> requestList = requestDAO.filterRequestsByType(typeFilter.trim());
                request.setAttribute("requestList", requestList);
                request.setAttribute("typeFilter", typeFilter);
                request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);

            } else if (action.equals("filterByStatus")) {
                // ✅ FILTER THEO TRẠNG THÁI
                String statusFilter = request.getParameter("statusFilter");
                if (statusFilter == null || statusFilter.trim().isEmpty()) {
                    response.sendRedirect("ProcessRequest?action=list");
                    return;
                }
                ArrayList<Request> requestList = requestDAO.filterRequestsByStatus(statusFilter.trim());
                request.setAttribute("requestList", requestList);
                request.setAttribute("statusFilter", statusFilter);
                request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);

            } else {
                String requestIdParam = request.getParameter("id");
                if (requestIdParam == null || requestIdParam.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID đơn");
                    return;
                }

                int requestId = Integer.parseInt(requestIdParam);
                Request requestDetail = requestDAO.getRequestDetailById(requestId);

                if (requestDetail == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn");
                    return;
                }

                request.setAttribute("requestDetail", requestDetail);
                request.getRequestDispatcher("/View/process-request-detail.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đơn không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }

        try {
            // Lấy tham số từ form xử lý đơn
            String requestIdParam = request.getParameter("requestId");
            String managerNote = request.getParameter("managerNote");
            String action = request.getParameter("action"); // "approve" hoặc "reject"

            if (requestIdParam == null || managerNote == null || action == null
                    || requestIdParam.trim().isEmpty() || managerNote.trim().isEmpty() || action.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Thiếu thông tin xử lý đơn.");
                request.getRequestDispatcher("View/error.jsp").forward(request, response);
                return;
            }

            int requestId = Integer.parseInt(requestIdParam);
            // lấy thông tin đơn
            Request requestDetail = requestDAO.getRequestDetailById(requestId);
            if (requestDetail == null) {
                request.setAttribute("errorMessage", "Không tìm thấy đơn cần xử lý.");
                request.getRequestDispatcher("View/error.jsp").forward(request, response);
                return;
            }

            String status;
            if ("approve".equals(action)) {
                status = "Đã duyệt";
                if ("Đơn xin chuyển lớp".equals(requestDetail.getTypeName())) {
                    String reason = requestDetail.getReason();
                    if (reason != null && reason.contains("TARGET_CLASS:")) {
                        String targetClassName = reason.split("\\|TARGET_CLASS:")[1];
                        studentDAO.transferStudentToClass(requestDetail.getSenderID(), targetClassName);
                    }
                }
            } else if ("reject".equals(action)) {
                status = "Từ chối";
            } else {
                request.setAttribute("errorMessage", "Hành động không hợp lệ.");
                request.getRequestDispatcher("View/error.jsp").forward(request, response);
                return;
            }

            boolean success = requestDAO.processRequest(requestId, status, managerNote, new Timestamp(new Date().getTime()));

            if (success) {
                // gửi thông báo cho hs nếu xử lý thành công
                try {
                    NotificationService.notifyRequestProcessed(
                            requestId,
                            requestDetail.getSenderName(),
                            requestDetail.getTypeName(),
                            status,
                            managerNote,
                            requestDetail.getSenderID()
                    );
                    System.out.println("Notification sent successfully for request " + requestId);
                } catch (Exception e) {
                    System.err.println("Failed to send notification for request " + requestId + ": " + e.getMessage());
                    e.printStackTrace();
                }
                response.sendRedirect("ProcessRequest?action=list&success=1");
            } else {
                request.setAttribute("errorMessage", "Không thể cập nhật trạng thái đơn.");
                request.getRequestDispatcher("View/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý đơn.");
            request.getRequestDispatcher("View/error.jsp").forward(request, response);
        }
    }
}
