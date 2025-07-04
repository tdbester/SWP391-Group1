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

import org.example.talentcenter.dao.CourseDAO;
import org.example.talentcenter.dao.ConsultationDAO;
import org.example.talentcenter.dto.CourseDto;
import org.example.talentcenter.model.Consultation;
import org.example.talentcenter.model.Course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ConsultationServlet", value = "/Consultation")
public class ConsultationServlet extends HttpServlet {
    private static final CourseDAO subjectDAO = new CourseDAO();
    private static final ConsultationDAO consultationDAO = new ConsultationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Course> subjects = subjectDAO.getAll();
        request.setAttribute("subjects", subjects);
        String action = request.getParameter("action");
        String view = request.getParameter("view");
        if (action == null || action.equals("list")) {
            int page = 1;
            int recordsPerPage = 10;
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null) page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }

            int offset = (page - 1) * recordsPerPage;
            ArrayList<Consultation> consultations = consultationDAO.getConsultationsWithPaging(offset, recordsPerPage);
            int totalRecords = consultationDAO.getTotalConsultationCount();
            int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

            request.setAttribute("consultations", consultations);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("subjects", subjects);
            request.getRequestDispatcher("View/consultation-list.jsp").forward(request, response);
            return;
        } else if (action.equals("edit")) {
            String idRaw = request.getParameter("id");
            try {
                int id = Integer.parseInt(idRaw);
                Consultation consult = consultationDAO.getById(id);
                if (consult == null) {
                    response.sendRedirect("Consultation?action=list");
                    return;
                }
                request.setAttribute("consult", consult);
                request.setAttribute("subjects", subjects);
                request.getRequestDispatcher("View/edit-consultation.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect("Consultation?action=list");
            }
        } else if (action.equals("search")) {
            String keyword = request.getParameter("keyword");
            if (keyword == null || keyword.trim().isEmpty()) {
                response.sendRedirect("Consultation?action=list");
                return;
            }
            ArrayList<Consultation> consultations = consultationDAO.searchConsultations(keyword.trim());
            request.setAttribute("consultations", consultations);
            request.setAttribute("keyword", keyword);
            request.setAttribute("subjects", subjects);
            request.getRequestDispatcher("View/consultation-list.jsp").forward(request, response);
        } else if (action.equals("filterByCourse")) {
            String courseFilter = request.getParameter("course_filter");
            if (courseFilter == null || courseFilter.trim().isEmpty()) {
                response.sendRedirect("Consultation?action=list");
                return;
            }
            ArrayList<Consultation> consultations = consultationDAO.filterConsultationsByCourse(courseFilter.trim());
            request.setAttribute("consultations", consultations);
            request.setAttribute("subjects", subjects);
            request.setAttribute("course_filter", courseFilter);
            request.getRequestDispatcher("View/consultation-list.jsp").forward(request, response);
        } else if (action.equals("filterByStatus")) {
            String statusFilter = request.getParameter("statusFilter");
            if (statusFilter == null || statusFilter.trim().isEmpty()) {
                response.sendRedirect("Consultation?action=list");
                return;
            }
            ArrayList<Consultation> consultations = consultationDAO.filterConsultationsByStatus(statusFilter.trim());
            request.setAttribute("consultations", consultations);
            request.setAttribute("subjects", subjects);
            request.setAttribute("statusFilter", statusFilter);
            request.getRequestDispatcher("View/consultation-list.jsp").forward(request, response);
        } else if ("dashboard".equals(action)) {
            request.getRequestDispatcher("View/sale-dashboard.jsp").forward(request, response);
            return;
        } else {
            response.sendRedirect("Consultation?action=list");
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String course = request.getParameter("course_interest");

                Consultation consult = new Consultation();
                consult.setFullName(name);
                consult.setEmail(email);
                consult.setPhone(phone);
                try {
                    consult.setCourseId(Integer.parseInt(course));
                } catch (NumberFormatException e) {
                    consult.setCourseId(0);
                }
                consultationDAO.addConsultation(consult);
                HttpSession session = request.getSession();
                session.setAttribute("message", "Thêm học sinh thành công.");
                response.sendRedirect("Consultation");
                return;
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String course = request.getParameter("course_interest");

                Consultation consult = new Consultation();
                consult.setId(id);
                consult.setFullName(name);
                consult.setEmail(email);
                consult.setPhone(phone);
                consult.setCourseId(Integer.parseInt(course));

                consultationDAO.updateConsultation(consult);
                Consultation updatedConsult = consultationDAO.getById(id);
                request.setAttribute("consult", updatedConsult);
                request.setAttribute("subjects", subjectDAO.getAll());
                HttpSession session = request.getSession();
                session.setAttribute("message", "Cập nhật thành công.");
                request.getRequestDispatcher("View/edit-consultation.jsp").forward(request, response);
                return;
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                consultationDAO.deleteConsultation(id);
                HttpSession session = request.getSession();
                session.setAttribute("message", "Xóa thành công.");
                response.sendRedirect("Consultation");
                return;
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
            return;
        }
    }
}
