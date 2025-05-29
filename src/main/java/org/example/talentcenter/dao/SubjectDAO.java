package org.example.talentcenter.dao;

import org.example.talentcenter.model.Subject;
import org.example.talentcenter.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubjectDAO {
    public ArrayList<Subject> getAllSubjects() {
        ArrayList<Subject> subjects = new ArrayList<>();
        String query = "SELECT id, name, description FROM Talent_Center.dbo.subjects";

        try (Connection conn = DBConnect.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"));
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }
}