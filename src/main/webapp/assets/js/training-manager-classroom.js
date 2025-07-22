/**
 * JavaScript for Training Manager Classroom Management
 * Handles modal functionality and AJAX operations for student assignment
 */

let currentClassroomId = null;
let selectedStudents = new Set();

/**
 * Open the student selection modal for a specific classroom
 */
function openStudentModal(classroomId, classroomName) {
    currentClassroomId = classroomId;
    selectedStudents.clear();
    
    // Update modal title
    document.getElementById('modalClassroomName').textContent = classroomName;
    
    // Show modal
    document.getElementById('studentModal').style.display = 'block';
    
    // Load eligible students
    loadEligibleStudents(classroomId);
    
    // Reset UI
    updateSelectedCount();
    updateConfirmButton();
}

/**
 * Close the student selection modal
 */
function closeStudentModal() {
    document.getElementById('studentModal').style.display = 'none';
    currentClassroomId = null;
    selectedStudents.clear();
}

/**
 * Load eligible students for the classroom via AJAX
 */
function loadEligibleStudents(classroomId) {
    const modalContent = document.getElementById('modalContent');
    
    // Show loading state
    modalContent.innerHTML = `
        <div class="loading">
            <i class="fas fa-spinner fa-spin"></i>
            Đang tải danh sách học sinh...
        </div>
    `;
    
    // Make AJAX request
    fetch(`TrainingManagerClassroom?action=getEligibleStudents&classroomId=${classroomId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                displayStudents(data.students);
            } else {
                showError(data.message || 'Lỗi khi tải danh sách học sinh');
            }
        })
        .catch(error => {
            console.error('Error loading students:', error);
            showError('Lỗi kết nối. Vui lòng thử lại.');
        });
}

/**
 * Display the list of eligible students in the modal
 */
function displayStudents(students) {
    const modalContent = document.getElementById('modalContent');
    
    if (!students || students.length === 0) {
        modalContent.innerHTML = `
            <div class="no-students">
                <i class="fas fa-user-slash" style="font-size: 48px; margin-bottom: 20px; color: #bdc3c7;"></i>
                <p>Không có học sinh nào phù hợp để thêm vào lớp này.</p>
                <small>Học sinh phải có tư vấn cùng khóa học và chưa có trong lớp.</small>
            </div>
        `;
        return;
    }
    
    let html = '<div class="student-list">';
    
    students.forEach(student => {
        html += `
            <div class="student-item">
                <input type="checkbox" 
                       class="student-checkbox" 
                       id="student_${student.id}"
                       value="${student.id}"
                       onchange="toggleStudent(${student.id})">
                <label for="student_${student.id}" class="student-info">
                    <div class="student-name">${student.name || 'Không có tên'}</div>
                    <div class="student-details">
                        <i class="fas fa-phone"></i> ${student.phoneNumber || 'Chưa có SĐT'}
                        ${student.parentPhone ? ` | <i class="fas fa-user"></i> Phụ huynh: ${student.parentPhone}` : ''}
                        ${student.enrollmentDate ? ` | <i class="fas fa-calendar"></i> Nhập học: ${formatDate(student.enrollmentDate)}` : ''}
                    </div>
                </label>
            </div>
        `;
    });
    
    html += '</div>';
    modalContent.innerHTML = html;
}

/**
 * Toggle student selection
 */
function toggleStudent(studentId) {
    const checkbox = document.getElementById(`student_${studentId}`);
    
    if (checkbox.checked) {
        selectedStudents.add(studentId);
    } else {
        selectedStudents.delete(studentId);
    }
    
    updateSelectedCount();
    updateConfirmButton();
}

/**
 * Update the selected count display
 */
function updateSelectedCount() {
    document.getElementById('selectedCount').textContent = selectedStudents.size;
}

/**
 * Update the confirm button state
 */
function updateConfirmButton() {
    const confirmBtn = document.getElementById('confirmBtn');
    confirmBtn.disabled = selectedStudents.size === 0;
}

/**
 * Confirm adding selected students to the classroom
 */
function confirmAddStudents() {
    if (selectedStudents.size === 0) {
        showAlert('Vui lòng chọn ít nhất một học sinh.', 'error');
        return;
    }
    
    if (!currentClassroomId) {
        showAlert('Lỗi: Không xác định được lớp học.', 'error');
        return;
    }
    
    // Disable confirm button and show loading
    const confirmBtn = document.getElementById('confirmBtn');
    const originalText = confirmBtn.innerHTML;
    confirmBtn.disabled = true;
    confirmBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
    
    // Prepare data
    const studentIds = Array.from(selectedStudents).join(',');
    const formData = new FormData();
    formData.append('action', 'addStudentsToClass');
    formData.append('classroomId', currentClassroomId);
    formData.append('studentIds', studentIds);
    
    // Make AJAX request
    fetch('TrainingManagerClassroom', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showAlert(data.message, 'success');
            closeStudentModal();
            // Reload page to update classroom info
            setTimeout(() => {
                window.location.reload();
            }, 1500);
        } else {
            showAlert(data.message || 'Lỗi khi thêm học sinh vào lớp.', 'error');
        }
    })
    .catch(error => {
        console.error('Error adding students:', error);
        showAlert('Lỗi kết nối. Vui lòng thử lại.', 'error');
    })
    .finally(() => {
        // Restore button state
        confirmBtn.disabled = false;
        confirmBtn.innerHTML = originalText;
    });
}

/**
 * Show error message in modal
 */
function showError(message) {
    const modalContent = document.getElementById('modalContent');
    modalContent.innerHTML = `
        <div class="alert alert-error">
            <i class="fas fa-exclamation-triangle"></i>
            ${message}
        </div>
    `;
}

/**
 * Show alert message
 */
function showAlert(message, type = 'info') {
    // Remove existing alerts
    const existingAlerts = document.querySelectorAll('.alert');
    existingAlerts.forEach(alert => alert.remove());
    
    // Create new alert
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.innerHTML = `
        <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-triangle'}"></i>
        ${message}
    `;
    
    // Insert at the top of the main content
    const mainContent = document.querySelector('.classroom-container');
    mainContent.insertBefore(alert, mainContent.firstChild);
    
    // Auto-remove after 5 seconds
    setTimeout(() => {
        if (alert.parentNode) {
            alert.remove();
        }
    }, 5000);
}

/**
 * Format date for display
 */
function formatDate(dateString) {
    if (!dateString) return '';
    
    try {
        const date = new Date(dateString);
        return date.toLocaleDateString('vi-VN');
    } catch (e) {
        return dateString;
    }
}

/**
 * Close modal when clicking outside of it
 */
window.onclick = function(event) {
    const modal = document.getElementById('studentModal');
    if (event.target === modal) {
        closeStudentModal();
    }
}

/**
 * Handle escape key to close modal
 */
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        closeStudentModal();
    }
});

/**
 * Initialize page
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Training Manager Classroom page loaded');
    
    // Add any initialization code here
    
    // Check for success/error messages in URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    const message = urlParams.get('message');
    const type = urlParams.get('type');
    
    if (message) {
        showAlert(decodeURIComponent(message), type || 'info');
        
        // Clean URL
        const newUrl = window.location.pathname;
        window.history.replaceState({}, document.title, newUrl);
    }
});
