package org.example.talentcenter.config;

import org.example.talentcenter.dao.BlogDAO;
import org.example.talentcenter.dto.BlogDto;
import org.example.talentcenter.model.Blog;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DBTest {
    public static void main(String[] args) {
//        try (Connection conn = DBConnect.getConnection()) {
//            if (conn != null) {
//                System.out.println("✅ Kết nối SQL Server thành công!");
//            } else {
//                System.out.println("❌ Không thể kết nối!");
//            }
//        } catch (SQLException e) {
//            System.out.println("❌ Lỗi kết nối:");
//            e.printStackTrace();
//        }
        BlogDAO blogDAO = new BlogDAO();
        List<BlogDto> blog = blogDAO.getAll();
        for (BlogDto blogDto : blog) {
            System.out.println(blogDto.getTitle());
        }
    }
}