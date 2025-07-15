package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

            ps.setInt(1, accountId);
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

    public List<Teacher> getAll() {
        List<Teacher> list = new ArrayList<>();
        String sql = """
        SELECT t.Id, t.AccountId, t.Department, t.Salary,
               a.FullName, a.PhoneNumber, a.Address, a.Email
        FROM Teacher t
        JOIN Account a ON t.AccountId = a.Id
        ORDER BY t.Id DESC
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(rs.getInt("Id"));
                teacher.setAccountId(rs.getInt("AccountId"));
                teacher.setDepartment(rs.getString("Department"));
                teacher.setSalary(rs.getDouble("Salary"));

                // Set account info if needed
                Account account = new Account();
                account.setId(rs.getInt("AccountId"));
                account.setFullName(rs.getString("FullName"));
                account.setPhoneNumber(rs.getString("PhoneNumber"));
                account.setAddress(rs.getString("Address"));
                account.setEmail(rs.getString("Email"));
                teacher.setAccount(account);

                list.add(teacher);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalTeacher() {
        String sql = "SELECT COUNT(*) FROM Teacher";
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

    public List<Teacher> pagingTeacher(int index) {
        List<Teacher> list = new ArrayList<>();
        String sql = """
        SELECT t.Id, t.AccountId, t.Department, t.Salary,
               a.FullName, a.PhoneNumber, a.Address, a.Email
        FROM Teacher t
        JOIN Account a ON t.AccountId = a.Id
        ORDER BY t.Id DESC
        OFFSET ? ROWS FETCH NEXT 10 ROWS ONLY
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, (index - 1) * 10);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(rs.getInt("Id"));
                teacher.setAccountId(rs.getInt("AccountId"));
                teacher.setDepartment(rs.getString("Department"));
                teacher.setSalary(rs.getDouble("Salary"));

                Account account = new Account();
                account.setId(rs.getInt("AccountId"));
                account.setFullName(rs.getString("FullName"));
                account.setPhoneNumber(rs.getString("PhoneNumber"));
                account.setAddress(rs.getString("Address"));
                account.setEmail(rs.getString("Email"));
                teacher.setAccount(account);

                list.add(teacher);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}