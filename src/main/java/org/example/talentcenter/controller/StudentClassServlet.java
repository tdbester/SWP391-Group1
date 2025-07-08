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

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("View/login.jsp");
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
        doGet(request, response);
    }
}
