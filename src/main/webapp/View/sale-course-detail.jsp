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

    <style>
        /* Course Detail Styles */
        .course-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 12px;
            margin-bottom: 30px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            color: #007bff;
            text-decoration: none;
            font-weight: 500;
            margin-bottom: 20px;
            padding: 8px 16px;
            border-radius: 6px;
            transition: all 0.3s ease;
            background: rgba(0,123,255,0.1);
        }

        .back-link:hover {
            background: rgba(0,123,255,0.2);
            transform: translateX(-5px);
            text-decoration: none;
            color: #0056b3;
        }

        .back-link i {
            margin-right: 8px;
        }

        .course-title {
            font-size: 2.5rem;
            font-weight: 700;
            margin: 0 0 15px 0;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3);
        }

        .course-price {
            font-size: 1.5rem;
            font-weight: 600;
            background: rgba(255,255,255,0.2);
            padding: 8px 16px;
            border-radius: 25px;
            display: inline-block;
        }

        .section-title {
            color: #333;
            font-size: 1.8rem;
            font-weight: 600;
            margin: 30px 0 20px 0;
            display: flex;
            align-items: center;
        }

        .section-title i {
            margin-right: 12px;
            color: #007bff;
        }

        .class-card {
            background: white;
            border: 1px solid #e9ecef;
            border-radius: 12px;
            padding: 25px;
            margin-bottom: 20px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .class-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 4px;
            height: 100%;
            background: linear-gradient(135deg, #007bff, #0056b3);
        }

        .class-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .class-name {
            color: #333;
            font-size: 1.4rem;
            font-weight: 600;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
        }

        .class-name i {
            margin-right: 10px;
            color: #007bff;
        }

        .class-info {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            margin-bottom: 20px;
        }

        .info-item {
            display: flex;
            align-items: center;
            padding: 10px;
            background: #f8f9fa;
            border-radius: 8px;
            transition: background 0.3s ease;
        }

        .info-item:hover {
            background: #e9ecef;
        }

        .info-item i {
            margin-right: 10px;
            color: #007bff;
            width: 20px;
            text-align: center;
        }

        .info-label {
            font-weight: 600;
            color: #495057;
            margin-right: 8px;
        }

        .seats-available {
            color: #28a745;
            font-weight: 600;
        }

        .seats-low {
            color: #dc3545;
            font-weight: 600;
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0% { opacity: 1; }
            50% { opacity: 0.6; }
            100% { opacity: 1; }
        }

        .schedule-section {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 15px;
            margin: 20px 0;
        }

        .schedule-title {
            font-weight: 600;
            color: #495057;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
        }

        .schedule-title i {
            margin-right: 8px;
            color: #007bff;
        }

        .schedule-list {
            max-height: 200px;
            overflow-y: auto;
        }

        .schedule-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 15px;
            margin-bottom: 8px;
            background: white;
            border-radius: 6px;
            border-left: 3px solid #007bff;
            transition: all 0.3s ease;
        }

        .schedule-item:hover {
            transform: translateX(5px);
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .schedule-date {
            font-weight: 600;
            color: #333;
        }

        .schedule-time {
            color: #007bff;
            font-weight: 500;
            background: rgba(0,123,255,0.1);
            padding: 4px 8px;
            border-radius: 4px;
        }

        .schedule-room {
            background: #28a745;
            color: white;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 0.9rem;
            font-weight: 500;
        }

        .no-schedule {
            color: #6c757d;
            font-style: italic;
            text-align: center;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 6px;
            border: 2px dashed #dee2e6;
        }

        .btn-consult {
            background: linear-gradient(135deg, #007bff, #0056b3);
            color: white;
            padding: 12px 24px;
            text-decoration: none;
            border-radius: 25px;
            font-weight: 600;
            display: inline-flex;
            align-items: center;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(0,123,255,0.3);
            margin-top: 15px;
        }

        .btn-consult:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0,123,255,0.4);
            text-decoration: none;
            color: white;
        }

        .btn-consult i {
            margin-right: 8px;
        }

        .btn-disabled {
            background: #6c757d;
            color: white;
            padding: 12px 24px;
            border-radius: 25px;
            font-weight: 600;
            display: inline-flex;
            align-items: center;
            margin-top: 15px;
            opacity: 0.6;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #6c757d;
        }

        .empty-state i {
            font-size: 4rem;
            margin-bottom: 20px;
            color: #dee2e6;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .course-title {
                font-size: 2rem;
            }

            .class-info {
                grid-template-columns: 1fr;
            }

            .schedule-item {
                flex-direction: column;
                align-items: flex-start;
                gap: 5px;
            }
        }
    </style>
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
                                    <div class="schedule-list">
                                        <c:forEach var="schedule" items="${requestScope[scheduleKey]}" begin="0" end="4" varStatus="status">
                                            <div class="schedule-item">
                                                <span class="schedule-date">
                                                    <i class="fas fa-calendar"></i>
                                                    ${schedule.date}
                                                </span>
                                                <span class="schedule-time">
                                                    <i class="fas fa-clock"></i>
                                                    ${schedule.slotStartTime} - ${schedule.slotEndTime}
                                                </span>
                                                <span class="schedule-room">
                                                    <i class="fas fa-door-open"></i>
                                                    ${schedule.roomCode}
                                                </span>
                                            </div>
                                        </c:forEach>

                                        <c:if test="${requestScope[scheduleKey].size() > 5}">
                                            <div style="text-align: center; margin-top: 10px; color: #6c757d; font-style: italic;">
                                                ... và ${requestScope[scheduleKey].size() - 5} buổi học khác
                                            </div>
                                        </c:if>
                                    </div>
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
