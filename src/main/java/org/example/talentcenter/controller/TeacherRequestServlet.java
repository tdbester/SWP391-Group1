package org.example.talentcenter.controller;

import jakarta.servlet.annotation.WebServlet;
import org.example.talentcenter.dao.RequestDAO;
import org.example.talentcenter.model.Request;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "requestForm", value = "/requestForm")
public class TeacherRequestServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("View/teacher-request.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            String type = request.getParameter("type");
            String reason = request.getParameter("reason");
            int senderId = (int) session.getAttribute("accountId");
            String status = "Pending";

            Request req = new Request();
            req.setType(type);
            req.setReason(reason);
            req.setSenderID(senderId);
            req.setStatus(status);

            RequestDAO requestDAO = new RequestDAO();
            boolean inserted = requestDAO.insertRequest(req);

            if (inserted) {
                request.setAttribute("success", "Gửi đơn thành công!");
            } else {
                request.setAttribute("error", "Gửi đơn thất bại, vui lòng thử lại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại!");
        }
        request.getRequestDispatcher("View/teacher-request.jsp").forward(request, response);
    }

}
