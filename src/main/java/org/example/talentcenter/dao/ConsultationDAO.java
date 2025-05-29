package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Consultation;

import java.sql.*;
import java.util.ArrayList;

public class ConsultationDAO {

    // Add consultation to the database
    public static Consultation addConsultation(Consultation consultation) {
        String sql = "INSERT INTO consultations (full_name, email, phone, course_interest, contacted, created_at, status) VALUES (?, ?, ?, ?, 0, GETDATE(), 'Đang xử lý')";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, consultation.getFullName());
            statement.setString(2, consultation.getEmail());
            statement.setString(3, consultation.getPhone());
            statement.setString(4, consultation.getCourseInterest());

            int rs = statement.executeUpdate();
            if (rs > 0) {
                return consultation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Consultation getById(int id) {
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM consultations WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getInt("id"));
                c.setFullName(rs.getString("full_name"));
                c.setEmail(rs.getString("email"));
                c.setPhone(rs.getString("phone"));
                c.setCourseInterest(rs.getString("course_interest"));
                c.setContacted(rs.getBoolean("contacted"));
                c.setStatus(rs.getString("status"));
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // Get all consultations from the database
    public ArrayList<Consultation> getAllConsultations() {
        ArrayList<Consultation> consultations = new ArrayList<>();
        String query = "SELECT * FROM consultations";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Consultation consultation = new Consultation(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("course_interest"),
                        rs.getBoolean("contacted"),
                        rs.getString("status")
                );
                consultations.add(consultation);
            }
            System.out.println("Số bản ghi lấy từ DB: " + consultations.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    // Update consultation
    public boolean updateConsultation(Consultation consultation) {
        String sql = "UPDATE consultations SET full_name = ?, email = ?, phone = ?, course_interest = ?, contacted = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, consultation.getFullName());
            ps.setString(2, consultation.getEmail());
            ps.setString(3, consultation.getPhone());
            ps.setString(4, consultation.getCourseInterest());
            ps.setBoolean(5, consultation.isContacted());
            ps.setString(6, consultation.getStatus());
            ps.setInt(7, consultation.getId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete consultation by ID
    public boolean deleteConsultation(int id) {
        String sql = "DELETE FROM consultations WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    // Search by name, email, or phone number
    public ArrayList<Consultation> searchConsultations(String keyword) {
        ArrayList<Consultation> result = new ArrayList<>();
        String sql = "SELECT * FROM consultations WHERE full_name LIKE ? OR phone LIKE ? OR email LIKE ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getInt("id"));
                c.setFullName(rs.getString("full_name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setCourseInterest(rs.getString("course_interest"));
                c.setContacted(rs.getBoolean("contacted"));
                result.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // filter by contacted status
    public ArrayList<Consultation> filterConsultationsByContactedStatus(String contactedStr) {
        ArrayList<Consultation> result = new ArrayList<>();
        String sql = "SELECT * FROM consultations WHERE contacted = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            boolean contacted = Boolean.parseBoolean(contactedStr);
            ps.setBoolean(1, contacted);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getInt("id"));
                c.setFullName(rs.getString("full_name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setCourseInterest(rs.getString("course_interest"));
                c.setContacted(rs.getBoolean("contacted"));
                result.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // filter by course
    public ArrayList<Consultation> filterConsultationsByCourse(String courseName) {
        ArrayList<Consultation> result = new ArrayList<>();
        String sql = "SELECT * FROM consultations WHERE course_interest = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseName);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getInt("id"));
                c.setFullName(rs.getString("full_name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setCourseInterest(rs.getString("course_interest"));
                c.setContacted(rs.getBoolean("contacted"));
                result.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // update contacted status
    public boolean updateContactedStatus(int id, boolean contacted) {
        String sql = "UPDATE consultations SET contacted = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, contacted);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE consultations SET status = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
