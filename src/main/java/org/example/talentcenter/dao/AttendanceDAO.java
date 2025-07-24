package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Attendance;
import org.example.talentcenter.model.ClassRooms;
import org.example.talentcenter.model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    // Lấy tất cả các lớp học mà giáo viên phụ trách
    public List<ClassRooms> getAllClassesByTeacherId(int teacherId) {
        List<ClassRooms> classes = new ArrayList<>();
        String sql = """
            SELECT c.Id, c.Name, c.CourseId, c.TeacherId, c.SlotId,
                   course.Title as CourseTitle,
                   slot.StartTime, slot.EndTime,
                   (SELECT COUNT(*) FROM Student_Class sc WHERE sc.ClassRoomId = c.Id) as StudentCount
            FROM ClassRooms c
            JOIN Course course ON c.CourseId = course.Id
            JOIN Slot slot ON c.SlotId = slot.Id
            WHERE c.TeacherId = ?
            ORDER BY c.Name
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ClassRooms classRoom = new ClassRooms();
                classRoom.setId(rs.getInt("Id"));
                classRoom.setName(rs.getString("Name"));
                classRoom.setCourseId(rs.getInt("CourseId"));
                classRoom.setTeacherId(rs.getInt("TeacherId"));
                classRoom.setSlotId(rs.getInt("SlotId"));
                classRoom.setCourseTitle(rs.getString("CourseTitle"));
                classRoom.setStartTime(rs.getTime("StartTime").toLocalTime());
                classRoom.setEndTime(rs.getTime("EndTime").toLocalTime());
                classRoom.setStudentCount(rs.getInt("StudentCount"));

                classes.add(classRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // Lấy các lớp hôm nay của giáo viên
    public List<ClassRooms> getTodayClassesByTeacherId(int teacherId) {
        List<ClassRooms> classes = new ArrayList<>();
        String sql = """
            SELECT DISTINCT c.Id, c.Name, c.CourseId, c.TeacherId, c.SlotId,
                   course.Title as CourseTitle,
                   slot.StartTime, slot.EndTime,
                   (SELECT COUNT(*) FROM Student_Class sc WHERE sc.ClassRoomId = c.Id) as StudentCount
            FROM ClassRooms c
            JOIN Course course ON c.CourseId = course.Id
            JOIN Slot slot ON c.SlotId = slot.Id
            JOIN Schedule s ON s.ClassRoomId = c.Id
            WHERE c.TeacherId = ? AND s.Date = CAST(GETDATE() AS DATE)
            ORDER BY slot.StartTime
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ClassRooms classRoom = new ClassRooms();
                classRoom.setId(rs.getInt("Id"));
                classRoom.setName(rs.getString("Name"));
                classRoom.setCourseId(rs.getInt("CourseId"));
                classRoom.setTeacherId(rs.getInt("TeacherId"));
                classRoom.setSlotId(rs.getInt("SlotId"));
                classRoom.setCourseTitle(rs.getString("CourseTitle"));
                classRoom.setStartTime(rs.getTime("StartTime").toLocalTime());
                classRoom.setEndTime(rs.getTime("EndTime").toLocalTime());
                classRoom.setStudentCount(rs.getInt("StudentCount"));

                classes.add(classRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // Lấy danh sách học sinh theo classId
    public List<Student> getStudentsByClassId(int classId) {
        List<Student> students = new ArrayList<>();
        String sql = """
            
                SELECT s.Id, a.FullName, s.ParentPhone,
                                      s.AccountId, s.EnrollmentDate
                               FROM Student s
                               JOIN Account a ON s.AccountId = a.Id
                               JOIN Student_Class sc ON s.Id = sc.StudentId
                               WHERE sc.ClassRoomId = ?
                               ORDER BY a.FullName
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("Id"));
                student.setFullName(rs.getString("FullName"));
                student.setParentPhone(rs.getString("ParentPhone"));
                student.setAccountId(rs.getInt("AccountId"));
                student.setEnrollmentDate(rs.getDate("EnrollmentDate").toLocalDate());

                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Lấy scheduleId theo classId và ngày
    public int getScheduleIdByClassAndDate(int classId, LocalDate date) {
        String sql = "SELECT Id FROM Schedule WHERE ClassRoomId = ? AND Date = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            pstmt.setDate(2, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Kiểm tra đã điểm danh chưa
    public boolean isAttendanceExist(int scheduleId) {
        String sql = "SELECT COUNT(*) FROM Attendance WHERE ScheduleId = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách điểm danh theo scheduleId
    public List<Attendance> getAttendanceByScheduleId(int scheduleId) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = """
            
                SELECT att.Id, att.ScheduleId, att.StudentId, att.Status, att.Note,
                                      a.FullName as StudentName
                               FROM Attendance att
                               JOIN Student s ON att.StudentId = s.Id
                               JOIN Account a ON s.AccountId = a.Id
                               WHERE att.ScheduleId = ?
                               ORDER BY a.FullName
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(rs.getInt("Id"));
                attendance.setScheduleId(rs.getInt("ScheduleId"));
                attendance.setStudentId(rs.getInt("StudentId"));
                attendance.setStatus(rs.getString("Status"));
                attendance.setNote(rs.getString("Note"));
                attendance.setStudentName(rs.getString("StudentName"));

                attendances.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendances;
    }

    // Thêm điểm danh hàng loạt
    public boolean addBulkAttendance(int scheduleId, List<Attendance> attendances) {
        String sql = "INSERT INTO Attendance (ScheduleId, StudentId, Status, Note) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Attendance attendance : attendances) {
                    pstmt.setInt(1, attendance.getScheduleId());
                    pstmt.setInt(2, attendance.getStudentId());
                    pstmt.setString(3, attendance.getStatus());
                    pstmt.setString(4, attendance.getNote());
                    pstmt.addBatch();
                }

                int[] results = pstmt.executeBatch();
                conn.commit();

                // Kiểm tra tất cả đều thành công
                for (int result : results) {
                    if (result <= 0) {
                        return false;
                    }
                }
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Lỗi khi thêm điểm danh: " + e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật điểm danh
    public boolean updateAttendance(Attendance attendance) {
        String sql = "UPDATE Attendance SET Status = ?, Note = ? WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, attendance.getStatus());
            pstmt.setString(2, attendance.getNote());
            pstmt.setInt(3, attendance.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy lịch sử điểm danh của một lớp
    public List<Attendance> getAttendanceHistoryByClassId(int classId) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = """
            
                SELECT att.Id, att.ScheduleId, att.StudentId, att.Status, att.Note,
                                                     acc.FullName as StudentName,
                                                     sch.Date,
                                                     c.Name as ClassName,
                                                     course.Title as CourseTitle
                                              FROM Attendance att
                                              JOIN Student s ON att.StudentId = s.Id
                                              JOIN Account acc ON s.AccountId = acc.Id
                                              JOIN Schedule sch ON att.ScheduleId = sch.Id
                                              JOIN ClassRooms c ON sch.ClassRoomId = c.Id
                                              JOIN Course course ON c.CourseId = course.Id
                                              WHERE sch.ClassRoomId = ?
                                              ORDER BY sch.Date DESC, acc.FullName
            """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(rs.getInt("Id"));
                attendance.setScheduleId(rs.getInt("ScheduleId"));
                attendance.setStudentId(rs.getInt("StudentId"));
                attendance.setStatus(rs.getString("Status"));
                attendance.setNote(rs.getString("Note"));
                attendance.setStudentName(rs.getString("StudentName"));
                attendance.setClassName(rs.getString("ClassName"));
                attendance.setCourseTitle(rs.getString("CourseTitle"));
                Date date = rs.getDate("Date");
                attendance.setDate(date);
                attendances.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendances;
    }

    // Kiểm tra xem có học sinh mới chưa được điểm danh không
    public boolean hasNewStudentsForAttendance(int scheduleId, int classId) {
        String sql = """
        SELECT COUNT(*) as NewStudents
        FROM Student s
        JOIN Student_Class sc ON s.Id = sc.StudentId
        WHERE sc.ClassRoomId = ? 
        AND s.Id NOT IN (
            SELECT att.StudentId 
            FROM Attendance att 
            WHERE att.ScheduleId = ?
        )
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            pstmt.setInt(2, scheduleId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("NewStudents") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách học sinh với thông tin điểm danh (có thể chưa được điểm danh)
    public List<Student> getStudentsWithAttendanceStatus(int classId, int scheduleId) {
        List<Student> students = new ArrayList<>();
        String sql = """
        SELECT s.Id, a.FullName, s.ParentPhone,
               s.AccountId, s.EnrollmentDate,
               att.Id as AttendanceId, att.Status, att.Note
        FROM Student s
        JOIN Account a ON s.AccountId = a.Id
        JOIN Student_Class sc ON s.Id = sc.StudentId
        LEFT JOIN Attendance att ON s.Id = att.StudentId AND att.ScheduleId = ?
        WHERE sc.ClassRoomId = ?
        ORDER BY a.FullName
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);
            pstmt.setInt(2, classId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("Id"));
                student.setFullName(rs.getString("FullName"));
                student.setParentPhone(rs.getString("ParentPhone"));
                student.setAccountId(rs.getInt("AccountId"));
                student.setEnrollmentDate(rs.getDate("EnrollmentDate").toLocalDate());

                // Thông tin điểm danh (nếu có)
                int attendanceId = rs.getInt("AttendanceId");
                if (attendanceId > 0) {
                    student.setAttendanceId(attendanceId);
                    student.setAttendanceStatus(rs.getString("Status"));
                    student.setAttendanceNote(rs.getString("Note"));
                    student.setHasAttendance(true);
                } else {
                    student.setHasAttendance(false);
                    student.setAttendanceStatus("Present"); // Mặc định
                    student.setAttendanceNote("");
                }

                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Lấy thông tin lớp học theo Id
    public ClassRooms getClassRoomById(int classRoomId) {
        String sql = """
        SELECT c.Id, c.Name, c.CourseId, c.TeacherId, c.SlotId,
               course.Title as CourseTitle,
               slot.StartTime, slot.EndTime,
               (SELECT COUNT(*) FROM Student_Class sc WHERE sc.ClassRoomId = c.Id) as StudentCount
        FROM ClassRooms c
        JOIN Course course ON c.CourseId = course.Id
        JOIN Slot slot ON c.SlotId = slot.Id
        WHERE c.Id = ?
        """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, classRoomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ClassRooms classRoom = new ClassRooms();
                classRoom.setId(rs.getInt("Id"));
                classRoom.setName(rs.getString("Name"));
                classRoom.setCourseId(rs.getInt("CourseId"));
                classRoom.setTeacherId(rs.getInt("TeacherId"));
                classRoom.setSlotId(rs.getInt("SlotId"));
                classRoom.setCourseTitle(rs.getString("CourseTitle"));
                classRoom.setStartTime(rs.getTime("StartTime").toLocalTime());
                classRoom.setEndTime(rs.getTime("EndTime").toLocalTime());
                classRoom.setStudentCount(rs.getInt("StudentCount"));
                return classRoom;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy classRoomId từ scheduleId
    public int getClassIdByScheduleId(int scheduleId) {
        String sql = "SELECT ClassRoomId FROM Schedule WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scheduleId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ClassRoomId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}