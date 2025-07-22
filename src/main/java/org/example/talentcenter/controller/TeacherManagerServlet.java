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
        String role = (String) session.getAttribute("userRole");

        if (session == null
                || session.getAttribute("accountId") == null
                || !role.equalsIgnoreCase("admin")) {
            resp.sendRedirect("login");
            return;
        }

        switch (action) {
            case "detail": showTeacherDetail(req, resp); break;
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

        String action = req.getParameter("action");
        if ("update".equals(action)) {
            updateTeacher(req, resp);
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
}