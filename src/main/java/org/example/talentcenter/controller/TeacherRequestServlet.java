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
        Account account = (Account) session.getAttribute("account");
        if (session == null || account == null) {
            response.sendRedirect(request.getContextPath() + "/View/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        ArrayList<Request> requestTypeList = requestDAO.getTeacherRequestType();
        request.setAttribute("requestTypeList", requestTypeList);

        if (action != null) {
            switch (action) {
                case "checkLeave":
                    handleCheckLeave(request, response, session);
                    break;
                case "checkChange":
                    handleCheckChange(request, response, session);
                    break;
                default:
                    request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("teacherName", account.getFullName());
            request.setAttribute("phoneNumber", account.getPhoneNumber());

            request.getRequestDispatcher("/View/teacher-request.jsp").forward(request, response);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            HttpSession session = request.getSession(false);
            Account account = (Account) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            try {
                String type = request.getParameter("type");
                String reason = request.getParameter("reason");
                Integer senderId = account.getId();

                // Validation
                if (type == null || type.trim().isEmpty()) {
                    session.setAttribute("error", "Vui lòng chọn loại đơn yêu cầu!");
                    redirectWithFormData(request, response);
                    return;
                }

                if (reason == null || reason.trim().length() < 10) {
                    session.setAttribute("error", "Lý do phải có ít nhất 10 ký tự!");
                    redirectWithFormData(request, response);
                    return;
                }

                // Tạo combined reason
                String combinedReason = createCombinedReason(request, type, reason);

                // Lấy TypeID
                int typeId = getTypeIdFromValue(type);
                if (typeId == 0) {
                    session.setAttribute("error", "Loại đơn không hợp lệ!");
                    redirectWithFormData(request, response);
                    return;
                }

                // Tạo request object
                Request teacherRequest = new Request();
                teacherRequest.setSenderID(senderId);
                teacherRequest.setReason(combinedReason);
                teacherRequest.setTypeId(typeId);
                teacherRequest.setStatus("Chờ xử lý");

                // Lưu vào database
                boolean success = requestDAO.insertRequest(teacherRequest);

                if (success) {
                    session.setAttribute("success", "Gửi đơn thành công!");
                    response.sendRedirect("teacherRequest");
                } else {
                    session.setAttribute("error", "Gửi đơn thất bại!");
                    redirectWithFormData(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
                redirectWithFormData(request, response);
            }
        }
        else {
            doGet(request, response);
        }
    }

    private String createCombinedReason(HttpServletRequest request, String type, String reason) {
        switch (type) {
            case "leave":
                String leaveDate = request.getParameter("leaveDate");
                return (leaveDate != null ? leaveDate : "") + "|" + reason;

            case "schedule_change":
                String fromDate = request.getParameter("changeFromDate");
                String toDate = request.getParameter("changeToDate");
                String[] selectedSchedules = request.getParameterValues("selectedSchedules");

                StringBuilder detailReason = new StringBuilder();
                detailReason.append(fromDate != null ? fromDate : "").append("|");
                detailReason.append(toDate != null ? toDate : "").append("|");
                if (selectedSchedules != null && selectedSchedules.length > 0) {
                    detailReason.append(String.join(",", selectedSchedules)).append("|");
                } else {
                    detailReason.append("|");
                }
                detailReason.append(reason);
                return detailReason.toString();

            default:
                return reason;
        }
    }

    private int getTypeIdFromValue(String typeValue) {
        switch (typeValue) {
            case "leave":
                return requestDAO.getRequestTypeId("Đơn xin nghỉ phép");
            case "schedule_change":
                return requestDAO.getRequestTypeId("Đơn xin đổi lịch dạy");
            case "other":
                return requestDAO.getRequestTypeId("Đơn khác");
            default:
                return 0;
        }
    }

    private void redirectWithFormData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder redirectUrl = new StringBuilder("teacherRequest?");

        String type = request.getParameter("type");
        if (type != null) redirectUrl.append("type=").append(type).append("&");

        String reason = request.getParameter("reason");
        if (reason != null) redirectUrl.append("reason=").append(java.net.URLEncoder.encode(reason, "UTF-8")).append("&");

        response.sendRedirect(redirectUrl.toString());
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
            Account account = (Account) session.getAttribute("account");
            Teacher teacher = teacherDAO.getTeacherByAccountId(account.getId());

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

            ArrayList<Request> requestTypeList = requestDAO.getTeacherRequestType();
            request.setAttribute("requestTypeList", requestTypeList);
            request.setAttribute("teacherName", account.getFullName());
            request.setAttribute("phoneNumber", account.getPhoneNumber());

            request.setAttribute("type", "leave");
            request.setAttribute("leaveDate", dateStr);
            request.setAttribute("reason", request.getParameter("reason"));
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
            request.setAttribute("changeToDate", request.getParameter("changeToDate"));
            request.setAttribute("reason", request.getParameter("reason"));


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
            req.setTypeName("Đơn xin nghỉ phép");
            req.setReason("Ngày nghỉ: " + dateStr + "\nLý do: " + reason);
            req.setSenderID(senderId);
            req.setStatus("Chờ xử lý");

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
            req.setTypeName("Đơn xin thay đổi lịch dạy");
            req.setReason(detailReason.toString());
            req.setSenderID(senderId);
            req.setStatus("Chờ xử lý");

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
            req.setStatus("Chờ xử lý");

            return requestDAO.insertRequest(req);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}