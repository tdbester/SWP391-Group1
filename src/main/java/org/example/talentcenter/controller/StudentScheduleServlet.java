package org.example.talentcenter.controller;

import org.example.talentcenter.dao.StudentScheduleDAO;
import org.example.talentcenter.dao.StudentDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.StudentSchedule;
import org.example.talentcenter.config.DBConnect;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/teacher/schedule")
public class StudentScheduleServlet extends HttpServlet {
    public static StudentDAO studentDAO = new StudentDAO();
    public static StudentScheduleDAO studentScheduleDAO = new StudentScheduleDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("user");

        if (account == null) {
            response.sendRedirect("/login.jsp");
            return;
        }

        try (Connection conn = DBConnect.getConnection()) {
            int studentId = studentDAO.getStudentById(account.getId()).getId();
            List<StudentSchedule> schedules = studentScheduleDAO.getScheduleByStudentId(studentId);

            request.setAttribute("schedules", schedules);
            request.getRequestDispatcher("View/student-schedule.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}