package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.TeacherSalary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherSalaryDAO {

    private static final int RECORDS_PER_PAGE = 10;

    /**
     * Lấy danh sách lương giáo viên với phân trang và filter - FIXED
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
                // FIX: Tính toán FinalSalary đúng cách
                .append("CASE ")
                .append("  WHEN ts.Id IS NULL THEN 0 ")
                .append("  WHEN ts.FinalSalary IS NULL OR ts.FinalSalary = 0 THEN ")
                .append("    ISNULL(ts.TotalSalary, 0) + ISNULL(ts.Adjustment, 0) ")  // Tính từ TotalSalary + Adjustment
                .append("  ELSE ts.FinalSalary ")
                .append("END as FinalSalary, ")
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
     * Tính lương cho giáo viên theo tháng - FIXED
     */
    public void calculateSalaryForTeacher(int teacherId, int month, int year) throws SQLException {
        String checkExistsSql = "SELECT COUNT(*) FROM TeacherSalary WHERE TeacherId = ? AND Month = ? AND Year = ?";

        String insertSql = """
        INSERT INTO TeacherSalary (TeacherId, Month, Year, TotalSessions, BaseSalary, TotalSalary, FinalSalary, CreatedAt, UpdatedAt)
        VALUES (?, ?, ?, 0, 0, 0, 0, GETDATE(), GETDATE())
    """;

        String updateSessionsSql = """
        UPDATE ts
        SET TotalSessions = ISNULL(sessionCount.TotalSessions, 0),
            BaseSalary = t.Salary,
            TotalSalary = ISNULL(sessionCount.TotalSessions, 0) * t.Salary,
            FinalSalary = (ISNULL(sessionCount.TotalSessions, 0) * t.Salary) + ISNULL(ts.Adjustment, 0),
            UpdatedAt = GETDATE()
        FROM TeacherSalary ts
        JOIN Teacher t ON ts.TeacherId = t.Id
        LEFT JOIN (
            SELECT cr.TeacherId, COUNT(s.Id) as TotalSessions
            FROM ClassRooms cr
            LEFT JOIN Schedule s ON cr.Id = s.ClassRoomId 
                               AND MONTH(s.Date) = ? AND YEAR(s.Date) = ?
            WHERE cr.TeacherId = ?
            GROUP BY cr.TeacherId
        ) sessionCount ON t.Id = sessionCount.TeacherId
        WHERE ts.TeacherId = ? AND ts.Month = ? AND ts.Year = ?
    """;

        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra xem record đã tồn tại chưa
            try (PreparedStatement checkPs = conn.prepareStatement(checkExistsSql)) {
                checkPs.setInt(1, teacherId);
                checkPs.setInt(2, month);
                checkPs.setInt(3, year);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    // Update existing record - 5 tham số
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSessionsSql)) {
                        updatePs.setInt(1, month);    // MONTH(s.Date) = ?
                        updatePs.setInt(2, year);     // YEAR(s.Date) = ?
                        updatePs.setInt(3, teacherId); // WHERE cr.TeacherId = ?
                        updatePs.setInt(4, teacherId); // WHERE ts.TeacherId = ?
                        updatePs.setInt(5, month);    // ts.Month = ?
                        updatePs.setInt(6, year);     // ts.Year = ?
                        updatePs.executeUpdate();
                    }
                } else {
                    // Insert new record - 3 tham số
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        insertPs.setInt(1, teacherId);
                        insertPs.setInt(2, month);
                        insertPs.setInt(3, year);
                        insertPs.executeUpdate();
                    }

                    // Sau khi insert, update lại sessions
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSessionsSql)) {
                        updatePs.setInt(1, month);
                        updatePs.setInt(2, year);
                        updatePs.setInt(3, teacherId);
                        updatePs.setInt(4, teacherId);
                        updatePs.setInt(5, month);
                        updatePs.setInt(6, year);
                        updatePs.executeUpdate();
                    }
                }
            }
        }
    }

    /**
     * Tính lương cho tất cả giáo viên
     */
    public void calculateSalaryForAllTeachers(int month, int year) throws SQLException {
        String getTeachersSql = "SELECT Id FROM Teacher";

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
     * Điều chỉnh lương (thưởng/phạt) - FIXED
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

    public List<TeacherSalary> getPaidSalariesByTeacherId(int teacherId) throws SQLException {
        List<TeacherSalary> list = new ArrayList<>();

        String sql = """
        SELECT ts.*, 
               a.FullName AS ProcessedByName,
               t.Salary AS SalaryPerSession,
               ta.FullName AS TeacherName
        FROM TeacherSalary ts 
        LEFT JOIN Account a ON ts.ProcessedBy = a.Id 
        JOIN Teacher t ON ts.TeacherId = t.Id
        JOIN Account ta ON t.AccountId = ta.Id
        WHERE ts.TeacherId = ? AND ts.PaymentDate IS NOT NULL 
        ORDER BY ts.PaymentDate DESC
    """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TeacherSalary ts = new TeacherSalary();
                ts.setSalaryId(rs.getInt("Id"));
                ts.setTeacherId(rs.getInt("TeacherId"));
                ts.setTeacherName(rs.getString("TeacherName"));
                ts.setSalaryPerSession(rs.getDouble("SalaryPerSession"));
                ts.setMonth(rs.getInt("Month"));
                ts.setYear(rs.getInt("Year"));
                ts.setTotalSessions(rs.getInt("TotalSessions"));
                ts.setBaseSalary(rs.getDouble("BaseSalary"));
                ts.setTotalSalary(rs.getDouble("TotalSalary"));
                ts.setAdjustment(rs.getDouble("Adjustment"));
                ts.setFinalSalary(rs.getDouble("FinalSalary"));
                ts.setNote(rs.getString("Note"));
                ts.setPaymentDate(rs.getTimestamp("PaymentDate"));
                ts.setProcessedByName(rs.getString("ProcessedByName"));
                list.add(ts);
            }
        }
        return list;
    }

}