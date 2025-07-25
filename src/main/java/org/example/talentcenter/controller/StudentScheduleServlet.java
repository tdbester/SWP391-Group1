/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-06-18
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-06-15  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.controller;

import org.example.talentcenter.dao.StudentScheduleDAO;
import org.example.talentcenter.dao.StudentDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.StudentSchedule;
import org.example.talentcenter.config.DBConnect;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "StudentScheduleServlet", value = "/StudentSchedule")
public class StudentScheduleServlet extends HttpServlet {
    public static StudentDAO studentDAO = new StudentDAO();
    public static StudentScheduleDAO studentScheduleDAO = new StudentScheduleDAO();

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

            List<StudentSchedule> schedules = studentScheduleDAO.getScheduleByStudentIdAndWeek(
                    studentId, selectedWeek, endOfWeek);

            WeekFields weekFields = WeekFields.of(Locale.getDefault());

            LocalDate lastDayOfYear = LocalDate.of(selectedYear, 12, 31);
            int totalWeeksInYear = lastDayOfYear.get(weekFields.weekOfYear());

            if (totalWeeksInYear < 10) {
                totalWeeksInYear = 52;
            }
            request.setAttribute("schedules", schedules);
            request.setAttribute("selectedWeek", selectedWeek);
            request.setAttribute("selectedYear", selectedYear);
            request.setAttribute("selectedWeekNumber", selectedWeekNumber);
            request.setAttribute("totalWeeksInYear", totalWeeksInYear);

            request.getRequestDispatcher("View/student-schedule.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}