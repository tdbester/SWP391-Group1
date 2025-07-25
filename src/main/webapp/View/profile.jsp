<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Trang Cá Nhân | TALENT01</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/assets/css/profile.css" rel="stylesheet">
  <style>
    .avatar-container {
      position: relative;
      display: inline-block;
      margin-bottom: 20px;
    }

    .avatar-preview {
      width: 150px;
      height: 150px;
      border-radius: 50%;
      object-fit: cover;
      border: 3px solid #ddd;
      background-color: #f0f0f0;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 48px;
      color: #888;
    }

    .avatar-upload {
      position: absolute;
      bottom: 10px;
      right: 10px;
      background: #007bff;
      color: white;
      border: none;
      border-radius: 50%;
      width: 40px;
      height: 40px;
      cursor: pointer;
      font-size: 18px;
    }

    .avatar-upload:hover {
      background: #0056b3;
    }

    #avatar-input {
      display: none;
    }

    .file-info {
      margin-top: 10px;
      font-size: 14px;
      color: #666;
    }
  </style>
</head>
<body>

<%@ include file="header.jsp" %>

<div class="container">
  <div class="sidebar">
    <a href="#profile-section" class="active">Trang cá nhân</a>
    <a href="#password-section">Đổi mật khẩu</a>
  </div>

  <div class="profile-content">
    <!-- thông tin cá nhân -->
    <div id="profile-section" class="profile-card">
      <div class="profile-header">
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

      <form method="post" action="profile" enctype="multipart/form-data">
        <!-- Avatar Section -->
        <div class="avatar-container">
          <% if (account != null && account.getAvatar() != null && !account.getAvatar().isEmpty()) { %>
          <img src="<%= account.getAvatar() %>" alt="Avatar" class="avatar-preview" id="avatar-preview">
          <% } else { %>
          <div class="avatar-preview" id="avatar-preview">📷</div>
          <% } %>
          <button type="button" class="avatar-upload" onclick="document.getElementById('avatar-input').click()">
            📸
          </button>
          <input type="file" id="avatar-input" name="avatar" accept="image/*" onchange="previewAvatar(this)">
        </div>
        <div class="file-info">
          <small>Chỉ chấp nhận file ảnh (JPG, PNG, GIF). Tối đa 5MB.</small>
        </div>

        <label for="name">Họ và tên:</label>
        <input type="text" id="name" name="name"
               value="<%= (account != null && account.getFullName() != null) ? account.getFullName() : "" %>" required>

        <label for="phone">Số điện thoại:</label>
        <input type="text" id="phone" name="phone"
               value="<%= (account != null && account.getPhoneNumber() != null) ? account.getPhoneNumber() : "" %>"
               pattern="^0\d{9}$" required>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email"
               value="<%= (account != null && account.getEmail() != null) ? account.getEmail() : "" %>" readonly>

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

      <form method="post" action="changePassword">
        <label for="currentPassword">Mật khẩu hiện tại:</label>
        <div class="password-field">
          <input type="password" id="currentPassword" name="currentPassword" required>
          <button type="button" class="toggle-pass" onclick="togglePassword('currentPassword', this)">👁</button>
        </div>

        <label for="newPassword">Mật khẩu mới:</label>
        <div class="password-field">
          <input type="password" id="newPassword" name="newPassword" required>
          <button type="button" class="toggle-pass" onclick="togglePassword('newPassword', this)">👁</button>
        </div>

        <label for="confirmPassword">Xác nhận mật khẩu mới:</label>
        <div class="password-field">
          <input type="password" id="confirmPassword" name="confirmPassword" required>
          <button type="button" class="toggle-pass" onclick="togglePassword('confirmPassword', this)">👁</button>
        </div>


        <button type="submit" class="btn-save">Đổi mật khẩu</button>
      </form>
    </div>

  </div>
</div>

<%@ include file="footer.jsp" %>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/profile.js"></script>
<script>
  //Avatar
  function previewAvatar(input) {
    if (input.files && input.files[0]) {
      const file = input.files[0];

      // Validate file type
      if (!file.type.startsWith('image/')) {
        alert('Chỉ được chọn file ảnh!');
        input.value = '';
        return;
      }
      // Validate file size (5MB)
      if (file.size > 5 * 1024 * 1024) {
        alert('Kích thước file không được vượt quá 5MB!');
        input.value = '';
        return;
      }
      const reader = new FileReader();
      reader.onload = function(e) {
        const preview = document.getElementById('avatar-preview');
        preview.innerHTML = '';
        const img = document.createElement('img');
        img.src = e.target.result;
        img.className = 'avatar-preview';
        img.style.width = '150px';
        img.style.height = '150px';
        img.style.objectFit = 'cover';
        img.style.borderRadius = '50%';
        preview.appendChild(img);
      };
      reader.readAsDataURL(file);
    }
  }

  function togglePassword(fieldId, btn) {
    const input = document.getElementById(fieldId);
    if (input.type === "password") {
      input.type = "text";
      btn.textContent = "🙈";
    } else {
      input.type = "password";
      btn.textContent = "👁";
    }
  }

</script>
</body>
</html>