<%--
Created by IntelliJ IDEA.
User: admin
Date: 6/21/2025
Time: 10:59 PM
To change this template use File | Settings | File Templates.
--%>

<%--/*
* Copyright (C) 2025 <Group1>
 * All rights reserved.
 *
 * This file is part of the <TalentCenterManagement> project.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * Created on: 2025-06-21
 * Author: Cù Thị Huyền Trang
 *
 * ========================== Change History ==========================
 * Date | Author | Description
 * ------------|----------------------|----------------------------------
 * 2025-06-21 | Cù Thị Huyền Trang | Initial creation
 */--%>

<%@ page import="java.util.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.talentcenter.model.StudentAttendanceReport" %>
<%
    @SuppressWarnings("unchecked")
    List<StudentAttendanceReport> attendanceReports = (List<StudentAttendanceReport>) request.getAttribute("attendanceReports");
    if (attendanceReports == null) {
        attendanceReports = new ArrayList<>();
    }

    Integer selectedYear = (Integer) request.getAttribute("selectedYear");
    if (selectedYear == null) selectedYear = LocalDate.now().getYear();

// Lấy thông tin môn học từ record đầu tiên
    String courseTitle = "Báo cáo điểm danh";

// Sắp xếp records theo ngày
    attendanceReports.sort((r1, r2) -> r1.getDate().compareTo(r2.getDate()));

// Tính toán thống kê
    int totalSessions = attendanceReports.size();
    int presentCount = 0;
    int absentCount = 0;
    int lateCount = 0;

    for (StudentAttendanceReport report : attendanceReports) {
        if (report.getStatus() != null) {
            switch (report.getStatus().toLowerCase()) {
                case "present":
                    presentCount++;
                    break;
                case "absent":
                    absentCount++;
                    break;
                case "late":
                    lateCount++;
                    break;
            }
        }
    }

// Tính phần trăm
    double presentPercent = totalSessions > 0 ? (double) presentCount / totalSessions * 100 : 0;
    double absentPercent = totalSessions > 0 ? (double) absentCount / totalSessions * 100 : 0;
    double latePercent = totalSessions > 0 ? (double) lateCount / totalSessions * 100 : 0;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
%>
<html lang="vi">
<head>
    <%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Báo cáo điểm danh - TALENT01</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/student-attendance-report.css">

</head>
<body>
<jsp:include page="header.jsp" />

<div class="container">
    <jsp:include page="student-sidebar.jsp" />
    <div class="main-content">
        <a href="StudentDashboard" class="back-link">
            <i class="fas fa-arrow-left"></i>
            Quay lại Dashboard
        </a>
        <div class="header-section">
            <div>
                <h1 class="header-title"><i class="fas fa-chart-bar"></i> Báo cáo điểm danh</h1>
                <div class="course-info"><%=courseTitle%></div>
            </div>
            <div class="filter-section">
                <div class="view-options">
                    <button class="view-option active">
                        <i class="fas fa-th"></i>
                    </button>
                    <button class="view-option">
                        <i class="fas fa-list"></i>
                    </button>
                </div>
            </div>
        </div>

        <form method="GET" action="${pageContext.request.contextPath}/StudentAttendanceReport"
              style="display: flex; gap: 15px; align-items: center; margin-bottom: 20px; padding: 15px; background: #f8f9fa; border-radius: 8px;">
            <div style="display: flex; align-items: center; gap: 8px;">
                <label for="class" style="font-weight: 600;">Lọc theo lớp:</label>
                <select name="class" id="class" onchange="this.form.submit()"
                        style="padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px;">
                    <option value="">Tất cả lớp</option>
                    <%
                        List<String> classNames = (List<String>) request.getAttribute("classNames");
                        String selectedClass = (String) request.getAttribute("selectedClass");
                        for (String className : classNames) {
                    %>
                    <option value="<%=className%>" <%= className.equals(selectedClass) ? "selected" : "" %>><%=className%></option>
                    <% } %>
                </select>
            </div>

            <div style="display: flex; align-items: center; gap: 8px;">
                <label for="year" style="font-weight: 600;">Năm:</label>
                <select name="year" id="year" onchange="this.form.submit()"
                        style="padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px;">
                    <%
                        int currentYear = LocalDate.now().getYear();
                        for (int y = currentYear - 2; y <= currentYear + 1; y++) {
                    %>
                    <option value="<%=y%>" <%= y == selectedYear ? "selected" : "" %>><%=y%></option>
                    <% } %>
                </select>
            </div>

            <a href="${pageContext.request.contextPath}/StudentAttendanceReport"
               style="padding: 8px 16px; background: #6c757d; border: none; text-decoration: none; color: white; border-radius: 4px; font-weight: 500;">
                <i class="fas fa-undo"></i> Bỏ lọc
            </a>
        </form>

        <div class="attendance-grid">
            <%
                if (attendanceReports.isEmpty()) {
            %>
            <div class="attendance-card">
                <div class="card-date">Không có dữ liệu điểm danh</div>
                <div class="card-session">
                    <i class="fas fa-info-circle"></i> Chưa có buổi học nào được ghi nhận
                </div>
            </div>
            <%
            } else {
                for (StudentAttendanceReport report : attendanceReports) {
                    String statusClass = "status-" + (report.getStatus() != null ? report.getStatus().toLowerCase() : "unknown");
                    String statusText = "";
                    if (report.getStatus() != null) {
                        switch(report.getStatus().toLowerCase()) {
                            case "present": statusText = "Có mặt"; break;
                            case "absent": statusText = "Vắng mặt"; break;
                            case "late": statusText = "Đi muộn"; break;
                            default: statusText = "Chưa xác định"; break;
                        }
                    } else {
                        statusText = "Chưa điểm danh";
                        statusClass = "status-unknown";
                    }

                    // Lấy tên thứ trong tuần bằng tiếng Việt
                    String dayOfWeek = "";
                    switch(report.getDate().getDayOfWeek()) {
                        case MONDAY: dayOfWeek = "Thứ 2"; break;
                        case TUESDAY: dayOfWeek = "Thứ 3"; break;
                        case WEDNESDAY: dayOfWeek = "Thứ 4"; break;
                        case THURSDAY: dayOfWeek = "Thứ 5"; break;
                        case FRIDAY: dayOfWeek = "Thứ 6"; break;
                        case SATURDAY: dayOfWeek = "Thứ 7"; break;
                        case SUNDAY: dayOfWeek = "Chủ nhật"; break;
                    }
            %>
            <div class="attendance-card">
                <div class="card-date"><%=dayOfWeek%> - <%=report.getDate().format(dateFormatter)%></div>
                <div class="card-session">
                    <i class="fas fa-clock"></i> <%=report.getSlotStartTime().format(timeFormatter)%>-<%=report.getSlotEndTime().format(timeFormatter)%>
                </div>
                <div class="card-time"><%=report.getRoomCode() != null ? report.getRoomCode() : "N/A"%></div>
                <div class="card-room"><%=report.getTeacherName() != null ? report.getTeacherName() : "N/A"%></div>
                <div class="card-room">Lớp: <%=report.getClassName() != null ? report.getClassName() : "N/A"%></div>
                <div class="status-badge <%=statusClass%>"><%=statusText%></div>
            </div>
            <%
                    }
                }
            %>
        </div>

        <div class="statistics-section">
            <h2 class="statistics-title"><i class="fas fa-chart-pie"></i> Thống kê điểm danh</h2>
            <div class="stats-grid">
                <div class="stat-card stat-present">
                    <div class="stat-icon">
                        <i class="fas fa-check"></i>
                    </div>
                    <div class="stat-number"><%=presentCount%></div>
                    <div class="stat-label">Có mặt</div>
                    <div class="stat-percent"><%=Math.round(presentPercent)%>%</div>
                </div>

                <div class="stat-card stat-absent">
                    <div class="stat-icon">
                        <i class="fas fa-times"></i>
                    </div>
                    <div class="stat-number"><%=absentCount%></div>
                    <div class="stat-label">Vắng mặt</div>
                    <div class="stat-percent"><%=Math.round(absentPercent)%>%</div>
                </div>

                <div class="stat-card stat-late">
                    <div class="stat-icon">
                        <i class="fas fa-clock"></i>
                    </div>
                    <div class="stat-number"><%=lateCount%></div>
                    <div class="stat-label">Đi trễ</div>
                    <div class="stat-percent"><%=Math.round(latePercent)%>%</div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />

<script>
    document.querySelectorAll('.view-option').forEach(option => {
        option.addEventListener('click', function() {
            document.querySelectorAll('.view-option').forEach(opt => opt.classroomList.remove('active'));
            this.classroomList.add('active');

            const grid = document.querySelector('.attendance-grid');
            if (this.querySelector('i').classroomList.contains('fa-list')) {
                grid.style.gridTemplateColumns = '1fr';
            } else {
                grid.style.gridTemplateColumns = 'repeat(auto-fit, minmax(280px, 1fr))';
            }
        });
    });
</script>

</body>
</html>
