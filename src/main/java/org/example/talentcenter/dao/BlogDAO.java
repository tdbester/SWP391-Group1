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
        String sql = "INSERT INTO Blog (Title, Description, image, Content, AuthorId, CategoryId, CreatedAt, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getDescription());
            stmt.setString(3, blog.getImage());
            stmt.setString(4, blog.getContent());
            stmt.setInt(5, blog.getAuthorId());
            stmt.setInt(6, blog.getCategory());
            stmt.setDate(7, new Date(System.currentTimeMillis()));
            stmt.setInt(8, blog.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // READ ALL
    public List<BlogDto> getAll() {
        List<BlogDto> list = new ArrayList<>();

        String sql = "SELECT b.Id, b.Title, b.Description, b.Content, b.image, b.CreatedAt, ac.FullName, b.CategoryId, b.Status " +
                "FROM Blog b " +
                "JOIN Account ac ON b.authorId = ac.Id";
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
                        rs.getInt("CategoryId"),
                        rs.getInt("Status")
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
                            rs.getInt("CategoryId"),
                            rs.getInt("Status")
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
                "AuthorId = ?, CategoryId = ?, Status = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getDescription());
            stmt.setString(3, blog.getImage());
            stmt.setString(4, blog.getContent());
            stmt.setInt(5, blog.getAuthorId());
            stmt.setInt(6, blog.getCategory());
            stmt.setInt(7, blog.getStatus());
            stmt.setInt(8, blog.getId());
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

        //tạo câu lệnh sql đ truy vấn
        String sql = "select * from Blog b " +
                "join Account ac on b.authorId = ac.Id\n" +
                "order by b.Id DESC\n" +
                "offset ? rows fetch next 10 rows only;";
        try {
            //tạo kết nối tới SQL
            Connection conn = DBConnect.getConnection();
            //tạo câu lệnh dựa vào sql
            PreparedStatement stmt = conn.prepareStatement(sql);
            // truyền tham số vào câu lệnh query
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
                        rs.getInt("CategoryId"),
                        rs.getInt("Status")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return list;
    }

    /**
     * Get all public blogs (status = 1)
     */
    public List<BlogDto> getPublicBlogs() {
        List<BlogDto> list = new ArrayList<>();

        String sql = "SELECT b.Id, b.Title, b.Description, b.Content, b.image, b.CreatedAt, ac.FullName, b.CategoryId, b.Status " +
                "FROM Blog b " +
                "JOIN Account ac ON b.authorId = ac.Id " +
                "WHERE b.Status = 1"+
                "order by b.Id DESC\n";
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
                        rs.getInt("CategoryId"),
                        rs.getInt("Status")
                );
                list.add(blogDto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get blog by ID for guests (works with existing database schema)
     */
    public BlogDto getPublicBlogById(int id) {
        String sql = "SELECT b.Id, b.Title, b.Description, b.Content, b.CreatedAt, ac.FullName, b.Category " +
                "FROM Blog b " +
                "JOIN Account ac ON b.authorId = ac.Id " +
                "WHERE b.Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BlogDto(
                            rs.getInt("Id"),
                            rs.getString("Title"),
                            rs.getString("Description"),
                            rs.getString("Content"),
                            rs.getString("image"),
                            rs.getDate("CreatedAt"),
                            rs.getString("FullName"),
                            rs.getInt("Category"),
                            1 // Default to public status
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
