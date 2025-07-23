<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.example.talentcenter.model.Request" %>
<%
    Request requestDetail = (Request) request.getAttribute("requestDetail");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String reason = "";
    String className = "";
    if (requestDetail != null) {
        reason = requestDetail.getReason();
        String[] parts = reason.split("\\|");
        if (parts.length >= 1) {
            className = parts[0].trim();
        }
    }
%>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn xin nghỉ học - Giáo viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
    <style>
        .info-section { background: white; border-radius: 15px; padding: 25px; margin-bottom: 25px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08); border: 1px solid #e9ecef; }
        .section-title { color: #2c3e50; font-size: 20px; font-weight: 600; margin-bottom: 20px; padding-bottom: 10px; border-bottom: 3px solid #3498db; display: flex; align-items: center; }
        .section-title i { margin-right: 10px; color: #3498db; }
        .info-table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        .info-table td { padding: 15px; border-bottom: 1px solid #eee; vertical-align: top; }
        .info-table td:first-child { background: #f8f9fa; font-weight: 600; color: #495057; width: 200px; border-right: 3px solid #dee2e6; }
        .info-table td:last-child { color: #2c3e50; }
        .info-table tr:last-child td { border-bottom: none; }
        .status-badge { padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; text-transform: uppercase; }
        .status-pending { background: #fff3cd; color: #856404; border: 1px solid #ffeaa7; }
        .status-approved { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .status-rejected { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .process-form { background: #f8f9fa; padding: 25px; border-radius: 15px; border: 2px solid #dee2e6; margin-top: 20px; }
        .form-group { margin-bottom: 20px; }
        .form-label { display: block; margin-bottom: 8px; font-weight: 600; color: #495057; font-size: 16px; }
        .form-textarea { width: 100%; min-height: 120px; padding: 15px; border: 2px solid #dee2e6; border-radius: 10px; font-size: 14px; resize: vertical; transition: all 0.3s ease; font-family: inherit; }
        .form-textarea:focus { outline: none; border-color: #3498db; box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1); }
        .button-group { display: flex; gap: 15px; margin-top: 25px; flex-wrap: wrap; }
        .btn { padding: 12px 25px; border: none; border-radius: 10px; font-size: 14px; font-weight: 600; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; transition: all 0.3s ease; min-width: 120px; justify-content: center; }
        .btn-approve { background: #28a745; color: white; }
        .btn-approve:hover { background: #218838; text-decoration: none; color: white; }
        .btn-reject { background: #dc3545; color: white; }
        .btn-reject:hover { background: #c82333; text-decoration: none; color: white; }
        .btn-secondary { background: #6c757d; color: white; }
        .btn-secondary:hover { background: #5a6268; text-decoration: none; color: white; }
        .btn i { margin-right: 8px; }
        .empty-state { text-align: center; padding: 40px; color: #6c757d; }
        .empty-state i { font-size: 48px; margin-bottom: 15px; color: #dee2e6; }
        .error-message { background: #f8d7da; color: #721c24; padding: 15px; border-radius: 10px; margin-bottom: 20px; border: 1px solid #f5c6cb; display: flex; align-items: center; }
        .error-message i { margin-right: 10px; font-size: 18px; }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <jsp:include page="teacher-sidebar.jsp"/>
    <div class="main-content">
        <h2 class="section-title"><i class="fas fa-user-times"></i> Chi tiết đơn xin nghỉ học</h2>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
            </div>
        </c:if>
        <% if (requestDetail == null) { %>
        <div class="empty-state">
            <i class="fas fa-exclamation-triangle"></i>
            <h3>Không tìm thấy đơn</h3>
            <p>Đơn xin nghỉ học này không tồn tại hoặc đã bị xóa.</p>
        </div>
        <% } else { %>
        <div class="info-section">
            <table class="info-table">
                <tr>
                    <td><i class="fas fa-user"></i> Học sinh:</td>
                    <td><%= requestDetail.getSenderName() != null ? requestDetail.getSenderName() : "" %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-graduation-cap"></i> Lớp:</td>
                    <td><%= className %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-comment"></i> Lý do:</td>
                    <td><%= reason != null ? reason : "" %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-clock"></i> Ngày gửi:</td>
                    <td><%= requestDetail.getCreatedAt() != null ? dateFormat.format(requestDetail.getCreatedAt()) : "" %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-info-circle"></i> Trạng thái:</td>
                    <td>
                        <span class="status-badge <%= "Chờ xử lý".equals(requestDetail.getStatus()) ? "status-pending" : ("Đã duyệt".equals(requestDetail.getStatus()) ? "status-approved" : ("Từ chối".equals(requestDetail.getStatus()) ? "status-rejected" : "")) %>">
                            <%= requestDetail.getStatus() != null ? requestDetail.getStatus() : "" %>
                        </span>
                    </td>
                </tr>
            </table>
        </div>
        <div class="info-section">
            <h3>Lịch sử xử lý</h3>
            <% if (requestDetail.getResponse() != null && !requestDetail.getResponse().trim().isEmpty()) { %>
            <table class="info-table">
                <tr>
                    <td><i class="fas fa-reply"></i> Phản hồi từ giáo viên:</td>
                    <td><%= requestDetail.getResponse() %></td>
                </tr>
                <tr>
                    <td><i class="fas fa-clock"></i> Thời gian xử lý:</td>
                    <td><%= requestDetail.getResponseAt() != null ? dateFormat.format(requestDetail.getResponseAt()) : "" %></td>
                </tr>
            </table>
            <% } else { %>
            <div class="empty-state">
                <i class="fas fa-inbox" style="font-size: 48px; margin-bottom: 15px; color: #dee2e6;"></i>
                <p>Chưa có lịch sử xử lý</p>
            </div>
            <% } %>
        </div>
        <% if ("Chờ xử lý".equals(requestDetail.getStatus())) { %>
        <div class="info-section">
            <form action="TeacherAbsenceRequest" method="post" class="process-form">
                <input type="hidden" name="requestId" value="<%= requestDetail.getId() %>">
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-comment"></i> Phản hồi của giáo viên <span style="color: #dc3545;">*</span>
                    </label>
                    <textarea name="teacherNote" class="form-textarea" placeholder="Nhập phản hồi của bạn về đơn xin nghỉ học này..." required></textarea>
                </div>
                <div class="button-group">
                    <button type="submit" name="action" value="approve" class="btn btn-approve">
                        <i class="fas fa-check"></i> Duyệt đơn
                    </button>
                    <button type="submit" name="action" value="reject" class="btn btn-reject">
                        <i class="fas fa-times"></i> Từ chối
                    </button>
                    <a href="TeacherAbsenceRequest?action=list" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Quay lại
                    </a>
                </div>
            </form>
        </div>
        <% } else { %>
        <div class="info-section">
            <div class="button-group">
                <a href="TeacherAbsenceRequest?action=list" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Quay lại danh sách
                </a>
            </div>
        </div>
        <% } %>
        <% } %>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html> 