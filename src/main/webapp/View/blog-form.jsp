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
    <link href="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.snow.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.js"></script>
    <style>
        .image-preview {
            margin-top: 15px;
            max-width: 300px;
            max-height: 200px;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 5px;
            object-fit: cover;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container mt-5">
    <h2 class="mb-4">
        <c:choose>
            <c:when test="${not empty blog}">Chỉnh sửa bài viết</c:when>
            <c:otherwise>Bài viết mới</c:otherwise>
        </c:choose>
    </h2>

    <form action="blogs" method="post" enctype="multipart/form-data" onsubmit="prepareSubmit()">
        <input type="hidden" name="action" value="${not empty blog ? 'update' : 'insert'}"/>
        <c:if test="${not empty blog}">
            <input type="hidden" name="id" value="${blog.id}"/>
            <input type="hidden" name="currentImageUrl" value="${blog.image}"/>
        </c:if>

        <div class="row">
            <div class="col-md-8">
                <div class="mb-3">
                    <label class="form-label">Tiêu đề</label>
                    <input type="text" name="title" class="form-control"
                           value="${blog.title}" required/>
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
                    <label class="form-label">Thể loại</label>
                    <select name="category" class="form-select" required>
                        <option value="">-- Chọn loại --</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}"
                                    <c:if test="${blog.category == cat.id}">selected</c:if>>
                                    ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="col-md-4">
                <div class="mb-3">
                    <img id="imagePreview"
                         src="${not empty blog.image ? blog.image : 'https://placehold.co/600x400?text=Chọn+ảnh'}"
                         alt="Preview" class="image-preview"/>
                </div>
                <div class="mb-3">
                    <label class="form-label">Chọn ảnh bìa</label>
                    <input type="file" name="imageFile" class="form-control" accept="image/*"
                           onchange="previewImage(event)"/>
                </div>
            </div>
        </div>

        <button type="submit" class="btn btn-success">
            <c:choose>
                <c:when test="${not empty blog}">Cập nhật</c:when>
                <c:otherwise>Tạo mới</c:otherwise>
            </c:choose>
        </button>
        <a href="blogs" class="btn btn-secondary ms-2">Hủy</a>
    </form>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger mt-3">${errorMessage}</div>
    </c:if>
</div>

<script>
    const toolbarOptions = [
        [{ header: [1, 2, false] }],
        ['bold','italic','underline'],
        ['link', 'image'],
        [{ list: 'ordered'}, { list: 'bullet' }],
        ['clean']
    ];
    const editorDesc = new Quill('#editor-description', { theme: 'snow', modules: { toolbar: toolbarOptions } });
    const editorCont = new Quill('#editor-content', { theme: 'snow',
        modules: {
        toolbar: toolbarOptions } });

    // Load existing content
    editorDesc.root.innerHTML = `<c:out value='${blog.description}' default=''/>`;
    editorCont.root.innerHTML = `<c:out value='${blog.content}' default=''/>`;

    function prepareSubmit() {
        document.getElementById('description').value = editorDesc.root.innerHTML;
        document.getElementById('content').value     = editorCont.root.innerHTML;
    }

    function previewImage(evt) {
        if (evt.target.files && evt.target.files[0]) {
            const reader = new FileReader();
            reader.onload = e => document.getElementById('imagePreview').src = e.target.result;
            reader.readAsDataURL(evt.target.files[0]);
        }
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
