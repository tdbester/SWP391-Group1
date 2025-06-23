package org.example.talentcenter.controller;

import jakarta.servlet.http.*;
import org.example.talentcenter.dao.ConsultationDAO;
import org.example.talentcenter.dao.RequestDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

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
                var student = dao.getById(id);
                if (student != null) {
                    boolean success = requestDao.sendCreateAccountRequest(senderId, student.getFullName(), student.getEmail(), student.getPhone());
                    if (success) successCount++;
                }
            }
            request.setAttribute("message", "Đã gửi yêu cầu cho " + successCount + " học sinh.");
        } else {
            // TH2: Form nhập tay
            String studentName = request.getParameter("studentName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            boolean success = requestDao.sendCreateAccountRequest(senderId, studentName, email, phone);
            if (success) {
                request.setAttribute("message", "Gửi yêu cầu thành công!");
            } else {
                request.setAttribute("message", "Gửi yêu cầu thất bại!");
            }
        }

        request.getRequestDispatcher("View/student-account-request.jsp").forward(request, response);
    }

}
