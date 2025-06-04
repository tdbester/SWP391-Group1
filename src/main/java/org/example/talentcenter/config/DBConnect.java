package org.example.talentcenter.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=SchoolManagement1;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123";

    static {
        try {
            // Load driver thủ công, đảm bảo driver được nạp
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot load SQL Server JDBC driver");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        // Tạo kết nối tới SQL Server
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

