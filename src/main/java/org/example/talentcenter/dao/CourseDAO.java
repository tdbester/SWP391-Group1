package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.dto.CourseDto;
import org.example.talentcenter.model.Course;
import org.example.talentcenter.model.Category;
import org.example.talentcenter.utilities.Level;
import org.example.talentcenter.utilities.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public void insert(Course course) {
        String sql = "INSERT INTO Course (Title, Price, Information, CreatedBy, Image, CategoryID, Level, Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getTitle());
            stmt.setDouble(2, course.getPrice());
            stmt.setString(3, course.getInformation());
            stmt.setInt(4, course.getCreatedBy());
            stmt.setString(5, course.getImage());
            stmt.setInt(6, course.getCategory().getId());
            stmt.setString(7, course.getLevel() != null ? course.getLevel().name() : null);
            stmt.setString(8, course.getType() != null ? course.getType().name() : null);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Course> getAll() {
        List<Course> list = new ArrayList<>();
        String sql = """
            SELECT
                c.Id, c.Title, c.Price, c.Information, c.CreatedBy, c.Image,
                c.CategoryID, c.Level, c.Type,
                cat.Name AS CategoryName, cat.Type AS CategoryType,
                a.FullName
            FROM Course c
            JOIN Category cat ON c.CategoryID = cat.Id
            LEFT JOIN Account a ON c.CreatedBy = a.Id
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getInt("CategoryType")
                );

                // Convert string to enum values
                Level level = null;
                String levelStr = rs.getString("Level");
                if (levelStr != null) {
                    try {
                        level = Level.valueOf(levelStr);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum value
                    }
                }

                Type type = null;
                String typeStr = rs.getString("Type");
                if (typeStr != null) {
                    try {
                        type = Type.valueOf(typeStr);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum value
                    }
                }

                list.add(new Course(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getDouble("Price"),
                        rs.getString("Information"),
                        rs.getInt("CreatedBy"),
                        rs.getString("Image"),
                        category,
                        level,
                        type
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Course getById(int id) {
        String sql = """
            SELECT
                c.Id, c.Title, c.Price, c.Information, c.CreatedBy, c.Image,
                c.CategoryID, c.Level, c.Type,
                cat.Name AS CategoryName, cat.Type AS CategoryType
            FROM Course c
            JOIN Category cat ON c.CategoryID = cat.Id
            WHERE c.Id = ?
            """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category(
                            rs.getInt("CategoryID"),
                            rs.getString("CategoryName"),
                            rs.getInt("CategoryType")
                    );

                    // Convert string to enum values
                    Level level = null;
                    String levelStr = rs.getString("Level");
                    if (levelStr != null) {
                        try {
                            level = Level.valueOf(levelStr);
                        } catch (IllegalArgumentException e) {
                            // Handle invalid enum value
                        }
                    }

                    Type type = null;
                    String typeStr = rs.getString("Type");
                    if (typeStr != null) {
                        try {
                            type = Type.valueOf(typeStr);
                        } catch (IllegalArgumentException e) {
                            // Handle invalid enum value
                        }
                    }

                    return new Course(
                            rs.getInt("Id"),
                            rs.getString("Title"),
                            rs.getDouble("Price"),
                            rs.getString("Information"),
                            rs.getInt("CreatedBy"),
                            rs.getString("Image"),
                            category,
                            level,
                            type
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Course course) {
        String sql = "UPDATE Course SET Title = ?, Price = ?, Information = ?, CreatedBy = ?, Image = ?, CategoryID = ?, Level = ?, Type = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getTitle());
            stmt.setDouble(2, course.getPrice());
            stmt.setString(3, course.getInformation());
            stmt.setInt(4, course.getCreatedBy());
            stmt.setString(5, course.getImage());
            stmt.setInt(6, course.getCategory().getId());
            stmt.setString(7, course.getLevel() != null ? course.getLevel().name() : null);
            stmt.setString(8, course.getType() != null ? course.getType().name() : null);
            stmt.setInt(9, course.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Course WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalCourse() {
        String sql = "SELECT COUNT(*) FROM Course";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<CourseDto> pagingCourse(int index) {
        List<CourseDto> list = new ArrayList<>();
        String sql = """
            SELECT
                c.Id, c.Title, c.Price, c.Information,
                c.CreatedBy, a.FullName, c.Image,
                c.CategoryID, c.Level, c.Type,
                cat.Name AS CategoryName, cat.Type AS CategoryType
            FROM Course c
            JOIN Account a    ON c.CreatedBy  = a.Id
            JOIN Category cat ON c.CategoryID = cat.Id
            ORDER BY c.Id DESC
            OFFSET ? ROWS FETCH NEXT 10 ROWS ONLY
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, (index - 1) * 10);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(
                            rs.getInt("CategoryID"),
                            rs.getString("CategoryName"),
                            rs.getInt("CategoryType")
                    );

                    // Convert string to enum values
                    Level level = null;
                    String levelStr = rs.getString("Level");
                    if (levelStr != null) {
                        try {
                            level = Level.valueOf(levelStr);
                        } catch (IllegalArgumentException e) {
                            // Handle invalid enum value
                        }
                    }

                    Type type = null;
                    String typeStr = rs.getString("Type");
                    if (typeStr != null) {
                        try {
                            type = Type.valueOf(typeStr);
                        } catch (IllegalArgumentException e) {
                            // Handle invalid enum value
                        }
                    }

                    list.add(new CourseDto(
                            rs.getInt("Id"),
                            rs.getString("Title"),
                            rs.getDouble("Price"),
                            rs.getString("Information"),
                            rs.getInt("CreatedBy"),
                            rs.getString("FullName"),
                            rs.getString("Image"),
                            category,
                            level,
                            type
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during course paging: " + e.getMessage(), e);
        }
        return list;
    }
}