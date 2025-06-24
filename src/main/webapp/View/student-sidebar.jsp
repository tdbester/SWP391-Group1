<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/12/2025
  Time: 10:08 PM
  To change this template use File | Settings | File Templates.
--%>
/*
*  Copyright (C) 2025 <Group 1>
    *  All rights reserved.
    *
    *  This file is part of the <Talent Center Management> project.
    *  Unauthorized copying of this file, via any medium is strictly prohibited.
    *  Proprietary and confidential.
    *
    *  Created on:        2025-06-12
    *  Author:            Cù Thị Huyền Trang
    *
    *  ========================== Change History ==========================
    *  Date        | Author               | Description
    *  ------------|----------------------|--------------------------------
    *  2025-06-12  | Cù Thị Huyền Trang   | Initial creation
    */

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="sidebar">

    <div class="logo">
        <i class="fas fa-users-cog"></i>
        <h2>Trung Tâm Năng Khiếu</h2>
    </div>
    <nav>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/View/student-dashboard.jsp"
               class="nav-item ${pageContext.request.requestURI.contains('student-dashboard.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('student-dashboard.jsp') ? 'page' : 'false'}">
                <i class="fas fa-tachometer-alt" aria-hidden="true"></i>
                <span>Dashboard</span>
            </a>
        </div>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/StudentSchedule"
               class="nav-item ${pageContext.request.requestURI.contains('student-schedule.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('student-schedule.jsp') ? 'page' : 'false'}">
                <i class="fa-solid fa-building" aria-hidden="true"></i>
                <span>Thời khoá biểu</span>
            </a>
        </div>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/StudentAttendanceReport"
               class="nav-item ${pageContext.request.requestURI.contains('student-attendance-report.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('student-attendance-report.jsp') ? 'page' : 'false'}">
                <i class="fas fa-search" aria-hidden="true"></i>
                <span>Báo cáo điểm danh</span>
            </a>
        </div>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/StudentApplication?action=application"
               class="nav-item ${pageContext.request.requestURI.contains('student-application.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('student-application.jsp') ? 'page' : 'false'}">
                <i class="fas fa-briefcase" aria-hidden="true"></i>
                <span>Chuyển lớp</span>
            </a>
        </div>
        <div class="nav-section">
            <a href="${pageContext.request.contextPath}/StudentApplication?action=list"
               class="nav-item ${pageContext.request.requestURI.contains('student-application-list.jsp') ? 'active' : ''}"
               role="link"
               aria-current="${pageContext.request.requestURI.contains('Student-application-list.jsp') ? 'page' : 'false'}">
                <i class="fas fa-user" aria-hidden="true"></i>
                <span>Xem đơn</span>
            </a>
        </div>
    </nav>
</div>
<script>
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', function () {
            document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
            this.classList.add('active');
        });
    });

    function toggleSidebar() {
        document.querySelector('.sidebar').classList.toggle('active');
    }
</script>

