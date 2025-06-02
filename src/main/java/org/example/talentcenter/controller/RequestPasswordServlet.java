package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.talentcenter.dao.AccountDAO;
import org.example.talentcenter.dao.TokenForgetDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.TokenForgetPassword;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "requestPassword", urlPatterns = {"/requestPassword"})
public class RequestPasswordServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Request Password Reset</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Request Password Reset</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set encoding for Vietnamese characters
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");

        // Validate email input
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        email = email.trim();

        try {
            AccountDAO accountDAO = new AccountDAO();
            Account account = accountDAO.getAccountByEmail(email);

            if (account == null) {
                request.setAttribute("error", "Email không tồn tại trong hệ thống");
                request.setAttribute("email", email); // Keep email for user convenience
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }

            ResetService service = new ResetService();
            String token = service.generateToken();
            String linkReset = "http://localhost:9999/TalentCenter_war_exploded/resetPassword?token=" + token;

            TokenForgetPassword newTokenForget = new TokenForgetPassword(
                    token,
                    service.expireDateTime(),
                    false,
                    account.getId()
            );

            // Save token to database
            TokenForgetDAO tokenDAO = new TokenForgetDAO();
            boolean isInserted = tokenDAO.insertTokenForget(newTokenForget);

            if (!isInserted) {
                request.setAttribute("error", "Lỗi hệ thống, không thể tạo token reset");
                request.setAttribute("email", email);
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }

            // Send email with reset link
            boolean isSent = service.sendEmail(email, linkReset, account.getFullName());

            if (!isSent) {
                request.setAttribute("error", "Lỗi trong quá trình gửi email. Vui lòng thử lại.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }

            request.setAttribute("error", "Vui lòng kiểm tra hộp thư!");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra trong quá trình xử lý. Vui lòng thử lại.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
    }
}