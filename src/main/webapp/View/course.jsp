<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Course List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        .thumb-img {
            max-width: 80px;
            max-height: 60px;
            object-fit: cover;
            border-radius: 4px;
        }

        /* Dành cho các nút tùy chỉnh, nếu bạn muốn style riêng như blog.jsp */
        .btn-primary, .btn-success, .btn-warning, .btn-add-custom {
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
            background-color: #7a6ad8; /* Màu nền cho trang hiện tại */
            border-color: #7a6ad8;
            color: white;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container mt-5">
    <h2 class="mb-4">Danh sách khóa học</h2>
    <form action="courses" method="get" class="row g-2 mb-3">
        <div class="col-auto">
            <input type="search" name="search" class="form-control"
                   placeholder="Tìm kiếm tiêu đề" value="${param.search}"/>
        </div>
        <div class="col-auto">
            <select name="category" class="form-select">
                <option value="">Tất cả danh mục</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat.id}"
                        ${param.category == cat.id.toString() ? 'selected' : ''}>
                            ${cat.name}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="col-auto">
            <button class="btn btn-primary">Lọc</button>
        </div>
        <div class="col-auto ms-auto">
            <a href="courses?action=new" class="btn btn-success">Tạo mới</a>
        </div>
    </form>

    <table class="table table-striped">
        <thead class="table-dark">
        <tr>
            <th>STT</th>
            <th>Tiêu đề</th>
            <th>Giá</th>
            <th>Danh mục</th>
            <th>Ảnh</th>
            <th style="width: 150px;">Hành động</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="course" items="${courseList}" varStatus="st">
            <tr>
                <td>${(currentIndex-1)*5 + st.index + 1}</td>
                <td>${course.title}</td>
                <td><fmt:formatNumber value="${course.price}" type="number" groupingUsed="true"/> VNĐ</td>                <td>${course.category.name}</td>
                <td>
                    <c:if test="${not empty course.image}">
                        <img src="${course.image}" class="thumb-img"/>
                    </c:if>
                </td>
                <td>
                    <a href="courses?action=edit&id=${course.id}" class="btn btn-warning btn-sm">Sửa</a>
                    <a href="courses?action=delete&id=${course.id}"
                       class="btn btn-danger btn-sm"
                       onclick="return confirm('Xác nhận xóa?')">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <nav>
        <ul class="pagination">
            <c:forEach begin="1" end="${endP}" var="i">
                <li class="page-item ${i==currentIndex?'active':''}">
                    <a class="page-link" href="courses?index=${i}&category=${param.category}&search=${param.search}">
                            ${i}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</div>
<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
