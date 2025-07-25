<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý lớp học - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/training-manager-view-class.css">
</head>
<body>
<!-- Include Header -->
<%@ include file="../View/header.jsp" %>

<!-- Main Dashboard Container -->
<div class="dashboard">
    <!-- Include Training Manager Sidebar -->
    <%@ include file="../View/training-manager-sidebar.jsp" %>

    <!-- Main Content Area -->
    <div class="main-content">
        <div class="content-wrapper">
            <!-- Page Header -->
            <div class="page-header">
                <h2><i class="fas fa-chalkboard-teacher"></i>Quản lý lớp học</h2>
                <p>Xem và quản lý danh sách các lớp học trong hệ thống</p>
            </div>

            <!-- Display error/success messages -->
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger alert-dismissible">
                    <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
                    <% session.removeAttribute("error"); %>
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-success alert-dismissible">
                    <i class="fas fa-check-circle"></i> ${sessionScope.message}
                    <% session.removeAttribute("message"); %>
                </div>
            </c:if>

            <!-- Form tìm kiếm và lọc -->
            <div class="search-form">
                <form method="GET" action="${pageContext.request.contextPath}/training-manager-view-class">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label for="courseSearch" class="form-label">
                                <i class="fas fa-book"></i>Tên khóa học
                            </label>
                            <input type="text"
                                   class="form-control"
                                   id="courseSearch"
                                   name="courseSearch"
                                   value="${param.courseSearch}"
                                   placeholder="Nhập tên khóa học...">
                        </div>
                        <div class="col-md-4">
                            <label for="classSearch" class="form-label">
                                <i class="fas fa-users"></i>Tên lớp học
                            </label>
                            <input type="text"
                                   class="form-control"
                                   id="classSearch"
                                   name="classSearch"
                                   value="${param.classSearch}"
                                   placeholder="Nhập tên lớp học...">
                        </div>
                        <div class="col-md-4 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary me-2">
                                <i class="fas fa-search"></i>Tìm kiếm
                            </button>
                            <a href="${pageContext.request.contextPath}/training-manager-view-class"
                               class="btn btn-outline-secondary">
                                <i class="fas fa-undo"></i>Đặt lại
                            </a>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Bảng danh sách lớp học -->
            <div class="table-container">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h5 class="mb-0">
                        <i class="fas fa-list"></i>Danh sách lớp học
                        <span class="badge bg-primary ms-2">${totalClassrooms} lớp</span>
                    </h5>
                </div>

                <c:if test="${empty classrooms}">
                    <div class="empty-state">
                        <i class="fas fa-inbox"></i>
                        <h5>Không tìm thấy lớp học nào</h5>
                        <p>Thử thay đổi điều kiện tìm kiếm hoặc tạo lớp học mới</p>
                    </div>
                </c:if>

                <c:if test="${not empty classrooms}">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                            <tr>
                                <th style="width: 50px;">STT</th>
                                <th style="width: 200px;">Khóa học</th>
                                <th style="width: 150px;">Lớp học</th>
                                <th style="width: 150px;">Giáo viên</th>
                                <th style="width: 100px;">Phòng học</th>
                                <th style="width: 100px;">Slot</th>
                                <th style="width: 120px;">Ngày bắt đầu</th>
                                <th style="width: 120px;">Ngày kết thúc</th>
                                <th style="width: 150px;">Thao tác</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="classroom" items="${classrooms}" varStatus="status">
                                <tr class="fade-in">
                                    <td class="text-center fw-bold">${(currentPage - 1) * 10 + status.index + 1}</td>
                                    <td>
                                    <div class="course-title">
                                        <a href="${pageContext.request.contextPath}/TrainingManagerClassroom?id=${classroom.id}"
                                           style="text-decoration: none; color: inherit;">
                                                ${classroom.courseTitle}
                                        </a>
                                    </div>
                                    <td>
                                        <div class="class-name">${classroom.name}</div>
                                        <small class="student-count">
                                            <i class="fas fa-users"></i>${classroom.studentCount} học viên
                                        </small>
                                    </td>
                                    <td>
                                        <div class="teacher-name">${classroom.teacherName}</div>
                                    </td>

                                    <td>
                                        <div class="room-name">
                                            <c:choose>
                                                <c:when test="${not empty classroom.roomCode}">
                                                    ${classroom.roomCode}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Chưa xác định</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>

                                    <td class="text-center">
                                        <span class="badge-slot">
                                            <c:if test="${not empty classroom.startTimeAsDate and not empty classroom.endTimeAsDate}">
                                                <fmt:formatDate value="${classroom.startTimeAsDate}" pattern="HH:mm"/> -
                                                <fmt:formatDate value="${classroom.endTimeAsDate}" pattern="HH:mm"/>
                                            </c:if>
                                            <c:if test="${empty classroom.startTimeAsDate or empty classroom.endTimeAsDate}">
                                                <span class="date-undefined">Chưa xác định</span>
                                            </c:if>
                                        </span>
                                    </td>

                                    <td class="text-center">
                                        <c:if test="${not empty classroom.startDateAsDate}">
                                            <span class="date-display">
                                                <fmt:formatDate value="${classroom.startDateAsDate}" pattern="dd/MM/yyyy"/>
                                            </span>
                                        </c:if>
                                        <c:if test="${empty classroom.startDateAsDate}">
                                            <span class="date-undefined">Chưa xác định</span>
                                        </c:if>
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${not empty classroom.endDateAsUtilDate}">
                                                <span class="date-display">
                                                    <fmt:formatDate value="${classroom.endDateAsUtilDate}" pattern="dd/MM/yyyy"/>
                                                </span>
                                            </c:when>
                                            <c:when test="${not empty classroom.endDateAsDate}">
                                                <span class="date-display">
                                                    <fmt:formatDate value="${classroom.endDateAsDate}" pattern="dd/MM/yyyy"/>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="date-undefined">Chưa xác định</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <!-- Button Edit - Show Modal -->
                                        <button type="button"
                                                class="btn btn-warning btn-action edit-btn"
                                                title="Chỉnh sửa"
                                                data-classroom-id="${classroom.id}"
                                                data-classroom-name="${classroom.name}"
                                                data-course-id="${classroom.courseId}"
                                                data-teacher-id="${classroom.teacherId}"
                                                data-slot-id="${classroom.slotId}"
                                                data-room-id="${classroom.roomId}"
                                                data-start-date="<fmt:formatDate value='${classroom.startDateAsDate}' pattern='yyyy-MM-dd'/>"
                                                data-end-date="<fmt:formatDate value='${classroom.endDateAsDate}' pattern='yyyy-MM-dd'/>"
                                                data-days-of-week="${classroom.daysOfWeekString}"
                                                data-class-started="${classroom.classStarted}"
                                                onclick="showEditModal(this)">
                                            <i class="fas fa-edit"></i> Sửa
                                        </button>

                                        <!-- Form Delete -->
                                        <form method="POST"
                                              action="${pageContext.request.contextPath}/training-manager-delete-classroom"
                                              style="display: inline-block;"
                                              onsubmit="return confirm('Bạn có chắc chắn muốn xóa lớp học ${classroom.name}?')">
                                            <input type="hidden" name="classroomId" value="${classroom.id}">
                                            <input type="hidden" name="courseSearch" value="${param.courseSearch}">
                                            <input type="hidden" name="classSearch" value="${param.classSearch}">
                                            <input type="hidden" name="page" value="${currentPage}">
                                            <button type="submit"
                                                    class="btn btn-danger btn-action"
                                                    title="Xóa">
                                                <i class="fas fa-trash"></i> Xóa
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>

                <!-- Phân trang -->
                <c:if test="${totalPages > 1}">
                    <nav aria-label="Phân trang lớp học" class="mt-4">
                        <ul class="pagination justify-content-center">
                            <c:if test="${currentPage > 1}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="?page=${currentPage - 1}&courseSearch=${param.courseSearch}&classSearch=${param.classSearch}">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </li>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link"
                                       href="?page=${i}&courseSearch=${param.courseSearch}&classSearch=${param.classSearch}">${i}</a>
                                </li>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="?page=${currentPage + 1}&courseSearch=${param.courseSearch}&classSearch=${param.classSearch}">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </c:if>

                <!-- Button tạo lớp học mới -->
                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/CreateClassRoomServlet"
                       class="btn btn-create">
                        <i class="fas fa-plus"></i>Tạo lớp học mới
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Edit Modal -->
<div id="editModal" class="modal-overlay">
    <div class="modal-content-custom" style="max-width: 800px;">
        <div class="modal-header-custom">
            <h4><i class="fas fa-edit"></i>Chỉnh sửa lớp học</h4>
            <button type="button" class="close-btn" onclick="hideEditModal()">&times;</button>
        </div>

        <!-- Form cập nhật trong modal -->
        <form method="POST" action="${pageContext.request.contextPath}/training-manager-update-classroom">
            <!-- Hidden fields to preserve search parameters -->
            <input type="hidden" name="courseSearch" value="${param.courseSearch}">
            <input type="hidden" name="classSearch" value="${param.classSearch}">
            <input type="hidden" name="page" value="${currentPage}">
            <input type="hidden" name="classroomId" id="editClassroomId">
            <input type="hidden" name="courseId" id="editCourseId">

            <div class="form-row">
                <div class="form-group full-width">
                    <label for="editName" class="form-label">
                        <i class="fas fa-tag"></i>Tên lớp học
                    </label>
                    <input type="text"
                           class="form-control"
                           id="editName"
                           name="name"
                           required
                           maxlength="100"
                           placeholder="Nhập tên lớp học...">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group half-width">
                    <label for="editTeacherId" class="form-label">
                        <i class="fas fa-user-tie"></i>Giáo viên
                    </label>
                    <select class="form-control" id="editTeacherId" name="teacherId" required>
                        <option value="">-- Chọn giáo viên --</option>
                        <c:forEach var="teacher" items="${teachers}">
                            <option value="${teacher.id}">${teacher.fullName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group half-width">
                    <label for="editSlotId" class="form-label">
                        <i class="fas fa-clock"></i>Slot thời gian
                    </label>
                    <select class="form-control" id="editSlotId" name="slotId" required>
                        <option value="">-- Chọn slot --</option>
                        <c:forEach var="slot" items="${slots}">
                            <option value="${slot.id}">
                                    ${slot.timeRange}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group half-width">
                    <label for="editRoomId" class="form-label">
                        <i class="fas fa-door-open"></i>Phòng học
                    </label>
                    <select class="form-control" id="editRoomId" name="roomId" required>
                        <option value="">-- Chọn phòng --</option>
                        <c:forEach var="room" items="${rooms}">
                            <option value="${room.id}">${room.code}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <!-- Thứ trong tuần -->
            <div class="form-row">
                <div class="form-group full-width">
                    <label class="form-label">
                        <i class="fas fa-calendar-week"></i>Thứ trong tuần
                    </label>
                    <div class="days-checkbox-group">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="daysOfWeek" value="1" id="editMon">
                            <label class="form-check-label" for="editMon">Thứ 2</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="daysOfWeek" value="2" id="editTue">
                            <label class="form-check-label" for="editTue">Thứ 3</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="daysOfWeek" value="3" id="editWed">
                            <label class="form-check-label" for="editWed">Thứ 4</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="daysOfWeek" value="4" id="editThu">
                            <label class="form-check-label" for="editThu">Thứ 5</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="daysOfWeek" value="5" id="editFri">
                            <label class="form-check-label" for="editFri">Thứ 6</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="daysOfWeek" value="6" id="editSat">
                            <label class="form-check-label" for="editSat">Thứ 7</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="daysOfWeek" value="7" id="editSun">
                            <label class="form-check-label" for="editSun">Chủ nhật</label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Ngày bắt đầu và kết thúc -->
            <div class="form-row">
                <div class="form-group half-width">
                    <label for="editStartDate" class="form-label">
                        <i class="fas fa-calendar-alt"></i>Ngày bắt đầu
                    </label>
                    <input type="date"
                           class="form-control"
                           id="editStartDate"
                           name="startDate"
                           required>
                    <small class="text-warning" id="startDateWarning" style="display: none;">
                        <i class="fas fa-exclamation-triangle"></i>
                        Lớp học đã bắt đầu, không thể thay đổi ngày bắt đầu
                    </small>
                </div>

                <div class="form-group half-width">
                    <label for="editEndDate" class="form-label">
                        <i class="fas fa-calendar-alt"></i>Ngày kết thúc
                    </label>
                    <input type="date"
                           class="form-control"
                           id="editEndDate"
                           name="endDate"
                           required>
                </div>
            </div>

            <div class="modal-actions">
                <button type="button" class="btn-cancel" onclick="hideEditModal()">
                    <i class="fas fa-times"></i>Hủy bỏ
                </button>
                <button type="submit" class="btn-save">
                    <i class="fas fa-save"></i>Cập nhật
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Include Footer -->
<%@ include file="../View/footer.jsp" %>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function showEditModal(button) {
        // Get data from button attributes
        const classroomId = button.getAttribute('data-classroom-id');
        const classroomName = button.getAttribute('data-classroom-name');
        const courseId = button.getAttribute('data-course-id');
        const teacherId = button.getAttribute('data-teacher-id');
        const slotId = button.getAttribute('data-slot-id');
        const roomId = button.getAttribute('data-room-id');
        const startDate = button.getAttribute('data-start-date');
        const endDate = button.getAttribute('data-end-date');
        const daysOfWeek = button.getAttribute('data-days-of-week');
        const classStarted = button.getAttribute('data-class-started') === 'true';

        console.log('Days of week from button:', daysOfWeek); // Debug log

        // Fill form fields
        document.getElementById('editClassroomId').value = classroomId;
        document.getElementById('editCourseId').value = courseId;
        document.getElementById('editName').value = classroomName;
        document.getElementById('editTeacherId').value = teacherId;
        document.getElementById('editSlotId').value = slotId;
        document.getElementById('editRoomId').value = roomId;
        document.getElementById('editStartDate').value = startDate;
        document.getElementById('editEndDate').value = endDate;

        // Set days of week checkboxes
        // Clear all checkboxes first
        const dayCheckboxes = document.querySelectorAll('#editModal input[name="daysOfWeek"]');
        console.log('Found checkboxes:', dayCheckboxes.length); // Debug log

        dayCheckboxes.forEach(cb => {
            cb.checked = false;
            cb.disabled = false; // Reset disabled state first
        });

        // Check the appropriate days
        if (daysOfWeek && daysOfWeek.trim() !== '') {
            const days = daysOfWeek.split(',');
            console.log('Days to check:', days); // Debug log

            days.forEach(day => {
                const trimmedDay = day.trim();
                const checkbox = document.querySelector(`#editModal input[name="daysOfWeek"][value="${trimmedDay}"]`);
                console.log(`Looking for checkbox with value ${trimmedDay}:`, checkbox); // Debug log
                if (checkbox) {
                    checkbox.checked = true;
                }
            });
        }

        // Handle class started logic
        const startDateField = document.getElementById('editStartDate');
        const startDateWarning = document.getElementById('startDateWarning');
        const dayCheckboxesContainer = document.querySelector('.days-checkbox-group');

        if (classStarted) {
            console.log('Class has started - disabling fields'); // Debug log
            // Disable start date and days of week if class has started
            startDateField.disabled = true;
            startDateWarning.style.display = 'block';

            // Disable all day checkboxes
            dayCheckboxes.forEach(cb => {
                cb.disabled = true;
            });

            // Add visual indication that these fields are disabled
            if (dayCheckboxesContainer) {
                dayCheckboxesContainer.style.opacity = '0.6';
                dayCheckboxesContainer.style.pointerEvents = 'none';
            }
        } else {
            console.log('Class has not started - enabling fields'); // Debug log
            // Enable all fields if class hasn't started
            startDateField.disabled = false;
            startDateWarning.style.display = 'none';

            dayCheckboxes.forEach(cb => {
                cb.disabled = false;
            });

            if (dayCheckboxesContainer) {
                dayCheckboxesContainer.style.opacity = '1';
                dayCheckboxesContainer.style.pointerEvents = 'auto';
            }
        }

        // Show modal
        document.getElementById('editModal').classList.add('show');
        document.body.style.overflow = 'hidden';
    }

    function hideEditModal() {
        document.getElementById('editModal').classList.remove('show');
        document.body.style.overflow = 'auto';

        // Reset form state
        const startDateField = document.getElementById('editStartDate');
        const startDateWarning = document.getElementById('startDateWarning');
        const dayCheckboxesContainer = document.querySelector('.days-checkbox-group');
        const dayCheckboxes = document.querySelectorAll('#editModal input[name="daysOfWeek"]');

        if (startDateField) startDateField.disabled = false;
        if (startDateWarning) startDateWarning.style.display = 'none';

        dayCheckboxes.forEach(cb => {
            cb.disabled = false;
        });

        if (dayCheckboxesContainer) {
            dayCheckboxesContainer.style.opacity = '1';
            dayCheckboxesContainer.style.pointerEvents = 'auto';
        }
    }

    // Close modal when clicking outside
    document.getElementById('editModal').addEventListener('click', function(e) {
        if (e.target === this) {
            hideEditModal();
        }
    });

    // Close modal on Escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            hideEditModal();
        }
    });

    // Validate form before submit
    document.querySelector('#editModal form').addEventListener('submit', function(e) {
        const checkedDays = document.querySelectorAll('#editModal input[name="daysOfWeek"]:checked');
        if (checkedDays.length === 0) {
            e.preventDefault();
            alert('Vui lòng chọn ít nhất một thứ trong tuần!');
            return false;
        }

        const startDate = new Date(document.getElementById('editStartDate').value);
        const endDate = new Date(document.getElementById('editEndDate').value);

        if (startDate >= endDate) {
            e.preventDefault();
            alert('Ngày kết thúc phải sau ngày bắt đầu!');
            return false;
        }
    });

    function hideEditModal() {
        document.getElementById('editModal').classList.remove('show');
        document.body.style.overflow = 'auto';

        // Reset form state
        const startDateField = document.getElementById('editStartDate');
        const startDateWarning = document.getElementById('startDateWarning');
        const dayCheckboxesContainer = document.querySelector('.days-checkbox-group');
        const dayCheckboxes = document.querySelectorAll('input[name="daysOfWeek"]');

        startDateField.disabled = false;
        startDateWarning.style.display = 'none';
        dayCheckboxes.forEach(cb => {
            cb.disabled = false;
        });
        dayCheckboxesContainer.style.opacity = '1';
        dayCheckboxesContainer.style.pointerEvents = 'auto';
    }

    // Close modal when clicking outside
    document.getElementById('editModal').addEventListener('click', function(e) {
        if (e.target === this) {
            hideEditModal();
        }
    });

    // Close modal on Escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            hideEditModal();
        }
    });

    // Validate form before submit
    document.querySelector('#editModal form').addEventListener('submit', function(e) {
        const checkedDays = document.querySelectorAll('input[name="daysOfWeek"]:checked');
        if (checkedDays.length === 0) {
            e.preventDefault();
            alert('Vui lòng chọn ít nhất một thứ trong tuần!');
            return false;
        }

        const startDate = new Date(document.getElementById('editStartDate').value);
        const endDate = new Date(document.getElementById('editEndDate').value);

        if (startDate >= endDate) {
            e.preventDefault();
            alert('Ngày kết thúc phải sau ngày bắt đầu!');
            return false;
        }
    });
</script>

<style>
    .days-checkbox-group {
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
        padding: 10px;
        border: 1px solid #dee2e6;
        border-radius: 5px;
        background-color: #f8f9fa;
    }

    .form-check-inline {
        margin-right: 0;
    }

    .form-check-input:disabled + .form-check-label {
        color: #6c757d;
        cursor: not-allowed;
    }

    .text-warning {
        font-size: 0.875em;
        margin-top: 5px;
    }

    .modal-content-custom {
        max-height: 90vh;
        overflow-y: auto;
    }

    .form-row {
        display: flex;
        gap: 20px;
        margin-bottom: 20px;
    }

    .form-group.full-width {
        flex: 1;
    }

    .form-group.half-width {
        flex: 0 0 calc(50% - 10px);
    }

    .form-label {
        font-weight: 600;
        margin-bottom: 8px;
        color: #495057;
    }

    .form-label i {
        margin-right: 8px;
        color: #007bff;
    }
</style>

</body>
</html>