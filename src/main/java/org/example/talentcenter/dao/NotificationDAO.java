package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Notification;
import org.example.talentcenter.model.ClassRooms;

import java.sql.*;
import java.util.ArrayList;

public class NotificationDAO {

    /**
     * Tạo một thông báo mới và lưu vào cơ sở dữ liệu.
     */
    public boolean createNotification(Notification notification) {
        String sql = """
            INSERT INTO Notification
            (Title, Content, SenderName, RecipientRole, RecipientAccountId, NotificationType, RelatedEntityId, RelatedEntityType, CreatedAt, IsRead, ClassRoomId)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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

            if (notification.getClassRoomId() != null) {
                stmt.setInt(11, notification.getClassRoomId());
            } else {
                stmt.setNull(11, java.sql.Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tạo thông báo gửi đến classroom
     */
    public boolean createNotificationForClassRoom(Notification notification) {
        String sql = """
            INSERT INTO Notification
            (Title, Content, SenderName, RecipientRole, ClassRoomId, NotificationType, CreatedAt, IsRead)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getContent());
            stmt.setString(3, notification.getSenderName());
            stmt.setString(4, "Student"); // Gửi đến học sinh
            stmt.setInt(5, notification.getClassRoomId());
            stmt.setString(6, notification.getNotificationType());
            stmt.setTimestamp(7, notification.getCreatedAt());
            stmt.setBoolean(8, notification.isRead());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách classroom để hiển thị trong dropdown
     */
    public ArrayList<ClassRooms> getAllClassRooms() {
        ArrayList<ClassRooms> classRooms = new ArrayList<>();
        String sql = """
            SELECT cr.Id, cr.Name, c.Title as CourseTitle
            FROM ClassRooms cr
            JOIN Course c ON cr.CourseId = c.Id
            ORDER BY cr.Name
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ClassRooms classRoom = new ClassRooms();
                classRoom.setId(rs.getInt("Id"));
                classRoom.setName(rs.getString("Name"));
                classRoom.setCourseTitle(rs.getString("CourseTitle"));
                classRooms.add(classRoom);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classRooms;
    }

    /**
     * Lấy thông báo theo classroom với filter
     */
    public ArrayList<Notification> getNotificationsForClassRoom(Integer classRoomId, String searchKeyword, String dateFrom, String dateTo) {
        ArrayList<Notification> notifications = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT n.*, cr.Name as ClassName, c.Title as CourseTitle
            FROM Notification n
            LEFT JOIN ClassRooms cr ON n.ClassRoomId = cr.Id
            LEFT JOIN Course c ON cr.CourseId = c.Id
            WHERE n.RecipientRole = 'Student'
            """);

        ArrayList<Object> params = new ArrayList<>();

        if (classRoomId != null) {
            sql.append(" AND n.ClassRoomId = ?");
            params.add(classRoomId);
        }

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (n.Title LIKE ? OR n.Content LIKE ? OR n.SenderName LIKE ?)");
            String searchPattern = "%" + searchKeyword + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        if (dateFrom != null && !dateFrom.trim().isEmpty()) {
            sql.append(" AND n.CreatedAt >= ?");
            params.add(dateFrom + " 00:00:00");
        }

        if (dateTo != null && !dateTo.trim().isEmpty()) {
            sql.append(" AND n.CreatedAt <= ?");
            params.add(dateTo + " 23:59:59");
        }

        sql.append(" ORDER BY n.CreatedAt DESC");

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
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

    /**
     * Lấy thông báo theo ID
     */
    public Notification getNotificationById(int notificationId) {
        Notification notification = null;
        String sql = "SELECT * FROM Notification WHERE Id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                notification = mapResultSetToNotification(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notification;
    }

    /**
     * Cập nhật thông báo
     */
    public boolean updateNotification(Notification notification) {
        String sql = """
            UPDATE Notification 
            SET Title = ?, Content = ?, NotificationType = ?, ClassRoomId = ?
            WHERE Id = ?
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getContent());
            stmt.setString(3, notification.getNotificationType());

            if (notification.getClassRoomId() != null) {
                stmt.setInt(4, notification.getClassRoomId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.setInt(5, notification.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa một thông báo theo ID.
     */
    public boolean deleteNotification(int notificationId) {
        String sql = "DELETE FROM Notification WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách thông báo mới nhất cho một vai trò cụ thể và tài khoản
     */
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

    /**
     * Đếm số lượng thông báo chưa đọc cho một vai trò cụ thể và tài khoản
     */
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

    /**
     * Đánh dấu một thông báo là đã đọc dựa trên ID.
     */
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

    /**
     * Đánh dấu tất cả thông báo là đã đọc cho một vai trò cụ thể
     */
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

    /**
     * Tìm kiếm thông báo theo từ khóa
     */
    public ArrayList<Notification> searchNotificationsByKeyword(String keyword, String role, Integer accountId) {
        ArrayList<Notification> notifications = new ArrayList<>();
        String sql = """
            SELECT * FROM Notification
            WHERE ((RecipientRole = ? AND RecipientAccountId IS NULL)
            OR (RecipientRole = ? AND RecipientAccountId = ?)
            OR (RecipientRole = 'ALL'))
            AND (Title LIKE ? OR Content LIKE ? OR SenderName LIKE ?)
            ORDER BY CreatedAt DESC
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

            String searchPattern = "%" + keyword + "%";
            stmt.setString(4, searchPattern);
            stmt.setString(5, searchPattern);
            stmt.setString(6, searchPattern);

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

    /**
     * Mapping dữ liệu từ ResultSet thành đối tượng Notification.
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getInt("Id"));
        notification.setTitle(rs.getString("Title"));
        notification.setContent(rs.getString("Content"));
        notification.setSenderName(rs.getString("SenderName"));
        notification.setRecipientRole(rs.getString("RecipientRole"));

        /*int recipientAccountId = rs.getInt("RecipientAccountId");
        if (!rs.wasNull()) {
            notification.setRecipientAccountId(recipientAccountId);
        }*/

        notification.setNotificationType(rs.getString("NotificationType"));
        notification.setRelatedEntityId(rs.getObject("RelatedEntityId", Integer.class));
        notification.setRelatedEntityType(rs.getString("RelatedEntityType"));
        notification.setCreatedAt(rs.getTimestamp("CreatedAt"));
        notification.setRead(rs.getBoolean("IsRead"));

        // Thêm ClassRoomId
        int classRoomId = rs.getInt("ClassRoomId");
        if (!rs.wasNull()) {
            notification.setClassRoomId(classRoomId);
        }

        return notification;
    }
}
