package org.example.talentcenter.controller;

import org.example.talentcenter.dao.StudentAttendanceReportDAO;
import org.example.talentcenter.dao.StudentDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.StudentAttendanceReport;
import org.example.talentcenter.config.DBConnect;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "StudentAttendanceReportServlet", value = "/StudentAttendanceReport")
public class StudentAttendanceReportServlet extends HttpServlet {
    public static StudentDAO studentDAO = new StudentDAO();
    public static StudentAttendanceReportDAO studentAttendanceReportDAO = new StudentAttendanceReportDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }

        try (Connection conn = DBConnect.getConnection()) {
            int studentId = studentDAO.getStudentById(account.getId()).getId();
            List<StudentAttendanceReport> attendanceReports = studentAttendanceReportDAO.getAttendanceByStudentId(studentId);
            request.setAttribute("attendanceReports", attendanceReports);
            request.getRequestDispatcher("View/student-attendance-report.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }

    }
}