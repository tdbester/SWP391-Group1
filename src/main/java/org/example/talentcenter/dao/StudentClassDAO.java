package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.*;

import java.sql.*;
import java.time.LocalDate;
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
}
