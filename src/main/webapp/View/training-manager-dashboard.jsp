<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/9/2025
  Time: 3:57 PM
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
  <!-- Kế thừa hoặc nhúng lại CSS từ trang chính -->

</head>
<body>
<jsp:include page="header.jsp" />
<!-- SIDEBAR -->
<!-- Tách sidebar thành file riêng để tái sử dụng -->

<!-- dashboard -->
<div class="container">
  <jsp:include page="training-manager-sidebar.jsp" />
  <div class="main-content">
    <div class="content-area">
      <!-- Stats Cards cho Sale -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon"><i class="fa-solid fa-building-user"></i></div>
          <div class="stat-number" style="color: #3498db;">12</div>
          <div class="stat-label">Khách hàng mới</div>
          <div class="stat-status status-info">Trong tuần này</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon"><i class="fa-solid fa-phone-volume"></i></div>
          <div class="stat-number" style="color: #f39c12;">8</div>
          <div class="stat-label">Cuộc gọi chưa xử lý</div>
          <div class="stat-status status-warning">Cần gọi lại</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon"><i class="fa-solid fa-chart-line"></i></div>
          <div class="stat-number" style="color: #2ecc71;">5</div>
          <div class="stat-label">Hợp đồng thành công</div>
          <div class="stat-status status-good">Trong tháng này</div>
        </div>
      </div>

      <!-- Content Grid (tùy chỉnh tùy ý) -->
      <div class="content-grid">
        <!-- Lịch gặp khách hôm nay -->
        <div class="card">
          <h2 class="card-title">Lịch hẹn khách hôm nay</h2>
          <div class="schedule-item">
            <div class="schedule-time">09:00</div>
            <div class="schedule-info">
              <div class="schedule-title">Gặp khách hàng Công ty A</div>
              <div class="schedule-details">Tòa nhà Pearl Plaza, Quận Bình Thạnh</div>
            </div>
            <div class="schedule-actions">
              <button class="btn-success">Xác nhận</button>
              <button class="btn-warning">Hủy</button>
            </div>
          </div>
          <div class="schedule-item">
            <div class="schedule-time">14:00</div>
            <div class="schedule-info">
              <div class="schedule-title">Call tư vấn Công ty B</div>
              <div class="schedule-details">Qua Zoom, khách hàng Nhật</div>
            </div>
            <div class="schedule-actions">
              <button class="btn-primary">Tham gia</button>
              <button class="btn-warning">Trễ</button>
            </div>
          </div>
        </div>

        <!-- Yêu cầu báo giá -->
        <div class="card">
          <h2 class="card-title">Yêu cầu báo giá</h2>

          <div class="request-item">
            <div class="request-title">Công ty C - Website giới thiệu</div>
            <div class="request-date">Gửi lúc: 30/05/2025</div>
            <div class="request-status pending">Đang xử lý</div>
          </div>

          <div class="request-item">
            <div class="request-title">Công ty D - Giải pháp CRM</div>
            <div class="request-date">Gửi lúc: 29/05/2025</div>
            <div class="request-status approved">Đã báo giá</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="footer.jsp" />
</body>
</html>
