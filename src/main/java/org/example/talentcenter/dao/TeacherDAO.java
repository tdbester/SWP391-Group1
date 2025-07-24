package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.ClassRooms;
import org.example.talentcenter.model.Course;
import org.example.talentcenter.model.Teacher;
import org.example.talentcenter.utilities.Level;
import org.example.talentcenter.utilities.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

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

    public List<Teacher> getAll() throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        String sql = """
            SELECT t.Id, a.FullName 
            FROM Teacher t 
            JOIN Account a ON t.AccountId = a.Id 
            ORDER BY a.FullName
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(rs.getInt("Id"));
                teacher.setFullName(rs.getString("FullName"));
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public Teacher getTeacherById(int teacherId) {
        Teacher teacher = null;
        String query = """
        SELECT t.Id, t.AccountId, t.Department, t.Salary,
               a.FullName, a.Email, a.PhoneNumber, a.Address, a.Password, a.RoleId
        FROM Teacher t
        JOIN Account a ON t.AccountId = a.Id
        WHERE t.Id = ?
    """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Create Account object with full information
                Account account = new Account(
                        rs.getInt("AccountId"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getInt("RoleId")
                );

                // Create Teacher object and set Account
                teacher = new Teacher();
                teacher.setId(rs.getInt("Id"));
                teacher.setAccountId(rs.getInt("AccountId"));
                teacher.setDepartment(rs.getString("Department"));
                teacher.setSalary(rs.getDouble("Salary"));
                teacher.setAccount(account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teacher;
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

    public boolean updateTeacher(Teacher teacher) {
        String sql = """
        UPDATE Teacher SET Department = ?, Salary = ? WHERE Id = ?;
        UPDATE Account SET FullName = ?, PhoneNumber = ?, Address = ?, Email = ? WHERE Id = ?;
        """;

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            // Update Teacher table
            try (PreparedStatement ps1 = conn.prepareStatement("UPDATE Teacher SET Department = ?, Salary = ? WHERE Id = ?")) {
                ps1.setString(1, teacher.getDepartment());
                ps1.setDouble(2, teacher.getSalary());
                ps1.setInt(3, teacher.getId());
                ps1.executeUpdate();
            }

            // Update Account table
            try (PreparedStatement ps2 = conn.prepareStatement("UPDATE Account SET FullName = ?, PhoneNumber = ?, Address = ?, Email = ? WHERE Id = ?")) {
                ps2.setString(1, teacher.getAccount().getFullName());
                ps2.setString(2, teacher.getAccount().getPhoneNumber());
                ps2.setString(3, teacher.getAccount().getAddress());
                ps2.setString(4, teacher.getAccount().getEmail());
                ps2.setInt(5, teacher.getAccountId());
                ps2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ClassRooms> getClassesByTeacherId(int teacherId) {
        List<ClassRooms> classes = new ArrayList<>();
        String sql = """
        SELECT c.Id, c.Name, c.CourseId, c.TeacherId
        FROM ClassRooms c
        WHERE c.TeacherId = ?
        ORDER BY c.Name
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ClassRooms classRoom = new ClassRooms();
                classRoom.setId(rs.getInt("Id"));
                classRoom.setName(rs.getString("Name"));
                classRoom.setCourseId(rs.getInt("CourseId"));
                classRoom.setTeacherId(rs.getInt("TeacherId"));
                classes.add(classRoom);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classes;
    }

    public List<Course> getCoursesByTeacherId(int teacherId) {
        List<Course> courses = new ArrayList<>();
        String sql = """
        SELECT DISTINCT co.Id, co.Title, co.Price, co.Information, co.Level, co.Type
        FROM Course co
        JOIN ClassRooms c ON co.Id = c.CourseId
        WHERE c.TeacherId = ?
        ORDER BY co.Title
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Convert string to enum values
                Level level = null;
                String levelStr = rs.getString("Level");
                if (levelStr != null) {
                    try {
                        level = Level.valueOf(levelStr);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum value
                    }
                }

                Type type = null;
                String typeStr = rs.getString("Type");
                if (typeStr != null) {
                    try {
                        type = Type.valueOf(typeStr);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum value
                    }
                }

                Course course = new Course();
                course.setId(rs.getInt("Id"));
                course.setTitle(rs.getString("Title"));
                course.setPrice(rs.getDouble("Price"));
                course.setInformation(rs.getString("Information"));
                course.setLevel(level);
                course.setType(type);
                courses.add(course);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }
}