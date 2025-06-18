package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Student;
import org.example.talentcenter.model.StudentSchedule;
import java.sql.*;
import java.util.*;

import java.util.ArrayList;

public class StudentScheduleDAO {
    public ArrayList<StudentSchedule> getScheduleByStudentId(int studentId) {
        List<StudentSchedule> list = new ArrayList<>();
        String sql = "SELECT s.Date, s.StartTime, s.EndTime, r.Code AS RoomCode, " +
                "c.Name AS ClassName, co.Title AS CourseTitle " +
                "FROM Schedule s " +
                "JOIN ClassRooms c ON s.ClassRoomId = c.Id " +
                "JOIN Room r ON s.RoomId = r.Id " +
                "JOIN Course co ON c.CourseId = co.Id " +
                "WHERE c.TeacherId = ? " +
                "ORDER BY s.Date, s.StartTime";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                student = new Student();
                student.setId(rs.getInt("Id"));
                student.setName(rs.getString("FullName"));
                student.setParentPhone(rs.getString("parentPhone"));
                student.setAccountId(rs.getString("AccountId"));
                student.setEnrollmentDate(rs.getDate("EnrollmentDate"));
                student.setClassName(rs.getString("class_name"));
                student.setPhoneNumber(rs.getString("PhoneNumber"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }
}
