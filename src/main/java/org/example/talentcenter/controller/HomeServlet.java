package org.example.talentcenter.controller;

import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;
import org.example.talentcenter.service.NotificationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
            boolean success = consultationDAO.addConsultation(consult);
            if (success) {
                Course course = courseDAO.getCourseById(consult.getCourseId());
                String courseName = (course != null) ? course.getTitle() : "Không xác định";
                NotificationService.notifyNewConsultation(
                        name, email, phone, courseName, consult.getId()
                );
                response.sendRedirect("home?success=true");
            } else {
                response.sendRedirect("home?error=true");
            }
            return;
        }
        doGet(request, response);
    }
}
