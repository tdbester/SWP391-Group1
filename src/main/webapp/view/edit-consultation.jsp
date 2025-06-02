<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 5/29/2025
  Time: 4:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <!-- Bootstrap core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/templatemo-scholar.css">
    <link rel="stylesheet" href="assets/css/fontawesome.css">
    <link rel="stylesheet" href="assets/css/owl.css">
    <link rel="stylesheet" href="assets/css/animate.css">
    <link rel="stylesheet" href="https://unpkg.com/swiper@7/swiper-bundle.min.css"/>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap"
          rel="stylesheet">
    <title>Chỉnh sửa học sinh tư vấn</title>
</head>
<body>

<div class="container mt-5">
    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>
    <h2 class="mb-4">Chỉnh sửa học sinh tư vấn</h2>
    <form method="post" action="Consultation" class="row g-3">
        <input type="hidden" name="id" value="${consult.id}"/>

        <div class="col-md-6">
            <label class="form-label">Họ tên</label>
            <input type="text" name="name" value="${consult.fullName}" required class="form-control"/>
        </div>

        <div class="col-md-6">
            <label class="form-label">Email</label>
            <input type="email" name="email" value="${consult.email}" required class="form-control"/>
        </div>

        <div class="col-md-6">
            <label class="form-label">Số điện thoại</label>
            <input type="text" name="phone" value="${consult.phone}" required class="form-control"/>
        </div>

        <div class="col-md-6">
            <label class="form-label">Khóa học quan tâm</label>
            <select name="course_interest" required class="form-select">
                <c:forEach var="subject" items="${subjects}">
                    <option value="${subject.id}" ${consult.courseId == subject.id ? 'selected' : ''}>
                            ${subject.title}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="col-12">
            <button type="submit" name="action" value="update" class="btn btn-primary">Cập nhật</button>
            <a href="Consultation" class="btn btn-secondary">Hủy</a>
            <a href="Consultation" class="btn btn-outline-dark">Quay lại</a>
        </div>

    </form>
</div>
</body>
</html>

