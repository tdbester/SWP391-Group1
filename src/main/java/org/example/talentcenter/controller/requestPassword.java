package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.talentcenter.dao.TokenForgetDAO;
import org.example.talentcenter.dao.UserDAO;
import org.example.talentcenter.model.TokenForgetPassword;
import org.example.talentcenter.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet (name = "requestPassword", urlPatterns = {"/requestPassword"})
public class requestPassword extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet requestPassword</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet requestPassword</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDAO daoUser = new UserDAO();
        String email =request.getParameter("email");
        //email co ton tai trong db k
        User user = daoUser.getUserByEmail(email);
        if(user==null){
            request.setAttribute("error","Email không tồn tại");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }
        resetService service = new resetService();
        String token = service.generateToken();
        String linkReset = "http://localhost:9999/TalentCenter_war_exploded/resetPassword?token="+token;

        TokenForgetPassword newTokenForget = new TokenForgetPassword(user.getId(), false, token, service.expireDateTime());

        //gui link vao email
        TokenForgetDAO daoToken = new TokenForgetDAO();
        boolean isInsert = daoToken.insertTokenForget(newTokenForget);
        if(!isInsert){
            request.setAttribute("error","Lỗi server");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }
        boolean isSend = service.sendEmail(email, linkReset, user.getFull_name());
        if(!isSend){
            request.setAttribute("error","Lỗi trong quá trình gửi mail");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }
        request.setAttribute("error","Kiểm tra email của bạn!");
        request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
    }

}
