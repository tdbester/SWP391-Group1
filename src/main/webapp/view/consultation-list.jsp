<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Danh sách học sinh tư vấn</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .no-results {
            color: red;
            font-weight: bold;
            text-align: center;
            margin-top: 20px;
        }

        .btn-primary, .btn-success, .btn-warning, .btn-add-custom {
            background-color: #7a6ad8;
            border-color:#7a6ad8;
            color: #fff;
            border-radius: 4px;
            padding: 6px 12px;
            font-family: 'Poppins', sans-serif;
        }
        .btn-primary:hover, .btn-success:hover, .btn-warning:hover, .btn-add-custom:hover {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
        }

        .btn-warning.btn-sm {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
            color: #fff;
            border-radius: 4px;
            font-family: 'Poppins', sans-serif;
        }
        .btn-warning.btn-sm:hover {
            background-color: #6a5acd;
            border-color: #6a5acd;
        }

        /* Button Xóa: Đỏ (giữ màu Bootstrap nhưng bo góc theo template) */
        .btn-danger.btn-sm {
            background-color: #dc3545;
            border-color: #dc3545;
            border-radius: 4px;
            font-family: 'Poppins', sans-serif;
        }
        .btn-danger.btn-sm:hover {
            background-color: #c82333;
            border-color: #c82333;
        }

        /* Button Quay lại: Trắng nền, chữ tím */
        .btn-back-custom {
            background-color: #fff;
            color: #7a6ad8;
            border: 1px solid #7a6ad8;
            border-radius: 20px;
            padding: 0px 25px;
            height: 40px;
            line-height: 40px;
            font-family: 'Poppins', sans-serif;
            font-weight: 500;
            transition: all .3s;
        }
        .btn-back-custom:hover {
            background-color: #7a6ad8;
            color: #fff;
        }
        th {
            background-color: #007bff;
            color: white;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container mt-4">
<c:if test="${not empty sessionScope.message}">
    <div class="alert alert-success">${sessionScope.message}</div>
    <c:remove var="message" scope="session" />
</c:if>
    <h1>Danh sách học sinh đăng ký tư vấn</h1>
    <form method="get" action="Consultation" class="mb-3">
        <input type="hidden" name="action" value="search" />
        <div class="row g-2 align-items-end">
            <div class="col-md-10">
                <input type="text" name="keyword" class="form-control"
                       placeholder="Tìm theo tên, email hoặc sđt" value="${param.keyword}"/>
            </div>
            <div class="col-md-2 d-flex gap-2">
                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                <a href="Consultation" class="btn btn-secondary">Hủy</a>
            </div>
        </div>
    </form>

    <form method="get" action="Consultation" class="mb-3">
        <input type="hidden" name="action" value="filterByCourse" />
        <div class="row g-2 align-items-end">
            <div class="col-md-9">
                <select name="course_filter" class="form-select">
                    <option value="">--Chọn khóa học--</option>
                    <c:forEach var="subject" items="${subjects}">
                        <option value="${subject.title}" ${param.course_filter == subject.title ? 'selected' : ''}>
                                ${subject.title}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3 d-flex gap-2">
                <button type="submit" class="btn btn-success">Lọc theo khóa học</button>
                <a href="Consultation" class="btn btn-secondary">Hủy</a>
            </div>
        </div>
    </form>

    <h2 class="mt-5">Thêm học sinh mới</h2>
    <form method="post" action="Consultation" class="row g-3">
        <div class="col-md-3">
            <input type="text" name="name" class="form-control" placeholder="Họ tên" required/>
        </div>
        <div class="col-md-3">
            <input type="email" name="email" class="form-control" placeholder="Email" required/>
        </div>
        <div class="col-md-2">
            <input type="text" name="phone" class="form-control" placeholder="Số điện thoại" required/>
        </div>
        <div class="col-md-3">
            <select name="course_interest" class="form-select" required>
                <option value="">--Chọn khóa học--</option>
                <c:forEach var="subject" items="${subjects}">
                    <option value="${subject.id}">${subject.title}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-1 d-grid">
            <button type="submit" name="action" value="add" class="btn btn-success">Thêm</button>
        </div>
    </form>

    <br>
    <table class="table table-bordered">
        <thead class="table-warning">
        <tr>
            <th>ID</th>
            <th>Họ tên</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Khóa học</th>
            <th>Trạng thái</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${consultations}">
            <tr>
                <td>${c.id}</td>
                <td>${c.fullName}</td>
                <td>${c.email}</td>
                <td>${c.phone}</td>
                <td>${c.title}</td>
                <td>
                    <form action="Consultation?action=updateConsultationStatus" method="post">
                        <input type="hidden" name="id" value="${c.id}"/>
                        <select name="status" class="form-select" onchange="this.form.submit()">
                            <option value="Đang xử lý" <c:if test="${c.status eq 'Đang xử lý'}">selected</c:if>>Đang xử lý</option>
                            <option value="Đồng ý" <c:if test="${c.status eq 'Đồng ý'}">selected</c:if>>Đồng ý</option>
                            <option value="Từ chối" <c:if test="${c.status eq 'Từ chối'}">selected</c:if>>Từ chối</option>
                        </select>
                    </form>
                </td>
                <td>
                    <a href="Consultation?action=edit&id=${c.id}" class="btn btn-sm btn-warning">Sửa</a>

                    <form method="post" action="Consultation" style="display:inline;">
                        <input type="hidden" name="id" value="${c.id}"/>
                        <button type="submit" name="action" value="delete" class="btn btn-sm btn-danger"
                                onclick="return confirm('Bạn có chắc muốn xóa?')">Xóa
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
    <a href="Consultation?action=dashboard" class="btn btn-secondary mt-4" style="margin-bottom: 30px;">Quay lại</a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<jsp:include page="footer.jsp" />
</body>
</html>
