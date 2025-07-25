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
        String sql = "INSERT INTO ClassRooms (Name, CourseId, TeacherId, SlotId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, classRoom.getName());
            stmt.setInt(2, classRoom.getCourseId());
            stmt.setInt(3, classRoom.getTeacherId());
            stmt.setInt(4, classRoom.getSlotId());
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
        sql.append("SELECT DISTINCT cr.Id, cr.Name, cr.CourseId, cr.TeacherId, cr.SlotId, ")
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
     * Xóa lớp học theo ID
     */
    public boolean deleteClassRoom(int classRoomId) {
        // Kiểm tra xem lớp học có học sinh không
        if (hasStudentsInClass(classRoomId)) {
            return false; // Không thể xóa nếu có học sinh
        }

        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // Xóa các schedule pattern trước
            String deletePatternSql = "DELETE FROM ClassSchedulePattern WHERE ClassRoomId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deletePatternSql)) {
                stmt.setInt(1, classRoomId);
                stmt.executeUpdate();
            }

            // Xóa các schedule
            String deleteScheduleSql = "DELETE FROM Schedule WHERE ClassRoomId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteScheduleSql)) {
                stmt.setInt(1, classRoomId);
                stmt.executeUpdate();
            }

            // Xóa lớp học
            String deleteClassSql = "DELETE FROM ClassRooms WHERE Id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteClassSql)) {
                stmt.setInt(1, classRoomId);
                int affected = stmt.executeUpdate();

                if (affected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
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
     * Kiểm tra xem lớp học có học sinh không
     */
    private boolean hasStudentsInClass(int classRoomId) {
        String sql = "SELECT COUNT(*) FROM Student_Class WHERE ClassRoomId = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classRoomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
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