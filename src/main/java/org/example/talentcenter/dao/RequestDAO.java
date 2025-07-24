/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-06-15
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestDAO {
    /**
     * tạo yêu cầu tạo tài khoản học viên và lưu vào bảng Request.
     *
     * <p>Loại yêu cầu (TypeId) được gán cố định là 6 - tương ứng với yêu cầu tạo tài khoản.</p>
     * <p>Thông tin họ tên, email và số điện thoại được gộp vào trường Reason theo định dạng: "name|email|phone".</p>
     * <p>Trạng thái ban đầu là "Chờ xử lý", ngày tạo là thời điểm hiện tại (GETDATE()).</p>
     *
     * @param senderId ID người gửi yêu cầu (sale)
     * @param name     Họ tên học viên
     * @param email    Email học viên
     * @param phone    Số điện thoại học viên
     * @return true nếu insert thành công, false nếu có lỗi xảy ra
     */
    public boolean sendCreateAccountRequest(int senderId, String name, String email, String phone, int courseId, int consultationId) {
        String sql = """
                    INSERT INTO Request (TypeId, SenderId, Reason, Status, CreatedAt) 
                    VALUES (6, ?, ?, ?, GETDATE())
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, senderId);
            stmt.setString(2, name + "|" + email + "|" + phone + "|" + courseId+"|" + consultationId);
            stmt.setString(3, "Chờ xử lý");

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách tất cả yêu cầu tạo tài khoản học viên đang ở trạng thái "Chờ xử lý".
     *
     * <p>Chỉ truy vấn các bản ghi trong bảng Request có {@code TypeID = 6} (đơn yêu cầu cấp tài khoản) và {@code Status = 'Chờ xử lý'}.</p>
     * <p>Mỗi yêu cầu được ánh xạ thành một {@code Map<String, String>} chứa 3 khóa:
     * {@code "id"} (mã yêu cầu), {@code "reason"} (nội dung lý do), {@code "sender"} (tên người gửi).</p>
     *
     * @return Danh sách các yêu cầu chưa xử lý, mỗi yêu cầu là một Map với thông tin cơ bản
     * @author Huyen Trang
     */
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

    /**
     * Cập nhật trạng thái yêu cầu thành "Đã xử lý" theo ID.
     *
     * @param requestId ID của yêu cầu
     * @return true nếu cập nhật thành công, ngược lại false
     * @author Huyen Trang
     */
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

    /**
     * Lấy chi tiết yêu cầu tạo tài khoản theo ID (chỉ khi đang ở trạng thái "Chờ xử lý").
     *
     * @param requestId ID của yêu cầu
     * @return đối tượng Request nếu tìm thấy, ngược lại trả về null
     * @author Huyen Trang
     */
    public Request getRequestById(int requestId) {
        String sql = "SELECT r.Id, r.Reason, r.SenderId, rt.TypeName " +
                "FROM Request r " +
                "JOIN RequestType rt ON r.TypeID = rt.TypeID " +
                "WHERE r.Id = ? AND r.TypeID = 6 AND r.Status = N'Chờ xử lý'";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requestId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    String fullReason = rs.getString("Reason");
                    String type = rs.getString("TypeName");

                    //tách chuỗi reason theo dấu |
                    String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];

                    if (type != null) {
                        switch (type) {
                            case "Đơn xin nghỉ phép":
                                if (parts.length >= 2) {
                                    request.setReason(parts[1]); // lý do chính
                                    request.setOffDate(LocalDate.parse(parts[0]));  // ngày nghỉ phép
                                } else {
                                    request.setReason(fullReason);
                                }
                                break;
                            case "Đơn xin đổi lịch dạy":
                                if (parts.length >= 5) {
                                    request.setReason(parts[4]); //lý do chính
                                    request.setScheduleId(Integer.parseInt(parts[3])); //lịch muốn đổi
                                    request.setFromDate(LocalDate.parse(parts[0])); //ngày gốc
                                    request.setToDate(LocalDate.parse(parts[1])); // ngày muốn đổi đến
                                    request.setSlot(Integer.parseInt(parts[2])); // slot muốn đổi đến
                                } else {
                                    request.setReason(fullReason);
                                }
                                break;
                            default:
                                if (parts.length >= 4) {
                                    request.setCourseName(parts[0]); //tên khoá học
                                    request.setParentPhone(parts[1]);//sdt phụ huynh
                                    request.setPhoneNumber(parts[2]);//sdt hs
                                    request.setReason(parts[3]);// lý do
                                } else {
                                    request.setReason(fullReason);
                                }
                                break;
                        }
                    } else {
                        request.setReason(fullReason);
                    }
                    request.setSenderID(rs.getInt("SenderId"));
                    return request;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm một yêu cầu mới vào bảng Request với trạng thái mặc định "Chờ xử lý".
     *
     * @param request đối tượng Request chứa thông tin cần thêm
     * @return true nếu thêm thành công, ngược lại false
     * @author Huyen Trang
     */
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

    /**
     * Đếm tổng số yêu cầu của một người gửi.
     *
     * @param senderId ID của người gửi
     * @return Tổng số yêu cầu
     */
    public int getTotalRequestCountBySenderId(int senderId) {
        String sql = "SELECT COUNT(*) FROM Request WHERE SenderId = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, senderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy chi tiết một yêu cầu theo ID, bao gồm cả thông tin người gửi và xử lý lý do theo loại đơn.
     *
     * @param requestId ID của yêu cầu cần lấy
     * @return Đối tượng Request đã phân tích đầy đủ hoặc {@code null} nếu không tìm thấy
     * @author Huyen Trang
     */
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
                    request.setSenderEmail(rs.getString("SenderEmail"));
                    request.setPhoneNumber(rs.getString("SenderPhone"));
                    request.setTypeName(rs.getString("TypeName"));
                    request.setStatus(rs.getString("Status"));
                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));

                    Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    if (createdAt != null) {
                        request.setCreatedAt(new java.util.Date(createdAt.getTime()));
                    }

                    String typeName = rs.getString("TypeName");
                    String fullReason = rs.getString("Reason");
                    String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];

                    switch (typeName) {
                        case "Đơn xin chuyển lớp":
                            if (parts.length >= 4) {
                                request.setCourseName(parts[0]);           // lớp hiện tại
                                request.setParentPhone(parts[1]);          // sđt phụ huynh
                                request.setPhoneNumber(parts[2]);          // sđt học sinh
                                request.setReason(parts[3]);               // lý do
                                if (parts.length > 4 && parts[4].startsWith("TRANSFER_TO_CLASS_ID:")) {
                                    String transferClassId = parts[4].split(":")[1];
                                    String classNameSql = "SELECT Name FROM Classrooms WHERE id = ?";
                                    try (PreparedStatement classStmt = conn.prepareStatement(classNameSql)) {
                                        classStmt.setInt(1, Integer.parseInt(transferClassId));
                                        ResultSet classRs = classStmt.executeQuery();
                                        if (classRs.next()) {
                                            String targetClassName = classRs.getString("Name");
                                            request.setReason(parts[3] + "|TARGET_CLASS:" + targetClassName);
                                        }
                                    }
                                }
                            } else {
                                request.setReason(fullReason);
                            }
                            break;

                        case "Đơn xin nghỉ phép":
                            // Format: date|reason
                            if (parts.length >= 2) {
                                request.setOffDate(LocalDate.parse(parts[0]));
                                request.setReason(parts[1]);
                            } else {
                                request.setReason(fullReason);
                            }
                            break;

                        case "Đơn xin đổi lịch dạy":
                            // Format: from|to|slot|scheduleId|reason
                            if (parts.length >= 5) {
                                request.setFromDate(LocalDate.parse(parts[0]));
                                request.setToDate(LocalDate.parse(parts[1]));
                                request.setSlot(Integer.parseInt(parts[2]));
                                request.setScheduleId(Integer.parseInt(parts[3]));
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

                    return request;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Cập nhật trạng thái, phản hồi và thời gian phản hồi của một yêu cầu.
     *
     * @param requestId    ID của yêu cầu
     * @param status       Trạng thái mới (VD: "Đã xử lý", "Từ chối")
     * @param responseNote Nội dung phản hồi
     * @param responseAt   Thời điểm phản hồi
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}
     * @author Huyen Trang
     */
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

    /**
     * Lấy danh sách các loại đơn mà học sinh có thể gửi (ID từ 1 đến 5).
     *
     * @return Danh sách đối tượng Request chứa TypeID và TypeName
     * @author Huyen Trang
     */
    public ArrayList<Request> getStudentRequestType() {
        ArrayList<Request> list = new ArrayList<>();
        String sql = "SELECT TypeID, TypeName FROM RequestType WHERE TypeID IN (1,3,4,5)";

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


    /**
     * Đếm số lượng yêu cầu (trừ yêu cầu tạo tài khoản) đã được xử lý trong tuần hiện tại.
     *
     * @return Số lượng yêu cầu đã duyệt hoặc từ chối trong tuần này
     * @author Huyen Trang
     */
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

    /**
     * Đếm số lượng yêu cầu đang chờ xử lý (loại trừ yêu cầu tạo tài khoản).
     *
     * @return Số lượng yêu cầu có trạng thái "Chờ xử lý"
     * @author Huyen Trang
     */
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

    /**
     * Đếm số học sinh chưa có tài khoản (đang có yêu cầu tạo tài khoản ở trạng thái "Chờ xử lý").
     *
     * @return Số lượng yêu cầu tạo tài khoản chưa xử lý
     * @author Huyen Trang
     */
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

    public ArrayList<Request> getAllAbsenceRequests() {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
                 SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt,
                        rt.TypeName, acc.FullName AS SenderName, role.Name AS SenderRole
                 FROM Request r
                 JOIN RequestType rt ON r.TypeID = rt.TypeID
                 JOIN Account acc ON r.SenderId = acc.Id
                 JOIN Role role ON acc.RoleId = role.Id
                 WHERE r.TypeID = 3
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
                    String fullReason = rs.getString("Reason");
                    request.setReason(fullReason);
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

    /**
     * Lấy danh sách đơn xin nghỉ học (TypeID = 3) theo trạng thái
     */
    public ArrayList<Request> getAbsenceRequestsByStatus(String status) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
                 SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt,
                        rt.TypeName, acc.FullName AS SenderName, role.Name AS SenderRole
                 FROM Request r
                 JOIN RequestType rt ON r.TypeID = rt.TypeID
                 JOIN Account acc ON r.SenderId = acc.Id
                 JOIN Role role ON acc.RoleId = role.Id
                 WHERE r.TypeID = 3 AND r.Status = ?
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
                    String fullReason = rs.getString("Reason");
                    request.setReason(fullReason);
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

    /**
     * Tìm kiếm đơn xin nghỉ học (TypeID = 3) theo keyword (tên học sinh, lý do, trạng thái)
     */
    public ArrayList<Request> searchAbsenceRequests(String keyword) {
        ArrayList<Request> requests = new ArrayList<>();
        String sql = """
                 SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt,
                        rt.TypeName, acc.FullName AS SenderName, role.Name AS SenderRole
                 FROM Request r
                 JOIN RequestType rt ON r.TypeID = rt.TypeID
                 JOIN Account acc ON r.SenderId = acc.Id
                 JOIN Role role ON acc.RoleId = role.Id
                 WHERE r.TypeID = 3 AND (
                    acc.FullName LIKE ? OR r.Reason LIKE ? OR r.Status LIKE ?
                 )
                 ORDER BY r.CreatedAt DESC
                """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));
                    request.setSenderName(rs.getString("SenderName"));
                    request.setSenderRole(rs.getString("SenderRole"));
                    String fullReason = rs.getString("Reason");
                    request.setReason(fullReason);
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

    /**
     * Lấy danh sách yêu cầu của học sinh với bộ lọc và phân trang.
     *
     * @param senderId ID của học sinh
     * @param keyword  Từ khóa tìm kiếm (có thể null)
     * @param typeId   ID loại đơn (có thể null)
     * @param status   Trạng thái (có thể null)
     * @param offset   Vị trí bắt đầu
     * @param limit    Số lượng bản ghi
     * @return Danh sách yêu cầu đã lọc
     */
    public ArrayList<Request> getStudentRequestsFiltered(int senderId, String keyword, Integer typeId, String status, int offset, int limit) {
        ArrayList<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                    SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt, rt.TypeName
                    FROM Request r JOIN RequestType rt ON r.TypeID = rt.TypeID
                    WHERE r.SenderId = ?
                """);

        ArrayList<Object> params = new ArrayList<>();
        params.add(senderId);

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (rt.TypeName LIKE ? OR r.Reason LIKE ? OR r.Status LIKE ? OR r.Response LIKE ?) ");
            String searchPattern = "%" + keyword + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        if (typeId != null) {
            sql.append(" AND r.TypeID = ? ");
            params.add(typeId);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.Status = ? ");
            params.add(status);
        }

        sql.append(" ORDER BY r.CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(limit);

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));
                    String fullReason = rs.getString("Reason");
                    String[] parts = fullReason != null ? fullReason.split("\\|") : new String[0];
                    String extractedReason = parts.length >= 4 ? parts[3] : fullReason;
                    request.setReason(extractedReason);
                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    request.setTypeName(rs.getString("TypeName"));
                    request.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * Đếm tổng số yêu cầu của học sinh với bộ lọc.
     *
     * @param senderId ID của học sinh
     * @param keyword  Từ khóa tìm kiếm (có thể null)
     * @param typeId   ID loại đơn (có thể null)
     * @param status   Trạng thái (có thể null)
     * @return Tổng số yêu cầu khớp
     */
    public int countStudentRequestsFiltered(int senderId, String keyword, Integer typeId, String status) {
        StringBuilder sql = new StringBuilder("""
                    SELECT COUNT(*)
                    FROM Request r JOIN RequestType rt ON r.TypeID = rt.TypeID
                    WHERE r.SenderId = ?
                """);

        ArrayList<Object> params = new ArrayList<>();
        params.add(senderId);

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (rt.TypeName LIKE ? OR r.Reason LIKE ? OR r.Status LIKE ? OR r.Response LIKE ?) ");
            String searchPattern = "%" + keyword + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        if (typeId != null) {
            sql.append(" AND r.TypeID = ? ");
            params.add(typeId);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.Status = ? ");
            params.add(status);
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy tất cả các loại đơn cho manager (trừ đơn tạo tài khoản và đơn nghỉ học).
     *
     * @return Danh sách các loại đơn
     */
    public ArrayList<Request> getAllRequestTypesForManager() {
        ArrayList<Request> requestTypes = new ArrayList<>();
        String sql = "SELECT TypeID, TypeName FROM RequestType WHERE TypeID NOT IN (3, 6)"; // Exclude absence and account creation
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
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
     * Lấy danh sách yêu cầu của manager với bộ lọc và phân trang.
     *
     * @param keyword      Từ khóa tìm kiếm
     * @param typeFilter   Tên loại đơn
     * @param statusFilter Trạng thái
     * @param offset       Vị trí bắt đầu
     * @param limit        Số lượng bản ghi
     * @return Danh sách yêu cầu đã lọc
     */
    public ArrayList<Request> getManagerRequestsFiltered(String keyword, String typeFilter, String statusFilter, int offset, int limit) {
        ArrayList<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                     SELECT r.Id, r.SenderId, r.Reason, r.Status, r.CreatedAt, r.Response, r.ResponseAt,
                            rt.TypeName, acc.FullName AS SenderName, role.Name AS SenderRole
                     FROM Request r
                     JOIN RequestType rt ON r.TypeID = rt.TypeID
                     JOIN Account acc ON r.SenderId = acc.Id
                     JOIN Role role ON acc.RoleId = role.Id
                     WHERE r.TypeID NOT IN (3, 6)
                """);

        ArrayList<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (acc.FullName LIKE ? OR rt.TypeName LIKE ? OR r.Reason LIKE ?) ");
            String searchPattern = "%" + keyword + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        if (typeFilter != null && !typeFilter.isEmpty()) {
            sql.append(" AND rt.TypeName = ? ");
            params.add(typeFilter);
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND r.Status = ? ");
            params.add(statusFilter);
        }

        sql.append(" ORDER BY r.CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
        params.add(offset);
        params.add(limit);

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setId(rs.getInt("Id"));
                    request.setSenderID(rs.getInt("SenderId"));
                    request.setSenderName(rs.getString("SenderName"));
                    request.setSenderRole(rs.getString("SenderRole"));

                    String fullReason = rs.getString("Reason");
                    String typeName = rs.getString("TypeName");
                    String senderRole = rs.getString("SenderRole");
                    String extractedReason = fullReason; // Default to full reason

                    if (fullReason != null && !fullReason.trim().isEmpty() && typeName != null) {
                        String[] parts = fullReason.split("\\|");
                        switch (typeName.trim()) {
                            case "Đơn xin nghỉ phép":
                                if (parts.length >= 2) {
                                    extractedReason = parts[1];
                                }
                                break;
                            case "Đơn xin đổi lịch dạy":
                                if (parts.length >= 5) {
                                    extractedReason = parts[4];
                                }
                                break;
                            case "Đơn xin chuyển lớp":
                            case "Đơn khiếu nại về giảng viên":
                            case "Đơn xin bảo lưu":
                            case "Đơn xin cấp lại chứng chỉ":
                            case "Đơn khác":
                                if (parts.length >= 4) {
                                    extractedReason = parts[3];
                                }
                                break;
                            default:
                                extractedReason = fullReason;
                                break;
                        }
                    }

                    if (extractedReason != null) {
                        request.setReason(extractedReason.replaceAll("<[^>]*>", "").trim());
                    } else {
                        request.setReason("");
                    }

                    request.setResponse(rs.getString("Response"));
                    request.setResponseAt(rs.getTimestamp("ResponseAt"));
                    request.setStatus(rs.getString("Status"));
                    request.setTypeName(rs.getString("TypeName"));
                    request.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * Đếm tổng số yêu cầu của manager với bộ lọc.
     *
     * @param keyword      Từ khóa tìm kiếm
     * @param typeFilter   Tên loại đơn
     * @param statusFilter Trạng thái
     * @return Tổng số yêu cầu khớp
     */
    public int countManagerRequestsFiltered(String keyword, String typeFilter, String statusFilter) {
        StringBuilder sql = new StringBuilder("""
                    SELECT COUNT(*)
                    FROM Request r
                    JOIN RequestType rt ON r.TypeID = rt.TypeID
                    JOIN Account acc ON r.SenderId = acc.Id
                    JOIN Role role ON acc.RoleId = role.Id
                    WHERE r.TypeID NOT IN (3, 6)
                """);

        ArrayList<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (acc.FullName LIKE ? OR rt.TypeName LIKE ? OR r.Reason LIKE ?) ");
            String searchPattern = "%" + keyword + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        if (typeFilter != null && !typeFilter.isEmpty()) {
            sql.append(" AND rt.TypeName = ? ");
            params.add(typeFilter);
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND r.Status = ? ");
            params.add(statusFilter);
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}