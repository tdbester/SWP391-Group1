package org.example.talentcenter.dao;

import org.example.talentcenter.model.Schedule;
import java.sql.*;
import java.util.*;

public class ScheduleDAO {
    private Connection conn;

    public ScheduleDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Schedule> getScheduleByTeacherId(int teacherId) throws SQLException {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT s.Date, s.StartTime, s.EndTime, r.Code AS RoomCode, " +
                "c.Name AS ClassName, co.Title AS CourseTitle " +
                "FROM Schedule s " +
                "JOIN ClassRooms c ON s.ClassRoomId = c.Id " +
                "JOIN Teacher t ON c.TeacherId = t.Id " +
                "JOIN Room r ON s.RoomId = r.Id " +
                "JOIN Course co ON c.CourseId = co.Id " +
                "WHERE t.Id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule s = new Schedule();
                s.setDate(rs.getDate("Date").toLocalDate());
                s.setStartTime(rs.getTime("StartTime").toLocalTime());
                s.setEndTime(rs.getTime("EndTime").toLocalTime());
                s.setRoomCode(rs.getString("RoomCode"));
                s.setClassName(rs.getString("ClassName"));
                s.setCourseTitle(rs.getString("CourseTitle"));
                list.add(s);
            }
        }
        return list;
    }

    public List<Schedule> getAllSchedules() throws SQLException {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT s.Date, s.StartTime, s.EndTime, r.Code AS RoomCode, " +
                "c.Name AS ClassName, co.Title AS CourseTitle " +
                "FROM Schedule s " +
                "JOIN ClassRooms c ON s.ClassRoomId = c.Id " +
                "JOIN Room r ON s.RoomId = r.Id " +
                "JOIN Course co ON c.CourseId = co.Id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule s = new Schedule();
                s.setDate(rs.getDate("Date").toLocalDate());
                s.setStartTime(rs.getTime("StartTime").toLocalTime());
                s.setEndTime(rs.getTime("EndTime").toLocalTime());
                s.setRoomCode(rs.getString("RoomCode"));
                s.setClassName(rs.getString("ClassName"));
                s.setCourseTitle(rs.getString("CourseTitle"));
                list.add(s);
            }
        }
        return list;
    }

}
