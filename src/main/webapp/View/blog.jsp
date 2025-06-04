<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Blog List</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <style>
        .thumb-img {
            max-width: 80px;
            max-height: 60px;
            object-fit: cover;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">Blog List</h2>
    <a href="blogs?action=new" class="btn btn-primary mb-3">Create New Blog</a>
    <table class="table table-striped table-bordered align-middle">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Description</th>
            <th>Content</th>
            <th>Image</th>
            <th>Created By</th>
            <th style="width: 150px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="blog" items="${blogList}">
            <tr>
                <td>${blog.id}</td>
                <td>${blog.title}</td>
                <td>${blog.description}</td>
                <td>${blog.content}</td>
                <td>
                    <c:if test="${not empty blog.image}">
                        <img src="${blog.image}" alt="Image" class="thumb-img" />
                    </c:if>
                    <c:if test="${empty blog.image}">
                        <span class="text-muted">No Image</span>
                    </c:if>
                </td>
                <td>${blog.fullname}</td>
                <td>
                    <a href="blogs?action=edit&id=${blog.id}" class="btn btn-sm btn-warning">Edit</a>
                    <a href="blogs?action=delete&id=${blog.id}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('Are you sure you want to delete this blog?');">
                        Delete
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Bootstrap JS Bundle (includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
