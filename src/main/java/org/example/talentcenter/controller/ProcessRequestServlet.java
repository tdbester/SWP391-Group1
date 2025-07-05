package org.example.talentcenter.controller;

import org.example.talentcenter.dao.RequestDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "ProcessRequestServlet", value = "/ProcessRequest")
public class ProcessRequestServlet extends HttpServlet {
    private RequestDAO requestDAO = new RequestDAO();

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

        try {
            if ("list".equals(action)) {
                // Hiển thị danh sách tất cả đơn
                ArrayList<Request> requestList = requestDAO.getAllRequest();
                request.setAttribute("requestList", requestList);
                request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);

            } else {
                // Hiển thị chi tiết đơn để xử lý
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
            String status;
            if ("approve".equals(action)) {
                status = "Đã duyệt";
            } else if ("reject".equals(action)) {
                status = "Từ chối";
            } else {
                request.setAttribute("errorMessage", "Hành động không hợp lệ.");
                request.getRequestDispatcher("View/error.jsp").forward(request, response);
                return;
            }

            boolean success = requestDAO.processRequest(requestId, status, managerNote, new Timestamp(new Date().getTime()));

            if (success) {
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
