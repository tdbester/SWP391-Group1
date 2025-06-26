<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Điểm Danh - Talent Center</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-attendance.css">
</head>
<body>
<!-- Include Header -->
<%@ include file="header.jsp" %>

<div class="main-container">
    <!-- Include Sidebar -->
    <%@ include file="teacher-sidebar.jsp" %>

    <!-- Main Content -->
    <div class="content">
        <div class="content-header">
            <h1><i class="fas fa-user-check"></i> Điểm Danh</h1>
            <div class="date-info">
                <i class="fas fa-calendar-day"></i>
                <span>Hôm nay: <fmt:formatDate value="${currentDate}" pattern="dd/MM/yyyy"/></span>
            </div>
        </div>

        <!-- Alert Messages -->
        <c:if test="${not empty success}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                    ${success}
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-triangle"></i>
                    ${error}
            </div>
        </c:if>

        <!-- Classes Grid -->
        <div class="classes-section">
            <div class="section-header">
                <h2>Danh Sách Lớp Học</h2>
                <div class="view-toggle">
                    <form method="get" action="attendance" style="display:inline;">
                        <input type="hidden" name="action" value="today">
                        <button type="submit" class="toggle-btn ${viewType == 'today' ? 'active' : ''}">
                            <i class="fas fa-calendar-day"></i> Lớp hôm nay
                        </button>
                    </form>
                    <form method="get" action="attendance" style="display:inline;">
                        <input type="hidden" name="action" value="list">
                        <button type="submit" class="toggle-btn ${viewType == 'all' ? 'active' : ''}">
                            <i class="fas fa-list"></i> Tất cả lớp
                        </button>
                    </form>
                </div>
            </div>

            <div class="classes-grid">
                <c:choose>
                    <c:when test="${not empty todayClasses}">
                        <c:forEach var="classRoom" items="${todayClasses}" varStatus="status">
                            <div class="class-card">
                                <div class="class-header">
                                    <h3>${classRoom.name}</h3>
                                    <div class="class-status">
                                        <span class="status-badge today">Hôm nay</span>
                                    </div>
                                </div>
                                <div class="class-info">
                                    <div class="info-item">
                                        <i class="fas fa-book"></i>
                                        <span>${classRoom.courseTitle}</span>
                                    </div>
                                    <div class="info-item">
                                        <i class="fas fa-clock"></i>
                                        <span>${classRoom.startTime} - ${classRoom.endTime}</span>
                                    </div>
                                    <div class="info-item">
                                        <i class="fas fa-users"></i>
                                        <span>${classRoom.studentCount} học sinh</span>
                                    </div>
                                </div>
                                <div class="class-actions">
                                    <form method="get" action="attendance" style="display:inline;">
                                        <input type="hidden" name="action" value="take">
                                        <input type="hidden" name="classId" value="${classRoom.id}">
                                        <input type="hidden" name="date" value="${currentDate}">
                                        <button type="submit" class="btn-primary">
                                            <i class="fas fa-user-check"></i> Điểm danh
                                        </button>
                                    </form>

                                    <form method="get" action="attendance" style="display:inline;">
                                        <input type="hidden" name="action" value="history">
                                        <input type="hidden" name="classId" value="${classRoom.id}">
                                        <button type="submit" class="btn-secondary">
                                            <i class="fas fa-history"></i> Lịch sử
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:when test="${not empty allClasses}">
                        <c:forEach var="classRoom" items="${allClasses}" varStatus="status">
                            <div class="class-card">
                                <div class="class-header">
                                    <h3>${classRoom.name}</h3>
                                </div>
                                <div class="class-info">
                                    <div class="info-item">
                                        <i class="fas fa-book"></i>
                                        <span>${classRoom.courseTitle}</span>
                                    </div>
                                    <div class="info-item">
                                        <i class="fas fa-clock"></i>
                                        <span>${classRoom.startTime} - ${classRoom.endTime}</span>
                                    </div>
                                    <div class="info-item">
                                        <i class="fas fa-users"></i>
                                        <span>${classRoom.studentCount} học sinh</span>
                                    </div>
                                </div>
                                <div class="class-actions">
                                    <form method="get" action="attendance" style="display:inline;">
                                        <input type="hidden" name="action" value="take">
                                        <input type="hidden" name="classId" value="${classRoom.id}">
                                        <input type="hidden" name="date" value="${currentDate}">
                                        <button type="submit" class="btn-primary">
                                            <i class="fas fa-user-check"></i> Điểm danh
                                        </button>
                                    </form>

                                    <form method="get" action="attendance" style="display:inline;">
                                        <input type="hidden" name="action" value="history">
                                        <input type="hidden" name="classId" value="${classRoom.id}">
                                        <button type="submit" class="btn-secondary">
                                            <i class="fas fa-history"></i> Lịch sử
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-classes">
                            <i class="fas fa-calendar-times"></i>
                            <h3>Không có lớp học nào</h3>
                            <p>Bạn chưa được phân công lớp học nào hoặc không có lớp học hôm nay.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Attendance Form Section -->
        <c:if test="${not empty students}">
            <div class="attendance-section">
                <div class="section-header">
                    <h2>Điểm danh lớp ${classRoom.name}</h2>
                    <form method="get" action="attendance" class="attendance-date">
                        <label>
                            <i class="fas fa-calendar"></i> Ngày điểm danh:
                        </label>
                        <input type="date" name="date" value="${param.date}" onchange="this.form.submit()">
                        <input type="hidden" name="action" value="take">
                        <input type="hidden" name="classId" value="${classRoom.id}">
                    </form>
                </div>

                <!-- Students Table -->
                <div class="students-table-container">
                    <form method="post" action="attendance">
                        <input type="hidden" name="action" value="save">
                        <input type="hidden" name="scheduleId" value="${scheduleId}">
                        <input type="hidden" name="classRoomId" value="${classRoom.id}">

                        <div class="table-controls">
                            <div class="batch-actions">
                                <button type="button" class="btn-batch" onclick="document.querySelectorAll('select[name^=status]').forEach(s => s.value = 'Present')">
                                    <i class="fas fa-check-double"></i> Có mặt tất cả
                                </button>
                                <button type="button" class="btn-batch" onclick="document.querySelectorAll('select[name^=status]').forEach(s => s.value = 'Absent')">
                                    <i class="fas fa-times-circle"></i> Vắng tất cả
                                </button>
                            </div>
                            <div class="search-box">
                                <i class="fas fa-search"></i>
                                <input type="text" placeholder="Tìm kiếm học sinh..." onkeyup="filterStudents(this.value)">
                            </div>
                        </div>

                        <div class="table-wrapper">
                            <table class="students-table">
                                <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Họ và Tên</th>
                                    <th>ID</th>
                                    <th>Số buổi đi học</th>
                                    <th>Số buổi vắng</th>
                                    <th>Điểm danh</th>
                                    <th>Ghi chú</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="student" items="${students}" varStatus="status">
                                    <tr>
                                        <td>
                                                ${status.index + 1}
                                            <input type="hidden" name="studentId[]" value="${student.id}">
                                        </td>
                                        <td>${student.fullName}</td>
                                        <td>${student.id}</td>
                                        <td>${student.presentCount}</td>
                                        <td>${student.absentCount}</td>
                                        <td>
                                            <select name="status_${student.id}" class="status-select">
                                                <option value="Present">Có mặt</option>
                                                <option value="Absent">Vắng</option>
                                                <option value="Late">Đi muộn</option>
                                            </select>
                                        </td>
                                        <td>
                                            <input type="text" name="note_${student.id}" class="note-input" placeholder="Ghi chú...">
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="form-actions">
                            <a href="attendance" class="btn-cancel">
                                <i class="fas fa-times"></i> Hủy
                            </a>
                            <button type="submit" class="btn-save">
                                <i class="fas fa-save"></i> Lưu điểm danh
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>

        <!-- Edit Attendance Section -->
        <c:if test="${isEdit}">
            <div class="attendance-section">
                <div class="section-header">
                    <h2><i class="fas fa-edit"></i> Chỉnh sửa điểm danh</h2>
                </div>

                <div class="students-table-container">
                    <form method="post" action="attendance">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="scheduleId" value="${scheduleId}">

                        <div class="table-wrapper">
                            <table class="students-table">
                                <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Họ và Tên</th>
                                    <th>Trạng thái hiện tại</th>
                                    <th>Điểm danh mới</th>
                                    <th>Ghi chú</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="attendance" items="${attendances}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${attendance.studentName}</td>
                                        <td>
                                                    <span class="status-badge ${attendance.status.toLowerCase()}">
                                                        <c:choose>
                                                            <c:when test="${attendance.status == 'Present'}">Có mặt</c:when>
                                                            <c:when test="${attendance.status == 'Absent'}">Vắng</c:when>
                                                            <c:when test="${attendance.status == 'Late'}">Đi muộn</c:when>
                                                        </c:choose>
                                                    </span>
                                        </td>
                                        <td>
                                            <input type="hidden" name="attendanceId" value="${attendance.id}">
                                            <select name="status_${attendance.id}" class="status-select">
                                                <option value="Present" ${attendance.status == 'Present' ? 'selected' : ''}>Có mặt</option>
                                                <option value="Absent" ${attendance.status == 'Absent' ? 'selected' : ''}>Vắng</option>
                                                <option value="Late" ${attendance.status == 'Late' ? 'selected' : ''}>Đi muộn</option>
                                            </select>
                                        </td>
                                        <td>
                                            <input type="text" name="note_${attendance.id}"
                                                   value="${attendance.note}" class="note-input"
                                                   placeholder="Ghi chú...">
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="form-actions">
                            <a href="javascript:history.back()" class="btn-cancel">
                                <i class="fas fa-arrow-left"></i> Quay lại
                            </a>
                            <button type="submit" class="btn-save">
                                <i class="fas fa-save"></i> Cập nhật
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>

        <!-- History Section -->
        <c:if test="${viewType == 'history'}">
            <div class="attendance-section">
                <div class="section-header">
                    <h2><i class="fas fa-history"></i> Lịch sử điểm danh</h2>
                </div>

                <div class="students-table-container">
                    <div class="table-wrapper">
                        <table class="students-table">
                            <thead>
                            <tr>
                                <th>STT</th>
                                <th>Ngày</th>
                                <th>Học sinh</th>
                                <th>Trạng thái</th>
                                <th>Ghi chú</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="attendance" items="${attendanceHistory}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td><fmt:formatDate value="${attendance.date}" pattern="dd/MM/yyyy"/></td>
                                    <td>${attendance.studentName}</td>
                                    <td>
                                                <span class="status-badge ${attendance.status.toLowerCase()}">
                                                    <c:choose>
                                                        <c:when test="${attendance.status == 'Present'}">Có mặt</c:when>
                                                        <c:when test="${attendance.status == 'Absent'}">Vắng</c:when>
                                                        <c:when test="${attendance.status == 'Late'}">Đi muộn</c:when>
                                                    </c:choose>
                                                </span>
                                    </td>
                                    <td>${attendance.note}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="form-actions">
                        <a href="attendance" class="btn-cancel">
                            <i class="fas fa-arrow-left"></i> Quay lại
                        </a>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>

<!-- Include Footer -->
<%@ include file="footer.jsp" %>

</body>
</html>