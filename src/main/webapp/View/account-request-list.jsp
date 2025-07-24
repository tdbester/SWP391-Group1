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

    <style>
        .batch-actions {
            margin-bottom: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
            border: 1px solid #dee2e6;
        }

        .batch-actions .btn-group {
            display: flex;
            gap: 10px;
            align-items: center;
        }

        .checkbox-cell {
            width: 40px;
            text-align: center;
        }

        .checkbox-cell input[type="checkbox"] {
            transform: scale(1.2);
        }

        .status-badge {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }

        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }

        .status-created {
            background-color: #d1edff;
            color: #0c5460;
        }

        .row-created {
            background-color: #f8f9fa;
            opacity: 0.8;
        }

        .row-created .checkbox-cell input[type="checkbox"] {
            opacity: 0.5;
            cursor: not-allowed;
        }

        #selectedCount {
            font-weight: bold;
            color: #007bff;
        }
    </style>

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

        function selectAll() {
            const checkboxes = document.querySelectorAll('input[name="selectedIds"]:not(:disabled)');
            const selectAllCheckbox = document.getElementById('selectAll');

            checkboxes.forEach(checkbox => {
                checkbox.checked = selectAllCheckbox.checked;
            });

            updateSelectedCount();
            updateBatchButtons();
        }

        function updateSelectedCount() {
            const selected = document.querySelectorAll('input[name="selectedIds"]:checked').length;
            document.getElementById('selectedCount').textContent = selected;
        }

        function updateBatchButtons() {
            const selected = document.querySelectorAll('input[name="selectedIds"]:checked').length;
            const batchCreateBtn = document.getElementById('batchCreateBtn');

            if (selected > 0) {
                batchCreateBtn.disabled = false;
                batchCreateBtn.textContent = `Tạo ${selected} tài khoản đã chọn`;
            } else {
                batchCreateBtn.disabled = true;
                batchCreateBtn.textContent = 'Tạo tài khoản đã chọn';
            }
        }

        function confirmBatchCreate() {
            const selected = document.querySelectorAll('input[name="selectedIds"]:checked');

            if (selected.length === 0) {
                alert("Vui lòng chọn ít nhất một yêu cầu!");
                return;
            }

            if (confirm(`Bạn có chắc chắn muốn tạo ${selected.length} tài khoản đã chọn không?`)) {
                const form = document.createElement("form");
                form.method = "POST";
                form.action = "CreateAccount";

                const batchInput = document.createElement("input");
                batchInput.type = "hidden";
                batchInput.name = "batchCreate";
                batchInput.value = "true";
                form.appendChild(batchInput);

                selected.forEach(checkbox => {
                    const input = document.createElement("input");
                    input.type = "hidden";
                    input.name = "selectedIds";
                    input.value = checkbox.value;
                    form.appendChild(input);
                });

                document.body.appendChild(form);
                form.submit();
            }
        }

        document.addEventListener('DOMContentLoaded', function() {
            // Add event listeners to individual checkboxes
            const checkboxes = document.querySelectorAll('input[name="selectedIds"]');
            checkboxes.forEach(checkbox => {
                checkbox.addEventListener('change', function() {
                    updateSelectedCount();
                    updateBatchButtons();

                    // Update select all checkbox state
                    const totalCheckboxes = document.querySelectorAll('input[name="selectedIds"]:not(:disabled)').length;
                    const checkedCheckboxes = document.querySelectorAll('input[name="selectedIds"]:checked').length;
                    const selectAllCheckbox = document.getElementById('selectAll');

                    if (checkedCheckboxes === 0) {
                        selectAllCheckbox.indeterminate = false;
                        selectAllCheckbox.checked = false;
                    } else if (checkedCheckboxes === totalCheckboxes) {
                        selectAllCheckbox.indeterminate = false;
                        selectAllCheckbox.checked = true;
                    } else {
                        selectAllCheckbox.indeterminate = true;
                    }
                });
            });

            // Initialize
            updateSelectedCount();
            updateBatchButtons();
        });
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
        } else if ("batch_created".equals(success)) {
            String createdCount = request.getParameter("count");
        %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong><i class="fas fa-check-circle"></i> Thành công!</strong> Đã tạo thành công <%= createdCount %> tài khoản và gửi email thông báo.
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
                case "no_selection":
                    errorMessage = "Vui lòng chọn ít nhất một yêu cầu để xử lý.";
                    break;
                case "partial_success":
                    String successCount = request.getParameter("success_count");
                    String failCount = request.getParameter("fail_count");
                    errorMessage = "Tạo thành công " + successCount + " tài khoản, thất bại " + failCount + " tài khoản.";
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

        <%
            List<Map<String, String>> requests = (List<Map<String, String>>) request.getAttribute("requests");
            if (requests != null && !requests.isEmpty()) {
        %>
        <div class="batch-actions">
            <div class="btn-group">
                <span><i class="fas fa-info-circle"></i> Đã chọn: <span id="selectedCount">0</span> yêu cầu</span>
                <button type="button" id="batchCreateBtn" class="btn btn-primary" onclick="confirmBatchCreate()" disabled>
                    <i class="fas fa-users"></i> Tạo tài khoản đã chọn
                </button>
            </div>
        </div>
        <%
            }
        %>

        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <%
                        if (requests != null && !requests.isEmpty()) {
                    %>
                    <th class="checkbox-cell">
                        <input type="checkbox" id="selectAll" onchange="selectAll()" title="Chọn tất cả">
                    </th>
                    <%
                        }
                    %>
                    <th>Tên học viên</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Người gửi</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (requests != null && !requests.isEmpty()) {
                        for (Map<String, String> req : requests) {
                            String[] parts = req.get("reason").split("\\|");
                            boolean isCreated = "Đã duyệt".equals(req.get("status"));
                %>
                <tr class="<%= isCreated ? "row-created" : "" %>">
                    <td class="checkbox-cell">
                        <input type="checkbox"
                               name="selectedIds"
                               value="<%= req.get("id") %>"
                            <%= isCreated ? "disabled" : "" %>
                               title="<%= isCreated ? "Tài khoản đã được tạo" : "Chọn để tạo tài khoản" %>">
                    </td>
                    <td><%= parts.length > 0 ? parts[0] : "" %></td>
                    <td><%= parts.length > 1 ? parts[1] : "" %></td>
                    <td><%= parts.length > 2 ? parts[2] : "" %></td>
                    <td><%= req.get("sender") %></td>
                    <td>
                        <span class="status-badge <%= isCreated ? "status-created" : "status-pending" %>">
                            <i class="fas <%= isCreated ? "fa-check-circle" : "fa-clock" %>"></i>
                            <%= isCreated ? "Đã duyệt" : "Chờ xử lý" %>
                        </span>
                    </td>
                    <td>
                        <div class="action-buttons">
                            <% if (!isCreated) { %>
                            <button class="btn btn-success btn-sm" onclick="confirmCreate(<%= req.get("id") %>)">
                                <i class="fas fa-user-plus"></i> Tạo tài khoản
                            </button>
                            <% } else { %>
                            <span class="text-muted">
                                <i class="fas fa-check"></i> Đã hoàn thành
                            </span>
                            <% } %>
                        </div>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="7" class="text-center text-danger">
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