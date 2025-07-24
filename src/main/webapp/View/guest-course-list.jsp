<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách khóa học - TalentCenter</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        .page-header {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        
        .page-title {
            color: white;
            font-weight: 700;
            font-size: 2.5rem;
            text-align: center;
            margin-bottom: 0.5rem;
        }
        
        .page-subtitle {
            color: rgba(255, 255, 255, 0.8);
            text-align: center;
            font-size: 1.1rem;
        }
        
        .search-section {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        .course-card {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
            margin-bottom: 2rem;
            height: 100%;
        }
        
        .course-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
        }
        
        .course-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-bottom: 3px solid #f8f9fa;
        }
        
        .course-content {
            padding: 1.5rem;
        }
        
        .category-badge {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 500;
            display: inline-block;
            margin-bottom: 1rem;
        }
        
        .level-badge {
            background: #28a745;
            color: white;
            padding: 0.2rem 0.6rem;
            border-radius: 15px;
            font-size: 0.75rem;
            font-weight: 500;
            margin-left: 0.5rem;
        }
        
        .level-badge.BEGINNER { background: #28a745; }
        .level-badge.INTERMEDIATE { background: #ffc107; color: #000; }
        .level-badge.ADVANCED { background: #dc3545; }
        
        .course-title {
            font-size: 1.3rem;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 0.8rem;
            line-height: 1.4;
        }
        
        .course-title a {
            color: inherit;
            text-decoration: none;
            transition: color 0.3s ease;
        }
        
        .course-title a:hover {
            color: #667eea;
        }
        
        .course-description {
            color: #6c757d;
            font-size: 0.95rem;
            line-height: 1.6;
            margin-bottom: 1rem;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        
        .course-price {
            font-size: 1.2rem;
            font-weight: 700;
            color: #e74c3c;
            margin-bottom: 1rem;
        }
        
        .course-meta {
            font-size: 0.85rem;
            color: #95a5a6;
            margin-bottom: 1rem;
        }
        
        .read-more-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 0.6rem 1.2rem;
            border-radius: 25px;
            text-decoration: none;
            font-weight: 500;
            font-size: 0.9rem;
            transition: all 0.3s ease;
            display: inline-block;
        }
        
        .read-more-btn:hover {
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .no-courses {
            text-align: center;
            padding: 4rem 2rem;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        .no-courses i {
            font-size: 4rem;
            color: #dee2e6;
            margin-bottom: 1rem;
        }
        
        .no-courses h3 {
            color: #6c757d;
            margin-bottom: 1rem;
        }
        
        .no-courses p {
            color: #95a5a6;
            margin-bottom: 2rem;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 10px;
            font-weight: 500;
        }
        
        .btn-primary:hover {
            background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
            transform: translateY(-2px);
        }
    </style>
</head>
<body>

<!-- Header -->
<jsp:include page="header.jsp"/>

<!-- Page Header -->
<div class="page-header">
    <div class="container">
        <h1 class="page-title">
            <i class="fas fa-graduation-cap"></i> Danh sách khóa học
        </h1>
        <p class="page-subtitle">Khám phá các khóa học chất lượng cao tại TalentCenter</p>
    </div>
</div>

<div class="container">
    <!-- Search and Filter Section -->
    <div class="search-section">
        <form method="get" action="${pageContext.request.contextPath}/guest-courses">
            <div class="row g-3">
                <div class="col-md-4">
                    <label for="search" class="form-label">
                        <i class="fas fa-search"></i> Tìm kiếm khóa học
                    </label>
                    <input type="text" class="form-control" id="search" name="search" 
                           placeholder="Nhập tên khóa học..." value="${searchQuery}">
                </div>
                <div class="col-md-3">
                    <label for="category" class="form-label">
                        <i class="fas fa-tags"></i> Danh mục
                    </label>
                    <select class="form-select" id="category" name="category">
                        <option value="">Tất cả danh mục</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}" 
                                    <c:if test="${selectedCategory == cat.id}">selected</c:if>>
                                ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="level" class="form-label">
                        <i class="fas fa-layer-group"></i> Cấp độ
                    </label>
                    <select class="form-select" id="level" name="level">
                        <option value="">Tất cả cấp độ</option>
                        <option value="BEGINNER" <c:if test="${selectedLevel == 'BEGINNER'}">selected</c:if>>Cơ bản</option>
                        <option value="INTERMEDIATE" <c:if test="${selectedLevel == 'INTERMEDIATE'}">selected</c:if>>Trung cấp</option>
                        <option value="ADVANCED" <c:if test="${selectedLevel == 'ADVANCED'}">selected</c:if>>Nâng cao</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label">&nbsp;</label>
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search"></i> Tìm kiếm
                    </button>
                </div>
            </div>
        </form>
    </div>

    <!-- Course List -->
    <div class="row">
        <c:choose>
            <c:when test="${not empty courseList}">
                <c:forEach var="course" items="${courseList}">
                    <div class="col-lg-4 col-md-6">
                        <div class="course-card">
                            <c:if test="${not empty course.image}">
                                <img src="${course.image}" class="course-image" alt="${course.title}">
                            </c:if>
                            <c:if test="${empty course.image}">
                                <div class="course-image d-flex align-items-center justify-content-center" 
                                     style="background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);">
                                    <i class="fas fa-graduation-cap" style="font-size: 2rem; color: #dee2e6;"></i>
                                </div>
                            </c:if>
                            
                            <div class="course-content">
                                <span class="category-badge">${course.category.name}</span>
                                <c:if test="${not empty course.level}">
                                    <span class="level-badge ${course.level}">
                                        <c:choose>
                                            <c:when test="${course.level == 'BEGINNER'}">Cơ bản</c:when>
                                            <c:when test="${course.level == 'INTERMEDIATE'}">Trung cấp</c:when>
                                            <c:when test="${course.level == 'ADVANCED'}">Nâng cao</c:when>
                                            <c:otherwise>${course.level}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </c:if>
                                
                                <h3 class="course-title">
                                    <a href="${pageContext.request.contextPath}/course-detail?id=${course.id}">
                                        ${course.title}
                                    </a>
                                </h3>
                                <div class="course-price">
                                    <fmt:formatNumber value="${course.price}" type="number" groupingUsed="true" /> VNĐ
                                </div>
                                
                                <div class="course-meta">
                                    <i class="fas fa-user"></i> ${course.fullname}
                                </div>
                                
                                <a href="${pageContext.request.contextPath}/courses?action=view&id=${course.id}"
                                   class="read-more-btn">
                                    Xem chi tiết <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12">
                    <div class="no-courses">
                        <i class="fas fa-graduation-cap"></i>
                        <h3>Chưa có khóa học nào</h3>
                        <p>Hiện tại chưa có khóa học nào được đăng tải. Vui lòng quay lại sau!</p>
                        <a href="${pageContext.request.contextPath}/home" class="read-more-btn">
                            <i class="fas fa-home"></i> Về trang chủ
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp"/>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
