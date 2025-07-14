/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-05-29
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-05-29  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.dao;

import org.example.talentcenter.model.Course;
import org.example.talentcenter.config.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseDAO {
    /**
     * Lấy toàn bộ danh sách các khóa học từ cơ sở dữ liệu.
     *
     * @return Danh sách các khóa học bao gồm Id, tiêu đề, giá, thông tin mô tả và người tạo.
     * @author Huyen Trang
     */
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> subjects = new ArrayList<>();
        String query = "SELECT Id, Title, Price, Information, CreatedBy FROM Course";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course subject = new Course(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getDouble("Price"),
                        rs.getString("Information"),
                        rs.getInt("CreatedBy")
                );
                subjects.add(subject);
            }
            System.out.println("Số lượng khóa học: " + subjects.size());
        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return subjects;
    }

    /**
     * Trả về thông tin chi tiết của một khóa học dựa trên ID.
     *
     * @param courseId ID của khóa học cần lấy.
     * @return Đối tượng Course nếu tìm thấy, ngược lại trả về null.
     * @author Huyen Trang
     */
    public Course getCourseById(int courseId) {
        String query = "SELECT Id, Title, Price, Information, CreatedBy FROM Course WHERE Id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Course(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getDouble("Price"),
                        rs.getString("Information"),
                        rs.getInt("CreatedBy")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL getCourseById: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lấy danh sách các khóa học mới nhất kèm số lượng lớp học tương ứng, giới hạn theo số lượng chỉ định.
     *
     * @param limit Số lượng khóa học tối đa cần lấy.
     * @return Danh sách các đối tượng Course mới nhất.
     * @author Huyen Trang
     */
    public ArrayList<Course> getLatestCourses(int limit) {
        ArrayList<Course> courses = new ArrayList<>();
        String query = """
                    SELECT TOP (?) 
                        c.Id, 
                        c.Title, 
                        c.Price, 
                        c.Information, 
                        c.CreatedBy,
                        COUNT(cr.Id) as ClassCount
                    FROM Course c
                    LEFT JOIN ClassRooms cr ON c.Id = cr.CourseId
                    GROUP BY c.Id, c.Title, c.Price, c.Information, c.CreatedBy
                    ORDER BY c.Id DESC
                """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getDouble("Price"),
                        rs.getString("Information"),
                        rs.getInt("CreatedBy"),
                        rs.getInt("ClassCount")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

}
