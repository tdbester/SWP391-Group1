package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.ClassRooms;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClassRoomsDAO {

    public int insertClassRoomAndReturnId(ClassRooms classRoom) throws SQLException {
        String sql = "INSERT INTO ClassRooms (Name, CourseId, TeacherId, SlotId, MaxCapacity) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, classRoom.getName());
            stmt.setInt(2, classRoom.getCourseId());
            stmt.setInt(3, classRoom.getTeacherId());
            stmt.setInt(4, classRoom.getSlotId());
            stmt.setInt(5, classRoom.getMaxCapacity());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return -1;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                else return -1;
            }
        }
    }

    /**
     * Lấy danh sách lớp học với thông tin chi tiết và chức năng tìm kiếm
     */
    public List<ClassRooms> getClassRoomsWithDetails(String courseSearch, String classSearch, int page, int pageSize) {
        List<ClassRooms> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT cr.Id, cr.Name, cr.CourseId, cr.TeacherId, cr.SlotId, cr.MaxCapacity, ")
                .append("c.Title as CourseTitle, ")
                .append("a.FullName as TeacherName, ")
                .append("s.StartTime, s.EndTime, ")
                .append("(SELECT TOP 1 r.Id FROM Schedule sch2 INNER JOIN Room r ON sch2.RoomId = r.Id WHERE sch2.ClassRoomId = cr.Id) as RoomId, ")
                .append("(SELECT TOP 1 r.Code FROM Schedule sch2 INNER JOIN Room r ON sch2.RoomId = r.Id WHERE sch2.ClassRoomId = cr.Id) as RoomCode, ")
                .append("(SELECT COUNT(*) FROM Student_Class sc WHERE sc.ClassRoomId = cr.Id) as StudentCount, ")
                .append("(SELECT MIN(csp.StartDate) FROM ClassSchedulePattern csp WHERE csp.ClassRoomId = cr.Id) as StartDate, ")
                .append("(SELECT MAX(csp.EndDate) FROM ClassSchedulePattern csp WHERE csp.ClassRoomId = cr.Id) as EndDate ")
                .append("FROM ClassRooms cr ")
                .append("INNER JOIN Course c ON cr.CourseId = c.Id ")
                .append("INNER JOIN Teacher t ON cr.TeacherId = t.Id ")
                .append("INNER JOIN Account a ON t.AccountId = a.Id ")
                .append("INNER JOIN Slot s ON cr.SlotId = s.Id ")
                .append("WHERE 1=1 ");

        // Thêm điều kiện tìm kiếm
        if (courseSearch != null && !courseSearch.trim().isEmpty()) {
            sql.append("AND c.Title LIKE ? ");
        }
        if (classSearch != null && !classSearch.trim().isEmpty()) {
            sql.append("AND cr.Name LIKE ? ");
        }

        sql.append("ORDER BY cr.Id DESC ")
                .append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            // Set parameters cho tìm kiếm
            if (courseSearch != null && !courseSearch.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + courseSearch.trim() + "%");
            }
            if (classSearch != null && !classSearch.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + classSearch.trim() + "%");
            }

            // Set parameters cho phân trang
            stmt.setInt(paramIndex++, (page - 1) * pageSize);
            stmt.setInt(paramIndex, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ClassRooms classRoom = new ClassRooms();
                    classRoom.setId(rs.getInt("Id"));
                    classRoom.setName(rs.getString("Name"));
                    classRoom.setCourseId(rs.getInt("CourseId"));
                    classRoom.setTeacherId(rs.getInt("TeacherId"));
                    classRoom.setSlotId(rs.getInt("SlotId"));
                    classRoom.setRoomId(rs.getInt("RoomId"));
                    classRoom.setMaxCapacity(rs.getInt("MaxCapacity"));
                    classRoom.setCourseTitle(rs.getString("CourseTitle"));
                    classRoom.setRoomCode(rs.getString("RoomCode"));

                    // Convert LocalTime to Date for JSP compatibility
                    Time startTime = rs.getTime("StartTime");
                    Time endTime = rs.getTime("EndTime");
                    if (startTime != null) {
                        classRoom.setStartTimeAsDate(new java.util.Date(startTime.getTime()));
                    }
                    if (endTime != null) {
                        classRoom.setEndTimeAsDate(new java.util.Date(endTime.getTime()));
                    }

                    classRoom.setStudentCount(rs.getInt("StudentCount"));
                    classRoom.setTeacherName(rs.getString("TeacherName"));

                    // Convert LocalDate to Date for JSP compatibility
                    if (rs.getDate("StartDate") != null) {
                        classRoom.setStartDate(rs.getDate("StartDate").toLocalDate());
                        classRoom.setStartDateAsDate(rs.getDate("StartDate"));
                    }
                    if (rs.getDate("EndDate") != null) {
                        classRoom.setEndDate(rs.getDate("EndDate").toLocalDate());
                        classRoom.setEndDateAsDate(rs.getDate("EndDate"));
                    }

                    list.add(classRoom);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Đếm tổng số lớp học với điều kiện tìm kiếm
     */
    public int getTotalClassRooms(String courseSearch, String classSearch) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ClassRooms cr ")
                .append("INNER JOIN Course c ON cr.CourseId = c.Id ")
                .append("WHERE 1=1 ");

        if (courseSearch != null && !courseSearch.trim().isEmpty()) {
            sql.append("AND c.Title LIKE ? ");
        }
        if (classSearch != null && !classSearch.trim().isEmpty()) {
            sql.append("AND cr.Name LIKE ? ");
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (courseSearch != null && !courseSearch.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + courseSearch.trim() + "%");
            }
            if (classSearch != null && !classSearch.trim().isEmpty()) {
                stmt.setString(paramIndex, "%" + classSearch.trim() + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy thông tin lớp học theo ID để chỉnh sửa
     */
    public ClassRooms getClassRoomById(int classRoomId) {
        String sql = "SELECT cr.Id, cr.Name, cr.CourseId, cr.TeacherId, cr.SlotId, " +
                "c.Title as CourseTitle, " +
                "a.FullName as TeacherName, " +
                "s.StartTime, s.EndTime, " +
                "r.Id as RoomId, r.Code as RoomCode " +
                "FROM ClassRooms cr " +
                "INNER JOIN Course c ON cr.CourseId = c.Id " +
                "INNER JOIN Teacher t ON cr.TeacherId = t.Id " +
                "INNER JOIN Account a ON t.AccountId = a.Id " +
                "INNER JOIN Slot s ON cr.SlotId = s.Id " +
                "LEFT JOIN Schedule sch ON cr.Id = sch.ClassRoomId " +
                "LEFT JOIN Room r ON sch.RoomId = r.Id " +
                "WHERE cr.Id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classRoomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ClassRooms classRoom = new ClassRooms();
                    classRoom.setId(rs.getInt("Id"));
                    classRoom.setName(rs.getString("Name"));
                    classRoom.setCourseId(rs.getInt("CourseId"));
                    classRoom.setTeacherId(rs.getInt("TeacherId"));
                    classRoom.setSlotId(rs.getInt("SlotId"));
                    classRoom.setRoomId(rs.getInt("RoomId")); // Thêm roomId từ Schedule
                    classRoom.setCourseTitle(rs.getString("CourseTitle"));
                    classRoom.setTeacherName(rs.getString("TeacherName"));
                    classRoom.setRoomCode(rs.getString("RoomCode")); // Thêm roomCode

                    // Convert LocalTime to Date for JSP compatibility
                    Time startTime = rs.getTime("StartTime");
                    Time endTime = rs.getTime("EndTime");
                    if (startTime != null) {
                        classRoom.setStartTimeAsDate(new java.util.Date(startTime.getTime()));
                    }
                    if (endTime != null) {
                        classRoom.setEndTimeAsDate(new java.util.Date(endTime.getTime()));
                    }

                    return classRoom;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin lớp học
     */
    public boolean updateClassRoom(ClassRooms classRoom) {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // 1. Cập nhật thông tin cơ bản của ClassRooms (không có RoomId)
            String updateClassSql = "UPDATE ClassRooms SET Name = ?, CourseId = ?, TeacherId = ?, SlotId = ? WHERE Id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateClassSql)) {
                stmt.setString(1, classRoom.getName());
                stmt.setInt(2, classRoom.getCourseId());
                stmt.setInt(3, classRoom.getTeacherId());
                stmt.setInt(4, classRoom.getSlotId());
                stmt.setInt(5, classRoom.getId());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Cập nhật RoomId trong bảng Schedule
            if (classRoom.getRoomId() > 0) {
                String updateScheduleSql = "UPDATE Schedule SET RoomId = ? WHERE ClassRoomId = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateScheduleSql)) {
                    stmt.setInt(1, classRoom.getRoomId());
                    stmt.setInt(2, classRoom.getId());
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error updating classroom: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Kiểm tra tên lớp học đã tồn tại hay chưa (trừ lớp học hiện tại khi update)
     */
    public boolean isClassNameExists(String className, int excludeId) {
        String sql = "SELECT COUNT(*) FROM ClassRooms WHERE LOWER(Name) = LOWER(?) AND Id != ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, className.trim());
            stmt.setInt(2, excludeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking class name existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra tên lớp học đã tồn tại hay chưa (cho trường hợp tạo mới)
     */
    public boolean isClassNameExists(String className) {
        String sql = "SELECT COUNT(*) FROM ClassRooms WHERE LOWER(Name) = LOWER(?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, className.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking class name existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa lớp học theo ID với cải tiến cho việc xóa schedule patterns
     */
    public boolean deleteClassRoom(int classRoomId) {
        // Kiểm tra xem lớp học có học sinh không
        if (hasStudentsInClass(classRoomId)) {
            System.out.println("Cannot delete classroom " + classRoomId + " - has enrolled students");
            return false; // Không thể xóa nếu có học sinh
        }

        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            System.out.println("Starting deletion process for classroom ID: " + classRoomId);

            // 1. Xóa các ClassSchedulePattern trước
            int deletedPatterns = deleteClassSchedulePatterns(conn, classRoomId);
            System.out.println("Deleted " + deletedPatterns + " schedule patterns for classroom " + classRoomId);

            // 2. Xóa các Schedule records
            int deletedSchedules = deleteScheduleRecords(conn, classRoomId);
            System.out.println("Deleted " + deletedSchedules + " schedule records for classroom " + classRoomId);

            // 3. Xóa các attendance records nếu có
            int deletedAttendances = deleteAttendanceRecords(conn, classRoomId);
            System.out.println("Deleted " + deletedAttendances + " attendance records for classroom " + classRoomId);

            // 4. Xóa các assessment records nếu có
            int deletedAssessments = deleteAssessmentRecords(conn, classRoomId);
            System.out.println("Deleted " + deletedAssessments + " assessment records for classroom " + classRoomId);

            // 5. Cuối cùng xóa lớp học
            String deleteClassSql = "DELETE FROM ClassRooms WHERE Id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteClassSql)) {
                stmt.setInt(1, classRoomId);
                int deletedClassrooms = stmt.executeUpdate();

                if (deletedClassrooms > 0) {
                    conn.commit();
                    System.out.println("Successfully deleted classroom " + classRoomId);
                    return true;
                } else {
                    conn.rollback();
                    System.err.println("Failed to delete classroom " + classRoomId + " - no rows affected");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error deleting classroom " + classRoomId + ": " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction rolled back for classroom " + classRoomId);
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Xóa các ClassSchedulePattern records
     */
    private int deleteClassSchedulePatterns(Connection conn, int classRoomId) throws SQLException {
        String sql = "DELETE FROM ClassSchedulePattern WHERE ClassRoomId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classRoomId);
            return stmt.executeUpdate();
        }
    }

    /**
     * Xóa các Schedule records
     */
    private int deleteScheduleRecords(Connection conn, int classRoomId) throws SQLException {
        String sql = "DELETE FROM Schedule WHERE ClassRoomId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classRoomId);
            return stmt.executeUpdate();
        }
    }

    /**
     * Xóa các Attendance records nếu có
     */
    private int deleteAttendanceRecords(Connection conn, int classRoomId) throws SQLException {
        String sql = "DELETE FROM Attendance WHERE ClassRoomId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classRoomId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            // Bảng Attendance có thể không tồn tại hoặc không có cột ClassRoomId
            System.out.println("Note: Could not delete attendance records (table may not exist or have different structure): " + e.getMessage());
            return 0;
        }
    }

    /**
     * Xóa các Assessment records nếu có
     */
    private int deleteAssessmentRecords(Connection conn, int classRoomId) throws SQLException {
        String sql = "DELETE FROM Assessment WHERE ClassRoomId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classRoomId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            // Bảng Assessment có thể không tồn tại hoặc không có cột ClassRoomId
            System.out.println("Note: Could not delete assessment records (table may not exist or have different structure): " + e.getMessage());
            return 0;
        }
    }

    /**
     * Kiểm tra xem lớp học có học sinh không
     */
    private boolean hasStudentsInClass(int classRoomId) {
        String sql = "SELECT COUNT(*) FROM Student_Class WHERE ClassRoomId = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classRoomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int studentCount = rs.getInt(1);
                    System.out.println("Classroom " + classRoomId + " has " + studentCount + " students");
                    return studentCount > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking students in class: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra xem giáo viên có đang dạy lớp khác trong cùng slot và cùng ngày trong tuần không
     */
    public boolean isTeacherAvailableInSlotAndDays(int teacherId, int slotId, List<Integer> daysOfWeek,
                                                   LocalDate startDate, LocalDate endDate, int excludeClassId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ClassRooms cr ")
                .append("INNER JOIN ClassSchedulePattern csp ON cr.Id = csp.ClassRoomId ")
                .append("WHERE cr.TeacherId = ? ")
                .append("AND cr.SlotId = ? ")
                .append("AND cr.Id != ? ")
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
            stmt.setInt(paramIndex++, teacherId);
            stmt.setInt(paramIndex++, slotId);
            stmt.setInt(paramIndex++, excludeClassId);

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
                    return rs.getInt(1) == 0; // True nếu không có xung đột
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking teacher availability with days: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Assume conflict on error to be safe
    }

    /**
     * Kiểm tra xem giáo viên có đang dạy lớp khác trong cùng slot và cùng ngày trong tuần không (cho trường hợp tạo mới)
     */
    public boolean isTeacherAvailableInSlotAndDays(int teacherId, int slotId, List<Integer> daysOfWeek,
                                                   LocalDate startDate, LocalDate endDate) {
        return isTeacherAvailableInSlotAndDays(teacherId, slotId, daysOfWeek, startDate, endDate, -1);
    }
}