package org.example.talentcenter.controller;

import org.example.talentcenter.dao.TeacherScheduleDAO;
import org.example.talentcenter.dao.TeacherDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Schedule;
import org.example.talentcenter.dao.AttendanceDAO;
import org.example.talentcenter.config.DBConnect;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TeacherDashboardServlet", value = "/TeacherDashboard")
public class TeacherDashboardServlet extends HttpServlet {
    private TeacherDAO teacherDAO = new TeacherDAO();
    private TeacherScheduleDAO teacherScheduleDAO = new TeacherScheduleDAO();

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

            // Lấy lịch hôm nay
            LocalDate today = LocalDate.now();
            List<Schedule> todaySchedules = teacherScheduleDAO.getScheduleByTeacherIdAndDate(teacherId, today);

            // Set attributes để JSP sử dụng
            request.setAttribute("todaySchedules", todaySchedules);
            request.setAttribute("currentDate", today);

            //Biểu đồ
            Map<String, Double> chartData = AttendanceDAO.getAttendanceRateByDay(teacherId);
            request.setAttribute("chartLabels", new ArrayList<>(chartData.keySet()));
            request.setAttribute("chartValues", new ArrayList<>(chartData.values()));

            // Forward đến teacher-dashboard.jsp
            request.getRequestDispatcher("View/teacher-dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("Lỗi server:");
            e.printStackTrace(out);
        }
    }
}