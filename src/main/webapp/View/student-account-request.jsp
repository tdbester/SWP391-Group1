<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/9/2025
  Time: 1:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Yêu cầu cấp tài khoản học viên</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        html, body {
            height: 100%;
            margin: 0;
            display: flex;
            flex-direction: column;
        }

        main {
            flex: 1; /* chiếm phần còn lại giữa header và footer */
        }

        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f8f9fa;
        }

        form {
            max-width: 500px;
            margin: auto;
            background: #fff;
            padding: 25px;
            border-radius: 8px;
        }

        .form-wrapper {
            max-width: 500px;
            margin: 50px auto;
            background: #fff;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
        }

        label.form-label {
            font-weight: 600;
            color: #333;
        }

        input.form-control {
            border-radius: 4px;
            border: 1px solid #ddd;
            padding: 8px 12px;
            font-size: 14px;
        }

        .btn-primary {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
            font-family: 'Poppins', sans-serif;
            font-weight: 500;
            border-radius: 4px;
            padding: 8px 16px;
            transition: background-color 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #6a5acd;
            border-color: #6a5acd;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<main>
    <div class="form-wrapper">
        <h1>Yêu cầu cấp tài khoản cho học sinh</h1>

        <form method="post" action="StudentAccountRequest">
            <div class="mb-3">
                <label for="studentName" class="form-label">Tên học viên</label>
                <input type="text" class="form-control" name="studentName" required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" name="email" required>
            </div>
            <div class="mb-3">
                <label for="phone" class="form-label">Số điện thoại</label>
                <input type="text" class="form-control" name="phone" required>
            </div>
            <button type="submit" class="btn btn-primary">Gửi đơn</button>
        </form>
        <c:if test="${not empty message}">
            <div class="alert alert-info">${message}</div>
        </c:if>
    </div>
</main>
<jsp:include page="footer.jsp"/>

</body>
</html>
