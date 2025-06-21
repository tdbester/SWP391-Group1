<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>… Bootstrap + Quill + styles …</head>
<body>
<jsp:include page="header.jsp"/>
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
        <input type="hidden" name="action" value="${course!=null?'update':'insert'}"/>
        <c:if test="${course!=null}">
            <input type="hidden" name="id"               value="${course.id}"/>
            <input type="hidden" name="currentImageUrl"  value="${course.image}"/>
        </c:if>

        <div class="row">
            <div class="col-md-8">
                <div class="mb-3">
                    <label class="form-label">Tiêu đề</label>
                    <input type="text" name="title" class="form-control"
                           value="${course.title}" required/>
                </div>
                <div class="mb-3">
                    <label class="form-label">Giá</label>
                    <input type="number" step="0.01" name="price"
                           class="form-control" value="${course.price}" required/>
                </div>
                <div class="mb-3">
                    <label class="form-label">Thông tin</label>
                    <div id="editor-information" style="height:150px;"></div>
                    <input type="hidden" name="information" id="information"/>
                </div>
            </div>

            <div class="col-md-4">
                <div class="mb-3">
                    <img id="imagePreview"
                         src="${course.image!=null?course.image:'https://placehold.co/600x400?text=Chọn+ảnh'}"
                         class="image-preview"/>
                </div>
                <div class="mb-3">
                    <label class="form-label">Ảnh bìa</label>
                    <input type="file" name="imageFile" class="form-control"
                           accept="image/*" onchange="previewImage(event)"/>
                </div>
                <div class="mb-3">
                    <label class="form-label">Danh mục</label>
                    <select name="category" class="form-select" required>
                        <option value="">--Chọn danh mục--</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}"
                                    <c:if test="${course!=null && course.category.id==cat.id}">selected</c:if>>
                                    ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <button class="btn btn-success">${course!=null?'Cập nhật':'Tạo mới'}</button>
        <a href="courses" class="btn btn-secondary ms-2">Hủy</a>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.js"></script>
<script>
    const editor = new Quill('#editor-information', { theme: 'snow', modules:{ toolbar: [
                [{ header: [1,2,false] }], ['bold','italic','underline'], ['link'],
                [{ list:'ordered'},{ list:'bullet'}], ['clean']
            ]}});
    editor.root.innerHTML = `<c:out value='${course.information}'/>`;

    function handleSubmit() {
        document.getElementById('information').value = editor.root.innerHTML;
    }
    function previewImage(e) {
        if (e.target.files[0]) {
            const reader = new FileReader();
            reader.onload = evt => document.getElementById('imagePreview').src = evt.target.result;
            reader.readAsDataURL(e.target.files[0]);
        }
    }
</script>
<jsp:include page="footer.jsp"/>
</body>
</html>
