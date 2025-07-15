package org.example.talentcenter.controller;

import org.example.talentcenter.dao.TeacherDAO;
import org.example.talentcenter.model.Teacher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
        import java.io.IOException;
import java.util.List;

@WebServlet(name = "teacher", value = "/teachers")
public class TeacherManagerServlet extends HttpServlet {
    private TeacherDAO teacherDAO = new TeacherDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String action = req.getParameter("action");
        if (action == null) action = "list";

        if (session == null || session.getAttribute("accountId") == null) {
            resp.sendRedirect("login");
            return;
        }

        switch (action) {
            default: listTeachers(req, resp); break;
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
}