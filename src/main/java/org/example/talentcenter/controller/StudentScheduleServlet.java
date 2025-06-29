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

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("View/login.jsp");
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

                // Tính toán ngày đầu tuần dựa trên năm và số tuần
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                selectedWeek = LocalDate.of(selectedYear, 1, 4)  // ISO standard: tuần 1 chứa ngày 4/1
                        .with(weekFields.weekOfYear(), selectedWeekNumber)
                        .with(DayOfWeek.MONDAY);
            } else {
                selectedWeek = LocalDate.now().with(DayOfWeek.MONDAY);
                selectedYear = selectedWeek.getYear();
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                selectedWeekNumber = selectedWeek.get(weekFields.weekOfYear());
            }
            LocalDate endOfWeek = selectedWeek.plusDays(6);

            // Lấy lịch học cho tuần được chọn
            List<StudentSchedule> schedules = studentScheduleDAO.getScheduleByStudentIdAndWeek(
                    studentId, selectedWeek, endOfWeek);

            // Tạo danh sách tất cả các tuần trong năm để hiển thị trong dropdown
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            LocalDate firstDayOfYear = LocalDate.of(selectedYear, 1, 1);

            // Tính số tuần trong năm (thường là 52 hoặc 53)
            LocalDate lastDayOfYear = LocalDate.of(selectedYear, 12, 31);
            int totalWeeksInYear = lastDayOfYear.get(weekFields.weekOfYear());

            // Nếu tuần cuối cùng của năm có số tuần nhỏ (vd: tuần 1), thì năm có 52 tuần
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