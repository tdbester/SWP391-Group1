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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <title>Danh sách học sinh tư vấn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/consultation-list.css">
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
                <i class="fas fa-comments text-primary me-2"></i>
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

                    <div class="col-md-2 d-grid gap-2 d-md-flex justify-content-md-end">
                        <button type="button" id="clearFiltersBtn" class="btn clear-filters-btn w-100">
                            <i class="fas fa-eraser me-1"></i>
                            Xóa bộ lọc
                        </button>
                        <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addModal">
                            <i class="fas fa-plus me-1"></i> Thêm
                        </button>
                    </div>
                </div>
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
                        <th><i class="fas fa-flag me-1"></i>Ghi chú</th>
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
                                <c:choose>
                                    <c:when test="${fn:length(c.note) > 10}">
                                        ${fn:substring(c.note, 0, 10)}...
                                    </c:when>
                                    <c:otherwise>
                                        ${c.note}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="dropdown">
                                    <button class="btn btn-sm btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                        <i class="fas fa-ellipsis-v"></i>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li><a class="dropdown-item editBtn" href="#" data-id="${c.id}" data-note="${c.note}"
                                               data-id="${c.id}"
                                               data-name="${c.fullName}"
                                               data-email="${c.email}"
                                               data-phone="${c.phone}"
                                               data-course="${c.courseId}"
                                               data-note="${c.note}"
                                               data-bs-toggle="modal" data-bs-target="#editModal">Sửa</a></li>
                                        <li>
                                            <form method="post" action="Consultation" onsubmit="return confirm('Xác nhận xóa?')">
                                                <input type="hidden" name="id" value="${c.id}"/>
                                                <button class="dropdown-item" name="action" value="delete">Xóa</button>
                                            </form>
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="Consultation?action=viewDetail&id=${c.id}">Xem chi tiết</a>
                                        </li>
                                    </ul>
                                </div>
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
                document.getElementById('editNote').value = this.getAttribute('data-note');

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
                    <div class="mb-3">
                        <label for="editCourse" class="form-label">Ghi chú</label>
                        <textarea class="form-control" name="note" id="addNote" rows="4"></textarea>
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
<!-- Add Modal -->
<div class="modal fade" id="addModal" tabindex="-1" aria-labelledby="addModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <form method="post" action="Consultation">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addModalLabel">Thêm học sinh mới</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="addName" class="form-label">Họ tên</label>
                        <input type="text" class="form-control" name="name" id="addName" required>
                    </div>
                    <div class="mb-3">
                        <label for="addEmail" class="form-label">Email</label>
                        <input type="email" class="form-control" name="email" id="addEmail" required>
                    </div>
                    <div class="mb-3">
                        <label for="addPhone" class="form-label">Số điện thoại</label>
                        <input type="text" class="form-control" name="phone" id="addPhone" required>
                    </div>
                    <div class="mb-3">
                        <label for="addCourse" class="form-label">Khóa học quan tâm</label>
                        <select class="form-select" name="course_interest" id="addCourse" required>
                            <c:forEach var="subject" items="${subjects}">
                                <option value="${subject.id}">${subject.title}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="addNote" class="form-label">Ghi chú</label>
                        <textarea class="form-control" name="note" id="addNote" rows="4"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" name="action" value="add" class="btn btn-primary">Thêm</button>
                </div>
            </div>
        </form>
    </div>
</div>
</html>