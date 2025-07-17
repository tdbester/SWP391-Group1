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
                    <p class="page-subtitle">Danh sách các yêu cầu đã được xử lý và phê duyệt</p>
                </div>

                <!-- Filter Section -->
                <div class="filter-container ${(currentFilter.typeName != 'all' || currentFilter.dateFrom != '' || currentFilter.dateTo != '' || currentFilter.searchKeyword != '') ? 'filter-active' : ''}">
                    <input type="checkbox" id="filterToggle" class="filter-toggle" ${(currentFilter.typeName != 'all' || currentFilter.dateFrom != '' || currentFilter.dateTo != '' || currentFilter.searchKeyword != '') ? 'checked' : ''}>
                    <div class="filter-content">
                        <form method="GET" action="teacher-notification">
                            <div class="filter-row">
                                <div class="filter-group">
                                    <label for="typeName">Loại yêu cầu:</label>
                                    <select name="typeName" id="typeName">
                                        <option value="all">Tất cả loại</option>
                                        <c:forEach items="${requestTypes}" var="type">
                                            <option value="${type.typeName}"
                                                ${currentFilter.typeName == type.typeName ? 'selected' : ''}>
                                                    ${type.typeName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

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
                                           placeholder="Tìm trong nội dung, phản hồi...">
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

                <!-- Request List -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${not empty approvedRequests}">
                            <table class="requests-table">
                                <thead>
                                <tr>
                                    <th>Loại yêu cầu</th>
                                    <th>Người gửi</th>
                                    <th>Nội dung</th>
                                    <th>Người xử lý</th>
                                    <th>Ngày phản hồi</th>
                                    <th>Trạng thái</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${approvedRequests}" var="request">
                                    <tr>
                                        <td>
                                            <span class="type-badge">${request.typeName}</span>
                                        </td>
                                        <td>${request.senderName}</td>
                                        <td>
                                            <div style="max-width: 200px; overflow: hidden; text-overflow: ellipsis;">
                                                    ${fn:substring(request.reason, 0, 50)}
                                                <c:if test="${fn:length(request.reason) > 50}">...</c:if>
                                            </div>
                                        </td>
                                        <td>${request.processedByName}</td>
                                        <td>
                                            <fmt:formatDate value="${request.responseAt}" pattern="dd/MM/yyyy HH:mm"/>
                                        </td>
                                        <td>
                                            <span class="status-badge status-approved">Đã Duyệt</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-inbox"></i>
                                <h3>Không tìm thấy yêu cầu nào</h3>
                                <p>Hãy thử điều chỉnh bộ lọc hoặc <a href="teacher-notification">xóa bộ lọc</a> để xem tất cả.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:url value="teacher-notification" var="baseUrl">
                            <c:param name="typeName" value="${currentFilter.typeName}"/>
                            <c:param name="dateFrom" value="${currentFilter.dateFrom}"/>
                            <c:param name="dateTo" value="${currentFilter.dateTo}"/>
                            <c:param name="searchKeyword" value="${currentFilter.searchKeyword}"/>
                        </c:url>

                        <c:if test="${currentPage > 1}">
                            <a href="${baseUrl}&page=1" class="btn btn-outline btn-sm">
                                <i class="fas fa-angle-double-left"></i>
                                Đầu
                            </a>
                            <a href="${baseUrl}&page=${currentPage - 1}" class="btn btn-outline btn-sm">
                                <i class="fas fa-angle-left"></i>
                                Trước
                            </a>
                        </c:if>

                        <c:forEach begin="${currentPage - 2 > 1 ? currentPage - 2 : 1}"
                                   end="${currentPage + 2 < totalPages ? currentPage + 2 : totalPages}"
                                   var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="btn btn-primary btn-sm">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="${baseUrl}&page=${i}" class="btn btn-outline btn-sm">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="${baseUrl}&page=${currentPage + 1}" class="btn btn-outline btn-sm">
                                Tiếp
                                <i class="fas fa-angle-right"></i>
                            </a>
                            <a href="${baseUrl}&page=${totalPages}" class="btn btn-outline btn-sm">
                                Cuối
                                <i class="fas fa-angle-double-right"></i>
                            </a>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </main>

        <!-- Include Footer -->
        <jsp:include page="footer.jsp" />
    </div>
</div>
</body>
</html>