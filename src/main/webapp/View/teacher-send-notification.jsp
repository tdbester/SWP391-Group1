<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gửi Thông Báo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-send-notification.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-sidebar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<!-- Header -->
<%@ include file="../View/header.jsp" %>

<!-- Main Layout Container -->
<div class="main-layout">
    <!-- Sidebar -->
    <%@ include file="../View/teacher-sidebar.jsp" %>

    <!-- Main Content -->
    <main class="main-content">
        <div class="content-wrapper">
            <div class="page-header">
                <h1><i class="fas fa-bell"></i> Quản Lý Thông Báo</h1>
            </div>

            <!-- Alert Messages -->
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i>
                    <span>${successMessage}</span>
                </div>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>${errorMessage}</span>
                </div>
            </c:if>

            <!-- Notification Form Section -->
            <div class="card">
                <div class="card-header">
                    <h2>
                        <i class="fas fa-plus-circle"></i>
                        <c:choose>
                            <c:when test="${editNotification != null}">
                                Chỉnh Sửa Thông Báo
                            </c:when>
                            <c:otherwise>
                                Gửi Thông Báo Mới
                            </c:otherwise>
                        </c:choose>
                    </h2>
                </div>
                <div class="card-body">
                    <form method="post" action="sendNotification" class="notification-form">
                        <c:choose>
                            <c:when test="${editNotification != null}">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="notificationId" value="${editNotification.id}">
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" name="action" value="send">
                            </c:otherwise>
                        </c:choose>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="title">
                                    <i class="fas fa-heading"></i>
                                    Tiêu đề thông báo
                                </label>
                                <input type="text" id="title" name="title"
                                       value="${editNotification != null ? editNotification.title : ''}"
                                       placeholder="Nhập tiêu đề thông báo" required>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="content">
                                    <i class="fas fa-align-left"></i>
                                    Nội dung thông báo
                                </label>
                                <textarea id="content" name="content"
                                          placeholder="Nhập nội dung thông báo" required>${editNotification != null ? editNotification.content : ''}</textarea>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="classRoomId">
                                    <i class="fas fa-users"></i>
                                    Lớp học
                                </label>
                                <select id="classRoomId" name="classRoomId" required>
                                    <option value="">Chọn lớp học</option>
                                    <c:forEach var="classRoom" items="${classRooms}">
                                        <option value="${classRoom.id}"
                                            ${(editNotification != null && classRoom.id == editNotification.classRoomId) ? 'selected' : ''}>
                                                ${classRoom.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="notificationType">
                                    <i class="fas fa-tags"></i>
                                    Loại thông báo
                                </label>
                                <select id="notificationType" name="notificationType">
                                    <option value="GENERAL"
                                    ${(editNotification != null && editNotification.notificationType == 'GENERAL') ? 'selected' : ''}>
                                        <i class="fas fa-info-circle"></i> Thông báo chung
                                    </option>
                                    <option value="URGENT"
                                    ${(editNotification != null && editNotification.notificationType == 'URGENT') ? 'selected' : ''}>
                                        <i class="fas fa-exclamation-triangle"></i> Khẩn cấp
                                    </option>
                                    <option value="ASSIGNMENT"
                                    ${(editNotification != null && editNotification.notificationType == 'ASSIGNMENT') ? 'selected' : ''}>
                                        <i class="fas fa-tasks"></i> Bài tập
                                    </option>
                                    <option value="EXAM"
                                    ${(editNotification != null && editNotification.notificationType == 'EXAM') ? 'selected' : ''}>
                                        <i class="fas fa-file-alt"></i> Kiểm tra
                                    </option>
                                </select>
                            </div>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-paper-plane"></i>
                                <c:choose>
                                    <c:when test="${editNotification != null}">
                                        Cập nhật thông báo
                                    </c:when>
                                    <c:otherwise>
                                        Gửi thông báo
                                    </c:otherwise>
                                </c:choose>
                            </button>

                            <c:if test="${editNotification != null}">
                                <a href="sendNotification" class="btn btn-secondary">
                                    <i class="fas fa-times"></i>
                                    Hủy chỉnh sửa
                                </a>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Search and Filter Section -->
            <div class="card">
                <div class="card-header">
                    <h3>
                        <i class="fas fa-search"></i>
                        Tìm kiếm và lọc thông báo
                    </h3>
                </div>
                <div class="card-body">
                    <form method="get" action="sendNotification" class="search-form">
                        <div class="search-row">
                            <div class="search-group">
                                <input type="text" name="search" value="${searchKeyword}"
                                       placeholder="Tìm kiếm theo tiêu đề hoặc nội dung">
                            </div>

                            <div class="search-group">
                                <input type="date" name="dateFrom" value="${dateFrom}"
                                       placeholder="Từ ngày">
                            </div>

                            <div class="search-group">
                                <input type="date" name="dateTo" value="${dateTo}"
                                       placeholder="Đến ngày">
                            </div>

                            <div class="search-group">
                                <select name="filterClassRoom">
                                    <option value="all">Tất cả lớp học</option>
                                    <c:forEach var="classRoom" items="${classRooms}">
                                        <option value="${classRoom.id}"
                                            ${filterClassRoom == classRoom.id.toString() ? 'selected' : ''}>
                                                ${classRoom.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="search-actions">
                                <button type="submit" class="btn btn-info">
                                    <i class="fas fa-search"></i>
                                    Tìm kiếm
                                </button>
                                <a href="sendNotification" class="btn btn-secondary">
                                    <i class="fas fa-refresh"></i>
                                    Xóa bộ lọc
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Notifications List Section -->
            <div class="card">
                <div class="card-header">
                    <h3>
                        <i class="fas fa-list"></i>
                        Danh sách thông báo đã gửi
                    </h3>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty notifications}">
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Tiêu đề</th>
                                        <th>Nội dung</th>
                                        <th>Lớp học</th>
                                        <th>Loại</th>
                                        <th>Ngày gửi</th>
                                        <th>Người gửi</th>
                                        <th>Thao tác</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="notification" items="${notifications}">
                                        <tr>
                                            <td>${notification.id}</td>
                                            <td class="notification-title">${notification.title}</td>
                                            <td class="notification-content">${notification.content}</td>
                                            <td>
                                                <c:forEach var="classRoom" items="${classRooms}">
                                                    <c:if test="${classRoom.id == notification.classRoomId}">
                                                        <span class="class-badge">${classRoom.name}</span>
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${notification.notificationType == 'GENERAL'}">
                                                                <span class="badge badge-general">
                                                                    <i class="fas fa-info-circle"></i>
                                                                    Thông báo chung
                                                                </span>
                                                    </c:when>
                                                    <c:when test="${notification.notificationType == 'URGENT'}">
                                                                <span class="badge badge-urgent">
                                                                    <i class="fas fa-exclamation-triangle"></i>
                                                                    Khẩn cấp
                                                                </span>
                                                    </c:when>
                                                    <c:when test="${notification.notificationType == 'ASSIGNMENT'}">
                                                                <span class="badge badge-assignment">
                                                                    <i class="fas fa-tasks"></i>
                                                                    Bài tập
                                                                </span>
                                                    </c:when>
                                                    <c:when test="${notification.notificationType == 'EXAM'}">
                                                                <span class="badge badge-exam">
                                                                    <i class="fas fa-file-alt"></i>
                                                                    Kiểm tra
                                                                </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                                <span class="badge badge-default">
                                                                        ${notification.notificationType}
                                                                </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${notification.createdAt}"
                                                                pattern="dd/MM/yyyy HH:mm"/>
                                            </td>
                                            <td>${notification.senderName}</td>
                                            <td>
                                                <div class="action-buttons">
                                                    <a href="sendNotification?action=edit&id=${notification.id}"
                                                       class="btn btn-sm btn-warning" title="Chỉnh sửa">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <a href="sendNotification?action=delete&id=${notification.id}"
                                                       class="btn btn-sm btn-danger" title="Xóa"
                                                       onclick="return confirm('Bạn có chắc muốn xóa thông báo này?')">
                                                        <i class="fas fa-trash"></i>
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-bell-slash"></i>
                                <p>Chưa có thông báo nào được gửi.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>
</div>

<!-- Footer -->
<%@ include file="../View/footer.jsp" %>

<script src="${pageContext.request.contextPath}/assets/js/teacher-send-notification.js"></script>
</body>
</html>