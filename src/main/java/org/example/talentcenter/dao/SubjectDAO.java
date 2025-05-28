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
            if (conn == null) {
                System.err.println("Kết nối cơ sở dữ liệu NULL!");
                return subjects;
            }
            System.out.println("Kết nối thành công, thực hiện truy vấn: " + query);

            try (PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                int rowCount = 0;
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getInt("id"));
                    subject.setName(rs.getString("name"));
                    subject.setDescription(rs.getString("description"));
                    subjects.add(subject);
                    rowCount++;
                    System.out.println("Đã thêm: ID=" + subject.getId() + ", Name=" + subject.getName());
                }
                System.out.println("Tổng số bản ghi: " + rowCount);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }
}