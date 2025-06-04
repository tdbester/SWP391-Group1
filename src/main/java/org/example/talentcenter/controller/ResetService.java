package org.example.talentcenter.controller;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

public class ResetService {
    private final int LIMIT_MINUTES = 15;
    private final String from = "talentcenter0001@gmail.com";
    private final String password = "ehmf tner ayhv flzm"; // App password for Gmail

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(LIMIT_MINUTES);
    }

    public boolean isExpireTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

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

            msg.addHeader("Content-type", "text/html; charset=UTF-8");
            msg.setFrom(new InternetAddress(from, "Talent Center System"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject("Đặt lại mật khẩu - Talent Center", "UTF-8");

            String content = buildEmailContent(name, link);
            msg.setContent(content, "text/html; charset=UTF-8");

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


    private String buildEmailContent(String name, String link) {
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html lang='vi'>");
        content.append("<head>");
        content.append("<meta charset='UTF-8'>");
        content.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        content.append("<title>Đặt lại mật khẩu</title>");
        content.append("<style>");
        content.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #2d1b44; background: linear-gradient(135deg, #f5f3ff 0%, #ede9fe 100%); margin: 0; padding: 20px; }");
        content.append(".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; box-shadow: 0 8px 32px rgba(139, 92, 246, 0.15); overflow: hidden; }");
        content.append(".header { background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 50%, #6d28d9 100%); color: white; padding: 30px 20px; text-align: center; position: relative; }");
        content.append(".header::before { content: ''; position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: url('data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 100 100\"><circle cx=\"20\" cy=\"20\" r=\"2\" fill=\"rgba(255,255,255,0.1)\"/><circle cx=\"80\" cy=\"40\" r=\"3\" fill=\"rgba(255,255,255,0.08)\"/><circle cx=\"40\" cy=\"80\" r=\"2\" fill=\"rgba(255,255,255,0.12)\"/></svg>'); }");
        content.append(".header h1 { margin: 0 0 10px 0; font-size: 28px; font-weight: 700; position: relative; z-index: 1; }");
        content.append(".header h2 { margin: 0; font-size: 18px; font-weight: 400; opacity: 0.9; position: relative; z-index: 1; }");
        content.append(".content { padding: 30px; background-color: #fefefe; }");
        content.append(".content h3 { color: #6d28d9; margin-top: 0; font-size: 20px; font-weight: 600; }");
        content.append(".content p { color: #4b5563; margin: 16px 0; }");
        content.append(".button { display: inline-block; background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%); color: white; padding: 14px 28px; text-decoration: none; border-radius: 8px; margin: 25px 0; font-weight: 600; transition: all 0.3s ease; box-shadow: 0 4px 15px rgba(139, 92, 246, 0.3); }");
        content.append(".button:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(139, 92, 246, 0.4); }");
        content.append(".footer { padding: 25px; text-align: center; color: #6b7280; font-size: 13px; background: linear-gradient(to right, #f8fafc, #f1f5f9); border-top: 1px solid #e2e8f0; }");
        content.append(".warning { background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%); border-left: 4px solid #d97706; padding: 20px; border-radius: 8px; margin: 20px 0; }");
        content.append(".warning strong { color: #92400e; display: block; margin-bottom: 10px; font-size: 16px; }");
        content.append(".warning ul { margin: 8px 0 0 0; padding-left: 20px; color: #78350f; }");
        content.append(".warning li { margin: 6px 0; }");
        content.append(".link-box { word-break: break-all; background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%); padding: 15px; border-radius: 8px; border: 1px solid #d1d5db; font-family: 'Courier New', monospace; font-size: 13px; color: #374151; }");
        content.append(".icon { display: inline-block; width: 20px; height: 20px; margin-right: 8px; vertical-align: middle; }");
        content.append("@media (max-width: 600px) { .container { margin: 10px; border-radius: 8px; } .header { padding: 20px 15px; } .content { padding: 20px 15px; } .button { padding: 12px 20px; font-size: 14px; } }");
        content.append("</style>");
        content.append("</head>");
        content.append("<body>");
        content.append("<div class='container'>");
        content.append("<div class='header'>");
        content.append("<h1>Talent Center</h1>");
        content.append("<h2>Đặt lại mật khẩu</h2>");
        content.append("</div>");
        content.append("<div class='content'>");
        content.append("<h3>👋 Xin chào ").append(name != null ? name : "bạn").append("!</h3>");
        content.append("<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn tại <strong>Talent Center</strong>.</p>");
        content.append("<p>Để tiếp tục, vui lòng nhấp vào nút bên dưới:</p>");
        content.append("<div style='text-align: center; margin: 30px 0;'>");
        content.append("<a href='").append(link).append("' class='button'>Đặt lại mật khẩu ngay</a>");
        content.append("</div>");
        content.append("<div class='warning'>");
        content.append("<strong>⚠️ Lưu ý quan trọng:</strong>");
        content.append("<ul>");
        content.append("<li><strong>Thời hạn:</strong> Link này sẽ hết hạn sau ").append(LIMIT_MINUTES).append(" phút</li>");
        content.append("<li><strong>Bảo mật:</strong> Không chia sẻ link này với bất kỳ ai khác</li>");
        content.append("<li><strong>Không yêu cầu:</strong> Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này</li>");
        content.append("</ul>");
        content.append("</div>");
        content.append("<p><strong>Nút không hoạt động?</strong> Sao chép và dán link sau vào trình duyệt:</p>");
        content.append("<div class='link-box'>");
        content.append(link);
        content.append("</div>");
        content.append("</div>");
        content.append("<div class='footer'>");
        content.append("<p><strong>Email tự động từ hệ thống Talent Center</strong></p>");
        content.append("<p>Vui lòng không trả lời email này. Nếu cần hỗ trợ, hãy liên hệ với chúng tôi qua website chính thức.</p>");
        content.append("</div>");
        content.append("</div>");
        content.append("</body>");
        content.append("</html>");

        return content.toString();
    }

    public int getExpirationMinutes() {
        return LIMIT_MINUTES;
    }
}