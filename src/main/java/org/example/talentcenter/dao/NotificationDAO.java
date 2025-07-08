package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Notification;

import java.sql.*;
import java.util.ArrayList;

public class NotificationDAO {

    public boolean createNotification(Notification notification) {
        String sql = """
            INSERT INTO Notification 
            (Title, Content, SenderName, RecipientRole, NotificationType, RelatedEntityId, RelatedEntityType, CreatedAt, IsRead) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;    

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getContent());
            stmt.setString(3, notification.getSenderName());
            stmt.setString(4, notification.getRecipientRole());
            stmt.setString(5, notification.getNotificationType());
            stmt.setObject(6, notification.getRelatedEntityId());
            stmt.setString(7, notification.getRelatedEntityType());
            stmt.setTimestamp(8, notification.getCreatedAt());
            stmt.setBoolean(9, notification.isRead());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Notification> getLatestNotificationsForSale(int limit) {
        ArrayList<Notification> notifications = new ArrayList<>();
        String sql = """
            SELECT TOP (?) * FROM Notification 
            WHERE RecipientRole = 'Sale'
            ORDER BY CreatedAt DESC
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setId(rs.getInt("Id"));
                notification.setTitle(rs.getString("Title"));
                notification.setContent(rs.getString("Content"));
                notification.setSenderName(rs.getString("SenderName"));
                notification.setRecipientRole(rs.getString("RecipientRole"));
                notification.setNotificationType(rs.getString("NotificationType"));
                notification.setRelatedEntityId(rs.getObject("RelatedEntityId", Integer.class));
                notification.setRelatedEntityType(rs.getString("RelatedEntityType"));
                notification.setCreatedAt(rs.getTimestamp("CreatedAt"));
                notification.setRead(rs.getBoolean("IsRead"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public int getUnreadCountForSale() {
        String sql = """
            SELECT COUNT(*) FROM Notification 
            WHERE (RecipientRole = 'Sale' OR RecipientRole = 'ALL') AND IsRead = 0
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE Notification SET IsRead = 1 WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<Notification> getAllNotifications() {
        ArrayList<Notification> notifications = new ArrayList<>();
        String sql = """
                    SELECT * FROM Notification
                                           WHERE RecipientRole = 'Sale'
                                           ORDER BY CreatedAt DESC;
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setId(rs.getInt("Id"));
                notification.setTitle(rs.getString("Title"));
                notification.setContent(rs.getString("Content"));
                notification.setSenderName(rs.getString("SenderName"));
                notification.setRecipientRole(rs.getString("RecipientRole"));
                notification.setNotificationType(rs.getString("NotificationType"));
                notification.setRelatedEntityId(rs.getObject("RelatedEntityId", Integer.class));
                notification.setRelatedEntityType(rs.getString("RelatedEntityType"));
                notification.setCreatedAt(rs.getTimestamp("CreatedAt"));
                notification.setRead(rs.getBoolean("IsRead"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
}
