package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Request;

import java.sql.*;
import java.util.ArrayList;

public class TeacherRequestDAO {

    /**
     * Thêm mới một đơn yêu cầu
     */
    public boolean insertRequest(Request request) {
        String sql = """
            INSERT INTO Request (Type, SenderId, Reason, Status, CreatedAt, TypeID)
            VALUES (?, ?, ?, ?, GETDATE(), ?)
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, request.getType());
            ps.setInt(2, request.getSenderID());
            ps.setString(3, request.getReason());
            ps.setString(4, request.getStatus());
            ps.setInt(5, getRequestTypeId(request.getType()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Lấy TypeID từ bảng RequestType
     */
    private int getRequestTypeId(String type) {
        String sql = "SELECT TypeID FROM RequestType WHERE TypeName = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("TypeID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1; // Default type
    }

    /**
     * Lấy danh sách đơn yêu cầu theo SenderId
     */
    public ArrayList<Request> getRequestsBySenderId(int senderId) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
            SELECT r.Id, r.Type, r.SenderId, r.Reason, r.Status, r.Response,
                   r.CreatedAt, r.ResponseAt, r.ProcessedBy, rt.TypeName
            FROM Request r
            LEFT JOIN RequestType rt ON r.TypeID = rt.TypeID
            WHERE r.SenderId = ?
            ORDER BY r.CreatedAt DESC
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, senderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Request request = new Request();
                request.setId(rs.getInt("Id"));
                request.setType(rs.getString("Type"));
                request.setSenderID(rs.getInt("SenderId"));
                request.setReason(rs.getString("Reason"));
                request.setStatus(rs.getString("Status"));
                request.setResponse(rs.getString("Response"));
                request.setCreatedAt(rs.getTimestamp("CreatedAt"));
                request.setResponseAt(rs.getTimestamp("ResponseAt"));
                request.setProcessedBy(rs.getInt("ProcessedBy"));

                requests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    /**
     * Cập nhật trạng thái đơn yêu cầu
     */
    public boolean updateRequestStatus(int requestId, String status, String response, int processedBy) {
        String sql = """
            UPDATE Request 
            SET Status = ?, Response = ?, ProcessedBy = ?, ResponseAt = GETDATE()
            WHERE Id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, response);
            ps.setInt(3, processedBy);
            ps.setInt(4, requestId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Kiểm tra xem giáo viên đã gửi đơn nghỉ cho ngày này chưa
     */
    public boolean hasLeaveRequestForDate(int teacherId, java.time.LocalDate date) {
        String sql = """
            SELECT COUNT(*) FROM Request r
            JOIN Teacher t ON r.SenderId = t.AccountId
            WHERE t.Id = ? AND r.Type = 'leave' 
            AND r.Status IN ('Pending', 'Approved')
            AND CAST(r.CreatedAt AS DATE) = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ps.setDate(2, Date.valueOf(date));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}