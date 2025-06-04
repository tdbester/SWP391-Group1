<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Trang Cá Nhân | TALENT01</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/assets/css/profile.css" rel="stylesheet">
</head>
<body>

<header>
  <div class="logo">TALENT01</div>
  <div class="nav">
    <a href="home.jsp">Trang Chủ</a>
    <a href="#">Dịch Vụ</a>
    <a href="#">Khóa Học</a>
    <a href="#">Sự Kiện</a>
    <a href="${pageContext.request.contextPath}/logout">Đăng Xuất</a>
  </div>
</header>

<div class="container">
  <div class="sidebar">
    <a href="#profile-section" class="active">Trang cá nhân</a>
    <a href="#password-section">Đổi mật khẩu</a>
  </div>

  <div class="profile-content">
    <!-- thông tin cá nhân -->
    <div id="profile-section" class="profile-card">
      <div class="profile-header">
        <div class="avatar">Ảnh</div>
        <h2>Thông tin cá nhân</h2>
      </div>

      <% if (request.getAttribute("success") != null) { %>
      <div class="alert alert-success">
        <%= request.getAttribute("success") %>
      </div>
      <% } %>

      <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-error">
        <%= request.getAttribute("error") %>
      </div>
      <% } %>

      <%
        org.example.talentcenter.model.Account account =
                (org.example.talentcenter.model.Account) session.getAttribute("account");
      %>

      <form method="post" action="${pageContext.request.contextPath}/profile">
        <label for="name">Họ và tên:</label>
        <input type="text" id="name" name="name"
               value="<%= (account != null && account.getFullName() != null) ? account.getFullName() : "" %>" required>

        <label for="phone">Số điện thoại:</label>
        <input type="text" id="phone" name="phone"
               value="<%= (account != null && account.getPhoneNumber() != null) ? account.getPhoneNumber() : "" %>"
               pattern="^0\d{9}$" required>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email"
               value="<%= (account != null && account.getEmail() != null) ? account.getEmail() : "" %>" required>

        <label for="address">Địa chỉ:</label>
        <input type="text" id="address" name="address"
               value="<%= (account != null && account.getAddress() != null) ? account.getAddress() : "" %>" required>

        <button type="submit" class="btn-save">Lưu</button>
      </form>
    </div>

    <!-- đổi mật khẩu -->
    <div id="password-section" class="profile-card">
      <div class="profile-header">
        <h2>Đổi mật khẩu</h2>
      </div>

      <% if (request.getAttribute("passwordSuccess") != null) { %>
      <div class="alert alert-success">
        <%= request.getAttribute("passwordSuccess") %>
      </div>
      <% } %>

      <% if (request.getAttribute("passwordError") != null) { %>
      <div class="alert alert-error">
        <%= request.getAttribute("passwordError") %>
      </div>
      <% } %>

      <form method="post" action="${pageContext.request.contextPath}/changePassword">
        <label for="currentPassword">Mật khẩu hiện tại:</label>
        <input type="password" name="currentPassword" required>

        <label for="newPassword">Mật khẩu mới:</label>
        <input type="password" name="newPassword" required>

        <label for="confirmPassword">Xác nhận mật khẩu mới:</label>
        <input type="password" name="confirmPassword" required>

        <button type="submit" class="btn-save">Đổi mật khẩu</button>
      </form>
    </div>
  </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/profile.js"></script>
</body>
</html>