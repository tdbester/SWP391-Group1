<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="org.example.talentcenter.model.TeacherSalary" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>

<%
    // Lấy dữ liệu từ servlet
    List<TeacherSalary> salaries = (List<TeacherSalary>) request.getAttribute("salaries");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
    Integer selectedMonth = (Integer) request.getAttribute("selectedMonth");
    Integer selectedYear = (Integer) request.getAttribute("selectedYear");
    String searchName = (String) request.getAttribute("searchName");
    Integer totalRecords = (Integer) request.getAttribute("totalRecords");

    // Salary đang được edit
    TeacherSalary editSalary = (TeacherSalary) request.getAttribute("editSalary");
    Boolean editMode = (Boolean) request.getAttribute("editMode");

    // Messages
    String message = request.getParameter("message");
    String error = request.getParameter("error");

    // Format currency
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    currencyFormat.setMaximumFractionDigits(0);

    // Date format
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Set defaults
    if (currentPage == null) currentPage = 1;
    if (selectedMonth == null) selectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    if (selectedYear == null) selectedYear = Calendar.getInstance().get(Calendar.YEAR);
    if (searchName == null) searchName = "";
    if (totalPages == null) totalPages = 1;
    if (totalRecords == null) totalRecords = 0;
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý lương giáo viên</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-salary.css">
</head>
<body>
<!-- Include Header -->
<jsp:include page="header.jsp" />

<!-- Mobile Sidebar Toggle -->
<button class="sidebar-toggle" onclick="toggleSidebar()">
    <i class="fas fa-bars"></i>
</button>

<!-- Dashboard Layout with Sidebar -->
<div class="dashboard">
    <!-- Include Admin Sidebar -->
    <div class="student-sidebar" id="sidebar">
        <jsp:include page="admin-sidebar.jsp" />
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="container">
            <h1>Quản lý lương giáo viên</h1>

            <!-- Alert Messages -->
            <% if (message != null) { %>
            <div class="alert alert-success">
                <%
                    switch (message) {
                        case "calculate_success":
                            out.print("Tính lương thành công!");
                            break;
                        case "pay_success":
                            out.print("Thanh toán lương thành công!");
                            break;
                        case "update_success":
                            out.print("Cập nhật lương thành công!");
                            break;
                        default:
                            out.print("Thao tác thành công!");
                    }
                %>
            </div>
            <% } %>

            <% if (error != null) { %>
            <div class="alert alert-error">
                <%
                    switch (error) {
                        case "calculate_failed":
                            out.print("Lỗi khi tính lương!");
                            break;
                        case "pay_failed":
                            out.print("Thanh toán thất bại!");
                            break;
                        case "update_failed":
                            out.print("Cập nhật thất bại!");
                            break;
                        case "invalid_parameters":
                            out.print("Tham số không hợp lệ!");
                            break;
                        default:
                            out.print("Có lỗi xảy ra!");
                    }
                %>
            </div>
            <% } %>

            <!-- Filter Section -->
            <div class="filter-section">
                <form method="GET" action="teacher-salary" class="filter-form">
                    <div class="filter-row">
                        <div class="form-group">
                            <label for="month">Tháng:</label>
                            <select name="month" id="month">
                                <% for (int i = 1; i <= 12; i++) { %>
                                <option value="<%= i %>" <%= (selectedMonth == i) ? "selected" : "" %>><%= i %></option>
                                <% } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="year">Năm:</label>
                            <select name="year" id="year">
                                <% for (int i = 2020; i <= 2030; i++) { %>
                                <option value="<%= i %>" <%= (selectedYear == i) ? "selected" : "" %>><%= i %></option>
                                <% } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="searchName">Tên giáo viên:</label>
                            <input type="text" name="searchName" id="searchName" value="<%= searchName %>" placeholder="Nhập tên giáo viên">
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Edit Form -->
            <% if (editMode != null && editMode && editSalary != null) { %>
            <div class="edit-form-container">
                <h3>Điều chỉnh lương giáo viên</h3>
                <form method="POST" action="teacher-salary" class="edit-form">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="teacherId" value="<%= editSalary.getTeacherId() %>">
                    <input type="hidden" name="month" value="<%= selectedMonth %>">
                    <input type="hidden" name="year" value="<%= selectedYear %>">

                    <div class="form-row">
                        <div class="form-group">
                            <label>Tên giáo viên:</label>
                            <input type="text" value="<%= editSalary.getTeacherName() %>" readonly>
                        </div>
                        <div class="form-group">
                            <label>Lương tháng hiện tại:</label>
                            <input type="text" value="<%= currencyFormat.format(editSalary.getTotalSalary()) %>" readonly class="current-salary">
                        </div>
                        <div class="form-group">
                            <label>Điều chỉnh hiện tại:</label>
                            <input type="text" value="<%= currencyFormat.format(editSalary.getAdjustment()) %>" readonly class="current-adjustment">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="adjustment">Điều chỉnh mới (VNĐ):</label>
                            <input type="number" name="adjustment" id="adjustment" placeholder="Nhập số tiền điều chỉnh" step="1000" value="<%= editSalary.getAdjustment() %>">
                            <small>Nhập số dương để thưởng, số âm để phạt</small>
                        </div>
                        <div class="form-group">
                            <label for="note">Ghi chú (bắt buộc):</label>
                            <textarea name="note" id="note" placeholder="Nhập lý do điều chỉnh lương..." required><%= editSalary.getNote() != null ? editSalary.getNote() : "" %></textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-success">Cập nhật</button>
                        <a href="teacher-salary?month=<%= selectedMonth %>&year=<%= selectedYear %>" class="btn btn-secondary">Hủy</a>
                    </div>
                </form>
            </div>
            <% } %>

            <!-- Summary Info -->
            <div class="alert alert-info">
                <strong>Thống kê:</strong> Tổng số giáo viên: <%= totalRecords %> |
                Tháng <%= selectedMonth %>/<%= selectedYear %> |
                Trang <%= currentPage %>/<%= totalPages %>
            </div>

            <!-- Salary Table -->
            <div class="table-container">
                <table class="salary-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Họ và tên</th>
                        <th>Số buổi dạy</th>
                        <th>Lương buổi</th>
                        <th>Lương tháng</th>
                        <th>Điều chỉnh</th>
                        <th>Lương cuối</th>
                        <th>Ngày trả lương</th>
                        <th>Thao tác</th>
                        <th>Ghi chú</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (salaries != null && !salaries.isEmpty()) { %>
                    <% for (TeacherSalary salary : salaries) { %>
                    <tr>
                        <td><%= salary.getTeacherId() %></td>
                        <td><%= salary.getTeacherName() %></td>
                        <td class="text-center"><%= salary.getTotalSessions() %></td>
                        <td class="currency"><%= currencyFormat.format(salary.getSalaryPerSession()) %></td>
                        <td class="currency"><%= currencyFormat.format(salary.getTotalSalary()) %></td>
                        <td class="currency">
                            <% if (salary.getAdjustment() != 0) { %>
                            <span class="<%= salary.getAdjustment() > 0 ? "positive" : "negative" %>">
                                                <%= salary.getAdjustment() > 0 ? "+" : "" %><%= currencyFormat.format(salary.getAdjustment()) %>
                                            </span>
                            <% } %>
                        </td>
                        <td class="currency final-salary"><%= currencyFormat.format(salary.getFinalSalary()) %></td>
                        <td>
                            <% if (salary.getPaymentDate() != null) { %>
                            <span class="paid-status"><%= dateFormat.format(salary.getPaymentDate()) %></span>
                            <% } else { %>
                            <span class="not-paid">Chưa thanh toán</span>
                            <% } %>
                        </td>
                        <td>
                            <div class="action-buttons">
                                <% if (salary.getPaymentDate() == null) { %>
                                <a href="teacher-salary?action=pay&teacherId=<%= salary.getTeacherId() %>&month=<%= selectedMonth %>&year=<%= selectedYear %>"
                                   class="btn btn-success btn-sm"
                                   onclick="return confirm('Xác nhận thanh toán lương cho <%= salary.getTeacherName() %>?')">
                                    Thanh toán
                                </a>
                                <% } %>
                                <a href="teacher-salary?action=edit&teacherId=<%= salary.getTeacherId() %>&month=<%= selectedMonth %>&year=<%= selectedYear %>"
                                   class="btn btn-warning btn-sm">
                                    Chỉnh sửa
                                </a>
                            </div>
                        </td>
                        <td class="note-cell">
                            <% if (salary.getNote() != null && !salary.getNote().isEmpty()) { %>
                            <span class="note-text"><%= salary.getNote() %></span>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td colspan="10" class="no-data">Không có dữ liệu lương cho tháng <%= selectedMonth %>/<%= selectedYear %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <% if (totalPages > 1) { %>
            <div class="pagination">
                <% if (currentPage > 1) { %>
                <a href="teacher-salary?month=<%= selectedMonth %>&year=<%= selectedYear %>&searchName=<%= searchName %>&page=<%= currentPage - 1 %>">
                    « Trước
                </a>
                <% } else { %>
                <span class="disabled">« Trước</span>
                <% } %>

                <%
                    int startPage = Math.max(1, currentPage - 2);
                    int endPage = Math.min(totalPages, currentPage + 2);

                    for (int i = startPage; i <= endPage; i++) {
                        if (i == currentPage) {
                %>
                <span class="current"><%= i %></span>
                <%      } else { %>
                <a href="teacher-salary?month=<%= selectedMonth %>&year=<%= selectedYear %>&searchName=<%= searchName %>&page=<%= i %>">
                    <%= i %>
                </a>
                <%      }
                }
                %>

                <% if (currentPage < totalPages) { %>
                <a href="teacher-salary?month=<%= selectedMonth %>&year=<%= selectedYear %>&searchName=<%= searchName %>&page=<%= currentPage + 1 %>">
                    Sau »
                </a>
                <% } else { %>
                <span class="disabled">Sau »</span>
                <% } %>
            </div>
            <% } %>
        </div>
    </div>
</div>

<!-- Include Footer -->
<jsp:include page="footer.jsp" />

<script>
    function toggleSidebar() {
        const sidebar = document.getElementById('sidebar');
        sidebar.classList.toggle('active');
    }

    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', function(event) {
        const sidebar = document.getElementById('sidebar');
        const toggleBtn = document.querySelector('.sidebar-toggle');

        if (window.innerWidth <= 1024) {
            if (!sidebar.contains(event.target) && !toggleBtn.contains(event.target)) {
                sidebar.classList.remove('active');
            }
        }
    });
</script>
</body>
</html>