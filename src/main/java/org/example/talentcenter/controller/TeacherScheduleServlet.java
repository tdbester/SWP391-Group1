package org.example.talentcenter.controller;

import org.example.talentcenter.dao.TeacherScheduleDAO;
import org.example.talentcenter.dao.TeacherDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Schedule;
import org.example.talentcenter.config.DBConnect;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "TeacherScheduleServlet", value = "/TeacherSchedule")
public class TeacherScheduleServlet extends HttpServlet {
    public static TeacherDAO teacherDAO = new TeacherDAO();
    public static TeacherScheduleDAO teacherScheduleDAO = new TeacherScheduleDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }

        try (Connection conn = DBConnect.getConnection()) {
            int teacherId = teacherDAO.getTeacherByAccountId(account.getId()).getId();
            List<Schedule> schedules = teacherScheduleDAO.getScheduleByTeacherId(teacherId);

            request.setAttribute("schedulesTeacher", schedules);
            request.getRequestDispatcher("View/teacher-schedule.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("Lỗi server:");
            e.printStackTrace(out);
        }
    }
}