package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Request;
import org.example.talentcenter.model.RequestType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApprovedRequestDAO {

    /**
     * Lấy danh sách request đã approved của teacher cụ thể với filter
     */
    public List<Request> getApprovedRequestsWithFilterByTeacher(int teacherId, String typeName,
                                                                String dateFrom, String dateTo, String searchKeyword) {
        List<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT r.Id, r.SenderId, r.Reason, r.Status, r.Response,
                   r.CreatedAt, r.ResponseAt, r.ProcessedBy, r.TypeID,
                   rt.TypeName, rt.Description,
                   a.FullName as SenderName,
                   p.FullName as ProcessedByName
            FROM Request r
            JOIN RequestType rt ON r.TypeID = rt.TypeID
            JOIN Account a ON r.SenderId = a.Id
            LEFT JOIN Account p ON r.ProcessedBy = p.Id
            WHERE r.Status = N'Đã duyệt' AND r.SenderId = ?
            """);

        List<Object> params = new ArrayList<>();
        params.add(teacherId);

        // Filter by type name
        if (typeName != null && !typeName.trim().isEmpty() && !typeName.equals("all")) {
            sql.append(" AND rt.TypeName = ?");
            params.add(typeName);
        }

        // Filter by date range
        if (dateFrom != null && !dateFrom.trim().isEmpty()) {
            sql.append(" AND CAST(r.CreatedAt AS DATE) >= ?");
            params.add(dateFrom);
        }

        if (dateTo != null && !dateTo.trim().isEmpty()) {
            sql.append(" AND CAST(r.CreatedAt AS DATE) <= ?");
            params.add(dateTo);
        }

        // Search keyword
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (r.Response LIKE ? OR r.Reason LIKE ? OR a.FullName LIKE ?)");
            String searchPattern = "%" + searchKeyword + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        sql.append(" ORDER BY r.ResponseAt DESC");

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Request request = mapResultSetToRequest(rs);
                requests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }



    /**
     * Lấy request theo ID và chỉ của teacher cụ thể
     */
    public Request getApprovedRequestByIdAndTeacher(int requestId, int teacherId) {
        String sql = """
            SELECT r.Id, r.SenderId, r.Reason, r.Status, r.Response,
                   r.CreatedAt, r.ResponseAt, r.ProcessedBy, r.TypeID,
                   rt.TypeName, rt.Description,
                   a.FullName as SenderName,
                   p.FullName as ProcessedByName
            FROM Request r
            JOIN RequestType rt ON r.TypeID = rt.TypeID
            JOIN Account a ON r.SenderId = a.Id
            LEFT JOIN Account p ON r.ProcessedBy = p.Id
            WHERE r.Id = ? AND r.Status = N'Đã duyệt' AND r.SenderId = ?
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requestId);
            stmt.setInt(2, teacherId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRequest(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    /**
     * Lấy danh sách tất cả request types
     */
    public List<RequestType> getAllRequestTypes() {
        List<RequestType> types = new ArrayList<>();
        String sql = "SELECT TypeID, TypeName, Description FROM RequestType ORDER BY TypeName";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RequestType type = new RequestType();
                type.setTypeId(rs.getInt("TypeID"));
                type.setTypeName(rs.getString("TypeName"));
                type.setDescription(rs.getString("Description"));
                types.add(type);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return types;
    }


    /**
     * Mapping từ ResultSet sang Request object
     */
    private Request mapResultSetToRequest(ResultSet rs) throws SQLException {
        Request request = new Request();
        request.setId(rs.getInt("Id"));
        request.setSenderID(rs.getInt("SenderId"));
        request.setReason(rs.getString("Reason"));
        request.setStatus(rs.getString("Status"));
        request.setResponse(rs.getString("Response"));
        request.setCreatedAt(rs.getTimestamp("CreatedAt"));
        request.setResponseAt(rs.getTimestamp("ResponseAt"));
        request.setProcessedBy(rs.getInt("ProcessedBy"));
        request.setTypeId(rs.getInt("TypeID"));

        // Set additional fields
        request.setTypeName(rs.getString("TypeName"));
        request.setSenderName(rs.getString("SenderName"));
        request.setProcessedByName(rs.getString("ProcessedByName"));

        return request;
    }
}