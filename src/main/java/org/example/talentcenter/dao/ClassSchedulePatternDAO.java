package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.ClassSchedulePattern;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassSchedulePatternDAO {
    public void insertPattern(ClassSchedulePattern pattern) throws SQLException {
        String sql = "INSERT INTO ClassSchedulePattern (ClassRoomId, StartDate, EndDate, SlotId, DayOfWeek) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pattern.getClassRoomId());
            stmt.setDate(2, java.sql.Date.valueOf(pattern.getStartDate()));
            stmt.setDate(3, java.sql.Date.valueOf(pattern.getEndDate()));
            stmt.setInt(4, pattern.getSlotId());
            stmt.setInt(5, pattern.getDayOfWeek());
            stmt.executeUpdate();
        }
    }
    public List<ClassSchedulePattern> getPatternsByClassRoom(int classRoomId) throws SQLException {
        String sql = "SELECT * FROM ClassSchedulePattern WHERE ClassRoomId = ?";
        List<ClassSchedulePattern> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classRoomId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ClassSchedulePattern pattern = new ClassSchedulePattern();
                    pattern.setId(rs.getInt("Id"));
                    pattern.setClassRoomId(rs.getInt("ClassRoomId"));
                    pattern.setStartDate(rs.getDate("StartDate").toLocalDate());
                    pattern.setEndDate(rs.getDate("EndDate").toLocalDate());
                    pattern.setSlotId(rs.getInt("SlotId"));
                    pattern.setDayOfWeek(rs.getInt("DayOfWeek"));
                    list.add(pattern);
                }
            }
        }
        return list;
    }
}
