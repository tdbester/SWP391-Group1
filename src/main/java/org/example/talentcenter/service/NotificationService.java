package org.example.talentcenter.service;

import org.example.talentcenter.dao.NotificationDAO;
import org.example.talentcenter.model.Notification;

import java.sql.Timestamp;

public class NotificationService {
    private static final NotificationDAO notificationDAO = new NotificationDAO();

    public static void notifyNewConsultation(String customerName, String customerEmail, String customerPhone, String courseName, int consultationId) {
        Notification notification = new Notification();
        notification.setTitle("Đăng ký tư vấn mới");
        notification.setContent(String.format("Khách hàng %s (%s - %s) đã đăng ký tư vấn khóa học %s",
                customerName, customerEmail, customerPhone, courseName));
        notification.setSenderName("SYSTEM");
        notification.setRecipientRole("Sale");
        notification.setNotificationType("CONSULTATION_REQUEST");
        notification.setRelatedEntityId(consultationId);
        notification.setRelatedEntityType("Consultation");
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notification.setRead(false);
        notificationDAO.createNotification(notification);
        System.out.println("Notification created for new consultation: " + customerName);
    }

    public static void notifyRequestProcessed(int requestId, String studentName, String requestType, String status, String response, int studentAccountId) {
        Notification notification = new Notification();

        if ("Đã duyệt".equals(status)) {
            notification.setTitle("Đơn của bạn đã được duyệt");
            notification.setContent(String.format("Đơn %s của bạn đã được duyệt. %s",
                    requestType, response != null ? "Phản hồi: " + response : ""));
        } else if ("Từ chối".equals(status)) {
            notification.setTitle("Đơn của bạn bị từ chối");
            notification.setContent(String.format("Đơn %s của bạn đã bị từ chối. %s",
                    requestType, response != null ? "Lý do: " + response : ""));
        } else {
            notification.setTitle("Cập nhật trạng thái đơn");
            notification.setContent(String.format("Đơn %s của bạn đã được cập nhật trạng thái: %s",
                    requestType, status));
        }

        notification.setSenderName("SYSTEM");
        notification.setRecipientRole("Student");
        notification.setRecipientAccountId(studentAccountId);
        notification.setNotificationType("REQUEST_UPDATE");
        notification.setRelatedEntityId(requestId);
        notification.setRelatedEntityType("Request");
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notification.setRead(false);

        notificationDAO.createNotification(notification);
        System.out.println("Notification sent to student " + studentName + " about request " + requestId);
    }

}
