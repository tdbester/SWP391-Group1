package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    /**
     * Get all roles
     */
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Role role = new Role(
                        rs.getInt("Id"),
                        rs.getString("Name")
                );
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * Get role by ID
     */
    public Role getRoleById(int roleId) {
        String sql = "SELECT * FROM Role WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, roleId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Role(
                        rs.getInt("Id"),
                        rs.getString("Name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Get role by name */
    public Role getRoleByName(String roleName) {
        String sql = "SELECT * FROM Role WHERE Name = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, roleName);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Role(
                        rs.getInt("Id"),
                        rs.getString("Name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insert new role
     */
    public boolean insertRole(Role role) {
        String sql = "INSERT INTO Role (Name) VALUES (?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, role.getName());
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}