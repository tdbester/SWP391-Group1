package org.example.talentcenter.dao;

import org.example.talentcenter.model.User;
import org.example.talentcenter.config.DBConnect;
import java.sql.*;
import java.time.LocalDateTime;

public class UserDAO {

    /**
     * Lấy thông tin user theo ID
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFull_name(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setPhone(rs.getString("phone"));

                // Convert Timestamp to LocalDateTime
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    user.setCreated_at(timestamp.toLocalDateTime());
                }

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin cá nhân của user
     */
    public boolean updateUserProfile(int userId, String fullName, String phone, String email) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, fullName);
            st.setString(2, phone);
            st.setString(3, email);
            st.setInt(4, userId);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật mật khẩu của user
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, newPassword);
            st.setInt(2, userId);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra email đã tồn tại chưa (trừ user hiện tại)
     */
    public boolean isEmailExists(String email, int currentUserId) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id != ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, email);
            st.setInt(2, currentUserId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra số điện thoại đã tồn tại chưa (trừ user hiện tại)
     */
    public boolean isPhoneExists(String phone, int currentUserId) {
        String sql = "SELECT COUNT(*) FROM users WHERE phone = ? AND id != ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, phone);
            st.setInt(2, currentUserId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy thông tin user theo email (để kiểm tra login)
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFull_name(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setPhone(rs.getString("phone"));

                // Convert Timestamp to LocalDateTime
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    user.setCreated_at(timestamp.toLocalDateTime());
                }

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePasswordByEmail(String email, String password) {
        String sql = "UPDATE [dbo].[users]\n"
                + "   SET [password] = ?\n"
                + " WHERE [email] = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)){
            st.setString(1, password);
            st.setString(2, email);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}