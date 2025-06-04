package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.talentcenter.dao.AccountDAO;
import org.example.talentcenter.dao.TokenForgetDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.TokenForgetPassword;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "resetPassword", urlPatterns = {"/resetPassword"})
public class ResetPasswordServlet extends HttpServlet {

    private TokenForgetDAO tokenDAO = new TokenForgetDAO();
    private AccountDAO accountDAO = new AccountDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Reset Password</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Reset Password</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");
        HttpSession session = request.getSession();

        if (token != null && !token.trim().isEmpty()) {
            try {
                TokenForgetPassword tokenForgetPassword = tokenDAO.getTokenPassword(token);
                ResetService service = new ResetService();

                // Validate token
                if (tokenForgetPassword == null) {
                    request.setAttribute("error", "Token không hợp lệ hoặc không tồn tại");
                    request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                    return;
                }

                if (tokenForgetPassword.isUsed()) {
                    request.setAttribute("error", "Token đã được sử dụng. Vui lòng yêu cầu đặt lại mật khẩu mới.");
                    request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                    return;
                }

                if (service.isExpireTime(tokenForgetPassword.getExpiryTime())) {
                    request.setAttribute("error", "Token đã hết hạn. Vui lòng yêu cầu đặt lại mật khẩu mới.");
                    request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                    return;
                }

                Account account = accountDAO.getAccountById(tokenForgetPassword.getAccountId());
                if (account == null) {
                    request.setAttribute("error", "Không tìm thấy tài khoản liên kết với token này");
                    request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                    return;
                }

                request.setAttribute("email", account.getEmail());
                session.setAttribute("resetToken", token);
                session.setAttribute("resetAccountId", account.getId());

                request.getRequestDispatcher("resetPassword.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Có lỗi xảy ra khi xử lý token");
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Token không hợp lệ");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        //validation
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu mới");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng xác nhận mật khẩu mới");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }

        // Password strength validation
        if (password.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        String tokenStr = (String) session.getAttribute("resetToken");
        Integer accountId = (Integer) session.getAttribute("resetAccountId");

        if (tokenStr == null || accountId == null) {
            request.setAttribute("error", "Phiên đặt lại mật khẩu đã hết hạn. Vui lòng thực hiện lại từ đầu.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        try {
            TokenForgetPassword tokenForgetPassword = tokenDAO.getTokenPassword(tokenStr);
            ResetService service = new ResetService();

            // Re-validate token (security check)
            if (tokenForgetPassword == null || tokenForgetPassword.isUsed() ||
                    service.isExpireTime(tokenForgetPassword.getExpiryTime())) {
                request.setAttribute("error", "Token không hợp lệ hoặc đã hết hạn");
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }

            // Update password
            boolean passwordUpdated = accountDAO.updatePasswordByEmail(email, password);

            if (!passwordUpdated) {
                request.setAttribute("error", "Không thể cập nhật mật khẩu. Vui lòng thử lại.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
                return;
            }

            // Mark token as used
            tokenForgetPassword.setUsed(true);
            boolean tokenUpdated = tokenDAO.updateStatus(tokenForgetPassword);

            if (!tokenUpdated) {
                System.out.println("Warning: Password updated but token status not updated for token: " + tokenStr);
            }

            session.removeAttribute("resetToken");
            session.removeAttribute("resetAccountId");

            request.setAttribute("success", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập với mật khẩu mới.");
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi đặt lại mật khẩu. Vui lòng thử lại.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Reset Password Servlet for Talent Center Application";
    }
}