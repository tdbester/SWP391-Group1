package org.example.talentcenter.config;

import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Kết nối SQL Server thành công!");
            } else {
                System.out.println("❌ Không thể kết nối!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối:");
            e.printStackTrace();
        }
    }
}