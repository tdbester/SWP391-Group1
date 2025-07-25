/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-05-29
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-05-29  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Consultation;

import java.sql.*;
import java.util.ArrayList;

public class ConsultationDAO {

    /**
     * Thêm mới một yêu cầu tư vấn.
     * @param consultation Thông tin tư vấn
     * @return true nếu thêm thành công, ngược lại false
     * @author Huyen Trang
     */
    public boolean addConsultation(Consultation consultation) {
        String sql = "INSERT INTO Consultations (FullName, Email, Phone, CourseId, CreatedAt, Status, Note, PaymentStatus) " +
                "VALUES (?, ?, ?, ?, GETDATE(), ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, consultation.getFullName());
            statement.setString(2, consultation.getEmail());
            statement.setString(3, consultation.getPhone());
            statement.setInt(4, consultation.getCourseId());
            statement.setString(5, consultation.getStatus());
            statement.setString(6, consultation.getNote());
            statement.setString(7, consultation.getPaymentStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    consultation.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Lấy thông tin tư vấn theo ID.
     * @param Id mã tư vấn
     * @return Consultation nếu tồn tại, ngược lại null
     * @author Huyen Trang
     */
    public Consultation getById(int Id) {
        String sql = "SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.Status " +
                "FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id WHERE c.Id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getInt("Id"));
                c.setFullName(rs.getString("FullName"));
                c.setEmail(rs.getString("Email"));
                c.setPhone(rs.getString("Phone"));
                c.setCourseId(rs.getInt("CourseId"));
                c.setTitle(rs.getString("Title"));
                c.setStatus(rs.getString("Status"));
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin tư vấn.
     * @param consultation đối tượng cần cập nhật
     * @return true nếu cập nhật thành công, ngược lại false
     * @author Huyen Trang
     */
    public boolean updateConsultation(Consultation consultation) {
        String sql = "UPDATE Consultations SET FullName = ?, Email = ?, Phone = ?, CourseId = ?, Status = ?, Note = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, consultation.getFullName());
            ps.setString(2, consultation.getEmail());
            ps.setString(3, consultation.getPhone());
            ps.setInt(4, consultation.getCourseId());
            ps.setString(5, consultation.getStatus());
            ps.setString(6, consultation.getNote());
            ps.setInt(7, consultation.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa tư vấn theo ID.
     * @param Id mã tư vấn
     * @return true nếu xóa thành công, false nếu thất bại
     * @author Huyen Trang
     */
    public boolean deleteConsultation(int Id) {
        String sql = "DELETE FROM Consultations WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Cập nhật trạng thái của bản ghi tư vấn theo ID.
     * @param Id ID của bản ghi tư vấn
     * @param Status trạng thái mới (VD: "Đang xử lý", "Đã xử lý")
     * @return true nếu cập nhật thành công, ngược lại false
     * @author Huyen Trang
     */
    public boolean updateStatus(int Id, String Status) {
        String sql = "UPDATE Consultations SET Status = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, Status);
            ps.setInt(2, Id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái thanh toán của bản ghi tư vấn theo ID.
     * @param Id ID của bản ghi tư vấn
     * @param Status trạng thái thanh toán mới
     * @return true nếu cập nhật thành công, ngược lại false
     * @author Huyen Trang
     */
    public boolean updatePaymentStatus(int Id, String Status) {
        String sql = "UPDATE Consultations SET PaymentStatus = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, Status);
            ps.setInt(2, Id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái gửi yêu cầu cấp tài khoản của bản ghi tư vấn theo ID.
     * @param consultationId ID của bản ghi tư vấn
     * @param sentStatus trạng thái gửi yêu cầu
     * @return true nếu cập nhật thành công, ngược lại false
     * @author Huyen Trang
     */
    public boolean updateAccountRequestSentStatus(int consultationId, boolean sentStatus) {
        String sql = "UPDATE consultations SET accountRequestSent = ? WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, sentStatus);
            ps.setInt(2, consultationId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách tư vấn có phân trang (paging).
     *
     * @param offset vị trí bắt đầu lấy dữ liệu (dòng đầu tiên)
     * @param limit  số lượng dòng cần lấy
     * @return danh sách đối tượng Consultation theo trang
     * @author Huyen Trang
     */
    public ArrayList<Consultation> getConsultationsWithPaging(int offset, int limit) {
        ArrayList<Consultation> list = new ArrayList<>();
        String sql = "SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.Status, c.Note " +
                "FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id " +
                "ORDER BY c.CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Consultation c = new Consultation(
                        rs.getInt("Id"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Status"),
                        rs.getString("Title"),
                        rs.getString("Note")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách các tư vấn đã được đồng ý.
     *
     * @return danh sách Consultation có trạng thái "Đồng ý"
     * @author Huyen Trang
     */
    public ArrayList<Consultation> getAgreedConsultationsWithPaging(int offset, int limit) {
        ArrayList<Consultation> list = new ArrayList<>();
        String sql = "SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.PaymentStatus, c.AccountRequestSent " +
                "FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id where c.Status = N'Đồng ý' " +
                "ORDER BY c.CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Consultation c = new Consultation(
                        rs.getInt("Id"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Title"),
                        rs.getString("PaymentStatus"),
                        rs.getBoolean("AccountRequestSent")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Đếm tổng số lượt học sinh đã đồng ý tư vấn trong hệ thống.
     *
     * @return tổng số dòng trong bảng Consultations
     * @author Huyen Trang
     */
    public int getTotalAgreedConsultationCount() {
        String sql = "SELECT COUNT(*) FROM Consultations c where c.Status = N'Đồng ý' ";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<Consultation> getAgreedConsultationsFiltered(String keyword, String status, int offset, int limit) {
        ArrayList<Consultation> consultations = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.PaymentStatus, c.AccountRequestSent
                FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id
                WHERE c.Status = N'Đồng ý'
            """);

        ArrayList<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (c.FullName LIKE ? OR c.Email LIKE ? OR c.Phone LIKE ?) ");
            String searchPattern = "%" + keyword + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND c.PaymentStatus = ? ");
            params.add(status);
        }

        sql.append(" ORDER BY c.CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(limit);

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Consultation c = new Consultation(
                            rs.getInt("Id"),
                            rs.getString("FullName"),
                            rs.getString("Email"),
                            rs.getString("Phone"),
                            rs.getString("Title"),
                            rs.getString("PaymentStatus"),
                            rs.getBoolean("AccountRequestSent")
                    );
                    consultations.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    public int countAgreedConsultationsFiltered(String keyword, String status) {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id
                WHERE c.Status = N'Đồng ý'
            """);

        ArrayList<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (c.FullName LIKE ? OR c.Email LIKE ? OR c.Phone LIKE ?) ");
            String searchPattern = "%" + keyword + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND c.PaymentStatus = ? ");
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
    public int countConsultationsFiltered(String keyword, String status, String courseTitle) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id WHERE 1=1 ");
        ArrayList<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (c.FullName LIKE ? OR c.Phone LIKE ? OR c.Email LIKE ? OR c.Note LIKE ?) ");
            String key = "%" + keyword + "%";
            params.add(key); params.add(key); params.add(key); params.add(key);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND c.Status = ? ");
            params.add(status);
        }
        if (courseTitle != null && !courseTitle.isBlank()) {
            sql.append(" AND cs.Title = ? ");
            params.add(courseTitle);
        }
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace();}
        return 0;
    }

    public ArrayList<Consultation> getConsultationsFiltered(String keyword, String status, String courseTitle, int offset, int limit) {
        ArrayList<Consultation> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.Status, c.Note FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id WHERE 1=1 ");
        ArrayList<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (c.FullName LIKE ? OR c.Phone LIKE ? OR c.Email LIKE ? OR c.Note LIKE ?) ");
            String key = "%" + keyword + "%";
            params.add(key); params.add(key); params.add(key); params.add(key);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND c.Status = ? ");
            params.add(status);
        }
        if (courseTitle != null && !courseTitle.isBlank()) {
            sql.append(" AND cs.Title = ? ");
            params.add(courseTitle);
        }
        sql.append("ORDER BY c.CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
        params.add(offset);
        params.add(limit);

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getInt("Id"));
                c.setFullName(rs.getString("FullName"));
                c.setPhone(rs.getString("Phone"));
                c.setEmail(rs.getString("Email"));
                c.setCourseId(rs.getInt("CourseId"));
                c.setTitle(rs.getString("Title"));
                c.setStatus(rs.getString("Status"));
                c.setNote(rs.getString("Note"));
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }


}