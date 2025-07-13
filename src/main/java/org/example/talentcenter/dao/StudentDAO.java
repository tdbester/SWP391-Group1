package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Student;
import org.example.talentcenter.model.StudentSchedule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

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
                student.setEnrollmentDate(rs.getDate("EnrollmentDate").toLocalDate());
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
                student.setEnrollmentDate(rs.getDate("EnrollmentDate").toLocalDate());
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
    public StudentSchedule getScheduleById(int id) {
        String sql = "SELECT s.*, c.Name as className, c.CourseTitle, r.Code as roomCode " +
                "FROM Schedule s " +
                "LEFT JOIN ClassRoom c ON s.ClassRoomId = c.Id " +
                "LEFT JOIN Room r ON s.RoomId = r.Id " +
                "WHERE s.Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    StudentSchedule schedule = new StudentSchedule();
                    schedule.setId(rs.getInt("Id"));
                    schedule.setDate(rs.getDate("Date").toLocalDate());
                    schedule.setSlotId(rs.getInt("SlotId"));
                    schedule.setClassName(rs.getString("className"));
                    schedule.setCourseTitle(rs.getString("CourseTitle"));
                    schedule.setRoomCode(rs.getString("roomCode"));
                    // Thêm các trường khác nếu cần
                    return schedule;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Account> getStudentsByClassId(int classId) {
        ArrayList<Account> students = new ArrayList<>();
        String sql = "SELECT * FROM Account a join Student s on a.Id = s.AccountId join Student_Class sc on s.Id = sc.StudentId where ClassRoomId = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account acc = new Account();
                acc.setId(rs.getInt("Id"));
                acc.setFullName(rs.getString("FullName"));
                acc.setEmail(rs.getString("Email"));
                students.add(acc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

}
