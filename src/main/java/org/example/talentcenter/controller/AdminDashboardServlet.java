package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.talentcenter.dao.AdminDAO;
import org.example.talentcenter.dao.NotificationDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Notification;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Admin Dashboard Servlet
 * Handles admin dashboard functionality including system statistics and notifications
 */
@WebServlet("/AdminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private final AdminDAO adminDAO = new AdminDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("account");
        String userRole = (String) session.getAttribute("userRole");

        // Session validation
        if (session == null || account == null) {
            response.sendRedirect("login");
            return;
        }

        // Admin role validation
        if (userRole == null || !userRole.equalsIgnoreCase("admin")) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("notifications".equals(action)) {
                handleNotificationsView(request, response);
            } else if ("markAllRead".equals(action)) {
                handleMarkAllRead(request, response);
            } else {
                handleDashboardView(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleError(request, response, e);
        }
    }

    /**
     * Handle main dashboard view with system statistics
     */
    private void handleDashboardView(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get system statistics
            int[] stats = adminDAO.getSystemStatistics();
            int totalTeachers = stats[0];
            int totalAccounts = stats[1];
            int totalCourses = stats[2];
            int totalStudents = stats[3];
            int totalClassrooms = stats[4];

            // Get admin notifications
            ArrayList<Notification> latestNotifications = adminDAO.getLatestAdminNotifications(5);
            int unreadCount = adminDAO.getUnreadAdminNotificationCount();

            // Set request attributes for JSP
            request.setAttribute("totalTeachers", totalTeachers);
            request.setAttribute("totalAccounts", totalAccounts);
            request.setAttribute("totalCourses", totalCourses);
            request.setAttribute("totalStudents", totalStudents);
            request.setAttribute("totalClassrooms", totalClassrooms);
            request.setAttribute("latestNotifications", latestNotifications);
            request.setAttribute("unreadCount", unreadCount);

            // Forward to admin dashboard JSP
            request.getRequestDispatcher("/View/admin-dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error loading admin dashboard", e);
        }
    }

    /**
     * Handle notifications view (show all notifications)
     */
    private void handleNotificationsView(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String keyword = request.getParameter("keyword");
            ArrayList<Notification> allNotifications;

            if (keyword != null && !keyword.trim().isEmpty()) {
                // Search functionality can be implemented later
                allNotifications = adminDAO.getLatestAdminNotifications(50);
                request.setAttribute("keyword", keyword);
            } else {
                allNotifications = adminDAO.getLatestAdminNotifications(50);
            }

            request.setAttribute("allNotifications", allNotifications);
            
            // Forward to admin notifications list JSP (to be created later if needed)
            // For now, redirect back to dashboard
            response.sendRedirect("AdminDashboard");
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error loading admin notifications", e);
        }
    }

    /**
     * Handle mark all notifications as read
     */
    private void handleMarkAllRead(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Mark all admin notifications as read
            // This functionality can be implemented when notification system is enhanced
            
            // Redirect back to dashboard
            response.sendRedirect("AdminDashboard");
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error marking notifications as read", e);
        }
    }

    /**
     * Handle errors by displaying error information
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e) 
            throws IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Admin Dashboard Error</title></head>");
        out.println("<body>");
        out.println("<h1>Lỗi Admin Dashboard</h1>");
        out.println("<p>Đã xảy ra lỗi khi tải trang quản trị:</p>");
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre>");
        out.println("<a href='AdminDashboard'>Quay lại Dashboard</a>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("markAsRead".equals(action)) {
            // Handle individual notification mark as read
            String notificationId = request.getParameter("notificationId");
            if (notificationId != null) {
                try {
                    // This can be implemented when notification system supports admin notifications
                    // notificationDAO.markAsRead(Integer.parseInt(notificationId));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            response.sendRedirect("AdminDashboard");
            
        } else if ("deleteNotification".equals(action)) {
            // Handle notification deletion
            String notificationId = request.getParameter("notificationId");
            if (notificationId != null) {
                try {
                    // This can be implemented when notification system supports admin notifications
                    // notificationDAO.deleteNotification(Integer.parseInt(notificationId));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            response.sendRedirect("AdminDashboard");
            
        } else {
            // Default: redirect to dashboard
            response.sendRedirect("AdminDashboard");
        }
    }
}
