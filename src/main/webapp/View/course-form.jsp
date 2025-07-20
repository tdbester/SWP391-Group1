<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <title>
        <c:choose>
            <c:when test="${not empty course}">Chỉnh sửa khóa học</c:when>
            <c:otherwise>Khóa học mới</c:otherwise>
        </c:choose>
    </title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.snow.css" rel="stylesheet">
    <style>
        /* CSS cho ảnh preview */
        .image-preview {
            margin-top: 15px;
            max-width: 300px;
            /* Giới hạn chiều rộng tối đa của ảnh */
            max-height: 200px;
            /* Giới hạn chiều cao tối đa */
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 5px;
            object-fit: cover;
            /* Đảm bảo ảnh không bị méo */
        }
    </style>
</head>

<body>
<jsp:include page="header.jsp" />
<div class="container mt-5">
    <h2>
        <c:choose>
            <c:when test="${not empty course}">Chỉnh sửa khóa học</c:when>
            <c:otherwise>Khóa học mới</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <form action="courses" method="post" enctype="multipart/form-data" onsubmit="handleSubmit()">
        <input type="hidden" name="action" value="${course!=null?'update':'insert'}" />
        <c:if test="${course!=null}">
            <input type="hidden" name="id" value="${course.id}" />
            <input type="hidden" name="currentImageUrl" value="${course.image}" />
        </c:if>

        <div class="row">
            <div class="col-md-8">
                <div class="mb-3">
                    <label class="form-label">Tiêu đề</label>
                    <input type="text" name="title" class="form-control" value="${course.title}" required />
                </div>
                <div class="mb-3">
                    <label class="form-label">Giá (VNĐ)</label>
                    <input type="number" step="1" name="price" class="form-control"
                           value="<fmt:formatNumber value='${course.price}' groupingUsed='false' pattern='#'/>" required
                           min="0" placeholder="Nhập giá khóa học" />
                </div>
                <div class="mb-3">
                    <label class="form-label">Mô Tả</label>
                    <div id="editor-information" style="height:150px;"></div>
                    <input type="hidden" name="information" id="information" />
                </div>
            </div>

            <div class="col-md-4">
                <div class="mb-3">
                    <img id="imagePreview"
                         src="${course.image!=null?course.image:'https://placehold.co/600x400?text=Chọn+ảnh'}"
                         class="image-preview" />
                </div>
                <div class="mb-3">
                    <label class="form-label">Ảnh bìa</label>
                    <input type="file" name="imageFile" class="form-control" accept="image/*"
                           onchange="previewImage(event)" />
                </div>
                <div class="mb-3">
                    <label class="form-label">Danh mục</label>
                    <select name="category" class="form-select" required>
                        <option value="">--Chọn danh mục--</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}" <c:if test="${course!=null && course.category.id==cat.id}">selected
                            </c:if>>
                                    ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Cấp độ</label>
                    <select name="level" class="form-select" required>
                        <option value="">--Chọn cấp độ--</option>
                        <option value="BEGINNER" <c:if test="${course!=null && course.level=='BEGINNER'}">selected</c:if>>Cơ
                            bản</option>
                        <option value="INTERMEDIATE" <c:if test="${course!=null && course.level=='INTERMEDIATE'}">selected
                        </c:if>>Trung cấp</option>
                        <option value="ADVANCED" <c:if test="${course!=null && course.level=='ADVANCED'}">selected</c:if>
                        >Nâng
                            cao</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Loại khóa học</label>
                    <select name="type" class="form-select" required>
                        <option value="">--Chọn loại--</option>
                        <option value="COMBO" <c:if test="${course!=null && course.type=='COMBO'}">selected</c:if>>Combo
                        </option>
                        <option value="LESSON" <c:if test="${course!=null && course.type=='LESSON'}">selected</c:if>>Theo
                            buổi
                        </option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Trạng thái</label>
                    <div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="status" id="statusPublic" value="1"
                                   <c:if test="${course == null || course.status == 1}">checked</c:if>>
                            <label class="form-check-label" for="statusPublic">Công khai</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="status" id="statusHidden" value="0"
                                   <c:if test="${course != null && course.status == 0}">checked</c:if>>
                            <label class="form-check-label" for="statusHidden">Ẩn</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <button class="btn btn-success">${course!=null?'Cập nhật':'Tạo mới'}</button>
        <a href="courses" class="btn btn-secondary ms-2">Hủy</a>
    </form>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.js"></script>

<!-- Hidden div to store course information for JavaScript -->
<div id="courseInformation" style="display: none;">
    <c:if test="${not empty course && not empty course.information}">
        ${course.information}
    </c:if>
</div>

<script>
    // Initialize Quill editor
    let editor;

    document.addEventListener('DOMContentLoaded', function () {
        // Initialize Quill editor with proper configuration
        editor = new Quill('#editor-information', {
            theme: 'snow',
            modules: {
                toolbar: [
                    [{ header: [1, 2, false] }],
                    ['bold', 'italic', 'underline'],
                    ['link'],
                    [{ list: 'ordered' }, { list: 'bullet' }],
                    ['clean']
                ]
            }
        });

        // Load existing content if editing
        const courseInfoDiv = document.getElementById('courseInformation');
        if (courseInfoDiv && courseInfoDiv.innerHTML.trim()) {
            editor.root.innerHTML = courseInfoDiv.innerHTML;
        }
    });

    function handleSubmit() {
        // Get the HTML content from Quill editor
        document.getElementById('information').value = editor.root.innerHTML;
        return true;
    }

    function previewImage(e) {
        if (e.target.files[0]) {
            const reader = new FileReader();
            reader.onload = evt => document.getElementById('imagePreview').src = evt.target.result;
            reader.readAsDataURL(e.target.files[0]);
        }
    }
</script>

<jsp:include page="footer.jsp" />
</body>

</html>