package org.example.talentcenter.dao;

import java.sql.*;
import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Account;

public class LoginDAO {
    public Account checkLogin(String email, String password) throws Exception {
        String sql = "SELECT a.*, r.Name as RoleName FROM Account a " +
                "JOIN Role r ON a.RoleId = r.Id " +
                "WHERE a.Email = ? AND a.Password = ?";
        try (Connection con = new DBConnect().getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            st.setString(2, password);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            rs.getInt("Id"),
                            rs.getString("FullName"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Address"),
                            rs.getInt("RoleId")
                    );
                }
            }
        }
        return null;
    }

    // Method to get role name by roleId
    public String getRoleName(int roleId) throws Exception {
        String sql = "SELECT Name FROM Role WHERE Id = ?";
        try (Connection con = new DBConnect().getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, roleId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Name");
                }
            }
        }
        return null;
    }
}
