<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/21/2025
  Time: 3:37 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ page import="org.example.talentcenter.model.Request" %>

<%
    ArrayList<Request> requestList = (ArrayList<Request>) request.getAttribute("requestList");
    if (requestList == null) requestList = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

<html lang="vi">
<head>
    <title>Danh sách đơn đã gửi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/table.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<jsp:include page="student-sidebar.jsp"/>

<div class="main-content">
    <div class="container">
        <h2>Danh sách đơn đã gửi</h2>
        <table border="1" cellpadding="8" cellspacing="0" style="width: 100%; border-collapse: collapse;">
            <thead>
            <tr>
                <th>Lý do</th>
                <th>Trạng thái</th>
                <th>Ngày tạo</th>
                <th>Ghi chú xử lý</th>
                <th>Ngày xử lý</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (requestList.isEmpty()) {
            %>
            <tr>
                <td colspan="6" style="text-align: center;">Chưa có đơn nào được gửi</td>
            </tr>
            <%
            } else {
                for (Request r : requestList) {
            %>
            <tr>
                <td><%= r.getReason() != null ? r.getReason() : "" %></td>
                <td><%= r.getStatus() != null ? r.getStatus() : "" %></td>
                <td><%= r.getCreatedAt() != null ? dateFormat.format(r.getCreatedAt()) : "" %></td>
                <td><%= r.getResponse() != null ? r.getResponse() : "Chưa phản hồi" %></td>
                <td><%= r.getResponseAt() != null ? dateFormat.format(r.getResponseAt()) : "Chưa xử lý" %></td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
