package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.talentcenter.dao.AccountRequestDAO;
import org.example.talentcenter.dao.AccountDAO;
import org.example.talentcenter.model.Request;

import java.io.IOException;

@WebServlet("/CreateAccount")
public class CreateAccountServlet extends HttpServlet {
    private final AccountRequestDAO dao = new AccountRequestDAO();
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
                System.out.println("Email exists, redirecting to error page.");
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
                // Gửi email thông báo tài khoản
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