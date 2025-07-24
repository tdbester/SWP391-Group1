<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/21/2025
  Time: 3:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%--/*--%>
<%--*  Copyright (C) 2025 <Group 1>--%>
<%--    *  All rights reserved.--%>
<%--    *--%>
<%--    *  This file is part of the <Talent Center Management> project.--%>
<%--    *  Unauthorized copying of this file, via any medium is strictly prohibited.--%>
<%--    *  Proprietary and confidential.--%>
<%--    *--%>
<%--    *  Created on:        2025-06-21--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-06-21  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ page import="org.example.talentcenter.model.Request" %>

<%
    ArrayList<Request> requestList = (ArrayList<Request>) request.getAttribute("requestList");
    if (requestList == null) requestList = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

<html lang="vi">
<head>
    <title>Danh sách đơn đã gửi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/student-request-list.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
</head>

<body>
<jsp:include page="header.jsp"/>

<div class="container">
<jsp:include page="student-sidebar.jsp"/>
    <div class="main-content">
        <a href="StudentDashboard" class="back-link">
            <i class="fas fa-arrow-left"></i>
            Quay lại Dashboard
        </a>
        <h2>Danh sách đơn đã gửi</h2><br>
        <div class="search-filter-section">
            <div class="filter-row">
                <div class="filter-group">
                    <label class="filter-label">
                        <i class="fas fa-search"></i>
                        Tìm kiếm
                    </label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-search"></i>
                        </span>
                        <input type="text"
                               id="searchInput"
                               class="form-control"
                               placeholder="Nhập từ khóa để tìm kiếm"
                               value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>"/>
                    </div>
                </div>
                <div class="filter-group">
                    <label class="filter-label">
                        <i class="fas fa-file-alt"></i>
                        Loại đơn
                    </label>
                    <select id="typeFilter" class="form-select">
                        <option value="">Tất cả loại đơn</option>
                        <c:forEach var="request" items="${requestTypeList}">
                            <option value="${request.typeId}" ${param.filterTypeId == request.typeId ? "selected" : ""}>
                                ${request.typeName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="filter-group">
                    <label class="filter-label">
                        <i class="fas fa-flag"></i>
                        Trạng thái
                    </label>
                    <select id="statusFilter" class="form-select">
                        <option value="">Tất cả trạng thái</option>
                        <option value="Chờ xử lý" <%= "Chờ xử lý".equals(request.getParameter("statusFilter")) ? "selected" : "" %>>Chờ xử lý</option>
                        <option value="Đã duyệt" <%= "Đã duyệt".equals(request.getParameter("statusFilter")) ? "selected" : "" %>>Đã duyệt</option>
                        <option value="Từ chối" <%= "Từ chối".equals(request.getParameter("statusFilter")) ? "selected" : "" %>>Từ chối</option>
                    </select>
                </div>
                <div class="filter-group">
                    <button type="button" id="clearFiltersBtn" class="clear-filters-btn">
                        <i class="fas fa-eraser"></i>
                        Xóa bộ lọc
                    </button>
                </div>
            </div>
        </div>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>Lý do</th>
                    <th>Ngày tạo</th>
                    <th>Ghi chú xử lý</th>
                    <th>Trạng thái</th>
                    <th>Ngày xử lý</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (requestList.isEmpty()) {
                %>
                <tr>
                    <td colspan="5" style="text-align: center;">Chưa có đơn nào được gửi</td>
                </tr>
                <%
                } else {
                    for (Request r : requestList) {
                %>
                <tr>
                    <td class="reason-text"><%= r.getReason() != null ? r.getReason() : "" %></td>
                    <td><%= r.getCreatedAt() != null ? dateFormat.format(r.getCreatedAt()) : "" %></td>
                    <td><%= r.getResponse() != null ? r.getResponse() : "" %></td>
                    <td>
                        <span class="status-badge <%= r.getStatus() != null ? r.getStatus().toLowerCase() : "" %>">
                            <%= r.getStatus() != null ? r.getStatus() : "" %>
                        </span>
                    </td>
                    <td><%= r.getResponseAt() != null ? dateFormat.format(r.getResponseAt()) : "" %></td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
        <div class="pagination-wrapper">
            <nav>
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="StudentApplication?action=list&page=${currentPage - 1}&keyword=${param.keyword}&statusFilter=${param.statusFilter}&filterTypeId=${param.filterTypeId}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="StudentApplication?action=list&page=${i}&keyword=${param.keyword}&statusFilter=${param.statusFilter}&filterTypeId=${param.filterTypeId}">${i}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="StudentApplication?action=list&page=${currentPage + 1}&keyword=${param.keyword}&statusFilter=${param.statusFilter}&filterTypeId=${param.filterTypeId}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const searchInput = document.getElementById('searchInput');
        const typeFilter = document.getElementById('typeFilter');
        const statusFilter = document.getElementById('statusFilter');
        const clearFiltersBtn = document.getElementById('clearFiltersBtn');
        let searchTimer;
        function searchWithDelay() {
            clearTimeout(searchTimer);
            searchTimer = setTimeout(function () {
                const keyword = searchInput.value.trim();
                let url = 'StudentApplication?action=list';
                if (keyword) {
                    url += '&keyword=' + encodeURIComponent(keyword);
                }
                if (typeFilter.value) {
                    url += '&filterTypeId=' + encodeURIComponent(typeFilter.value);
                }
                if (statusFilter.value) {
                    url += '&statusFilter=' + encodeURIComponent(statusFilter.value);
                }
                window.location.href = url;
            }, 800);
        }
        function filterByType() {
            let url = 'StudentApplication?action=list';
            if (searchInput.value.trim()) {
                url += '&keyword=' + encodeURIComponent(searchInput.value.trim());
            }
            if (typeFilter.value) {
                url += '&filterTypeId=' + encodeURIComponent(typeFilter.value);
            }
            if (statusFilter.value) {
                url += '&statusFilter=' + encodeURIComponent(statusFilter.value);
            }
            window.location.href = url;
        }
        function filterByStatus() {
            let url = 'StudentApplication?action=list';
            if (searchInput.value.trim()) {
                url += '&keyword=' + encodeURIComponent(searchInput.value.trim());
            }
            if (typeFilter.value) {
                url += '&filterTypeId=' + encodeURIComponent(typeFilter.value);
            }
            if (statusFilter.value) {
                url += '&statusFilter=' + encodeURIComponent(statusFilter.value);
            }
            window.location.href = url;
        }
        function clearAllFilters() {
            window.location.href = 'StudentApplication?action=list';
        }
        searchInput.addEventListener('input', searchWithDelay);
        typeFilter.addEventListener('change', filterByType);
        statusFilter.addEventListener('change', filterByStatus);
        clearFiltersBtn.addEventListener('click', clearAllFilters);
    });
</script>
</body>
</html>
