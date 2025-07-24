package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.TeacherSalary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherSalaryDAO {

    private static final int RECORDS_PER_PAGE = 10;

    /**
     * Lấy danh sách lương giáo viên với phân trang và filter
     */
    public List<TeacherSalary> getTeacherSalaries(int month, int year, String searchName, int page) throws SQLException {
        List<TeacherSalary> salaries = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("WITH TeacherSalaryData AS (")
                .append("SELECT t.Id as TeacherId, a.FullName, t.Salary, ")
                .append("ISNULL(ts.Month, ?) as Month, ISNULL(ts.Year, ?) as Year, ")
                .append("ISNULL(ts.TotalSessions, 0) as TotalSessions, ")
                .append("ISNULL(ts.BaseSalary, t.Salary) as BaseSalary, ")
                .append("ISNULL(ts.TotalSalary, 0) as TotalSalary, ")
                .append("ISNULL(ts.Adjustment, 0) as Adjustment, ")
                .append("ISNULL(ts.FinalSalary, 0) as FinalSalary, ")
                .append("ts.PaymentDate, ts.Note, ts.Id as SalaryId, ")
                .append("pa.FullName as ProcessedByName ")
                .append("FROM Teacher t ")
                .append("JOIN Account a ON t.AccountId = a.Id ")
                .append("LEFT JOIN TeacherSalary ts ON t.Id = ts.TeacherId ")
                .append("AND ts.Month = ? AND ts.Year = ? ")
                .append("LEFT JOIN Account pa ON ts.ProcessedBy = pa.Id ")
                .append("WHERE 1=1 ");

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append("AND a.FullName LIKE ? ");
        }

        sql.append(") SELECT * FROM TeacherSalaryData ")
                .append("ORDER BY FullName ")
                .append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, month);  // Cho ISNULL Month
            ps.setInt(paramIndex++, year);   // Cho ISNULL Year
            ps.setInt(paramIndex++, month);  // Cho WHERE ts.Month
            ps.setInt(paramIndex++, year);   // Cho WHERE ts.Year

            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + searchName.trim() + "%");
            }

            ps.setInt(paramIndex++, (page - 1) * RECORDS_PER_PAGE);
            ps.setInt(paramIndex, RECORDS_PER_PAGE);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TeacherSalary salary = new TeacherSalary();
                salary.setTeacherId(rs.getInt("TeacherId"));
                salary.setTeacherName(rs.getString("FullName"));
                salary.setSalaryPerSession(rs.getDouble("Salary"));
                salary.setMonth(rs.getInt("Month"));
                salary.setYear(rs.getInt("Year"));
                salary.setTotalSessions(rs.getInt("TotalSessions"));
                salary.setBaseSalary(rs.getDouble("BaseSalary"));
                salary.setTotalSalary(rs.getDouble("TotalSalary"));
                salary.setAdjustment(rs.getDouble("Adjustment"));
                salary.setFinalSalary(rs.getDouble("FinalSalary"));
                salary.setPaymentDate(rs.getTimestamp("PaymentDate"));
                salary.setNote(rs.getString("Note"));
                salary.setSalaryId(rs.getObject("SalaryId") != null ? rs.getInt("SalaryId") : 0);
                salary.setProcessedByName(rs.getString("ProcessedByName"));

                salaries.add(salary);
            }
        }

        return salaries;
    }

    /**
     * Tính lương cho giáo viên theo tháng - phù hợp với cấu trúc bảng hiện tại
     */
    public void calculateSalaryForTeacher(int teacherId, int month, int year) throws SQLException {
        String checkExistsSql = "SELECT COUNT(*) FROM TeacherSalary WHERE TeacherId = ? AND Month = ? AND Year = ?";

        String insertSql = """
        INSERT INTO TeacherSalary (TeacherId, Month, Year, TotalSessions, BaseSalary, TotalSalary, FinalSalary, CreatedAt, UpdatedAt)
        SELECT ?, ?, ?, 
               COUNT(s.Id) as TotalSessions,
               t.Salary as BaseSalary,
               COUNT(s.Id) * t.Salary as TotalSalary,
               COUNT(s.Id) * t.Salary as FinalSalary,
               GETDATE(), GETDATE()
        FROM Teacher t
        LEFT JOIN ClassRooms cr ON t.Id = cr.TeacherId
        LEFT JOIN Schedule s ON cr.Id = s.ClassRoomId 
                           AND MONTH(s.Date) = ? AND YEAR(s.Date) = ?
        WHERE t.Id = ?
        GROUP BY t.Id, t.Salary
    """;

        String updateSql = """
        UPDATE TeacherSalary 
        SET TotalSessions = (
                SELECT COUNT(s.Id)
                FROM ClassRooms cr
                LEFT JOIN Schedule s ON cr.Id = s.ClassRoomId 
                                   AND MONTH(s.Date) = ? AND YEAR(s.Date) = ?
                WHERE cr.TeacherId = ?
            ),
            BaseSalary = (SELECT Salary FROM Teacher WHERE Id = ?),
            TotalSalary = (
                SELECT COUNT(s.Id) * t.Salary
                FROM Teacher t
                LEFT JOIN ClassRooms cr ON t.Id = cr.TeacherId
                LEFT JOIN Schedule s ON cr.Id = s.ClassRoomId 
                                   AND MONTH(s.Date) = ? AND YEAR(s.Date) = ?
                WHERE t.Id = ?
                GROUP BY t.Id, t.Salary
            ),
            FinalSalary = CASE 
                WHEN PaymentDate IS NULL THEN (
                    SELECT COUNT(s.Id) * t.Salary
                    FROM Teacher t
                    LEFT JOIN ClassRooms cr ON t.Id = cr.TeacherId
                    LEFT JOIN Schedule s ON cr.Id = s.ClassRoomId 
                                       AND MONTH(s.Date) = ? AND YEAR(s.Date) = ?
                    WHERE t.Id = ?
                    GROUP BY t.Id, t.Salary
                ) + ISNULL(Adjustment, 0)
                ELSE FinalSalary 
            END,
            UpdatedAt = GETDATE()
        WHERE TeacherId = ? AND Month = ? AND Year = ?
    """;

        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra xem record đã tồn tại chưa
            try (PreparedStatement checkPs = conn.prepareStatement(checkExistsSql)) {
                checkPs.setInt(1, teacherId);
                checkPs.setInt(2, month);
                checkPs.setInt(3, year);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    // Update existing record
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                        updatePs.setInt(1, month);
                        updatePs.setInt(2, year);
                        updatePs.setInt(3, teacherId);
                        updatePs.setInt(4, teacherId);
                        updatePs.setInt(5, month);
                        updatePs.setInt(6, year);
                        updatePs.setInt(7, teacherId);
                        updatePs.setInt(8, month);
                        updatePs.setInt(9, year);
                        updatePs.setInt(10, teacherId);
                        updatePs.setInt(11, teacherId);
                        updatePs.setInt(12, month);
                        updatePs.setInt(13, year);
                        updatePs.executeUpdate();
                    }
                } else {
                    // Insert new record
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        insertPs.setInt(1, teacherId);
                        insertPs.setInt(2, month);
                        insertPs.setInt(3, year);
                        insertPs.setInt(4, month);
                        insertPs.setInt(5, year);
                        insertPs.setInt(6, teacherId);
                        insertPs.executeUpdate();
                    }
                }
            }
        }
    }

    /**
     * Tính lương cho tất cả giáo viên - bỏ điều kiện Status
     */
    public void calculateSalaryForAllTeachers(int month, int year) throws SQLException {
        String getTeachersSql = "SELECT Id FROM Teacher"; // Bỏ WHERE Status = 1

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(getTeachersSql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int teacherId = rs.getInt("Id");
                calculateSalaryForTeacher(teacherId, month, year);
            }
        }
    }

    /**
     * Đếm tổng số record cho phân trang
     */
    public int getTotalRecords(int month, int year, String searchName) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM Teacher t ")
                .append("JOIN Account a ON t.AccountId = a.Id ")
                .append("WHERE 1=1 ");

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append("AND a.FullName LIKE ? ");
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(1, "%" + searchName.trim() + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    /**
     * Thanh toán lương cho giáo viên
     */
    public boolean payTeacherSalary(int teacherId, int month, int year, int processedBy) throws SQLException {
        String sql = """
            UPDATE TeacherSalary 
            SET PaymentDate = GETDATE(), ProcessedBy = ?, UpdatedAt = GETDATE()
            WHERE TeacherId = ? AND Month = ? AND Year = ? AND PaymentDate IS NULL
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, processedBy);
            ps.setInt(2, teacherId);
            ps.setInt(3, month);
            ps.setInt(4, year);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Điều chỉnh lương (thưởng/phạt)
     */
    public boolean adjustSalary(int teacherId, int month, int year, double adjustment, String note, int processedBy) throws SQLException {
        String sql = """
            UPDATE TeacherSalary 
            SET Adjustment = ?, 
                FinalSalary = TotalSalary + ?, 
                Note = ?, 
                ProcessedBy = ?,
                UpdatedAt = GETDATE()
            WHERE TeacherId = ? AND Month = ? AND Year = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, adjustment);
            ps.setDouble(2, adjustment);
            ps.setString(3, note);
            ps.setInt(4, processedBy);
            ps.setInt(5, teacherId);
            ps.setInt(6, month);
            ps.setInt(7, year);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy thông tin lương của 1 giáo viên
     */
    public TeacherSalary getTeacherSalaryById(int teacherId, int month, int year) throws SQLException {
        String sql = """
            SELECT ts.*, t.Salary as SalaryPerSession, a.FullName as TeacherName
            FROM TeacherSalary ts
            JOIN Teacher t ON ts.TeacherId = t.Id
            JOIN Account a ON t.AccountId = a.Id
            WHERE ts.TeacherId = ? AND ts.Month = ? AND ts.Year = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ps.setInt(2, month);
            ps.setInt(3, year);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TeacherSalary salary = new TeacherSalary();
                salary.setTeacherId(rs.getInt("TeacherId"));
                salary.setTeacherName(rs.getString("TeacherName"));
                salary.setSalaryPerSession(rs.getDouble("SalaryPerSession"));
                salary.setMonth(rs.getInt("Month"));
                salary.setYear(rs.getInt("Year"));
                salary.setTotalSessions(rs.getInt("TotalSessions"));
                salary.setBaseSalary(rs.getDouble("BaseSalary"));
                salary.setTotalSalary(rs.getDouble("TotalSalary"));
                salary.setAdjustment(rs.getDouble("Adjustment"));
                salary.setFinalSalary(rs.getDouble("FinalSalary"));
                salary.setPaymentDate(rs.getTimestamp("PaymentDate"));
                salary.setNote(rs.getString("Note"));
                return salary;
            }
        }

        return null;
    }
}
