package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.StudentClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentClassDAO {
    public ArrayList<StudentClass> getAllStudentClassByStudentId(int studentId) {
        ArrayList<StudentClass> studentClasses = new ArrayList<>();
        String query = """
                SELECT c.Id, c.Name FROM Student s join Student_Class sc on s.id = sc.StudentId
                join ClassRooms c on sc.ClassRoomId = c.Id where s.Id = ?
                """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StudentClass sc = new StudentClass(
                        rs.getInt("Id"),
                        rs.getString("Name")
                );
                studentClasses.add(sc);
            }
            System.out.println("Tìm được số lớp = " + studentClasses.size());
        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return studentClasses;
    }

    public ArrayList<String> getClassNamesByStudentId(int studentId) {
        ArrayList<String> classNames = new ArrayList<>();
        String sql = """
                    SELECT DISTINCT c.Name
                    FROM ClassRooms c
                    JOIN Student_Class sc ON c.Id = sc.ClassRoomId
                    WHERE sc.StudentId = ?
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                classNames.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classNames;
    }

}
