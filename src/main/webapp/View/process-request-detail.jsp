<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 7/5/2025
  Time: 3:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%--/*--%>
<%--*  Copyright (C) 2025 <Group 1>--%>
<%--*  All rights reserved.--%>
<%--*--%>
<%--*  This file is part of the <Talent Center Management> project.--%>
<%--*  Unauthorized copying of this file, via any medium is strictly prohibited.--%>
<%--*  Proprietary and confidential.--%>
<%--*--%>
<%--*  Created on:        2025-07-05--%>
<%--*  Author:            Training Manager--%>
<%--*--%>
<%--*  ========================== Change History ==========================--%>
<%--*  Date        | Author               | Description--%>
<%--*  ------------|----------------------|----------------------------------%>
<%--*  2025-07-05  | Training Manager     | Request detail processing page--%>
<%--*/--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.example.talentcenter.model.Request" %>

<%
    Request requestDetail = (Request) request.getAttribute("requestDetail");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

<html lang="vi">
<head>
    <title>Chi tiết đơn - Training Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
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
</head>

<body>
<jsp:include page="header.jsp"/>
<jsp:include page="training-manager-sidebar.jsp"/>

<div class="main-content">
    <div class="container">
        <h1>Chi tiết đơn từ</h1>
        <a href="ProcessRequest?action=list">← Quay lại danh sách</a>

        <% if (requestDetail != null) { %>

        <!-- Thông tin người gửi đơn -->
        <h2>Thông tin người gửi đơn</h2>
        <table border="1">
            <tr>
                <td><strong>Họ và tên:</strong></td>
                <td><%= requestDetail.getSenderName() != null ? requestDetail.getSenderName() : "" %></td>
            </tr>
            <tr>
                <td><strong>Số điện thoại:</strong></td>
                <td><%= requestDetail.getPhoneNumber() != null ? requestDetail.getPhoneNumber() : "" %></td>
            </tr>
            <tr>
                <td><strong>SĐT phụ huynh:</strong></td>
                <td><%= requestDetail.getParentPhone() != null ? requestDetail.getParentPhone() : "" %></td>
            </tr>
            <tr>
                <td><strong>Vai trò:</strong></td>
                <td><%= requestDetail.getSenderRole() != null ? requestDetail.getSenderRole() : "" %></td>
            </tr>
            <tr>
                <td><strong>Lớp hiện tại:</strong></td>
                <td><%= requestDetail.getCourseName() != null ? requestDetail.getCourseName() : "" %></td>
            </tr>
        </table>

        <!-- Thông tin đơn -->
        <h2>Thông tin đơn</h2>
        <table border="1">
            <tr>
                <td><strong>Loại đơn đã gửi:</strong></td>
                <td><%= requestDetail.getTypeName() != null ? requestDetail.getTypeName() : "" %></td>
            </tr>
            <tr>
                <td><strong>Ngày gửi:</strong></td>
                <td><%= requestDetail.getCreatedAt() != null ? dateFormat.format(requestDetail.getCreatedAt()) : "" %></td>
            </tr>
            <tr>
                <td><strong>Trạng thái:</strong></td>
                <td><%= requestDetail.getStatus() != null ? requestDetail.getStatus() : "" %></td>
            </tr>
        </table>

        <!-- Nội dung đơn -->
        <h2>Nội dung đơn</h2>
        <div style="border: 1px solid #ccc; padding: 10px; white-space: pre-wrap;">
            <%= requestDetail.getReason() != null ? requestDetail.getReason() : "" %>
        </div>

        <!-- Lịch sử xử lý -->
        <h2>Lịch sử xử lý</h2>
        <% if (requestDetail.getResponse() != null && !requestDetail.getResponse().trim().isEmpty()) { %>
        <table border="1">
            <tr>
                <td><strong>Phản hồi từ Training Manager:</strong></td>
                <td><%= requestDetail.getResponse() %></td>
            </tr>
            <tr>
                <td><strong>Thời gian xử lý:</strong></td>
                <td><%= requestDetail.getResponseAt() != null ? dateFormat.format(requestDetail.getResponseAt()) : "" %></td>
            </tr>
        </table>
        <% } else { %>
        <p>Chưa có lịch sử xử lý</p>
        <% } %>

        <!-- Form xử lý đơn (chỉ hiện khi đơn chờ xử lý) -->
        <% if ("Chờ xử lý".equals(requestDetail.getStatus())) { %>
        <h2>Form xử lý đơn</h2>
        <form action="ProcessRequest" method="post">
            <input type="hidden" name="requestId" value="<%= requestDetail.getId() %>">

            <label for="managerNote"><strong>Ghi chú/Phản hồi của Training Manager:</strong></label><br>
            <textarea name="managerNote" id="managerNote" rows="5" cols="80"
                      placeholder="Nhập phản hồi cho người gửi đơn..." required></textarea><br><br>

            <button type="submit" name="action" value="approve">Duyệt</button>
            <button type="submit" name="action" value="reject">Từ chối</button>
        </form>
        <% } %>

        <% } else { %>
        <p>Không tìm thấy thông tin đơn.</p>
        <% } %>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
