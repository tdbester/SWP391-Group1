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

        notificationDAO.createNotification(notification);
        System.out.println("Notification created for new consultation: " + customerName);
    }
}
