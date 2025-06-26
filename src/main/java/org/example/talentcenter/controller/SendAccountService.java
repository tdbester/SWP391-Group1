package org.example.talentcenter.controller;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

public class SendAccountService {
    private final String from = "talentcenter0001@gmail.com";
    private final String password = "ehmf tner ayhv flzm";

    public boolean sendNewAccountEmail(String to, String username, String password1, String name) {
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
            msg.setSubject("Thông tin tài khoản mới - Talent Center", "UTF-8");

            String content = buildNewAccountContent(name, username, password1);
            msg.setContent(content, "text/html; charset=UTF-8");

            Transport.send(msg);
            System.out.println("New account email sent successfully to: " + to);
            return true;

        } catch (Exception e) {
            System.out.println("Failed to send new account email to: " + to);
            e.printStackTrace();
            return false;
        }
    }

    private String buildNewAccountContent(String name, String username, String password1) {
        return "<html><body>" +
                "<h3>Xin chào " + (name != null ? name : "bạn") + "!</h3>" +
                "<p>Tài khoản mới của bạn tại <strong>Talent Center</strong> đã được tạo thành công.</p>" +
                "<p><b>Tên tài khoản (email):</b> " + username + "</p>" +
                "<p><b>Mật khẩu:</b> " + password1 + "</p>" +
                "<p>Vui lòng đổi mật khẩu sau lần đăng nhập đầu tiên để bảo mật.</p>" +
                "<p>Chúc bạn học tập hiệu quả!</p>" +
                "<p><i>Email tự động từ hệ thống Talent Center, vui lòng không trả lời email này.</i></p>" +
                "</body></html>";
    }
    public String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}
