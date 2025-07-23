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
<!DOCTYPE html>
<html>
<head>
    <title>Danh sách yêu cầu cấp tài khoản</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/create-account-request.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <script>
        function confirmCreate(id) {
            if (confirm("Bạn có chắc chắn muốn tạo tài khoản này không?")) {
                const form = document.createElement("form");
                form.method = "POST";
                form.action = "CreateAccount";

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
<jsp:include page="header.jsp"/>
<div class="container">
    <jsp:include page="training-manager-sidebar.jsp"/>
    <div class="main-content">
        <h1><i class="fas fa-user-cog"></i>Danh sách yêu cầu cấp tài khoản</h1>

        <%
            String success = request.getParameter("success");
            String error = request.getParameter("error");

            if ("account_created".equals(success)) {
        %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong><i class="fas fa-check-circle"></i> Thành công!</strong> Tài khoản đã được tạo và email thông báo đã được gửi đến học sinh.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <%
        } else if (error != null) {
            String errorMessage = "";
            switch (error) {
                case "request_not_found":
                    errorMessage = "Không tìm thấy yêu cầu này.";
                    break;
                case "invalid_data":
                    errorMessage = "Dữ liệu yêu cầu không hợp lệ.";
                    break;
                case "email_failed":
                    errorMessage = "Tài khoản đã được tạo nhưng gửi email thất bại.";
                    break;
                case "account_creation_failed":
                    errorMessage = "Không thể tạo tài khoản. Có thể email đã tồn tại.";
                    break;
                case "invalid_id":
                    errorMessage = "ID yêu cầu không hợp lệ.";
                    break;
                default:
                    errorMessage = "Đã xảy ra lỗi trong quá trình xử lý.";
            }
        %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong><i class="fas fa-exclamation-triangle"></i> Lỗi!</strong> <%= errorMessage %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <%
            }
        %>

        <div class="table-container">
            <table>
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
                    <td><%= parts.length > 0 ? parts[0] : "" %></td>
                    <td><%= parts.length > 1 ? parts[1] : "" %></td>
                    <td><%= parts.length > 2 ? parts[2] : "" %></td>
                    <td><%= req.get("sender") %></td>
                    <td>
                        <div class="action-buttons">
                            <button class="btn btn-success btn-sm" onclick="confirmCreate(<%= req.get("id") %>)">
                                <i class="fas fa-user-plus"></i> Tạo tài khoản
                            </button>
                        </div>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="5" class="text-center text-danger">
                        <i class="fas fa-inbox"></i> Không có yêu cầu nào
                    </td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
