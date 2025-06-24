package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Teacher;

import java.sql.*;

public class TeacherDAO {

    public Teacher getTeacherById(int teacherId) {
        Teacher teacher = null;
        String query = """
            SELECT t.Id, a.FullName, a.PhoneNumber, a.Address,
                   t.AccountId, t.Department, t.Salary
            FROM Teacher t
            JOIN Account a ON t.AccountId = a.Id
            WHERE t.Id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                teacher = new Teacher();
                teacher.setId(rs.getInt("Id"));
                teacher.setAccountId(rs.getInt("AccountId"));
                teacher.setDepartment(rs.getString("Department"));
                teacher.setSalary(rs.getDouble("Salary"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    public Teacher getTeacherByAccountId(int accountId) {
        Teacher teacher = null;
        String query = """
        SELECT t.Id, a.FullName, a.PhoneNumber, a.Address,
               t.AccountId, t.Department, t.Salary
        FROM Teacher t
        JOIN Account a ON t.AccountId = a.Id
        WHERE t.AccountId = ?
    """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, accountId);  // ✅ Dùng accountId thay vì teacherId
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                teacher = new Teacher();
                teacher.setId(rs.getInt("Id"));
                teacher.setAccountId(rs.getInt("AccountId"));
                teacher.setDepartment(rs.getString("Department"));
                teacher.setSalary(rs.getDouble("Salary"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teacher;
    }

}