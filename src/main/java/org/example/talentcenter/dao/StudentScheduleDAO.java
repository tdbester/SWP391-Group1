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
import org.example.talentcenter.model.StudentSchedule;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import java.util.ArrayList;

public class StudentScheduleDAO {

    /**
     * Lấy danh sách lịch học trong tuần của học sinh theo ID và khoảng ngày.
     *
     * @param studentId   ID học sinh
     * @param startOfWeek Ngày bắt đầu tuần (thứ 2)
     * @param endOfWeek   Ngày kết thúc tuần (chủ nhật)
     * @return Danh sách lịch học trong tuần
     * @author Huyen Trang
     */
    public ArrayList<StudentSchedule> getScheduleByStudentIdAndWeek(int studentId, LocalDate startOfWeek, LocalDate endOfWeek) {
        ArrayList<StudentSchedule> schedules = new ArrayList<>();
        String sql = """
                    SELECT s.Date, s.SlotId, r.Code AS RoomCode, sl.StartTime, sl.EndTime,
                           c.Name AS ClassName, co.Title AS CourseTitle, a.FullName AS TeacherName
                    FROM Schedule s
                    JOIN Slot sl on s.SlotId = sl.Id
                    JOIN Room r ON s.RoomId = r.Id
                    JOIN ClassRooms c ON s.ClassRoomId = c.Id
                    JOIN Teacher t ON c.TeacherId = t.Id
                    JOIN Account a ON t.AccountId = a.Id
                    JOIN Course co ON c.CourseId = co.Id
                    JOIN Student_Class sc ON c.Id = sc.ClassRoomId
                    JOIN Student st ON sc.StudentId = st.Id
                    WHERE st.Id = ? AND s.Date >= ? AND s.Date <= ?
                    ORDER BY s.Date, s.SlotId
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setDate(2, Date.valueOf(startOfWeek));
            ps.setDate(3, Date.valueOf(endOfWeek));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StudentSchedule schedule = new StudentSchedule();
                schedule.setDate(rs.getDate("Date").toLocalDate());
                schedule.setSlotId(rs.getInt("SlotId"));
                schedule.setRoomCode(rs.getString("RoomCode"));
                schedule.setSlotStartTime(rs.getTime("StartTime").toLocalTime());
                schedule.setSlotEndTime(rs.getTime("EndTime").toLocalTime());
                schedule.setClassName(rs.getString("ClassName"));
                schedule.setCourseTitle(rs.getString("CourseTitle"));
                schedule.setTeacherName(rs.getString("TeacherName"));
                schedules.add(schedule);
            }

            System.out.println("Tổng số lịch học: " + schedules.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Lấy lịch học của học sinh theo ngày cụ thể.
     *
     * @param studentId ID học sinh
     * @param date      Ngày cần lấy lịch
     * @return Danh sách lịch học trong ngày
     * @author Huyen Trang
     */
    public List<StudentSchedule> getScheduleByStudentIdAndDate(int studentId, LocalDate date) {
        List<StudentSchedule> list = new ArrayList<>();
        String sql = """
                SELECT s.Date, s.SlotId, r.Code AS RoomCode, sl.StartTime, sl.EndTime,
                           c.Name AS ClassName, co.Title AS CourseTitle, a.FullName AS TeacherName
                    FROM Schedule s
                    JOIN Slot sl on s.SlotId = sl.Id
                    JOIN Room r ON s.RoomId = r.Id
                    JOIN ClassRooms c ON s.ClassRoomId = c.Id
                    JOIN Teacher t ON c.TeacherId = t.Id
                    JOIN Account a ON t.AccountId = a.Id
                    JOIN Course co ON c.CourseId = co.Id
                    JOIN Student_Class sc ON c.Id = sc.ClassRoomId
                    JOIN Student st ON sc.StudentId = st.Id
                    WHERE st.Id = ? AND s.Date = ? 
                    ORDER BY s.Date, s.SlotId
                """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setDate(2, Date.valueOf(date));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StudentSchedule schedule = new StudentSchedule();
                schedule.setDate(rs.getDate("Date").toLocalDate());
                schedule.setSlotId(rs.getInt("SlotId"));
                schedule.setRoomCode(rs.getString("RoomCode"));
                schedule.setSlotStartTime(rs.getTime("StartTime").toLocalTime());
                schedule.setSlotEndTime(rs.getTime("EndTime").toLocalTime());
                schedule.setClassName(rs.getString("ClassName"));
                schedule.setCourseTitle(rs.getString("CourseTitle"));
                schedule.setTeacherName(rs.getString("TeacherName"));
                list.add(schedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}