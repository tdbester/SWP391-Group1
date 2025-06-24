<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/9/2025
  Time: 1:57 PM
  To change this template use File | Settings | File Templates.
--%>
/*
*  Copyright (C) 2025 <Group 1>
    *  All rights reserved.
    *
    *  This file is part of the <Talent Center Management> project.
    *  Unauthorized copying of this file, via any medium is strictly prohibited.
    *  Proprietary and confidential.
    *
    *  Created on:        2025-06-09
    *  Author:            Cù Thị Huyền Trang
    *
    *  ========================== Change History ==========================
    *  Date        | Author               | Description
    *  ------------|----------------------|--------------------------------
    *  2025-06-09  | Cù Thị Huyền Trang   | Initial creation
    */
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Yêu cầu cấp tài khoản học viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .btn-primary {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
            font-family: 'Poppins', sans-serif;
            font-weight: 500;
            border-radius: 4px;
            padding: 8px 16px;
            transition: background-color 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #6a5acd;
            border-color: #6a5acd;
        }

        th {
            background-color: #7a6ad8;
            color: white;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<jsp:include page="sale-sidebar.jsp"/>
<div class="dashboard">
    <div class="main-content">
        <div class=" container mt-4">
            <h2 class="mb-4"><i class="fas fa-user-plus me-2"></i>Danh sách học sinh đã đồng ý tư vấn</h2>

            <form method="post" action="${pageContext.request.contextPath}/StudentAccountRequest">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th><input type="checkbox" id="selectAll"/></th>
                        <th>ID</th>
                        <th>Họ tên</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="s" items="${agreedStudents}">
                        <tr>
                            <td><input type="checkbox" name="selectedStudentIds" value="${s.id}"/></td>
                            <td>${s.id}</td>
                            <td>${s.fullName}</td>
                            <td>${s.email}</td>
                            <td>${s.phone}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <button type="submit" class="btn btn-primary mt-3">
                    <i class="fas fa-paper-plane me-1"></i> Gửi yêu cầu cấp tài khoản
                </button>
            </form>

            <c:if test="${not empty message}">
                <div class="alert alert-info mt-3">${message}</div>
            </c:if>
        </div>
    </div>
</div>

<script>
    document.getElementById('selectAll').addEventListener('click', function () {
        const checkboxes = document.querySelectorAll('input[name="selectedStudentIds"]');
        for (let cb of checkboxes) {
            cb.checked = this.checked;
        }
    });
</script>

<jsp:include page="footer.jsp"/>
</body>
</html>
