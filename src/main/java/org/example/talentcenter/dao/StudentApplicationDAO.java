package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Request;

import java.sql.*;

public class StudentApplicationDAO {
    public boolean insertTransferRequest(Request request) {
        String reason = "SĐT phụ huynh: " + request.getParentPhone() + "\n"
                + "Lý do: " + request.getReason();

        String sql = "INSERT INTO Request (Type, SenderId, Reason, Status, CreatedAt) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Chuyển lớp");
            stmt.setInt(2, request.getSenderID());
            stmt.setString(3, reason);
            stmt.setString(4, "Chờ xử lý");
            stmt.setDate(5, new java.sql.Date(request.getCreatedAt().getTime()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
