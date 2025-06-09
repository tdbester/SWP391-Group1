<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Login | TALENT01</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>
<header class="top-header">
    <div class="header-content">
        <div class="logo">TALENT01</div>
        <a href="home.jsp" class="back-home-btn">← Về Trang Chủ</a>
    </div>
</header>

<div class="container">
    <div class="login form">
        <header>Đăng nhập</header>
        <%
            if (request.getAttribute("error")!= null){
                String er=(String)request.getAttribute("error");
        %>
        <div class="error-message"><%= er %></div>
        <%
            }
        %>
        <form action="login" method="post">
            <input type="text" name="email" pattern="[^ @]*@[^ @]*" placeholder="Email" required>
            <input type="password" name="password" placeholder="Mật khẩu" required>
            <a href="requestPassword.jsp">Quên mật khẩu?</a>
            <input type="submit" class="button" value="Đăng nhập">
        </form>
    </div>
</div>

<footer class="bottom-footer">
    <p>Copyright © 2025 Talent Center Management. SWP391-Group 01.</p>
</footer>
</body>
</html>