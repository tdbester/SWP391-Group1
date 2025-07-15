<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 7/15/2025
  Time: 11:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <title>Teacher Detail</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
  <style>
    .btn-primary,
    .btn-success,
    .btn-warning,
    .btn-add-custom {
      background-color: #7a6ad8;
      border-color: #7a6ad8;
      color: #fff;
      border-radius: 4px;
      padding: 6px 12px;
      font-family: 'Poppins', sans-serif;
    }

    header {
      background: #7A5AF8;
      color: white;
      padding: 16px 32px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: sticky;
      top: 0;
      z-index: 100;
    }

    .nav a {
      color: white;
      text-decoration: none;
      font-weight: 500;
    }

    .section-title {
      background-color: #f8f9fa;
      padding: 10px 15px;
      border-left: 4px solid #7a6ad8;
      margin: 20px 0 15px 0;
      font-weight: bold;
    }
  </style>
</head>

<body>
<jsp:include page="header.jsp" />
<div class="container mt-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Chi tiết giáo viên</h2>
    <a href="teachers" class="btn btn-secondary">Quay lại danh sách</a>
  </div>

  <c:if test="${param.success == 'true'}">
    <div class="alert alert-success">Cập nhật thông tin thành công!</div>
  </c:if>
  <c:if test="${param.error == 'UpdateFailed'}">
    <div class="alert alert-danger">Cập nhật thông tin thất bại!</div>
  </c:if>

  <!-- Teacher Information Form -->
  <div class="section-title">Thông tin giáo viên</div>
  <form action="teachers" method="post" class="mb-4">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="teacherId" value="${teacher.id}">
    <input type="hidden" name="accountId" value="${teacher.accountId}">

    <div class="row">
      <div class="col-md-6">
        <div class="mb-3">
          <label for="fullName" class="form-label">Họ tên</label>
          <input type="text" class="form-control" id="fullName" name="fullName"
                 value="${teacher.account.fullName}" required>
        </div>
        <div class="mb-3">
          <label for="department" class="form-label">Khoa</label>
          <input type="text" class="form-control" id="department" name="department"
                 value="${teacher.department}" required>
        </div>
        <div class="mb-3">
          <label for="salary" class="form-label">Lương</label>
          <input type="number" class="form-control" id="salary" name="salary"
                 value="${teacher.salary}" step="0.01" required>
        </div>
      </div>
      <div class="col-md-6">
        <div class="mb-3">
          <label for="phoneNumber" class="form-label">Số điện thoại</label>
          <input type="text" class="form-control" id="phoneNumber" name="phoneNumber"
                 value="${teacher.account.phoneNumber}">
        </div>
        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input type="email" class="form-control" id="email" name="email"
                 value="${teacher.account.email}">
        </div>
        <div class="mb-3">
          <label for="address" class="form-label">Địa chỉ</label>
          <textarea class="form-control" id="address" name="address" rows="3">${teacher.account.address}</textarea>
        </div>
      </div>
    </div>
    <button type="submit" class="btn btn-primary">Cập nhật thông tin</button>
  </form>

  <!-- Classes Table -->
  <div class="section-title">Danh sách lớp giảng dạy</div>
  <c:choose>
    <c:when test="${not empty classes}">
      <table class="table table-striped mb-4">
        <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>Tên lớp</th>
          <th>Tên khóa học</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="classRoom" items="${classes}">
          <tr>
            <td>${classRoom.id}</td>
            <td>${classRoom.name}</td>
            <td>
              <c:set var="courseName" value="Không tìm thấy" />
              <c:forEach var="course" items="${allCourses}">
                <c:if test="${course.id == classRoom.courseId}">
                  <c:set var="courseName" value="${course.title}" />
                </c:if>
              </c:forEach>
                ${courseName}
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:when>
    <c:otherwise>
      <div class="alert alert-info">Giáo viên chưa được phân công lớp nào.</div>
    </c:otherwise>
  </c:choose>

</div>

<jsp:include page="footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
