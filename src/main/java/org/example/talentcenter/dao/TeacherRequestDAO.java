package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Request;
import org.example.talentcenter.model.RequestStatistics;
import org.example.talentcenter.model.RequestType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TeacherRequestDAO {

    /**
     * Thêm mới một đơn yêu cầu
     */
    public boolean insertRequest(Request request) {
        String sql = """
        INSERT INTO Request (SenderId, Reason, Status, CreatedAt, TypeID)
        VALUES (?, ?, ?, GETDATE(), ?)
        """;


        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, request.getSenderID());
            ps.setString(2, request.getReason());
            ps.setString(3, request.getStatus());
            ps.setInt(4, getRequestTypeId(request.getTypeName()));

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
        // Mapping từ type code sang tên trong database
        String typeName;
        switch (type) {
            case "leave":
                typeName = "Xin nghỉ phép";
                break;
            case "schedule_change":
                typeName = "Thay đổi lịch dạy";
                break;
            case "room_change":
                typeName = "Thay đổi lớp học";
                break;
            case "other":
                typeName = "Khác";
                break;
            default:
                typeName = type; // Nếu đã là tên đầy đủ
        }

        String sql = "SELECT TypeID FROM RequestType WHERE TypeName = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, typeName);
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
            SELECT r.Id, r.SenderId, r.Reason, r.Status, r.Response,
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
                request.setTypeName(rs.getString("TypeName"));
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
    public boolean hasLeaveRequestForDate(int teacherId, LocalDate date) {
        String sql = """
        SELECT COUNT(*) FROM Request r
        JOIN Teacher t ON r.SenderId = t.AccountId
        WHERE t.Id = ? AND r.TypeID = ? 
        AND r.Status IN ('Pending', 'Approved')
        AND CAST(r.CreatedAt AS DATE) = ?
    """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ps.setInt(2, getRequestTypeId("Xin nghỉ phép"));
            ps.setDate(3, Date.valueOf(date));

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
     * Lấy thông tin chi tiết của một đơn từ theo ID
     */
    public Request getRequestById(int requestId) {
        Request request = null;
        String sql = """
            SELECT r.Id, r.SenderId, r.Reason, r.Status, r.Response,
                   r.CreatedAt, r.ResponseAt, r.ProcessedBy, rt.TypeName
            FROM Request r
            LEFT JOIN RequestType rt ON r.TypeID = rt.TypeID
            WHERE r.Id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                request = new Request();
                request.setId(rs.getInt("Id"));
                request.setTypeName(rs.getString("TypeName"));
                request.setSenderID(rs.getInt("SenderId"));
                request.setReason(rs.getString("Reason"));
                request.setStatus(rs.getString("Status"));
                request.setResponse(rs.getString("Response"));
                request.setCreatedAt(rs.getTimestamp("CreatedAt"));
                request.setResponseAt(rs.getTimestamp("ResponseAt"));
                request.setProcessedBy(rs.getInt("ProcessedBy"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return request;
    }

    /**
     * Lấy danh sách đơn từ theo bộ lọc
     */
    public ArrayList<Request> getFilteredRequests(int senderId, String requestType, String status) {
        ArrayList<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT r.Id, r.SenderId, r.Reason, r.Status, r.Response,
                   r.CreatedAt, r.ResponseAt, r.ProcessedBy, rt.TypeName
            FROM Request r
            LEFT JOIN RequestType rt ON r.TypeID = rt.TypeID
            WHERE r.SenderId = ?
        """);

        ArrayList<Object> params = new ArrayList<>();
        params.add(senderId);

        // Thêm điều kiện lọc theo loại đơn
        if (requestType != null && !requestType.trim().isEmpty() && !"all".equals(requestType)) {
            sql.append(" AND rt.TypeName = ?");
            params.add(requestType);
        }

        // Thêm điều kiện lọc theo trạng thái
        if (status != null && !status.trim().isEmpty() && !"all".equals(status)) {
            sql.append(" AND r.Status = ?");
            params.add(status);
        }

        sql.append(" ORDER BY r.CreatedAt DESC");

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Request request = new Request();
                request.setId(rs.getInt("Id"));
                request.setTypeName(rs.getString("TypeName"));
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
     * Tìm kiếm đơn từ theo từ khóa
     */
    public ArrayList<Request> searchRequests(int senderId, String keyword) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
            SELECT r.Id, r.SenderId, r.Reason, r.Status, r.Response,
                   r.CreatedAt, r.ResponseAt, r.ProcessedBy, rt.TypeName
            FROM Request r
            LEFT JOIN RequestType rt ON r.TypeID = rt.TypeID
            WHERE r.SenderId = ? 
            AND (r.Reason LIKE ? OR rt.TypeName LIKE ? OR r.Status LIKE ? OR r.Response LIKE ?)
            ORDER BY r.CreatedAt DESC
        """;

        String searchPattern = "%" + keyword + "%";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, senderId);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setString(5, searchPattern);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Request request = new Request();
                request.setId(rs.getInt("Id"));
                request.setTypeName(rs.getString("TypeName"));
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
     * Xóa đơn từ (chỉ cho phép xóa đơn từ ở trạng thái Pending)
     */
    public boolean deleteRequest(int requestId) {
        String sql = "DELETE FROM Request WHERE Id = ? AND Status = 'Pending'";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Lấy tên người xử lý đơn từ
     */
    public String getProcessorName(int processorId) {
        String name = null;
        String sql = """
            SELECT a.FullName 
            FROM Account a 
            WHERE a.Id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, processorId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                name = rs.getString("FullName");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }

    /**
     * Lấy thống kê đơn từ theo trạng thái
     */
    public RequestStatistics getRequestStatistics(int senderId) {
        RequestStatistics stats = new RequestStatistics();
        String sql = """
            SELECT 
                COUNT(*) as total,
                SUM(CASE WHEN Status = 'Pending' THEN 1 ELSE 0 END) as pending,
                SUM(CASE WHEN Status = 'Approved' THEN 1 ELSE 0 END) as approved,
                SUM(CASE WHEN Status = 'Rejected' THEN 1 ELSE 0 END) as rejected
            FROM Request 
            WHERE SenderId = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, senderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                stats.setTotal(rs.getInt("total"));
                stats.setPending(rs.getInt("pending"));
                stats.setApproved(rs.getInt("approved"));
                stats.setRejected(rs.getInt("rejected"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    /**
     * Lấy danh sách các loại đơn từ
     */
    public ArrayList<RequestType> getAllRequestTypes() {
        ArrayList<RequestType> types = new ArrayList<>();
        String sql = "SELECT TypeID, TypeName FROM RequestType ORDER BY TypeName";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequestType type = new RequestType();
                type.setTypeId(rs.getInt("TypeID"));
                type.setTypeName(rs.getString("TypeName"));
                types.add(type);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return types;
    }

    /**
     * Kiểm tra quyền sở hữu đơn từ
     */
    public boolean isRequestOwner(int requestId, int senderId) {
        String sql = "SELECT COUNT(*) FROM Request WHERE Id = ? AND SenderId = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ps.setInt(2, senderId);

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
     * Lấy danh sách đơn từ gần đây (7 ngày gần nhất)
     */
    public ArrayList<Request> getRecentRequests(int senderId, int limit) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
            SELECT TOP (?) r.Id, r.SenderId, r.Reason, r.Status, r.Response,
                   r.CreatedAt, r.ResponseAt, r.ProcessedBy, rt.TypeName
            FROM Request r
            LEFT JOIN RequestType rt ON r.TypeID = rt.TypeID
            WHERE r.SenderId = ? 
            AND r.CreatedAt >= DATEADD(day, -7, GETDATE())
            ORDER BY r.CreatedAt DESC
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, senderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Request request = new Request();
                request.setId(rs.getInt("Id"));
                request.setTypeName(rs.getString("TypeName"));
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
}