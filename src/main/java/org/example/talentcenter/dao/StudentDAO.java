package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Student;

import java.sql.*;

public class StudentDAO {
    public Student getStudentByAccountId(int accountId) {
        Student student = null;
        String query = """
                    SELECT s.Id, a.FullName, s.parentPhone, a.PhoneNumber,
                                     s.AccountId, s.EnrollmentDate, c.Name AS class_name
                              FROM Student s
                              JOIN Account a ON s.AccountId = a.id
                              JOIN Student_Class sc ON s.id = sc.StudentId
                              JOIN ClassRooms c ON sc.ClassRoomId = c.id
                              WHERE s.Id = ?
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                student = new Student();
                student.setId(rs.getInt("Id"));
                student.setName(rs.getString("FullName"));
                student.setParentPhone(rs.getString("parentPhone"));
                student.setAccountId(rs.getString("AccountId"));
                student.setEnrollmentDate(rs.getDate("EnrollmentDate"));
                student.setClassName(rs.getString("Name"));
                student.setPhoneNumber(rs.getString("PhoneNumber"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }
}
