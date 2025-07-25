package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.StudentClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * DAO class for managing StudentClass operations
 * Handles the junction table between Student and ClassRooms
 * 
 * @author Training Manager Feature
 */
public class StudentClassDAO {


    /**
     * Thêm nhiều học sinh vào lớp học trong một transaction.
     * Đảm bảo tính toàn vẹn dữ liệu - nếu có lỗi thì rollback tất cả.
     *
     * @param studentIds Danh sách ID của các học sinh
     * @param classroomId ID của lớp học
     * @return true nếu thêm tất cả thành công, false nếu có lỗi
     */
    public boolean addMultipleStudentsToClass(ArrayList<Integer> studentIds, int classroomId) {
        if (studentIds == null || studentIds.isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO Student_Class (ClassRoomId, StudentId, JoinDate) VALUES (?, ?, GETDATE())";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            ps = conn.prepareStatement(sql);
            
            for (Integer studentId : studentIds) {
                ps.setInt(1, classroomId);
                ps.setInt(2, studentId);
                ps.addBatch();
            }
            
            int[] results = ps.executeBatch();
            
            // Kiểm tra kết quả
            for (int result : results) {
                if (result <= 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm nhiều học sinh vào lớp: " + e.getMessage());
            e.printStackTrace();
            
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Lỗi khi rollback: " + rollbackEx.getMessage());
                }
            }
            return false;
            
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Khôi phục auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng connection: " + e.getMessage());
            }
        }
    }

    /**
     * Kiểm tra xem học sinh đã có trong lớp học chưa.
     *
     * @param studentId ID của học sinh
     * @param classroomId ID của lớp học
     * @return true nếu học sinh đã có trong lớp, false nếu chưa
     */
    public boolean isStudentInClass(int studentId, int classroomId) {
        String sql = "SELECT COUNT(*) FROM Student_Class WHERE StudentId = ? AND ClassRoomId = ?";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, studentId);
            ps.setInt(2, classroomId);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra học sinh trong lớp: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
