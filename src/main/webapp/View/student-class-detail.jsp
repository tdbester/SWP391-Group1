<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 7/4/2025
  Time: 9:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%--/*--%>
<%--*  Copyright (C) 2025 <Group 1>--%>
<%--*  All rights reserved.--%>
<%--*--%>
<%--*  This file is part of the <Talent Center Management> project.--%>
<%--    *  Unauthorized copying of this file, via any medium is strictly prohibited.--%>
<%--    *  Proprietary and confidential.--%>
<%--    *--%>
<%--    *  Created on:        2025-07-04--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-07-04  | Cù Thị Huyền Trang   | Class detail for student--%>
<%--    */--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="vi">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết buổi học - TALENT01</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/schedule.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="cointainer">
<jsp:include page="student-sidebar.jsp"/>
    <div class="main-content">
        <a href="StudentSchedule">← Quay lại lịch học</a>

        <h1>Chi tiết buổi học</h1>
        <h2>Ngày: ${formattedDate}</h2>

        <c:forEach var="detail" items="${classDetails}" varStatus="status">
            <div>
                <h3>${detail.className}</h3>
                <p><strong>Slot:</strong> ${detail.slotId} (${detail.slotStartTime} - ${detail.slotEndTime})</p>

                <table border="1">
                    <tr>
                        <td><strong>Khóa học:</strong></td>
                        <td>${detail.courseTitle}</td>
                    </tr>
                    <tr>
                        <td><strong>Giảng viên:</strong></td>
                        <td>${detail.teacherName}</td>
                    </tr>
                    <c:if test="${not empty detail.teacherEmail}">
                        <tr>
                            <td><strong>Email giảng viên:</strong></td>
                            <td><a href="mailto:${detail.teacherEmail}">${detail.teacherEmail}</a></td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty detail.teacherPhoneNumber}">
                        <tr>
                            <td><strong>Số điện thoại:</strong></td>
                            <td><a href="tel:${detail.teacherPhoneNumber}">${detail.teacherPhoneNumber}</a></td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty detail.roomCode}">
                        <tr>
                            <td><strong>Phòng học:</strong></td>
                            <td>${detail.roomCode}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td><strong>Trạng thái điểm danh:</strong></td>
                        <td>
                            <c:choose>
                                <c:when test="${detail.attendaceStatus == 'Present'}">
                                    Có mặt
                                </c:when>
                                <c:when test="${detail.attendaceStatus == 'Absent'}">
                                    Vắng mặt
                                </c:when>
                                <c:otherwise>
                                    Chưa điểm danh
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>
                <hr>
            </div>
        </c:forEach>
    </div>
</div>

<jsp:include page="footer.jsp"/>


</body>
</html>

