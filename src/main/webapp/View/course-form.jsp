
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.example.talentcenter.utilities.CourseCategory" %> <%-- Import your enum here --%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>
        <c:choose>
            <c:when test="${not empty course}">Chỉnh sửa khóa học</c:when>
            <c:otherwise>Khóa học mới</c:otherwise>
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
<jsp:include page="header.jsp"/> <%-- Đảm bảo bạn có file header.jsp --%>

<div class="container mt-5">
    <h2 class="mb-4">
        <c:choose>
            <c:when test="${not empty course}">Chỉnh sửa khóa học</c:when>
            <c:otherwise>Khóa học mới</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger mt-3" role="alert">
                ${errorMessage}
        </div>
    </c:if>

    <form action="courses" method="post" onsubmit="handleSubmit()" enctype="multipart/form-data">
        <%-- Chỉ cần một trường action duy nhất --%>
        <input type="hidden" name="action" value="${not empty course ? 'update' : 'insert'}"/>
        <div class="row">
            <div class="col-12 col-md-8">
                <c:if test="${not empty course}">
                    <input type="hidden" name="id" value="${course.id}"/>
                    <input type="hidden" name="currentImageUrl" value="${course.image}"/>
                </c:if>

                <div class="mb-3">
                    <label class="form-label">Tiêu đề</label>
                    <input type="text" class="form-control" name="title" value="${course.title}" required/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Giá</label>
                    <input type="number" step="0.01" class="form-control" name="price" value="${course.price}" required/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Thông tin</label>
                    <div id="editor-information" style="height: 150px;"></div>
                    <input type="hidden" name="information" id="information"/>
                </div>

                <div class="mb-3">
                    <label for="createdBy" class="form-label">Người tạo (ID)</label>
                    <%-- Trong một ứng dụng thực tế, giá trị này nên được lấy từ session của người dùng đã đăng nhập --%>
                    <input id="createdBy" name="createdBy" type="number" class="form-control" value="${course.createdBy}" required/>
                </div>
            </div>
            <div class="col-12 col-md-4">
                <div class="mb-3">
                    <img id="imagePreview"
                         src="${not empty course.image ? course.image : 'https://placehold.co/600x400?text=select-image'}"
                         alt="Image Preview" class="image-preview img-fluid max-w-40"/>
                </div>
                <div class="mb-3">
                    <label for="imageFile" class="form-label">Chọn ảnh bìa:</label>
                    <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*" onchange="previewImage(event)"/>
                </div>
                <div class="mb-3">
                    <label for="category" class="form-label">Danh mục:</label>
                    <select id="category" name="category" class="form-select" required>
                        <option value="">--Chọn danh mục--</option>
                        <c:forEach var="cat" items="${categories}"> <%-- 'categories' được truyền từ CourseServlet --%>
                            <option value="${cat.name()}" <c:if test="${course != null && course.category == cat}">selected</c:if>>
                                    ${cat.name()}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <button type="submit" class="btn btn-success">${not empty course ? 'Cập nhật' : 'Tạo khóa học'}</button>
        <a href="courses" class="btn btn-secondary ms-2">Hủy</a>
    </form>
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

    const editorInformation = new Quill('#editor-information', {
        theme: 'snow',
        modules: {
            toolbar: toolbarOptions
        },
        placeholder: 'Nhập thông tin khóa học...'
    });

    const courseInformation = `<c:out value='${course.information}' default=''/>`;
    editorInformation.root.innerHTML = courseInformation;

    function handleSubmit() {
        // Lấy nội dung từ Quill editor và gán vào các trường input ẩn trước khi submit
        document.getElementById('information').value = editorInformation.root.innerHTML;
    }

    function previewImage(event) {
        if (event.target.files && event.target.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const preview = document.getElementById('imagePreview');
                preview.src = e.target.result;
            };
            reader.readAsDataURL(event.target.files[0]);
        }
    }
</script>
<jsp:include page="footer.jsp"/> <%-- Đảm bảo bạn có file footer.jsp --%>
</body>
</html>
