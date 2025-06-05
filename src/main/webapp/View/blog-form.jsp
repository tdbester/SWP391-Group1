<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>
        <c:choose>
            <c:when test="${not empty blog}">Chỉnh sửa bài viết</c:when>
            <c:otherwise>Bài viết mới</c:otherwise>
        </c:choose>
    </title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.snow.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.js"></script>
</head>
<body>

<div class="container mt-5" style="max-width: 600px;">
    <h2 class="mb-4">
        <c:choose>
            <c:when test="${not empty blog}">Chỉnh sửa bài viết</c:when>
            <c:otherwise>Bài viết mới</c:otherwise>
        </c:choose>
    </h2>

    <form action="blogs" method="post" onsubmit="handleSubmit()" enctype="multipart/form-data">
        <%-- Chỉ cần một trường action duy nhất --%>
        <input type="hidden" name="action" value="${not empty blog ? 'update' : 'insert'}"/>

        <c:if test="${not empty blog}">
            <input type="hidden" name="id" value="${blog.id}"/>
            <input type="hidden" name="currentImageUrl" value="${blog.image}"/>
        </c:if>

        <div class="mb-3">
            <label class="form-label">Tiêu đề</label>
            <div id="editor-title" style="height: 30px;"></div>
            <input type="hidden" name="title" id="title"/>
        </div>

        <div class="mb-3">
            <label class="form-label">Mô tả</label>
            <div id="editor-description" style="height: 75px;"></div>
            <input type="hidden" name="description" id="description"/>
        </div>

        <div class="mb-3">
            <label class="form-label">Nội dung</label>
            <div id="editor-content" style="height: 150px;"></div>
            <input type="hidden" name="content" id="content"/>
        </div>

        <div class="mb-3">
            <label for="imageFile" class="form-label">Chọn ảnh:</label>
            <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*" onchange="previewImage(event)"/>

        </div>

        <div class="mb-3">
            <label for="authorId" class="form-label">Tạo bởi</label>
            <input id="authorId" name="authorId" type="number" class="form-control" value="${blog.authorId}" required/>
        </div>

        <button type="submit" class="btn btn-success">${not empty blog ? 'Sửa' : 'Tạo'}</button>
        <a href="blogs" class="btn btn-secondary ms-2">Hủy</a>
    </form>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger mt-3" role="alert">
                ${errorMessage}
        </div>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const toolbarOptions = [
        [{ 'header': [1, 2, false] }],
        ['bold', 'italic', 'underline'],
        ['link'],
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        ['clean']
    ];

    const editorTitle = new Quill('#editor-title', {
        theme: 'snow',
        modules: {
            toolbar: toolbarOptions
        },
        placeholder: 'Nhập tiêu đề bài viết...'
    });

    const editorDescription = new Quill('#editor-description', {
        theme: 'snow',
        modules: {
            toolbar: toolbarOptions
        },
        placeholder: 'Nhập mô tả...'
    });

    const editorContent = new Quill('#editor-content', {
        theme: 'snow',
        modules: {
            toolbar: toolbarOptions
        },
        placeholder: 'Nhập nội dung bài viết...'
    });

    const blogTitle = '${blog.title != null ? blog.title : ""}';
    const blogDescription = '${blog.description != null ? blog.description : ""}';
    const blogContent = '${blog.content != null ? blog.content : ""}';

    editorTitle.root.innerHTML = blogTitle;
    editorDescription.root.innerHTML = blogDescription;
    editorContent.root.innerHTML = blogContent;

    function handleSubmit() {
        document.getElementById('title').value = editorTitle.root.innerHTML;
        document.getElementById('description').value = editorDescription.root.innerHTML;
        document.getElementById('content').value = editorContent.root.innerHTML;
    }
</script>
</body>
</html>

