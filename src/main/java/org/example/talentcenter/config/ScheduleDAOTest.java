package org.example.talentcenter.config;

import java.sql.*;
import java.util.List;
import org.example.talentcenter.dao.ScheduleDAO;
import org.example.talentcenter.model.Schedule;

public class ScheduleDAOTest {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:sqlserver://localhost:1433;databaseName=SchoolManagement1;encrypt=true;trustServerCertificate=true";
        String dbUser = "sa";
        String dbPassword = "123";

        try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            ScheduleDAO dao = new ScheduleDAO(conn);

            int teacherId = 1; // Nhập ID của giáo viên có dữ liệu trong DB

            List<Schedule> schedules = dao.getScheduleByTeacherId(teacherId);

            for (Schedule s : schedules) {
                System.out.println("Date: " + s.getDate());
                System.out.println("Start: " + s.getStartTime());
                System.out.println("End: " + s.getEndTime());
                System.out.println("Room: " + s.getRoomCode());
                System.out.println("Class: " + s.getClassName());
                System.out.println("Course: " + s.getCourseTitle());
                System.out.println("------------------------------");
            }

            if (schedules.isEmpty()) {
                System.out.println("No schedule found for teacher ID: " + teacherId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
