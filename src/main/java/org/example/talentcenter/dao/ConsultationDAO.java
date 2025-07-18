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
        String sql = "INSERT INTO Consultations (FullName, Email, Phone, CourseId, CreatedAt, Status) VALUES (?, ?, ?, ?, GETDATE(), N'Đang xử lý'); SELECT @@IDENTITY AS ID";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, consultation.getFullName());
            statement.setString(2, consultation.getEmail());
            statement.setString(3, consultation.getPhone());
            statement.setInt(4, consultation.getCourseId());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                consultation.setId(rs.getInt("ID"));
                return true;
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
        String sql = "UPDATE Consultations SET FullName = ?, Email = ?, Phone = ?, CourseId = ?, Status = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, consultation.getFullName());
            ps.setString(2, consultation.getEmail());
            ps.setString(3, consultation.getPhone());
            ps.setInt(4, consultation.getCourseId());
            ps.setString(5, consultation.getStatus());
            ps.setInt(6, consultation.getId());
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
     * Tìm kiếm tư vấn theo từ khoá (họ tên, số điện thoại hoặc email).
     * @param keyword từ khoá tìm kiếm
     * @return danh sách tư vấn khớp từ khoá
     * @author Huyen Trang
     */
    public ArrayList<Consultation> searchConsultations(String keyword) {
        ArrayList<Consultation> result = new ArrayList<>();
        String sql = "SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.Status " +
                "FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id " +
                "WHERE c.FullName LIKE ? OR c.Phone LIKE ? OR c.Email LIKE ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

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
                result.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Lọc danh sách tư vấn theo tên khoá học.
     * @param CourseTitle tên khoá học
     * @return danh sách tư vấn thuộc khoá học đó
     * @author Huyen Trang
     */
    public ArrayList<Consultation> filterConsultationsByCourse(String CourseTitle) {
        ArrayList<Consultation> result = new ArrayList<>();
        String sql = "SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.Status " +
                "FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id WHERE cs.Title = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, CourseTitle);

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
                result.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Lọc danh sách tư vấn theo trạng thái xử lý.
     * @param status trạng thái (VD: "Đang xử lý", "Đã xử lý", ...)
     * @return danh sách tư vấn có trạng thái tương ứng
     * @author Huyen Trang
     */
    public ArrayList<Consultation> filterConsultationsByStatus(String status) {
        ArrayList<Consultation> result = new ArrayList<>();
        String sql = "SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.Status " +
                "FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id WHERE c.Status = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);

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
                result.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
     * Lấy danh sách tư vấn có phân trang (paging).
     *
     * @param offset vị trí bắt đầu lấy dữ liệu (dòng đầu tiên)
     * @param limit  số lượng dòng cần lấy
     * @return danh sách đối tượng Consultation theo trang
     * @author Huyen Trang
     */
    public ArrayList<Consultation> getConsultationsWithPaging(int offset, int limit) {
        ArrayList<Consultation> list = new ArrayList<>();
        String sql = "SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.Status " +
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
                        rs.getString("Title")
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
    public ArrayList<Consultation> getAgreedConsultations() {
        ArrayList<Consultation> list = new ArrayList<>();
        String sql = "SELECT Id, FullName, Email, Phone, Status FROM Consultations WHERE Status = N'Đồng ý'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getInt("Id"));
                c.setFullName(rs.getString("FullName"));
                c.setEmail(rs.getString("Email"));
                c.setPhone(rs.getString("Phone"));
                c.setStatus(rs.getString("Status"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Đếm tổng số lượt tư vấn trong hệ thống.
     *
     * @return tổng số dòng trong bảng Consultations
     * @author Huyen Trang
     */
    public int getTotalConsultationCount() {
        String sql = "SELECT COUNT(*) FROM Consultations";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
