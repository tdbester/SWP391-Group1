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
        .page-title {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
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

        <div class="filter-section">
            <form action="ProcessRequest" method="get">
                <input type="hidden" name="action" value="list"/>
                <i class="fas fa-filter"></i>
                <label for="filterStatus">Lọc theo trạng thái:</label>
                <select name="filterStatus" id="filterStatus" onchange="this.form.submit()">
                    <option value="">-- Tất cả --</option>
                    <option value="Chờ xử lý" ${param.filterStatus == 'Chờ xử lý' ? 'selected' : ''}>Chờ xử lý</option>
                    <option value="Đã duyệt" ${param.filterStatus == 'Đã duyệt' ? 'selected' : ''}>Đã duyệt</option>
                    <option value="Từ chối" ${param.filterStatus == 'Từ chối' ? 'selected' : ''}>Từ chối</option>
                </select>
            </form>
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
                    <td style="text-align: center; font-weight: 600;"><%= index++ %></td>
                    <td><%= r.getSenderName() != null ? r.getSenderName() : "" %></td>
                    <td class="<%= roleClass %>">
                        <%= r.getSenderRole() != null ? r.getSenderRole() : "" %>
                    </td>
                    <td><%= r.getTypeName() != null ? r.getTypeName() : "" %></td>
                    <td>
                        <div class="reason-text" title="<%= r.getReason() != null ? r.getReason().replace("\"", "&quot;") : "" %>">
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
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
