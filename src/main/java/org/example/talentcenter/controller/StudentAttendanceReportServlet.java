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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "StudentAttendanceReportServlet", value = "/StudentAttendanceReport")
public class StudentAttendanceReportServlet extends HttpServlet {
    public static StudentDAO studentDAO = new StudentDAO();
    public static StudentAttendanceReportDAO studentAttendanceReportDAO = new StudentAttendanceReportDAO();
    public static ClassroomDAO classroomDAO = new ClassroomDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }
        try {
            int studentId = studentDAO.getStudentById(account.getId()).getId();

            String selectedClass = request.getParameter("class");
            String yearParam = request.getParameter("year");
            String weekParam = request.getParameter("week");

            LocalDate selectedWeek;
            int selectedYear;
            int selectedWeekNumber;

            if (yearParam != null && weekParam != null) {
                selectedYear = Integer.parseInt(yearParam);
                selectedWeekNumber = Integer.parseInt(weekParam);

                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                selectedWeek = LocalDate.of(selectedYear, 1, 1)
                        .with(weekFields.weekOfYear(), selectedWeekNumber)
                        .with(DayOfWeek.MONDAY);
            } else {
                selectedWeek = LocalDate.now().with(DayOfWeek.MONDAY);
                selectedYear = selectedWeek.getYear();
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                selectedWeekNumber = selectedWeek.get(weekFields.weekOfYear());
            }

            LocalDate endOfWeek = selectedWeek.plusDays(6);

            List<StudentAttendanceReport> attendanceReports = studentAttendanceReportDAO.getAttendanceByStudentId(studentId);
            if (selectedClass != null && !selectedClass.isEmpty()) {
                attendanceReports.removeIf(r -> !r.getClassName().equalsIgnoreCase(selectedClass));
            }

            attendanceReports.removeIf(r -> r.getDate().isBefore(selectedWeek) || r.getDate().isAfter(endOfWeek));

            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            LocalDate lastDayOfYear = LocalDate.of(selectedYear, 12, 31);
            int totalWeeksInYear = lastDayOfYear.get(weekFields.weekOfYear());
            if (totalWeeksInYear < 10) totalWeeksInYear = 52;

            List<String> classNames = classroomDAO.getClassNamesByStudentId(studentId);
            request.setAttribute("classNames", classNames);
            request.setAttribute("selectedClass", selectedClass);
            request.setAttribute("attendanceReports", attendanceReports);
            request.setAttribute("selectedWeek", selectedWeek);
            request.setAttribute("selectedYear", selectedYear);
            request.setAttribute("selectedWeekNumber", selectedWeekNumber);
            request.setAttribute("totalWeeksInYear", totalWeeksInYear);

            request.getRequestDispatcher("View/student-attendance-report.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}