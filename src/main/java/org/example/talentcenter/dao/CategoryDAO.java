package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public Category getById(int id) {
        String sql = "SELECT Id, Name, Type FROM Category WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                            rs.getInt("Id"),
                            rs.getString("Name"),
                            rs.getInt("Type")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> getByType(int type) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT Id, Name, Type FROM Category WHERE Type = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(new Category(
                            rs.getInt("Id"),
                            rs.getString("Name"),
                            rs.getInt("Type")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
