<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title><c:out value="${blog.title}"/></title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Lora:ital,wght@0,400..700;1,400..700&family=Poppins:wght@400;500;700&display=swap" rel="stylesheet">

  <style>
    body {
      font-family: 'Poppins', sans-serif;
      background-color: #f8f9fa;
    }
    .post-container {
      background-color: #ffffff;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.08);
      padding: 30px 40px;
      margin-top: 20px;
      margin-bottom: 40px;
    }
    .post-image {
      width: 100%;
      height: auto;
      max-height: 450px;
      object-fit: cover;
      border-radius: 8px;
      margin-bottom: 25px;
    }
    .post-title {
      font-family: 'Lora', serif;
      font-weight: 700;
      font-size: 2.5rem; /* 40px */
      margin-bottom: 15px;
      color: #2c3e50;
    }
    .post-meta {
      font-size: 0.9rem; /* 14.4px */
      color: #7f8c8d;
      margin-bottom: 30px;
    }
    .post-meta strong {
      color: #34495e;
    }
    .post-content {
      font-family: 'Lora', serif;
      font-size: 1.1rem; /* 17.6px */
      line-height: 1.8;
      color: #34495e;
    }
    .post-content p {
      margin-bottom: 1.25rem;
    }
    .btn-back {
      background-color: #7a6ad8;
      border-color: #7a6ad8;
      color: white;
    }
    .btn-back:hover {
      background-color: #6a5ac8;
      border-color: #6a5ac8;
    }
  </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-lg-8">

      <c:if test="${not empty blog}">
        <article class="post-container">
          <c:if test="${not empty blog.image}">
            <img src="${blog.image}" class="post-image" alt="Ảnh bìa cho bài viết ${blog.title}">
          </c:if>

          <h1 class="post-title">
            <c:out value="${blog.title}" escapeXml="false"/>
          </h1>

          <div class="post-meta">
<%--            Đăng bởi <strong><c:out value="${blog.fullname}"/></strong>--%>

            <c:if test="${not empty blog.createdAt}">
              <fmt:formatDate value="${blog.createdAt}" pattern="dd/MM/yyyy"/>
            </c:if>
          </div>

          <hr class="my-4">

          <div class="post-content">
            <c:out value="${blog.content}" escapeXml="false"/>
          </div>

        </article>

        <div class="text-center mb-5">
          <a href="blogs" class="btn btn-back">&larr; Quay lại danh sách bài viết</a>
        </div>
      </c:if>

      <c:if test="${empty blog}">
        <div class="alert alert-warning text-center" role="alert">
          Không tìm thấy bài viết này hoặc đã bị xóa.
        </div>
        <div class="text-center mb-5">
          <a href="blogs" class="btn btn-secondary">&larr; Quay lại danh sách</a>
        </div>
      </c:if>

    </div>
  </div>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>

