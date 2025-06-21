<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<%-- Đảm bảo bạn có file header.jsp --%>

<div class="container mt-5">
    <h2 class="mb-4">Danh sách khóa học</h2>
    <div class="row justify-content-between ms-1">
        <a href="courses?action=new" class="btn btn-success mb-3 col-3">Tạo khóa học mới</a>
        <form action="courses" method="get" class="col-6 mb-3 d-flex justify-content-end">
            <input type="search"
                   name="search"
                   class="form-control me-2"
                   placeholder="Tìm kiếm theo tiêu đề"
                   value="${param.search}">
            <select name="category" class="form-select me-2" style="width: 200px;">
                <option value="">Tất cả danh mục</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat}" ${param.category == cat ? 'selected' : ''}> ${cat} </option>
                </c:forEach> </select>
            <button type="submit" class="btn btn-primary">Lọc</button>
        </form>

    </div>
    <table class="table table-striped table-bordered align-middle">
        <thead class="table-dark">
        <tr>
            <th>STT</th>
            <th>Tiêu đề</th>
            <th>Giá</th>
            <th>Danh mục</th>
            <th>Ảnh</th>
            <th>Người tạo</th>
            <th style="width: 150px;">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="course" items="${courseList}" varStatus="status">
            <tr>
                <td>${(currentIndex - 1) * 5 + status.index + 1}</td>
                    <%-- Cập nhật STT cho phân trang --%>
                <td>${course.title}</td>
                <td>${course.price}</td>
                <td>${course.category}</td>
                <td>
                    <c:if test="${not empty course.image}">
                        <img src="${course.image}" alt="Course Image" class="thumb-img"/>
                    </c:if>
                    <c:if test="${empty course.image}">
                        <span class="text-muted">No Image</span>
                    </c:if>
                </td>
                <td>${course.fullname}</td>
                <td>
                    <a href="courses?action=edit&id=${course.id}" class="btn btn-sm btn-warning">Sửa</a>
                    <a href="courses?action=delete&id=${course.id}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('Bạn có chắc chắn muốn xóa khóa học này không?');">
                        Xóa
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <nav aria-label="Page navigation example">
        <ul class="pagination">
            <c:forEach begin="1" end="${endP}" var="i">
                <li class="page-item ${i == currentIndex ? 'active' : ''}"> <%-- Thêm class 'active' cho trang hiện tại --%>
                    <a class="page-link" href="courses?index=${i}">${i}</a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</div>

<jsp:include page="footer.jsp"/>
<%-- Đảm bảo bạn có file footer.jsp --%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
