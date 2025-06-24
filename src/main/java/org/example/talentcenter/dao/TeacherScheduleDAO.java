package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Teacher;
import org.example.talentcenter.model.Schedule;

import java.sql.*;
import java.util.*;

import java.util.ArrayList;

public class TeacherScheduleDAO {
    public ArrayList<Schedule> getScheduleByTeacherId(int teacherId) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        String sql = """
                    SELECT s.Date, s.StartTime, s.EndTime, r.Code AS RoomCode,
                                                                           c.Name AS ClassName, co.Title AS CourseTitle, a.FullName AS TeacherName
                                                                    FROM Schedule s
                                                                    JOIN Room r ON s.RoomId = r.Id
                                                                    JOIN ClassRooms c ON s.ClassRoomId = c.Id
                                                                    JOIN Teacher t ON c.TeacherId = t.Id
                                                                    JOIN Account a ON t.AccountId = a.Id
                                                                    JOIN Course co ON c.CourseId = co.Id
                                                                    WHERE t.Id = ?
                                                                    ORDER BY s.Date, s.StartTime
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setDate(rs.getDate("Date").toLocalDate());
                schedule.setStartTime(rs.getTime("StartTime").toLocalTime());
                schedule.setEndTime(rs.getTime("EndTime").toLocalTime());
                schedule.setRoomCode(rs.getString("RoomCode"));
                schedule.setClassName(rs.getString("ClassName"));
                schedule.setCourseTitle(rs.getString("CourseTitle"));
                schedule.setTeacherName(rs.getString("TeacherName"));
                schedules.add(schedule);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }
}
