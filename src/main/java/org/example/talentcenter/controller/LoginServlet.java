package org.example.talentcenter.controller;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.talentcenter.dao.LoginDAO;
import org.example.talentcenter.model.Account;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("View/login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set request encoding to handle Vietnamese characters
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Basic validation
        if (email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ email và mật khẩu");
            request.getRequestDispatcher("/View/login.jsp").forward(request, response);
            return;
        }

        try {
            LoginDAO dao = new LoginDAO();
            Account account = dao.checkLogin(email.trim(), password);

            if (account != null) {
                // Get role name for the account
                String roleName = dao.getRoleName(account.getRoleId());

                // Create session and store user information
                HttpSession session = request.getSession();
                session.setAttribute("account", account);
                session.setAttribute("accountId", account.getId());
                session.setAttribute("userEmail", account.getEmail());
                session.setAttribute("userFullName", account.getFullName());
                session.setAttribute("userRole", roleName);
                session.setAttribute("roleId", account.getRoleId());

                // Redirect based on role
                if (roleName != null) {
                    switch (roleName.toLowerCase()) {
                        case "admin":
                            response.sendRedirect(request.getContextPath() + "/View/home.jsp");
                            break;
                        case "student":
                            response.sendRedirect(request.getContextPath() + "/StudentDashboard");
                            break;
                        case "teacher":
                            response.sendRedirect(request.getContextPath() + "/TeacherDashboard");
                            break;
                        case "sale":
                            response.sendRedirect("SaleDashboard");
                            break;
                        case "training manager":
                            response.sendRedirect(request.getContextPath() + "/TrainingManagerDashboard");
                            break;
                        default:
                            response.sendRedirect(request.getContextPath() + "/View/home.jsp");
                            break;

                    }
                } else {
                    response.sendRedirect("View/home.jsp");
                }

            } else {
                request.setAttribute("error", "Email hoặc mật khẩu không đúng");
                request.setAttribute("email", email);
                request.getRequestDispatcher("/View/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            /*request.setAttribute("error", "Có lỗi xảy ra trong quá trình đăng nhập. Vui lòng thử lại.");
            request.setAttribute("email", email); // Keep email for user convenience
            request.getRequestDispatcher("/View/login.jsp").forward(request, response);*/
            PrintWriter out = response.getWriter();
            out.println("Lỗi server:");
            e.printStackTrace(out);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Login Servlet for Talent Center Application";
    }
}