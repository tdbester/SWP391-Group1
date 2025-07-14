/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-07-07
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-07-07  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Notification;

import java.sql.*;
import java.util.ArrayList;

public class NotificationDAO {
    /**
     * Tạo một thông báo mới và lưu vào cơ sở dữ liệu.
     *
     * @param notification đối tượng {@link Notification} chứa thông tin thông báo cần tạo
     * @return {@code true} nếu thêm thành công, {@code false} nếu có lỗi xảy ra
     * @author Huyen Trang
     */
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

    /**
     * Lấy danh sách thông báo mới nhất cho một vai trò cụ thể và tài khoản
     * Trả về các thông báo được gửi đến:
     * - Tất cả người dùng ("ALL"),
     * - Người dùng thuộc vai trò {@code role} và không chỉ định người nhận cụ thể,
     * - Người dùng thuộc vai trò {@code role} và có {@code accountId} trùng khớp.
     *
     * @param role vai trò người nhận (ví dụ: "Student", "Teacher", ...)
     * @param accountId ID tài khoản người nhận cụ thể (có thể null)
     * @param limit số lượng thông báo muốn lấy
     * @return danh sách {@link Notification} theo điều kiện lọc
     * @author Huyen Trang
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
     * Tìm kiếm thông báo theo từ khóa trong tiêu đề, nội dung hoặc tên người gửi,
     * áp dụng cho vai trò và (tùy chọn) tài khoản cụ thể.
     *
     * @param keyword   Từ khóa tìm kiếm
     * @param role      Vai trò người nhận
     * @param accountId ID tài khoản người nhận
     * @return Danh sách thông báo phù hợp với từ khóa
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
     * Tìm kiếm thông báo dành cho nhân viên Sale theo từ khóa.
     */
    public ArrayList<Notification> searchNotificationsForSale(String keyword) {
        return searchNotificationsByKeyword(keyword, "Sale", null);
    }

    /**
     * Tìm kiếm thông báo dành cho học viên theo từ khóa.
     *
     * @param keyword Từ khóa tìm kiếm
     * @param accountId ID tài khoản học viên
     */
    public ArrayList<Notification> searchNotificationsForStudent(String keyword, int accountId) {
        return searchNotificationsByKeyword(keyword, "Student", accountId);
    }

    /**
     * Tìm kiếm thông báo dành cho quản lý đào tạo theo từ khóa.
     */
    public ArrayList<Notification> searchNotificationsForTrainingManager(String keyword) {
        return searchNotificationsByKeyword(keyword, "TrainingManager", null);
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