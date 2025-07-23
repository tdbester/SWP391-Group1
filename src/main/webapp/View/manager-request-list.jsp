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
%>

<html lang="vi">
<head>
    <title>Quản lý đơn từ - Training Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
    <style>
        /* Filter row layout */
        .filter-row {
            display: flex;
            gap: 20px;
            align-items: end;
            flex-wrap: wrap;
        }

        .filter-group {
            display: flex;
            flex-direction: column;
            min-width: 200px;
            flex: 1;
        }

        .filter-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 8px;
            font-size: 14px;
            display: flex;
            align-items: center;
            gap: 5px;
        }

        /* Input group styling */
        .input-group {
            display: flex;
            width: 100%;
        }

        .input-group-text {
            background-color: #7a6ad8;
            border: 1px solid #7a6ad8;
            color: white;
            padding: 10px 15px;
            border-radius: 8px 0 0 8px;
            border-right: none;
        }

        .input-group .form-control {
            border-radius: 0 8px 8px 0;
            border-left: none;
            flex: 1;
        }

        .form-control, .form-select {
            border: 1px solid #d0d7ff;
            border-radius: 8px;
            padding: 10px 15px;
            font-size: 14px;
            transition: all 0.3s ease;
            width: 100%;
            background: white;
        }

        .form-control:focus, .form-select:focus {
            border-color: #7a6ad8;
            box-shadow: 0 0 0 0.2rem rgba(122, 106, 216, 0.25);
            outline: none;
        }

        .clear-filters-btn {
            background-color: #6c757d;
            border: 1px solid #6c757d;
            color: white;
            border-radius: 8px;
            padding: 10px 20px;
            font-size: 14px;
            transition: all 0.3s ease;
            cursor: pointer;
            margin-top: 24px; /* Align với các input khác */
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .clear-filters-btn:hover {
            background-color: #5a6268;
            border-color: #545b62;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .filter-row {
                flex-direction: column;
                gap: 15px;
            }

            .filter-group {
                min-width: 100%;
            }

            .clear-filters-btn {
                margin-top: 0;
                width: 100%;
                justify-content: center;
            }
        }

        .search-filter-section {
            background: linear-gradient(135deg, #f8f9ff 0%, #e8eaff 100%);
            border: 1px solid #e0e6ff;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 30px;
            box-shadow: 0 2px 10px rgba(122, 106, 216, 0.1);
        }

        .filter-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 5px;
            font-size: 14px;
        }

        .form-control, .form-select {
            border: 1px solid #d0d7ff;
            border-radius: 8px;
            padding: 10px 15px;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .form-control:focus, .form-select:focus {
            border-color: #7a6ad8;
            box-shadow: 0 0 0 0.2rem rgba(122, 106, 216, 0.25);
        }

        .clear-filters-btn {
            background-color: #6c757d;
            border-color: #6c757d;
            color: white;
            border-radius: 8px;
            padding: 10px 20px;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .clear-filters-btn:hover {
            background-color: #5a6268;
            border-color: #545b62;
        }

        .page-title {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .page-title h2 {
            margin: 0;
            color: #333;
            display: flex;
            align-items: center;
        }

        .page-title h2 i {
            margin-right: 10px;
            color: #007bff;
        }

        .filter-section {
            background: white;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .filter-section label {
            font-weight: 600;
            margin-right: 10px;
        }

        .filter-section select {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background: white;
        }

        .table-container {
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        thead {
            background: #007bff;
            color: white;
        }

        th {
            padding: 15px 10px;
            text-align: left;
            font-weight: 600;
        }

        tbody tr {
            border-bottom: 1px solid #eee;
            transition: background 0.2s;
        }

        tbody tr:hover {
            background: #f8f9fa;
        }

        td {
            padding: 12px 10px;
            vertical-align: middle;
        }

        .reason-text {
            max-width: 250px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            cursor: help;
        }

        .reason-text:hover {
            white-space: normal;
            background: #f8f9fa;
            padding: 8px;
            border-radius: 4px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .status-badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
        }

        .status-pending {
            background: #fff3cd;
            color: #856404;
        }

        .status-approved {
            background: #d4edda;
            color: #155724;
        }

        .status-rejected {
            background: #f8d7da;
            color: #721c24;
        }

        .role-student {
            color: #007bff;
            font-weight: 600;
        }

        .role-teacher {
            color: #6f42c1;
            font-weight: 600;
        }

        .action-btn {
            display: inline-block;
            padding: 6px 12px;
            margin: 2px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 600;
            transition: all 0.2s;
        }

        .btn-view {
            background: #007bff;
            color: white;
        }

        .btn-view:hover {
            background: #0056b3;
            color: white;
            text-decoration: none;
        }

        .btn-process {
            background: #28a745;
            color: white;
        }

        .btn-process:hover {
            background: #1e7e34;
            color: white;
            text-decoration: none;
        }

        .empty-state {
            text-align: center;
            padding: 40px;
            color: #6c757d;
        }

        .empty-state i {
            font-size: 48px;
            margin-bottom: 15px;
            color: #dee2e6;
        }

        @media (max-width: 768px) {
            .main-content {
                padding: 10px;
            }

            .table-container {
                overflow-x: auto;
            }

            .reason-text {
                max-width: 150px;
            }
        }

        .pagination-wrapper {
            display: flex;
            justify-content: center;
            margin-top: 30px;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .pagination {
            display: flex;
            list-style: none;
            margin: 0;
            padding: 0;
            gap: 5px;
        }

        .page-item {
            display: flex;
        }

        .page-link {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 10px 15px;
            text-decoration: none;
            border: 1px solid #dee2e6;
            color: #007bff;
            background: white;
            border-radius: 4px;
            transition: all 0.3s ease;
            min-width: 45px;
            height: 45px;
            font-weight: 500;
        }

        .page-link:hover {
            background: #e9ecef;
            border-color: #adb5bd;
            color: #0056b3;
            text-decoration: none;
            transform: translateY(-1px);
        }

        .page-item.active .page-link {
            background: #007bff;
            border-color: #007bff;
            color: white;
            box-shadow: 0 2px 8px rgba(0, 123, 255, 0.3);
        }

        .page-item.active .page-link:hover {
            background: #0056b3;
            border-color: #0056b3;
            transform: translateY(-1px);
        }

        @media (max-width: 768px) {
            .pagination-wrapper {
                padding: 15px;
            }

            .page-link {
                padding: 8px 12px;
                min-width: 40px;
                height: 40px;
                font-size: 14px;
            }
        }
    </style>
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
                    <td style="text-align: center; font-weight: 600;"><%= index++ %>
                    </td>
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
                        <a href="ProcessRequest?id=<%= r.getId() %>" class="action-btn btn-view">
                            <i class="fas fa-eye"></i> Xem
                        </a>
                        <% if ("Chờ xử lý".equals(r.getStatus())) { %>
                        <a href="ProcessRequest?id=<%= r.getId() %>" class="action-btn btn-process">
                            <i class="fas fa-cogs"></i> Xử lý
                        </a>
                        <% } %>
                    </td>
                </tr>
                <%
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
