package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.ClassSchedulePattern;
import org.example.talentcenter.model.Schedule;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeacherScheduleDAO {

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
               cr.Name as ClassName, cr.Name as CourseName,
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
    public boolean updateScheduleDateAndSlot(int scheduleId, String newDate, int newSlot) {
        String sql = "UPDATE Schedule SET Date = ?, SlotId = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newDate);
            ps.setInt(2, newSlot);
            ps.setInt(3, scheduleId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<Schedule> getAllSlots() {
        ArrayList<Schedule> slots = new ArrayList<>();
        String sql = "SELECT Id, StartTime, EndTime FROM Slot ORDER BY Id";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Schedule slot = new Schedule();
                slot.setSlotId(rs.getInt("Id"));
                slot.setSlotStartTime(rs.getTime("StartTime").toLocalTime());
                slot.setSlotEndTime(rs.getTime("EndTime").toLocalTime());
                slots.add(slot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }
    /**
     * Kiểm tra xung đột phòng học với lịch
     */
    public boolean hasRoomConflict(int roomId, int slotId, List<Integer> daysOfWeek,
                                   LocalDate startDate, LocalDate endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM Schedule s ")
                .append("INNER JOIN ClassSchedulePattern csp ON s.ClassRoomId = csp.ClassRoomId ")
                .append("WHERE s.RoomId = ? ")
                .append("AND s.SlotId = ? ")
                .append("AND csp.DayOfWeek IN (");

        // Add placeholders for days of week
        for (int i = 0; i < daysOfWeek.size(); i++) {
            sql.append("?");
            if (i < daysOfWeek.size() - 1) {
                sql.append(",");
            }
        }

        sql.append(") ")
                .append("AND (")
                .append("(csp.StartDate <= ? AND csp.EndDate >= ?) OR ") // New period starts during existing
                .append("(csp.StartDate <= ? AND csp.EndDate >= ?) OR ") // New period ends during existing
                .append("(csp.StartDate >= ? AND csp.EndDate <= ?)")     // Existing period within new period
                .append(")");

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            stmt.setInt(paramIndex++, roomId);
            stmt.setInt(paramIndex++, slotId);

            // Set days of week parameters
            for (Integer day : daysOfWeek) {
                stmt.setInt(paramIndex++, day);
            }

            // Set date parameters for overlap check
            stmt.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
            stmt.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
            stmt.setDate(paramIndex++, java.sql.Date.valueOf(endDate));
            stmt.setDate(paramIndex++, java.sql.Date.valueOf(endDate));
            stmt.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
            stmt.setDate(paramIndex, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // True if conflict exists
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking room conflict: " + e.getMessage());
            e.printStackTrace();
            return true; // Assume conflict on error to be safe
        }
        return false;
    }

    // Duyệt các pattern, lặp từ startDate đến endDate, đúng dayOfWeek thì insert
    public void generateSchedulesFromPattern(List<ClassSchedulePattern> patterns, int classRoomId, int roomId) throws SQLException {
        if (patterns == null || patterns.isEmpty()) {
            throw new IllegalArgumentException("Patterns cannot be null or empty");
        }

        if (classRoomId <= 0 || roomId <= 0) {
            throw new IllegalArgumentException("ClassRoomId and RoomId must be positive");
        }

        String insertSql = "INSERT INTO Schedule (Date, SlotId, RoomId, ClassRoomId) VALUES (?, ?, ?, ?)";
        String checkSql = "SELECT COUNT(*) FROM Schedule WHERE Date = ? AND SlotId = ? AND RoomId = ?";

        Connection conn = null;
        PreparedStatement insertStmt = null;
        PreparedStatement checkStmt = null;

        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false); // Start transaction

            insertStmt = conn.prepareStatement(insertSql);
            checkStmt = conn.prepareStatement(checkSql);

            int batchCount = 0;

            for (ClassSchedulePattern pattern : patterns) {
                if (pattern.getStartDate() == null || pattern.getEndDate() == null) {
                    throw new IllegalArgumentException("Pattern dates cannot be null");
                }

                LocalDate currentDate = pattern.getStartDate();

                while (!currentDate.isAfter(pattern.getEndDate())) {
                    // Check if current date matches the pattern's day of week
                    if (currentDate.getDayOfWeek().getValue() == pattern.getDayOfWeek()) {
                        // Check for schedule conflicts
                        checkStmt.setDate(1, Date.valueOf(currentDate));
                        checkStmt.setInt(2, pattern.getSlotId());
                        checkStmt.setInt(3, roomId);

                        try (ResultSet rs = checkStmt.executeQuery()) {
                            if (rs.next() && rs.getInt(1) == 0) { // No conflict found
                                insertStmt.setDate(1, Date.valueOf(currentDate));
                                insertStmt.setInt(2, pattern.getSlotId());
                                insertStmt.setInt(3, roomId);
                                insertStmt.setInt(4, classRoomId);
                                insertStmt.addBatch();
                                batchCount++;

                                // Execute batch every 100 records for performance
                                if (batchCount % 100 == 0) {
                                    insertStmt.executeBatch();
                                    insertStmt.clearBatch();
                                }
                            } else {
                                // Log conflict but continue processing
                                System.out.println("Schedule conflict detected for date: " + currentDate +
                                        ", slot: " + pattern.getSlotId() +
                                        ", room: " + roomId);
                            }
                        }
                    }
                    currentDate = currentDate.plusDays(1);
                }
            }

            // Execute remaining batch
            if (batchCount % 100 != 0) {
                insertStmt.executeBatch();
            }

            conn.commit(); // Commit transaction

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new SQLException("Error generating schedules from pattern: " + e.getMessage(), e);
        } finally {
            // Close resources
            if (checkStmt != null) {
                try { checkStmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (insertStmt != null) {
                try { insertStmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
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