package org.example.talentcenter.controller;

import org.example.talentcenter.dao.ApprovedRequestDAO;
import org.example.talentcenter.model.Request;
import org.example.talentcenter.model.RequestType;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/teacher-notification")
public class TeacherNotificationServlet extends HttpServlet {

    private ApprovedRequestDAO approvedRequestDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        approvedRequestDAO = new ApprovedRequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("detail".equals(action)) {
            showRequestDetail(request, response);
        } else {
            showRequestList(request, response);
        }
    }

    private void showRequestList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy parameters từ request
        String typeName = request.getParameter("typeName");
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        String searchKeyword = request.getParameter("searchKeyword");


        // Lấy danh sách request types cho dropdown
        List<RequestType> requestTypes = approvedRequestDAO.getAllRequestTypes();

        // Lấy danh sách approved requests với filter
        List<Request> approvedRequests;
        int filteredCount;

        if (hasFilter(typeName, dateFrom, dateTo, searchKeyword)) {
            approvedRequests = approvedRequestDAO.getApprovedRequestsWithFilter(
                    typeName, dateFrom, dateTo, searchKeyword);
            filteredCount = approvedRequestDAO.getFilteredRequestCount(
                    typeName, dateFrom, dateTo, searchKeyword);
        } else {
            approvedRequests = approvedRequestDAO.getApprovedRequestsWithFilter(
                    null, null, null, null);
            filteredCount = approvedRequestDAO.getTotalApprovedRequests();
        }

        // Set attributes cho JSP
        request.setAttribute("approvedRequests", approvedRequests);
        request.setAttribute("requestTypes", requestTypes);
        request.setAttribute("filteredCount", filteredCount);
        request.setAttribute("currentFilter", createCurrentFilter(typeName, dateFrom, dateTo, searchKeyword));

        // Forward to JSP
        request.getRequestDispatcher("/View/teacher-notification.jsp").forward(request, response);
    }

    private void showRequestDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int requestId = Integer.parseInt(request.getParameter("id"));
            Request requestDetail = approvedRequestDAO.getApprovedRequestById(requestId);

            if (requestDetail != null) {
                request.setAttribute("requestDetail", requestDetail);
                request.getRequestDispatcher("/View/teacher-notification-detail.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID");
        }
    }

    private boolean hasFilter(String typeName, String dateFrom, String dateTo, String searchKeyword) {
        return (typeName != null && !typeName.trim().isEmpty() && !typeName.equals("all")) ||
                (dateFrom != null && !dateFrom.trim().isEmpty()) ||
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
}
