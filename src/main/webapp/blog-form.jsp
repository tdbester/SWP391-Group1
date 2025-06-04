<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title><c:choose><c:when
            test="${not empty blog}">Edit Blog</c:when><c:otherwise>New Blog</c:otherwise></c:choose></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<div class="container mt-5" style="max-width: 600px;">
    <h2 class="mb-4"><c:choose><c:when
            test="${not empty blog}">Edit Blog</c:when><c:otherwise>New Blog</c:otherwise></c:choose></h2>

    <form action="blogs" method="post">
        <input type="hidden" name="action" value="${not empty blog ? 'update' : 'insert'}"/>
        <c:if test="${not empty blog}">
            <input type="hidden" name="id" value="${blog.id}"/>
        </c:if>

        <div class="mb-3">
            <label for="title" class="form-label">Title</label>
            <input id="title" name="title" type="text" class="form-control" value="${blog.title}" required/>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea id="description" name="description" rows="3" class="form-control" required>${blog.description}</textarea>
        </div>
        <div class="mb-3">
            <label for="content" class="form-label">Content</label>
            <textarea id="content" name="content" rows="5" class="form-control" required>${blog.content}</textarea>
        </div>

        <div class="mb-3">
            <label for="image" class="form-label">Image URL</label>
            <input type="text" id="image" name="image" class="form-control" value="${blog.image}" />
        </div>

        <div class="mb-3">
            <label for="authorId" class="form-label">Created By (User ID)</label>
            <input id="authorId" name="authorId" type="number" class="form-control" value="${blog.authorId}"
                   required/>
        </div>

        <button type="submit" class="btn btn-success">${not empty blog ? 'Update' : 'Create'}</button>
        <a href="blogs" class="btn btn-secondary ms-2">Cancel</a>
    </form>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
