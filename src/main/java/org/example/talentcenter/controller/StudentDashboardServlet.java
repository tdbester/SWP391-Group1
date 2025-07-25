/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-06-29
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-06-29  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.controller;

import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "StudentDashboardServlet", value = "/StudentDashboard")
public class StudentDashboardServlet extends HttpServlet {
    public static StudentDAO studentDAO = new StudentDAO();
    public static StudentScheduleDAO studentScheduleDAO = new StudentScheduleDAO();
    private static final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (role == null || !"học sinh".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Student student = studentDAO.getStudentById(account.getId());
        int studentId = student.getId();

        if (student == null) {
            request.setAttribute("errorMessage", "Không tìm thấy hồ sơ học sinh");
            request.getRequestDispatcher("View/error.jsp").forward(request, response);
            return;
        }
        if ("notifications".equals(action)) {
            String keyword = request.getParameter("keyword");
            ArrayList<Notification> allNotifications;
            int page = 1;
            int recordsPerPage = 10;
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null) page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }

            int offset = (page - 1) * recordsPerPage;
            if (keyword != null && !keyword.trim().isEmpty()) {
                allNotifications = notificationDAO.searchNotificationsForStudentWithPaging(keyword.trim(), account.getId(), offset, recordsPerPage);
                request.setAttribute("keyword", keyword);
            } else {
                allNotifications = notificationDAO.getAllNotificationsForStudentWithPaging(account.getId(), offset, recordsPerPage);
            }
            int totalRecords = notificationDAO.getTotalNotificationsCountForStudent(account.getId());
            int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

            request.setAttribute("allNotifications", allNotifications);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("View/student-notification-list.jsp").forward(request, response);
        } else {
            // lấy lịch hojc hôm nay
            LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            List<StudentSchedule> todaySchedules = studentScheduleDAO.getScheduleByStudentIdAndDate(studentId, today);

            // lấy thông báo
            ArrayList<Notification> latestNotifications = notificationDAO.getLatestNotificationsForStudent(account.getId(), 3);
            int unreadCount = notificationDAO.getUnreadCountForStudent(account.getId());

            request.setAttribute("student", student);
            request.setAttribute("todaySchedules", todaySchedules);
            request.setAttribute("latestNotifications", latestNotifications);
            request.setAttribute("unreadCount", unreadCount);
            request.setAttribute("currentDate", new java.util.Date());
            request.getRequestDispatcher("View/student-dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (role == null || !"học sinh".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("deleteNotification".equals(action)) {
            String notificationId = request.getParameter("notificationId");
            if (notificationId != null) {
                notificationDAO.deleteNotification(Integer.parseInt(notificationId));
            }
            response.sendRedirect("StudentDashboard?action=notifications");

        } else if ("markAsRead".equals(action)) {
            String notificationIdParam = request.getParameter("notificationId");
            try {
                int notificationId = Integer.parseInt(notificationIdParam);
                notificationDAO.markAsRead(notificationId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            response.sendRedirect("StudentDashboard?action=notifications");

        } else if ("markAllAsRead".equals(action)) {
            try {
                boolean success = notificationDAO.markAllAsReadForStudent(account.getId());
                if (success) {
                    session.setAttribute("message", "Đã đánh dấu tất cả thông báo đã đọc");
                } else {
                    session.setAttribute("error", "Không thể đánh dấu thông báo");
                }
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Có lỗi xảy ra khi đánh dấu thông báo");
            }
            response.sendRedirect("StudentDashboard?action=notifications");
        }
    }
}
