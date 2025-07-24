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
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Consultation;
import org.example.talentcenter.model.Student;
import org.example.talentcenter.service.NotificationService;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/StudentAccountRequest")
public class StudentAccountRequestServlet extends HttpServlet {
    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final RequestDAO dao = new RequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.equals("list") || action.equals("search") || action.equals("filterByStatus")) {
            int page = 1;
            int recordsPerPage = 10;
            if (request.getParameter("page") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("page"));
                } catch (NumberFormatException ignored) {
                }
            }

            String keyword = request.getParameter("keyword");
            String statusFilter = request.getParameter("statusFilter");

            // Đếm tổng số bản ghi
            int totalRecords = consultationDAO.countAgreedConsultationsFiltered(keyword, statusFilter);

            ArrayList<Consultation> agreedStudents = consultationDAO.getAgreedConsultationsFiltered(keyword, statusFilter, (page - 1) * recordsPerPage, recordsPerPage);

            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

            request.setAttribute("agreedStudents", agreedStudents);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("keyword", keyword);
            request.setAttribute("statusFilter", statusFilter);
            request.getRequestDispatcher("View/student-account-request.jsp").forward(request, response);
        } else {
            response.sendRedirect("StudentAccountRequest?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int senderId = (Integer) request.getSession().getAttribute("accountId");
        String action = request.getParameter("action");
        String[] selectedIds = request.getParameterValues("selectedStudentIds");

        try {
            if ("sentRequest".equals(action)) {
                if (selectedIds != null && selectedIds.length > 0) {
                    int successCount = 0;
                    HttpSession session = request.getSession();
                    Account saleAccount = (Account) session.getAttribute("account");

                    for (String idStr : selectedIds) {
                        int id = Integer.parseInt(idStr);
                        Consultation consult = consultationDAO.getById(id);
                        if (consult != null) {
                            boolean success = dao.sendCreateAccountRequest(senderId, consult.getFullName(), consult.getEmail(), consult.getPhone(), consult.getCourseId());
                            if (success) {
                                consultationDAO.updateAccountRequestSentStatus(id, true);
                                successCount++;
                                NotificationService.notifyAccountCreationRequest(
                                        saleAccount.getFullName(),
                                        consult.getFullName(),
                                        consult.getEmail(),
                                        0
                                );
                            }
                        }
                    }

                    request.setAttribute("message", "Đã gửi yêu cầu cho " + successCount + " học sinh.");
                } else {
                    request.setAttribute("message", "Vui lòng chọn ít nhất một học sinh để gửi yêu cầu.");
                }

                // Reload danh sách sau khi gửi
                int page = 1;
                int recordsPerPage = 10;
                int offset = (page - 1) * recordsPerPage;
                ArrayList<Consultation> agreedStudents = consultationDAO.getAgreedConsultationsWithPaging(offset, recordsPerPage);
                int totalRecords = consultationDAO.getTotalAgreedConsultationCount();
                int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

                request.setAttribute("agreedStudents", agreedStudents);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("View/student-account-request.jsp").forward(request, response);
            } else if ("updatePaymentStatus".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String status = request.getParameter("status");

                boolean updated = consultationDAO.updatePaymentStatus(id, status);

                // Load lại danh sách
                int page = 1;
                int recordsPerPage = 10;
                int offset = (page - 1) * recordsPerPage;
                ArrayList<Consultation> agreedStudents = consultationDAO.getAgreedConsultationsWithPaging(offset, recordsPerPage);
                int totalRecords = consultationDAO.getTotalAgreedConsultationCount();
                int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

                request.setAttribute("agreedStudents", agreedStudents);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("message", updated ? "Đã cập nhật trạng thái." : "Cập nhật thất bại.");
                request.getRequestDispatcher("View/student-account-request.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("View/student-account-request.jsp").forward(request, response);
        }
    }

}
