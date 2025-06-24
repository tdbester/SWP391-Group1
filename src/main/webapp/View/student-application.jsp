<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/13/2025
  Time: 12:02 AM
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
    *  Created on:        2025-06-13
    *  Author:            Cù Thị Huyền Trang
    *
    *  ========================== Change History ==========================
    *  Date        | Author               | Description
    *  ------------|----------------------|--------------------------------
    *  2025-06-13  | Cù Thị Huyền Trang   | Initial creation
    */

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="vi">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn Xin Chuyển Lớp - TALENT01</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
            color: #333;
            line-height: 1.6;
        }

        .main-content {
            margin-left: 320px;
            padding: 20px;
            background-color: #f5f5f5;
            min-height: 100vh;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 8px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .header-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #e9ecef;
        }

        .header-title {
            color: #2c3e50;
            font-size: 24px;
            font-weight: bold;
            margin: 0;
        }

        .course-info {
            color: #7f8c8d;
            font-size: 14px;
            margin-top: 5px;
        }



        .card {
            background: white;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            margin-bottom: 20px;
            overflow: hidden;
        }

        .card-header {
            padding: 20px;
            border-bottom: 1px solid #e9ecef;
            background: #f8f9fa;
            font-weight: 600;
            color: #2c3e50;
        }

        .card-body {
            padding: 20px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }

        .form-col {
            flex: 1;
        }

        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #495057;
        }

        .required {
            color: #dc3545;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .form-control:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
        }

        .form-control:disabled {
            background-color: #f8f9fa;
            color: #6c757d;
        }

        .textarea {
            min-height: 120px;
            resize: vertical;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .btn-primary {
            background: #007bff;
            color: white;
        }

        .btn-primary:hover {
            background: #0056b3;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .alert {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid;
        }

        .alert-success {
            background-color: #d4edda;
            border-color: #28a745;
            color: #155724;
        }

        .alert-warning {
            background-color: #fff3cd;
            border-color: #ffc107;
            color: #856404;
        }

        .info-box {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 20px;
        }

        .info-box p {
            margin-bottom: 15px;
        }

        .info-box p:last-child {
            margin-bottom: 0;
        }

        .button-group {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }

        @media (max-width: 768px) {
            .main-content {
                margin-left: 0;
                padding: 10px;
            }

            .header-section {
                flex-direction: column;
                align-items: flex-start;
                gap: 15px;
            }

            .form-row {
                flex-direction: column;
                gap: 0;
            }

            .button-group {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<jsp:include page="student-sidebar.jsp" />

<div class="main-content">
    <div class="container">
        <div class="header-section">
            <div>
                <h1 class="header-title"><i class="fas fa-file-alt"></i> Đơn Xin Chuyển Lớp</h1>
                <div class="course-info">Hệ thống quản lý đơn xin chuyển lớp - Trung Tâm Năng Khiếu</div>
            </div>
        </div>

        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> ${sessionScope.message}
            </div>
            <c:remove var="message" scope="session"/>
        </c:if>

        <div class="card">
            <div class="card-header">
                <i class="fas fa-edit"></i> Thông tin đơn xin chuyển lớp
            </div>
            <div class="card-body">
                <form action="StudentApplication" method="post">
                    <input type="hidden" name="action" value="create">

                    <div class="form-row">
                        <div class="form-col">
                            <label class="form-label">Họ và Tên<span class="required">*</span></label>
                            <input type="text" class="form-control" name="studentName" placeholder="Nhập họ và tên" required>
                        </div>
                        <div class="form-col">
                            <label class="form-label">Số điện thoại<span class="required">*</span></label>
                            <input type="text" class="form-control" name="phoneNumber" placeholder="Nhập số điện thoại" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-col">
                            <label class="form-label">Lớp hiện tại <span class="required">*</span></label>
                            <input type="text" class="form-control" name="currentClass" placeholder="Nhập lớp hiện tại" required>
                        </div>
                        <div class="form-col">
                            <label class="form-label">Ngày nộp đơn <span class="required">*</span></label>
                            <input type="date" class="form-control" name="requestDate" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-col">
                            <label class="form-label">Số điện thoại phụ huynh <span class="required">*</span></label>
                            <input type="tel" class="form-control" name="parentPhone"
                                   placeholder="Nhập số điện thoại phụ huynh" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Mô tả chi tiết lý do <span class="required">*</span></label>
                        <textarea class="form-control textarea" name="detailedReason"
                                  placeholder="Vui lòng mô tả chi tiết lý do chuyển lớp, tình huống cụ thể..."
                                  required></textarea>
                        <small style="color: #6c757d; font-size: 0.875rem; margin-top: 5px; display: block;">
                            <i class="fas fa-info-circle"></i> Tối thiểu 50 ký tự. Mô tả càng chi tiết càng giúp việc xét duyệt nhanh chóng.
                        </small>
                    </div>

                    <div class="button-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-paper-plane"></i> Gửi đơn xin chuyển lớp
                        </button>
                        <button type="reset" class="btn btn-secondary">
                            <i class="fas fa-undo"></i> Làm mới
                        </button>
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
                        <li>Đơn chỉ được nộp trong thời gian quy định</li>
                        <li>Cần có sự đồng ý của phụ huynh</li>
                        <li>Thời gian xử lý: 5-7 ngày làm việc</li>
                        <li>Liên hệ GVCN nếu cần hỗ trợ</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />

<script>
    // mặc định là ngyaf hiện tại
    document.addEventListener('DOMContentLoaded', function () {
        var dateInput = document.querySelector('input[name="requestDate"]');
        if (dateInput) {
            var today = new Date().toISOString().split('T')[0];
            dateInput.value = today;
        }
    });

    // validate form
    function validateForm() {
        var detailedReason = document.querySelector('textarea[name="detailedReason"]').value;
        var parentPhone = document.querySelector('input[name="parentPhone"]').value;
        if (!detailedReason || !parentPhone) {
            alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
            return false;
        }
        return true;
    }

    // Add form validation to submit button
    document.addEventListener('DOMContentLoaded', function () {
        var form = document.querySelector('form[action="StudentApplication"]');
        if (form) {
            form.addEventListener('submit', function (e) {
                if (!validateForm()) {
                    e.preventDefault();
                }
            });
        }
    });
</script>

</body>
</html>