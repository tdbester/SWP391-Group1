/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-07-03
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-07-03  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.controller;

import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "StudentClassServlet", value = "/StudentClass")
public class StudentClassServlet extends HttpServlet {
    private StudentDAO studentDAO = new StudentDAO();
    private ClassroomDAO classroomDAO = new ClassroomDAO();

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
            String dateParam = request.getParameter("date");
            String slotIdParam = request.getParameter("slotId");
            LocalDate selectedDate = LocalDate.parse(dateParam);
            int slotId = Integer.parseInt(slotIdParam);
            List<StudentSchedule> classDetails = classroomDAO.getClassDetail(studentId, slotId, selectedDate);

            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = selectedDate.format(displayFormatter);

            request.setAttribute("classDetails", classDetails);
            request.setAttribute("selectedDate", selectedDate);
            request.setAttribute("slotId", slotId);
            request.setAttribute("formattedDate", formattedDate);

            request.getRequestDispatcher("View/student-class-detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
        doGet(request, response);
    }
}
