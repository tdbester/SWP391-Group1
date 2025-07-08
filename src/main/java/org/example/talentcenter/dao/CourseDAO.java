package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.dto.CourseDto;
import org.example.talentcenter.model.Course;
import org.example.talentcenter.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public void insert(Course course) {
        String sql = "INSERT INTO Course (Title, Price, Information, CreatedBy, Image, CategoryID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getTitle());
            stmt.setDouble(2, course.getPrice());
            stmt.setString(3, course.getInformation());
            stmt.setInt(4, course.getCreatedBy());
            stmt.setString(5, course.getImage());
            stmt.setInt(6, course.getCategory().getId());

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
                c.CategoryID,
                cat.Name AS CategoryName, cat.Type AS CategoryType,
                a.FullName
            FROM Course c
            JOIN Account a    ON c.CreatedBy  = a.Id
            JOIN Category cat ON c.CategoryID = cat.Id
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
                list.add(new Course(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getDouble("Price"),
                        rs.getString("Information"),
                        rs.getInt("CreatedBy"),
                        rs.getString("Image"),
                        category
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
                c.CategoryID,
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
                    return new Course(
                            rs.getInt("Id"),
                            rs.getString("Title"),
                            rs.getDouble("Price"),
                            rs.getString("Information"),
                            rs.getInt("CreatedBy"),
                            rs.getString("Image"),
                            category
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Course course) {
        String sql = "UPDATE Course SET Title = ?, Price = ?, Information = ?, CreatedBy = ?, Image = ?, CategoryID = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getTitle());
            stmt.setDouble(2, course.getPrice());
            stmt.setString(3, course.getInformation());
            stmt.setInt(4, course.getCreatedBy());
            stmt.setString(5, course.getImage());
            stmt.setInt(6, course.getCategory().getId());
            stmt.setInt(7, course.getId());

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
                c.CategoryID,
                cat.Name AS CategoryName, cat.Type AS CategoryType
            FROM Course c
            JOIN Account a    ON c.CreatedBy  = a.Id
            JOIN Category cat ON c.CategoryID = cat.Id
            ORDER BY c.Id DESC 
            OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, (index - 1) * 5);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(
                            rs.getInt("CategoryID"),
                            rs.getString("CategoryName"),
                            rs.getInt("CategoryType")
                    );
                    list.add(new CourseDto(
                            rs.getInt("Id"),
                            rs.getString("Title"),
                            rs.getDouble("Price"),
                            rs.getString("Information"),
                            rs.getInt("CreatedBy"),
                            rs.getString("FullName"),
                            rs.getString("Image"),
                            category
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during course paging: " + e.getMessage(), e);
        }
        return list;
    }

    public Course getCourseById(int courseId) {
        String query = "SELECT Id, Title, Price, Information, CreatedBy FROM Course WHERE Id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Course(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getDouble("Price"),
                        rs.getString("Information"),
                        rs.getInt("CreatedBy")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL getCourseById: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

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

}
