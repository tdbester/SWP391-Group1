package org.example.talentcenter.controller;

import jakarta.servlet.http.*;
import org.example.talentcenter.dao.AccountRequestDAO;
import org.example.talentcenter.model.Request;

import org.example.talentcenter.dao.AccountRequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;

@WebServlet("/StudentAccountRequest")
public class StudentAccountRequestServlet extends HttpServlet {
    private final AccountRequestDAO dao = new AccountRequestDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            request.setAttribute("requests", dao.getAllAccountRequests());
            request.getRequestDispatcher("/View/account-request-list.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int senderId = (Integer) request.getSession().getAttribute("accountId");
        String studentName = request.getParameter("studentName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        HttpSession session = request.getSession();

        String studentInfoText = "Tên: " + studentName + ", Email: " + email + ", SĐT: " + phone;

        AccountRequestDAO dao = new AccountRequestDAO();
        boolean success = dao.sendCreateAccountRequest(senderId, studentInfoText);

        if (success) {
            request.setAttribute("message", "Gửi yêu cầu thành công!");
        } else {
            request.setAttribute("message", "Gửi yêu cầu thất bại!");
        }
        request.getRequestDispatcher("View/student-account-request.jsp").forward(request, response);
    }
}
