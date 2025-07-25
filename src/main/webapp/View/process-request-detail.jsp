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
<%@ page import="org.example.talentcenter.model.Schedule" %>
<%
    Request requestDetail = (Request) request.getAttribute("requestDetail");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    Schedule oldSchedule = (Schedule) request.getAttribute("oldSchedule");
    String ngayNghi = (String) request.getAttribute("ngayNghi");
    String reason = "";
    String targetClassName = "";
    boolean isTransferRequest = false;
    String changeFrom = "";
    String changeTo = "";
    String changeSlot = "";
    String scheduleId = "";
    boolean isChangeRequest = false;
    boolean isLeaveRequest = false;

    if (requestDetail != null) {
        String typeName = requestDetail.getTypeName();
        String fullReason = requestDetail.getReason();

        if ("Đơn xin đổi lịch dạy".equals(typeName)) {
            isChangeRequest = true;
            reason = fullReason != null ? fullReason : "";

            if (requestDetail.getFromDate() != null) {
                changeFrom = requestDetail.getFromDate().toString();
            }
            if (requestDetail.getToDate() != null) {
                changeTo = requestDetail.getToDate().toString();
            }
            if (requestDetail.getSlot() > 0) {
                changeSlot = String.valueOf(requestDetail.getSlot());
            }
            if (requestDetail.getScheduleId() > 0) {
                scheduleId = String.valueOf(requestDetail.getScheduleId());
            }
        }
        // Kiểm tra đơn xin nghỉ phép
        else if ("Đơn xin nghỉ phép".equals(typeName)) {
            isLeaveRequest = true;
            reason = fullReason != null ? fullReason : "";
        }
        // Kiểm tra đơn chuyển lớp
        else if ("Đơn xin chuyển lớp".equals(typeName)) {
            isTransferRequest = true;
            if (fullReason != null && fullReason.contains("TARGET_CLASS:")) {
                String[] reasonParts = fullReason.split("\\|TARGET_CLASS:");
                if (reasonParts.length >= 2) {
                    reason = reasonParts[0];
                    targetClassName = reasonParts[1];
                }
            } else {
                reason = fullReason != null ? fullReason : "";
            }
        }
        // Các loại đơn khác
        else {
            reason = fullReason != null ? fullReason : "";
        }

    }
%>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn - Training Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/process-request-detail.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">

</head>

<body>
<jsp:include page="header.jsp"/>

<div class="container">
    <jsp:include page="training-manager-sidebar.jsp"/>
    <div class="main-content">
        <div class="page-header">
            <h1><i class="fas fa-file-alt"></i> Chi tiết đơn từ</h1>
            <a href="ProcessRequest?action=list" class="back-link">
                <i class="fas fa-arrow-left"></i> Quay lại danh sách
            </a>
        </div>

        <% if (requestDetail != null) { %>

        <!-- Thông tin người gửi đơn -->
        <div class="info-section">
            <h2 class="section-title">
                <i class="fas fa-user"></i> Thông tin người gửi đơn
            </h2>
            <% String senderRole = requestDetail.getSenderRole(); %>
            <table class="info-table">
                <tr>
                    <td><i class="fas fa-id-card"></i> Họ và tên:</td>
                    <td><%= requestDetail.getSenderName() != null ? requestDetail.getSenderName() : "" %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-phone"></i> Số điện thoại:</td>
                    <td><%= requestDetail.getPhoneNumber() != null ? requestDetail.getPhoneNumber() : "" %></td>
                </tr>

                <% if ("giáo viên".equalsIgnoreCase(senderRole)) { %>
                <tr>
                    <td><i class="fas fa-envelope"></i> Email:</td>
                    <td><%= requestDetail.getSenderEmail() != null ? requestDetail.getSenderEmail() : "" %></td>
                </tr>
                <% } else { %>
                <tr>
                    <td><i class="fas fa-phone-alt"></i> SĐT phụ huynh:</td>
                    <td><%= requestDetail.getParentPhone() != null ? requestDetail.getParentPhone() : "" %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-chalkboard-teacher"></i> Lớp hiện tại:</td>
                    <td><%= requestDetail.getCourseName() != null ? requestDetail.getCourseName() : "" %></td>
                </tr>
                <% } %>

                <tr>
                    <td><i class="fas fa-user-tag"></i> Vai trò:</td>
                    <td><%= senderRole %></td>
                </tr>
            </table>

        </div>

        <!-- Thông tin đơn -->
        <div class="info-section">
            <h2 class="section-title">
                <i class="fas fa-info-circle"></i> Thông tin đơn
            </h2>
            <table class="info-table">
                <tr>
                    <td><i class="fas fa-file-alt"></i> Loại đơn:</td>
                    <td><%= requestDetail.getTypeName() != null ? requestDetail.getTypeName() : "" %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-calendar"></i> Ngày gửi:</td>
                    <td><%= requestDetail.getCreatedAt() != null ? dateFormat.format(requestDetail.getCreatedAt()) : "" %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-flag"></i> Trạng thái:</td>

                    <td>
                        <%
                            String status = requestDetail.getStatus();
                            String statusClass = "";
                            if ("Chờ xử lý".equals(status)) statusClass = "status-pending";
                            else if ("Đã duyệt".equals(status)) statusClass = "status-approved";
                            else if ("Từ chối".equals(status)) statusClass = "status-rejected";
                        %>
                        <span class="status-badge <%= statusClass %>"><%= status %></span>
                    </td>
                </tr>
            </table>
        </div>

        <!-- Nội dung đơn -->
        <div class="info-section">
            <h2 class="section-title">
                <i class="fas fa-file-text"></i> Nội dung đơn
            </h2>
            <div class="content-area">
                <!-- Lý do -->
                <div class="reason-box">
                    <h3><i class="fas fa-comment-alt"></i> Lý do chi tiết</h3>
                    <div class="reason-content"><%= reason %></div>
                </div>
                <!-- Ngày nghỉ -->
                <% if (isLeaveRequest && requestDetail.getOffDate() != null) { %>
                <div class="transfer-box">
                    <h3><i class="fas fa-calendar-times"></i> Thông tin ngày nghỉ</h3>
                    <div class="transfer-detail">
                        <strong><i class="fas fa-calendar-day"></i> Ngày nghỉ:</strong> <%= requestDetail.getOffDate().toString() %>
                    </div>
                </div>
                <% } %>

                <% if (isChangeRequest) { %>
                <div class="transfer-box">
                    <h3><i class="fas fa-calendar-alt"></i> Thông tin đổi lịch dạy</h3>

                    <!-- Hiển thị thông tin chi tiết nếu có -->
                    <% if (!changeFrom.isEmpty()) { %>
                    <div class="transfer-detail">
                        <strong><i class="fas fa-calendar-day"></i> Ngày chuyển từ:</strong> <%= changeFrom %>
                    </div>
                    <% } %>

                    <% if (!changeTo.isEmpty()) { %>
                    <div class="transfer-detail">
                        <strong><i class="fas fa-calendar-check"></i> Ngày chuyển đến:</strong> <%= changeTo %>
                    </div>
                    <% } %>

                    <% if (!changeSlot.isEmpty()) { %>
                    <div class="transfer-detail">
                        <strong><i class="fas fa-clock"></i> Slot:</strong> <%= changeSlot %>
                    </div>
                    <% } %>

                    <!-- Hiển thị thông tin lịch cũ từ database -->
                    <% if (oldSchedule != null) { %>
                    <div class="transfer-detail">
                        <strong><i class="fas fa-chalkboard-teacher"></i> Lớp:</strong> <%= oldSchedule.getClassName() %>
                    </div>
                    <div class="transfer-detail">
                        <strong><i class="fas fa-calendar-alt"></i> Ngày học:</strong> <%= oldSchedule.getDate() %>
                    </div>
                    <div class="transfer-detail">
                        <strong><i class="fas fa-clock"></i> Thời gian:</strong> <%= oldSchedule.getSlotStartTime() %> - <%= oldSchedule.getSlotEndTime() %>
                    </div>
                    <div class="transfer-detail">
                        <strong><i class="fas fa-door-open"></i> Phòng:</strong> <%= oldSchedule.getRoomCode() %>
                    </div>
                    <% } %>

                </div>
                <% } %>

                <!-- Thông tin lớp chuyển -->
                <% if (isTransferRequest) { %>
                <div class="transfer-box">
                    <h3><i class="fas fa-exchange-alt"></i> Lớp muốn chuyển đến</h3>
                    <div class="transfer-detail">
                        <strong><i class="fas fa-chalkboard"></i> Tên lớp:</strong> <%= targetClassName %>
                    </div>
                    <div class="transfer-note">
                        <i class="fas fa-info-circle"></i>
                        <strong>Lưu ý:</strong> Học viên muốn chuyển từ lớp
                        <strong><%= requestDetail.getCourseName() %></strong>
                        sang lớp <strong><%= targetClassName %></strong>
                    </div>
                </div>
                <% } %>
            </div>
        </div>

        <!-- Lịch sử xử lý -->
        <% if (!"Chờ xử lý".equals(requestDetail.getStatus())) { %>
        <div class="info-section">
            <h2 class="section-title">
                <i class="fas fa-history"></i> Lịch sử xử lý
            </h2>
            <% if (requestDetail.getResponse() != null && !requestDetail.getResponse().trim().isEmpty()) { %>
            <table class="info-table">
                <tr>
                    <td><i class="fas fa-reply"></i> Phản hồi từ Training Manager:</td>
                    <td><%= requestDetail.getResponse() %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-clock"></i> Thời gian xử lý:</td>
                    <td><%= requestDetail.getResponseAt() != null ? dateFormat.format(requestDetail.getResponseAt()) : "" %></td>
                </tr>
            </table>
            <% } else { %>
            <div class="empty-state">
                <i class="fas fa-inbox" style="font-size: 48px; margin-bottom: 15px; color: #dee2e6;"></i>
                <p>Chưa có lịch sử xử lý</p>
            </div>
            <% } %>
        </div>
        <% } %>

        <!-- Form xử lý đơn -->
        <% if ("Chờ xử lý".equals(requestDetail.getStatus())) { %>
        <div class="info-section">
            <h2 class="section-title">
                <i class="fas fa-cogs"></i> Xử lý đơn
            </h2>
            <form action="ProcessRequest" method="post" class="process-form">
                <input type="hidden" name="requestId" value="<%= requestDetail.getId() %>">

                <div class="form-group">
                    <label for="managerNote" class="form-label">
                        <i class="fas fa-edit"></i> Ghi chú/Phản hồi của người xử lý:
                    </label>
                    <textarea name="managerNote" id="managerNote" rows="5" class="form-textarea"
                              placeholder="Nhập phản hồi chi tiết cho người gửi đơn..." required></textarea>
                </div>

                <div class="btn-group">
                    <button type="submit" name="action" value="approve" class="btn btn-approve">
                        <i class="fas fa-check"></i> Duyệt đơn
                    </button>
                    <button type="submit" name="action" value="reject" class="btn btn-reject">
                        <i class="fas fa-times"></i> Từ chối
                    </button>
                </div>
            </form>
        </div>
        <% } %>

        <% } else { %>
        <div class="info-section">
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle" style="font-size: 48px; margin-bottom: 15px; color: #e74c3c;"></i>
                <p>Không tìm thấy thông tin đơn.</p>
            </div>
        </div>
        <% } %>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
