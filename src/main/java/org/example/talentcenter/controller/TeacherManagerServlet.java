package org.example.talentcenter.controller;

import org.example.talentcenter.dao.CourseDAO;
import org.example.talentcenter.dao.TeacherDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.ClassRooms;
import org.example.talentcenter.model.Course;
import org.example.talentcenter.model.Teacher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
        import java.io.IOException;
import java.util.List;

@WebServlet(name = "teacher", value = "/teachers")
public class TeacherManagerServlet extends HttpServlet {
    private TeacherDAO teacherDAO = new TeacherDAO();
    private CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String action = req.getParameter("action");
        if (action == null) action = "list";

        // Check session and authentication
        if (session == null || session.getAttribute("accountId") == null) {
            resp.sendRedirect("login");
            return;
        }

        String role = (String) session.getAttribute("userRole");

        // Check role-based authorization for all actions
        if (!hasRequiredRole(role, "admin")) {
            resp.sendRedirect("login");
            return;
        }

        switch (action) {
            case "detail": showTeacherDetail(req, resp); break;
            case "new": showNewTeacherForm(req, resp); break;
            case "edit": showEditTeacherForm(req, resp); break;
            case "delete": deleteTeacher(req, resp); break;
            default: listTeachers(req, resp); break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            resp.sendRedirect("login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (!hasRequiredRole(role, "admin")) {
            resp.sendRedirect("login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "update": updateTeacher(req, resp); break;
            case "insert": insertTeacher(req, resp); break;
            default: resp.sendRedirect("teachers"); break;
        }
    }

    private void listTeachers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String search = req.getParameter("search");

        int index = 1;
        try {
            index = Integer.parseInt(req.getParameter("index"));
        } catch (Exception ignored) { }

        List<Teacher> all = teacherDAO.pagingTeacher(index);
        var filtered = all.stream()
                .filter(t -> search == null
                        || t.getAccount().getFullName().toLowerCase().contains(search.toLowerCase())
                        || t.getDepartment().toLowerCase().contains(search.toLowerCase()))
                .toList();

        int total = teacherDAO.getTotalTeacher();
        int endPage = (int)Math.ceil((double) total / 10);

        req.setAttribute("teacherList", filtered);
        req.setAttribute("endP", endPage);
        req.setAttribute("currentIndex", index);

        // Handle success/error messages from previous operations
        String successMessage = req.getParameter("success");
        String errorMessage = req.getParameter("error");

        if (successMessage != null) {
            switch (successMessage) {
                case "created":
                    req.setAttribute("successMessage", "Giáo viên đã được thêm thành công!");
                    break;
                case "updated":
                    req.setAttribute("successMessage", "Thông tin giáo viên đã được cập nhật thành công!");
                    break;
                case "deleted":
                    req.setAttribute("successMessage", "Giáo viên đã được xóa thành công!");
                    break;
            }
        }

        if (errorMessage != null) {
            switch (errorMessage) {
                case "TeacherNotFound":
                    req.setAttribute("errorMessage", "Không tìm thấy giáo viên!");
                    break;
                case "InvalidID":
                    req.setAttribute("errorMessage", "ID giáo viên không hợp lệ!");
                    break;
                case "DeleteFailed":
                    req.setAttribute("errorMessage", "Không thể xóa giáo viên! Giáo viên có thể đang được phân công lớp học.");
                    break;
                case "CreateFailed":
                    req.setAttribute("errorMessage", "Không thể thêm giáo viên!");
                    break;
                case "UpdateFailed":
                    req.setAttribute("errorMessage", "Cập nhật thông tin thất bại!");
                    break;
                case "InvalidData":
                    req.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
                    break;
                case "EmailExists":
                    req.setAttribute("errorMessage", "Email đã tồn tại trong hệ thống!");
                    break;
            }
        }

        req.getRequestDispatcher("/View/teacher-list.jsp").forward(req, resp);
    }

    private void showTeacherDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Teacher teacher = teacherDAO.getTeacherById(id);
            if (teacher != null) {
                // Get teacher's classes
                List<ClassRooms> classes = teacherDAO.getClassesByTeacherId(id);

                // Get all courses to display course names
                List<Course> allCourses = courseDAO.getAll();

                req.setAttribute("teacher", teacher);
                req.setAttribute("classes", classes);
                req.setAttribute("allCourses", allCourses);
                req.getRequestDispatcher("/View/teacher-detail.jsp").forward(req, resp);
            } else {
                resp.sendRedirect("teachers?error=TeacherNotFound");
            }
        } catch (NumberFormatException ex) {
            resp.sendRedirect("teachers?error=InvalidID");
        }
    }

    private void updateTeacher(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int teacherId = Integer.parseInt(req.getParameter("teacherId"));
            int accountId = Integer.parseInt(req.getParameter("accountId"));

            Teacher teacher = new Teacher();
            teacher.setId(teacherId);
            teacher.setAccountId(accountId);
            teacher.setDepartment(req.getParameter("department"));
            teacher.setSalary(Double.parseDouble(req.getParameter("salary")));

            Account account = new Account();
            account.setId(accountId);
            account.setFullName(req.getParameter("fullName"));
            account.setPhoneNumber(req.getParameter("phoneNumber"));
            account.setAddress(req.getParameter("address"));
            account.setEmail(req.getParameter("email"));
            teacher.setAccount(account);

            boolean success = teacherDAO.updateTeacher(teacher);
            if (success) {
                resp.sendRedirect("teachers?action=detail&id=" + teacherId + "&success=true");
            } else {
                resp.sendRedirect("teachers?action=detail&id=" + teacherId + "&error=UpdateFailed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("teachers?error=InvalidData");
        }
    }

    private void showNewTeacherForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/View/teacher-form.jsp").forward(req, resp);
    }

    private void showEditTeacherForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Teacher teacher = teacherDAO.getTeacherById(id);
            if (teacher != null) {
                req.setAttribute("teacher", teacher);
                req.getRequestDispatcher("/View/teacher-form.jsp").forward(req, resp);
            } else {
                resp.sendRedirect("teachers?error=TeacherNotFound");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect("teachers?error=InvalidID");
        }
    }

    private void insertTeacher(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Validate required fields
            String fullName = req.getParameter("fullName");
            String email = req.getParameter("email");
            String phoneNumber = req.getParameter("phoneNumber");
            String address = req.getParameter("address");
            String department = req.getParameter("department");
            String salaryStr = req.getParameter("salary");

            if (fullName == null || fullName.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phoneNumber == null || phoneNumber.trim().isEmpty() ||
                department == null || department.trim().isEmpty() ||
                salaryStr == null || salaryStr.trim().isEmpty()) {
                resp.sendRedirect("teachers?error=InvalidData");
                return;
            }

            double salary;
            try {
                salary = Double.parseDouble(salaryStr);
                if (salary < 0) {
                    resp.sendRedirect("teachers?error=InvalidData");
                    return;
                }
            } catch (NumberFormatException e) {
                resp.sendRedirect("teachers?error=InvalidData");
                return;
            }

            // Check if email already exists
            if (teacherDAO.isEmailExists(email.trim())) {
                resp.sendRedirect("teachers?error=EmailExists");
                return;
            }

            // Create Account object
            Account account = new Account();
            account.setFullName(fullName.trim());
            account.setEmail(email.trim());
            account.setPassword("123456"); // Default password
            account.setPhoneNumber(phoneNumber.trim());
            account.setAddress(address != null ? address.trim() : "");

            // Create Teacher object
            Teacher teacher = new Teacher();
            teacher.setAccount(account);
            teacher.setDepartment(department.trim());
            teacher.setSalary(salary);

            boolean success = teacherDAO.addTeacher(teacher);
            if (success) {
                resp.sendRedirect("teachers?success=created");
            } else {
                resp.sendRedirect("teachers?error=CreateFailed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("teachers?error=InvalidData");
        }
    }

    private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            boolean success = teacherDAO.deleteTeacher(id);
            if (success) {
                resp.sendRedirect("teachers?success=deleted");
            } else {
                resp.sendRedirect("teachers?error=DeleteFailed");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect("teachers?error=InvalidID");
        } catch (Exception e) {
            resp.sendRedirect("teachers?error=DeleteFailed");
        }
    }

    private boolean hasRequiredRole(String userRole, String requiredRole) {
        return userRole != null && userRole.equalsIgnoreCase(requiredRole);
    }
}