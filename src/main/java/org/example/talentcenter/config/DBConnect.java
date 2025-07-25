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
            + "databaseName=Final;"
            + "encrypt=true;"
            + "trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "12345678";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}