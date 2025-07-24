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
        return getTotalBlogWithFilters(null, null);
    }

    public int getTotalBlogWithFilters(String search, Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Blog b WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // Add search filter
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND b.Title LIKE ?");
            parameters.add("%" + search.trim() + "%");
        }

        // Add category filter
        if (categoryId != null) {
            sql.append(" AND b.CategoryId = ?");
            parameters.add(categoryId);
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<BlogDto> pagingBlog(int index) {
        return pagingBlogWithFilters(index, null, null);
    }

    public List<BlogDto> pagingBlogWithFilters(int index, String search, Integer categoryId) {
        List<BlogDto> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT b.Id, b.Title, b.Description, b.Content, b.image, b.CreatedAt,
                   ac.FullName, b.CategoryId, b.Status
            FROM Blog b
            JOIN Account ac ON b.authorId = ac.Id
            WHERE 1=1
            """);

        List<Object> parameters = new ArrayList<>();

        // Add search filter
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND b.Title LIKE ?");
            parameters.add("%" + search.trim() + "%");
        }

        // Add category filter
        if (categoryId != null) {
            sql.append(" AND b.CategoryId = ?");
            parameters.add(categoryId);
        }

        sql.append(" ORDER BY b.Id DESC OFFSET ? ROWS FETCH NEXT 10 ROWS ONLY");
        parameters.add((index - 1) * 10);

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during blog paging: " + e.getMessage(), e);
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
    public int getSaleIdByAccountId(int accountId) {
        int saleId = -1;
        String sql = "SELECT Id FROM Sale WHERE AccountId = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                saleId = rs.getInt("Id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saleId;
    }
}
