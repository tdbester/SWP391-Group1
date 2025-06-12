<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Blog List</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <%--    <link href="${pageContext.request.contextPath}/assets/css/profile.css" rel="stylesheet"/>--%>
    <style>
        .thumb-img {
            max-width: 80px;
            max-height: 60px;
            object-fit: cover;
            border-radius: 4px;
        }

        btn-primary, .btn-success, .btn-warning, .btn-add-custom {
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
    </style>

</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container mt-5">
    <h2 class="mb-4">Danh sách bài viết</h2>
    <div class="row justify-content-between ms-1">
        <a href="blogs?action=new" class="btn btn-success mb-3 col-3">Tạo bài viết mới</a>
        <form action="blogs" method="get" class="col-4 mb-3">
            <input type="search" name="search" class="form-control me-2" placeholder="Tìm kiếm">
        </form>
    </div>
    <table class="table table-striped table-bordered align-middle">
        <thead class="table-dark">
        <tr>
            <th>STT</th>
            <th>Tiêu đề</th>
            <th>Mô tả</th>
<%--            <th>Nội dung</th>--%>
            <th>Ảnh</th>
            <th>Tác giả</th>
            <th style="width: 150px;">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="blog" items="${blogList}" varStatus="status">
            <tr>
                <td>${status.index+1}</td>
                <td>${blog.title}</td>
                <td>${blog.description}</td>
<%--                <td>${blog.content}</td>--%>
                <td>
                    <c:if test="${not empty blog.image}">
                        <img src="${blog.image}" alt="Image" class="thumb-img"/>
                    </c:if>
                    <c:if test="${empty blog.image}">
                        <span class="text-muted">No Image</span>
                    </c:if>
                </td>
                <td>${blog.fullname}</td>
                <td>
                    <a href="blogs?action=edit&id=${blog.id}" class="btn btn-sm btn-warning">Sửa</a>
                    <a href="blogs?action=delete&id=${blog.id}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('Are you sure you want to delete this blog?');">
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
                <li class="page-item"><a class="page-link" href="blogs?index=${i}">${i}</a></li>
            </c:forEach>
        </ul>
    </nav>
</div>
<jsp:include page="footer.jsp"/>

<!-- Bootstrap JS Bundle (includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
