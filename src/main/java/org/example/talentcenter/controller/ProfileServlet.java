package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.example.talentcenter.dao.AccountDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.utilities.CloudinaryUtils;
import java.io.IOException;

@WebServlet("/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ProfileServlet extends HttpServlet {
    private AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer accountId = (Integer) session.getAttribute("accountId");

        if (accountId == null) {
            response.sendRedirect("/View/login.jsp");
            return;
        }

        // Lấy thông tin account từ database
        Account account = accountDAO.getAccountById(accountId);
        if (account != null) {
            session.setAttribute("account", account);
        }

        request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer accountId = (Integer) session.getAttribute("accountId");

        if (accountId == null) {
            response.sendRedirect("/View/login.jsp");
            return;
        }

        // Lấy dữ liệu từ form
        String fullName = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        // Xử lý upload ảnh
        Part avatarPart = request.getPart("avatar");
        String newAvatarUrl = null;

        // Validate dữ liệu
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Họ và tên không được để trống!");
            request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
            return;
        }

        if (phone == null || !phone.matches("^0\\d{9}$")) {
            request.setAttribute("error", "Số điện thoại không hợp lệ! (10 số, bắt đầu bằng 0)");
            request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
            return;
        }

        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            request.setAttribute("error", "Email không hợp lệ!");
            request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra email và phone đã tồn tại chưa
        if (accountDAO.isEmailExists(email, accountId)) {
            request.setAttribute("error", "Email này đã được sử dụng bởi tài khoản khác!");
            request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
            return;
        }

        if (accountDAO.isPhoneExists(phone, accountId)) {
            request.setAttribute("error", "Số điện thoại này đã được sử dụng bởi tài khoản khác!");
            request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
            return;
        }

        // Xử lý upload ảnh nếu có
        if (avatarPart != null && avatarPart.getSize() > 0) {
            try {
                // Validate file type
                String contentType = avatarPart.getContentType();
                if (!contentType.startsWith("image/")) {
                    request.setAttribute("error", "Chỉ được upload file ảnh!");
                    request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
                    return;
                }

                // Validate file size (5MB)
                if (avatarPart.getSize() > 5 * 1024 * 1024) {
                    request.setAttribute("error", "Kích thước file không được vượt quá 5MB!");
                    request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
                    return;
                }

                // Upload ảnh mới lên Cloudinary
                newAvatarUrl = CloudinaryUtils.uploadImage(avatarPart);

                // Xóa ảnh cũ nếu có
                Account currentAccount = accountDAO.getAccountById(accountId);
                if (currentAccount != null && currentAccount.getAvatar() != null) {
                    CloudinaryUtils.deleteImage(currentAccount.getAvatar());
                }

            } catch (IOException e) {
                request.setAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
                request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
                return;
            }
        }

        // Cập nhật thông tin
        boolean isUpdated = accountDAO.updateAccountProfile(accountId, fullName.trim(), phone, email, address, newAvatarUrl);

        if (isUpdated) {
            request.setAttribute("success", "Cập nhật thông tin thành công!");

            // Cập nhật lại thông tin account trong session
            Account updatedAccount = accountDAO.getAccountById(accountId);
            session.setAttribute("account", updatedAccount);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin!");
        }

        // Forward về trang profile
        request.getRequestDispatcher("/View/profile.jsp").forward(request, response);
    }
}