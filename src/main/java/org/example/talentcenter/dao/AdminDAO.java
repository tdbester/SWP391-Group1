package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Notification;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data Access Object for Admin-specific database operations
 * Provides methods to retrieve system-wide statistics and admin notifications
 */
public class AdminDAO {

    /**
     * Get system statistics for admin dashboard
     * @return array containing [totalTeachers, totalAccounts, totalCourses, totalStudents, totalClassrooms]
     */
    public int[] getSystemStatistics() {
        int[] stats = new int[5];
        
        String sql = """
            SELECT 
                (SELECT COUNT(*) FROM Teacher) as TotalTeachers,
                (SELECT COUNT(*) FROM Account) as TotalAccounts,
                (SELECT COUNT(*) FROM Course) as TotalCourses,
                (SELECT COUNT(*) FROM Student) as TotalStudents,
                (SELECT COUNT(*) FROM ClassRooms) as TotalClassrooms
            """;
            
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                stats[0] = rs.getInt("TotalTeachers");
                stats[1] = rs.getInt("TotalAccounts");
                stats[2] = rs.getInt("TotalCourses");
                stats[3] = rs.getInt("TotalStudents");
                stats[4] = rs.getInt("TotalClassrooms");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stats;
    }

    /**
     * Get latest admin notifications (placeholder for future implementation)
     * @param limit maximum number of notifications to retrieve
     * @return list of admin notifications
     */
    public ArrayList<Notification> getLatestAdminNotifications(int limit) {
        ArrayList<Notification> notifications = new ArrayList<>();
        
        // Placeholder implementation - can be expanded later when notification system is enhanced
        String sql = """
            SELECT TOP (?) Id, Title, Content, CreatedAt, IsRead, NotificationType, RelatedEntityId
            FROM Notification 
            WHERE RecipientRole = 'Admin' OR RecipientRole IS NULL
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
                notification.setCreatedAt(rs.getTimestamp("CreatedAt"));
                notification.setRead(rs.getBoolean("IsRead"));
                notification.setNotificationType(rs.getString("NotificationType"));
                notification.setRelatedEntityId(rs.getInt("RelatedEntityId"));
                
                notifications.add(notification);
            }
        } catch (SQLException e) {
            // If notification table doesn't have admin notifications yet, just return empty list
            System.out.println("Admin notifications not available yet: " + e.getMessage());
        }
        
        return notifications;
    }

    /**
     * Get count of unread admin notifications
     * @return count of unread notifications for admin
     */
    public int getUnreadAdminNotificationCount() {
        String sql = """
            SELECT COUNT(*) FROM Notification 
            WHERE (RecipientRole = 'Admin' OR RecipientRole IS NULL) AND IsRead = 0
            """;
            
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // If notification table doesn't support admin notifications yet, return 0
            System.out.println("Admin notification count not available: " + e.getMessage());
        }
        return 0;
    }
}
