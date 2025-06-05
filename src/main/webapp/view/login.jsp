<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../assets/css/login.css">
</head>
<body>
<div class="container">
    <div class="login form">
        <header>Đăng nhập</header>
        <%
            if (request.getAttribute("error")!= null){
                String er=(String)request.getAttribute("error");
        %>
        <h5 style="color: red"><%= er %></h5>
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
</body>
</html>