package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Schedule;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class TeacherScheduleDAO {

    //Lấy lịch theo teacherId
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

    //Lấy lịch trong tuần
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

    //lấy lịch theo ngày cụ thể
    public ArrayList<Schedule> getScheduleByTeacherIdAndDate(int teacherId, LocalDate date) {
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
        WHERE t.Id = ? AND s.Date = ?
        ORDER BY sl.StartTime
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ps.setDate(2, Date.valueOf(date));
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

    /**
     * Lấy thông tin chi tiết của một lịch học
     */
    public Schedule getScheduleById(int scheduleId) {
        String sql = """
            SELECT s.Id, s.Date, s.RoomId, s.ClassRoomId, s.SlotId,
                   c.Name as ClassName, cr.Name as CourseName, cr.Title as CourseTitle,
                   r.Code as RoomCode, sl.StartTime, sl.EndTime
            FROM Schedule s
            INNER JOIN ClassRooms cr ON s.ClassRoomId = cr.Id
            INNER JOIN Room r ON s.RoomId = r.Id
            INNER JOIN Slot sl ON s.SlotId = sl.Id
            WHERE s.Id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(rs.getInt("Id"));
                schedule.setDate(rs.getDate("Date").toLocalDate());
                schedule.setRoomId(rs.getInt("RoomId"));
                schedule.setClassRoomId(rs.getInt("ClassRoomId"));
                schedule.setSlotId(rs.getInt("SlotId"));
                schedule.setClassName(rs.getString("ClassName"));
                schedule.setCourseTitle(rs.getString("CourseTitle"));
                schedule.setRoomCode(rs.getString("RoomCode"));
                schedule.setSlotStartTime(rs.getTime("StartTime").toLocalTime());
                schedule.setSlotEndTime(rs.getTime("EndTime").toLocalTime());

                return schedule;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Kiểm tra xem có lịch học nào trong ngày và slot cụ thể không
     */
    public boolean hasScheduleInSlot(LocalDate date, int slotId, int roomId) {
        String sql = "SELECT COUNT(*) FROM Schedule WHERE Date = ? AND SlotId = ? AND RoomId = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ps.setInt(2, slotId);
            ps.setInt(3, roomId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Cập nhật phòng học cho một lịch học
     */
    public boolean updateScheduleRoom(int scheduleId, int newRoomId) {
        String sql = "UPDATE Schedule SET RoomId = ? WHERE Id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newRoomId);
            ps.setInt(2, scheduleId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Cập nhật ngày học cho một lịch học
     */
    public boolean updateScheduleDate(int scheduleId, LocalDate newDate) {
        String sql = "UPDATE Schedule SET Date = ? WHERE Id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(newDate));
            ps.setInt(2, scheduleId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Xóa lịch học (để xử lý nghỉ phép)
     */
    public boolean deleteSchedule(int scheduleId) {
        String sql = "DELETE FROM Schedule WHERE Id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
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