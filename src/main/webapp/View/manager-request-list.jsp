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
</head>
<style>
    body {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }

    .main-content {
        margin-left: 320px;
        padding: 20px;
        background-color: #fff;
        min-height: 100vh;
    }

    .container {
        max-width: 1400px;
        margin: auto;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        background-color: white;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }

    th, td {
        padding: 10px;
        border: 1px solid #ccc;
        text-align: left;
        vertical-align: top;
    }

    th {
        background-color: #eee;
        font-weight: bold;
    }

    .status-pending {
        color: #ff9800;
        font-weight: bold;
    }

    .status-approved {
        color: #4caf50;
        font-weight: bold;
    }

    .status-rejected {
        color: #f44336;
        font-weight: bold;
    }

    .role-student {
        color: #2196f3;
        font-weight: bold;
    }

    .role-teacher {
        color: #9c27b0;
        font-weight: bold;
    }

    .action-btn {
        background-color: #007bff;
        color: white;
        padding: 5px 10px;
        text-decoration: none;
        border-radius: 3px;
        font-size: 12px;
    }

    .action-btn:hover {
        background-color: #0056b3;
        text-decoration: none;
        color: white;
    }

    .filter-section {
        margin-bottom: 20px;
        padding: 15px;
        background-color: #f8f9fa;
        border-radius: 5px;
    }
</style>

<body>
<jsp:include page="header.jsp"/>
<jsp:include page="training-manager-sidebar.jsp"/>

<div class="main-content">
    <div class="container">
        <h2>Quản lý đơn từ</h2>

            <form action="ProcessRequest" method="get" style="display: inline-block;">
                <input type="hidden" name="action" value="list" />
                <label for="filterStatus">Lọc theo trạng thái:</label>
                <select name="filterStatus" id="filterStatus" onchange="this.form.submit()">
                    <option value="">-- Tất cả --</option>
                    <option value="Chờ xử lý" ${param.filterStatus == 'Chờ xử lý' ? 'selected' : ''}>Chờ xử lý</option>
                    <option value="Đã duyệt" ${param.filterStatus == 'Đã duyệt' ? 'selected' : ''}>Đã duyệt</option>
                    <option value="Từ chối" ${param.filterStatus == 'Từ chối' ? 'selected' : ''}>Từ chối</option>
                </select>
            </form>

        <table>
            <thead>
            <tr>
                <th>STT</th>
                <th>Người gửi</th>
                <th>Vai trò</th>
                <th>Loại đơn</th>
                <th>Lý do</th>
                <th>Ngày gửi</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (requestList.isEmpty()) {
            %>
            <tr>
                <td colspan="8" style="text-align: center;">Chưa có đơn nào</td>
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
                <td><%= index++ %></td>
                <td><%= r.getSenderName() != null ? r.getSenderName() : "" %></td>
                <td class="<%= roleClass %>"><%= r.getSenderRole() != null ? r.getSenderRole() : "" %></td>
                <td><%= r.getTypeName() != null ? r.getTypeName() : "" %></td>
                <td style="white-space: pre-wrap; word-wrap: break-word; max-width: 300px;">
                    <%= r.getReason() != null ? r.getReason() : "" %>
                </td>
                <td><%= r.getCreatedAt() != null ? dateFormat.format(r.getCreatedAt()) : "" %></td>
                <td class="<%= statusClass %>"><%= r.getStatus() != null ? r.getStatus() : "" %></td>
                <td>
                    <a href="ProcessRequest?id=<%= r.getId() %>" class="action-btn">Xem</a>
                    <% if ("Chờ xử lý".equals(r.getStatus())) { %>
                    <a href="ProcessRequest?id=<%= r.getId() %>" class="action-btn" style="background-color: #28a745;">Xử lý</a>
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

<jsp:include page="footer.jsp"/>
</body>
</html>
