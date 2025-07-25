/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-07-10
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-07-10  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Notification;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/TrainingManagerDashboard")
public class TrainingManagerDashboardServlet extends HttpServlet {
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        if (role == null || !"training manager".equalsIgnoreCase(role)) {
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
                allNotifications = notificationDAO.searchNotificationsForTrainingManagerWithPaging(keyword.trim(), offset, recordsPerPage);
                request.setAttribute("keyword", keyword);
            } else {
                allNotifications = notificationDAO.getAllNotificationsForTrainingManagerWithPaging(offset, recordsPerPage);
            }
            int totalRecords = notificationDAO.getTotalNotificationsCountForTrainingManager();
            int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

            request.setAttribute("allNotifications", allNotifications);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/View/training-manager-notification-list.jsp").forward(request, response);
        } else if ("markAllRead".equals(action)) {
            // Đánh dấu tất cả đã đọc
            notificationDAO.markAllAsReadForTrainingManager();
            response.sendRedirect("TrainingManagerDashboard");
        } else {
            ArrayList<Notification> latestNotifications = notificationDAO.getLatestNotificationsForTrainingManager(5);
            int unreadCount = notificationDAO.getUnreadCountForTrainingManager();
            int processedRequestsThisWeek = requestDAO.getProcessedRequestsThisWeek();
            int pendingRequests = requestDAO.getPendingRequests();
            int studentsWithoutAccount = requestDAO.getStudentsWithoutAccount();

            request.setAttribute("latestNotifications", latestNotifications);
            request.setAttribute("unreadCount", unreadCount);
            request.setAttribute("processedRequestsThisWeek", processedRequestsThisWeek);
            request.setAttribute("pendingRequests", pendingRequests);
            request.setAttribute("studentsWithoutAccount", studentsWithoutAccount);
            request.setAttribute("currentDate", new java.util.Date());

            request.getRequestDispatcher("/View/training-manager-dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        if (role == null || !"training manager".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if ("deleteNotification".equals(action)) {
            String notificationId = request.getParameter("notificationId");
            if (notificationId != null) {
                notificationDAO.deleteNotification(Integer.parseInt(notificationId));
            }
            response.sendRedirect("TrainingManagerDashboard?action=notifications");

        } else if ("markAsRead".equals(action)) {
            String notificationId = request.getParameter("notificationId");
            if (notificationId != null) {
                notificationDAO.markAsRead(Integer.parseInt(notificationId));
            }
            response.sendRedirect("TrainingManagerDashboard?action=notifications");
        } else if ("markAllAsRead".equals(action)) {
            try {
                boolean success = notificationDAO.markAllAsReadForTrainingManager();
                if (success) {
                    session.setAttribute("message", "Đã đánh dấu tất cả thông báo đã đọc");
                } else {
                    session.setAttribute("error", "Không thể đánh dấu thông báo");
                }
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Có lỗi xảy ra khi đánh dấu thông báo");
            }
            response.sendRedirect("TrainingManagerDashboard?action=notifications");
        }
    }
}
