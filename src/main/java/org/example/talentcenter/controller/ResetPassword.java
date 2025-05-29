package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.talentcenter.dao.TokenForgetDAO;
import org.example.talentcenter.dao.UserDAO;
import org.example.talentcenter.model.TokenForgetPassword;
import org.example.talentcenter.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 *
 * @author HP
 */
@WebServlet(name="resetPassword", urlPatterns={"/resetPassword"})
public class ResetPassword extends HttpServlet {
    TokenForgetDAO DAOToken = new TokenForgetDAO();
    UserDAO DAOUser = new UserDAO();
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet resetPassword</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet resetPassword at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        HttpSession session = request.getSession();

        if(token != null) {
            TokenForgetPassword tokenForgetPassword = DAOToken.getTokenPassword(token);
            resetService service = new resetService();

            if(tokenForgetPassword == null) {
                request.setAttribute("error", "Token không hợp lệ");
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }
            if(tokenForgetPassword.isUsed()) {
                request.setAttribute("error", "Token đã được sử dụng");
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }
            if(service.isExpireTime(tokenForgetPassword.getExpiryTime())) {
                request.setAttribute("error", "Token đã hết hạn");
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }

            User user = DAOUser.getUserById(tokenForgetPassword.getUserId());
            request.setAttribute("email", user.getEmail());
            session.setAttribute("token", token); // ← Đảm bảo dòng này có
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate password...
        if(!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới không khớp!");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        String tokenStr = (String) session.getAttribute("token");

        // FIXED: Also check for token in request parameter as backup
        if (tokenStr == null) {
            tokenStr = request.getParameter("token");
        }

        if (tokenStr == null) {
            request.setAttribute("error", "Token không hợp lệ");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        TokenForgetPassword tokenForgetPassword = DAOToken.getTokenPassword(tokenStr);

        // Check token is valid, expired, or used
        resetService service = new resetService();
        if (tokenForgetPassword == null) {
            request.setAttribute("error", "Token không hợp lệ");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }
        if (tokenForgetPassword.isUsed()) {
            request.setAttribute("error", "Token đã được sử dụng");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }
        if (service.isExpireTime(tokenForgetPassword.getExpiryTime())) {
            request.setAttribute("error", "Token đã hết hạn");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        // Update password and mark token as used
        try {
            DAOUser.updatePasswordByEmail(email, password);

            // Mark token as used - FIXED: Don't set token again, just mark as used
            tokenForgetPassword.setUsed(true);
            boolean isUpdated = DAOToken.updateStatus(tokenForgetPassword);

            if (!isUpdated) {
                request.setAttribute("error", "Lỗi khi cập nhật trạng thái token");
                request.setAttribute("email", email);
                request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
                return;
            }

            // Clear session
            session.removeAttribute("token");

            // Success message
            request.setAttribute("success", "Đổi mật khẩu thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("Error updating password: " + e);
            request.setAttribute("error", "Lỗi server khi đổi mật khẩu");
            request.setAttribute("email", email);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
        }
    }

}
