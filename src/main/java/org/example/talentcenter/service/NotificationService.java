package org.example.talentcenter.service;

import jakarta.ejb.Local;
import org.example.talentcenter.dao.NotificationDAO;
import org.example.talentcenter.model.Notification;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
            notification.setContent(String.format("%s của bạn đã được duyệt. %s",
                    requestType, response != null ? "Phản hồi: " + response : ""));
        } else if ("Từ chối".equals(status)) {
            notification.setTitle("Đơn của bạn bị từ chối");
            notification.setContent(String.format("%s của bạn đã bị từ chối. %s",
                    requestType, response != null ? "Lý do: " + response : ""));
        } else {
            notification.setTitle("Cập nhật trạng thái đơn");
            notification.setContent(String.format("%s của bạn đã được cập nhật trạng thái: %s",
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

    public static void notifyStudentRequestSubmitted(String studentName, String requestType, int requestId, int studentAccountId) {
        Notification notification = new Notification();
        notification.setTitle("Đơn học viên mới cần xử lý");
        notification.setContent(String.format("Học viên %s đã gửi %s cần được xem xét và phê duyệt",
                studentName, requestType));
        notification.setSenderName(studentName);
        notification.setRecipientRole("TrainingManager");
        notification.setNotificationType("STUDENT_REQUEST");
        notification.setRelatedEntityId(requestId);
        notification.setRelatedEntityType("Request");
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notification.setRead(false);

        notificationDAO.createNotification(notification);
        System.out.println("Notification sent to Training Manager for student request: " + requestId);
    }

    public static void notifyAccountCreationRequest(String saleName, String studentName, String studentEmail, int requestId) {
        Notification notification = new Notification();
        notification.setTitle("Yêu cầu tạo tài khoản học viên");
        notification.setContent(String.format("Sale %s đã gửi yêu cầu tạo tài khoản cho học viên %s (%s)",
                saleName, studentName, studentEmail));
        notification.setSenderName(saleName);
        notification.setRecipientRole("TrainingManager");
        notification.setNotificationType("ACCOUNT_CREATION_REQUEST");
        notification.setRelatedEntityId(requestId);
        notification.setRelatedEntityType("Request");
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notification.setRead(false);

        notificationDAO.createNotification(notification);
        System.out.println("Notification sent to Training Manager for account creation request: " + requestId);
    }
    public static void notifyStudentAbsence(
            int studentAccountId,
            String teacherName,
            String className,
            LocalDate offDate,
            LocalTime slotStartTime,
            LocalTime slotEndTime,
            int scheduleId
    ) {
        Notification notification = new Notification();
        notification.setTitle("Thông báo nghỉ học");
        notification.setContent(String.format(
                "Buổi học lớp %s vào ngày %s, ca %s-%s sẽ nghỉ do giáo viên %s xin nghỉ phép.",
                className,
                offDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                slotStartTime,
                slotEndTime,
                teacherName
        ));
        notification.setSenderName("SYSTEM");
        notification.setRecipientRole("Student");
        notification.setRecipientAccountId(studentAccountId);
        notification.setNotificationType("ABSENCE_NOTICE");
        notification.setRelatedEntityId(scheduleId);
        notification.setRelatedEntityType("Schedule");
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notification.setRead(false);

        notificationDAO.createNotification(notification);
        System.out.println("Sent absence notification to student " + studentAccountId);
    }
    public static void notifyStudentScheduleChanged(
            int studentAccountId,
            String teacherName,
            String className,
            LocalDate newDate,
            java.time.LocalTime slotStartTime,
            java.time.LocalTime slotEndTime,
            int scheduleId
    ) {
        Notification notification = new Notification();
        notification.setTitle("Thông báo thay đổi lịch học");
        notification.setContent(String.format(
                "Lớp %s đã được đổi sang ngày %s, ca %s-%s",
                className,
                newDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                slotStartTime != null ? slotStartTime.toString() : "",
                slotEndTime != null ? slotEndTime.toString() : ""
        ));
        notification.setSenderName("SYSTEM");
        notification.setRecipientRole("Student");
        notification.setRecipientAccountId(studentAccountId);
        notification.setNotificationType("SCHEDULE_CHANGE_NOTICE");
        notification.setRelatedEntityId(scheduleId);
        notification.setRelatedEntityType("Schedule");
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notification.setRead(false);

        boolean ok = notificationDAO.createNotification(notification);
        System.out.println("Sent schedule change notification to student " + studentAccountId + ": " + ok);
    }

    public static void notifyTeacherRequestSubmitted(
            String teacherName,
            String requestType,
            int requestId
    ) {
        Notification notification = new Notification();
        notification.setTitle("Đơn giáo viên mới cần xử lý");
        notification.setContent(String.format("Giáo viên %s đã gửi %s cần được xem xét và phê duyệt", teacherName, requestType));
        notification.setSenderName(teacherName);
        notification.setRecipientRole("TrainingManager");
        notification.setNotificationType("TEACHER_REQUEST");
        notification.setRelatedEntityId(requestId);
        notification.setRelatedEntityType("Request");
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notification.setRead(false);

        notificationDAO.createNotification(notification);
        System.out.println("Notification sent to Training Manager for teacher request: " + requestId);
    }

}
