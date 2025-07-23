<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ page import="org.example.talentcenter.model.Request" %>
<%
    ArrayList<Request> requestList = (ArrayList<Request>) request.getAttribute("requestList");
    if (requestList == null) requestList = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<html lang="vi">
<head>
    <title>Đơn xin nghỉ học - Giáo viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
    <style>
        .table-container { background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; }
        thead { background: #007bff; color: white; }
        th { padding: 15px 10px; text-align: left; font-weight: 600; }
        tbody tr { border-bottom: 1px solid #eee; transition: background 0.2s; }
        tbody tr:hover { background: #f8f9fa; }
        td { padding: 12px 10px; vertical-align: middle; }
        .reason-text { max-width: 250px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; cursor: help; }
        .reason-text:hover { white-space: normal; background: #f8f9fa; padding: 8px; border-radius: 4px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        .status-badge { padding: 4px 8px; border-radius: 12px; font-size: 12px; font-weight: 600; }
        .status-pending { background: #fff3cd; color: #856404; }
        .status-approved { background: #d4edda; color: #155724; }
        .status-rejected { background: #f8d7da; color: #721c24; }
        .action-btn { display: inline-block; padding: 6px 12px; margin: 2px; text-decoration: none; border-radius: 4px; font-size: 12px; font-weight: 600; transition: all 0.2s; }
        .btn-view { background: #007bff; color: white; }
        .btn-view:hover { background: #0056b3; color: white; text-decoration: none; }
        .btn-process { background: #28a745; color: white; }
        .btn-process:hover { background: #1e7e34; color: white; text-decoration: none; }
        .empty-state { text-align: center; padding: 40px; color: #6c757d; }
        .empty-state i { font-size: 48px; margin-bottom: 15px; color: #dee2e6; }
        .pagination-wrapper { display: flex; justify-content: center; margin-top: 30px; padding: 20px; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }
        .pagination { display: flex; list-style: none; margin: 0; padding: 0; gap: 5px; }
        .page-item { display: flex; }
        .page-link { display: flex; align-items: center; justify-content: center; padding: 10px 15px; text-decoration: none; border: 1px solid #dee2e6; color: #007bff; background: white; border-radius: 4px; transition: all 0.3s ease; min-width: 45px; height: 45px; font-weight: 500; }
        .page-link:hover { background: #e9ecef; border-color: #adb5bd; color: #0056b3; text-decoration: none; transform: translateY(-1px); }
        .page-item.active .page-link { background: #007bff; border-color: #007bff; color: white; box-shadow: 0 2px 8px rgba(0, 123, 255, 0.3); }
        .page-item.active .page-link:hover { background: #0056b3; border-color: #0056b3; transform: translateY(-1px); }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <jsp:include page="teacher-sidebar.jsp"/>
    <div class="main-content">
        <h2><i class="fas fa-user-times"></i> Danh sách đơn xin nghỉ học</h2>
        <!-- Form lọc và tìm kiếm -->
        <form method="get" action="TeacherAbsenceRequest" style="margin-bottom: 20px; display: flex; gap: 16px; align-items: center;">
            <input type="hidden" name="action" value="list"/>
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %>" style="padding: 8px; border-radius: 4px; border: 1px solid #ccc;"/>
            <select name="status" style="padding: 8px; border-radius: 4px; border: 1px solid #ccc;">
                <option value="all" <%= (request.getAttribute("statusFilter") == null || "all".equals(request.getAttribute("statusFilter"))) ? "selected" : "" %>>Tất cả trạng thái</option>
                <option value="Chờ xử lý" <%= "Chờ xử lý".equals(request.getAttribute("statusFilter")) ? "selected" : "" %>>Chờ xử lý</option>
                <option value="Đã duyệt" <%= "Đã duyệt".equals(request.getAttribute("statusFilter")) ? "selected" : "" %>>Đã duyệt</option>
                <option value="Từ chối" <%= "Từ chối".equals(request.getAttribute("statusFilter")) ? "selected" : "" %>>Từ chối</option>
            </select>
            <button type="submit" style="padding: 8px 16px; border-radius: 4px; background: #007bff; color: white; border: none;">Lọc</button>
        </form>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>STT</th>
                    <th>Học sinh</th>
                    <th>Lý do</th>
                    <th>Ngày gửi</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <% if (requestList.isEmpty()) { %>
                <tr>
                    <td colspan="6">
                        <div class="empty-state">
                            <i class="fas fa-inbox"></i>
                            <p>Chưa có đơn xin nghỉ học nào trong lớp của bạn</p>
                        </div>
                    </td>
                </tr>
                <% } else { int index = 1; for (Request r : requestList) { %>
                <tr>
                    <td style="text-align: center; font-weight: 600;"><%= index++ %></td>
                    <td><%= r.getSenderName() != null ? r.getSenderName() : "" %></td>
                    <td>
                        <div class="reason-text" title="<%= r.getReason() != null ? r.getReason().replace("\"", "&quot;") : "" %>">
                            <%= r.getReason() != null ? r.getReason() : "" %>
                        </div>
                    </td>
                    <td style="color: #6c757d;">
                        <%= r.getCreatedAt() != null ? dateFormat.format(r.getCreatedAt()) : "" %>
                    </td>
                    <td style="text-align: center;">
                        <span class="status-badge <%= "Chờ xử lý".equals(r.getStatus()) ? "status-pending" : ("Đã duyệt".equals(r.getStatus()) ? "status-approved" : ("Từ chối".equals(r.getStatus()) ? "status-rejected" : "")) %>">
                            <%= r.getStatus() != null ? r.getStatus() : "" %>
                        </span>
                    </td>
                    <td style="text-align: center;">
                        <a href="TeacherAbsenceRequest?action=detail&id=<%= r.getId() %>" class="action-btn btn-view">
                            <i class="fas fa-eye"></i> Xem
                        </a>
                        <% if ("Chờ xử lý".equals(r.getStatus())) { %>
                        <a href="TeacherAbsenceRequest?action=detail&id=<%= r.getId() %>" class="action-btn btn-process">
                            <i class="fas fa-cogs"></i> Xử lý
                        </a>
                        <% } %>
                    </td>
                </tr>
                <% } } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html> 