package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Request;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestDAO {
    public boolean sendCreateAccountRequest(int senderId, String name, String email, String phone) {
        String formattedReason = name + "|" + email + "|" + phone;
        String sql = "INSERT INTO Request (Type, SenderId, Reason, Status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "CreateStudentAccount");
            stmt.setInt(2, senderId);
            stmt.setString(3, formattedReason);
            stmt.setString(4, "Chờ xử lý");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, String>> getAllAccountRequests() {
        List<Map<String, String>> requests = new ArrayList<>();
        String sql = "SELECT r.Id, r.Reason, u.FullName AS SenderName FROM Request r JOIN [Account] u ON r.SenderId = u.Id WHERE r.Type = 'CreateStudentAccount' AND r.Status = N'Chờ xử lý'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("id", rs.getString("Id"));
                map.put("reason", rs.getString("Reason"));
                map.put("sender", rs.getString("SenderName"));
                requests.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public boolean markAsCreated(int requestId) {
        String sql = "UPDATE Request SET Status = 'Đã xử lý' WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Request getRequestById(int requestId) {
        String sql = "SELECT r.Id, r.Reason, r.SenderId " +
                "FROM Request r " +
                "WHERE r.Id = ? AND r.Type = 'CreateStudentAccount' AND r.Status = N'Chờ xử lý'";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requestId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setReason(rs.getString("Reason"));
                    request.setSenderID(rs.getInt("SenderId"));
                    return request;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean insert(Request transferRequest) {
        String combinedReason = transferRequest.getCourseName() + "|" +
                transferRequest.getParentPhone() + "|" +
                transferRequest.getPhoneNumber() + "|" +
                transferRequest.getReason();

        String sql = "INSERT INTO Request (Type, SenderId, Reason, Status, CreatedAt) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Đơn xin chuyển lớp");
            stmt.setInt(2, transferRequest.getSenderID());
            stmt.setString(3, combinedReason);
            stmt.setString(4, "Chờ xử lý");
            Timestamp timestamp = new Timestamp(transferRequest.getCreatedAt().getTime());
            stmt.setTimestamp(5, timestamp);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<Request> getRequestBySenderId(int senderId) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = "SELECT Id, Type, SenderId, Reason, Status, CreatedAt FROM Request WHERE SenderId = ? order by CreatedAt DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, senderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setType(rs.getString("Type"));
                    request.setSenderID(rs.getInt("SenderId"));
                    String fullReason = rs.getString("Reason");
                    String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];
                    String extractedReason = parts.length > 0 ? parts[parts.length - 1] : "";

                    request.setReason(extractedReason);

                    request.setStatus(rs.getString("Status"));
                    Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    if (createdAt != null) {
                        request.setCreatedAt(new java.util.Date(createdAt.getTime()));
                    }

                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

}
