package org.example.talentcenter.controller;

import org.example.talentcenter.dao.AccountDAO;
import org.example.talentcenter.dao.ApprovedRequestDAO;
import org.example.talentcenter.dao.TeacherSalaryDAO;
import org.example.talentcenter.model.Request;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@WebServlet("/teacher-notification")
public class TeacherNotificationServlet extends HttpServlet {

    private ApprovedRequestDAO approvedRequestDAO;
    private TeacherSalaryDAO teacherSalaryDAO;
    private AccountDAO accountDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        accountDAO = new AccountDAO();
        approvedRequestDAO = new ApprovedRequestDAO();
        teacherSalaryDAO = new TeacherSalaryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra session và lấy thông tin teacher hiện tại
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("account");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/View/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("detail".equals(action)) {
            showRequestDetail(request, response, currentUser.getId());
        } else {
            showNotificationList(request, response, currentUser.getId());
        }
    }

    private void showNotificationList(HttpServletRequest request, HttpServletResponse response, int accountId)
            throws ServletException, IOException {

        try {
            // *** FIX: Lấy teacherId từ accountId ***
            int teacherId = accountDAO.getTeacherIdByAccountId(accountId);
            if (teacherId == -1) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Teacher not found");
                return;
            }

            // Lấy parameters từ request
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            String searchKeyword = request.getParameter("searchKeyword");

            // Tạo danh sách thông báo tổng hợp
            List<NotificationItem> allNotifications = new ArrayList<>();

            // 1. Lấy thông báo từ approved requests
            List<Request> approvedRequests;
            if (hasFilter(dateFrom, dateTo, searchKeyword)) {
                approvedRequests = approvedRequestDAO.getApprovedRequestsWithFilterByTeacher(
                        accountId, null, dateFrom, dateTo, searchKeyword); // Vẫn dùng accountId cho requests
            } else {
                approvedRequests = approvedRequestDAO.getApprovedRequestsWithFilterByTeacher(
                        accountId, null, null, null, null);
            }

            // Chuyển đổi requests thành notification items
            for (Request req : approvedRequests) {
                NotificationItem item = new NotificationItem();
                item.setContent(req.getReason());
                item.setNotification(req.getResponse());
                item.setProcessedBy(req.getProcessedByName());
                item.setResponseDate(req.getResponseAt());
                item.setType("REQUEST");
                allNotifications.add(item);
            }

            // 2. *** FIX: Dùng teacherId thay vì accountId ***
            List<TeacherSalary> paidSalaries = teacherSalaryDAO.getPaidSalariesByTeacherId(teacherId);

            for (TeacherSalary salary : paidSalaries) {
                // Áp dụng filter cho salary notifications
                if (matchesSalaryFilter(salary, dateFrom, dateTo, searchKeyword)) {
                    NotificationItem item = new NotificationItem();

                    item.setContent("Lương tháng " + salary.getMonth() + "/" + salary.getYear() +
                            ": " + String.format("%,.0f", salary.getFinalSalary()) + " VND");

                    item.setNotification(salary.getNote() != null ? salary.getNote() : "Thanh toán lương");
                    item.setProcessedBy(salary.getProcessedByName() != null ?
                            salary.getProcessedByName() : "Giám đốc trung tâm");
                    item.setResponseDate(salary.getPaymentDate());
                    item.setType("SALARY");
                    allNotifications.add(item);
                }
            }

            // 3. Sắp xếp theo ngày phản hồi (mới nhất trước)
            Collections.sort(allNotifications, new Comparator<NotificationItem>() {
                @Override
                public int compare(NotificationItem o1, NotificationItem o2) {
                    if (o1.getResponseDate() == null && o2.getResponseDate() == null) return 0;
                    if (o1.getResponseDate() == null) return 1;
                    if (o2.getResponseDate() == null) return -1;
                    return o2.getResponseDate().compareTo(o1.getResponseDate());
                }
            });

            // Set attributes cho JSP
            request.setAttribute("allNotifications", allNotifications);
            request.setAttribute("filteredCount", allNotifications.size());
            request.setAttribute("currentFilter", createCurrentFilter(null, dateFrom, dateTo, searchKeyword));

            // Forward to JSP
            request.getRequestDispatcher("/View/teacher-notification.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }



    private boolean matchesSalaryFilter(TeacherSalary salary, String dateFrom, String dateTo, String searchKeyword) {
        // Filter by date range
        if (dateFrom != null && !dateFrom.trim().isEmpty()) {
            try {
                Date fromDate = java.sql.Date.valueOf(dateFrom);
                if (salary.getPaymentDate() != null && salary.getPaymentDate().before(fromDate)) {
                    return false;
                }
            } catch (IllegalArgumentException e) {
                // Invalid date format, ignore filter
            }
        }

        if (dateTo != null && !dateTo.trim().isEmpty()) {
            try {
                Date toDate = java.sql.Date.valueOf(dateTo);
                // Add 1 day to include the entire day
                toDate = new Date(toDate.getTime() + 24 * 60 * 60 * 1000);
                if (salary.getPaymentDate() != null && salary.getPaymentDate().after(toDate)) {
                    return false;
                }
            } catch (IllegalArgumentException e) {
                // Invalid date format, ignore filter
            }
        }

        // Filter by search keyword
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            String keyword = searchKeyword.toLowerCase();
            String content = "Lương tháng " + salary.getMonth() + "/" + salary.getYear() +
                    ": " + String.format("%,.0f", salary.getFinalSalary()) + " VND";
            String note = salary.getNote() != null ? salary.getNote() : "Thanh toán lương";

            if (!content.toLowerCase().contains(keyword) &&
                    !note.toLowerCase().contains(keyword)) {
                return false;
            }
        }

        return true;
    }

    private void showRequestDetail(HttpServletRequest request, HttpServletResponse response, int teacherId)
            throws ServletException, IOException {

        try {
            int requestId = Integer.parseInt(request.getParameter("id"));
            Request requestDetail = approvedRequestDAO.getApprovedRequestByIdAndTeacher(requestId, teacherId);

            if (requestDetail != null) {
                request.setAttribute("requestDetail", requestDetail);
                request.getRequestDispatcher("/View/teacher-notification-detail.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found or access denied");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID");
        }
    }

    private boolean hasFilter(String dateFrom, String dateTo, String searchKeyword) {
        return (dateFrom != null && !dateFrom.trim().isEmpty()) ||
                (dateTo != null && !dateTo.trim().isEmpty()) ||
                (searchKeyword != null && !searchKeyword.trim().isEmpty());
    }

    private FilterInfo createCurrentFilter(String typeName, String dateFrom, String dateTo, String searchKeyword) {
        FilterInfo filter = new FilterInfo();
        filter.setTypeName(typeName);
        filter.setDateFrom(dateFrom);
        filter.setDateTo(dateTo);
        filter.setSearchKeyword(searchKeyword);
        return filter;
    }

    // Inner class để lưu thông tin filter hiện tại
    public static class FilterInfo {
        private String typeName;
        private String dateFrom;
        private String dateTo;
        private String searchKeyword;

        // Getters and setters
        public String getTypeName() { return typeName; }
        public void setTypeName(String typeName) { this.typeName = typeName; }

        public String getDateFrom() { return dateFrom; }
        public void setDateFrom(String dateFrom) { this.dateFrom = dateFrom; }

        public String getDateTo() { return dateTo; }
        public void setDateTo(String dateTo) { this.dateTo = dateTo; }

        public String getSearchKeyword() { return searchKeyword; }
        public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }
    }

    // Inner class để lưu thông tin notification tổng hợp
    public static class NotificationItem {
        private String content;
        private String notification;
        private String processedBy;
        private Date responseDate;
        private String type; // REQUEST hoặc SALARY

        // Getters and setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getNotification() { return notification; }
        public void setNotification(String notification) { this.notification = notification; }

        public String getProcessedBy() { return processedBy; }
        public void setProcessedBy(String processedBy) { this.processedBy = processedBy; }

        public Date getResponseDate() { return responseDate; }
        public void setResponseDate(Date responseDate) { this.responseDate = responseDate; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
}