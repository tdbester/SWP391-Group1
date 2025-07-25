package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Notification;
import org.example.talentcenter.model.ClassRooms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

            if (notification.getRecipientAccountId() != null && notification.getRecipientAccountId() > 0) {
                stmt.setInt(5, notification.getRecipientAccountId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            stmt.setString(6, notification.getNotificationType());
            if (notification.getRelatedEntityId() != null) {
                stmt.setObject(7, notification.getRelatedEntityId());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }
            stmt.setString(8, notification.getRelatedEntityType());
            stmt.setTimestamp(9, notification.getCreatedAt());
            stmt.setBoolean(10, notification.isRead());

            if (notification.getClassRoomId() != null && notification.getClassRoomId() > 0) {
                stmt.setInt(11, notification.getClassRoomId());
            } else {
                stmt.setNull(11, java.sql.Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting notification: " + e.getMessage());
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
     * Thông báo mới của giáo viên
     */

    public List<Notification> getRecentNotificationsForTeacher(int accountId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT Id, Title, Content, SenderName, RecipientRole, RecipientAccountId, " +
                "NotificationType, RelatedEntityId, RelatedEntityType, CreatedAt, IsRead, ClassRoomId " +
                "FROM Notification " +
                "WHERE RecipientAccountId = ? " +
                "AND CreatedAt >= DATEADD(DAY, -7, GETDATE()) " +
                "ORDER BY CreatedAt DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    n.setId(rs.getInt("Id"));
                    n.setTitle(rs.getString("Title"));
                    n.setContent(rs.getString("Content"));
                    n.setSenderName(rs.getString("SenderName"));
                    n.setRecipientRole(rs.getString("RecipientRole"));
                    n.setRecipientAccountId(rs.getInt("RecipientAccountId"));
                    n.setNotificationType(rs.getString("NotificationType"));
                    n.setRelatedEntityId(rs.getInt("RelatedEntityId"));
                    n.setRelatedEntityType(rs.getString("RelatedEntityType"));
                    n.setCreatedAt(Timestamp.valueOf(rs.getTimestamp("CreatedAt").toLocalDateTime()));
                    n.setRead(rs.getBoolean("IsRead"));
                    n.setClassRoomId(rs.getInt("ClassRoomId"));
                    notifications.add(n);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
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

    /**
     * Lấy danh sách thông báo của một người gửi cụ thể (theo tên)
     * Chỉ lấy thông báo do teacher gửi, không bao gồm thông báo hệ thống
     */
    public ArrayList<Notification> getNotificationsBySender(String senderName, Integer classRoomId,
                                                            String searchKeyword, String dateFrom, String dateTo) {
        ArrayList<Notification> notifications = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
    SELECT n.*, cr.Name as ClassName, c.Title as CourseTitle
    FROM Notification n
    LEFT JOIN ClassRooms cr ON n.ClassRoomId = cr.Id
    LEFT JOIN Course c ON cr.CourseId = c.Id
    WHERE n.SenderName = ? 
    AND n.SenderName != 'SYSTEM'
    AND n.RecipientRole = 'Student'
    """);

        ArrayList<Object> params = new ArrayList<>();
        params.add(senderName);

        if (classRoomId != null) {
            sql.append(" AND n.ClassRoomId = ?");
            params.add(classRoomId);
        }

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (n.Title LIKE ? OR n.Content LIKE ?)");
            String searchPattern = "%" + searchKeyword + "%";
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
                // Set className if available
                notification.setClassName(rs.getString("ClassName"));
                notifications.add(notification);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    /**
     * Lấy danh sách classroom của một teacher cụ thể
     */
    public ArrayList<ClassRooms> getClassRoomsByTeacherId(int teacherId) {
        ArrayList<ClassRooms> classRooms = new ArrayList<>();
        String sql = """
        SELECT cr.Id, cr.Name, c.Title as CourseTitle, cr.CourseId, cr.TeacherId
        FROM ClassRooms cr
        JOIN Course c ON cr.CourseId = c.Id
        WHERE cr.TeacherId = ?
        ORDER BY cr.Name
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ClassRooms classRoom = new ClassRooms();
                classRoom.setId(rs.getInt("Id"));
                classRoom.setName(rs.getString("Name"));
                classRoom.setCourseTitle(rs.getString("CourseTitle"));
                classRoom.setCourseId(rs.getInt("CourseId"));
                classRoom.setTeacherId(rs.getInt("TeacherId"));
                classRooms.add(classRoom);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classRooms;
    }

    /**
     * Update the existing mapResultSetToNotification method to handle className
     */
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

        int classRoomId = rs.getInt("ClassRoomId");
        if (!rs.wasNull()) {
            notification.setClassRoomId(classRoomId);
        }

        return notification;
    }

    /**
     * Đếm số lượng thông báo chưa đọc cho một vai trò cụ thể và tài khoản
     * Bao gồm các thông báo:
     * - Gửi đến vai trò {@code role} mà không chỉ định tài khoản,
     * - Gửi đến vai trò {@code role} và đúng {@code accountId},
     * - Gửi đến tất cả người dùng ("ALL").
     *
     * @param role vai trò người nhận
     * @param accountId ID tài khoản người nhận
     * @return số lượng thông báo chưa đọc
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
     * Lấy danh sách thông báo với phân trang cho một vai trò cụ thể và tài khoản.
     * Trả về các thông báo được gửi đến:
     * - Tất cả người dùng ("ALL"),
     * - Người dùng thuộc vai trò {@code role} và không chỉ định người nhận cụ thể,
     * - Người dùng thuộc vai trò {@code role} và có {@code accountId} trùng khớp.
     *
     * Kết quả sắp xếp theo thời gian tạo mới nhất, hỗ trợ lấy từng trang theo offset và limit.
     *
     * @param role vai trò người nhận (ví dụ: "Student", "Teacher", ...)
     * @param accountId ID tài khoản người nhận cụ thể (có thể null)
     * @param offset số dòng bỏ qua (dùng cho phân trang)
     * @param limit số lượng thông báo muốn lấy ở mỗi trang
     * @return danh sách {@link Notification} theo phân trang và điều kiện lọc
     * @author Huyen Trang
     */
    public ArrayList<Notification> getAllNotificationsForRoleWithPaging(
            String role, Integer accountId, int offset, int limit) {
        ArrayList<Notification> notifications = new ArrayList<>();
        String sql = """
        SELECT * FROM Notification 
        WHERE (RecipientRole = ? AND RecipientAccountId IS NULL) 
           OR (RecipientRole = ? AND RecipientAccountId = ?)
           OR (RecipientRole = 'ALL')
        ORDER BY CreatedAt DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
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

            stmt.setInt(4, offset);
            stmt.setInt(5, limit);

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
     * Đánh dấu một thông báo là đã đọc dựa trên ID.
     *
     * @param notificationId ID của thông báo cần đánh dấu
     * @return true nếu cập nhật thành công, false nếu có lỗi xảy ra
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
     * Đánh dấu tất cả thông báo là đã đọc cho một vai trò cụ thể và (nếu có) tài khoản người nhận cụ thể.
     *
     * @param role Vai trò người nhận
     * @param accountId ID tài khoản người nhận, hoặc null nếu là thông báo chung
     * @return true nếu có ít nhất một thông báo được cập nhật, false nếu không hoặc có lỗi xảy ra
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
     * Tìm kiếm thông báo theo từ khoá với phân trang cho từng vai trò và tài khoản cụ thể.
     *
     * @param keyword   Từ khoá tìm kiếm (áp dụng cho Title, Content, SenderName)
     * @param role      Vai trò người nhận (ví dụ: "Student", "Teacher", ...)
     * @param accountId ID tài khoản người nhận (có thể null)
     * @param offset    Số dòng bỏ qua (dùng cho phân trang)
     * @param limit     Số lượng thông báo cần lấy cho mỗi trang
     * @return Danh sách {@link Notification} tìm thấy theo điều kiện, có phân trang
     */
    public ArrayList<Notification> searchNotificationsByKeywordWithPaging(
            String keyword, String role, Integer accountId, int offset, int limit) {

        ArrayList<Notification> notifications = new ArrayList<>();
        String sql = """
            SELECT * FROM Notification 
            WHERE ((RecipientRole = ? AND RecipientAccountId IS NULL) 
               OR (RecipientRole = ? AND RecipientAccountId = ?)
               OR (RecipientRole = 'ALL'))
            AND (Title LIKE ? OR Content LIKE ? OR SenderName LIKE ?)
            ORDER BY CreatedAt DESC
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
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
            stmt.setInt(7, offset);
            stmt.setInt(8, limit);

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
     * Xóa một thông báo theo ID.
     *
     * @param notificationId ID của thông báo cần xóa
     * @return true nếu xóa thành công, ngược lại false
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
     * Đếm tổng số thông báo cho một vai trò cụ thể và tài khoản (nếu có).
     *
     * @param role vai trò người nhận (ví dụ: "Student", "Sale", "TrainingManager")
     * @param accountId ID người nhận cụ thể, có thể null nếu lấy cho tất cả của role
     * @return tổng số bản ghi thỏa mãn trong bảng Notification
     * @author Huyen Trang
     */
    public int getTotalNotificationsCountForRole(String role, Integer accountId) {
        String sql = """
        SELECT COUNT(*) FROM Notification
        WHERE (RecipientRole = ? AND RecipientAccountId IS NULL)
           OR (RecipientRole = ? AND RecipientAccountId = ?)
           OR (RecipientRole = 'ALL')
    """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setString(2, role);
            if (accountId != null) {
                ps.setInt(3, accountId);
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * Lấy danh sách thông báo với phân trang dành cho role "Sale".
     *
     * @param offset Số dòng bỏ qua (dùng cho phân trang)
     * @param limit  Số lượng thông báo cần lấy
     * @return Danh sách thông báo (có phân trang)
     */
    public ArrayList<Notification> getAllNotificationsForSaleWithPaging(int offset, int limit) {
        return getAllNotificationsForRoleWithPaging("Sale", null, offset, limit);
    }

    /**
     * Lấy danh sách thông báo với phân trang dành cho một học sinh cụ thể.
     *
     * @param accountId ID học sinh
     * @param offset    Số dòng bỏ qua (dùng cho phân trang)
     * @param limit     Số lượng thông báo cần lấy
     * @return Danh sách thông báo mới nhất (có phân trang)
     */
    public ArrayList<Notification> getAllNotificationsForStudentWithPaging(int accountId, int offset, int limit) {
        return getAllNotificationsForRoleWithPaging("Student", accountId, offset, limit);
    }

    /**
     * Lấy danh sách thông báo với phân trang dành cho quản lý đào tạo.
     *
     * @param offset Số dòng bỏ qua (dùng cho phân trang)
     * @param limit  Số lượng thông báo cần lấy
     * @return Danh sách thông báo (có phân trang)
     */
    public ArrayList<Notification> getAllNotificationsForTrainingManagerWithPaging(int offset, int limit) {
        return getAllNotificationsForRoleWithPaging("TrainingManager", null, offset, limit);
    }

    /**
     * Lấy danh sách thông báo mới nhất dành cho role "Sale".
     *
     * @param limit Số lượng thông báo cần lấy
     * @return Danh sách thông báo mới nhất
     */
    public ArrayList<Notification> getLatestNotificationsForSale(int limit) {
        return getLatestNotificationsForRole("Sale", null, limit);
    }

    /**
     * Lấy danh sách thông báo mới nhất dành cho học sinh cụ thể.
     *
     * @param accountId ID học sinh
     * @param limit Số lượng thông báo cần lấy
     * @return Danh sách thông báo mới nhất
     */
    public ArrayList<Notification> getLatestNotificationsForStudent(int accountId, int limit) {
        return getLatestNotificationsForRole("Student", accountId, limit);
    }

    /**
     * Đếm số thông báo chưa đọc dành cho role "Sale".
     * @return Số lượng thông báo chưa đọc
     */
    public int getUnreadCountForSale() {
        return getUnreadCountForRole("Sale", null);
    }

    /**
     * Đếm số thông báo chưa đọc dành cho một học sinh cụ thể.
     * @param accountId ID học sinh
     * @return Số lượng thông báo chưa đọc
     */
    public int getUnreadCountForStudent(int accountId) {
        return getUnreadCountForRole("Student", accountId);
    }

    /**
     * Đánh dấu tất cả thông báo của vai trò "Sale" là đã đọc.
     * @return true nếu thành công, ngược lại false
     */
    public boolean markAllAsReadForSale() {
        return markAllAsReadForRole("Sale", null);
    }


    /**
     * Đánh dấu tất cả thông báo của một học sinh là đã đọc.
     * @param accountId ID tài khoản học sinh
     * @return true nếu thành công, ngược lại false
     */
    public boolean markAllAsReadForStudent(int accountId) {
        return markAllAsReadForRole("Student", accountId);
    }

    /**
     * Lấy danh sách thông báo mới nhất cho quản lý đào tạo.
     */
    public ArrayList<Notification> getLatestNotificationsForTrainingManager(int limit) {
        return getLatestNotificationsForRole("TrainingManager", null, limit);
    }

    /**
     * Đếm số thông báo chưa đọc của quản lý đào tạo.
     */
    public int getUnreadCountForTrainingManager() {
        return getUnreadCountForRole("TrainingManager", null);
    }

    /**
     * Đánh dấu tất cả thông báo của quản lý đào tạo là đã đọc.
     */
    public boolean markAllAsReadForTrainingManager() {
        return markAllAsReadForRole("TrainingManager", null);
    }

    /**
     * Tìm kiếm thông báo dành cho nhân viên Sale theo từ khóa, hỗ trợ phân trang.
     *
     * @param keyword Từ khóa tìm kiếm
     * @param offset  Số dòng bỏ qua (phân trang)
     * @param limit   Số lượng thông báo cần lấy
     * @return Danh sách thông báo tìm thấy (theo phân trang)
     */
    public ArrayList<Notification> searchNotificationsForSaleWithPaging(String keyword, int offset, int limit) {
        return searchNotificationsByKeywordWithPaging(keyword, "Sale", null, offset, limit);
    }

    /**
     * Tìm kiếm thông báo dành cho học viên theo từ khóa, hỗ trợ phân trang.
     *
     * @param keyword   Từ khóa tìm kiếm
     * @param accountId ID tài khoản học viên
     * @param offset    Số dòng bỏ qua (phân trang)
     * @param limit     Số lượng thông báo cần lấy
     * @return Danh sách thông báo tìm thấy dành cho học viên (theo phân trang)
     */
    public ArrayList<Notification> searchNotificationsForStudentWithPaging(String keyword, int accountId, int offset, int limit) {
        return searchNotificationsByKeywordWithPaging(keyword, "Student", accountId, offset, limit);
    }

    /**
     * Tìm kiếm thông báo dành cho quản lý đào tạo theo từ khóa, hỗ trợ phân trang.
     *
     * @param keyword Từ khóa tìm kiếm
     * @param offset  Số dòng bỏ qua (phân trang)
     * @param limit   Số lượng thông báo cần lấy
     * @return Danh sách thông báo tìm thấy dành cho Training Manager (theo phân trang)
     */
    public ArrayList<Notification> searchNotificationsForTrainingManagerWithPaging(String keyword, int offset, int limit) {
        return searchNotificationsByKeywordWithPaging(keyword, "TrainingManager", null, offset, limit);
    }



    /**
     * Đếm tổng số thông báo dành cho Sale.
     *
     * @return tổng số dòng thông báo dành cho Sale
     */
    public int getTotalNotificationsCountForSale() {
        return getTotalNotificationsCountForRole("Sale", null);
    }

    /**
     * Đếm tổng số thông báo dành cho một học viên cụ thể.
     *
     * @param accountId ID của học viên
     * @return tổng số dòng thông báo cho học viên này
     */
    public int getTotalNotificationsCountForStudent(int accountId) {
        return getTotalNotificationsCountForRole("Student", accountId);
    }

    /**
     * Đếm tổng số thông báo dành cho Training Manager.
     *
     * @return tổng số dòng thông báo dành cho Training Manager
     */
    public int getTotalNotificationsCountForTrainingManager() {
        return getTotalNotificationsCountForRole("TrainingManager", null);
    }
}