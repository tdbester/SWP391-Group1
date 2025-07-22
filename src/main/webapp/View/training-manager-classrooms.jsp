<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý lớp học - Training Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
    <style>
        .classroom-container {
            background: white;
            border-radius: 12px;
            padding: 30px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        
        .classroom-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            border-bottom: 2px solid #f0f0f0;
            padding-bottom: 20px;
        }
        
        .classroom-title {
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
        
        .classrooms-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
            gap: 25px;
            margin-top: 20px;
        }
        
        .classroom-card {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 12px;
            padding: 25px;
            transition: all 0.3s ease;
            position: relative;
        }
        
        .classroom-name {
            font-size: 20px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
        }
        
        .classroom-info {
            margin-bottom: 20px;
        }
        
        .info-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 8px;
            font-size: 14px;
        }
        
        .info-label {
            color: #7f8c8d;
            font-weight: 500;
        }
        
        .info-value {
            color: #2c3e50;
            font-weight: 600;
        }
        
        .student-count {
            background: #e8f5e8;
            color: #27ae60;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
        }
        
        .add-student-btn {
            background: linear-gradient(135deg, #3498db, #2980b9);
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            width: 100%;
            font-size: 14px;
        }
        
        .add-student-btn:hover {
            background: linear-gradient(135deg, #2980b9, #1f5f8b);
            transform: translateY(-2px);
        }
        
        .add-student-btn:disabled {
            background: #bdc3c7;
            cursor: not-allowed;
            transform: none;
        }

        .no-students {
            text-align: center;
            padding: 40px;
            color: #7f8c8d;
            font-style: italic;
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
            <div class="classroom-container">
                <div class="classroom-header">
                    <h1 class="classroom-title">
                        <i class="fas fa-chalkboard-teacher"></i>
                        Quản lý lớp học
                    </h1>
                    <a href="${pageContext.request.contextPath}/TrainingManagerDashboard" class="back-link">
                        <i class="fas fa-arrow-left"></i> Quay lại Dashboard
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

                <c:if test="${not empty param.success}">
                    <div class="alert alert-success">
                        <i class="fas fa-check-circle"></i>
                        ${param.success}
                    </div>
                </c:if>
                
                <c:choose>
                    <c:when test="${empty classrooms}">
                        <div class="no-students">
                            <i class="fas fa-chalkboard" style="font-size: 48px; margin-bottom: 20px; color: #bdc3c7;"></i>
                            <p>Chưa có lớp học nào được tạo.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="classrooms-grid">
                            <c:forEach var="classroom" items="${classrooms}">
                                <div class="classroom-card">
                                    <div class="classroom-name">
                                        <i class="fas fa-door-open"></i>
                                        ${classroom.classroomName}
                                    </div>
                                    
                                    <div class="classroom-info">
                                        <div class="info-row">
                                            <span class="info-label">Khóa học:</span>
                                            <span class="info-value">${classroom.courseName}</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">Giáo viên:</span>
                                            <span class="info-value">${classroom.teacherName}</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">Sức chứa:</span>
                                            <span class="info-value">${classroom.maxCapacity} học sinh</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">Hiện tại:</span>
                                            <span class="student-count">
                                                ${classroom.maxCapacity - classroom.availableSeats} học sinh
                                            </span>
                                        </div>
                                        <div class="info-row">
                                            <span class="info-label">Chỗ trống:</span>
                                            <span class="info-value">${classroom.availableSeats} chỗ</span>
                                        </div>
                                    </div>
                                    
                                    <c:choose>
                                        <c:when test="${classroom.availableSeats <= 0}">
                                            <button class="add-student-btn" disabled>
                                                <i class="fas fa-user-plus"></i>
                                                Lớp đã đầy
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/TrainingManagerClassroom?action=addStudents&classroomId=${classroom.classroomID}"
                                               class="add-student-btn" style="text-decoration: none; display: block; text-align: center;">
                                                <i class="fas fa-user-plus"></i>
                                                Thêm học sinh
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

</body>
</html>
