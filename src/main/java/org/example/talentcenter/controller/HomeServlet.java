package org.example.talentcenter.controller;

import org.example.talentcenter.dao.CourseDAO;
import org.example.talentcenter.dao.ConsultationDAO;
import org.example.talentcenter.model.Consultation;
import org.example.talentcenter.model.Course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

import java.io.IOException;

@WebServlet(name="home", value="/home")
public class HomeServlet extends HttpServlet {
    private CourseDAO courseDAO = new CourseDAO();
    private ConsultationDAO consultationDAO = new ConsultationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Course> subjects = courseDAO.getAllCourses();
        request.setAttribute("subjects", subjects);
        if ("true".equals(request.getParameter("success"))) {
            request.setAttribute("message0", "Gửi tư vấn thành công!");
        }
        request.getRequestDispatcher("/View/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("addConsultation".equals(action)) {
            String name  = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String courseIdStr = request.getParameter("course_interest");

            Consultation consult = new Consultation();
            consult.setFullName(name);
            consult.setEmail(email);
            consult.setPhone(phone);
            try {
                consult.setCourseId(Integer.parseInt(courseIdStr));
            } catch (NumberFormatException e) {
                consult.setCourseId(0);
            }
            consultationDAO.addConsultation(consult);
            response.sendRedirect("home?success=true");
            return;
        }
        doGet(request, response);
    }
}
