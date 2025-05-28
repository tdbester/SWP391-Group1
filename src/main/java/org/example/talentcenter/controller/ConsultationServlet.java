package org.example.talentcenter.controller;

import org.example.talentcenter.dao.SubjectDAO;
import org.example.talentcenter.dao.ConsultationDAO;
import org.example.talentcenter.model.Consultation;
import org.example.talentcenter.model.Subject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import java.io.IOException;

@WebServlet(name = "ConsultationServlet", value = "/Consultation")
public class ConsultationServlet extends HttpServlet {
    private static final SubjectDAO subjectDAO = new SubjectDAO();
    private static final ConsultationDAO consultationDAO = new ConsultationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Subject> subjects = subjectDAO.getAllSubjects();
        request.setAttribute("subjects", subjects);
        request.getRequestDispatcher("/view/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String courseInterest = request.getParameter("course_interest");

        Consultation consultation = new Consultation(fullName, email, phone, courseInterest);
        try {
            consultationDAO.addConsultation(consultation);
            request.setAttribute("message", "Consultation request sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error sending consultation request.");
        }

        ArrayList<Subject> subjects = subjectDAO.getAllSubjects();
        request.setAttribute("subjects", subjects);
        request.getRequestDispatcher("/view/index.jsp").forward(request, response);
    }
}
