package org.example.talentcenter.controller;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

public class ResetService {
    private final int LIMIT_MINUTES = 15; // Increased to 15 minutes for better UX
    private final String from = "talentcenter0001@gmail.com";
    private final String password = "ehmf tner ayhv flzm"; // App password for Gmail

    /**
     * Generate a unique token for password reset
     * @return UUID string as token
     */
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Calculate expiration time for the token
     * @return LocalDateTime representing when token expires
     */
    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(LIMIT_MINUTES);
    }

    /**
     * Check if the given time has expired
     * @param time LocalDateTime to check
     * @return true if expired, false otherwise
     */
    public boolean isExpireTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

    /**
     * Send password reset email to user
     * @param to recipient email address
     * @param link reset password link
     * @param name recipient's full name
     * @return true if email sent successfully, false otherwise
     */
    public boolean sendEmail(String to, String link, String name) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);

            // Set email headers
            msg.addHeader("Content-type", "text/html; charset=UTF-8");
            msg.setFrom(new InternetAddress(from, "Talent Center System"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject("Đặt lại mật khẩu - Talent Center", "UTF-8");

            // Create email content
            String content = buildEmailContent(name, link);
            msg.setContent(content, "text/html; charset=UTF-8");

            // Send email
            Transport.send(msg);
            System.out.println("Password reset email sent successfully to: " + to);
            return true;

        } catch (Exception e) {
            System.out.println("Failed to send password reset email to: " + to);
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Build HTML email content for password reset
     * @param name recipient's name
     * @param link reset password link
     * @return formatted HTML email content
     */
    private String buildEmailContent(String name, String link) {
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html lang='vi'>");
        content.append("<head>");
        content.append("<meta charset='UTF-8'>");
        content.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        content.append("<title>Đặt lại mật khẩu</title>");
        content.append("<style>");
        content.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        content.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        content.append(".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }");
        content.append(".content { padding: 20px; background-color: #f9f9f9; }");
        content.append(".button { display: inline-block; background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; margin: 20px 0; }");
        content.append(".footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }");
        content.append(".warning { background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 10px; border-radius: 5px; margin: 15px 0; }");
        content.append("</style>");
        content.append("</head>");
        content.append("<body>");
        content.append("<div class='container'>");
        content.append("<div class='header'>");
        content.append("<h1>Talent Center</h1>");
        content.append("<h2>Đặt lại mật khẩu</h2>");
        content.append("</div>");
        content.append("<div class='content'>");
        content.append("<h3>Xin chào ").append(name != null ? name : "").append(",</h3>");
        content.append("<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn tại Talent Center.</p>");
        content.append("<p>Vui lòng nhấp vào nút bên dưới để đặt lại mật khẩu:</p>");
        content.append("<div style='text-align: center;'>");
        content.append("<a href='").append(link).append("' class='button'>Đặt lại mật khẩu</a>");
        content.append("</div>");
        content.append("<div class='warning'>");
        content.append("<strong>Lưu ý quan trọng:</strong>");
        content.append("<ul>");
        content.append("<li>Link này sẽ hết hạn sau ").append(LIMIT_MINUTES).append(" phút</li>");
        content.append("<li>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này</li>");
        content.append("<li>Không chia sẻ link này với bất kỳ ai khác</li>");
        content.append("</ul>");
        content.append("</div>");
        content.append("<p>Nếu nút không hoạt động, vui lòng sao chép và dán link sau vào trình duyệt:</p>");
        content.append("<p style='word-break: break-all; background-color: #f5f5f5; padding: 10px; border-radius: 3px;'>");
        content.append(link);
        content.append("</p>");
        content.append("</div>");
        content.append("<div class='footer'>");
        content.append("<p>Email này được gửi tự động từ hệ thống Talent Center.</p>");
        content.append("<p>Vui lòng không trả lời email này.</p>");
        content.append("</div>");
        content.append("</div>");
        content.append("</body>");
        content.append("</html>");

        return content.toString();
    }

    /**
     * Get token expiration time in minutes
     * @return expiration time in minutes
     */
    public int getExpirationMinutes() {
        return LIMIT_MINUTES;
    }
}