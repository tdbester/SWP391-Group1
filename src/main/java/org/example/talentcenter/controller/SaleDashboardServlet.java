/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-07-08
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-07-08  | Cù Thị Huyền Trang   | Initial creation - Dashboard & Notifications
 */

package org.example.talentcenter.controller;

import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.io.IOException;

@WebServlet(name = "SaleDashboardServlet", value = "/SaleDashboard")
public class SaleDashboardServlet extends HttpServlet {
    private static final NotificationDAO notificationDAO = new NotificationDAO();
    private static final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
        if (role == null || !"nhân viên sale".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String action = request.getParameter("action");

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
                allNotifications = notificationDAO.searchNotificationsForSaleWithPaging(keyword.trim(), offset, recordsPerPage);
                request.setAttribute("keyword", keyword);
            } else {
                allNotifications = notificationDAO.getAllNotificationsForSaleWithPaging(offset, recordsPerPage);
            }
            int totalRecords = notificationDAO.getTotalNotificationsCountForSale();
            int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

            request.setAttribute("allNotifications", allNotifications);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("View/sale-notification-list.jsp").forward(request, response);
        } else if ("courseList".equals(action)) {
            ArrayList<Course> allCourses = courseDAO.getAllCoursesForSale();
            if (allCourses == null) {
                response.sendRedirect("SaleDashboard");
                return;
            }
            request.setAttribute("allCourses", allCourses);
            request.getRequestDispatcher("View/sale-course-list.jsp").forward(request, response);
        } else if ("course".equals(action)) {
            int courseId = request.getParameter("courseId") == null ? 0 : Integer.parseInt(request.getParameter("courseId"));
            Course course = courseDAO.getCourseById(courseId);
            if (course == null) {
                response.sendRedirect("SaleDashboard");
                return;
            }

            ClassroomDAO classroomDAO = new ClassroomDAO();
            ArrayList<Classroom> classes = classroomDAO.getClassesByCourseId(courseId);

            for (Classroom classroom : classes) {
                ArrayList<StudentSchedule> schedules = classroomDAO.getClassSchedule(classroom.getClassroomID());
                request.setAttribute("schedules_" + classroom.getClassroomID(), schedules);
            }
            request.setAttribute("course", course);
            request.setAttribute("classes", classes);
            request.getRequestDispatcher("View/sale-course-detail.jsp").forward(request, response);
        } else {
            ArrayList<Notification> latestNotifications = notificationDAO.getLatestNotificationsForSale(3);
            int unreadCount = notificationDAO.getUnreadCountForSale();
            ArrayList<Course> latestCourse = courseDAO.getLatestCourses(2);
            ArrayList<Course> allCourse = (ArrayList<Course>) courseDAO.getAllCourses();
            request.setAttribute("latestCoursesWithClass", latestCourse);
            request.setAttribute("latestNotifications", latestNotifications);
            request.setAttribute("unreadCount", unreadCount);
            request.setAttribute("allCourse", allCourse);
            request.setAttribute("currentDate", new java.util.Date());
            request.getRequestDispatcher("View/sale-dashboard.jsp").forward(request, response);
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
        if (role == null || !"nhân viên sale".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if ("deleteNotification".equals(action)) {
            String notificationId = request.getParameter("notificationId");
            if (notificationId != null) {
                notificationDAO.deleteNotification(Integer.parseInt(notificationId));
            }
            response.sendRedirect("SaleDashboard?action=notifications");


        } else if ("markAsRead".equals(action)) {
            String notificationIdParam = request.getParameter("notificationId");
            try {
                int notificationId = Integer.parseInt(notificationIdParam);
                notificationDAO.markAsRead(notificationId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            response.sendRedirect("SaleDashboard?action=notifications");

        } else if ("markAllAsRead".equals(action)) {
            try {
                boolean success = notificationDAO.markAllAsReadForSale();
                if (success) {
                    session.setAttribute("message", "Đã đánh dấu tất cả thông báo đã đọc");
                } else {
                    session.setAttribute("error", "Không thể đánh dấu thông báo");
                }
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Có lỗi xảy ra khi đánh dấu thông báo");
            }
            response.sendRedirect("SaleDashboard?action=notifications");
        }
    }
}
