package org.example.talentcenter.controller;

import org.example.talentcenter.dao.NotificationDAO;
import org.example.talentcenter.dao.TeacherScheduleDAO;
import org.example.talentcenter.dao.TeacherDAO;
import org.example.talentcenter.dao.TeacherRequestDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Notification;
import org.example.talentcenter.model.Schedule;
import org.example.talentcenter.model.Request;
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

@WebServlet(name = "TeacherDashboardServlet", value = "/TeacherDashboard")
public class TeacherDashboardServlet extends HttpServlet {
    private TeacherDAO teacherDAO = new TeacherDAO();
    private TeacherScheduleDAO teacherScheduleDAO = new TeacherScheduleDAO();
    private TeacherRequestDAO teacherRequestDAO = new TeacherRequestDAO();

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
            int accountId = account.getId();

            // Lấy lịch hôm nay
            LocalDate today = LocalDate.now();
            List<Schedule> todaySchedules = teacherScheduleDAO.getScheduleByTeacherIdAndDate(teacherId, today);

            // Lấy danh sách đơn gần đây (5 đơn gần nhất trong 7 ngày)
            ArrayList<Request> recentRequests = teacherRequestDAO.getRecentRequests(accountId, 5);

            // Lấy thông báo gần đây cho giáo viên
            NotificationDAO dao = new NotificationDAO();
            List<Notification> recentNotifications = dao.getRecentNotificationsForTeacher(accountId);

            // Set attributes để JSP sử dụng
            request.setAttribute("todaySchedules", todaySchedules);
            request.setAttribute("currentDate", today);
            request.setAttribute("recentRequests", recentRequests);
            request.setAttribute("recentNotifications", recentNotifications);

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