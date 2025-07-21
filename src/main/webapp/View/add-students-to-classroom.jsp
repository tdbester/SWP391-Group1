<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm học sinh vào lớp - Training Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
    <style>
        .add-students-container {
            background: white;
            border-radius: 12px;
            padding: 30px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            border-bottom: 2px solid #f0f0f0;
            padding-bottom: 20px;
        }
        
        .page-title {
            color: #2c3e50;
            font-size: 28px;
            font-weight: 600;
            margin: 0;
        }
        
        .back-link {
            color: #3498db;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s;
        }
        
        .back-link:hover {
            color: #2980b9;
        }
        
        .classroom-info {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 30px;
        }
        
        .classroom-info h3 {
            color: #2c3e50;
            margin-bottom: 15px;
            font-size: 18px;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }
        
        .info-item {
            display: flex;
            justify-content: space-between;
        }
        
        .info-label {
            color: #7f8c8d;
            font-weight: 500;
        }
        
        .info-value {
            color: #2c3e50;
            font-weight: 600;
        }
        
        .students-form {
            margin-top: 20px;
        }
        
        .form-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .form-title {
            color: #2c3e50;
            font-size: 20px;
            font-weight: 600;
            margin: 0;
        }
        
        .select-all-container {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .select-all-checkbox {
            transform: scale(1.2);
        }
        
        .students-list {
            max-height: 400px;
            overflow-y: auto;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 15px;
        }
        
        .student-item {
            display: flex;
            align-items: center;
            padding: 15px;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            margin-bottom: 10px;
            transition: background-color 0.3s;
        }
        
        .student-item:hover {
            background-color: #f8f9fa;
        }
        
        .student-item:last-child {
            margin-bottom: 0;
        }
        
        .student-checkbox {
            margin-right: 15px;
            transform: scale(1.2);
        }
        
        .student-info {
            flex: 1;
        }
        
        .student-name {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 5px;
            font-size: 16px;
        }
        
        .student-details {
            font-size: 14px;
            color: #7f8c8d;
            line-height: 1.4;
        }
        
        .no-students {
            text-align: center;
            padding: 40px;
            color: #7f8c8d;
            font-style: italic;
        }
        
        .form-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #e9ecef;
        }
        
        .selected-count {
            color: #7f8c8d;
            font-size: 14px;
        }
        
        .action-buttons {
            display: flex;
            gap: 15px;
        }
        
        .btn {
            padding: 12px 24px;
            border-radius: 8px;
            font-weight: 600;
            text-decoration: none;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 14px;
        }
        
        .btn-cancel {
            background: #95a5a6;
            color: white;
        }
        
        .btn-cancel:hover {
            background: #7f8c8d;
        }
        
        .btn-submit {
            background: linear-gradient(135deg, #27ae60, #229954);
            color: white;
        }
        
        .btn-submit:hover {
            background: linear-gradient(135deg, #229954, #1e8449);
        }
        
        .btn-submit:disabled {
            background: #bdc3c7;
            cursor: not-allowed;
        }
        
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
            font-weight: 500;
        }
        
        .alert-success {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
        }
        
        .alert-error {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <div class="container">
        <jsp:include page="training-manager-sidebar.jsp" />
        
        <div class="main-content">
            <div class="add-students-container">
                <div class="page-header">
                    <h1 class="page-title">
                        <i class="fas fa-user-plus"></i>
                        Thêm học sinh vào lớp
                    </h1>
                    <a href="${pageContext.request.contextPath}/TrainingManagerClassroom" class="back-link">
                        <i class="fas fa-arrow-left"></i> Quay lại danh sách lớp
                    </a>
                </div>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        <i class="fas fa-exclamation-triangle"></i>
                        ${error}
                    </div>
                </c:if>
                
                <c:if test="${not empty param.error}">
                    <div class="alert alert-error">
                        <i class="fas fa-exclamation-triangle"></i>
                        ${param.error}
                    </div>
                </c:if>
                
                <c:if test="${not empty classroom}">
                    <div class="classroom-info">
                        <h3><i class="fas fa-chalkboard-teacher"></i> Thông tin lớp học</h3>
                        <div class="info-grid">
                            <div class="info-item">
                                <span class="info-label">Tên lớp:</span>
                                <span class="info-value">${classroom.classroomName}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Khóa học:</span>
                                <span class="info-value">${classroom.courseName}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Giáo viên:</span>
                                <span class="info-value">${classroom.teacherName}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Sức chứa:</span>
                                <span class="info-value">${classroom.maxCapacity} học sinh</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Hiện tại:</span>
                                <span class="info-value">${classroom.maxCapacity - classroom.availableSeats} học sinh</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Chỗ trống:</span>
                                <span class="info-value">${classroom.availableSeats} chỗ</span>
                            </div>
                        </div>
                    </div>
                    
                    <form action="${pageContext.request.contextPath}/TrainingManagerClassroom" method="post" class="students-form" id="addStudentsForm">
                        <input type="hidden" name="action" value="addStudentsToClass">
                        <input type="hidden" name="classroomId" value="${classroom.classroomID}">
                        
                        <div class="form-header">
                            <h3 class="form-title">
                                <i class="fas fa-users"></i>
                                Chọn học sinh để thêm vào lớp
                            </h3>
                            <c:if test="${not empty eligibleStudents}">
                                <div class="select-all-container">
                                    <input type="checkbox" id="selectAll" class="select-all-checkbox" onchange="toggleAllStudents()">
                                    <label for="selectAll">Chọn tất cả</label>
                                </div>
                            </c:if>
                        </div>
                        
                        <c:choose>
                            <c:when test="${empty eligibleStudents}">
                                <div class="no-students">
                                    <i class="fas fa-user-slash" style="font-size: 48px; margin-bottom: 20px; color: #bdc3c7;"></i>
                                    <p>Không có học sinh nào phù hợp để thêm vào lớp này.</p>
                                    <small>Học sinh phải có tư vấn cùng khóa học và chưa có trong lớp.</small>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="students-list">
                                    <c:forEach var="student" items="${eligibleStudents}">
                                        <div class="student-item">
                                            <input type="checkbox" 
                                                   name="selectedStudents" 
                                                   value="${student.id}" 
                                                   class="student-checkbox"
                                                   id="student_${student.id}"
                                                   onchange="updateSelectedCount()">
                                            <label for="student_${student.id}" class="student-info">
                                                <div class="student-name">${student.name}</div>
                                                <div class="student-details">
                                                    <i class="fas fa-phone"></i> ${student.phoneNumber != null ? student.phoneNumber : 'Chưa có SĐT'}
                                                    <c:if test="${not empty student.parentPhone}">
                                                        | <i class="fas fa-user"></i> Phụ huynh: ${student.parentPhone}
                                                    </c:if>
                                                    <c:if test="${not empty student.consultationNote}">
                                                        | <i class="fas fa-sticky-note"></i> Ghi chú: ${student.consultationNote}
                                                    </c:if>
                                                </div>
                                            </label>
                                        </div>
                                    </c:forEach>
                                </div>
                                
                                <div class="form-actions">
                                    <div class="selected-count">
                                        Đã chọn: <span id="selectedCount">0</span> học sinh
                                    </div>
                                    <div class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/TrainingManagerClassroom" class="btn btn-cancel">
                                            <i class="fas fa-times"></i> Hủy
                                        </a>
                                        <button type="submit" class="btn btn-submit" id="submitBtn" disabled>
                                            <i class="fas fa-check"></i> Thêm học sinh đã chọn
                                        </button>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
    
    <script>
        function updateSelectedCount() {
            const checkboxes = document.querySelectorAll('input[name="selectedStudents"]:checked');
            const count = checkboxes.length;
            document.getElementById('selectedCount').textContent = count;
            document.getElementById('submitBtn').disabled = count === 0;
            
            // Update select all checkbox
            const allCheckboxes = document.querySelectorAll('input[name="selectedStudents"]');
            const selectAllCheckbox = document.getElementById('selectAll');
            if (selectAllCheckbox) {
                selectAllCheckbox.checked = count === allCheckboxes.length;
                selectAllCheckbox.indeterminate = count > 0 && count < allCheckboxes.length;
            }
        }
        
        function toggleAllStudents() {
            const selectAllCheckbox = document.getElementById('selectAll');
            const studentCheckboxes = document.querySelectorAll('input[name="selectedStudents"]');
            
            studentCheckboxes.forEach(checkbox => {
                checkbox.checked = selectAllCheckbox.checked;
            });
            
            updateSelectedCount();
        }
        
        // Initialize count on page load
        document.addEventListener('DOMContentLoaded', function() {
            updateSelectedCount();
        });
    </script>
</body>
</html>
