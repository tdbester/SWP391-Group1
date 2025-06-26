<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gửi đơn cho phòng quản lý đào tạo - TALENT01</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-request.css">

</head>
<body>
<!-- Header -->
<%@ include file="header.jsp" %>

<!-- Dashboard Layout -->
<div class="dashboard">
    <!-- Sidebar -->
    <%@ include file="teacher-sidebar.jsp" %>

    <!-- Main Content -->
    <div class="main-content">
        <div class="content-wrapper">
            <h1 class="page-title">Gửi đơn cho phòng quản lý đào tạo</h1>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/requestForm" method="post">
                <div class="form-group">
                    <label for="requestType">Chọn loại đơn:</label>
                    <select id="requestType" name="type" required>
                        <option value="">-- Chọn loại đơn --</option>
                        <option value="Xin nghỉ phép">Xin nghỉ phép</option>
                        <option value="Khiếu nại">Khiếu nại</option>
                        <option value="Đề xuất">Đề xuất</option>
                        <option value="Thay đổi lịch học">Thay đổi lịch học</option>
                        <option value="Khác">Khác</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="reason">Lý do:</label>
                    <div class="toolbar">
                        <button type="button" onclick="formatText('bold')" title="Đậm">
                            <i class="fas fa-bold"></i>
                        </button>
                        <button type="button" onclick="formatText('italic')" title="Nghiêng">
                            <i class="fas fa-italic"></i>
                        </button>
                        <button type="button" onclick="formatText('underline')" title="Gạch chân">
                            <i class="fas fa-underline"></i>
                        </button>
                        <button type="button" onclick="insertList('ul')" title="Danh sách">
                            <i class="fas fa-list-ul"></i>
                        </button>
                        <button type="button" onclick="insertList('ol')" title="Danh sách số">
                            <i class="fas fa-list-ol"></i>
                        </button>
                    </div>
                    <textarea
                            id="reason"
                            name="reason"
                            placeholder="Vui lòng mô tả chi tiết lý do gửi đơn của bạn..."
                            required
                    ></textarea>
                </div>

                <button type="submit" class="submit-btn">
                    <i class="fas fa-paper-plane"></i> Gửi đơn
                </button>
            </form>
        </div>
    </div>
</div>

<!-- Footer -->
<%@ include file="footer.jsp" %>

<script>
    // Text formatting functions
    function formatText(command) {
        const textarea = document.getElementById('reason');
        const start = textarea.selectionStart;
        const end = textarea.selectionEnd;
        const selectedText = textarea.value.substring(start, end);

        if (selectedText) {
            let formattedText = selectedText;
            switch(command) {
                case 'bold':
                    formattedText = `**${selectedText}**`;
                    break;
                case 'italic':
                    formattedText = `*${selectedText}*`;
                    break;
                case 'underline':
                    formattedText = `<u>${selectedText}</u>`;
                    break;
            }

            textarea.value = textarea.value.substring(0, start) + formattedText + textarea.value.substring(end);
            textarea.focus();
            textarea.setSelectionRange(start + formattedText.length, start + formattedText.length);
        }
    }

    function insertList(type) {
        const textarea = document.getElementById('reason');
        const start = textarea.selectionStart;
        const listItem = type === 'ul' ? '• ' : '1. ';

        textarea.value = textarea.value.substring(0, start) + listItem + textarea.value.substring(start);
        textarea.focus();
        textarea.setSelectionRange(start + listItem.length, start + listItem.length);
    }

    // Auto-resize textarea
    document.getElementById('reason').addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = Math.max(200, this.scrollHeight) + 'px';
    });

    // Request type change handler
    document.getElementById('requestType').addEventListener('change', function() {
        const reasonTextarea = document.getElementById('reason');
        const placeholders = {
            'Xin nghỉ phép': 'Vui lòng nêu rõ thời gian nghỉ phép và lý do nghỉ phép...',
            'Khiếu nại': 'Vui lòng mô tả chi tiết vấn đề bạn muốn khiếu nại...',
            'Đề xuất': 'Vui lòng trình bày đề xuất của bạn một cách chi tiết...',
            'Thay đổi lịch học': 'Vui lòng nêu rõ lịch học hiện tại và lịch học mong muốn...'
        };

        reasonTextarea.placeholder = placeholders[this.value] || 'Vui lòng mô tả chi tiết lý do gửi đơn của bạn...';
    });
</script>
</body>
</html>