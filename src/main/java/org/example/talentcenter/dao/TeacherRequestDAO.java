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
        String sql = "INSERT INTO Request (TypeID, SenderId, Reason, Status, CreatedAt) VALUES (?, ?, ?, ?, GETDATE())";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, request.getTypeId());
            stmt.setInt(2, request.getSenderID());
            stmt.setString(3, request.getReason());
            stmt.setString(4, request.getStatus() != null ? request.getStatus() : "Chờ xử lý");

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Request> getTeacherRequestType() {
        ArrayList<Request> requestTypes = new ArrayList<>();
        String sql = "SELECT TypeID, TypeName FROM RequestType WHERE TypeID IN (5,7,8)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Request requestType = new Request();
                requestType.setTypeId(rs.getInt("TypeID"));
                requestType.setTypeName(rs.getString("TypeName"));
                requestTypes.add(requestType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestTypes;
    }

    /**
     * Lấy TypeID từ bảng RequestType
     */
    public int getRequestTypeId(String typeName) {
        if (typeName == null || typeName.trim().isEmpty()) {
            return 0;
        }
        String sql = "SELECT TypeID FROM RequestType WHERE TypeName = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, typeName.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int typeId = rs.getInt("TypeID");
                return typeId;
            } else {
                System.err.println("Không tìm thấy TypeID cho TypeName: " + typeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
                request.setStatus(rs.getString("Status"));
                request.setResponse(rs.getString("Response"));
                request.setCreatedAt(rs.getTimestamp("CreatedAt"));
                request.setResponseAt(rs.getTimestamp("ResponseAt"));
                request.setProcessedBy(rs.getInt("ProcessedBy"));

                String fullReason = rs.getString("Reason");
                String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];
                String type = request.getTypeName();

                if (type != null) {
                    switch (type) {
                        case "Đơn xin nghỉ phép":
                            // Format: date|reason
                            if (parts.length >= 2) {
                                request.setReason(parts[1]);
                            } else {
                                request.setReason(fullReason);
                            }
                            break;

                        case "Đơn xin đổi lịch dạy":
                            // Format: from|to|slot|scheduleId|reason
                            if (parts.length >= 5) {
                                request.setReason(parts[4]);
                            } else if (parts.length >= 1) {
                                request.setReason(parts[parts.length - 1]);
                            } else {
                                request.setReason(fullReason);
                            }
                            break;


                        default:
                            if (parts.length >= 4) {
                                request.setCourseName(parts[0]);
                                request.setParentPhone(parts[1]);
                                request.setPhoneNumber(parts[2]);
                                request.setReason(parts[3]);
                            } else {
                                request.setReason(fullReason);
                            }
                            break;
                    }
                } else {
                    request.setReason(fullReason);
                }

                requests.add(request);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
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
                String fullReason = rs.getString("Reason");
                String type = rs.getString("TypeName");
                String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];

                if (type != null) {
                    switch (type) {
                        case "Đơn xin nghỉ phép":
                            if (parts.length >= 2) {
                                request.setReason(parts[1]);
                                request.setOffDate(LocalDate.parse(parts[0]));  // nhớ set vào model
                            } else {
                                request.setReason(fullReason);
                            }
                            break;

                        case "Đơn xin đổi lịch dạy":
                            if (parts.length >= 5) {
                                request.setReason(parts[4]);
                                request.setScheduleId(Integer.parseInt(parts[3]));  // nhớ set vào model
                                request.setFromDate(LocalDate.parse(parts[0]));
                                request.setToDate(LocalDate.parse(parts[1]));
                                request.setSlot(Integer.parseInt(parts[2]));
                            } else {
                                request.setReason(fullReason);
                            }
                            break;

                        default:
                            if (parts.length >= 4) {
                                request.setCourseName(parts[0]);
                                request.setParentPhone(parts[1]);
                                request.setPhoneNumber(parts[2]);
                                request.setReason(parts[3]);
                            } else {
                                request.setReason(fullReason);
                            }
                            break;
                    }
                } else {
                    request.setReason(fullReason);
                }

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