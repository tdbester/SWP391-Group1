package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.dto.BlogDto;
import org.example.talentcenter.model.Blog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlogDAO {

    // CREATE blog by using insert command SQL
    public void insert(Blog blog) {
        String sql = "INSERT INTO blogs (title, content, image, status, created_by, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getContent());
            stmt.setString(3, blog.getImage());
            stmt.setBoolean(4, blog.getStatus());
            stmt.setInt(5, blog.getCreatedBy());
            stmt.setDate(6, new Date(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ResultSet:[blog1, blog2, blog3] ;
//while: blog1 -> new Blog(blog1.id, blog1.title,...) -> list.add(blog)
    // READ ALL
    public List<BlogDto> getAll() {
        List<BlogDto> list = new ArrayList<>();
        String sql = "SELECT b.id, b.title, b.image, b.status, u.full_name " +
                "FROM blogs b " +
                "JOIN users u ON b.created_by = u.id";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BlogDto dto = new BlogDto(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getBoolean("status"),
                        rs.getString("full_name")
                );
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    // READ BY ID
    public Blog getById(int id) {
        String sql = "SELECT * FROM blogs WHERE id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Blog(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("image"),
                            rs.getBoolean("status"),
                            rs.getInt("created_by"),
                            rs.getDate("created_at"),
                            rs.getDate("updated_at")
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
        String sql = "UPDATE blogs SET title = ?, content = ?, image= ?, status = ?, created_by = ?, updated_at = getutcdate() WHERE id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getContent());
            stmt.setString(3, blog.getImage());
            stmt.setBoolean(4, blog.getStatus());
            stmt.setInt(5, blog.getCreatedBy());
            stmt.setInt(6, blog.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM blogs WHERE id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
