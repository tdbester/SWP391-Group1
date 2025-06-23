<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 5/31/2025
  Time: 11:46 PM
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sale Dashboard - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<!-- dashboard -->
<div class="container">
    <jsp:include page="sale-sidebar.jsp" />
    <div class="main-content">
        <div class="welcome-section">
            <div class="welcome-card">
                <div class="welcome-text">
                    <h1>Xin chào nhân viên Sale Nguyen Van A!</h1>
                    <p>Hôm nay là ngày tuyệt vời để làm việc</p>
                    <div class="current-time">
                        <i class="fas fa-calendar-alt"></i>
                        <span id="currentDate"></span>
                    </div>
                </div>
                <div class="welcome-avatar">
                    <a href="<%=request.getContextPath()%>/profile">
                        <i class="fas fa-user-circle"></i>
                    </a>
                </div>
            </div>
        </div>
        <div class="content-area">
            <div class="sale-quick-nav">
                <h2>Quick Actions</h2>
                <div class="sale-nav-buttons">
                    <a href="consultationList.jsp" class="sale-nav-btn"><i class="fas fa-users"></i> View Consultations</a>
                    <a href="addConsultation.jsp" class="sale-nav-btn"><i class="fas fa-user-plus"></i> Add New Lead</a>
                    <a href="courseList.jsp" class="sale-nav-btn"><i class="fas fa-book-open"></i> Courses</a>
                </div>
            </div>

            <div class="sale-notifications">
                <h2>🔔 New Notifications</h2>
                <ul class="sale-notification-list">
                    <li><strong>03/06:</strong> 5 new registrations waiting for follow-up.</li>
                    <li><strong>02/06:</strong> Consultation success rate report updated.</li>
                    <li><strong>01/06:</strong> New summer courses added to the system.</li>
                </ul>
            </div>

            <div class="sale-new-courses">
                <h2>📋 Courses Needing Consultation</h2>
                <table class="sale-course-table">
                    <thead>
                    <tr>
                        <th>Course</th>
                        <th>Start Date</th>
                        <th>Slots Left</th>
                        <th>Interested Leads</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Piano for Beginners</td>
                        <td>10/07/2025</td>
                        <td>8</td>
                        <td>12</td>
                        <td><a href="#" class="sale-btn-view">View</a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>