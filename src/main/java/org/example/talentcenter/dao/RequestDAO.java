/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-06-18
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-06-15  | Cù Thị Huyền Trang   | Initial creation
 */

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
        String sql = """
                    INSERT INTO Request (TypeId, SenderId, Reason, Status, CreatedAt) 
                    VALUES (6, ?, ?, ?, GETDATE())
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, senderId);
            stmt.setString(2, name + "|" + email + "|" + phone);
            stmt.setString(3, "Chờ xử lý");

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, String>> getAllAccountRequests() {
        List<Map<String, String>> requests = new ArrayList<>();
        String sql = "SELECT r.Id, r.Reason, a.FullName AS SenderName FROM Request r JOIN Account a ON r.SenderId = a.Id WHERE r.TypeID = 6 AND r.Status = N'Chờ xử lý'";
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
                "WHERE r.Id = ? AND r.TypeID = 6 AND r.Status = N'Chờ xử lý'";

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

    public boolean insert(Request request) {
        String sql = "INSERT INTO Request (TypeID, SenderId, Reason, Status, CreatedAt) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, request.getTypeId());
            stmt.setInt(2, request.getSenderID());
            stmt.setString(3, request.getReason());
            stmt.setString(4, "Chờ xử lý");
            Timestamp timestamp = new Timestamp(request.getCreatedAt().getTime());
            stmt.setTimestamp(5, timestamp);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== METHODS CHO DANH SÁCH (CHỈ HIỂN THỊ LÝ DO) =====

    public ArrayList<Request> getAllRequest() {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
                 SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt,
                                                  rt.TypeName, acc.FullName AS SenderName, role.Name AS SenderRole
                                           FROM Request r
                                           JOIN RequestType rt ON r.TypeID = rt.TypeID
                                           JOIN Account acc ON r.SenderId = acc.Id
                                           JOIN Role role ON acc.RoleId = role.Id
                                           WHERE r.TypeID <> 6
                                           ORDER BY r.CreatedAt DESC
                """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));
                    request.setSenderName(rs.getString("SenderName"));
                    request.setSenderRole(rs.getString("SenderRole"));

                    // CHỈ LẤY LÝ DO CHO DANH SÁCH
                    String fullReason = rs.getString("Reason");
                    String extractedReason = "";
                    String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];

                    if (parts.length >= 4) {
                        // Lấy lý do ở vị trí thứ 4 (index 3)
                        extractedReason = parts[3];
                    } else {
                        // Fallback cho format cũ hoặc không đúng
                        if (fullReason != null && fullReason.contains("|TRANSFER_TO_CLASS_ID:")) {
                            extractedReason = fullReason.split("\\|TRANSFER_TO_CLASS_ID:")[0];
                        } else {
                            extractedReason = fullReason != null ? fullReason : "";
                        }
                    }
                    request.setReason(extractedReason);

                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    request.setTypeName(rs.getString("TypeName"));
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

    public ArrayList<Request> getRequestBySenderId(int senderId) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
            SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt, rt.TypeName 
            FROM Request r join RequestType rt on r.TypeID = rt.TypeID 
            WHERE SenderId = ? order by CreatedAt DESC
            """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, senderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));

                    // CHỈ LẤY LÝ DO CHO DANH SÁCH
                    String fullReason = rs.getString("Reason");
                    String extractedReason;
                    String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];

                    if (parts.length >= 4) {
                        extractedReason = parts[3];
                    } else {
                        // Fallback cho format cũ
                        if (fullReason != null && fullReason.contains("|TRANSFER_TO_CLASS_ID:")) {
                            extractedReason = fullReason.split("\\|TRANSFER_TO_CLASS_ID:")[0];
                        } else {
                            extractedReason = fullReason != null ? fullReason : "";
                        }
                    }

                    request.setReason(extractedReason);

                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    request.setTypeName(rs.getString("TypeName"));
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

    public ArrayList<Request> getRequestBySenderIdAndType(int senderId, int typeId) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
            SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt, rt.TypeName 
            FROM Request r 
            JOIN RequestType rt ON r.TypeID = rt.TypeID 
            WHERE r.SenderId = ? AND r.TypeID = ? 
            ORDER BY r.CreatedAt DESC
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, typeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));

                    // CHỈ LẤY LÝ DO CHO DANH SÁCH
                    String fullReason = rs.getString("Reason");
                    String extractedReason;
                    String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];

                    if (parts.length >= 4) {
                        extractedReason = parts[3]; // ✅ CHỈ LẤY LÝ DO
                    } else {
                        // Fallback cho format cũ
                        if (fullReason != null && fullReason.contains("|TRANSFER_TO_CLASS_ID:")) {
                            extractedReason = fullReason.split("\\|TRANSFER_TO_CLASS_ID:")[0];
                        } else {
                            extractedReason = fullReason != null ? fullReason : "";
                        }
                    }

                    request.setReason(extractedReason); // ✅ CHỈ SET MỘT LẦN

                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    request.setTypeName(rs.getString("TypeName"));
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

    public Request getRequestDetailById(int requestId) {
        String sql = """
                    SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt, 
                           rt.TypeName, acc.FullName AS SenderName, acc.Email AS SenderEmail, 
                           acc.PhoneNumber AS SenderPhone, role.Name AS SenderRole
                    FROM Request r
                    JOIN RequestType rt ON r.TypeID = rt.TypeID
                    JOIN Account acc ON r.SenderId = acc.Id
                    JOIN Role role ON acc.RoleId = role.Id
                    WHERE r.Id = ?
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requestId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));
                    request.setSenderName(rs.getString("SenderName"));
                    request.setSenderRole(rs.getString("SenderRole"));

                    // PARSE ĐẦY ĐỦ THÔNG TIN CHO CHI TIẾT
                    String fullReason = rs.getString("Reason");
                    // Format: lớp|sdt_phụ_huynh|sdt_học_sinh|lý_do[|TRANSFER_TO_CLASS_ID:x]
                    String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];

                    if (parts.length >= 4) {
                        request.setCourseName(parts[0]);        // Lớp hiện tại
                        request.setParentPhone(parts[1]);       // SĐT phụ huynh
                        request.setPhoneNumber(parts[2]);       // SĐT học sinh
                        request.setReason(parts[3]);            // Lý do

                        // Xử lý thông tin chuyển lớp nếu có
                        if (parts.length > 4 && parts[4].startsWith("TRANSFER_TO_CLASS_ID:")) {
                            String transferClassId = parts[4].split(":")[1];

                            // Chỉ lấy tên lớp
                            String classNameSql = "SELECT Name FROM Classrooms WHERE id = ?";
                            try (PreparedStatement classStmt = conn.prepareStatement(classNameSql)) {
                                classStmt.setInt(1, Integer.parseInt(transferClassId));
                                ResultSet classRs = classStmt.executeQuery();
                                if (classRs.next()) {
                                    String targetClassName = classRs.getString("Name");
                                    // Thêm tên lớp vào reason
                                    request.setReason(parts[3] + "|TARGET_CLASS:" + targetClassName);
                                }
                            }
                        }
                    } else {
                        // Fallback cho format cũ
                        request.setReason(fullReason);
                    }

                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    request.setTypeName(rs.getString("TypeName"));

                    Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    if (createdAt != null) {
                        request.setCreatedAt(new java.util.Date(createdAt.getTime()));
                    }

                    return request;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean processRequest(int requestId, String status, String responseNote, Timestamp responseAt) {
        String sql = "UPDATE Request SET Status = ?, Response = ?, ResponseAt = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, responseNote);
            stmt.setTimestamp(3, responseAt);
            stmt.setInt(4, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Request> getStudentRequestType() {
        ArrayList<Request> list = new ArrayList<>();
        String sql = "SELECT TypeID, TypeName FROM RequestType WHERE TypeID IN (1, 2,3,4,5)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Request request = new Request();
                request.setTypeId(rs.getInt("TypeID"));
                request.setTypeName(rs.getString("TypeName"));
                list.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getProcessedRequestsThisWeek() {
        String sql = """
                    SELECT COUNT(*) FROM Request 
                    WHERE Status IN (N'Đã duyệt', N'Từ chối') 
                    AND DATEPART(week, ResponseAt) = DATEPART(week, GETDATE())
                    AND YEAR(ResponseAt) = YEAR(GETDATE())
                    AND TypeID <> 6
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getPendingRequests() {
        String sql = "SELECT COUNT(*) FROM Request WHERE Status = N'Chờ xử lý' AND TypeID <> 6";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getStudentsWithoutAccount() {
        String sql = "SELECT COUNT(*) FROM Request WHERE TypeID = 6 AND Status = N'Chờ xử lý'";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<Request> getAllRequestWithPaging(int offset, int limit) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
             SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt,
                    rt.TypeName, acc.FullName AS SenderName, role.Name AS SenderRole
             FROM Request r
             JOIN RequestType rt ON r.TypeID = rt.TypeID
             JOIN Account acc ON r.SenderId = acc.Id
             JOIN Role role ON acc.RoleId = role.Id
             WHERE r.TypeID <> 6
             ORDER BY r.CreatedAt DESC
             OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));
                    request.setSenderName(rs.getString("SenderName"));
                    request.setSenderRole(rs.getString("SenderRole"));

                    // Extract reason đơn giản
                    String fullReason = rs.getString("Reason");
                    String extractedReason = "";
                    if (fullReason != null && !fullReason.trim().isEmpty()) {
                        String[] parts = fullReason.split("\\|");
                        if (parts.length >= 4) {
                            extractedReason = parts[3];
                        } else {
                            extractedReason = fullReason;
                        }
                    }
                    request.setReason(extractedReason);

                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    request.setTypeName(rs.getString("TypeName"));

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

    public int getTotalRequestCount() {
        String sql = "SELECT COUNT(*) FROM Request WHERE TypeID <> 6";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // ✅ TÌM KIẾM THEO TỪ KHÓA
    public ArrayList<Request> searchRequests(String keyword) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
             SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt,
                    rt.TypeName, acc.FullName AS SenderName, role.Name AS SenderRole
             FROM Request r
             JOIN RequestType rt ON r.TypeID = rt.TypeID
             JOIN Account acc ON r.SenderId = acc.Id
             JOIN Role role ON acc.RoleId = role.Id
             WHERE r.TypeID <> 6 
             AND (acc.FullName LIKE ? OR rt.TypeName LIKE ? OR r.Reason LIKE ?)
             ORDER BY r.CreatedAt DESC
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));
                    request.setSenderName(rs.getString("SenderName"));
                    request.setSenderRole(rs.getString("SenderRole"));

                    // Extract reason đơn giản
                    String fullReason = rs.getString("Reason");
                    String extractedReason = "";
                    if (fullReason != null && !fullReason.trim().isEmpty()) {
                        String[] parts = fullReason.split("\\|");
                        if (parts.length >= 4) {
                            extractedReason = parts[3];
                        } else {
                            extractedReason = fullReason;
                        }
                    }
                    request.setReason(extractedReason);

                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    request.setTypeName(rs.getString("TypeName"));

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

    // ✅ FILTER THEO LOẠI ĐƠN
    public ArrayList<Request> filterRequestsByType(String typeName) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
             SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt,
                    rt.TypeName, acc.FullName AS SenderName, role.Name AS SenderRole
             FROM Request r
             JOIN RequestType rt ON r.TypeID = rt.TypeID
             JOIN Account acc ON r.SenderId = acc.Id
             JOIN Role role ON acc.RoleId = role.Id
             WHERE r.TypeID <> 6 AND rt.TypeName = ?
             ORDER BY r.CreatedAt DESC
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, typeName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));
                    request.setSenderName(rs.getString("SenderName"));
                    request.setSenderRole(rs.getString("SenderRole"));

                    // Extract reason đơn giản
                    String fullReason = rs.getString("Reason");
                    String extractedReason = "";
                    if (fullReason != null && !fullReason.trim().isEmpty()) {
                        String[] parts = fullReason.split("\\|");
                        if (parts.length >= 4) {
                            extractedReason = parts[3];
                        } else {
                            extractedReason = fullReason;
                        }
                    }
                    request.setReason(extractedReason);

                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    request.setTypeName(rs.getString("TypeName"));

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

    // ✅ FILTER THEO TRẠNG THÁI
    public ArrayList<Request> filterRequestsByStatus(String status) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
             SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt,
                    rt.TypeName, acc.FullName AS SenderName, role.Name AS SenderRole
             FROM Request r
             JOIN RequestType rt ON r.TypeID = rt.TypeID
             JOIN Account acc ON r.SenderId = acc.Id
             JOIN Role role ON acc.RoleId = role.Id
             WHERE r.TypeID <> 6 AND r.Status = ?
             ORDER BY r.CreatedAt DESC
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));
                    request.setSenderName(rs.getString("SenderName"));
                    request.setSenderRole(rs.getString("SenderRole"));

                    // Extract reason đơn giản
                    String fullReason = rs.getString("Reason");
                    String extractedReason = "";
                    if (fullReason != null && !fullReason.trim().isEmpty()) {
                        String[] parts = fullReason.split("\\|");
                        if (parts.length >= 4) {
                            extractedReason = parts[3];
                        } else {
                            extractedReason = fullReason;
                        }
                    }
                    request.setReason(extractedReason);

                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    request.setTypeName(rs.getString("TypeName"));

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
