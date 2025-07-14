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
import org.example.talentcenter.dao.RequestDAO;
import org.example.talentcenter.dao.AccountDAO;
import org.example.talentcenter.model.Request;

import java.io.IOException;

@WebServlet("/CreateAccount")
public class CreateAccountServlet extends HttpServlet {
    private final RequestDAO dao = new RequestDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final SendAccountService service = new SendAccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("requests", dao.getAllAccountRequests());
        request.getRequestDispatcher("/View/account-request-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                    studentPhone
            );

            if (accountCreated) {
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