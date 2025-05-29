<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Login</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="assets/css/login.css">
</head>
<body>
<div class="container">
  <div class="login form">
    <header>Đổi mật khẩu</header>
    <%
      if (request.getAttribute("error")!= null){
        String er=(String)request.getAttribute("error");
    %>
    <h4 style="color: red"><%= er %></h3>
        <%
                        }
                    %>
      <form action="resetPassword" method="post">
        <input type="hidden" name="token" value="${sessionScope.token}" />
        <input type="email" name="email" value="$[email]" placeholder="Email" required>
        <input type="password" name="password" placeholder="Mật khẩu mới" required>
        <input type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu" required>
        <input type="submit" class="button" value="Đổi mật khẩu">
      </form>
  </div>
</div>
</body>
</html>