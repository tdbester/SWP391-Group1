<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gửi Đơn Yêu Cầu - Talent Center</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-request.css">

    <!-- TinyMCE -->
    <script src="https://cdn.tiny.cloud/1/s9ulrr1k52taw3pfbbdpo04t76gcsewieq7ljy51r2dmau1j/tinymce/5/tinymce.min.js" referrerpolicy="origin" onerror="console.error('Failed to load TinyMCE')"></script>
</head>
<body>
<jsp:include page="header.jsp" />

<div class="main-container">
    <!-- Sidebar -->
    <div class="sidebar-col">
        <jsp:include page="teacher-sidebar.jsp" />
    </div>

    <!-- Main Content -->
    <div class="content-col">
        <div class="request-card">
            <div class="request-card-header">
                <h4 class="mb-0"><i class="fas fa-paper-plane me-2"></i>Gửi đơn cho phòng quản lý đào tạo</h4>
            </div>

            <div class="request-card-body">
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
                            <option value="leave" ${param.type == 'leave' || param.type == 'Xin nghỉ phép' ? 'selected' : ''}>Xin nghỉ phép</option>
                            <option value="schedule_change" ${param.type == 'schedule_change' || param.type == 'Thay đổi lịch dạy' ? 'selected' : ''}>Thay đổi lịch dạy</option>
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
                                <h6 class="section-title">Lịch học trong ngày ${param.leaveDate}:</h6>
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

                    <!-- Form thay đổi lịch dạy -->
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
                                <h6 class="section-title">Chọn lớp muốn thay đổi (tối đa 1 lớp):</h6>
                                <c:forEach items="${changeSchedules}" var="schedule" varStatus="status">
                                    <div class="schedule-item">
                                        <div class="form-check">
                                            <input class="form-check-input schedule-checkbox" type="checkbox"
                                                   name="selectedSchedules" value="${schedule.id}"
                                                   id="schedule${status.index}"
                                                ${paramValues.selectedSchedules != null && paramValues.selectedSchedules[0] == schedule.id ? 'checked' : ''}>
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
                                <label class="form-label fw-bold">Ngày muốn chuyển sang <span class="text-danger">*</span></label>
                                <input type="date" name="changeToDate" class="form-control"
                                       value="${param.changeToDate}" min="<fmt:formatDate value='<%=new java.util.Date()%>' pattern='yyyy-MM-dd'/>" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Chọn ca dạy (slot) <span class="text-danger">*</span></label>
                                <select name="changeToSlot" class="form-select" required>
                                    <option value="">-- Chọn ca dạy --</option>
                                    <c:forEach var="slot" items="${slotList}">
                                        <option value="${slot.slotId}" ${param.changeToSlot == slot.slotId ? 'selected' : ''}>
                                            Ca ${slot.slotId}: ${slot.slotStartTime} - ${slot.slotEndTime}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- Lý do (cho tất cả loại đơn) -->
                    <div class="mb-4">
                        <label class="form-label fw-bold">Lý do <span class="text-danger">*</span></label>
                        <textarea id="reason" name="reason" class="form-control" rows="6" required>${param.reason}</textarea>
                    </div>

                    <!-- Nút gửi -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary btn-submit">
                            <i class="fas fa-paper-plane me-2"></i>Gửi Đơn Yêu Cầu
                        </button>
                        <button type="reset" class="btn btn-outline-secondary">
                            <i class="fas fa-undo me-2"></i>Làm Mới
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Fallback nếu TinyMCE không load được
    if (typeof tinymce === 'undefined') {
        console.warn('TinyMCE not loaded, using fallback textarea');
        window.tinymce = {
            init: function(config) {
                console.log('TinyMCE fallback: using regular textarea');
                const textarea = document.querySelector(config.selector);
                if (textarea) {
                    textarea.style.display = 'block';
                }
            },
            get: function(id) {
                return null;
            },
            triggerSave: function() {
                console.log('TinyMCE fallback: triggerSave called');
            }
        };
    }

    // Initialize TinyMCE
    document.addEventListener('DOMContentLoaded', function() {
        tinymce.init({
            selector: '#reason',
            height: 200,
            plugins: 'lists link image code',
            toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright | bullist numlist | link image | code',
            content_style: 'body { font-family: Arial, sans-serif; }',
            setup: function(editor) {
                editor.on('change', function() {
                    editor.save();
                });
            },
            init_instance_callback: function(editor) {
                console.log('TinyMCE initialized successfully');
            }
        });
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
                'schedule_change': 'scheduleChangeSection'
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

    // Function để hiển thị loading state
    function showLoading(button) {
        const originalText = button.innerHTML;
        button.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Đang tải...';
        button.disabled = true;
        return originalText;
    }

    function hideLoading(button, originalText) {
        button.innerHTML = originalText;
        button.disabled = false;
    }

    // Kiểm tra lịch nghỉ phép bằng AJAX
    document.getElementById('checkLeaveBtn').addEventListener('click', function() {
        const date = document.getElementById('leaveDate').value;
        if (!date) {
            alert('Vui lòng chọn ngày nghỉ!');
            return;
        }

        const button = this;
        const originalText = showLoading(button);

        // Sử dụng AJAX để lấy dữ liệu mà không reload trang
        fetch('${pageContext.request.contextPath}/teacherRequest?action=checkLeave&date=' + date, {
            method: 'GET',
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => response.text())
            .then(data => {
                // Parse HTML response để lấy phần schedules
                const parser = new DOMParser();
                const doc = parser.parseFromString(data, 'text/html');
                const schedulesSection = doc.querySelector('#leaveSchedules');

                if (schedulesSection) {
                    document.getElementById('leaveSchedules').innerHTML = schedulesSection.innerHTML;
                } else {
                    document.getElementById('leaveSchedules').innerHTML =
                        '<div class="alert alert-info"><i class="fas fa-info-circle me-2"></i>Không có lịch học trong ngày này.</div>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('leaveSchedules').innerHTML =
                    '<div class="alert alert-danger"><i class="fas fa-exclamation-circle me-2"></i>Có lỗi xảy ra khi tải lịch học.</div>';
            })
            .finally(() => {
                hideLoading(button, originalText);
            });
    });

    // Kiểm tra lịch thay đổi bằng AJAX
    document.getElementById('checkChangeBtn').addEventListener('click', function() {
        const date = document.getElementById('changeFromDate').value;
        if (!date) {
            alert('Vui lòng chọn ngày muốn thay đổi!');
            return;
        }

        const button = this;
        const originalText = showLoading(button);

        // Sử dụng AJAX để lấy dữ liệu mà không reload trang
        fetch('${pageContext.request.contextPath}/teacherRequest?action=checkChange&date=' + date, {
            method: 'GET',
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => response.text())
            .then(data => {
                // Parse HTML response để lấy phần schedules
                const parser = new DOMParser();
                const doc = parser.parseFromString(data, 'text/html');
                const schedulesSection = doc.querySelector('#changeSchedules');

                if (schedulesSection) {
                    document.getElementById('changeSchedules').innerHTML = schedulesSection.innerHTML;
                    // Re-attach event listeners cho các checkbox mới
                    attachCheckboxListeners();
                } else {
                    document.getElementById('changeSchedules').innerHTML =
                        '<div class="alert alert-info"><i class="fas fa-info-circle me-2"></i>Không có lịch học trong ngày này.</div>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('changeSchedules').innerHTML =
                    '<div class="alert alert-danger"><i class="fas fa-exclamation-circle me-2"></i>Có lỗi xảy ra khi tải lịch học.</div>';
            })
            .finally(() => {
                hideLoading(button, originalText);
            });
    });

    // Function để attach event listeners cho checkboxes
    function attachCheckboxListeners() {
        const scheduleCheckboxes = document.querySelectorAll('.schedule-checkbox');
        scheduleCheckboxes.forEach(checkbox => {
            checkbox.addEventListener('change', function() {
                const checkedBoxes = document.querySelectorAll('.schedule-checkbox:checked');
                if (checkedBoxes.length > 1) {
                    this.checked = false;
                    alert('Bạn chỉ có thể chọn tối đa 1 lớp học!');
                }
            });
        });
    }

    // Attach listeners lần đầu
    attachCheckboxListeners();

    // Validation form trước khi submit
    document.getElementById('requestForm').addEventListener('submit', function(e) {
        console.log('Form submit triggered');

        // Đợi TinyMCE sẵn sàng
        if (typeof tinymce !== 'undefined' && tinymce.get('reason')) {
            tinymce.triggerSave();
        }

        const requestType = document.getElementById('requestType').value;
        console.log('Request type:', requestType);

        if (!requestType) {
            e.preventDefault();
            alert('Vui lòng chọn loại đơn yêu cầu!');
            return;
        }

        const reason = document.getElementById('reason').value;
        if (!reason || reason.trim() === '') {
            e.preventDefault();
            alert('Vui lòng nhập lý do!');
            return;
        }

        // Validation riêng cho từng loại đơn
        if (requestType === 'leave') {
            const leaveDate = document.getElementById('leaveDate').value;
            if (!leaveDate) {
                e.preventDefault();
                alert('Vui lòng chọn ngày nghỉ!');
                return;
            }
        }

        if (requestType === 'schedule_change') {
            const changeFromDate = document.getElementById('changeFromDate').value;
            if (!changeFromDate) {
                e.preventDefault();
                alert('Vui lòng chọn ngày muốn thay đổi!');
                return;
            }

            const selectedSchedules = document.querySelectorAll('.schedule-checkbox:checked');
            if (selectedSchedules.length === 0) {
                e.preventDefault();
                alert('Vui lòng chọn lớp học muốn thay đổi!');
                return;
            }
        }

        console.log('Form validation passed, submitting...');
    });

    // Auto-save form data to prevent data loss
    const formInputs = document.querySelectorAll('#requestForm input, #requestForm select, #requestForm textarea');
    formInputs.forEach(input => {
        input.addEventListener('change', function() {
            // Save to sessionStorage to preserve data
            const formData = new FormData(document.getElementById('requestForm'));
            const data = {};
            for (let [key, value] of formData.entries()) {
                data[key] = value;
            }
            try {
                sessionStorage.setItem('teacherRequestFormData', JSON.stringify(data));
            } catch (e) {
                console.log('SessionStorage not available');
            }
        });
    });

    // Restore form data on page load
    try {
        const savedData = sessionStorage.getItem('teacherRequestFormData');
        if (savedData && !document.querySelector('.alert-success')) { // Don't restore if form was just submitted successfully
            const data = JSON.parse(savedData);
            Object.keys(data).forEach(key => {
                const input = document.querySelector(`[name="${key}"]`);
                if (input && !input.value) { // Only restore if current value is empty
                    if (input.type === 'checkbox') {
                        input.checked = true;
                    } else {
                        input.value = data[key];
                    }
                }
            });
        }
    } catch (e) {
        console.log('Error restoring form data:', e);
    }

    // Clear saved data when form is successfully submitted
    if (document.querySelector('.alert-success')) {
        try {
            sessionStorage.removeItem('teacherRequestFormData');
        } catch (e) {
            console.log('SessionStorage not available');
        }
    }
</script>
</body>
</html>