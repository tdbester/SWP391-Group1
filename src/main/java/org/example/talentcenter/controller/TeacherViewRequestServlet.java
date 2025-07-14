package org.example.talentcenter.controller;

import jakarta.servlet.annotation.WebServlet;
import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "teacherViewRequest", value = "/teacherViewRequest")
public class TeacherViewRequestServlet extends HttpServlet {

    private TeacherDAO teacherDAO;
    private TeacherRequestDAO requestDAO;

    @Override
    public void init() throws ServletException {
        teacherDAO = new TeacherDAO();
        requestDAO = new TeacherRequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            response.sendRedirect(request.getContextPath() + "/View/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if (action != null){
            switch (action) {
                case "delete":
                    handleDeleteRequest(request, response, session);
                    break;
                case "filter":
                    handleFilterRequests(request, response, session);
                    break;
                case "search":
                    handleSearchRequests(request, response, session);
                    break;
                case "viewDetail":
                    handleViewRequestDetail(request, response, session);
                    break;
                default:
                    loadAllRequests(request, response, session);
            }
        } else {
            loadAllRequests(request, response, session);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void loadAllRequests(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        try {
            int accountId = (int) session.getAttribute("accountId");

            // Lấy thông tin giáo viên
            Teacher teacher = teacherDAO.getTeacherByAccountId(accountId);
            if (teacher == null) {
                request.setAttribute("error", "Không tìm thấy thông tin giáo viên!");
                request.getRequestDispatcher("/View/error.jsp").forward(request, response);
                return;
            }

            // Lấy danh sách tất cả đơn từ của giáo viên
            ArrayList<Request> requests = requestDAO.getRequestsBySenderId(accountId);

            // Lấy thông tin phân trang
            int page = 1;
            int pageSize = 10;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // Tính toán phân trang
            int totalRequests = requests.size();
            int totalPages = (int) Math.ceil((double) totalRequests / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalRequests);

            ArrayList<Request> paginatedRequests = new ArrayList<>();
            if (startIndex < totalRequests) {
                paginatedRequests = new ArrayList<>(requests.subList(startIndex, endIndex));
            }

            // Set attributes cho JSP
            request.setAttribute("requests", paginatedRequests);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRequests", totalRequests);

            // Thống kê đơn từ theo trạng thái
            int pendingCount = 0, approvedCount = 0, rejectedCount = 0;
            for (Request req : requests) {
                switch (req.getStatus().toLowerCase()) {
                    case "pending":
                        pendingCount++;
                        break;
                    case "approved":
                        approvedCount++;
                        break;
                    case "rejected":
                        rejectedCount++;
                        break;
                }
            }

            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("approvedCount", approvedCount);
            request.setAttribute("rejectedCount", rejectedCount);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải danh sách đơn từ!");
        }

        request.getRequestDispatcher("/View/teacher-view-request.jsp").forward(request, response);
    }

    private void handleFilterRequests(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        try {
            int accountId = (int) session.getAttribute("accountId");
            String requestType = request.getParameter("requestType");
            String status = request.getParameter("status");

            // Lấy danh sách đơn từ theo bộ lọc
            ArrayList<Request> requests = requestDAO.getFilteredRequests(accountId, requestType, status);

            // Lấy thông tin phân trang
            int page = 1;
            int pageSize = 10;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // Tính toán phân trang
            int totalRequests = requests.size();
            int totalPages = (int) Math.ceil((double) totalRequests / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalRequests);

            ArrayList<Request> paginatedRequests = new ArrayList<>();
            if (startIndex < totalRequests) {
                paginatedRequests = new ArrayList<>(requests.subList(startIndex, endIndex));
            }

            // Set attributes cho JSP
            request.setAttribute("requests", paginatedRequests);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRequests", totalRequests);
            request.setAttribute("selectedRequestType", requestType);
            request.setAttribute("selectedStatus", status);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi lọc danh sách đơn từ!");
        }

        request.getRequestDispatcher("/View/teacher-view-request.jsp").forward(request, response);
    }

    private void handleSearchRequests(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        try {
            int accountId = (int) session.getAttribute("accountId");
            String searchKeyword = request.getParameter("searchKeyword");

            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                loadAllRequests(request, response, session);
                return;
            }

            // Tìm kiếm đơn từ theo từ khóa
            ArrayList<Request> requests = requestDAO.searchRequests(accountId, searchKeyword.trim());

            // Lấy thông tin phân trang
            int page = 1;
            int pageSize = 10;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // Tính toán phân trang
            int totalRequests = requests.size();
            int totalPages = (int) Math.ceil((double) totalRequests / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalRequests);

            ArrayList<Request> paginatedRequests = new ArrayList<>();
            if (startIndex < totalRequests) {
                paginatedRequests = new ArrayList<>(requests.subList(startIndex, endIndex));
            }

            // Set attributes cho JSP
            request.setAttribute("requests", paginatedRequests);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRequests", totalRequests);
            request.setAttribute("searchKeyword", searchKeyword);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tìm kiếm đơn từ!");
        }

        request.getRequestDispatcher("/View/teacher-view-request.jsp").forward(request, response);
    }

    private void handleDeleteRequest(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        try {
            String requestIdStr = request.getParameter("requestId");
            if (requestIdStr == null || requestIdStr.trim().isEmpty()) {
                request.setAttribute("error", "ID đơn từ không hợp lệ!");
                loadAllRequests(request, response, session);
                return;
            }

            int requestId = Integer.parseInt(requestIdStr);
            int accountId = (int) session.getAttribute("accountId");

            // Kiểm tra quyền sở hữu đơn từ
            Request requestToDelete = requestDAO.getRequestById(requestId);
            if (requestToDelete == null || requestToDelete.getSenderID() != accountId) {
                request.setAttribute("error", "Bạn không có quyền xóa đơn từ này!");
                loadAllRequests(request, response, session);
                return;
            }

            // Chỉ cho phép xóa đơn từ ở trạng thái "Pending"
            if (!"Pending".equalsIgnoreCase(requestToDelete.getStatus())) {
                request.setAttribute("error", "Chỉ được xóa đơn từ đang chờ xử lý!");
                loadAllRequests(request, response, session);
                return;
            }

            // Xóa đơn từ
            boolean success = requestDAO.deleteRequest(requestId);

            if (success) {
                request.setAttribute("success", "Xóa đơn từ thành công!");
            } else {
                request.setAttribute("error", "Xóa đơn từ thất bại!");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID đơn từ không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi xóa đơn từ!");
        }

        loadAllRequests(request, response, session);
    }

    private void handleViewRequestDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        try {
            String requestIdStr = request.getParameter("requestId");
            if (requestIdStr == null || requestIdStr.trim().isEmpty()) {
                request.setAttribute("error", "ID đơn từ không hợp lệ!");
                loadAllRequests(request, response, session);
                return;
            }

            int requestId = Integer.parseInt(requestIdStr);
            int accountId = (int) session.getAttribute("accountId");

            // Lấy chi tiết đơn từ
            Request requestDetail = requestDAO.getRequestById(requestId);
            if (requestDetail == null || requestDetail.getSenderID() != accountId) {
                request.setAttribute("error", "Không tìm thấy đơn từ hoặc bạn không có quyền xem!");
                loadAllRequests(request, response, session);
                return;
            }

            String processorName = null;
            if (requestDetail.getProcessedBy() > 0) {
                processorName = requestDAO.getProcessorName(requestDetail.getProcessedBy());
            }

            request.setAttribute("requestDetail", requestDetail);
            request.setAttribute("processorName", processorName);
            request.setAttribute("showDetailModal", true);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID đơn từ không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi xem chi tiết đơn từ!");
        }

        loadAllRequests(request, response, session);
    }
}