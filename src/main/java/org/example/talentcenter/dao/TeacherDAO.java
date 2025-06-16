package org.example.talentcenter.dao;

import java.sql.*;

public class TeacherDAO {
    public static int getTeacherIdByAccountId(Connection conn, int accountId) throws SQLException {
        String sql = "SELECT Id FROM Teacher WHERE AccountId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Id");
            }
        }
        return -1;
    }
}
