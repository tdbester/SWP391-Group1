<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/9/2025
  Time: 1:57 PM
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
<%--    *  Created on:        2025-06-09--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-06-09  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Yêu cầu cấp tài khoản học viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .btn-primary {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
            font-family: 'Poppins', sans-serif;
            font-weight: 500;
            border-radius: 4px;
            padding: 8px 16px;
            transition: background-color 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #6a5acd;
            border-color: #6a5acd;
        }

        th {
            background-color: #7a6ad8;
            color: white;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<jsp:include page="sale-sidebar.jsp"/>
<div class="dashboard">
    <div class="main-content">
        <div class=" container mt-4">
            <h2 class="mb-4"><i class="fas fa-user-plus me-2"></i>Danh sách học sinh đã đồng ý tư vấn</h2>
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
                            <i class="fas fa-flag me-1"></i>
                            Trạng thái
                        </label>
                        <select id="statusFilter" name="statusFilter" class="form-select">
                            <option value="">Tất cả trạng thái</option>
                            <option value="Đã thanh toán"
                                    <c:if test="${statusFilter == 'Đã thanh toán'}">selected</c:if>>Đã
                                thanh toán
                            </option>
                            <option value="Chưa thanh toán"
                                    <c:if test="${statusFilter == 'Chưa thanh toán'}">selected</c:if>>Chưa thanh toán
                            </option>
                        </select>
                    </div>

                    <div class="col-md-2 d-grid gap-2 d-md-flex justify-content-md-end">
                        <button type="button" id="clearFiltersBtn" class="btn clear-filters-btn w-100">
                            <i class="fas fa-eraser me-1"></i>
                            Xóa bộ lọc
                        </button>
                    </div>
                </div>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/StudentAccountRequest">
                <input type="hidden" name="action" value="sentRequest"/>
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th><input type="checkbox" id="selectAll"/></th>
                        <th>ID</th>
                        <th>Họ tên</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Trạng thái thanh toán</th>
                        <th>Trạng thái yêu cầu</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="s" items="${agreedStudents}">
                        <tr>
                            <td><input type="checkbox" name="selectedStudentIds" value="${s.id}"/></td>
                            <td>${s.id}</td>
                            <td>${s.fullName}</td>
                            <td>${s.email}</td>
                            <td>${s.phone}</td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/StudentAccountRequest">
                                    <input type="hidden" name="action" value="updatePaymentStatus"/>
                                    <input type="hidden" name="id" value="${s.id}"/>
                                    <select name="status" onchange="this.form.submit()" class="form-select form-select-sm">
                                        <option value="Đã thanh toán" <c:if test="${s.paymentStatus == 'Đã thanh toán'}">selected</c:if>>Đã thanh toán</option>
                                        <option value="Chưa thanh toán" <c:if test="${s.paymentStatus == 'Chưa thanh toán'}">selected</c:if>>Chưa thanh toán</option>
                                    </select>
                                </form>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${s.accountRequestSent}">
                                        <span class="text-success">Đã gửi</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-danger">Chưa gửi</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <button type="submit" class="btn btn-primary mt-3">
                    <i class="fas fa-paper-plane me-1"></i> Gửi yêu cầu cấp tài khoản
                </button>
            </form>
            <c:if test="${not empty message}">
                <div class="alert alert-info mt-3">${message}</div>
            </c:if>
        </div>
    </div>
</div>
<div class="d-flex justify-content-center mt-4">
    <nav>
        <ul class="pagination">
            <c:forEach begin="1" end="${totalPages}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="StudentAccountRequest?action=list&page=${i}">${i}</a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</div>

<script>
    document.getElementById('selectAll').addEventListener('click', function () {
        const checkboxes = document.querySelectorAll('input[name="selectedStudentIds"]');
        for (let cb of checkboxes) {
            cb.checked = this.checked;
        }
    });
    document.addEventListener('DOMContentLoaded', function () {
        const searchInput = document.getElementById('searchInput');
        const statusFilter = document.getElementById('statusFilter');
        const clearFiltersBtn = document.getElementById('clearFiltersBtn');
        let searchTimer;

        // Tìm kiếm với delay
        function searchWithDelay() {
            clearTimeout(searchTimer);
            searchTimer = setTimeout(function () {
                const keyword = searchInput.value.trim();
                const contextPath = '${pageContext.request.contextPath}';
                const baseUrl = contextPath + '/StudentAccountRequest';
                if (keyword) {
                    window.location.href = baseUrl + '?action=search&keyword=' + encodeURIComponent(keyword);
                } else {
                    window.location.href = baseUrl + '?action=list';
                }
            }, 800);
        }

        function filterByStatus() {
            const statusValue = statusFilter.value;
            const contextPath = '${pageContext.request.contextPath}';
            const baseUrl = contextPath + '/StudentAccountRequest';
            if (statusValue) {
                window.location.href = baseUrl + '?action=filterByStatus&statusFilter=' + encodeURIComponent(statusValue);
            } else {
                window.location.href = baseUrl + '?action=list';
            }
        }

        function clearAllFilters() {
            const contextPath = '${pageContext.request.contextPath}';
            window.location.href = contextPath + '/StudentAccountRequest?action=list';
        }

        searchInput.addEventListener('input', searchWithDelay);
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
</script>

<jsp:include page="footer.jsp"/>
</body>
</html>
