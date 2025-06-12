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
    <style>
        /* CSS cho ảnh preview */
        .image-preview {
            margin-top: 15px;
            max-width: 300px; /* Giới hạn chiều rộng tối đa của ảnh */
            max-height: 200px; /* Giới hạn chiều cao tối đa */
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 5px;
            object-fit: cover; /* Đảm bảo ảnh không bị méo */
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

    <form action="blogs" method="post" onsubmit="handleSubmit()" enctype="multipart/form-data">
        <%-- Chỉ cần một trường action duy nhất --%>
        <input type="hidden" name="action" value="${not empty blog ? 'update' : 'insert'}"/>
        <div class="row">
            <div class="col-12 col-md-8">
                <c:if test="${not empty blog}">
                    <input type="hidden" name="id" value="${blog.id}"/>
                    <input type="hidden" name="currentImageUrl" value="${blog.image}"/>
                </c:if>

                <div class="mb-3">
                    <label class="form-label">Tiêu đề</label>
                    <input type="text" class="form-control" name="title" value="${blog.title}" required/>
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
                    <label for="authorId" class="form-label">Tạo bởi</label>
                    <input id="authorId" name="authorId" type="number" class="form-control" value="${blog.authorId}" required/>
                </div>
            </div>
            <div class="col-12 col-md-4">
                <div class="mb-3">
                    <img id="imagePreview"
                         src="${not empty blog.image ? blog.image : 'https://placehold.co/600x400?text=select-image'}"
                         alt="Image Preview" class="image-preview img-fluid max-w-40"/>
                </div>
                <div class="mb-3">
                    <label for="imageFile" class="form-label">Chọn ảnh bìa:</label>
                    <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*" onchange="previewImage(event)"/>
                </div>
            </div>
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
        [{'header': [1, 2, false]}],
        ['bold', 'italic', 'underline'],
        ['link'],
        [{'list': 'ordered'}, {'list': 'bullet'}],
        ['clean']
    ];

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

    const blogDescription = `<c:out value='${blog.description}' default=''/>`;
    const blogContent = `<c:out value='${blog.content}' default=''/>`;

    editorDescription.root.innerHTML = blogDescription;
    editorContent.root.innerHTML = blogContent;

    function handleSubmit() {
        // Lấy nội dung từ Quill editor và gán vào các trường input ẩn trước khi submit
        document.getElementById('description').value = editorDescription.root.innerHTML;
        document.getElementById('content').value = editorContent.root.innerHTML;
    }

    function previewImage(event) {
        // Kiểm tra xem người dùng có chọn file không
        if (event.target.files && event.target.files[0]) {
            const reader = new FileReader();

            reader.onload = function(e) {
                // Lấy thẻ img và cập nhật thuộc tính src của nó
                const preview = document.getElementById('imagePreview');
                preview.src = e.target.result;
            };

            // Đọc file ảnh dưới dạng Data URL
            reader.readAsDataURL(event.target.files[0]);
        }
    }
</script>
<jsp:include page="footer.jsp"/>
</body>
</html>

