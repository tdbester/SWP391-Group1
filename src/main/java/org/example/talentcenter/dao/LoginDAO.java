package org.example.talentcenter.dao;

import java.sql.*;

import org.example.talentcenter.model.User;
import org.example.talentcenter.config.DBConnect;

public class LoginDAO {
    public User checkLogin(String email, String password) throws Exception {
        String sql = "SELECT * FROM users WHERE email=? AND password=?";
        try (Connection con = new DBConnect().getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            st.setString(2, password);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("phone"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }
}
