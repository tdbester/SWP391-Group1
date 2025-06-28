<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gửi Đơn Yêu Cầu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.tiny.cloud/1/s9ulrr1k52taw3pfbbdpo04t76gcsewieq7ljy51r2dmau1j/tinymce/5/tinymce.min.js" referrerpolicy="origin"></script>
    <style>
        .card {
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            border: 1px solid rgba(0, 0, 0, 0.125);
        }
        .form-section {
            display: none;
        }
        .form-section.active {
            display: block;
        }
        .alert {
            margin-bottom: 1rem;
        }
        .schedule-item {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            padding: 1rem;
            margin-bottom: 0.5rem;
        }
        .room-item {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            padding: 0.75rem;
            margin-bottom: 0.5rem;
        }
        .btn-submit {
            background: linear-gradient(45deg, #007bff, #0056b3);
            border: none;
            border-radius: 0.5rem;
            padding: 0.75rem 2rem;
            font-weight: 500;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<div class="container-fluid">
    <div class="row">
        <div class="col-md-3">
            <jsp:include page="teacher-sidebar.jsp" />
        </div>

        <div class="col-md-9">
            <div class="container mt-4">
                <div class="row justify-content-center">
                    <div class="col-lg-10">
                        <div class="card">
                            <div class="card-header bg-primary text-white">
                                <h4 class="mb-0"><i class="fas fa-paper-plane me-2"></i>Gửi đơn cho phòng quản lý đào tạo</h4>
                            </div>

                            <div class="card-body">
                                <!-- Hiển thị thông báo -->
                                <c:if test="${not empty success}">
                                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                                        <i class="fas fa-check-circle me-2"></i>${success}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>

                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>

                                <c:if test="${not empty warning}">
                                    <div class="alert alert-warning alert-dismissible fade show" role="alert">
                                        <i class="fas fa-exclamation-triangle me-2"></i>${warning}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>

                                <form id="requestForm" method="post" action="${pageContext.request.contextPath}/teacherRequest">
                                    <!-- Chọn loại đơn -->
                                    <div class="mb-4">
                                        <label class="form-label fw-bold">Loại đơn yêu cầu <span class="text-danger">*</span></label>
                                        <select id="requestType" name="type" class="form-select" required>
                                            <option value="">-- Chọn loại đơn --</option>
                                            <option value="leave" ${param.type == 'leave' ? 'selected' : ''}>Xin nghỉ phép</option>
                                            <option value="schedule_change" ${param.type == 'schedule_change' ? 'selected' : ''}>Thay đổi lịch học</option>
                                            <option value="room_change" ${param.type == 'room_change' ? 'selected' : ''}>Thay đổi lớp học</option>
                                            <option value="other" ${param.type == 'other' ? 'selected' : ''}>Khác</option>
                                        </select>
                                    </div>

                                    <!-- Form xin nghỉ phép -->
                                    <div id="leaveSection" class="form-section">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label class="form-label fw-bold">Ngày nghỉ <span class="text-danger">*</span></label>
                                                <input type="date" id="leaveDate" name="leaveDate" class="form-control"
                                                       value="${param.leaveDate}" min="<fmt:formatDate value='<%=new java.util.Date()%>' pattern='yyyy-MM-dd'/>">
                                            </div>
                                            <div class="col-md-6">
                                                <button type="button" id="checkLeaveBtn" class="btn btn-outline-primary mt-4">
                                                    <i class="fas fa-search me-1"></i>Kiểm tra lịch học
                                                </button>
                                            </div>
                                        </div>

                                        <!-- Hiển thị lịch học trong ngày nghỉ -->
                                        <div id="leaveSchedules" class="mt-3">
                                            <c:if test="${not empty leaveSchedules}">
                                                <h6 class="fw-bold text-info">Lịch học trong ngày ${param.leaveDate}:</h6>
                                                <c:forEach items="${leaveSchedules}" var="schedule">
                                                    <div class="schedule-item">
                                                        <div class="row">
                                                            <div class="col-md-8">
                                                                <strong>${schedule.courseTitle}</strong> - Lớp: ${schedule.className}
                                                            </div>
                                                            <div class="col-md-4 text-end">
                                                                    <span class="badge bg-primary">
                                                                        ${schedule.slotStartTime} - ${schedule.slotEndTime}
                                                                    </span>
                                                                <span class="badge bg-secondary">Phòng ${schedule.roomCode}</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                        </div>
                                    </div>

                                    <!-- Form thay đổi lịch học -->
                                    <div id="scheduleChangeSection" class="form-section">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label class="form-label fw-bold">Ngày muốn thay đổi <span class="text-danger">*</span></label>
                                                <input type="date" id="changeFromDate" name="changeFromDate" class="form-control"
                                                       value="${param.changeFromDate}" min="<fmt:formatDate value='<%=new java.util.Date()%>' pattern='yyyy-MM-dd'/>">
                                            </div>
                                            <div class="col-md-6">
                                                <button type="button" id="checkChangeBtn" class="btn btn-outline-primary mt-4">
                                                    <i class="fas fa-search me-1"></i>Kiểm tra lịch học
                                                </button>
                                            </div>
                                        </div>

                                        <!-- Hiển thị lịch học để chọn -->
                                        <div id="changeSchedules" class="mt-3">
                                            <c:if test="${not empty changeSchedules}">
                                                <h6 class="fw-bold text-info">Chọn lớp muốn thay đổi (tối đa 2 lớp):</h6>
                                                <c:forEach items="${changeSchedules}" var="schedule" varStatus="status">
                                                    <div class="schedule-item">
                                                        <div class="form-check">
                                                            <input class="form-check-input schedule-checkbox" type="checkbox"
                                                                   name="selectedSchedules" value="${schedule.id}"
                                                                   id="schedule${status.index}">
                                                            <label class="form-check-label" for="schedule${status.index}">
                                                                <div class="row">
                                                                    <div class="col-md-8">
                                                                        <strong>${schedule.courseTitle}</strong> - Lớp: ${schedule.className}
                                                                    </div>
                                                                    <div class="col-md-4 text-end">
                                                                            <span class="badge bg-primary">
                                                                                ${schedule.slotStartTime} - ${schedule.slotEndTime}
                                                                            </span>
                                                                        <span class="badge bg-secondary">Phòng ${schedule.roomCode}</span>
                                                                    </div>
                                                                </div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                        </div>

                                        <div class="row mt-3">
                                            <div class="col-md-6">
                                                <label class="form-label fw-bold">Ngày muốn chuyển sang</label>
                                                <input type="date" name="changeToDate" class="form-control"
                                                       value="${param.changeToDate}" min="<fmt:formatDate value='<%=new java.util.Date()%>' pattern='yyyy-MM-dd'/>">
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Form thay đổi phòng học -->
                                    <div id="roomChangeSection" class="form-section">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label class="form-label fw-bold">Ngày muốn đổi phòng <span class="text-danger">*</span></label>
                                                <input type="date" id="roomChangeDate" name="roomChangeDate" class="form-control"
                                                       value="${param.roomChangeDate}" min="<fmt:formatDate value='<%=new java.util.Date()%>' pattern='yyyy-MM-dd'/>">
                                            </div>
                                            <div class="col-md-6">
                                                <button type="button" id="checkRoomBtn" class="btn btn-outline-primary mt-4">
                                                    <i class="fas fa-search me-1"></i>Kiểm tra lịch học
                                                </button>
                                            </div>
                                        </div>

                                        <!-- Chọn môn học -->
                                        <div id="roomChangeSchedules" class="mt-3">
                                            <c:if test="${not empty roomChangeSchedules}">
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Chọn môn học:</label>
                                                    <select id="selectedSchedule" name="selectedSchedule" class="form-select">
                                                        <option value="">-- Chọn môn học --</option>
                                                        <c:forEach items="${roomChangeSchedules}" var="schedule">
                                                            <option value="${schedule.id}" data-room-id="${schedule.roomId}" data-slot-id="${schedule.slotId}">
                                                                    ${schedule.courseTitle} - Lớp ${schedule.className}
                                                                (${schedule.slotStartTime} - ${schedule.slotEndTime}, Phòng ${schedule.roomCode})
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </c:if>
                                        </div>

                                        <!-- Hiển thị phòng trống -->
                                        <div id="availableRooms" class="mt-3">
                                            <c:if test="${not empty availableRooms}">
                                                <h6 class="fw-bold text-info">Phòng học khả dụng:</h6>
                                                <c:forEach items="${availableRooms}" var="room">
                                                    <div class="room-item">
                                                        <div class="form-check">
                                                            <input class="form-check-input" type="radio" name="selectedRoom"
                                                                   value="${room.id}" id="room${room.id}">
                                                            <label class="form-check-label" for="room${room.id}">
                                                                <strong>Phòng ${room.code}</strong>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                        </div>
                                    </div>

                                    <!-- Lý do (cho tất cả loại đơn) -->
                                    <div class="mb-4">
                                        <label class="form-label fw-bold">Lý do <span class="text-danger">*</span></label>
                                        <textarea id="reason" name="reason" class="form-control" rows="6" required>${param.reason}</textarea>
                                    </div>

                                    <!-- Nút gửi -->
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary btn-submit">
                                            <i class="fas fa-paper-plane me-2"></i>Gửi Đơn Yêu Cầu
                                        </button>
                                        <button type="reset" class="btn btn-outline-secondary ms-2">
                                            <i class="fas fa-undo me-2"></i>Làm Mới
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Initialize TinyMCE
    tinymce.init({
        selector: '#reason',
        height: 200,
        plugins: 'lists link image code',
        toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright | bullist numlist | link image | code',
        content_style: 'body { font-family: Arial, sans-serif; }'
    });

    // Xử lý thay đổi loại đơn
    document.getElementById('requestType').addEventListener('change', function() {
        // Ẩn tất cả các section
        const sections = document.querySelectorAll('.form-section');
        sections.forEach(section => section.classList.remove('active'));

        // Hiển thị section tương ứng
        const selectedType = this.value;
        if (selectedType) {
            const sectionMap = {
                'leave': 'leaveSection',
                'schedule_change': 'scheduleChangeSection',
                'room_change': 'roomChangeSection'
            };

            const sectionId = sectionMap[selectedType];
            if (sectionId) {
                document.getElementById(sectionId).classList.add('active');
            }
        }
    });

    // Trigger change event nếu đã có giá trị được chọn
    if (document.getElementById('requestType').value) {
        document.getElementById('requestType').dispatchEvent(new Event('change'));
    }

    // Kiểm tra lịch nghỉ phép
    document.getElementById('checkLeaveBtn').addEventListener('click', function() {
        const date = document.getElementById('leaveDate').value;
        if (date) {
            window.location.href = `${pageContext.request.contextPath}/teacherRequest?action=checkLeave&date=` + date;
        }
    });

    // Kiểm tra lịch thay đổi
    document.getElementById('checkChangeBtn').addEventListener('click', function() {
        const date = document.getElementById('changeFromDate').value;
        if (date) {
            window.location.href = `${pageContext.request.contextPath}/teacherRequest?action=checkChange&date=` + date;
        }
    });

    // Kiểm tra lịch đổi phòng
    document.getElementById('checkRoomBtn').addEventListener('click', function() {
        const date = document.getElementById('roomChangeDate').value;
        if (date) {
            window.location.href = `${pageContext.request.contextPath}/teacherRequest?action=checkRoom&date=` + date;
        }
    });

    // Giới hạn chọn tối đa 2 checkbox cho thay đổi lịch
    const scheduleCheckboxes = document.querySelectorAll('.schedule-checkbox');
    scheduleCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            const checkedBoxes = document.querySelectorAll('.schedule-checkbox:checked');
            if (checkedBoxes.length > 2) {
                this.checked = false;
                alert('Bạn chỉ có thể chọn tối đa 2 lớp học!');
            }
        });
    });

    // Xử lý chọn môn học để hiển thị phòng trống
    document.getElementById('selectedSchedule')?.addEventListener('change', function() {
        const scheduleId = this.value;
        const date = document.getElementById('roomChangeDate').value;
        if (scheduleId && date) {
            window.location.href = `${pageContext.request.contextPath}/teacherRequest?action=getAvailableRooms&scheduleId=` + scheduleId + '&date=' + date;
        }
    });

    // Validation form trước khi submit
    document.getElementById('requestForm').addEventListener('submit', function(e) {
        const requestType = document.getElementById('requestType').value;

        if (requestType === 'leave') {
            const leaveDate = document.getElementById('leaveDate').value;
            if (!leaveDate) {
                e.preventDefault();
                alert('Vui lòng chọn ngày nghỉ!');
                return;
            }
        } else if (requestType === 'schedule_change') {
            const changeFromDate = document.getElementById('changeFromDate').value;
            const selectedSchedules = document.querySelectorAll('input[name="selectedSchedules"]:checked');

            if (!changeFromDate) {
                e.preventDefault();
                alert('Vui lòng chọn ngày muốn thay đổi!');
                return;
            }

            if (selectedSchedules.length === 0) {
                e.preventDefault();
                alert('Vui lòng chọn ít nhất một lớp học!');
                return;
            }
        } else if (requestType === 'room_change') {
            const roomChangeDate = document.getElementById('roomChangeDate').value;
            const selectedSchedule = document.getElementById('selectedSchedule')?.value;
            const selectedRoom = document.querySelector('input[name="selectedRoom"]:checked');

            if (!roomChangeDate) {
                e.preventDefault();
                alert('Vui lòng chọn ngày muốn đổi phòng!');
                return;
            }

            if (!selectedSchedule) {
                e.preventDefault();
                alert('Vui lòng chọn môn học!');
                return;
            }

            if (!selectedRoom) {
                e.preventDefault();
                alert('Vui lòng chọn phòng học mới!');
                return;
            }
        }

        // Cập nhật nội dung từ TinyMCE
        tinymce.triggerSave();
    });
</script>
</body>
</html>