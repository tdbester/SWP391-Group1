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

    // Add consultation to the database
    public static Consultation addConsultation(Consultation consultation) {
        String sql = "INSERT INTO Consultations (FullName, Email, Phone, CourseId, CreatedAt, Status) VALUES (?, ?, ?, ?, GETDATE(), 'Đang xử lý')";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, consultation.getFullName());
            statement.setString(2, consultation.getEmail());
            statement.setString(3, consultation.getPhone());
            statement.setInt(4, consultation.getCourseId());

            int rs = statement.executeUpdate();
            if (rs > 0) {
                return consultation;
            }
            System.out.println("Inserting consultation: " + consultation.getFullName() + ", courseId: " + consultation.getCourseId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    // Get all Consultations from the database
    public ArrayList<Consultation> getAllConsultations() {
        ArrayList<Consultation> Consultations = new ArrayList<>();
        String query = "SELECT c.Id, c.FullName, c.Email, c.Phone, c.CourseId, cs.Title, c.Status " +
                "FROM Consultations c JOIN Course cs ON c.CourseId = cs.Id order by CreatedAt desc";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Consultation consultation = new Consultation(
                        rs.getInt("Id"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Status"),
                        rs.getString("Title")
                );
                Consultations.add(consultation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Consultations;
    }

    // Update consultation
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

    // Delete consultation by ID
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

    // Search by name, Email, or Phone number
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

    // filter by Course
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

    // filter by Course
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
