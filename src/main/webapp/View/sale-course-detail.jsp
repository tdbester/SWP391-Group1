<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 7/8/2025
  Time: 5:37 PM
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết khóa học - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sale-course-detail.css">

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

        <div class="course-header">
            <h1 class="course-title">
                <i class="fas fa-graduation-cap"></i>
                ${course.title}
            </h1>
            <div class="course-price">
                <i class="fas fa-tag"></i>
                <fmt:formatNumber value="${course.price}" type="currency" currencySymbol="₫"/>
            </div>
        </div>

        <h2 class="section-title">
            <i class="fas fa-chalkboard-teacher"></i>
            Danh sách lớp học
        </h2>

        <c:choose>
            <c:when test="${empty classes}">
                <div class="empty-state">
                    <i class="fas fa-chalkboard-teacher"></i>
                    <h3>Chưa có lớp học nào</h3>
                    <p>Khóa học này hiện chưa có lớp học được mở.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="classRoom" items="${classes}">
                    <div class="class-card">
                        <h3 class="class-name">
                            <i class="fas fa-users"></i>
                                ${classRoom.classroomName}
                        </h3>

                        <div class="class-info">
                            <div class="info-item">
                                <i class="fas fa-chalkboard-teacher"></i>
                                <span class="info-label">Giáo viên:</span>
                                <span>${classRoom.teacherName}</span>
                            </div>

                            <div class="info-item">
                                <i class="fas fa-chair"></i>
                                <span class="info-label">Sức chứa:</span>
                                <span>${classRoom.maxCapacity} học sinh</span>
                            </div>

                            <div class="info-item">
                                <i class="fas fa-user-plus"></i>
                                <span class="info-label">Chỗ trống:</span>
                                <span class="${classRoom.availableSeats > 5 ? 'seats-available' : 'seats-low'}">
                                    ${classRoom.availableSeats} chỗ
                                </span>
                            </div>
                        </div>

                        <div class="schedule-section">
                            <h4 class="schedule-title">
                                <i class="fas fa-calendar-alt"></i>
                                Lịch học
                            </h4>

                            <c:set var="scheduleKey" value="schedules_${classRoom.classroomID}" />
                            <c:choose>
                                <c:when test="${empty requestScope[scheduleKey]}">
                                    <div class="no-schedule">
                                        <i class="fas fa-calendar-times"></i>
                                        <p>Chưa có lịch học</p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <ul class="schedule-list" style="list-style: none; padding: 0; margin: 0;">
                                        <c:forEach var="schedule" items="${requestScope[scheduleKey]}" begin="0" end="4" varStatus="status">
                                            <li style="margin-bottom: 3px; padding: 8px 12px; background: white; border-radius: 6px; border-left: 3px solid #007bff;">
                                                <c:set var="dayOfWeek" value="${schedule.date.dayOfWeek.toString()}"/>
                                                <c:choose>
                                                    <c:when test="${dayOfWeek == 'MONDAY'}">Thứ 2</c:when>
                                                    <c:when test="${dayOfWeek == 'TUESDAY'}">Thứ 3</c:when>
                                                    <c:when test="${dayOfWeek == 'WEDNESDAY'}">Thứ 4</c:when>
                                                    <c:when test="${dayOfWeek == 'THURSDAY'}">Thứ 5</c:when>
                                                    <c:when test="${dayOfWeek == 'FRIDAY'}">Thứ 6</c:when>
                                                    <c:when test="${dayOfWeek == 'SATURDAY'}">Thứ 7</c:when>
                                                    <c:when test="${dayOfWeek == 'SUNDAY'}">CN</c:when>
                                                </c:choose>
                                                ${schedule.slotStartTime}-${schedule.slotEndTime} (${schedule.roomCode})
                                            </li>
                                        </c:forEach>

                                        <c:if test="${requestScope[scheduleKey].size() > 5}">
                                            <li style="text-align: center; margin-top: 10px; color: #6c757d; font-style: italic; padding: 8px 12px;">
                                                ... và ${requestScope[scheduleKey].size() - 5} buổi học khác
                                            </li>
                                        </c:if>
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:choose>
                            <c:when test="${classRoom.availableSeats > 0}">
                                <a href="Consultation?action=add&classId=${classRoom.classroomID}" class="btn-consult">
                                    <i class="fas fa-comments"></i>
                                    Tư vấn lớp này
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="btn-disabled">
                                    <i class="fas fa-ban"></i>
                                    Lớp đã đầy
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
