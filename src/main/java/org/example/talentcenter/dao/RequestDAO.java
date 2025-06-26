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

    //Teacher's request
    public boolean insertRequest(Request req) {
        String sql = "INSERT INTO Request (Type, SenderId, Reason, Status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, req.getType());
            ps.setInt(2, req.getSenderID());
            ps.setString(3, req.getReason());
            ps.setString(4, req.getStatus());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Request> getRequestsBySender(int senderId) throws SQLException {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT * FROM Request WHERE SenderId = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, senderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Request req = new Request();
                req.setId(rs.getInt("Id"));
                req.setType(rs.getString("Type"));
                req.setSenderID(rs.getInt("SenderId"));
                req.setReason(rs.getString("Reason"));
                req.setStatus(rs.getString("Status"));
                list.add(req);
            }
        }
        return list;
    }

}

