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
        String sql = "INSERT INTO Course (Title, Price, Information, CreatedBy, Image, CategoryID, Level, Type, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            stmt.setInt(9, course.getStatus());

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
                c.CategoryID, c.Level, c.Type, c.Status,
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
                        type,
                        rs.getInt("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Course> getAllForGuest() {
        List<Course> list = new ArrayList<>();
        String sql = """
            SELECT
                c.Id, c.Title, c.Price, c.Information, c.CreatedBy, c.Image,
                c.CategoryID, c.Level, c.Type, c.Status,
                cat.Name AS CategoryName, cat.Type AS CategoryType,
                a.FullName
            FROM Course c
            JOIN Category cat ON c.CategoryID = cat.Id
            LEFT JOIN Account a ON c.CreatedBy = a.Id
            WHERE c.status = 1
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
                        type,
                        rs.getInt("Status")
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
                c.CategoryID, c.Level, c.Type, c.Status,
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
                            type,
                            rs.getInt("Status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Course course) {
        String sql = "UPDATE Course SET Title = ?, Price = ?, Information = ?, CreatedBy = ?, Image = ?, CategoryID = ?, Level = ?, Type = ?, Status = ? WHERE Id = ?";
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
            stmt.setInt(9, course.getStatus());
            stmt.setInt(10, course.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Course WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No course was deleted.");
        }
    }

    public int getTotalCourseWithFilters(String search, Integer categoryId, String level) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Course c WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // Add search filter
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND c.Title LIKE ?");
            parameters.add("%" + search.trim() + "%");
        }

        // Add category filter
        if (categoryId != null) {
            sql.append(" AND c.CategoryID = ?");
            parameters.add(categoryId);
        }

        // Add level filter
        if (level != null && !level.trim().isEmpty()) {
            sql.append(" AND c.Level = ?");
            parameters.add(level.trim());
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

    public List<CourseDto> pagingCourseWithFilters(int index, String search, Integer categoryId, String level) {
        List<CourseDto> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT
                c.Id, c.Title, c.Price, c.Information,
                c.CreatedBy, a.FullName, c.Image,
                c.CategoryID, c.Level, c.Type, c.Status,
                cat.Name AS CategoryName, cat.Type AS CategoryType
            FROM Course c
            JOIN Account a    ON c.CreatedBy  = a.Id
            JOIN Category cat ON c.CategoryID = cat.Id
            WHERE 1=1
            """);

        List<Object> parameters = new ArrayList<>();

        // Add search filter
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND c.Title LIKE ?");
            parameters.add("%" + search.trim() + "%");
        }

        // Add category filter
        if (categoryId != null) {
            sql.append(" AND c.CategoryID = ?");
            parameters.add(categoryId);
        }

        // Add level filter
        if (level != null && !level.trim().isEmpty()) {
            sql.append(" AND c.Level = ?");
            parameters.add(level.trim());
        }

        sql.append(" ORDER BY c.Id DESC OFFSET ? ROWS FETCH NEXT 10 ROWS ONLY");
        parameters.add((index - 1) * 10);

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(
                            rs.getInt("CategoryID"),
                            rs.getString("CategoryName"),
                            rs.getInt("CategoryType")
                    );

                    // Convert string to enum values
                    Level levelEnum = null;
                    String levelStr = rs.getString("Level");
                    if (levelStr != null) {
                        try {
                            levelEnum = Level.valueOf(levelStr);
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
                            levelEnum,
                            type,
                            rs.getInt("Status")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during course paging: " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * Lấy danh sách các khóa học mới nhất kèm số lượng lớp học tương ứng, giới hạn theo số lượng chỉ định.
     *
     * @param limit Số lượng khóa học tối đa cần lấy.
     * @return Danh sách các đối tượng Course mới nhất.
     * @author Huyen Trang
     */
    public ArrayList<Course> getLatestCourses(int limit) {
        ArrayList<Course> courses = new ArrayList<>();
        String query = """
                    SELECT TOP (?) 
                        c.Id, 
                        c.Title, 
                        c.Price, 
                        c.Information, 
                        c.CreatedBy,
                        COUNT(cr.Id) as ClassCount
                    FROM Course c
                    LEFT JOIN ClassRooms cr ON c.Id = cr.CourseId
                    GROUP BY c.Id, c.Title, c.Price, c.Information, c.CreatedBy
                    ORDER BY c.Id DESC
                """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getDouble("Price"),
                        rs.getString("Information"),
                        rs.getInt("CreatedBy"),
                        rs.getInt("ClassCount")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Get all public courses (status = 1)
     */
    public List<Course> getPublicCourses() {
        List<Course> list = new ArrayList<>();
        String sql = """
            SELECT
                c.Id, c.Title, c.Price, c.Information, c.CreatedBy, c.Image,
                c.CategoryID, c.Level, c.Type, c.Status,
                cat.Name AS CategoryName, cat.Type AS CategoryType,
                a.FullName
            FROM Course c
            JOIN Category cat ON c.CategoryID = cat.Id
            LEFT JOIN Account a ON c.CreatedBy = a.Id
            WHERE c.Status = 1
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
                        type,
                        rs.getInt("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get all public courses as CourseDto for guest access (status = 1)
     */
    public List<CourseDto> getPublicCoursesDto() {
        List<CourseDto> list = new ArrayList<>();
        String sql = """
            SELECT
                c.Id, c.Title, c.Price, c.Information,
                c.CreatedBy, a.FullName, c.Image,
                c.CategoryID, c.Level, c.Type, c.Status,
                cat.Name AS CategoryName, cat.Type AS CategoryType
            FROM Course c
            JOIN Account a    ON c.CreatedBy  = a.Id
            JOIN Category cat ON c.CategoryID = cat.Id
            WHERE c.Status = 1
            ORDER BY c.Id DESC
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
                        type,
                        rs.getInt("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}