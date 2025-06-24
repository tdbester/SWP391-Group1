/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-06-18
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-06-15  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Student;
import org.example.talentcenter.model.StudentSchedule;

import java.sql.*;
import java.util.*;

import java.util.ArrayList;

public class StudentScheduleDAO {
    public ArrayList<StudentSchedule> getScheduleByStudentId(int studentId) {
        ArrayList<StudentSchedule> schedules = new ArrayList<>();
        String sql = """
                    SELECT s.Date, s.StartTime, s.EndTime, r.Code AS RoomCode, 
                           c.Name AS ClassName, co.Title AS CourseTitle, a.FullName AS TeacherName
                    FROM Schedule s
                    JOIN Room r ON s.RoomId = r.Id
                    JOIN ClassRooms c ON s.ClassRoomId = c.Id
                    JOIN Teacher t ON c.TeacherId = t.Id
                    JOIN Account a ON t.AccountId = a.Id
                    JOIN Course co ON c.CourseId = co.Id
                    JOIN Student_Class sc ON c.Id = sc.ClassRoomId
                    JOIN Student st ON sc.StudentId = st.Id
                    WHERE st.Id = ?
                    ORDER BY s.Date, s.StartTime
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StudentSchedule schedule = new StudentSchedule();
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
