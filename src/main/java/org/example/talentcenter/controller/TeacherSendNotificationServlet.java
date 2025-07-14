package org.example.talentcenter.controller;

import org.example.talentcenter.dao.NotificationDAO;
import org.example.talentcenter.model.Notification;
import org.example.talentcenter.model.ClassRooms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

@WebServlet("/sendNotification")
public class TeacherSendNotificationServlet extends HttpServlet {
    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        notificationDAO = new NotificationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            handleDelete(request, response);
        } else if ("edit".equals(action)) {
            handleEdit(request, response);
        } else {
            loadNotifications(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");

        if ("send".equals(action)) {
            handleSendNotification(request, response);
        } else if ("update".equals(action)) {
            handleUpdateNotification(request, response);
        }
    }

    private void handleSendNotification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String senderName = (String) session.getAttribute("fullName");

            // Kiểm tra session
            if (senderName == null) {
                senderName = "Admin"; // Default value
            }

            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String classRoomIdStr = request.getParameter("classRoomId");
            String notificationType = request.getParameter("notificationType");

            // Validation
            if (title == null || title.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Tiêu đề không được để trống!");
                loadNotifications(request, response);
                return;
            }

            if (content == null || content.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Nội dung không được để trống!");
                loadNotifications(request, response);
                return;
            }

            if (classRoomIdStr == null || classRoomIdStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng chọn lớp học!");
                loadNotifications(request, response);
                return;
            }

            Notification notification = new Notification();
            notification.setTitle(title.trim());
            notification.setContent(content.trim());
            notification.setSenderName(senderName);
            notification.setRecipientRole("Student");
            notification.setNotificationType(notificationType != null ? notificationType : "GENERAL");
            notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            notification.setRead(false);

            // Set ClassRoomId
            try {
                notification.setClassRoomId(Integer.parseInt(classRoomIdStr));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID lớp học không hợp lệ!");
                loadNotifications(request, response);
                return;
            }

            boolean success = notificationDAO.createNotificationForClassRoom(notification);

            if (success) {
                request.setAttribute("successMessage", "Gửi thông báo thành công!");
            } else {
                request.setAttribute("errorMessage", "Có lỗi xảy ra khi gửi thông báo!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        loadNotifications(request, response);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "ID thông báo không hợp lệ!");
                loadNotifications(request, response);
                return;
            }

            int notificationId = Integer.parseInt(idStr);
            boolean success = notificationDAO.deleteNotification(notificationId);

            if (success) {
                request.setAttribute("successMessage", "Xóa thông báo thành công!");
            } else {
                request.setAttribute("errorMessage", "Có lỗi xảy ra khi xóa thông báo!");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID thông báo không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        loadNotifications(request, response);
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "ID thông báo không hợp lệ!");
                loadNotifications(request, response);
                return;
            }

            int notificationId = Integer.parseInt(idStr);
            Notification notification = notificationDAO.getNotificationById(notificationId);

            if (notification != null) {
                request.setAttribute("editNotification", notification);
            } else {
                request.setAttribute("errorMessage", "Không tìm thấy thông báo!");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID thông báo không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        loadNotifications(request, response);
    }

    private void handleUpdateNotification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("notificationId");
            if (idStr == null || idStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "ID thông báo không hợp lệ!");
                loadNotifications(request, response);
                return;
            }

            int notificationId = Integer.parseInt(idStr);
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String notificationType = request.getParameter("notificationType");
            String classRoomIdStr = request.getParameter("classRoomId");

            // Validation
            if (title == null || title.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Tiêu đề không được để trống!");
                loadNotifications(request, response);
                return;
            }

            if (content == null || content.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Nội dung không được để trống!");
                loadNotifications(request, response);
                return;
            }

            Notification notification = new Notification();
            notification.setId(notificationId);
            notification.setTitle(title.trim());
            notification.setContent(content.trim());
            notification.setNotificationType(notificationType != null ? notificationType : "GENERAL");

            if (classRoomIdStr != null && !classRoomIdStr.trim().isEmpty()) {
                notification.setClassRoomId(Integer.parseInt(classRoomIdStr));
            }

            boolean success = notificationDAO.updateNotification(notification);

            if (success) {
                request.setAttribute("successMessage", "Cập nhật thông báo thành công!");
            } else {
                request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật thông báo!");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        loadNotifications(request, response);
    }

    private void loadNotifications(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String searchKeyword = request.getParameter("search");
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            String classRoomIdStr = request.getParameter("filterClassRoom");

            Integer classRoomId = null;
            if (classRoomIdStr != null && !classRoomIdStr.trim().isEmpty() && !"all".equals(classRoomIdStr)) {
                try {
                    classRoomId = Integer.parseInt(classRoomIdStr);
                } catch (NumberFormatException e) {
                    // Ignore invalid classRoomId
                }
            }

            // Lấy danh sách thông báo
            ArrayList<Notification> notifications = notificationDAO.getNotificationsForClassRoom(
                    classRoomId, searchKeyword, dateFrom, dateTo);

            // Lấy danh sách classroom cho dropdown
            ArrayList<ClassRooms> classRooms = notificationDAO.getAllClassRooms();

            // Set attributes
            request.setAttribute("notifications", notifications);
            request.setAttribute("classRooms", classRooms);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("dateFrom", dateFrom);
            request.setAttribute("dateTo", dateTo);
            request.setAttribute("filterClassRoom", classRoomIdStr);

            // Forward to JSP
            request.getRequestDispatcher("/View/teacher-send-notification.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/View/teacher-send-notification.jsp").forward(request, response);
        }
    }
}
