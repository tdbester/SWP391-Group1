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
        max-width: 1200px;
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
    }

    th {
        background-color: #eee;
    }
</style>

<body>
<jsp:include page="header.jsp"/>
<jsp:include page="student-sidebar.jsp"/>

<div class="main-content">
    <div class="container">
        <h2>Danh sách đơn đã gửi</h2>
        <form action="StudentApplication" method="get" style="margin-bottom: 20px;">
            <input type="hidden" name="action" value="list" />
            <label for="filterTypeId">Lọc theo loại đơn:</label>
            <select name="filterTypeId" id="filterTypeId" onchange="this.form.submit()">
                <option value="">-- Tất cả --</option>
                <c:forEach var="request" items="${requestTypeList}">
                    <option value="${request.typeId}" ${param.filterTypeId == request.typeId ? "selected" : ""}>
                            ${request.typeName}
                    </option>
                </c:forEach>
            </select>
        </form>
        <table style="width: 100%; border-collapse: collapse;">
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
                <td style="white-space: pre-wrap; word-wrap: break-word; max-width: 300px;"><%= r.getReason() != null ? r.getReason() : "" %></td>
                <td><%= r.getCreatedAt() != null ? dateFormat.format(r.getCreatedAt()) : "" %></td>
                <td><%= r.getResponse() != null ? r.getResponse() : "" %></td>
                <td><%= r.getStatus() != null ? r.getStatus() : "" %></td>
                <td><%= r.getResponseAt() != null ? dateFormat.format(r.getResponseAt()) : "" %></td>
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
