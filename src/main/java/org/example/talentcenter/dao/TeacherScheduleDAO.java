package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Schedule;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class TeacherScheduleDAO {

    public ArrayList<Schedule> getScheduleByTeacherId(int teacherId) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        String sql = """
        SELECT s.Id, s.Date, s.SlotId, sl.StartTime, sl.EndTime,
               r.Code AS RoomCode,
               c.Id AS ClassRoomId, c.Name AS ClassName,
               co.Title AS CourseTitle,
               a.FullName AS TeacherName,
               t.Id AS TeacherId,
               r.Id AS RoomId
        FROM Schedule s
        JOIN Slot sl ON s.SlotId = sl.Id
        JOIN Room r ON s.RoomId = r.Id
        JOIN ClassRooms c ON s.ClassRoomId = c.Id
        JOIN Teacher t ON c.TeacherId = t.Id
        JOIN Account a ON t.AccountId = a.Id
        JOIN Course co ON c.CourseId = co.Id
        WHERE t.Id = ?
        ORDER BY s.Date, sl.StartTime
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Schedule schedule = createScheduleFromResultSet(rs);
                schedules.add(schedule);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }

    public ArrayList<Schedule> getScheduleByTeacherIdAndWeek(int teacherId, LocalDate weekStart) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        LocalDate weekEnd = weekStart.plusDays(6);

        String sql = """
        SELECT s.Id, s.Date, s.SlotId, sl.StartTime, sl.EndTime,
               r.Code AS RoomCode,
               c.Id AS ClassRoomId, c.Name AS ClassName,
               co.Title AS CourseTitle,
               a.FullName AS TeacherName,
               t.Id AS TeacherId,
               r.Id AS RoomId
        FROM Schedule s
        JOIN Slot sl ON s.SlotId = sl.Id
        JOIN Room r ON s.RoomId = r.Id
        JOIN ClassRooms c ON s.ClassRoomId = c.Id
        JOIN Teacher t ON c.TeacherId = t.Id
        JOIN Account a ON t.AccountId = a.Id
        JOIN Course co ON c.CourseId = co.Id
        WHERE t.Id = ? AND s.Date >= ? AND s.Date <= ?
        ORDER BY s.Date, sl.StartTime
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ps.setDate(2, Date.valueOf(weekStart));
            ps.setDate(3, Date.valueOf(weekEnd));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Schedule schedule = createScheduleFromResultSet(rs);
                schedules.add(schedule);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }

    // Helper method để tạo Schedule object từ ResultSet
    private Schedule createScheduleFromResultSet(ResultSet rs) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setId(rs.getInt("Id"));
        schedule.setDate(rs.getDate("Date").toLocalDate());
        schedule.setSlotId(rs.getInt("SlotId"));
        schedule.setSlotStartTime(rs.getTime("StartTime").toLocalTime());
        schedule.setSlotEndTime(rs.getTime("EndTime").toLocalTime());
        schedule.setRoomId(rs.getInt("RoomId"));
        schedule.setRoomCode(rs.getString("RoomCode"));
        schedule.setClassRoomId(rs.getInt("ClassRoomId"));
        schedule.setClassName(rs.getString("ClassName"));
        schedule.setCourseTitle(rs.getString("CourseTitle"));
        schedule.setTeacherId(rs.getInt("TeacherId"));
        schedule.setTeacherName(rs.getString("TeacherName"));
        return schedule;
    }
}