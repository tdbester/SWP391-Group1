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

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
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

        // Set encoding để hỗ trợ tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy dữ liệu từ form
        String fullName = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        // Validate dữ liệu
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Họ và tên không được để trống!");
            doGet(request, response);
            return;
        }

        if (phone == null || !phone.matches("^0\\d{9}$")) {
            request.setAttribute("error", "Số điện thoại không hợp lệ! (10 số, bắt đầu bằng 0)");
            doGet(request, response);
            return;
        }

        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            request.setAttribute("error", "Email không hợp lệ!");
            doGet(request, response);
            return;
        }

        // Kiểm tra email và phone đã tồn tại chưa
        if (userDAO.isEmailExists(email, userId)) {
            request.setAttribute("error", "Email này đã được sử dụng bởi tài khoản khác!");
            doGet(request, response);
            return;
        }

        if (userDAO.isPhoneExists(phone, userId)) {
            request.setAttribute("error", "Số điện thoại này đã được sử dụng bởi tài khoản khác!");
            doGet(request, response);
            return;
        }

        // Cập nhật thông tin
        boolean isUpdated = userDAO.updateUserProfile(userId, fullName.trim(), phone, email);

        if (isUpdated) {
            request.setAttribute("success", "Cập nhật thông tin thành công!");

            // Cập nhật lại thông tin user trong session
            User updatedUser = userDAO.getUserById(userId);
            session.setAttribute("user", updatedUser);
            request.setAttribute("user", updatedUser);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin!");
        }

        // Forward về trang profile
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}