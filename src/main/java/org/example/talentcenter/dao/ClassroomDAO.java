/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-06-29
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-06-29  | Cù Thị Huyền Trang   | Initial creation
 */


package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.*;
import org.example.talentcenter.model.Classroom;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ClassroomDAO {
    /**
     * Lấy danh sách tất cả các lớp mà học sinh đang hoặc từng học.
     *
     * @param studentId ID của học sinh
     * @return danh sách các lớp thuộc về học sinh đó
     * @author Huyen Trang
     */
    public ArrayList<Classroom> getAllStudentClassByStudentId(int studentId) {
        ArrayList<Classroom> aClassrooms = new ArrayList<>();
        String query = """
                SELECT c.Id, c.Name FROM Student s join Student_Class sc on s.id = sc.StudentId
                join ClassRooms c on sc.ClassRoomId = c.Id where s.Id = ?
                """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Classroom sc = new Classroom(
                        rs.getInt("Id"),
                        rs.getString("Name")
                );
                aClassrooms.add(sc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aClassrooms;
    }

    /**
     * Lấy danh sách tên các lớp học mà học sinh đã tham gia (không trùng).
     *
     * @param studentId ID của học sinh
     * @return danh sách tên lớp học
     * @author Huyen Trang
     */
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

    /**
     * Lấy thông tin chi tiết lịch học theo học sinh, slot và ngày cụ thể.
     * Bao gồm thông tin lớp, giáo viên, phòng học và trạng thái điểm danh.
     *
     * @param studentId ID của học sinh
     * @param slotId    ID của ca học
     * @param date      Ngày học cụ thể
     * @return Danh sách chi tiết lịch học ứng với điều kiện
     * @author Huyen Trang
     */
    public ArrayList<StudentSchedule> getClassDetail(int studentId, int slotId, LocalDate date) {
        ArrayList<StudentSchedule> list = new ArrayList<>();
        String sql = """
                SELECT s.Date, s.SlotId, r.Code AS RoomCode, sl.StartTime, sl.EndTime,
                       c.Name AS ClassName, co.Title AS CourseTitle, a.FullName AS TeacherName,
                       a.Email AS TeacherEmail, a.PhoneNumber AS TeacherPhoneNumber,
                       att.Status AS AttendanceStatus
                FROM Schedule s
                JOIN Slot sl ON s.SlotId = sl.Id
                JOIN Room r ON s.RoomId = r.Id
                JOIN ClassRooms c ON s.ClassRoomId = c.Id
                JOIN Teacher t ON c.TeacherId = t.Id
                JOIN Account a ON t.AccountId = a.Id
                JOIN Course co ON c.CourseId = co.Id
                JOIN Student_Class sc ON c.Id = sc.ClassRoomId
                JOIN Student st ON sc.StudentId = st.Id
                LEFT JOIN Attendance att ON att.ScheduleId = s.Id AND att.StudentId = st.Id
                WHERE st.Id = ? AND s.SlotId = ? AND s.Date = ?
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, slotId);
            ps.setDate(3, Date.valueOf(date));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StudentSchedule schedule = new StudentSchedule();
                schedule.setDate(rs.getDate("Date").toLocalDate());
                schedule.setSlotId(rs.getInt("SlotId"));
                schedule.setSlotStartTime(rs.getTime("StartTime").toLocalTime());
                schedule.setSlotEndTime(rs.getTime("EndTime").toLocalTime());
                schedule.setRoomCode(rs.getString("RoomCode"));
                schedule.setClassName(rs.getString("ClassName"));
                schedule.setCourseTitle(rs.getString("CourseTitle"));
                schedule.setTeacherName(rs.getString("TeacherName"));
                schedule.setTeacherEmail(rs.getString("TeacherEmail"));
                schedule.setTeacherPhoneNumber(rs.getString("TeacherPhoneNumber"));
                schedule.setAttendaceStatus(rs.getString("AttendanceStatus"));

                list.add(schedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Trả về danh sách các lớp thuộc một khóa học cụ thể,
     * bao gồm số chỗ trống còn lại và tên giáo viên giảng dạy.
     *
     * @param courseId ID của khóa học
     * @return Danh sách các lớp (Classroom) tương ứng với khóa học
     * @author Huyen Trang
     */
    public ArrayList<Classroom> getClassesByCourseId(int courseId) {
        ArrayList<Classroom> classrooms = new ArrayList<>();
        String query = """
                    SELECT 
                        cr.Id as ClassroomID,
                        cr.Name as ClassroomName,
                        cr.CourseId,
                        cr.TeacherId,
                        cr.MaxCapacity,
                        COUNT(sc.StudentId) as CurrentStudents,
                        a.FullName as TeacherName
                    FROM ClassRooms cr
                    JOIN Student_Class sc ON cr.Id = sc.ClassRoomId
                    JOIN Teacher t ON cr.TeacherId = t.Id
                    JOIN Account a ON t.AccountId = a.Id
                    WHERE cr.CourseId = ?
                    GROUP BY cr.Id, cr.Name, cr.CourseId, cr.TeacherId, cr.MaxCapacity, a.FullName
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Classroom classRoom = new Classroom();
                classRoom.setClassroomID(rs.getInt("ClassroomID"));
                classRoom.setClassroomName(rs.getString("ClassroomName"));
                classRoom.setCourseId(rs.getInt("CourseId"));
                classRoom.setTeacherId(rs.getInt("TeacherId"));
                classRoom.setMaxCapacity(rs.getInt("MaxCapacity"));
                int currentStudents = rs.getInt("CurrentStudents");
                int availableSeats = rs.getInt("MaxCapacity") - currentStudents;
                classRoom.setAvailableSeats(availableSeats);
                classRoom.setTeacherName(rs.getString("TeacherName"));
                System.out.println("Lớp: " + rs.getString("ClassroomName") +
                        " - GV: " + rs.getString("TeacherName") +
                        " - Chỗ trống: " + availableSeats);

                classrooms.add(classRoom);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return classrooms;
    }

    /**
     * Lấy lịch học của một lớp cụ thể, bao gồm ngày học, giờ bắt đầu/kết thúc và mã phòng học.
     *
     * @param classId ID của lớp cần lấy lịch
     * @return Danh sách lịch học (StudentSchedule) của lớp
     * @author Huyen Trang
     */
    public ArrayList<StudentSchedule> getClassSchedule(int classId) {
        ArrayList<StudentSchedule> schedules = new ArrayList<>();
        String query = """
                    SELECT 
                        s.Date,
                        sl.StartTime,
                        sl.EndTime,
                        r.Code as RoomCode
                    FROM Schedule s
                    JOIN Slot sl ON s.SlotId = sl.Id
                    JOIN Room r ON s.RoomId = r.Id
                    WHERE s.ClassRoomId = ?
                    ORDER BY s.Date, sl.StartTime                
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StudentSchedule schedule = new StudentSchedule();
                schedule.setDate(rs.getDate("Date").toLocalDate());
                schedule.setSlotStartTime(rs.getTime("StartTime").toLocalTime());
                schedule.setSlotEndTime(rs.getTime("EndTime").toLocalTime());
                schedule.setRoomCode(rs.getString("RoomCode"));

                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }

    /**
     * Lấy danh sách các lớp học còn chỗ trống (số học viên hiện tại < sức chứa tối đa).
     *
     * @return Danh sách lớp học còn chỗ trống kèm theo thông tin giáo viên và khóa học nếu có
     * @author Huyen Trang
     */
    public ArrayList<Classroom> getAvailableClassrooms() {
        ArrayList<Classroom> classrooms = new ArrayList<>();
        String query = """
                SELECT 
                    cr.Id as ClassroomID,
                    cr.Name as ClassroomName,
                    cr.CourseId,
                    cr.TeacherId,
                    cr.MaxCapacity,
                    COUNT(sc.StudentId) as CurrentStudents,
                    a.FullName as TeacherName,
                    c.Title as CourseName
                FROM ClassRooms cr
                LEFT JOIN Student_Class sc ON cr.Id = sc.ClassRoomId
                LEFT JOIN Teacher t ON cr.TeacherId = t.Id
                LEFT JOIN Account a ON t.AccountId = a.Id
                LEFT JOIN Course c ON cr.CourseId = c.Id 
                WHERE cr.CourseId IS NOT NULL
                GROUP BY cr.Id, cr.Name, cr.CourseId, cr.TeacherId, cr.MaxCapacity, a.FullName, c.Title
                HAVING COUNT(sc.StudentId) < cr.MaxCapacity
                ORDER BY cr.Name
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Classroom classRoom = new Classroom();
                classRoom.setClassroomID(rs.getInt("ClassroomID"));
                classRoom.setClassroomName(rs.getString("ClassroomName"));
                classRoom.setCourseId(rs.getInt("CourseId"));
                classRoom.setTeacherId(rs.getInt("TeacherId"));
                classRoom.setMaxCapacity(rs.getInt("MaxCapacity"));

                int currentStudents = rs.getInt("CurrentStudents");
                int availableSeats = rs.getInt("MaxCapacity") - currentStudents;
                classRoom.setAvailableSeats(availableSeats);
                classRoom.setTeacherName(rs.getString("TeacherName"));

                classrooms.add(classRoom);
                System.out.println("Lớp: " + classRoom.getClassroomName() + " - Chỗ trống: " + availableSeats);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách lớp có chỗ trống: " + e.getMessage());
            e.printStackTrace();
        }
        return classrooms;
    }

}