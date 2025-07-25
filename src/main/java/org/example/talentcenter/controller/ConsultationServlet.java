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

package org.example.talentcenter.controller;

import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

import java.io.IOException;

@WebServlet(name = "ConsultationServlet", value = "/Consultation")
public class ConsultationServlet extends HttpServlet {
    private static final CourseDAO subjectDAO = new CourseDAO();
    private static final ConsultationDAO consultationDAO = new ConsultationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Kiểm tra session có tồn tại không
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Kiểm tra user có trong session không
        Object user = session.getAttribute("account");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Kiểm tra role có phải là "sale" không
        String role = (String) session.getAttribute("userRole");
        if (role == null || !"nhân viên sale".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        ArrayList<Course> subjects = (ArrayList<Course>) subjectDAO.getAllCourses();
        request.setAttribute("subjects", subjects);
        String action = request.getParameter("action");
        int page = 1, recordsPerPage = 10;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) page = Integer.parseInt(pageParam);
        } catch (NumberFormatException ignored) {
        }

        int offset = (page - 1) * recordsPerPage;

        if (action == null || action.equals("list") || action.equals("search") || action.equals("filterByStatus") || action.equals("filterByCourse")) {
            String keyword = request.getParameter("keyword");
            String statusFilter = request.getParameter("statusFilter");
            String courseFilter = request.getParameter("course_filter");

            // Tạo hàm lọc linh hoạt trong DAO theo nhiều điều kiện filter.
            int totalRecords = consultationDAO.countConsultationsFiltered(keyword, statusFilter, courseFilter);
            ArrayList<Consultation> consultations = consultationDAO.getConsultationsFiltered(
                    keyword, statusFilter, courseFilter, offset, recordsPerPage);

            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

            request.setAttribute("consultations", consultations);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("subjects", subjects);

            request.setAttribute("keyword", keyword);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("course_filter", courseFilter); // giữ lại các filter cho paging

            request.getRequestDispatcher("View/consultation-list.jsp").forward(request, response);
        } else {
            response.sendRedirect("Consultation?action=list");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        // Kiểm tra session có tồn tại không
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Kiểm tra user có trong session không
        Object user = session.getAttribute("account");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Kiểm tra role có phải là "sale" không
        String role = (String) session.getAttribute("userRole");
        if (role == null || !"nhân viên sale".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            if ("add".equals(action)) {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String course = request.getParameter("course_interest");
                String note = request.getParameter("note");

                Consultation consult = new Consultation();
                consult.setFullName(name);
                consult.setEmail(email);
                consult.setPhone(phone);
                consult.setStatus("Đang xử lý");
                consult.setPaymentStatus("Chưa thanh toán");
                consult.setNote(note);
                try {
                    consult.setCourseId(Integer.parseInt(course));
                } catch (NumberFormatException e) {
                    consult.setCourseId(0);
                }
                consultationDAO.addConsultation(consult);
                session.setAttribute("message", "Thêm học sinh thành công.");
                response.sendRedirect("Consultation");
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                int courseId = Integer.parseInt(request.getParameter("course_interest"));
                String note = request.getParameter("note");

                Consultation consult = new Consultation();
                consult.setId(id);
                consult.setFullName(name);
                consult.setEmail(email);
                consult.setPhone(phone);
                consult.setCourseId(courseId);
                consult.setNote(note);

                consultationDAO.updateConsultation(consult);

                session.setAttribute("message", "Cập nhật thành công.");
                response.sendRedirect("Consultation");
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                consultationDAO.deleteConsultation(id);
                session.setAttribute("message", "Xóa thành công.");
                response.sendRedirect("Consultation");
            } else if (action.equals("updateConsultationStatus")) {
                String idParam = request.getParameter("id");
                String status = request.getParameter("status");

                try {
                    int consultationId = Integer.parseInt(idParam);
                    boolean success = consultationDAO.updateStatus(consultationId, status);
                    if (success) {
                        System.out.println("Status updated successfully for ID: " + consultationId + " to: " + status);
                    } else {
                        System.out.println("Failed to update status for ID: " + consultationId);
                    }
                } catch (Exception e) {
                    System.out.println("Error updating consultation status: " + e.getMessage());
                    e.printStackTrace();
                }
                response.sendRedirect("Consultation?action=list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Lỗi xử lý dữ liệu!");
            request.getRequestDispatcher("View/consultation-list.jsp").forward(request, response);
        }
    }
}
