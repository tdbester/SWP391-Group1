package org.example.talentcenter.controller;

import org.example.talentcenter.dao.UserDAO;
import org.example.talentcenter.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/changePassword")
public class ChangePasswordServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy thông tin user từ database
        User user = userDAO.getUserById(userId);
        if (user != null) {
            request.setAttribute("user", user);
        }
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
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
            doGet(request, response);
            return;
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("passwordError", "Vui lòng nhập mật khẩu mới!");
            doGet(request, response);
            return;
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("passwordError", "Vui lòng xác nhận mật khẩu mới!");
            doGet(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận có khớp không
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("passwordError", "Mật khẩu mới không khớp!");
            doGet(request, response);
            return;
        }

        // Kiểm tra độ dài mật khẩu mới
        if (newPassword.length() < 6) {
            request.setAttribute("passwordError", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            doGet(request, response);
            return;
        }

        // Kiểm tra mật khẩu hiện tại có đúng không
        User user = userDAO.getUserById(userId);
        if (user == null) {
            request.setAttribute("passwordError", "Không tìm thấy thông tin người dùng!");
            doGet(request, response);
            return;
        }
        if (!user.getPassword().equals(oldPassword)) {
            request.setAttribute("passwordError", "Mật khẩu hiện tại không đúng!");
            doGet(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới không trùng với mật khẩu cũ
        if (oldPassword.equals(newPassword)) {
            request.setAttribute("passwordError", "Mật khẩu mới phải khác mật khẩu hiện tại!");
            doGet(request, response);
            return;
        }

        // Cập nhật mật khẩu
        boolean isUpdated = userDAO.updatePassword(userId, newPassword);

        if (isUpdated) {
            request.setAttribute("passwordSuccess", "Đổi mật khẩu thành công!");
            User updatedUser = userDAO.getUserById(userId);
            session.setAttribute("user", updatedUser);
            request.setAttribute("user", updatedUser);
        } else {
            request.setAttribute("passwordError", "Có lỗi xảy ra khi đổi mật khẩu!");
        }

        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}