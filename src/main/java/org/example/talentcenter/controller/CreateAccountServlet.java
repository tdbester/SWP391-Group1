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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.talentcenter.dao.CourseDAO;
import org.example.talentcenter.dao.RequestDAO;
import org.example.talentcenter.dao.AccountDAO;
import org.example.talentcenter.model.*;
import org.example.talentcenter.service.NotificationService;

import java.io.IOException;

@WebServlet("/CreateAccount")
public class CreateAccountServlet extends HttpServlet {
    private final RequestDAO dao = new RequestDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final SendAccountService service = new SendAccountService();
    private final CourseDAO courseDAO = new CourseDAO();

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
        if (role == null || !"admin".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("requests", dao.getAllAccountRequests());
        request.getRequestDispatcher("/View/account-request-list.jsp").forward(request, response);
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
        if (role == null || !"admin".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int requestId = Integer.parseInt(request.getParameter("id"));

            Request accountRequest = dao.getRequestById(requestId);
            if (accountRequest == null) {
                response.sendRedirect("CreateAccount?error=request_not_found");
                return;
            }

            String[] parts = accountRequest.getReason().split("\\|");
            if (parts.length < 2) {
                response.sendRedirect("CreateAccount?error=invalid_data");
                return;
            }

            String studentName = parts[0];
            String studentEmail = parts[1];
            String studentPhone = parts.length > 2 ? parts[2] : "";
            int courseId = 0;
            Integer consultationId = null;
            if (parts.length > 3) {
                try {
                    courseId = Integer.parseInt(parts[3].trim());
                    consultationId = Integer.parseInt(parts[4].trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    response.sendRedirect("CreateAccount?error=invalid_id");
                }
            }
            if (parts.length > 4) {
                try {
                    consultationId = Integer.parseInt(parts[4].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi parse consultationId: " + parts[4]);
                }
            }

            if (studentName == null || studentEmail == null || studentPhone == null) {
                response.sendRedirect("CreateAccount?error=invalid_data");
                return;
            }

            String tuitionAmount = courseDAO.getTuitionByCourseId(courseId);
            if (accountDAO.isUsernameExists(studentEmail)) {
                System.out.println("Email đã tồn tại");
                response.sendRedirect("CreateAccount?error=email_exists");
                return;
            }

            String randomPassword = service.generateRandomPassword();
            boolean accountCreated = accountDAO.createStudentAccount(
                    randomPassword,
                    studentName,
                    studentEmail,
                    studentPhone,
                    consultationId
            );


            if (accountCreated) {
                int studentAccountId = accountDAO.getAccountIdByEmail(studentEmail);

                if (studentAccountId > 0 && tuitionAmount != null && !tuitionAmount.isEmpty()) {
                    NotificationService.notifyStudentTuitionFee(
                            studentAccountId,
                            studentName,
                            tuitionAmount
                    );
                }

                boolean emailSent = service.sendNewAccountEmail(
                        studentEmail,
                        studentEmail,
                        randomPassword,
                        studentName
                );

                if (emailSent) {
                    dao.markAsCreated(requestId);
                    response.sendRedirect("CreateAccount?success=account_created");
                } else {
                    response.sendRedirect("CreateAccount?error=email_failed");
                }
            } else {
                response.sendRedirect("CreateAccount?error=account_creation_failed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("CreateAccount?error=invalid_id");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CreateAccount?error=system_error");
        }
    }
}