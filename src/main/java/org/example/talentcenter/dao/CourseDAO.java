package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.dto.CourseDto;
import org.example.talentcenter.model.Course;
import org.example.talentcenter.utilities.CourseCategory; // Import your enum

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public void insert(Course course) {
        String sql = "INSERT INTO Course (Title, Price, Information, CreatedBy, Image, Category) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getTitle());
            stmt.setDouble(2, course.getPrice());
            stmt.setString(3, course.getInformation());
            stmt.setInt(4, course.getCreatedBy());
            stmt.setString(5, course.getImage());
            stmt.setInt(6, course.getCategory().ordinal()); // Store enum as String (its name)

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<CourseDto> getAll() {
        List<CourseDto> list = new ArrayList<>();
        String sql = "SELECT c.Id, c.Title, c.Price, c.Information, a.FullName ,c.CreatedBy, c.Image, c.Category, a.FullName " +
                "FROM Course c JOIN Account a ON c.CreatedBy = a.Id";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CourseCategory category = null;
                try {
                    category = CourseCategory.valueOf(rs.getString("Category").toUpperCase());
                } catch (IllegalArgumentException e) {
                    category = CourseCategory.KHAC;
                }

                CourseDto courseDto = new CourseDto(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getDouble("Price"),
                        rs.getString("Information"),
                        rs.getInt("CreatedBy"),
                        rs.getString("FullName"),
                        rs.getString("Image"),
                        category
                );
                list.add(courseDto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Course getById(int id) {
        String sql = "SELECT Id, Title, Price, Information, CreatedBy, Image, Category FROM Course WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CourseCategory category = null;
                    CourseCategory[] categories = CourseCategory.values();
                    try {
                        category = categories[ rs.getInt( "Category" )];
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid CourseCategory value from DB: " + rs.getString("Category"));
                        category = CourseCategory.KHAC;
                    }

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
        String sql = "UPDATE Course SET Title = ?, Price = ?, Information = ?, CreatedBy = ?, Image = ?, Category = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getTitle());
            stmt.setDouble(2, course.getPrice());
            stmt.setString(3, course.getInformation());
            stmt.setInt(4, course.getCreatedBy());
            stmt.setString(5, course.getImage());
            stmt.setInt(6, course.getCategory().ordinal());
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
        String sql = "SELECT c.Id, c.Title, c.Price, c.Information, a.FullName, c.CreatedBy, c.Image, c.Category, a.FullName " +
                "FROM Course c JOIN Account a ON c.CreatedBy = a.Id " +
                "ORDER BY c.Id " +
                "OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, (index - 1) * 5);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CourseCategory category = null;
                    CourseCategory[] categories = CourseCategory.values();
                    try {
                        category = categories[ rs.getInt( "Category" )];
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid CourseCategory value from DB: " + rs.getString("Category"));
                        category = CourseCategory.KHAC;
                    }

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
}


