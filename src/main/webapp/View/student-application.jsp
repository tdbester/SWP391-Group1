<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/13/2025
  Time: 12:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Đơn Xin Chuyển Lớp - Trung Tâm Năng Khiếu</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f6fa;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .header {
            background: linear-gradient(135deg, #7a6ad8, #8d78e4);
            color: white;
            padding: 20px 0;
            text-align: center;
            margin-bottom: 30px;
            border-radius: 10px;
        }

        .header h1 {
            font-size: 2rem;
            margin-bottom: 10px;
        }

        .header p {
            font-size: 1.1rem;
            opacity: 0.9;
        }

        .nav-tabs {
            display: flex;
            background: white;
            border-radius: 10px 10px 0 0;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 0;
        }

        .nav-tab {
            flex: 1;
            padding: 15px 20px;
            background: #f8f9fa;
            border: none;
            cursor: pointer;
            font-size: 1rem;
            font-weight: 500;
            transition: all 0.3s ease;
            border-right: 1px solid #dee2e6;
        }

        .nav-tab:last-child {
            border-right: none;
        }

        .nav-tab.active {
            background: #7a6ad8;
            color: white;
        }

        .nav-tab:hover:not(.active) {
            background: #e9ecef;
        }

        .tab-content {
            background: white;
            border-radius: 0 0 10px 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            min-height: 600px;
        }

        .tab-pane {
            display: none;
            padding: 30px;
        }

        .tab-pane.active {
            display: block;
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
            border-color: #7a6ad8;
            box-shadow: 0 0 0 3px rgba(122, 106, 216, 0.1);
        }

        .form-control:disabled {
            background-color: #f8f9fa;
            color: #6c757d;
        }

        .form-select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 1rem;
            background-color: white;
            cursor: pointer;
            transition: border-color 0.3s ease;
        }

        .form-select:focus {
            outline: none;
            border-color: #7a6ad8;
            box-shadow: 0 0 0 3px rgba(122, 106, 216, 0.1);
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
            background: #7a6ad8;
            color: white;
        }

        .btn-primary:hover {
            background: #6a5acd;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .btn-success {
            background: #28a745;
            color: white;
        }

        .btn-success:hover {
            background: #218838;
        }

        .btn-warning {
            background: #ffc107;
            color: #212529;
        }

        .btn-warning:hover {
            background: #e0a800;
        }

        .btn-danger {
            background: #dc3545;
            color: white;
        }

        .btn-danger:hover {
            background: #c82333;
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

        .alert-info {
            background-color: #d1ecf1;
            border-color: #17a2b8;
            color: #0c5460;
        }

        .card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        .card-header {
            padding: 20px;
            border-bottom: 1px solid #e9ecef;
            background: #f8f9fa;
            border-radius: 10px 10px 0 0;
        }

        .card-body {
            padding: 20px;
        }

        .card-title {
            font-size: 1.25rem;
            font-weight: 600;
            color: #7a6ad8;
            margin-bottom: 15px;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .table th,
        .table td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #e9ecef;
        }

        .table th {
            background: #7a6ad8;
            color: white;
            font-weight: 600;
        }

        .table tr:hover {
            background: #f8f9fa;
        }

        .badge {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 0.875rem;
            font-weight: 500;
        }

        .badge-pending {
            background: #fff3cd;
            color: #856404;
        }

        .badge-approved {
            background: #d4edda;
            color: #155724;
        }

        .badge-rejected {
            background: #f8d7da;
            color: #721c24;
        }

        .badge-primary {
            background: #7a6ad8;
            color: white;
        }

        .badge-secondary {
            background: #6c757d;
            color: white;
        }

        .step-guide {
            display: flex;
            align-items: flex-start;
            margin-bottom: 20px;
        }

        .step-number {
            width: 40px;
            height: 40px;
            background: #7a6ad8;
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            margin-right: 15px;
            flex-shrink: 0;
        }

        .step-content h4 {
            color: #7a6ad8;
            margin-bottom: 5px;
        }

        .step-content p {
            color: #6c757d;
            margin: 0;
        }

        .info-box {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }

        .info-box h5 {
            color: #7a6ad8;
            margin-bottom: 10px;
        }

        .search-form {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .search-row {
            display: flex;
            gap: 15px;
            align-items: end;
        }

        .search-col {
            flex: 1;
        }

        .search-buttons {
            display: flex;
            gap: 10px;
        }

        .no-results {
            text-align: center;
            padding: 40px;
            color: #6c757d;
        }

        .no-results i {
            font-size: 3rem;
            margin-bottom: 15px;
            color: #dc3545;
        }

        @media (max-width: 768px) {
            .container {
                padding: 10px;
            }

            .form-row {
                flex-direction: column;
                gap: 0;
            }

            .nav-tabs {
                flex-direction: column;
            }

            .search-row {
                flex-direction: column;
                align-items: stretch;
            }

            .search-buttons {
                justify-content: center;
            }

            .table {
                font-size: 0.875rem;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Đơn Xin Chuyển Lớp</h1>
        <p>Hệ thống quản lý đơn xin chuyển lớp - Trung Tâm Năng Khiếu</p>
    </div>

    <!-- Success Message -->
    <c:if test="${not empty sessionScope.message}">
    <div class="alert alert-success">
            ${sessionScope.message}
    </div>
        <c:remove var="message" scope="session"/>
    </c:if>

    <!-- Navigation Tabs -->
    <div class="nav-tabs">
        <button class="nav-tab active" onclick="showTab('form')">Tạo đơn mới</button>
        <button class="nav-tab" onclick="showTab('guide')">Hướng dẫn</button>
        <button class="nav-tab" onclick="showTab('history')">Lịch sử đơn</button>
    </div>

    <div class="tab-content">
        <!-- Form Tab -->
        <div id="form" class="tab-pane active">
            <div style="display: flex; gap: 30px;">
                <div style="flex: 2;">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="card-title">Thông tin đơn xin chuyển lớp</h3>
                        </div>
                        <div class="card-body">
                            <form action="StudentApplication" method="post">
                                <input type="hidden" name="action" value="create">
                                <div class="form-row">
                                    <div class="form-col">
                                        <label class="form-label">Họ và Tên<span class="required">*</span></label>
                                        <input type="text" class="form-control" value="${student.name}" placeholder="${student.name}">
                                        <input type="hidden" name="studentName" value="${student.name}">
                                    </div>
                                    <div class="form-col">
                                        <label class="form-label">Số điện thoại<span class="required">*</span></label>
                                        <input type="text" class="form-control" name="phoneNumber"
                                               value="${student.parentPhone}" required placeholder="${student.name}">
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-col">
                                        <label class="form-label">Lớp hiện tại <span class="required">*</span></label>
                                        <input type="text" class="form-control" value="${student.className}">
                                        <input type="hidden" name="currentClass" value="${student.className}">
                                    </div>
                                    <div class="form-col">
                                        <label class="form-label">Ngày nộp đơn <span class="required">*</span></label>
                                        <input type="date" class="form-control" name="requestDate" required>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-col">
                                        <label class="form-label">Số điện thoại phụ huynh <span
                                                class="required">*</span></label>
                                        <input type="tel" class="form-control" name="parentPhone"
                                               placeholder="Nhập số điện thoại" required>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="form-label">Mô tả chi tiết lý do <span
                                            class="required">*</span></label>
                                    <textarea class="form-control textarea" name="detailedReason"
                                              placeholder="Vui lòng mô tả chi tiết lý do chuyển lớp, tình huống cụ thể..."
                                              required></textarea>
                                    <small style="color: #6c757d;">Tối thiểu 50 ký tự. Mô tả càng chi tiết càng giúp
                                        việc xét duyệt nhanh chóng.</small>
                                </div>

                                <div style="display: flex; gap: 15px; margin-top: 30px;">
                                    <button type="submit" class="btn btn-primary">
                                        📄 Gửi đơn xin chuyển lớp
                                    </button>
                                    <button type="reset" class="btn btn-secondary">
                                        🔄 Làm mới
                                    </button>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>

                <div style="flex: 1;">
                    <div class="card">
                        <div class="card-header">
                            <h4>Thông tin học sinh</h4>
                        </div>
                        <div class="card-body">
                            <div class="info-box">
                                <p><strong>Họ và tên:</strong><br>Nguyễn Văn A</p>
                                <p><strong>Mã học sinh:</strong><br>HS2024001</p>
                                <p><strong>Lớp hiện tại:</strong><br><span class="badge badge-primary">12A1</span></p>
                                <p><strong>GVCN:</strong><br>Cô Nguyễn Thị Hương</p>
                                <p><strong>Năm học:</strong><br>2024-2025</p>
                            </div>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">
                            <h4>Lưu ý quan trọng</h4>
                        </div>
                        <div class="card-body">
                            <div class="alert alert-warning">
                                <strong>Chú ý:</strong>
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
        </div>
    </div>
    <script>
        function showTab(tabName) {
            // Hide all tab panes
            var tabPanes = document.querySelectorAll('.tab-pane');
            tabPanes.forEach(function (pane) {
                pane.classList.remove('active');
            });

            // Remove active class from all nav tabs
            var navTabs = document.querySelectorAll('.nav-tab');
            navTabs.forEach(function (tab) {
                tab.classList.remove('active');
            });

            // Show selected tab pane
            document.getElementById(tabName).classList.add('active');

            // Add active class to clicked nav tab
            event.target.classList.add('active');
        }

        // Set current date as default
        document.addEventListener('DOMContentLoaded', function () {
            var dateInput = document.querySelector('input[name="requestDate"]');
            if (dateInput) {
                var today = new Date().toISOString().split('T')[0];
                dateInput.value = today;
            }
        });

        // Form validation
        function validateForm() {
            var targetClass = document.querySelector('select[name="targetClass"]').value;
            var reason = document.querySelector('select[name="reason"]').value;
            var detailedReason = document.querySelector('textarea[name="detailedReason"]').value;
            var parentPhone = document.querySelector('input[name="parentPhone"]').value;

            if (!targetClass || !reason || !detailedReason || !parentPhone) {
                alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
                return false;
            }

            if (detailedReason.length < 50) {
                alert('Mô tả chi tiết phải có ít nhất 50 ký tự!');
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
