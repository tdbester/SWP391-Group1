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
    private static final ClassroomDAO classroomDAO = new ClassroomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("notifications".equals(action)) {
            String keyword = request.getParameter("keyword");
            ArrayList<Notification> allNotifications;

            if (keyword != null && !keyword.trim().isEmpty()) {
                allNotifications = notificationDAO.searchNotificationsForSale(keyword.trim());
                request.setAttribute("keyword", keyword);
            } else {
                allNotifications = notificationDAO.getLatestNotificationsForSale(50);
            }

            request.setAttribute("allNotifications", allNotifications);
            request.getRequestDispatcher("View/sale-notification-list.jsp").forward(request, response); } else if ("course".equals(action)) {
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
            request.setAttribute("latestCoursesWithClass", latestCourse);
            request.setAttribute("latestNotifications", latestNotifications);
            request.setAttribute("unreadCount", unreadCount);
            request.getRequestDispatcher("View/sale-dashboard.jsp").forward(request, response);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("deleteNotification".equals(action)) {
            String notificationId = request.getParameter("notificationId");
            if (notificationId != null) {
                notificationDAO.deleteNotification(Integer.parseInt(notificationId));
            }
            response.sendRedirect("SaleDashboard?action=notifications");


        } else if ("markReadId".equals(action)) {
            String notificationId = request.getParameter("markReadId");
            if (notificationId != null) {
                notificationDAO.markAsRead(Integer.parseInt(notificationId));
            }
            response.sendRedirect("SaleDashboard?action=notifications");
        }
    }
}
