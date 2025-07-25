<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xem đơn từ - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-view-request.css">
</head>
<body>
<!-- Include Header -->
<%@ include file="../View/header.jsp" %>

<div class="main-container">
    <!-- Include Teacher Sidebar -->
    <div class="sidebar-col">
        <%@ include file="../View/teacher-sidebar.jsp" %>
    </div>

    <!-- Main Content -->
    <div class="content-col">
        <div class="content">
            <div class="page-header">
                <h1><i class="fas fa-eye"></i> Thông tin xử lý đơn từ</h1>
                <p>Xem tình trạng và kết quả xử lý các đơn từ đã gửi</p>
            </div>

            <!-- Success/Error Messages -->
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i>
                        ${success}
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                        ${error}
                </div>
            </c:if>

            <!-- Statistics Cards -->
            <div class="stats-section">
                <div class="stat-card">
                    <div class="stat-icon pending">
                        <i class="fas fa-clock"></i>
                    </div>
                    <div class="stat-info">
                        <h3>${pendingCount}</h3>
                        <p>Đang chờ xử lý</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon approved">
                        <i class="fas fa-check"></i>
                    </div>
                    <div class="stat-info">
                        <h3>${approvedCount}</h3>
                        <p>Đã duyệt</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon rejected">
                        <i class="fas fa-times"></i>
                    </div>
                    <div class="stat-info">
                        <h3>${rejectedCount}</h3>
                        <p>Bị từ chối</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon total">
                        <i class="fas fa-file-alt"></i>
                    </div>
                    <div class="stat-info">
                        <h3>${totalRequests}</h3>
                        <p>Tổng đơn từ</p>
                    </div>
                </div>
            </div>

            <!-- Filter Section -->
            <div class="filter-section">
                <form method="GET" action="${pageContext.request.contextPath}/teacherViewRequest">
                    <input type="hidden" name="action" value="filter">
                    <div class="filter-group">
                        <label for="requestType">Chọn loại đơn:</label>
                        <select id="requestType" name="requestType">
                            <option value="">Tất cả loại đơn</option>
                            <option value="Đơn xin nghỉ phép" ${selectedRequestType == 'Đơn xin nghỉ phép' ? 'selected' : ''}>Đơn xin nghỉ phép</option>
                            <option value="Đơn thay đổi lịch dạy" ${selectedRequestType == 'Đơn thay đổi lịch dạy' ? 'selected' : ''}>Đơn thay đổi lịch dạy</option>
                            <option value="Đơn khác" ${selectedRequestType == 'Đơn khác' ? 'selected' : ''}>Đơn khác</option>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label for="status">Trạng thái:</label>
                        <select id="status" name="status">
                            <option value="">Tất cả trạng thái</option>
                            <option value="Chờ xử lý" ${selectedStatus == 'Chờ xử lý' ? 'selected' : ''}>Chờ xử lý</option>
                            <option value="Đã duyệt" ${selectedStatus == 'Đã duyệt' ? 'selected' : ''}>Đã duyệt</option>
                            <option value="Từ chối" ${selectedStatus == 'Từ chối' ? 'selected' : ''}>Từ chối</option>
                        </select>
                    </div>
                    <button type="submit" class="btn-filter">
                        <i class="fas fa-filter"></i> Lọc
                    </button>
                </form>

                <form method="GET" action="${pageContext.request.contextPath}/teacherViewRequest">
                    <input type="hidden" name="action" value="search">
                    <div class="filter-group">
                        <label for="searchKeyword">
                            <i class="fas fa-search"></i>
                            <input type="text" id="searchKeyword" name="searchKeyword"
                                   placeholder="Tìm kiếm đơn từ..." value="${searchKeyword}">
                        </label>
                        <button type="submit" class="btn-search">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                </form>
            </div>

            <!-- Requests Table -->
            <div class="table-container">
                <c:choose>
                    <c:when test="${not empty requests}">
                        <table class="requests-table">
                            <thead>
                            <tr>
                                <th>Loại đơn</th>
                                <th>Lý do</th>
                                <th>Ngày gửi</th>
                                <th>Quá trình xử lý</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="request" items="${requests}">
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${request.typeName == 'Đơn xin nghỉ phép'}">
                                                <span class="request-type leave">${request.typeName}</span>
                                            </c:when>
                                            <c:when test="${request.typeName == 'Đơn thay đổi lịch dạy'}">
                                                <span class="request-type schedule-change">${request.typeName}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="request-type">${request.typeName}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="reason-cell">
                                        <span class="reason-text" title="${request.reason}">
                                                ${request.reason.length() > 50 ? request.reason.substring(0, 50).concat('...') : request.reason}
                                        </span>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${request.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="process-cell">
                                        <c:choose>
                                            <c:when test="${request.status == 'Chờ xử lý'}">
                                                <span class="process-text pending-status">
                                                <i class="fas fa-clock"></i>
                                                Hiện đang chờ xử lý...
                                                </span>
                                            </c:when>
                                            <c:when test="${request.status == 'Đã duyệt'}">
                                                <div class="process-text approved-status">
                                                    <c:if test="${not empty request.response}">
                                                        <div class="response-content">
                                                            <p class="response-text">${request.response}</p>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${empty request.response}">
                                                        <div class="response-content">
                                                            <small class="no-response">Không có phản hồi cụ thể</small>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </c:when>
                                            <c:when test="${request.status == 'Từ chối'}">
                                                <div class="process-text rejected-status">
                                                    <c:if test="${not empty request.response}">
                                                        <div class="response-content">
                                                            <p class="response-text">${request.response}</p>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${empty request.response}">
                                                        <div class="response-content">
                                                            <small class="no-response">Không có lý do cụ thể</small>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="process-text unknown-status">
                                                <c:if test="${not empty request.response}">
                                                    <p>${request.response}</p>
                                                </c:if>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="status ${request.status.toLowerCase()}">
                                            <c:choose>
                                                <c:when test="${request.status == 'Chờ xử lý'}">Đang xử lý</c:when>
                                                <c:when test="${request.status == 'Đã duyệt'}">Đã duyệt</c:when>
                                                <c:when test="${request.status == 'Từ chối'}">Bị từ chối</c:when>
                                                <c:otherwise>${request.status}</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <button class="btn-action view" onclick="viewRequestDetail(${request.id})">
                                                <i class="fas fa-eye"></i> Xem
                                            </button>
                                            <c:if test="${request.status == 'Chờ xử lý'}">
                                                <button class="btn-action delete" onclick="confirmDelete(${request.id})">
                                                    <i class="fas fa-trash"></i> Xóa
                                                </button>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="no-results">
                            <i class="fas fa-inbox"></i>
                            <p>Không có đơn từ nào được tìm thấy</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="?page=${currentPage - 1}" class="page-btn">
                            <i class="fas fa-chevron-left"></i>
                        </a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span class="page-btn active">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="?page=${i}" class="page-btn">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}" class="page-btn">
                            <i class="fas fa-chevron-right"></i>
                        </a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
</div>

<!-- Include Footer -->
<%@ include file="../View/footer.jsp" %>

<!-- Request Detail Modal -->
<div id="requestDetailModal" class="modal" style="${showDetailModal ? 'display: block;' : ''}">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Chi tiết đơn từ</h3>
            <span class="close" onclick="closeModal()">&times;</span>
        </div>
        <div class="modal-body">
            <div id="requestDetailContent">
                <c:if test="${not empty requestDetail}">
                    <div class="detail-section">
                        <h4>Thông tin đơn từ</h4>
                        <div class="detail-row">
                            <strong>Loại đơn:</strong> ${requestDetail.typeName}
                        </div>
                        <div class="detail-row">
                            <strong>Lý do:</strong> ${requestDetail.reason}
                        </div>
                        <div class="detail-row">
                            <strong>Ngày gửi:</strong>
                            <fmt:formatDate value="${requestDetail.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </div>
                        <div class="detail-row">
                            <strong>Trạng thái:</strong>
                            <span class="status ${requestDetail.status.toLowerCase()}">
                                <c:choose>
                                    <c:when test="${requestDetail.status == 'Chờ xử lý'}">Đang xử lý</c:when>
                                    <c:when test="${requestDetail.status == 'Đã duyệt'}">Đã duyệt</c:when>
                                    <c:when test="${requestDetail.status == 'Từ chối'}">Bị từ chối</c:when>
                                    <c:otherwise>${requestDetail.status}</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <c:if test="${not empty requestDetail.response}">
                            <div class="detail-row">
                                <strong>Phản hồi:</strong> ${requestDetail.response}
                            </div>
                        </c:if>
                        <c:if test="${requestDetail.responseAt != null}">
                            <div class="detail-row">
                                <strong>Ngày phản hồi:</strong>
                                <fmt:formatDate value="${requestDetail.responseAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                            </div>
                        </c:if>
                        <c:if test="${not empty processorName}">
                            <div class="detail-row">
                                <strong>Người xử lý:</strong> ${processorName}
                            </div>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn-secondary" onclick="closeModal()">Đóng</button>
        </div>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Xác nhận xóa</h3>
            <span class="close" onclick="closeDeleteModal()">&times;</span>
        </div>
        <div class="modal-body">
            <p>Bạn có chắc chắn muốn xóa đơn từ này không?</p>
            <p class="warning-text">Chỉ có thể xóa đơn từ đang ở trạng thái "Đang xử lý".</p>
        </div>
        <div class="modal-footer">
            <button class="btn-secondary" onclick="closeDeleteModal()">Hủy</button>
            <button class="btn-danger" onclick="deleteRequest()">Xóa</button>
        </div>
    </div>
</div>

<script>
    let requestToDelete = null;

    function viewRequestDetail(requestId) {
        window.location.href = '${pageContext.request.contextPath}/teacherViewRequest?action=viewDetail&requestId=' + requestId;
    }

    function confirmDelete(requestId) {
        requestToDelete = requestId;
        document.getElementById('deleteModal').style.display = 'block';
    }

    function deleteRequest() {
        if (requestToDelete) {
            window.location.href = '${pageContext.request.contextPath}/teacherViewRequest?action=delete&requestId=' + requestToDelete;
        }
    }

    function closeModal() {
        document.getElementById('requestDetailModal').style.display = 'none';
        // Remove the showDetailModal parameter from URL
        const url = new URL(window.location);
        url.searchParams.delete('action');
        url.searchParams.delete('requestId');
        window.history.replaceState({}, document.title, url.pathname);
    }

    function closeDeleteModal() {
        document.getElementById('deleteModal').style.display = 'none';
        requestToDelete = null;
    }

    // Close modals when clicking outside
    window.onclick = function(event) {
        const detailModal = document.getElementById('requestDetailModal');
        const deleteModal = document.getElementById('deleteModal');

        if (event.target == detailModal) {
            closeModal();
        }
        if (event.target == deleteModal) {
            closeDeleteModal();
        }
    }

    // Auto-hide success/error messages
    document.addEventListener('DOMContentLoaded', function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            setTimeout(function() {
                alert.style.opacity = '0';
                setTimeout(function() {
                    alert.style.display = 'none';
                }, 300);
            }, 5000);
        });
    });
</script>
</body>
</html>