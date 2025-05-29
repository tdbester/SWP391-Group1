package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Blog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlogDAO {

    // CREATE blog by using insert command SQL
    public void insert(Blog blog) {
        String sql = "INSERT INTO Blog (title, content, status, created_by, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getContent());
            stmt.setBoolean(3, blog.getStatus());
            stmt.setDate(4, blog.getCreatedBy());
            stmt.setDate(5, new Date(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//ResultSet:[blog1, blog2, blog3] ;
//while: blog1 -> new Blog(blog1.id, blog1.title,...) -> list.add(blog)
    // READ ALL
    public List<Blog> getAll() {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM Blog";
        try (Connection conn = DBConnect.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Blog blog = new Blog(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getBoolean("status"),
                        rs.getDate("createdBy"),
                        rs.getDate("createdAt")
                );
                list.add(blog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ BY ID
    public Blog getById(int id) {
        String sql = "SELECT * FROM Blog WHERE id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Blog(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getBoolean("status"),
                            rs.getDate("createdBy"),
                            rs.getDate("createdAt")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public void update(Blog blog) {
        String sql = "UPDATE Blog SET title = ?, content = ?, status = ?, createdBy = ?, createdAt = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getContent());
            stmt.setBoolean(3, blog.getStatus());
            stmt.setDate(4, blog.getCreatedBy());
            stmt.setDate(5, blog.getCreatedAt());
            stmt.setInt(6, blog.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM Blog WHERE id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
