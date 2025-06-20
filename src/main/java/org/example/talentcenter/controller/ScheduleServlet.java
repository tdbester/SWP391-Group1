package org.example.talentcenter.controller;

import org.example.talentcenter.model.Schedule;
import org.example.talentcenter.model.Teacher;
import org.example.talentcenter.dao.ScheduleDAO;
import org.example.talentcenter.dao.TeacherDAO;
import org.example.talentcenter.config.DBConnect;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/ScheduleServlet")
public class ScheduleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mở kết nối DB
        try (Connection conn = DBConnect.getConnection()) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("accountId") == null) {
                response.sendRedirect(request.getContextPath()+"/View/login.jsp");
                return;
            }

            int accountId = (int) session.getAttribute("accountId");
            System.out.println("AccountId = " + accountId);

            // Lấy TeacherId từ AccountId
            TeacherDAO teacherDAO = new TeacherDAO(conn);
            int teacherId = teacherDAO.getTeacherIdByAccountId(accountId);
            System.out.println("TeacherId = " + teacherId);

            if (teacherId == -1) {
                request.setAttribute("error", "Không tìm thấy giáo viên với tài khoản này.");
                request.getRequestDispatcher(request.getContextPath()+"/View/schedule.jsp").forward(request, response);
                return;
            }

            // Lấy lịch giảng dạy
            ScheduleDAO scheduleDAO = new ScheduleDAO(conn);
            List<Schedule> scheduleList = scheduleDAO.getScheduleByTeacherId(teacherId);
            System.out.println("Schedule list size = " + scheduleList.size());

            // Gửi dữ liệu sang JSP
            request.setAttribute("scheduleList", scheduleList);
            request.getRequestDispatcher(request.getContextPath()+"/View/schedule.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher(request.getContextPath()+"/View/error.jsp").forward(request, response);
        }
    }
}
