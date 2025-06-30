<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title><c:out value="${course.title}"/></title>

    <!-- Bootstrap & Google Fonts -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Lora:ital,wght@0,400..700;1,400..700&family=Poppins:wght@400;500;700&display=swap"
          rel="stylesheet"/>

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: #f8f9fa
        }

        .course-container {
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, .08);
            padding: 30px 40px;
            margin: 20px 0 40px
        }

        .course-image {
            width: 100%;
            height: auto;
            max-height: 450px;
            object-fit: cover;
            border-radius: 8px;
            margin-bottom: 25px
        }

        .course-title {
            font-family: 'Lora', serif;
            font-weight: 700;
            font-size: 2.5rem;
            color: #2c3e50;
            margin-bottom: 15px
        }

        .course-meta {
            font-size: .95rem;
            color: #7f8c8d;
            margin-bottom: 25px
        }

        .course-meta span {
            margin-right: 20px
        }

        .course-meta strong {
            color: #34495e
        }

        .course-info {
            font-family: 'Lora', serif;
            font-size: 1.1rem;
            line-height: 1.75;
            color: #34495e
        }

        .course-info p {
            margin-bottom: 1.25rem
        }

        .btn-back {
            background: #7a6ad8;
            border-color: #7a6ad8;
            color: #fff
        }

        .btn-back:hover {
            background: #6a5ac8;
            border-color: #6a5ac8
        }
    </style>
</head>
<body>

<!-- header chung -->
<jsp:include page="header.jsp"/>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">

            <c:if test="${not empty course}">
                <article class="course-container">

                    <!-- Ảnh khóa học -->
                    <c:if test="${not empty course.image}">
                        <img src="${course.image}" alt="Ảnh bìa khóa học ${course.title}" class="course-image"/>
                    </c:if>

                    <!-- Tiêu đề -->
                    <h1 class="course-title">
                        <c:out value="${course.title}" escapeXml="false"/>
                    </h1>

                    <!-- Thông tin meta: danh mục, giá, ngày tạo -->
                    <div class="course-meta">
            <span>
              <strong>Danh mục:</strong>
              <c:out value="${course.category.name}"/>
            </span>
                        <span>
              <strong>Học phí:</strong>
              <fmt:formatNumber value="${course.price}" type="currency" currencySymbol="₫" groupingUsed="true"/>
            </span>
                    </div>

                    <hr class="my-4"/>

                    <!-- Nội dung mô tả / thông tin khóa học -->
                    <div class="course-info">
                        <c:out value="${course.information}" escapeXml="false"/>
                    </div>

                </article>

                <!-- Nút quay lại -->
                <div class="text-center mb-5">
                    <a href="courses" class="btn btn-back">&larr; Quay lại danh sách khóa học</a>
                </div>
            </c:if>

            <!-- Nếu không tìm thấy khóa học -->
            <c:if test="${empty course}">
                <div class="alert alert-warning text-center" role="alert">
                    Không tìm thấy khóa học này hoặc đã bị xóa.
                </div>
                <div class="text-center mb-5">
                    <a href="courses" class="btn btn-secondary">&larr; Quay lại danh sách</a>
                </div>
            </c:if>

        </div>
    </div>
</div>

<!-- footer chung -->
<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
