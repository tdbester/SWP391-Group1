<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 7/23/2025
  Time: 11:05 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sale Course List</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <jsp:include page="sale-sidebar.jsp"/>
    <div class="main-content">
        <a href="SaleDashboard" class="back-link">
            <i class="fas fa-arrow-left"></i>
            Quay lại Dashboard
        </a>
        <div class="sale-new-courses">
            <h2>Danh sách khoá học</h2>
            <table class="sale-course-table">
                <thead>
                <tr>
                    <th>Khoá học</th>
                    <th>Giá</th>
                    <th>Số lượng lớp</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty allCourses}">
                        <tr>
                            <td colspan="4" style="text-align: center; color: #666;">
                                Chưa có khóa học nào
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="course" items="${allCourses}">
                            <tr>
                                <td>
                                    <strong>${course.title}</strong>
                                    <c:if test="${not empty course.information}">
                                        <br><small style="color: #666;">${course.information}</small>
                                    </c:if>
                                </td>
                                <td>
                                <span style="font-weight: bold; color: #007bff;">
                                    <fmt:formatNumber value="${course.price}" type="currency" currencySymbol="₫"/>
                                </span>
                                </td>
                                <td>
                                <span class="class-count-badge"
                                      style="background: ${course.classCount > 0 ? '#28a745' : '#dc3545'};
                                              color: white; padding: 4px 8px; border-radius: 12px; font-size: 12px;">
                                    ${course.classCount} lớp
                                </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${course.classCount > 0}">
                                            <a href="SaleDashboard?action=course&courseId=${course.id}"
                                               class="btn-consult"
                                               style="background: #007bff; color: white; padding: 6px 12px;
                                                  text-decoration: none; border-radius: 4px; font-size: 12px;">
                                                <i class="fas fa-comments"></i> Chi tiết
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #999; font-size: 12px;">Chưa có lớp</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
