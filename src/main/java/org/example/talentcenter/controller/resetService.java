package org.example.talentcenter.controller;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

public class resetService {
    private final int LIMIT_MINUS =10;
    private final String from = "talentcenter0001@gmail.com";
    private final String password = "ehmf tner ayhv flzm";

    public String generateToken(){
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(LIMIT_MINUS);
    }

    public boolean isExpireTime(LocalDateTime time){
        return LocalDateTime.now().isAfter(time);
    }

    public boolean sendEmail(String to, String link, String name){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port","587");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,password);
            }
        };
        Session session = Session.getInstance(props, auth);

        MimeMessage msg = new MimeMessage(session);//gui 1 email co noi dung
        try {
            msg.addHeader("Content-type", "text/html; charset=utf-8");
            msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
            msg.setSubject("Đổi mật khẩu", "utf-8");
            String content = "<h1>Xin chào"+name+"</h1>"+"<p>Ấn vào link để đặt lại mật khẩu" +
                    " <a href = "+link+">Ấn vào đây</a></p>";
            msg.setContent(content, "text/html; charset=utf-8");
            Transport.send(msg);
            System.out.println("Sent message successfully....");
            return true;
        } catch (Exception e) {
            System.out.println("Sent message failed....");
            System.out.println(e);
            return false;
        }
    }
}
