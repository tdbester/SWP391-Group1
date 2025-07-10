package org.example.talentcenter.controller;

import jakarta.servlet.annotation.WebServlet;
import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet(name = "teacherRequest", value = "/teacherRequest")
public class TeacherRequestServlet extends HttpServlet {

    private TeacherDAO teacherDAO;
    private TeacherScheduleDAO scheduleDAO;
    private TeacherRequestDAO requestDAO;

    @Override
    public void init() throws ServletException {
        teacherDAO = new TeacherDAO();
        scheduleDAO = new TeacherScheduleDAO();
        requestDAO = new TeacherRequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            response.sendRedirect(request.getContextPath() + "/View/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "checkLeave":
                    handleCheckLeave(request, response, session);
                    break;
                case "checkChange":
                    handleCheckChange(request, response, session);
                    break;
                default:
                    request.getRequestDispatcher("/iew/teacher-request.jsp").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            response.sendRedirect(request.getContextPath() + "/View/login.jsp");
            return;
        }

        try {
            String type = request.getParameter("type");
            String reason = request.getParameter("reason");
            int senderId = (int) session.getAttribute("accountId");

            // Validate chung
            if (type == null || type.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng chọn loại đơn yêu cầu!");
                request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
                return;
            }

            if (reason == null || reason.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập lý do!");
                request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
                return;
            }

            // Xử lý theo từng loại đơn
            boolean success = false;
            String errorMessage = "";

            switch (type) {
                case "leave":
                    success = handleLeaveRequest(request, senderId, reason);
                    errorMessage = "Xin nghỉ phép thất bại!";
                    break;
                case "schedule_change":
                    success = handleScheduleChangeRequest(request, senderId, reason);
                    errorMessage = "Thay đổi lịch dạy thất bại!";
                    break;
                case "other":
                    success = handleOtherRequest(request, senderId, reason, type);
                    errorMessage = "Gửi đơn thất bại!";
                    break;
                default:
                    request.setAttribute("error", "Loại đơn không hợp lệ!");
                    request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
                    return;
            }

            if (success) {
                request.setAttribute("success", "Gửi đơn thành công! Vui lòng chờ phản hồi từ quản trị viên.");
            } else {
                request.setAttribute("error", errorMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại!");
        }

        request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
    }

    private void handleCheckLeave(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        String dateStr = request.getParameter("date");
        if (dateStr == null || dateStr.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn ngày nghỉ!");
            request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
            return;
        }

        try {
            LocalDate leaveDate = LocalDate.parse(dateStr);
            LocalDate today = LocalDate.now();

            // Kiểm tra ngày đã qua
            if (leaveDate.isBefore(today)) {
                request.setAttribute("warning", "Ngày bạn chọn đã qua!");
                request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin giáo viên
            int accountId = (int) session.getAttribute("accountId");
            Teacher teacher = teacherDAO.getTeacherByAccountId(accountId);

            if (teacher == null) {
                request.setAttribute("error", "Không tìm thấy thông tin giáo viên!");
                request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
                return;
            }

            // Lấy lịch học trong ngày
            ArrayList<Schedule> schedules = scheduleDAO.getScheduleByTeacherIdAndDate(
                    teacher.getId(), leaveDate);

            if (schedules.isEmpty()) {
                request.setAttribute("warning", "Ngày " + dateStr + " bạn không có lịch trình!");
            } else {
                request.setAttribute("leaveSchedules", schedules);
            }

            // Giữ lại các thông tin đã nhập
            request.setAttribute("type", "leave");
            request.setAttribute("leaveDate", dateStr);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ngày không hợp lệ!");
        }

        request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
    }

    private void handleCheckChange(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        String dateStr = request.getParameter("date");
        if (dateStr == null || dateStr.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn ngày muốn thay đổi!");
            request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
            return;
        }

        try {
            LocalDate changeDate = LocalDate.parse(dateStr);
            LocalDate today = LocalDate.now();

            // Kiểm tra ngày đã qua
            if (changeDate.isBefore(today)) {
                request.setAttribute("warning", "Ngày bạn chọn đã qua!");
                request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin giáo viên
            int accountId = (int) session.getAttribute("accountId");
            Teacher teacher = teacherDAO.getTeacherByAccountId(accountId);

            if (teacher == null) {
                request.setAttribute("error", "Không tìm thấy thông tin giáo viên!");
                request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
                return;
            }

            // Lấy lịch học trong ngày
            ArrayList<Schedule> schedules = scheduleDAO.getScheduleByTeacherIdAndDate(
                    teacher.getId(), changeDate);

            if (schedules.isEmpty()) {
                request.setAttribute("warning", "Ngày " + dateStr + " bạn không có lịch trình!");
            } else {
                request.setAttribute("changeSchedules", schedules);
            }

            // Giữ lại các thông tin đã nhập
            request.setAttribute("type", "schedule_change");
            request.setAttribute("changeFromDate", dateStr);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ngày không hợp lệ!");
        }

        request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
    }



    private boolean handleLeaveRequest(HttpServletRequest request, int senderId, String reason) {
        try {
            String dateStr = request.getParameter("leaveDate");
            if (dateStr == null || dateStr.trim().isEmpty()) {
                return false;
            }

            LocalDate leaveDate = LocalDate.parse(dateStr);
            LocalDate today = LocalDate.now();

            // Kiểm tra ngày đã qua
            if (leaveDate.isBefore(today)) {
                return false;
            }

            // Tạo đơn yêu cầu
            Request req = new Request();
            req.setTypeName("Xin nghỉ phép");
            req.setReason("Ngày nghỉ: " + dateStr + "\nLý do: " + reason);
            req.setSenderID(senderId);
            req.setStatus("Pending");

            return requestDAO.insertRequest(req);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean handleScheduleChangeRequest(HttpServletRequest request, int senderId, String reason) {
        try {
            String fromDateStr = request.getParameter("changeFromDate");
            String toDateStr = request.getParameter("changeToDate");
            String[] selectedSchedules = request.getParameterValues("selectedSchedules");

            if (fromDateStr == null || fromDateStr.trim().isEmpty()) {
                return false;
            }

            LocalDate fromDate = LocalDate.parse(fromDateStr);
            LocalDate today = LocalDate.now();

            // Kiểm tra ngày đã qua
            if (fromDate.isBefore(today)) {
                return false;
            }

            // Kiểm tra ngày chuyển sang
            if (toDateStr != null && !toDateStr.trim().isEmpty()) {
                LocalDate toDate = LocalDate.parse(toDateStr);
                if (toDate.isBefore(today)) {
                    return false;
                }
            }

            // Kiểm tra số lớp được chọn
            if (selectedSchedules == null || selectedSchedules.length == 0) {
                return false;
            }

            if (selectedSchedules.length > 1) {
                return false;
            }

            // Tạo lý do chi tiết
            StringBuilder detailReason = new StringBuilder();
            detailReason.append("Ngày muốn thay đổi: ").append(fromDateStr).append("\n");
            if (toDateStr != null && !toDateStr.trim().isEmpty()) {
                detailReason.append("Ngày muốn chuyển sang: ").append(toDateStr).append("\n");
            }
            detailReason.append("Các lớp được chọn: ").append(String.join(", ", selectedSchedules)).append("\n");
            detailReason.append("Lý do: ").append(reason);

            // Tạo đơn yêu cầu
            Request req = new Request();
            req.setTypeName("Thay đổi lịch dạy");
            req.setReason(detailReason.toString());
            req.setSenderID(senderId);
            req.setStatus("Pending");

            return requestDAO.insertRequest(req);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean handleOtherRequest(HttpServletRequest request, int senderId, String reason, String type) {
        try {
            // Tạo đơn yêu cầu
            Request req = new Request();
            req.setTypeName("Khác");
            req.setReason(reason);
            req.setSenderID(senderId);
            req.setStatus("Pending");

            return requestDAO.insertRequest(req);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}