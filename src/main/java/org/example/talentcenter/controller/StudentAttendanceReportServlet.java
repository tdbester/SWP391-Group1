/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-06-21
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-06-21  | Cù Thị Huyền Trang   | Initial creation
 */
package org.example.talentcenter.controller;

import org.example.talentcenter.dao.StudentAttendanceReportDAO;
import org.example.talentcenter.dao.ClassroomDAO;
import org.example.talentcenter.dao.StudentDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.StudentAttendanceReport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "StudentAttendanceReportServlet", value = "/StudentAttendanceReport")
public class StudentAttendanceReportServlet extends HttpServlet {
    public static StudentDAO studentDAO = new StudentDAO();
    public static StudentAttendanceReportDAO studentAttendanceReportDAO = new StudentAttendanceReportDAO();
    public static ClassroomDAO classroomDAO = new ClassroomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (role == null || !"học sinh".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int studentId = studentDAO.getStudentById(account.getId()).getId();

            String selectedClass = request.getParameter("class");
            String monthParam = request.getParameter("month");
            Integer selectedMonth = null;
            if (monthParam != null && !monthParam.isEmpty()) {
                selectedMonth = Integer.parseInt(monthParam);
            }

            // Lấy tất cả báo cáo điểm danh của sinh viên
            List<StudentAttendanceReport> attendanceReports = studentAttendanceReportDAO.getAttendanceByStudentId(studentId);

            final String classFilter = selectedClass;
            final Integer monthFilter = selectedMonth;

            if (classFilter != null && !classFilter.isEmpty()) {
                attendanceReports.removeIf(r -> !r.getClassName().equalsIgnoreCase(classFilter));
            }

            if (monthFilter != null) {
                attendanceReports.removeIf(r -> r.getDate().getMonthValue() != monthFilter);
            }

            List<String> classNames = classroomDAO.getClassNamesByStudentId(studentId);
            request.setAttribute("classNames", classNames);
            request.setAttribute("selectedClass", selectedClass);
            request.setAttribute("attendanceReports", attendanceReports);
            request.setAttribute("selectedMonth", selectedMonth);

            request.getRequestDispatcher("View/student-attendance-report.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
