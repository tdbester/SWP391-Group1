<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<style>
  /* Header styles */
  header {
    background: #7A5AF8;
    color: white;
    padding: 16px 32px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: sticky;
    top: 0;
    z-index: 100;
  }

  .logo {
    font-weight: 600;
    font-size: 20px;
  }

  .nav {
    display: flex;
    gap: 24px;
  }

  .nav a {
    color: white;
    text-decoration: none;
    font-weight: 500;
  }

  .nav a:hover {
    opacity: 0.8;
    transition: opacity 0.3s ease;
  }

  /* Responsive design for header */
  @media (max-width: 768px) {
    header {
      flex-direction: column;
      gap: 16px;
      padding: 16px;
    }

    .nav {
      gap: 16px;
    }

    .nav a {
      font-size: 14px;
    }
  }
</style>

<header>
  <div class="logo">TALENT01</div>
  <div class="nav">
    <a href="<%=request.getContextPath()%>/View/home.jsp">Trang Chủ</a>
    <a href="#">Dịch Vụ</a>
    <a href="#">Khóa Học</a>
    <%
      String role = (String) session.getAttribute("userRole");
      String dashboardURL = "View/home.jsp";
      if ("teacher".equalsIgnoreCase(role)) {
        dashboardURL = request.getContextPath() + "/View/teacher-dashboard.jsp";
      } else if ("sale".equalsIgnoreCase(role)) {
        dashboardURL ="/View/sale-dashboard.jsp";
      }
    %>
    <a href="<%=dashboardURL%>">Dashboard</a>
    <a href="<%=request.getContextPath()%>/logout">Đăng Xuất</a>
  </div>
</header>