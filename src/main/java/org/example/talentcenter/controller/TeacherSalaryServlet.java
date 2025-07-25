package org.example.talentcenter.controller;

import org.example.talentcenter.dao.TeacherSalaryDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.TeacherSalary;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/teacher-salary")
public class TeacherSalaryServlet extends HttpServlet {

    private TeacherSalaryDAO teacherSalaryDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        teacherSalaryDAO = new TeacherSalaryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null || account.getRoleId() != 1) { // Chỉ Admin mới được truy cập
            response.sendRedirect("View/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("calculate".equals(action)) {
            handleCalculateSalary(request, response);
            return;
        } else if ("pay".equals(action)) {
            handlePaySalary(request, response, account);
            return;
        } else if ("edit".equals(action)) {
            handleEditSalary(request, response);
            return;
        } else if ("update".equals(action)) {
            handleUpdateSalary(request, response, account);
            return;
        }

        // Hiển thị danh sách lương
        displaySalaryList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void displaySalaryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy parameters
            int month = getIntParameter(request, "month", LocalDate.now().getMonthValue());
            int year = getIntParameter(request, "year", LocalDate.now().getYear());
            String searchName = request.getParameter("searchName");
            int currentPage = getIntParameter(request, "page", 1);

            // Tự động tính lương cho tất cả giáo viên nếu chưa có
            teacherSalaryDAO.calculateSalaryForAllTeachers(month, year);

            // Lấy dữ liệu
            List<TeacherSalary> salaries = teacherSalaryDAO.getTeacherSalaries(month, year, searchName, currentPage);
            int totalRecords = teacherSalaryDAO.getTotalRecords(month, year, searchName);
            int totalPages = (int) Math.ceil((double) totalRecords / 10);

            // Set attributes
            request.setAttribute("salaries", salaries);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("selectedMonth", month);
            request.setAttribute("selectedYear", year);
            request.setAttribute("searchName", searchName);
            request.setAttribute("totalRecords", totalRecords);

            request.getRequestDispatcher("View/teacher-salary.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi truy cập cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("View/teacher-salary.jsp").forward(request, response);
        }
    }

    private void handleCalculateSalary(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int month = getIntParameter(request, "month", LocalDate.now().getMonthValue());
            int year = getIntParameter(request, "year", LocalDate.now().getYear());

            teacherSalaryDAO.calculateSalaryForAllTeachers(month, year);

            response.sendRedirect("teacher-salary?month=" + month + "&year=" + year + "&message=calculate_success");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("teacher-salary?error=calculate_failed");
        }
    }

    private void handlePaySalary(HttpServletRequest request, HttpServletResponse response, Account admin) throws IOException {
        try {
            int teacherId = getIntParameter(request, "teacherId", 0);
            int month = getIntParameter(request, "month", 0);
            int year = getIntParameter(request, "year", 0);

            if (teacherId == 0 || month == 0 || year == 0) {
                response.sendRedirect("teacher-salary?error=invalid_parameters");
                return;
            }

            boolean success = teacherSalaryDAO.payTeacherSalary(teacherId, month, year, admin.getId());

            if (success) {
                response.sendRedirect("teacher-salary?month=" + month + "&year=" + year + "&message=pay_success");
            } else {
                response.sendRedirect("teacher-salary?month=" + month + "&year=" + year + "&error=pay_failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("teacher-salary?error=pay_error");
        }
    }

    private void handleEditSalary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int teacherId = getIntParameter(request, "teacherId", 0);
            int month = getIntParameter(request, "month", 0);
            int year = getIntParameter(request, "year", 0);

            if (teacherId == 0 || month == 0 || year == 0) {
                response.sendRedirect("teacher-salary?error=invalid_parameters");
                return;
            }

            TeacherSalary salary = teacherSalaryDAO.getTeacherSalaryById(teacherId, month, year);

            if (salary != null) {
                request.setAttribute("editSalary", salary);
                request.setAttribute("editMode", true);
            }

            displaySalaryList(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("teacher-salary?error=edit_error");
        }
    }

    private void handleUpdateSalary(HttpServletRequest request, HttpServletResponse response, Account admin) throws IOException {
        try {
            int teacherId = getIntParameter(request, "teacherId", 0);
            int month = getIntParameter(request, "month", 0);
            int year = getIntParameter(request, "year", 0);
            double adjustment = getDoubleParameter(request, "adjustment", 0);
            String note = request.getParameter("note");

            if (teacherId == 0 || month == 0 || year == 0) {
                response.sendRedirect("teacher-salary?error=invalid_parameters");
                return;
            }

            boolean success = teacherSalaryDAO.adjustSalary(teacherId, month, year, adjustment, note, admin.getId());

            if (success) {
                response.sendRedirect("teacher-salary?month=" + month + "&year=" + year + "&message=update_success");
            } else {
                response.sendRedirect("teacher-salary?month=" + month + "&year=" + year + "&error=update_failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("teacher-salary?error=update_error");
        }
    }

    private int getIntParameter(HttpServletRequest request, String paramName, int defaultValue) {
        String param = request.getParameter(paramName);
        if (param != null && !param.trim().isEmpty()) {
            try {
                return Integer.parseInt(param);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private double getDoubleParameter(HttpServletRequest request, String paramName, double defaultValue) {
        String param = request.getParameter(paramName);
        if (param != null && !param.trim().isEmpty()) {
            try {
                return Double.parseDouble(param);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
