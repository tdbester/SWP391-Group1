// ChangePasswordServlet.java
package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.talentcenter.dao.AccountDAO;
import org.example.talentcenter.model.Account;

import java.io.IOException;

@WebServlet("/changePassword")
public class ChangePasswordServlet extends HttpServlet {
    private AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer accountId = (Integer) session.getAttribute("accountId");

        if (accountId == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }

        // Lấy thông tin account từ database
        Account account = accountDAO.getAccountById(accountId);
        if (account != null) {
            request.setAttribute("account", account);
        }
        request.getRequestDispatcher("View/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer accountId = (Integer) session.getAttribute("accountId");

        if (accountId == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }

        String oldPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        //String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{6,}$";
        //y/c mk có số, chữ, kí tự

        // Validate dữ liệu đầu vào
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            request.setAttribute("passwordError", "Vui lòng nhập mật khẩu hiện tại!");
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
            return;
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("passwordError", "Vui lòng nhập mật khẩu mới!");
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
            return;
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("passwordError", "Vui lòng xác nhận mật khẩu mới!");
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận có khớp không
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("passwordError", "Mật khẩu mới không khớp!");
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra độ dài mật khẩu mới
        if (newPassword.length() < 6) {
            request.setAttribute("passwordError", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu hiện tại có đúng không
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            request.setAttribute("passwordError", "Không tìm thấy thông tin người dùng!");
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
            return;
        }
        if (!account.getPassword().equals(oldPassword)) {
            request.setAttribute("passwordError", "Mật khẩu hiện tại không đúng!");
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới không trùng với mật khẩu cũ
        if (oldPassword.equals(newPassword)) {
            request.setAttribute("passwordError", "Mật khẩu mới phải khác mật khẩu hiện tại!");
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
            return;
        }

        // Cập nhật mật khẩu
        boolean isUpdated = accountDAO.updatePassword(accountId, newPassword);

        if (isUpdated) {
            request.setAttribute("passwordSuccess", "Đổi mật khẩu thành công!");
            Account updatedAccount = accountDAO.getAccountById(accountId);
            session.setAttribute("account", updatedAccount);
            request.setAttribute("account", updatedAccount);
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
        } else {
            request.setAttribute("passwordError", "Có lỗi xảy ra khi đổi mật khẩu!");
        }
    }
}