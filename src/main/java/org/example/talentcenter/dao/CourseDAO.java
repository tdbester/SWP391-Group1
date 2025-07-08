package org.example.talentcenter.dao;

import org.example.talentcenter.model.Course;
import org.example.talentcenter.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseDAO {
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> subjects = new ArrayList<>();
        String query = "SELECT Id, Title, Price, Information, CreatedBy FROM Course";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course subject = new Course(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getDouble("Price"),
                        rs.getString("Information"),
                        rs.getInt("CreatedBy")
                );
                subjects.add(subject);
            }
            System.out.println("Số lượng khóa học: " + subjects.size());
        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return subjects;
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
