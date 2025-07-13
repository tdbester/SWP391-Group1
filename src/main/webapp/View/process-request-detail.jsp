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
            // Đây là đơn đổi lịch dạy, kiểm tra oldSchedule từ backend
            isChangeRequest = true;
            reason = fullReason != null ? fullReason : "";

            // Sử dụng model fields từ DAO thay vì parsing lại
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

        // Debug information
        System.out.println("Type: " + typeName);
        System.out.println("Reason: " + reason);
        System.out.println("Old Schedule: " + (oldSchedule != null ? "Available" : "Null"));
        System.out.println("isChangeRequest: " + isChangeRequest);
    }
%>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn - Training Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
    <style>
        /* Header */
        .page-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px 30px;
            border-radius: 15px;
            margin-bottom: 30px;
            box-shadow: 0 8px 32px rgba(102, 126, 234, 0.3);
        }

        .page-header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 600;
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            color: white;
            text-decoration: none;
            margin-top: 10px;
            padding: 8px 15px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .back-link:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateX(-5px);
        }

        /* Section styling */
        .info-section {
            background: white;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 25px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            border: 1px solid #e9ecef;
        }

        .section-title {
            color: #2c3e50;
            font-size: 20px;
            font-weight: 600;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 3px solid #3498db;
            display: flex;
            align-items: center;
        }

        .section-title i {
            margin-right: 10px;
            color: #3498db;
        }

        /* Table styling */
        .info-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        .info-table td {
            padding: 15px;
            border-bottom: 1px solid #eee;
            vertical-align: top;
        }

        .info-table td:first-child {
            background: #f8f9fa;
            font-weight: 600;
            color: #495057;
            width: 200px;
            border-right: 3px solid #dee2e6;
        }

        .info-table td:last-child {
            color: #2c3e50;
        }

        .info-table tr:last-child td {
            border-bottom: none;
        }

        /* Status badge */
        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
        }

        .status-pending {
            background: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }

        .status-approved {
            background: #d4edda;
            color: #155724;
            border: 1px solid #00b894;
        }

        .status-rejected {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #e17055;
        }

        /* Content area */
        .content-area {
            display: flex;
            gap: 25px;
            margin-bottom: 25px;
        }

        .reason-box {
            flex: 1;
            background: white;
            border: 1px solid #e9ecef;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        }

        .reason-box h3 {
            color: #2c3e50;
            margin-top: 0;
            margin-bottom: 15px;
            font-size: 18px;
            display: flex;
            align-items: center;
        }

        .reason-box h3 i {
            margin-right: 10px;
            color: #3498db;
        }

        .reason-content {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            border-left: 4px solid #3498db;
            line-height: 1.6;
            color: #2c3e50;
            white-space: pre-wrap;
        }

        /* Transfer info box */
        .transfer-box {
            flex: 1;
            background: linear-gradient(135deg, #74b9ff 0%, #0984e3 100%);
            color: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 8px 25px rgba(116, 185, 255, 0.3);
        }

        .transfer-box h3 {
            margin-top: 0;
            margin-bottom: 20px;
            font-size: 18px;
            display: flex;
            align-items: center;
        }

        .transfer-box h3 i {
            margin-right: 10px;
        }

        .transfer-detail {
            background: rgba(255, 255, 255, 0.15);
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 15px;
            backdrop-filter: blur(10px);
        }

        .transfer-note {
            background: rgba(255, 255, 255, 0.2);
            padding: 15px;
            border-radius: 10px;
            border-left: 4px solid white;
            margin-top: 15px;
        }

        /* Form styling */
        .process-form {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 6px 25px rgba(0, 0, 0, 0.1);
            border: 1px solid #e9ecef;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #2c3e50;
        }

        .form-textarea {
            width: 100%;
            padding: 15px;
            border: 2px solid #e9ecef;
            border-radius: 10px;
            font-family: inherit;
            font-size: 14px;
            resize: vertical;
            transition: border-color 0.3s ease;
        }

        .form-textarea:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
        }

        /* Button styling */
        .btn-group {
            display: flex;
            gap: 15px;
            margin-top: 25px;
        }

        .btn {
            padding: 12px 25px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            text-decoration: none;
        }

        .btn i {
            margin-right: 8px;
        }

        .btn-approve {
            background: linear-gradient(135deg, #00b894 0%, #00a085 100%);
            color: white;
            box-shadow: 0 4px 15px rgba(0, 184, 148, 0.3);
        }

        .btn-approve:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0, 184, 148, 0.4);
        }

        .btn-reject {
            background: linear-gradient(135deg, #e17055 0%, #d63031 100%);
            color: white;
            box-shadow: 0 4px 15px rgba(225, 112, 85, 0.3);
        }

        .btn-reject:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(225, 112, 85, 0.4);
        }

        /* Empty state */
        .empty-state {
            text-align: center;
            padding: 40px;
            color: #6c757d;
            font-style: italic;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .main-content {
                padding: 20px;
            }

            .content-area {
                flex-direction: column;
            }

            .btn-group {
                flex-direction: column;
            }

            .info-table td:first-child {
                width: auto;
            }
        }
    </style>
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

                <% if ("Teacher".equalsIgnoreCase(senderRole)) { %>
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
