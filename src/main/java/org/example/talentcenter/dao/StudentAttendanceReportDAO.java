package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.StudentAttendanceReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentAttendanceReportDAO {
    public ArrayList<StudentAttendanceReport> getAttendanceByStudentId(int studentId) {
        ArrayList<StudentAttendanceReport> schedules = new ArrayList<>();
        String sql = """
                SELECT s.Date, s.StartTime, s.EndTime, att.Status, 
                                       r.Code AS RoomCode, c.Name AS ClassName, 
                                       co.Title AS CourseTitle, a.FullName AS TeacherName
                                FROM Schedule s
                                JOIN Room r ON s.RoomId = r.Id
                                JOIN ClassRooms c ON s.ClassRoomId = c.Id
                                JOIN Teacher t ON c.TeacherId = t.Id
                                JOIN Account a ON t.AccountId = a.Id
                                JOIN Course co ON c.CourseId = co.Id
                                JOIN Student_Class sc ON c.Id = sc.ClassRoomId
                                JOIN Student st ON sc.StudentId = st.Id
                                JOIN Attendance att ON att.ScheduleId = s.Id AND att.StudentId = st.Id
                                WHERE st.Id = ?
                                ORDER BY s.Date, s.StartTime          
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StudentAttendanceReport studentAttendanceReport = new StudentAttendanceReport();
                studentAttendanceReport.setDate(rs.getDate("Date").toLocalDate());
                studentAttendanceReport.setStartTime(rs.getTime("StartTime").toLocalTime());
                studentAttendanceReport.setEndTime(rs.getTime("EndTime").toLocalTime());
                studentAttendanceReport.setRoomCode(rs.getString("RoomCode"));
                studentAttendanceReport.setClassName(rs.getString("ClassName"));
                studentAttendanceReport.setCourseTitle(rs.getString("CourseTitle"));
                studentAttendanceReport.setTeacherName(rs.getString("TeacherName"));
                studentAttendanceReport.setStatus(rs.getString("Status"));
                schedules.add(studentAttendanceReport);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }
}
