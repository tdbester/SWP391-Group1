<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/13/2025
  Time: 12:02 AM
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
<%--    *  Created on:        2025-06-13--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-06-13  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="vi">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn Xin Chuyển Lớp - TALENT01</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/student-request.css">

</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container">
<jsp:include page="student-sidebar.jsp"/>
    <div class="main-content">
        <a href="StudentDashboard" class="back-link">
            <i class="fas fa-arrow-left"></i>
            Quay lại Dashboard
        </a>
        <div class="header-section">
            <div>
                <h1 class="header-title"><i class="fas fa-file-alt"></i> Gửi đơn</h1>
                <div class="course-info">Hệ thống quản lý đơn - Trung Tâm Năng Khiếu</div>
            </div>
        </div>

        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> ${sessionScope.message}
            </div>
            <c:remove var="message" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <div class="card">
            <div class="card-header">
                <i class="fas fa-edit"></i> Nhập thông tin đơn
            </div>
            <div class="card-body">
                <!-- Form duy nhất cho tất cả thông tin -->
                <form action="StudentApplication" method="get">
                    <div class="form-row">
                        <div class="form-col">
                            <label class="form-label">Loại đơn<span class="required">*</span></label>
                            <select class="form-control" name="requestType" onchange="this.form.submit()">
                                <option value="">-- Chọn loại đơn --</option>
                                <c:forEach var="request" items="${requestTypeList}">
                                    <option value="${request.typeId}"
                                            <c:if test="${param.requestType == request.typeId || selectedRequestType == request.typeId}">selected</c:if>>
                                            ${request.typeName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-col">
                            <label class="form-label">Họ và Tên<span class="required">*</span></label>
                            <input type="text" class="form-control" name="studentName" value="${studentName}" readonly>
                        </div>
                        <div class="form-col">
                            <label class="form-label">Số điện thoại<span class="required">*</span></label>
                            <input type="text" class="form-control" name="phoneNumber"
                                   value="${param.phoneNumber != null ? param.phoneNumber : phoneNumber}" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-col">
                            <label class="form-label">Lớp hiện tại <span class="required">*</span></label>
                            <select class="form-control" name="currentClass" required>
                                <option value="">-- Chọn lớp hiện tại --</option>
                                <c:forEach var="classRoom" items="${classList}">
                                    <option value="${classRoom.classroomName}"
                                            <c:if test="${param.currentClass == classRoom.classroomName}">selected</c:if>>
                                            ${classRoom.classroomName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-col">
                            <label class="form-label">Số điện thoại phụ huynh<span class="required">*</span></label>
                            <input type="text" class="form-control" name="parentPhone"
                                   value="${param.parentPhone}" required>
                        </div>
                    </div>

                    <!-- Phần chuyển lớp -->
                    <c:if test="${isTransferRequest || param.requestType == '1'}">
                        <div class="form-row" style="background: #f8f9ff; padding: 15px; border-radius: 8px; margin: 20px 0;">
                            <div class="form-col">
                                <label class="form-label">
                                    <i class="fas fa-exchange-alt" style="color: #4c63d2;"></i>
                                    Chọn lớp muốn chuyển tới <span class="required">*</span>
                                </label>
                                <select class="form-control" name="selectedClass" required>
                                    <option value="">-- Chọn lớp --</option>
                                    <c:forEach var="classroom" items="${availableClasses}">
                                        <option value="${classroom.classroomID}"
                                                <c:if test="${param.selectedClass == classroom.classroomID.toString()}">selected</c:if>>
                                                ${classroom.classroomName}
                                        </option>
                                    </c:forEach>
                                </select>
                                <button type="submit" class="btn btn-info" style="margin-top: 10px;" formnovalidate>
                                    <i class="fas fa-calendar-alt"></i> Xem lịch
                                </button>
                            </div>

                            <!-- Thông tin lớp đã chọn -->
                            <c:if test="${not empty selectedClassInfo}">
                                <div class="form-col">
                                    <label class="form-label">
                                        <i class="fas fa-info-circle" style="color: #28a745;"></i>
                                        Thông tin lớp
                                    </label>
                                    <div style="background: white; padding: 15px; border-radius: 8px; border: 1px solid #e9ecef;">
                                        <p style="margin: 0 0 10px 0;"><strong>Giáo viên:</strong> ${selectedClassInfo.teacherName}</p>
                                        <p style="margin: 0; font-weight: 600; color: #2c3e50;">Lịch học:</p>
                                        <ul style="margin: 5px 0 0 0; padding-left: 20px;">
                                            <c:forEach var="schedule" items="${classSchedules}">
                                                <li style="margin-bottom: 3px;">
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
                                        </ul>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </c:if>

                    <div class="form-group">
                        <label class="form-label">Mô tả chi tiết lý do <span class="required">*</span></label>
                        <textarea class="form-control textarea" name="detailedReason"
                                  placeholder="Vui lòng mô tả chi tiết lý do chuyển lớp, tình huống cụ thể..."
                                  >${param.detailedReason}</textarea>
                        <small style="color: #6c757d; font-size: 0.875rem; margin-top: 5px; display: block;">
                            <i class="fas fa-info-circle"></i> Tối thiểu 20 ký tự. Mô tả càng chi tiết càng giúp việc xét duyệt nhanh chóng.
                        </small>
                    </div>

                    <div class="button-group">
                        <button type="submit" class="btn btn-primary" formaction="StudentApplication" formmethod="post" name="action" value="create">
                            <i class="fas fa-paper-plane"></i> Gửi đơn
                        </button>
                        <a href="StudentApplication" class="btn btn-secondary">
                            <i class="fas fa-undo"></i> Làm mới
                        </a>
                    </div>
                </form>

            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <i class="fas fa-exclamation-triangle"></i> Lưu ý quan trọng
            </div>
            <div class="card-body">
                <div class="alert alert-warning">
                    <strong><i class="fas fa-info-circle"></i> Chú ý:</strong>
                    <ul style="margin: 10px 0 0 20px;">
                        <li>Thời gian xử lý: 1-7 ngày làm việc</li>
                        <li>Liên hệ qua email talencenter@gmail.com nếu cần hỗ trợ</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>