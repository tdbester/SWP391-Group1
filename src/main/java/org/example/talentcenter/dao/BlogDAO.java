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
        String sql = "INSERT INTO Blog (Title, Description, image, Content, AuthorId, Category, CreatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getDescription());
            stmt.setString(3, blog.getImage());
            stmt.setString(4, blog.getContent());
            stmt.setInt(5, blog.getAuthorId());
            stmt.setInt(6, blog.getCategory());
            stmt.setDate(7, new Date(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // READ ALL
    public List<BlogDto> getAll() {
        List<BlogDto> list = new ArrayList<>();

        String sql = "SELECT b.Id, b.Title, b.Description, b.Content, b.image, b.CreatedAt, ac.FullName, b.Category " +
                "FROM Blog b " +
                "JOIN Sale s ON b.AuthorId = s.id " +
                "JOIN Account ac ON s.id = ac.Id";
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
                        rs.getString("FullName"),
                        rs.getInt("Category")
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
                            rs.getString("Description"),
                            rs.getInt("Category")
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
        String sql = "UPDATE Blog SET Title = ?, Description = ?, image = ?, Content = ?, " +
                "AuthorId = ?, Category = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getDescription());
            stmt.setString(3, blog.getImage());
            stmt.setString(4, blog.getContent());
            stmt.setInt(5, blog.getAuthorId());
            stmt.setInt(6, blog.getCategory());
            stmt.setInt(7, blog.getId());
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

    public int getTotalBlog() {
        String sql = "SELECT COUNT(*) FROM Blog";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public List<BlogDto> pagingBlog(int index) {
        List<BlogDto> list = new ArrayList<>();
        String sql = "select * from Blog b " +
                "join Sale s ON b.AuthorId = s.id " +
                "join Account ac on s.id = ac.Id\n" +
                "order by b.Id\n" +
                "offset ? rows fetch next 10 rows only;";
        try {
            Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (index - 1) * 10);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new BlogDto(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Content"),
                        rs.getString("image"),
                        new java.util.Date(rs.getDate("CreatedAt").getTime()),
                        rs.getString("FullName"),
                        rs.getInt("CategoryId")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return list;
    }

}
