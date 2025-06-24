/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-06-09
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-06-09  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.controller;

import jakarta.servlet.http.*;
import org.example.talentcenter.dao.ConsultationDAO;
import org.example.talentcenter.dao.RequestDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import org.example.talentcenter.model.Consultation;
import org.example.talentcenter.model.Student;

import java.io.IOException;

@WebServlet("/StudentAccountRequest")
public class StudentAccountRequestServlet extends HttpServlet {
    private final RequestDAO dao = new RequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        ConsultationDAO consultationDAO = new ConsultationDAO();
        request.setAttribute("agreedStudents", consultationDAO.getAgreedConsultations());
        request.getRequestDispatcher("/View/student-account-request.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int senderId = (Integer) request.getSession().getAttribute("accountId");
        ConsultationDAO dao = new ConsultationDAO();
        RequestDAO requestDao = new RequestDAO();
        String[] selectedIds = request.getParameterValues("selectedStudentIds");

        if (selectedIds != null && selectedIds.length > 0) {
            int successCount = 0;
            for (String idStr : selectedIds) {
                int id = Integer.parseInt(idStr);
                Consultation consult = dao.getById(id);
                if (consult != null) {
                    boolean success = requestDao.sendCreateAccountRequest(senderId, consult.getFullName(), consult.getEmail(), consult.getPhone());
                    if (success) successCount++;
                }
            }
            request.setAttribute("message", "Đã gửi yêu cầu cho " + successCount + " học sinh.");
        }
        request.getRequestDispatcher("View/student-account-request.jsp").forward(request, response);
    }

}
