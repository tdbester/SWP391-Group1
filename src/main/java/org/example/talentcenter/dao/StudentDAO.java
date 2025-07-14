package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Student;
import org.example.talentcenter.model.StudentSchedule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudentDAO {
    /**
     * Lấy thông tin học sinh dựa theo ID tài khoản.
     *
     * @param accountId ID của tài khoản học sinh
     * @return Đối tượng {@code Student} nếu tìm thấy, ngược lại trả về {@code null}
     * @author Huyen Trang
     */
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

    /**
     * Cập nhật lớp học mới cho học sinh dựa trên tên lớp và accountId.
     *
     * @param studentAccountId ID tài khoản của học sinh
     * @param targetClassName  Tên lớp mục tiêu cần chuyển đến
     * @return {@code true} nếu chuyển lớp thành công, ngược lại {@code false}
     * @author Huyen Trang
     */
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

    /**
     * Lấy danh sách tài khoản học sinh thuộc một lớp cụ thể.
     *
     * @param classId ID của lớp học
     * @return Danh sách {@link Account} của học sinh trong lớp, gồm ID, họ tên và email
     * @author Huyen Trang
     */
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