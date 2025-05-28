package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Consultation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;

public class ConsultationDAO {
    public static Consultation addConsultation(Consultation consultation) {
        int rs = 0;
        String sql = """
                INSERT INTO consultations (full_name, email, phone, course_interest, contacted, created_at)
                VALUES (?, ?, ?, ?, 0, GETDATE())
                """;
        try (Connection conn = DBConnect.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, consultation.getFullName());
            statement.setString(2, consultation.getEmail());
            statement.setString(3, consultation.getPhone());
            statement.setString(4, consultation.getCourseInterest());
            rs = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rs == 0) {
            return null;
        } else {
            return consultation;
        }
    }
}