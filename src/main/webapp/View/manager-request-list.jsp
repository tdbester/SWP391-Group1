<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 7/5/2025
  Time: 2:00 PM
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
<%--    *  Created on:        2025-07-05--%>
<%--    *  Author:            Training Manager--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-07-05  | Training Manager     | Request management page--%>
<%--    */--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ page import="org.example.talentcenter.model.Request" %>

<%
    ArrayList<Request> requestList = (ArrayList<Request>) request.getAttribute("requestList");
    ArrayList<Request> requestTypes = (ArrayList<Request>) request.getAttribute("requestTypes");
    if (requestList == null) requestList = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    int currentPage = (int) request.getAttribute("currentPage");
%>

<html lang="vi">
<head>
    <title>Quản lý đơn từ - Training Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/manager-request-list.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">

</head>

<body>
<jsp:include page="header.jsp"/>

<div class="container">
    <jsp:include page="training-manager-sidebar.jsp"/>
    <div class="main-content">

        <div class="page-title">
            <h2><i class="fas fa-tasks"></i> Quản lý đơn từ</h2>
        </div>

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
                               value="${keyword}"/>
                    </div>
                </div>

                <div class="filter-group">
                    <label class="filter-label">
                        <i class="fas fa-file-alt"></i>
                        Loại đơn
                    </label>
                    <select id="typeFilter" class="form-select">
                        <option value="">Tất cả loại đơn</option>
                        <% for (Request type : requestTypes) { %>
                        <option value="<%= type.getTypeName() %>"
                                <%= type.getTypeName().equals(request.getAttribute("typeFilter")) ? "selected" : "" %>>
                            <%= type.getTypeName() %>
                        </option>
                        <% } %>
                    </select>
                </div>

                <div class="filter-group">
                    <label class="filter-label">
                        <i class="fas fa-flag"></i>
                        Trạng thái
                    </label>
                    <select id="statusFilter" name="statusFilter" class="form-select">
                        <option value="">Tất cả trạng thái</option>
                        <option value="Chờ xử lý" <%= "Chờ xử lý".equals(request.getAttribute("statusFilter")) ? "selected" : "" %>>
                            Chờ xử lý
                        </option>
                        <option value="Đã duyệt" <%= "Đã duyệt".equals(request.getAttribute("statusFilter")) ? "selected" : "" %>>
                            Đã duyệt
                        </option>
                        <option value="Từ chối" <%= "Từ chối".equals(request.getAttribute("statusFilter")) ? "selected" : "" %>>
                            Từ chối
                        </option>
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
                    <th>STT</th>
                    <th><i class="fas fa-user"></i> Người gửi</th>
                    <th><i class="fas fa-user-tag"></i> Vai trò</th>
                    <th><i class="fas fa-file-alt"></i> Loại đơn</th>
                    <th><i class="fas fa-comment"></i> Lý do</th>
                    <th><i class="fas fa-calendar"></i> Ngày gửi</th>
                    <th><i class="fas fa-flag"></i> Trạng thái</th>
                    <th><i class="fas fa-cogs"></i> Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (requestList.isEmpty()) {
                %>
                <tr>
                    <td colspan="8">
                        <div class="empty-state">
                            <i class="fas fa-inbox"></i>
                            <p>Chưa có đơn nào trong hệ thống</p>
                        </div>
                    </td>
                </tr>
                <%
                } else {
                    int index = 1;
                    for (Request r : requestList) {
                        String statusClass = "";
                        if ("Chờ xử lý".equals(r.getStatus())) {
                            statusClass = "status-pending";
                        } else if ("Đã duyệt".equals(r.getStatus())) {
                            statusClass = "status-approved";
                        } else if ("Từ chối".equals(r.getStatus())) {
                            statusClass = "status-rejected";
                        }

                        String roleClass = "";
                        if ("Student".equals(r.getSenderRole())) {
                            roleClass = "role-student";
                        } else if ("Teacher".equals(r.getSenderRole())) {
                            roleClass = "role-teacher";
                        }
                %>
                <tr>
                    <td style="text-align: center; font-weight: 600;"><%= (currentPage-1)*10 + index %></td>
                    <td><%= r.getSenderName() != null ? r.getSenderName() : "" %>
                    </td>
                    <td class="<%= roleClass %>">
                        <%= r.getSenderRole() != null ? r.getSenderRole() : "" %>
                    </td>
                    <td><%= r.getTypeName() != null ? r.getTypeName() : "" %>
                    </td>
                    <td>
                        <div class="reason-text"
                             title="<%= r.getReason() != null ? r.getReason().replace("\"", "&quot;") : "" %>">
                            <%= r.getReason() != null ? r.getReason() : "" %>
                        </div>
                    </td>
                    <td style="color: #6c757d;">
                        <%= r.getCreatedAt() != null ? dateFormat.format(r.getCreatedAt()) : "" %>
                    </td>
                    <td style="text-align: center;">
                        <span class="status-badge <%= statusClass %>">
                            <%= r.getStatus() != null ? r.getStatus() : "" %>
                        </span>
                    </td>
                    <td style="text-align: center;">
                        <% if ("Chờ xử lý".equals(r.getStatus())) { %>
                        <a href="ProcessRequest?id=<%= r.getId() %>" class="action-btn btn-process">
                            <i class="fas fa-cogs"></i> Xử lý
                        </a>
                        <% } else { %>
                        <a href="ProcessRequest?id=<%= r.getId() %>" class="action-btn btn-view">
                            <i class="fas fa-eye"></i> Xem
                        </a>
                        <% } %>
                    </td>
                </tr>
                <%
                        index++;
                    }
                }
                %>
                </tbody>
            </table>

            <div class="pagination-wrapper">
                <nav>
                    <ul class="pagination">
                        <c:if test="${currentPage > 1}">
                            <li class="page-item">
                                <a class="page-link" href="ProcessRequest?action=list&page=${currentPage - 1}&keyword=${param.keyword}&typeFilter=${param.typeFilter}&statusFilter=${param.statusFilter}">&laquo;</a>
                            </li>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link" href="ProcessRequest?action=list&page=${i}&keyword=${param.keyword}&typeFilter=${param.typeFilter}&statusFilter=${param.statusFilter}">${i}</a>
                            </li>
                        </c:forEach>
                        
                        <c:if test="${currentPage < totalPages}">
                            <li class="page-item">
                                <a class="page-link" href="ProcessRequest?action=list&page=${currentPage + 1}&keyword=${param.keyword}&typeFilter=${param.typeFilter}&statusFilter=${param.statusFilter}">&raquo;</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>

        </div>
    </div>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const searchInput = document.getElementById('searchInput');
        const typeFilter = document.getElementById('typeFilter');
        const statusFilter = document.getElementById('statusFilter');
        const clearFiltersBtn = document.getElementById('clearFiltersBtn');

        function applyFilters() {
            const keyword = searchInput.value.trim();
            const typeValue = typeFilter.value;
            const statusValue = statusFilter.value;

            let url = 'ProcessRequest?action=list';
            if (keyword) {
                url += '&keyword=' + encodeURIComponent(keyword);
            }
            if (typeValue) {
                url += '&typeFilter=' + encodeURIComponent(typeValue);
            }
            if (statusValue) {
                url += '&statusFilter=' + encodeURIComponent(statusValue);
            }
            
            window.location.href = url;
        }

        function clearAllFilters() {
            window.location.href = 'ProcessRequest?action=list';
        }
        
        // Use a single function for all filter changes
        searchInput.addEventListener('input', () => {
             // Basic debouncing
            clearTimeout(searchInput.timer);
            searchInput.timer = setTimeout(applyFilters, 500);
        });
        typeFilter.addEventListener('change', applyFilters);
        statusFilter.addEventListener('change', applyFilters);
        clearFiltersBtn.addEventListener('click', clearAllFilters);
    });
</script>
<jsp:include page="footer.jsp"/>
</body>
</html>
