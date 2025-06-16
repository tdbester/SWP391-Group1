package org.example.talentcenter.controller;

import org.example.talentcenter.dao.ScheduleDAO;
import org.example.talentcenter.dao.TeacherDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Schedule;
import org.example.talentcenter.config.DBConnect;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/teacher/schedule")
public class ScheduleServlet extends HttpServlet {
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
            int teacherId = TeacherDAO.getTeacherIdByAccountId(conn, account.getId());
            ScheduleDAO scheduleDAO = new ScheduleDAO(conn);
            List<Schedule> schedules = scheduleDAO.getScheduleByTeacherId(teacherId);

            request.setAttribute("schedules", schedules);
            request.getRequestDispatcher("View/schedule.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}
