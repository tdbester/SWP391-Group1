<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 7/15/2025
  Time: 11:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <title>Teacher List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <style>
        .btn-primary,
        .btn-success,
        .btn-warning,
        .btn-add-custom {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
            color: #fff;
            border-radius: 4px;
            padding: 6px 12px;
            font-family: 'Poppins', sans-serif;
        }

        header {
            background: #7A5AF8;
            color: white;
            padding: 16px 32px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: sticky;
            top: 0;
            z-index: 100;
        }

        .nav a {
            color: white;
            text-decoration: none;
            font-weight: 500;
        }

        .pagination .page-item.active .page-link {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
            color: white;
        }
    </style>
</head>

<body>
<jsp:include page="header.jsp" />

<div class="">
        <%
        String userRole = (String) session.getAttribute("userRole");
        if ("admin".equalsIgnoreCase(userRole)) {
    %>
        <jsp:include page="admin-sidebar.jsp" />
    <%
        } else {
    %>
        <jsp:include page="training-manager-sidebar.jsp" />
    <%
        }
    %>
    <div class="main-content">
    <h2 class="mb-4">Danh sách giáo viên</h2>

    <!-- Success Message -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Error Message -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <form action="teachers" method="get" class="row g-2 mb-3">
        <div class="col-auto">
            <input type="search" name="search" class="form-control" placeholder="Tìm kiếm tên hoặc khoa"
                   value="${param.search}" />
        </div>
        <div class="col-auto">
            <button class="btn btn-primary">Lọc</button>
        </div>
        <div class="col-auto ms-auto">
            <a href="teachers?action=new" class="btn btn-success">
                <i class="fas fa-plus"></i> Thêm giáo viên
            </a>
        </div>
    </form>

    <table class="table table-striped">
        <thead class="table-dark">
        <tr>
            <th>STT</th>
            <th>Họ tên</th>
            <th>Khoa</th>
            <th>Đơn giá/slot</th>
            <th>Số điện thoại</th>
            <th>Email</th>
            <th>Địa chỉ</th>
            <th style="width: 150px;">Hành động</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="teacher" items="${teacherList}" varStatus="st">
            <tr>
                <td>${(currentIndex-1)*10 + st.index + 1}</td>
                <td>${teacher.account.fullName}</td>
                <td>${teacher.department}</td>
                <td>
                    <fmt:formatNumber value="${teacher.salary}" type="number" groupingUsed="true" /> VNĐ
                </td>
                <td>${teacher.account.phoneNumber}</td>
                <td>${teacher.account.email}</td>
                <td>${teacher.account.address}</td>
                <td>
                    <a href="teachers?action=detail&id=${teacher.id}" class="btn btn-primary btn-sm">Chi tiết</a>
                    <a href="teachers?action=edit&id=${teacher.id}" class="btn btn-warning btn-sm">Sửa</a>
                    <a href="teachers?action=delete&id=${teacher.id}" class="btn btn-danger btn-sm"
                       onclick="return confirm('Xác nhận xóa giáo viên này? Lưu ý: Không thể xóa nếu giáo viên đang được phân công lớp học.')">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <nav>
        <ul class="pagination">
            <c:forEach begin="1" end="${endP}" var="i">
                <li class="page-item ${i==currentIndex?'active':''}">
                    <a class="page-link" href="teachers?index=${i}&search=${param.search}">
                            ${i}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </nav>
    </div>
</div>
<jsp:include page="footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>