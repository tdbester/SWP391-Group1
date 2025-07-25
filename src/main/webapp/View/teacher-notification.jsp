<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông báo - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-notification.css">
</head>
<body>
<!-- Include Header -->
<jsp:include page="header.jsp" />
<div class="main-layout">
    <!-- Include Sidebar -->
    <jsp:include page="teacher-sidebar.jsp" />
    <div class="content-area">
        <!-- Main Content -->
        <main class="main-content">
            <div class="notification-container">
                <!-- Page Header -->
                <div class="page-header">
                    <h1>
                        <i class="fas fa-bell"></i>
                        Thông báo
                    </h1>
                </div>

                <!-- Filter Section -->
                <div class="filter-container ${(currentFilter.dateFrom != '' || currentFilter.dateTo != '' || currentFilter.searchKeyword != '') ? 'filter-active' : ''}">
                    <input type="checkbox" id="filterToggle" class="filter-toggle" ${(currentFilter.dateFrom != '' || currentFilter.dateTo != '' || currentFilter.searchKeyword != '') ? 'checked' : ''}>
                    <div class="filter-content">
                        <form method="GET" action="teacher-notification">
                            <div class="filter-row">
                                <div class="filter-group">
                                    <label for="dateFrom">Từ ngày:</label>
                                    <input type="date" name="dateFrom" id="dateFrom"
                                           value="${currentFilter.dateFrom}">
                                </div>

                                <div class="filter-group">
                                    <label for="dateTo">Đến ngày:</label>
                                    <input type="date" name="dateTo" id="dateTo"
                                           value="${currentFilter.dateTo}">
                                </div>
                            </div>

                            <div class="filter-row">
                                <div class="filter-group search-group">
                                    <label for="searchKeyword">Tìm kiếm:</label>
                                    <input type="text" name="searchKeyword" id="searchKeyword"
                                           value="${currentFilter.searchKeyword}"
                                           placeholder="Tìm trong nội dung, thông báo...">
                                </div>
                            </div>

                            <div class="filter-actions">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search"></i>
                                    Lọc
                                </button>
                                <a href="teacher-notification" class="btn btn-secondary">
                                    <i class="fas fa-times"></i>
                                    Xóa bộ lọc
                                </a>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Notification List -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${not empty allNotifications}">
                            <table class="requests-table">
                                <thead>
                                <tr>
                                    <th>Loại</th>
                                    <th>Nội Dung</th>
                                    <th>Thông báo</th>
                                    <th>Người xử lý</th>
                                    <th>Ngày phản hồi</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${allNotifications}" var="notification">
                                    <tr>
                                        <td>
                                            <c:choose>
                                                <c:when test="${notification.type == 'SALARY'}">
                                                    <span class="type-badge salary-badge">
                                                        <i class="fas fa-money-bill-wave"></i>
                                                        Lương
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="type-badge request-badge">
                                                        <i class="fas fa-file-alt"></i>
                                                        Yêu cầu
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="content-cell">
                                                <c:choose>
                                                    <c:when test="${fn:length(notification.content) > 80}">
                                                        ${fn:substring(notification.content, 0, 80)}...
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${notification.content}
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="notification-cell">
                                                <c:choose>
                                                    <c:when test="${fn:length(notification.notification) > 50}">
                                                        ${fn:substring(notification.notification, 0, 50)}...
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${notification.notification}
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="processed-by">
                                                    ${notification.processedBy}
                                            </span>
                                        </td>
                                        <td>
                                            <span class="response-date">
                                                <fmt:formatDate value="${notification.responseDate}" pattern="dd/MM/yyyy HH:mm"/>
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-inbox"></i>
                                <h3>Không tìm thấy thông báo nào</h3>
                                <p>Hãy thử điều chỉnh bộ lọc hoặc <a href="teacher-notification">xóa bộ lọc</a> để xem tất cả.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Summary Info -->
                <c:if test="${not empty allNotifications}">
                    <div class="summary-info">
                        <p>
                            <i class="fas fa-info-circle"></i>
                            Tổng cộng: <strong>${filteredCount}</strong> thông báo
                        </p>
                    </div>
                </c:if>
            </div>
        </main>

        <!-- Include Footer -->
        <jsp:include page="footer.jsp" />
    </div>
</div>

<style>
    .salary-badge {
        background-color: #28a745;
        color: white;
        padding: 4px 8px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 500;
        display: inline-flex;
        align-items: center;
        gap: 4px;
    }

    .request-badge {
        background-color: #007bff;
        color: white;
        padding: 4px 8px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 500;
        display: inline-flex;
        align-items: center;
        gap: 4px;
    }

    .content-cell {
        max-width: 300px;
        word-wrap: break-word;
        line-height: 1.4;
    }

    .notification-cell {
        max-width: 250px;
        word-wrap: break-word;
        line-height: 1.4;
    }

    .processed-by {
        font-weight: 500;
        color: #495057;
    }

    .response-date {
        color: #6c757d;
        font-size: 14px;
    }

    .summary-info {
        margin-top: 16px;
        padding: 12px;
        background-color: #f8f9fa;
        border-radius: 6px;
        color: #495057;
    }

    .summary-info i {
        color: #007bff;
        margin-right: 8px;
    }

    .requests-table th:first-child {
        width: 80px;
    }

    .requests-table th:nth-child(2) {
        width: 30%;
    }

    .requests-table th:nth-child(3) {
        width: 25%;
    }

    .requests-table th:nth-child(4) {
        width: 15%;
    }

    .requests-table th:nth-child(5) {
        width: 15%;
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
        .content-cell, .notification-cell {
            max-width: 200px;
        }

        .requests-table th:nth-child(2),
        .requests-table th:nth-child(3) {
            width: auto;
            min-width: 150px;
        }
    }
</style>

</body>
</html>