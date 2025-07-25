package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Account;

import java.sql.*;

public class AccountDAO {

    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM Account WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("Id"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getInt("RoleId")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean updateAccountProfile(int accountId, String fullName, String phoneNumber, String email, String address) {
        String sql = "UPDATE Account SET FullName = ?, PhoneNumber = ?, Email = ?, Address = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, fullName);
            st.setString(2, phoneNumber);
            st.setString(3, email);
            st.setString(4, address);
            st.setInt(5, accountId);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updatePassword(int accountId, String newPassword) {
        String sql = "UPDATE Account SET Password = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, newPassword);
            st.setInt(2, accountId);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isEmailExists(String email, int currentAccountId) {
        String sql = "SELECT COUNT(*) FROM Account WHERE Email = ? AND Id != ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, email);
            st.setInt(2, currentAccountId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean isPhoneExists(String phone, int currentAccountId) {
        String sql = "SELECT COUNT(*) FROM Account WHERE PhoneNumber = ? AND Id != ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, phone);
            st.setInt(2, currentAccountId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Account getAccountByEmail(String email) {
        String sql = "SELECT * FROM Account WHERE Email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("Id"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getInt("RoleId")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean updatePasswordByEmail(String email, String password) {
        String sql = "UPDATE Account SET Password = ? WHERE Email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, password);
            st.setString(2, email);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e);
            e.printStackTrace();
            return false;
        }
    }


    public boolean insertAccount(Account account) {
        String sql = "INSERT INTO Account (FullName, Email, Password, PhoneNumber, Address, RoleId) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, account.getFullName());
            st.setString(2, account.getEmail());
            st.setString(3, account.getPassword());
            st.setString(4, account.getPhoneNumber());
            st.setString(5, account.getAddress());
            st.setInt(6, account.getRoleId());

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public java.util.List<Account> getAllAccounts() {
        java.util.List<Account> accounts = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Account";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("Id"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getInt("RoleId")
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public java.util.List<Account> getAccountsByRole(int roleId) {
        java.util.List<Account> accounts = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Account WHERE RoleId = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, roleId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("Id"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getInt("RoleId")
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM Account WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, accountId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTeacherIdByAccountId(int accountId) throws SQLException {
        String sql = "SELECT Id FROM Teacher WHERE AccountId = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Id");
            }
        }
        return -1; // Không tìm thấy
    }

    public boolean createStudentAccount(String password, String name, String email, String phone, int consultationId) {

        // Log giá trị truyền vào
        System.out.println("DAO INPUT: " + name + " | " + email + " | " + phone + " | " + password);
        if (password == null || password.trim().isEmpty() ||
                name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty()) {
            System.out.println("ERROR: Một hoặc nhiều trường truyền vào bị null hoặc rỗng!");
            return false;
        }
        String sqlAccount = """
                    INSERT INTO Account (Password, Email, FullName, PhoneNumber, RoleId) 
                    VALUES (?, ?, ?, ?, 2)
                """;

        String sqlStudent = """
                    INSERT INTO Student (AccountId, parentPhone,EnrollmentDate, consultationId)
                    VALUES (?, ?, GETDATE(), ?)
                """;

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            try {
                int accountId;

                try (PreparedStatement stmtAccount = conn.prepareStatement(sqlAccount, Statement.RETURN_GENERATED_KEYS)) {
                    stmtAccount.setString(1, password);
                    stmtAccount.setString(2, email);
                    stmtAccount.setString(3, name);
                    stmtAccount.setString(4, phone);

                    int result = stmtAccount.executeUpdate();
                    if (result == 0) {
                        System.out.println("ERROR: Creating account failed, no rows affected.");
                        throw new SQLException("Creating account failed, no rows affected.");
                    }

                    try (ResultSet generatedKeys = stmtAccount.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            accountId = generatedKeys.getInt(1);
                        } else {
                            System.out.println("ERROR: Creating account failed, no ID obtained.");
                            throw new SQLException("Creating account failed, no ID obtained.");
                        }
                    }
                }

                try (PreparedStatement stmtStudent = conn.prepareStatement(sqlStudent)) {
                    stmtStudent.setInt(1, accountId);
                    stmtStudent.setString(2, phone);
                    stmtStudent.setInt(3, consultationId);
                    stmtStudent.executeUpdate();
                }

                conn.commit();
                System.out.println("DEBUG: createStudentAccount thành công cho email=" + email);
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("ERROR: SQL Exception khi tạo tài khoản: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("ERROR: SQL Exception ngoài cùng khi tạo tài khoản: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUsernameExists(String email) {
        String sql = "SELECT COUNT(*) FROM Account WHERE Email = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getAccountIdByEmail(String email) {
        String sql = "SELECT Id FROM Account WHERE Email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
