<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/9/2025
  Time: 4:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Map" %>
<html>
<head>
    <title>Title</title>
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
            border-color: #7a6ad8;
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
    <script>
        function confirmCreate(id) {
            if (confirm("Bạn có chắc chắn muốn tạo tài khoản này không?")) {
                const form = document.createElement("form");
                form.method = "POST";
                form.action = "account-request-list";

                const input = document.createElement("input");
                input.type = "hidden";
                input.name = "id";
                input.value = id;

                form.appendChild(input);
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</head>
<body>
<div class="container mt-5">
    <h2>Danh sách yêu cầu cấp tài khoản</h2>
    <table class="table table-bordered mt-3">
        <thead>
        <tr>
            <th>Tên học viên</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Người gửi</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Map<String, String>> requests = (List<Map<String, String>>) request.getAttribute("requests");
            if (requests != null && !requests.isEmpty()) {
                for (Map<String, String> req : requests) {
                    String[] parts = req.get("reason").split("\\|");
        %>
        <tr>
            <td><%= parts.length > 0 ? parts[0] : "" %>
            </td>
            <td><%= parts.length > 1 ? parts[1] : "" %>
            </td>
            <td><%= parts.length > 2 ? parts[2] : "" %>
            </td>
            <td><%= req.get("sender") %>
            </td>
            <td>
                <button class="btn btn-success btn-sm" onclick="confirmCreate(<%= req.get("id") %>)">Tạo</button>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="5" class="text-center text-danger">Không có yêu cầu nào</td>
        </tr>
        <%
            }
        %>

        </tbody>
    </table>
</div>
</body>
</html>
