package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.TokenForgetPassword;
import org.example.talentcenter.model.User;
import org.example.talentcenter.config.DBConnect;
import java.sql.*;
import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;

public class TokenForgetDAO extends DBConnect {

    public String getFormatDate(LocalDateTime myDateObj){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

    public boolean insertTokenForget(TokenForgetPassword tokenForget) {
        String sql = "INSERT INTO [dbo].[tokenForgetPassword]\n" +
                "           ([token]\n" +
                "           ,[expiryTime]\n" +
                "           ,[isUsed]\n" +
                "           ,[userId])\n" +
                "     VALUES(?, ?, ?, ?)";
        try (Connection con = new DBConnect().getConnection();
             PreparedStatement st = con.prepareStatement(sql)){
            st.setString(1, tokenForget.getToken());
            st.setTimestamp(2, Timestamp.valueOf(getFormatDate(tokenForget.getExpiryTime())));
            st.setBoolean(3, tokenForget.isUsed());
            st.setInt(4, tokenForget.getUserId());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting token: " + e);
            e.printStackTrace();
        }
        return false;
    }

    public TokenForgetPassword getTokenPassword(String token) {
        String sql = "Select * from [tokenForgetPassword] where token = ?";
        try (Connection con = new DBConnect().getConnection();
             PreparedStatement st = con.prepareStatement(sql)){
            st.setString(1, token);
            ResultSet rs = st.executeQuery();
            if (rs.next()) { // Changed from while to if
                return new TokenForgetPassword(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getBoolean("isUsed"),
                        rs.getString("token"),
                        rs.getTimestamp("expiryTime").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting token: " + e);
            e.printStackTrace();
        }
        return null;
    }

    // Fixed: Changed return type from void to boolean
    public boolean updateStatus(TokenForgetPassword token) {
        System.out.println("Updating token status: " + token.getToken() + ", isUsed: " + token.isUsed());
        String sql = "UPDATE [dbo].[tokenForgetPassword]\n"
                + "   SET [isUsed] = ?\n"
                + " WHERE token = ?";
        try (Connection con = new DBConnect().getConnection();
             PreparedStatement st = con.prepareStatement(sql)){
            st.setBoolean(1, token.isUsed());
            st.setString(2, token.getToken());
            int rowsAffected = st.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating token status: " + e);
            e.printStackTrace();
        }
        return false;
    }
}