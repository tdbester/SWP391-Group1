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


        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return subjects;
    }
}
