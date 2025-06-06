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
        String sql = "INSERT INTO Blog (Title, Description, image, Content, Authorid, CreatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getDescription());
            stmt.setString(3, blog.getImage());
            stmt.setString(4, blog.getContent());
            stmt.setInt(5, blog.getAuthorId());
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

        String sql = "SELECT b.Id, b.Title, b.Description, b.Content, b.image, b.CreatedAt, ac.FullName " +
                "FROM Blog b join Sale s ON b.AuthorId = s.id join Account ac on s.id = ac.Id";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                BlogDto blogDto = new
                        BlogDto(
                    rs.getInt("Id"),
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Content"),
                    rs.getString("image"),
                    rs.getDate("CreatedAt"),
                    rs.getString("FullName")
                );
                list.add(blogDto);
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
                            rs.getInt("Id"),
                            rs.getString("Title"),
                            rs.getString("Content"),
                            rs.getString("image"),
                            rs.getInt("AuthorId"),
                            rs.getDate("CreatedAt"),
                            rs.getString("Description")
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
        String sql = "UPDATE Blog SET Title = ?, Description = ?, image= ?, Content = ?, AuthorId = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getDescription());
            stmt.setString(3, blog.getImage());
            stmt.setString(4, blog.getContent());
            stmt.setInt(5, blog.getAuthorId());
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
