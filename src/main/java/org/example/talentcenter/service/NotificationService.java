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

    /**
     * Gửi thông báo đến Sale khi có khách hàng đăng ký tư vấn khóa học mới.
     *
     * @param customerName   Tên khách hàng đăng ký tư vấn.
     * @param customerEmail  Email của khách hàng.
     * @param customerPhone  Số điện thoại của khách hàng.
     * @param courseName     Tên khóa học được tư vấn.
     * @param consultationId ID của bản ghi tư vấn liên quan.
     *                       <p>
     *                       Thông báo sẽ chứa nội dung mô tả khách hàng và khóa học, được gửi từ "SYSTEM" đến vai trò "Sale".
     *                       Notification được đánh dấu là chưa đọc và lưu thời gian tạo hiện tại.
     * @author Huyen Trang
     */
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

    /**
     * Gửi thông báo cho học sinh về trạng thái xử lý đơn
     *
     * @param requestId        ID của đơn liên quan.
     * @param studentName      Tên học sinh nhận thông báo.
     * @param requestType      Loại đơn
     * @param status           Trạng thái đơn
     * @param response         Phản hồi hoặc lý do từ người xử lý đơn.
     * @param studentAccountId ID tài khoản học sinh nhận thông báo.
     *                         <p>
     *                         Thông báo sẽ có tiêu đề và nội dung thay đổi tùy theo trạng thái đơn,
     *                         được gửi từ "SYSTEM" đến vai trò "Student" và đánh dấu chưa đọc.
     * @author Huyen Trang
     */
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

    /**
     * Gửi thông báo cho Training Manager khi học viên gửi đơn mới cần xử lý.
     *
     * @param studentName      Tên học viên gửi đơn.
     * @param requestType      Loại đơn mà học viên gửi
     * @param requestId        ID của đơn vừa được gửi.
     * @param studentAccountId ID tài khoản học viên gửi đơn.
     *                         <p>
     *                         Thông báo có tiêu đề và nội dung mô tả học viên cùng loại đơn,
     *                         gửi từ tên học viên đến vai trò "TrainingManager",
     *                         đánh dấu chưa đọc
     * @author Huyen Trang
     */
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

    /**
     * Gửi thông báo cho Training Manager khi có yêu cầu tạo tài khoản học viên mới từ Sale.
     *
     * @param saleName     Tên nhân viên Sale gửi yêu cầu.
     * @param studentName  Tên học viên cần tạo tài khoản.
     * @param studentEmail Email học viên cần tạo tài khoản.
     * @param requestId    ID của yêu cầu tạo tài khoản.
     *                     <p>
     *                     Thông báo có tiêu đề, nội dung chi tiết về Sale và học viên,
     *                     gửi từ Sale đến vai trò "TrainingManager",
     *                     đánh dấu chưa đọc
     * @author Huyen Trang
     */
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

    /**
     * Gửi thông báo nghỉ học tới học viên về buổi học mà giáo viên xin nghỉ.
     *
     * @param studentAccountId ID tài khoản học viên nhận thông báo.
     * @param teacherName      Tên giáo viên xin nghỉ.
     * @param className        Tên lớp học.
     * @param offDate          Ngày nghỉ
     * @param slotStartTime    Thời gian bắt đầu ca học
     * @param slotEndTime      Thời gian kết thúc ca học
     * @param scheduleId       ID lịch học liên quan.
     *                         <p>
     *                         Thông báo có tiêu đề, nội dung chi tiết ngày, ca học, lớp và giáo viên,
     *                         gửi từ hệ thống đến vai trò "Student",
     *                         đánh dấu chưa đọc và liên kết đến lịch học tương ứng.
     * @author Huyen Trang
     */
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

    /**
     * Gửi thông báo thay đổi lịch học đến học viên.
     *
     * @param studentAccountId ID tài khoản học viên nhận thông báo.
     * @param teacherName      Tên giáo viên
     * @param className        Tên lớp học bị thay đổi lịch.
     * @param newDate          Ngày học mới.
     * @param slotStartTime    Thời gian bắt đầu ca học mới
     * @param slotEndTime      Thời gian kết thúc ca học mới
     * @param scheduleId       ID lịch học liên quan.
     *                         <p>
     *                         Thông báo bao gồm tiêu đề và nội dung mô tả lớp học, ngày và ca học mới,
     *                         gửi từ SYSTEM tới vai trò "Student", chưa đọc,
     *                         liên kết với lịch học tương ứng.
     * @author Huyen Trang
     */
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

    /**
     * Gửi thông báo khi giáo viên gửi đơn mới cần xử lý.
     *
     * @param teacherName Tên giáo viên gửi đơn.
     * @param requestType Loại đơn giáo viên gửi.
     * @param requestId   ID của đơn gửi.
     *                    <p>
     *                    Thông báo có tiêu đề, nội dung nêu rõ giáo viên và loại đơn,
     *                    gửi từ giáo viên đến vai trò TrainingManager,
     *                    liên kết với đơn tương ứng,
     *                    đánh dấu chưa đọc.
     * @author Huyen Trang
     */
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

    /**
     * Gửi thông báo học phí cho student sau khi được cấp tài khoản thành công.
     *
     * @param studentAccountId ID tài khoản sinh viên (dùng để gửi notification).
     * @param studentName      Tên sinh viên.
     * @param tuitionAmount    Số tiền học phí (chuỗi để tuỳ biến thông báo).
     */
    public static void notifyStudentTuitionFee(int studentAccountId, String studentName, String tuitionAmount) {
        Notification notification = new Notification();
        notification.setTitle("Thông báo học phí");
        notification.setContent(String.format(
                "Chào %s, học phí của bạn là %s. Vui lòng chuyển khoản vào số tài khoản 1234567890 MB Bank và gửi Bill qua email TalenCenter@gmail.com.",
                studentName, tuitionAmount
        ));
        notification.setSenderName("SYSTEM");
        notification.setRecipientRole("Student");
        notification.setRecipientAccountId(studentAccountId);
        notification.setNotificationType("TUITION_NOTICE");
        notification.setRelatedEntityId(null);
        notification.setRelatedEntityType("Account");
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notification.setRead(false);

        notificationDAO.createNotification(notification);
        System.out.println("Tuition notification sent to student " + studentName + ", id=" + studentAccountId);
    }

}
