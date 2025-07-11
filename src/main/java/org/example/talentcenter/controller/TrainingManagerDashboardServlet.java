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
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("notifications".equals(action)) {
            String keyword = request.getParameter("keyword");
            ArrayList<Notification> allNotifications;

            if (keyword != null && !keyword.trim().isEmpty()) {
                allNotifications = notificationDAO.searchNotificationsForTrainingManager(keyword.trim());
                request.setAttribute("keyword", keyword);
            } else {
                allNotifications = notificationDAO.getLatestNotificationsForTrainingManager(50);
            }

            request.setAttribute("allNotifications", allNotifications);
            request.getRequestDispatcher("/View/training-manager-notification-list.jsp").forward(request, response);
        } else if ("markAllRead".equals(action)) {
            // Đánh dấu tất cả đã đọc
            notificationDAO.markAllAsReadForTrainingManager();
            response.sendRedirect("TrainingManagerDashboard");
        } else {
            // Dashboard chính
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

            request.getRequestDispatcher("/View/training-manager-dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("deleteNotification".equals(action)) {
            // ✅ THÊM XỬ LÝ XÓA
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
        }
    }
}
