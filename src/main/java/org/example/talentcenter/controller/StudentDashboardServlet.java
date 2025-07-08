package org.example.talentcenter.controller;

import org.example.talentcenter.dao.StudentScheduleDAO;
import org.example.talentcenter.dao.StudentDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Student;
import org.example.talentcenter.model.StudentSchedule;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@WebServlet(name = "StudentDashboardServlet", value = "/StudentDashboard")
public class StudentDashboardServlet extends HttpServlet {
    public static StudentDAO studentDAO = new StudentDAO();
    public static StudentScheduleDAO studentScheduleDAO = new StudentScheduleDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        Student student = studentDAO.getStudentById(account.getId());

        if (student == null) {
            request.setAttribute("errorMessage", "Không tìm thấy hồ sơ học sinh");
            request.getRequestDispatcher("View/error.jsp").forward(request, response);
            return;
        }

        int studentId = student.getId();

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        List<StudentSchedule> todaySchedules = studentScheduleDAO.getScheduleByStudentIdAndDate(studentId, today);

        request.setAttribute("student", student);
        request.setAttribute("todaySchedules", todaySchedules);
        request.getRequestDispatcher("View/student-dashboard.jsp").forward(request, response);
    }

}
