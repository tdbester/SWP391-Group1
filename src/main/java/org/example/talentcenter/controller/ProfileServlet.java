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

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy thông tin account từ database
        Account account = accountDAO.getAccountById(userId);
        if (account != null) {
            request.setAttribute("account", account);
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
        String address = request.getParameter("address");

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
        if (accountDAO.isEmailExists(email, userId)) {
            request.setAttribute("error", "Email này đã được sử dụng bởi tài khoản khác!");
            doGet(request, response);
            return;
        }

        if (accountDAO.isPhoneExists(phone, userId)) {
            request.setAttribute("error", "Số điện thoại này đã được sử dụng bởi tài khoản khác!");
            doGet(request, response);
            return;
        }

        // Cập nhật thông tin
        boolean isUpdated = accountDAO.updateAccountProfile(userId, fullName.trim(), phone, email, address);

        if (isUpdated) {
            request.setAttribute("success", "Cập nhật thông tin thành công!");

            // Cập nhật lại thông tin account trong session
            Account updatedAccount = accountDAO.getAccountById(userId);
            session.setAttribute("account", updatedAccount);
            request.setAttribute("account", updatedAccount);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin!");
        }

        // Forward về trang profile
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}