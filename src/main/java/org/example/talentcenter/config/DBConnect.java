package org.example.talentcenter.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final String URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=SchoolManagement1;"
            + "encrypt=true;"
            + "trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456789";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}