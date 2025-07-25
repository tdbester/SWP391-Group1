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


    /**
     * Check if email already exists in the system
     */
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Account WHERE Email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Add a new teacher to the system
     * Creates both Account and Teacher records in a transaction
     */
    public boolean addTeacher(Teacher teacher) {
        String sqlAccount = "INSERT INTO Account (FullName, Email, Password, PhoneNumber, Address, RoleId) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlTeacher = "INSERT INTO Teacher (AccountId, Department, Salary) VALUES (?, ?, ?)";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            try {
                int accountId;

                // Insert Account first
                try (PreparedStatement ps1 = conn.prepareStatement(sqlAccount, Statement.RETURN_GENERATED_KEYS)) {
                    ps1.setString(1, teacher.getAccount().getFullName());
                    ps1.setString(2, teacher.getAccount().getEmail());
                    ps1.setString(3, teacher.getAccount().getPassword());
                    ps1.setString(4, teacher.getAccount().getPhoneNumber());
                    ps1.setString(5, teacher.getAccount().getAddress());
                    ps1.setInt(6, 3); // RoleId 3 for Teacher (based on database schema)

                    int result = ps1.executeUpdate();
                    if (result == 0) {
                        throw new SQLException("Creating account failed, no rows affected.");
                    }

                    try (ResultSet generatedKeys = ps1.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            accountId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Creating account failed, no ID obtained.");
                        }
                    }
                }

                // Insert Teacher record
                try (PreparedStatement ps2 = conn.prepareStatement(sqlTeacher)) {
                    ps2.setInt(1, accountId);
                    ps2.setString(2, teacher.getDepartment());
                    ps2.setDouble(3, teacher.getSalary());
                    ps2.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a teacher from the system
     * Checks for constraints (classes assigned) before deletion
     */
    public boolean deleteTeacher(int teacherId) {
        // First check if teacher has any classes assigned
        String checkClassesSQL = "SELECT COUNT(*) FROM ClassRooms WHERE TeacherId = ?";
        String deleteTeacherSQL = "DELETE FROM Teacher WHERE Id = ?";
        String deleteAccountSQL = "DELETE FROM Account WHERE Id = ?";

        try (Connection conn = DBConnect.getConnection()) {
            // Check for constraints
            try (PreparedStatement checkStmt = conn.prepareStatement(checkClassesSQL)) {
                checkStmt.setInt(1, teacherId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Teacher has classes assigned, cannot delete
                    return false;
                }
            }

            // Get the accountId before deletion
            int accountId = 0;
            String getAccountIdSQL = "SELECT AccountId FROM Teacher WHERE Id = ?";
            try (PreparedStatement getAccountStmt = conn.prepareStatement(getAccountIdSQL)) {
                getAccountStmt.setInt(1, teacherId);
                ResultSet rs = getAccountStmt.executeQuery();
                if (rs.next()) {
                    accountId = rs.getInt("AccountId");
                }
            }

            if (accountId == 0) {
                return false; // Teacher not found
            }

            conn.setAutoCommit(false);

            try {
                // Delete Teacher record first
                try (PreparedStatement deleteTeacherStmt = conn.prepareStatement(deleteTeacherSQL)) {
                    deleteTeacherStmt.setInt(1, teacherId);
                    deleteTeacherStmt.executeUpdate();
                }

                // Delete Account record
                try (PreparedStatement deleteAccountStmt = conn.prepareStatement(deleteAccountSQL)) {
                    deleteAccountStmt.setInt(1, accountId);
                    deleteAccountStmt.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}