package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Notification;

import java.sql.*;
import java.util.ArrayList;

public class NotificationDAO {

    public boolean createNotification(Notification notification) {
        String sql = """
                    INSERT INTO Notification 
                    (Title, Content, SenderName, RecipientRole, RecipientAccountId, NotificationType, RelatedEntityId, RelatedEntityType, CreatedAt, IsRead) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getContent());
            stmt.setString(3, notification.getSenderName());
            stmt.setString(4, notification.getRecipientRole());
            if (notification.getRecipientAccountId() != null) {
                stmt.setInt(5, notification.getRecipientAccountId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            stmt.setString(6, notification.getNotificationType());
            stmt.setObject(7, notification.getRelatedEntityId());
            stmt.setString(8, notification.getRelatedEntityType());
            stmt.setTimestamp(9, notification.getCreatedAt());
            stmt.setBoolean(10, notification.isRead());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Notification> getLatestNotificationsForRole(String role, Integer accountId, int limit) {
        ArrayList<Notification> notifications = new ArrayList<>();
        String sql = """
                    SELECT TOP (?) * FROM Notification 
                    WHERE (RecipientRole = ? AND RecipientAccountId IS NULL) 
                       OR (RecipientRole = ? AND RecipientAccountId = ?)
                       OR (RecipientRole = 'ALL')
                    ORDER BY CreatedAt DESC
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setString(2, role);
            stmt.setString(3, role);

            if (accountId != null) {
                stmt.setInt(4, accountId);
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = mapResultSetToNotification(rs);
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public int getUnreadCountForRole(String role, Integer accountId) {
        String sql = """
                    SELECT COUNT(*) FROM Notification 
                    WHERE ((RecipientRole = ? AND RecipientAccountId IS NULL) 
                        OR (RecipientRole = ? AND RecipientAccountId = ?)
                        OR (RecipientRole = 'ALL'))
                      AND IsRead = 0
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            stmt.setString(2, role);

            if (accountId != null) {
                stmt.setInt(3, accountId);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            ResultSet rs = stmt.executeQuery();

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

    public boolean markAllAsReadForRole(String role, Integer accountId) {
        String sql = """
        UPDATE Notification SET IsRead = 1 
        WHERE ((RecipientRole = ? AND RecipientAccountId IS NULL) 
            OR (RecipientRole = ? AND RecipientAccountId = ?)
            OR (RecipientRole = 'ALL'))
          AND IsRead = 0
    """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            stmt.setString(2, role);

            if (accountId != null) {
                stmt.setInt(3, accountId);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Marked " + rowsAffected + " notifications as read for role: " + role);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error marking all notifications as read for role " + role + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<Notification> getLatestNotificationsForSale(int limit) {
        return getLatestNotificationsForRole("Sale", null, limit);
    }

    public ArrayList<Notification> getLatestNotificationsForStudent(int accountId, int limit) {
        return getLatestNotificationsForRole("Student", accountId, limit);
    }

    public int getUnreadCountForSale() {
        return getUnreadCountForRole("Sale", null);
    }

    public int getUnreadCountForStudent(int accountId) {
        return getUnreadCountForRole("Student", accountId);
    }

    public boolean markAllAsReadForSale() {
        return markAllAsReadForRole("Sale", null);
    }

    public boolean markAllAsReadForStudent(int accountId) {
        return markAllAsReadForRole("Student", accountId);
    }

    public ArrayList<Notification> getLatestNotificationsForTrainingManager(int limit) {
        return getLatestNotificationsForRole("TrainingManager", null, limit);
    }

    public int getUnreadCountForTrainingManager() {
        return getUnreadCountForRole("TrainingManager", null);
    }

    public boolean markAllAsReadForTrainingManager() {
        return markAllAsReadForRole("TrainingManager", null);
    }


    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getInt("Id"));
        notification.setTitle(rs.getString("Title"));
        notification.setContent(rs.getString("Content"));
        notification.setSenderName(rs.getString("SenderName"));
        notification.setRecipientRole(rs.getString("RecipientRole"));

        int recipientAccountId = rs.getInt("RecipientAccountId");
        if (!rs.wasNull()) {
            notification.setRecipientAccountId(recipientAccountId);
        }

        notification.setNotificationType(rs.getString("NotificationType"));
        notification.setRelatedEntityId(rs.getObject("RelatedEntityId", Integer.class));
        notification.setRelatedEntityType(rs.getString("RelatedEntityType"));
        notification.setCreatedAt(rs.getTimestamp("CreatedAt"));
        notification.setRead(rs.getBoolean("IsRead"));

        return notification;
    }

}
