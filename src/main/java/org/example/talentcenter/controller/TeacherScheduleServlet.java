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
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
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

            // Lấy parameters từ request
            String weekParam = request.getParameter("week");

            List<Schedule> schedules;
            LocalDate currentDate = LocalDate.now();

            // Xử lý filter theo tuần
            if (weekParam != null && !weekParam.isEmpty()) {
                try {
                    LocalDate selectedDate = LocalDate.parse(weekParam);
                    LocalDate weekStart = selectedDate.with(DayOfWeek.MONDAY);
                    schedules = teacherScheduleDAO.getScheduleByTeacherIdAndWeek(teacherId, weekStart);
                    request.setAttribute("selectedWeek", weekStart);
                } catch (DateTimeParseException e) {
                    schedules = getDefaultWeekSchedule(teacherId, currentDate);
                }
            } else {
                // Mặc định hiển thị tuần hiện tại
                schedules = getDefaultWeekSchedule(teacherId, currentDate);
            }

            // Set các giá trị mặc định để hiển thị trên form
            setDefaultFormValues(request, currentDate);

            request.setAttribute("schedulesTeacher", schedules);
            request.getRequestDispatcher("View/teacher-schedule.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("Lỗi server:");
            e.printStackTrace(out);
        }
    }

    private List<Schedule> getDefaultWeekSchedule(int teacherId, LocalDate currentDate) {
        LocalDate weekStart = currentDate.with(DayOfWeek.MONDAY);
        return teacherScheduleDAO.getScheduleByTeacherIdAndWeek(teacherId, weekStart);
    }

    private void setDefaultFormValues(HttpServletRequest request, LocalDate currentDate) {
        // Set giá trị mặc định cho input week
        LocalDate weekStart = currentDate.with(DayOfWeek.MONDAY);
        request.setAttribute("defaultWeek", weekStart.toString());
        request.setAttribute("currentDate", currentDate);
    }
}