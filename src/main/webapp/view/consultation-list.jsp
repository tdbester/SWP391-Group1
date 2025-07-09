<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 5/29/2025
  Time: 3:20 PM
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
<%--    *  Created on:        2025-05-29--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-05-29  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Danh sách học sinh tư vấn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .no-results {
            color: red;
            font-weight: bold;
            text-align: center;
            margin-top: 20px;
        }

        .btn-primary, .btn-success, .btn-warning, .btn-add-custom {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
            color: #fff;
            border-radius: 4px;
            padding: 6px 12px;
            font-family: 'Poppins', sans-serif;
        }

        .btn-primary:hover, .btn-success:hover, .btn-warning:hover, .btn-add-custom:hover {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
        }

        .btn-warning.btn-sm {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
            color: #fff;
            border-radius: 4px;
            font-family: 'Poppins', sans-serif;
        }

        .btn-warning.btn-sm:hover {
            background-color: #6a5acd;
            border-color: #6a5acd;
        }

        /* Button Xóa: Đỏ (giữ màu Bootstrap nhưng bo góc theo template) */
        .btn-danger.btn-sm {
            background-color: #dc3545;
            border-color: #dc3545;
            border-radius: 4px;
            font-family: 'Poppins', sans-serif;
        }

        .btn-danger.btn-sm:hover {
            background-color: #c82333;
            border-color: #c82333;
        }

        /* Button Quay lại: Trắng nền, chữ tím */
        .btn-back-custom {
            background-color: #fff;
            color: #7a6ad8;
            border: 1px solid #7a6ad8;
            border-radius: 20px;
            padding: 0px 25px;
            height: 40px;
            line-height: 40px;
            font-family: 'Poppins', sans-serif;
            font-weight: 500;
            transition: all .3s;
        }

        .btn-back-custom:hover {
            background-color: #7a6ad8;
            color: #fff;
        }

        th {
            background-color: #007bff;
            color: white;
        }

        /* Custom styles for search and filter section */
        .search-filter-section {
            background: linear-gradient(135deg, #f8f9ff 0%, #e8eaff 100%);
            border: 1px solid #e0e6ff;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 30px;
            box-shadow: 0 2px 10px rgba(122, 106, 216, 0.1);
        }

        .search-filter-section .form-control,
        .search-filter-section .form-select {
            border: 1px solid #d0d7ff;
            border-radius: 8px;
            padding: 10px 15px;
            font-size: 14px;
            transition: all 0.3s ease;
            background-color: #fff;
        }

        .search-filter-section .form-control:focus,
        .search-filter-section .form-select:focus {
            border-color: #7a6ad8;
            box-shadow: 0 0 0 0.2rem rgba(122, 106, 216, 0.25);
            transform: translateY(-1px);
        }

        .search-filter-section .input-group-text {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
            color: white;
            border-radius: 8px 0 0 8px;
        }

        .clear-filters-btn {
            background-color: #6c757d;
            border-color: #6c757d;
            color: white;
            border-radius: 8px;
            padding: 10px 20px;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .clear-filters-btn:hover {
            background-color: #5a6268;
            border-color: #545b62;
            transform: translateY(-1px);
        }

        .filter-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 5px;
            font-size: 14px;
        }

        /* Loading spinner for auto-filtering */
        .loading-spinner {
            display: none;
            width: 20px;
            height: 20px;
            border: 2px solid #f3f3f3;
            border-top: 2px solid #7a6ad8;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin-left: 10px;
        }

        @keyframes spin {
            0% {
                transform: rotate(0deg);
            }
            100% {
                transform: rotate(360deg);
            }
        }

        /* Add form section styling */
        .add-student-section {
            background: linear-gradient(135deg, #fff8e1 0%, #fff3c4 100%);
            border: 1px solid #ffe082;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 30px;
            box-shadow: 0 2px 10px rgba(255, 193, 7, 0.1);
        }

        .add-student-section h2 {
            color: #f57c00;
            margin-bottom: 20px;
            font-size: 1.3rem;
        }

        /* Table improvements */
        .table-container {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .table thead th {
            background: linear-gradient(135deg, #7a6ad8 0%, #6a5acd 100%);
            border: none;
            font-weight: 600;
            padding: 15px;
            font-size: 14px;
        }

        .table tbody td {
            padding: 12px 15px;
            border-color: #f1f3f4;
            vertical-align: middle;
        }

        .table tbody tr:hover {
            background-color: #f8f9ff;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="dashboard">
<jsp:include page="sale-sidebar.jsp"/>
    <div class="main-content">
        <div class="container mt-4">
            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-success">${sessionScope.message}</div>
                <c:remove var="message" scope="session"/>
            </c:if>

            <h1 class="mb-4">
                <i class="fas fa-users text-primary me-2"></i>
                Danh sách học sinh đăng ký tư vấn
            </h1>

            <div class="search-filter-section">
                <div class="row g-3 align-items-end">
                    <div class="col-md-4">
                        <label class="filter-label">
                            <i class="fas fa-search me-1"></i>
                            Tìm kiếm
                        </label>
                        <div class="input-group">
                    <span class="input-group-text">
                        <i class="fas fa-search"></i>
                    </span>
                            <input type="text"
                                   id="searchInput"
                                   class="form-control"
                                   placeholder="Nhập từ khoá để tìm kiếm"
                                   value="${keyword}"/>
                        </div>
                    </div>

                    <div class="col-md-3">
                        <label class="filter-label">
                            <i class="fas fa-book me-1"></i>
                            Khóa học
                        </label>
                        <select id="courseFilter" class="form-select">
                            <option value="">Tất cả khóa học</option>
                            <c:forEach var="subject" items="${subjects}">
                                <option value="${subject.title}" ${course_filter == subject.title ? 'selected' : ''}>
                                        ${subject.title}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="filter-label">
                            <i class="fas fa-flag me-1"></i>
                            Trạng thái
                        </label>
                        <select id="statusFilter" name="statusFilter" class="form-select">
                            <option value="">Tất cả trạng thái</option>
                            <option value="Đồng ý" <c:if test="${statusFilter == 'Đồng ý'}">selected</c:if>>Đồng ý</option>
                            <option value="Đang xử lý" <c:if test="${statusFilter == 'Đang xử lý'}">selected</c:if>>Đang xử lý</option>
                            <option value="Từ chối" <c:if test="${statusFilter == 'Từ chối'}">selected</c:if>>Từ chối</option>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <button type="button" id="clearFiltersBtn" class="btn clear-filters-btn w-100">
                            <i class="fas fa-eraser me-1"></i>
                            Xóa bộ lọc
                        </button>
                    </div>
                </div>
            </div>

            <div class="add-student-section">
                <h2>
                    <i class="fas fa-user-plus me-2"></i>
                    Thêm học sinh mới
                </h2>
                <form method="post" action="Consultation" class="row g-3">
                    <div class="col-md-3">
                        <input type="text" name="name" class="form-control" placeholder="Họ tên" required/>
                    </div>
                    <div class="col-md-3">
                        <input type="email" name="email" class="form-control" placeholder="Email" required/>
                    </div>
                    <div class="col-md-2">
                        <input type="text" name="phone" class="form-control" placeholder="Số điện thoại" required/>
                    </div>
                    <div class="col-md-3">
                        <select name="course_interest" class="form-select" required>
                            <option value="">--Chọn khóa học--</option>
                            <c:forEach var="subject" items="${subjects}">
                                <option value="${subject.id}">${subject.title}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-1 d-grid">
                        <button type="submit" name="action" value="add" class="btn btn-success">
                            Thêm
                        </button>
                    </div>
                </form>
            </div>

            <!-- Table Section -->
            <div class="table-container">
                <table class="table table-hover mb-0">
                    <thead>
                    <tr>
                        <th><i class="fas fa-hashtag me-1"></i>ID</th>
                        <th><i class="fas fa-user me-1"></i>Họ tên</th>
                        <th><i class="fas fa-envelope me-1"></i>Email</th>
                        <th><i class="fas fa-phone me-1"></i>Số điện thoại</th>
                        <th><i class="fas fa-book me-1"></i>Khóa học</th>
                        <th><i class="fas fa-flag me-1"></i>Trạng thái</th>
                        <th><i class="fas fa-cog me-1"></i>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="c" items="${consultations}">
                        <tr>
                            <td><span class="badge bg-light text-dark">${c.id}</span></td>
                            <td>${c.fullName}</td>
                            <td>${c.email}</td>
                            <td>${c.phone}</td>
                            <td><span class="badge bg-info text-white">${c.title}</span></td>
                            <td>
                                <form action="Consultation?action=updateConsultationStatus" method="post">
                                    <input type="hidden" name="id" value="${c.id}"/>
                                    <select name="status" class="form-select form-select-sm"
                                            onchange="this.form.submit()">
                                        <option value="Đang xử lý"
                                                <c:if test="${c.status eq 'Đang xử lý'}">selected</c:if>>
                                            Đang xử lý
                                        </option>
                                        <option value="Đồng ý" <c:if test="${c.status eq 'Đồng ý'}">selected</c:if>>
                                            Đồng ý
                                        </option>
                                        <option value="Từ chối" <c:if test="${c.status eq 'Từ chối'}">selected</c:if>>
                                            Từ chối
                                        </option>
                                    </select>
                                </form>
                            </td>
                            <td>
                                <button
                                        type="button"
                                        class="btn btn-sm btn-warning me-1 editBtn"
                                        data-id="${c.id}"
                                        data-name="${c.fullName}"
                                        data-email="${c.email}"
                                        data-phone="${c.phone}"
                                        data-course-id="${c.courseId}"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editModal">
                                    Sửa
                                </button>


                                <form method="post" action="Consultation" style="display:inline;">
                                    <input type="hidden" name="id" value="${c.id}"/>
                                    <button type="submit" name="action" value="delete" class="btn btn-sm btn-danger"
                                            onclick="return confirm('Bạn có chắc muốn xóa?')">
                                        Xóa
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="d-flex justify-content-center mt-4">
                <nav>
                    <ul class="pagination">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link" href="Consultation?action=list&page=${i}">${i}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const searchInput = document.getElementById('searchInput');
        const courseFilter = document.getElementById('courseFilter');
        const statusFilter = document.getElementById('statusFilter');
        const clearFiltersBtn = document.getElementById('clearFiltersBtn');

        let searchTimer;

        // Tìm kiếm với delay
        function searchWithDelay() {
            clearTimeout(searchTimer);
            searchTimer = setTimeout(function () {
                const keyword = searchInput.value.trim();
                const contextPath = '${pageContext.request.contextPath}';
                const baseUrl = contextPath + '/Consultation';
                if (keyword) {
                    window.location.href = baseUrl + '?action=search&keyword=' + encodeURIComponent(keyword);
                } else {
                    window.location.href = baseUrl;
                }
            }, 800);
        }

        function filterByCourse() {
            const courseValue = courseFilter.value;
            if (courseValue) {
                window.location.href = 'Consultation?action=filterByCourse&course_filter=' + encodeURIComponent(courseValue);
            } else {
                window.location.href = 'Consultation';
            }
        }

        function filterByStatus() {
            const statusValue = statusFilter.value;
            if (statusValue) {
                window.location.href = 'Consultation?action=filterByStatus&statusFilter=' + encodeURIComponent(statusValue);
            } else {
                window.location.href = 'Consultation';
            }
        }

        function clearAllFilters() {
            window.location.href = 'Consultation';
        }

        searchInput.addEventListener('input', searchWithDelay);
        courseFilter.addEventListener('change', filterByCourse);
        statusFilter.addEventListener('change', filterByStatus);
        clearFiltersBtn.addEventListener('click', clearAllFilters);

        searchInput.addEventListener('input', function () {
            if (this.value.length > 0) {
                this.style.borderColor = '#7a6ad8';
            } else {
                this.style.borderColor = '#d0d7ff';
            }
        });
    });
    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('.editBtn').forEach(function (button) {
            button.addEventListener('click', function () {
                document.getElementById('editId').value = this.getAttribute('data-id');
                document.getElementById('editName').value = this.getAttribute('data-name');
                document.getElementById('editEmail').value = this.getAttribute('data-email');
                document.getElementById('editPhone').value = this.getAttribute('data-phone');

                const courseId = this.getAttribute('data-course-id');
                const courseSelect = document.getElementById('editCourse');

                Array.from(courseSelect.options).forEach(function(option) {
                    option.selected = (option.value === courseId);
                });
            });
        });
    });
</script>

<jsp:include page="footer.jsp"/>
</body>
<!-- Edit Modal -->
<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <form method="post" action="Consultation">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editModalLabel">Chỉnh sửa học sinh</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" id="editId">
                    <div class="mb-3">
                        <label for="editName" class="form-label">Họ tên</label>
                        <input type="text" class="form-control" name="name" id="editName" required>
                    </div>
                    <div class="mb-3">
                        <label for="editEmail" class="form-label">Email</label>
                        <input type="email" class="form-control" name="email" id="editEmail" required>
                    </div>
                    <div class="mb-3">
                        <label for="editPhone" class="form-label">Số điện thoại</label>
                        <input type="text" class="form-control" name="phone" id="editPhone" required>
                    </div>
                    <div class="mb-3">
                        <label for="editCourse" class="form-label">Khóa học quan tâm</label>
                        <select class="form-select" name="course_interest" id="editCourse" required>
                            <c:forEach var="subject" items="${subjects}">
                                <option value="${subject.id}">${subject.title}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" name="action" value="update" class="btn btn-primary">Lưu thay đổi</button>
                </div>
            </div>
        </form>
    </div>
</div>

</html>