<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/1/2025
  Time: 11:33 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Sidebar and Header Demo</title>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<style>
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background: #7a6ad8;
    min-height: 100vh;
    color: #333;
  }

  .dashboard {
    display: flex;
    min-height: 100vh;
  }

  /* SIDEBAR STYLES */
  .sidebar {
    width: 280px;
    background: #7a6ad8;
    backdrop-filter: blur(10px);
    border-right: 1px solid rgba(255, 255, 255, 0.1);
    padding: 20px;
    color: white;
    position: fixed;
    height: 100vh;
    overflow-y: auto;
  }

  .logo {
    display: flex;
    align-items: center;
    margin-bottom: 30px;
    padding-bottom: 20px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  }

  .logo i {
    font-size: 2rem;
    margin-right: 10px;
    color: #4CAF50;
  }

  .logo h2 {
    font-size: 1.5rem;
    font-weight: 600;
  }

  .nav-section {
    margin-bottom: 25px;
  }

  .nav-section-title {
    font-size: 0.9rem;
    color: rgba(255, 255, 255, 0.6);
    text-transform: uppercase;
    letter-spacing: 1px;
    margin-bottom: 10px;
    font-weight: 600;
  }

  .nav-item {
    display: flex;
    align-items: center;
    padding: 12px 15px;
    margin-bottom: 5px;
    border-radius: 10px;
    cursor: pointer;
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
    text-decoration: none;
    color: inherit;
  }

  .nav-item:hover {
    background: rgba(255, 255, 255, 0.1);
    transform: translateX(5px);
  }

  .nav-item.active {
    background: linear-gradient(135deg, #4CAF50, #45a049);
    box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
  }

  .nav-item i {
    margin-right: 12px;
    font-size: 1rem;
    width: 18px;
  }

  .nav-item span {
    font-size: 0.9rem;
  }


</style>


<div class="dashboard">
  <!-- SIDEBAR -->
  <div class="sidebar">

    <div class="logo">
      <i class="fas fa-users-cog"></i>
      <h2>Trung Tâm Năng Khiếu</h2>
    </div>
    <nav>
      <div class="nav-section">
        <a href="DashBoardTeacher.jsp" class="nav-item ${pageContext.request.requestURI.contains('DashBoardTeacher.jsp') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('DashBoardTeacher.jsp') ? 'page' : 'false'}">
          <i class="fas fa-tachometer-alt" aria-hidden="true"></i>
          <span>Dashboard</span>
        </a>
      </div>
      <div class="nav-section">
        <a href="${pageContext.request.contextPath}/Consultation?action=list"
           class="nav-item ${pageContext.request.requestURI.contains('consultation-list.jsp') ? 'active' : ''}"
           role="link"
           aria-current="${pageContext.request.requestURI.contains('consultation-list.jsp') ? 'page' : 'false'}">
          <i class="fa-solid fa-building" aria-hidden="true"></i>
          <span>Danh sách đăng kí tư vấn</span>
        </a>
      </div>
      <div class="nav-section">
        <a href="CandidateDetails.jsp" class="nav-item ${pageContext.request.requestURI.contains('CandidateDetails.jsp') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('CandidateDetails.jsp') ? 'page' : 'false'}">
          <i class="fas fa-user" aria-hidden="true"></i>
          <span>Danh sách lớp</span>
        </a>
      </div>
      <div class="nav-section">
        <a href="SearchCandidate.jsp" class="nav-item ${pageContext.request.requestURI.contains('SearchCandidate.jsp') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('SearchCandidate.jsp') ? 'page' : 'false'}">
          <i class="fas fa-search" aria-hidden="true"></i>
          <span>Quản lý Blog</span>
        </a>
      </div>
      <div class="nav-section">
        <a href="JobList.jsp" class="nav-item ${pageContext.request.requestURI.contains('JobList.jsp') ? 'active' : ''}" role="link" aria-current="${pageContext.request.requestURI.contains('JobList.jsp') ? 'page' : 'false'}">
          <i class="fas fa-briefcase" aria-hidden="true"></i>
          <span>Yêu cầu cấp tài khoản học viên</span>
        </a>
      </div>

    </nav>
  </div>
  <script>
    document.querySelectorAll('.nav-item').forEach(item => {
      item.addEventListener('click', function() {
        document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
        this.classList.add('active');
      );
      });
      function toggleSidebar() {
        elector('.sidebar').classList.toggle('active');
      }
  </script>
  <!-- MAIN CONTENT WITH HEADER -->
