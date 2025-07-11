package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Student;

import java.sql.*;

public class StudentDAO {
    public Student getStudentById(int accountId) {
        Student student = null;
        String query = """
                    SELECT s.Id, a.FullName, s.parentPhone, a.PhoneNumber,
                                     s.AccountId, s.EnrollmentDate
                              FROM Student s
                              JOIN Account a ON s.AccountId = a.id
                              WHERE s.AccountId = ?
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
                student.setAccountId(rs.getInt("AccountId"));
                student.setEnrollmentDate(rs.getDate("EnrollmentDate"));
                student.setPhoneNumber(rs.getString("PhoneNumber"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }

    public Student getStudentByStudentId(int studentId) {
        Student student = new Student();
        String query = """
                    SELECT * FROM Student s WHERE s.Id = ?
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                student = new Student();
                student.setId(rs.getInt("Id"));
                student.setName(rs.getString("FullName"));
                student.setParentPhone(rs.getString("parentPhone"));
                student.setAccountId(rs.getInt("AccountId"));
                student.setEnrollmentDate(rs.getDate("EnrollmentDate"));
                student.setClassName(rs.getString("class_name"));
                student.setPhoneNumber(rs.getString("PhoneNumber"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public boolean transferStudentToClass(int studentAccountId, String targetClassName) {
        String sql = """
        UPDATE StudentClass 
        SET ClassroomID = (
            SELECT ClassroomID FROM Classroom WHERE ClassroomName = ?
        )
        WHERE StudentID = (
            SELECT Id FROM Student WHERE AccountID = ?
        )
    """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, targetClassName);  // ✅ Dùng String
            stmt.setInt(2, studentAccountId);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected in transfer: " + rowsAffected);
            System.out.println("Transferred to class: " + targetClassName);

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi chuyển lớp: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
