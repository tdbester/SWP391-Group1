
package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;
import org.example.talentcenter.service.NotificationService;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@WebServlet(name = "TeacherAbsenceRequestServlet", value = "/TeacherAbsenceRequest")
public class TeacherAbsenceRequestServlet extends HttpServlet {
    private final RequestDAO requestDAO = new RequestDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final TeacherDAO teacherDAO = new TeacherDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (role == null || !"giáo viên".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int teacherId = teacherDAO.getTeacherByAccountId(account.getId()).getId();

        List<ClassRooms> teacherClasses = attendanceDAO.getAllClassesByTeacherId(teacherId);
        Set<String> classNames = new HashSet<>();
        for (ClassRooms c : teacherClasses) classNames.add(c.getName().trim());

        String action = request.getParameter("action");
        if (action == null || action.equals("list")) {
            String statusFilter = request.getParameter("status");
            String keyword = request.getParameter("keyword");
            List<Request> allAbsenceRequests;
            if (keyword != null && !keyword.trim().isEmpty()) {
                allAbsenceRequests = requestDAO.searchAbsenceRequests(keyword.trim());
            } else if (statusFilter != null && !statusFilter.trim().isEmpty() && !"all".equalsIgnoreCase(statusFilter)) {
                allAbsenceRequests = requestDAO.getAbsenceRequestsByStatus(statusFilter.trim());
            } else {
                allAbsenceRequests = requestDAO.getAllAbsenceRequests();
            }
            List<Request> filtered = new ArrayList<>();
            for (Request r : allAbsenceRequests) {
                String[] parts = r.getReason() != null ? r.getReason().split("\\|") : new String[0];
                String className = parts.length > 0 ? parts[0].trim() : "";
                String reason = parts.length >= 4 ? parts[3].trim() : (r.getReason() != null ? r.getReason() : "");
                for (String teacherClass : classNames) {
                    if (teacherClass.equalsIgnoreCase(className)) {
                        r.setReason(reason);
                        filtered.add(r);
                        break;
                    }
                }
            }
            request.setAttribute("requestList", filtered);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("/View/teacher-absence-request-list.jsp").forward(request, response);
        } else if (action.equals("detail")) {
            int requestId = Integer.parseInt(request.getParameter("id"));
            Request req = requestDAO.getRequestDetailById(requestId);
            if (req == null) {
                request.setAttribute("errorMessage", "Không tìm thấy đơn");
                request.getRequestDispatcher("/View/teacher-absence-request-detail.jsp").forward(request, response);
                return;
            }
            String[] parts = req.getReason().split("\\|");
            String className = parts.length > 0 ? parts[0].trim() : "";

            request.setAttribute("requestDetail", req);
            request.setAttribute("className", className);
            request.getRequestDispatcher("/View/teacher-absence-request-detail.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (role == null || !"nhân viên sale".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        int teacherId = teacherDAO.getTeacherByAccountId(account.getId()).getId();
        List<ClassRooms> teacherClasses = attendanceDAO.getAllClassesByTeacherId(teacherId);
        Set<String> classNames = new HashSet<>();
        for (ClassRooms c : teacherClasses) classNames.add(c.getName().trim());

        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String action = request.getParameter("action");
        String note = request.getParameter("teacherNote");
        Request req = requestDAO.getRequestDetailById(requestId);
        if (req == null) {
            request.setAttribute("errorMessage", "Không tìm thấy đơn");
            request.getRequestDispatcher("/View/teacher-absence-request-detail.jsp").forward(request, response);
            return;
        } 
        String status = null;
        if ("approve".equals(action)) status = "Đã duyệt";
        else if ("reject".equals(action)) status = "Từ chối";
        if (status == null) {
            request.setAttribute("errorMessage", "Hành động không hợp lệ");
            request.getRequestDispatcher("/View/teacher-absence-request-detail.jsp").forward(request, response);
            return;
        }
        boolean ok = requestDAO.processRequest(requestId, status, note, new Timestamp(System.currentTimeMillis()));

        if (ok) {
            NotificationService.notifyRequestProcessed(requestId, req.getSenderName(), req.getTypeName(), status, note, req.getSenderID());
            response.sendRedirect("TeacherAbsenceRequest?action=list&success=1");
        } else {
            request.setAttribute("errorMessage", "Không thể cập nhật trạng thái đơn");
            request.getRequestDispatcher("/View/teacher-absence-request-detail.jsp").forward(request, response);
        }
    }
}