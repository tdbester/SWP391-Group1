<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blog - Talent Center</title>
    
    <!-- Bootstrap core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Additional CSS Files -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/fontawesome.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/templatemo-scholar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/owl.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/animate.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Lora:ital,wght@0,400..700;1,400..700&display=swap" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f8f9fa;
        }
        
        .page-header {
            background: linear-gradient(135deg, #9B7BFA 0%, #7A5AF8 100%);
            color: white;
            padding: 60px 0;
            margin-bottom: 40px;
        }
        
        .page-header h1 {
            font-family: 'Lora', serif;
            font-weight: 700;
            font-size: 2.5rem;
            margin-bottom: 10px;
        }
        
        .page-header p {
            font-size: 1.1rem;
            opacity: 0.9;
        }
        
        .search-filter-section {
            background: white;
            border-radius: 12px;
            padding: 25px;
            margin-bottom: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }
        
        .blog-card {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            margin-bottom: 30px;
            height: 100%;
        }
        
        .blog-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        
        .blog-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }
        
        .blog-content {
            padding: 25px;
        }
        
        .blog-title {
            font-family: 'Lora', serif;
            font-weight: 600;
            font-size: 1.3rem;
            color: #2c3e50;
            margin-bottom: 12px;
            line-height: 1.4;
        }
        
        .blog-title a {
            color: inherit;
            text-decoration: none;
        }
        
        .blog-title a:hover {
            color: #7A5AF8;
        }
        
        .blog-description {
            color: #6c757d;
            font-size: 0.95rem;
            line-height: 1.6;
            margin-bottom: 15px;
        }
        
        .blog-meta {
            font-size: 0.85rem;
            color: #8e9aaf;
            margin-bottom: 15px;
        }
        
        .blog-meta i {
            margin-right: 5px;
        }
        
        .read-more-btn {
            background: linear-gradient(135deg, #9B7BFA 0%, #7A5AF8 100%);
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 20px;
            font-size: 0.9rem;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        
        .read-more-btn:hover {
            color: white;
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(122, 90, 248, 0.3);
        }
        
        .no-blogs {
            text-align: center;
            padding: 60px 20px;
            color: #6c757d;
        }
        
        .no-blogs i {
            font-size: 4rem;
            margin-bottom: 20px;
            color: #dee2e6;
        }
        
        .category-badge {
            background: #e9ecef;
            color: #495057;
            padding: 4px 12px;
            border-radius: 15px;
            font-size: 0.8rem;
            display: inline-block;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<!-- Include existing header -->
<jsp:include page="header.jsp"/>

<!-- Page Header -->
<div class="page-header">
    <div class="container">
        <div class="row">
            <div class="col-12 text-center">
                <h1><i class="fas fa-music"></i> Blog Âm Nhạc</h1>
                <p>Khám phá kiến thức âm nhạc, mẹo luyện tập và cảm hứng học nhạc từ các chuyên gia</p>
            </div>
        </div>
    </div>
</div>

<!-- Main Content -->
<div class="container">
    <!-- Search and Filter Section -->
    <div class="search-filter-section">
        <form method="GET" action="${pageContext.request.contextPath}/guest-blogs">
            <div class="row align-items-end">
                <div class="col-md-6">
                    <label for="search" class="form-label">Tìm kiếm bài viết</label>
                    <input type="text" class="form-control" id="search" name="search" 
                           placeholder="Nhập từ khóa..." value="${searchQuery}">
                </div>
                <div class="col-md-4">
                    <label for="category" class="form-label">Danh mục</label>
                    <select class="form-select" id="category" name="category">
                        <option value="">Tất cả danh mục</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}" ${selectedCategory == cat.id ? 'selected' : ''}>
                                ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search"></i> Tìm kiếm
                    </button>
                </div>
            </div>
        </form>
    </div>

    <!-- Blog List -->
    <div class="row">
        <c:choose>
            <c:when test="${not empty blogList}">
                <c:forEach var="blog" items="${blogList}">
                    <div class="col-lg-4 col-md-6">
                        <div class="blog-card">
                            <c:if test="${not empty blog.image}">
                                <img src="${blog.image}" class="blog-image" alt="${blog.title}">
                            </c:if>
                            <c:if test="${empty blog.image}">
                                <div class="blog-image d-flex align-items-center justify-content-center" 
                                     style="background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);">
                                    <i class="fas fa-music" style="font-size: 2rem; color: #dee2e6;"></i>
                                </div>
                            </c:if>
                            
                            <div class="blog-content">
                                <c:if test="${not empty categories}">
                                    <c:forEach var="cat" items="${categories}">
                                        <c:if test="${cat.id == blog.category}">
                                            <span class="category-badge">${cat.name}</span>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                                
                                <h3 class="blog-title">
                                    <a href="${pageContext.request.contextPath}/blog-detail?id=${blog.id}">
                                        ${blog.title}
                                    </a>
                                </h3>
                                
                                <p class="blog-description">
                                    ${blog.description}
                                </p>
                                
                                <div class="blog-meta">
                                    <i class="fas fa-user"></i> ${blog.fullname}
                                    <span class="mx-2">•</span>
                                    <i class="fas fa-calendar"></i> 
                                    <fmt:formatDate value="${blog.createdAt}" pattern="dd/MM/yyyy"/>
                                </div>
                                
                                <a href="${pageContext.request.contextPath}/blogs?action=view&id=${blog.id}"
                                   class="read-more-btn">
                                    Đọc thêm <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12">
                    <div class="no-blogs">
                        <i class="fas fa-music"></i>
                        <h3>Chưa có bài viết nào</h3>
                        <p>Hiện tại chưa có bài viết nào được đăng tải. Vui lòng quay lại sau!</p>
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
