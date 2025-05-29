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
        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            ArrayList<Consultation> consultations = consultationDAO.getAllConsultations();
            request.setAttribute("consultations", consultations);
            request.setAttribute("subjects", subjects);
            request.getRequestDispatcher("view/consultation-list.jsp").forward(request, response);

        } else if (action.equals("edit")) {
            String idRaw = request.getParameter("id");
            try {
                int id = Integer.parseInt(idRaw);
                Consultation consult = consultationDAO.getById(id);
                if (consult == null) {
                    response.sendRedirect("Consultation?action=list");
                    return;
                }
                request.setAttribute("consult", consult);  // phải đặt đúng tên "consult" cho edit jsp
                request.setAttribute("subjects", subjects);
                request.getRequestDispatcher("view/edit-consultation.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect("Consultation?action=list");
            }
        } else if (action.equals("search")) {
            String keyword = request.getParameter("keyword");
            if (keyword == null || keyword.trim().isEmpty()) {
                response.sendRedirect("Consultation?action=list");
                return;
            }
            ArrayList<Consultation> consultations = consultationDAO.searchConsultations(keyword.trim());
            request.setAttribute("consultations", consultations);
            request.setAttribute("keyword", keyword);
            request.setAttribute("subjects", subjects);
            request.getRequestDispatcher("view/consultation-list.jsp").forward(request, response);

        } else if (action.equals("filterByContacted")) {
            String contactedStr = request.getParameter("contacted_filter");
            if (contactedStr == null || contactedStr.trim().isEmpty()) {
                response.sendRedirect("Consultation?action=list");
                return;
            }
            ArrayList<Consultation> consultations = consultationDAO.filterConsultationsByContactedStatus(contactedStr.trim());
            request.setAttribute("consultations", consultations);
            request.setAttribute("contacted_filter", contactedStr);
            request.setAttribute("subjects", subjects);
            request.getRequestDispatcher("view/consultation-list.jsp").forward(request, response);


        }else if (action.equals("filterByCourse")) {
            String courseFilter = request.getParameter("course_filter");
            if (courseFilter == null || courseFilter.trim().isEmpty()) {
                response.sendRedirect("Consultation?action=list");
                return;
            }
            ArrayList<Consultation> consultations = consultationDAO.filterConsultationsByCourse(courseFilter.trim());
            request.setAttribute("consultations", consultations);
            request.setAttribute("subjects", subjects);
            request.setAttribute("course_filter", courseFilter);
            request.getRequestDispatcher("view/consultation-list.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("Consultation?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String course = request.getParameter("course_interest");

                Consultation consult = new Consultation();
                consult.setFullName(name);
                consult.setEmail(email);
                consult.setPhone(phone);
                consult.setCourseInterest(course);
                consult.setContacted(false);

                consultationDAO.addConsultation(consult);
                request.setAttribute("message", "Thêm học sinh thành công.");
                response.sendRedirect("Consultation");
                return;
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String course = request.getParameter("course_interest");
                boolean contacted = request.getParameter("contacted") != null;

                Consultation consult = new Consultation();
                consult.setId(id);
                consult.setFullName(name);
                consult.setEmail(email);
                consult.setPhone(phone);
                consult.setCourseInterest(course);
                consult.setContacted(contacted);

                consultationDAO.updateConsultation(consult);
                request.setAttribute("message", "Cập nhật thành công.");
                response.sendRedirect("Consultation");
                return;
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                consultationDAO.deleteConsultation(id);
                request.setAttribute("message", "Xóa thành công.");
                response.sendRedirect("Consultation");
                return;
            } else if ("updateContacted".equals(action)) {
                String idRaw = request.getParameter("id");
                String contactedParam = request.getParameter("contacted"); // checkbox checked gửi "on", unchecked thì null
                try {
                    int id = Integer.parseInt(idRaw);
                    boolean contacted = contactedParam != null; // checked -> true, unchecked -> false
                    boolean updated = consultationDAO.updateContactedStatus(id, contacted);
                    if (updated) {
                        response.sendRedirect("Consultation?action=list");
                    } else {
                        request.setAttribute("error", "Cập nhật trạng thái thất bại");
                        request.getRequestDispatcher("view/edit-consultation.jsp").forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect("Consultation?action=list");
                }
            }  else {
                response.sendRedirect("Consultation");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Lỗi xử lý dữ liệu!");
            request.getRequestDispatcher("view/consultation-list.jsp").forward(request, response);
            return;
        }
    }
}
