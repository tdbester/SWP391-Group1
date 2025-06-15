package org.example.talentcenter.controller;

import jakarta.servlet.http.*;
import org.example.talentcenter.dao.RequestDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;

@WebServlet("/StudentAccountRequest")
public class StudentAccountRequestServlet extends HttpServlet {
    private final RequestDAO dao = new RequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            request.setAttribute("requests", dao.getAllAccountRequests());
            request.getRequestDispatcher("/View/account-request-list.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/View/student-account-request.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int senderId = (Integer) request.getSession().getAttribute("accountId");
        String studentName = request.getParameter("studentName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        HttpSession session = request.getSession();

        String studentInfoText = studentName + "|" + email + "|" + phone;

        RequestDAO dao = new RequestDAO();
        boolean success = dao.sendCreateAccountRequest(senderId, studentName, email, phone);
        if (success) {
            request.setAttribute("message", "Gửi yêu cầu thành công!");
        } else {
            request.setAttribute("message", "Gửi yêu cầu thất bại!");
        }
        request.getRequestDispatcher("View/student-account-request.jsp").forward(request, response);
    }
}
