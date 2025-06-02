package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Account;
import java.sql.*;

public class AccountDAO {

    /**
     * Get account by ID
     */
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

    /**
     * Update account profile
     */
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

    /**
     * Update password
     */
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

    /**
     * Check if email exists (excluding current account)
     */
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

    /**
     * Check if phone exists (excluding current account)
     */
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

    /**
     * Get account by email (for login check)
     */
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

    /**
     * Update password by email (for forgot password feature)
     */
    public boolean updatePasswordByEmail(String email, String password) {
        String sql = "UPDATE Account SET Password = ? WHERE Email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)){
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

    /**
     * Insert new account
     */
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

    /**
     * Get all accounts
     */
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

    /**
     * Get accounts by role
     */
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

    /**
     * Delete account by ID
     */
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
}