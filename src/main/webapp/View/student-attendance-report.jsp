<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/21/2025
  Time: 10:59 PM
<%--  To change this template use File | Settings | File Templates.--%>
<%--&ndash;%&gt;--%>

<%--/*--%>
<%--*  Copyright (C) 2025 <Group1>--%>
<%--    *  All rights reserved.--%>
<%--    *--%>
<%--    *  This file is part of the <TalentCenterManagement> project.--%>
<%--    *  Unauthorized copying of this file, via any medium is strictly prohibited.--%>
<%--    *  Proprietary and confidential.--%>
<%--    *--%>
<%--    *  Created on:        2025-06-21--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-06-21  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */--%>

<%@ page import="java.util.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.talentcenter.model.StudentAttendanceReport" %>
<%@ page import="java.time.temporal.WeekFields" %>
<%
    @SuppressWarnings("unchecked")
    List<StudentAttendanceReport> attendanceReports = (List<StudentAttendanceReport>) request.getAttribute("attendanceReports");
    if (attendanceReports == null) {
        attendanceReports = new ArrayList<>();
    }

    Integer selectedYear = (Integer) request.getAttribute("selectedYear");
    Integer selectedWeekNumber = (Integer) request.getAttribute("selectedWeekNumber");
    Integer totalWeeksInYear = (Integer) request.getAttribute("totalWeeksInYear");

    if (selectedYear == null) selectedYear = LocalDate.now().getYear();
    if (selectedWeekNumber == null) selectedWeekNumber = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfYear());
    if (totalWeeksInYear == null) totalWeeksInYear = 52;

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
    <style>
        .main-content {
            margin-left: 320px;
            background-color: #f5f5f5;
            min-height: 100vh;
        }

        .container {
            max-width: 1200px;
            margin: auto;
        }


        .header-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #e9ecef;
        }

        .header-title {
            color: #2c3e50;
            font-size: 24px;
            font-weight: bold;
            margin: 0;
        }

        .course-info {
            color: #7f8c8d;
            font-size: 14px;
            margin-top: 5px;
        }

        .filter-section {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .filter-btn {
            padding: 8px 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background: white;
            cursor: pointer;
            font-size: 14px;
        }

        .view-options {
            display: flex;
            gap: 10px;
        }

        .view-option {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background: white;
            cursor: pointer;
            font-size: 14px;
        }

        .view-option.active {
            background: #007bff;
            color: white;
            border-color: #007bff;
        }

        .attendance-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }

        .attendance-card {
            background: white;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 20px;
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .attendance-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        .card-date {
            font-size: 16px;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 5px;
        }

        .card-session {
            font-size: 14px;
            color: #7f8c8d;
            margin-bottom: 8px;
        }

        .card-time {
            font-size: 12px;
            color: #95a5a6;
            margin-bottom: 8px;
        }

        .card-room {
            font-size: 12px;
            color: #95a5a6;
            margin-bottom: 15px;
        }

        .status-badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: bold;
            text-transform: uppercase;
        }

        .status-present {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .status-absent {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .status-late {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }

        .status-unknown {
            background-color: #f8f9fa;
            color: #6c757d;
            border: 1px solid #dee2e6;
        }

        .statistics-section {
            margin-top: 40px;
            padding-top: 30px;
            border-top: 2px solid #e9ecef;
        }

        .statistics-title {
            font-size: 20px;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 20px;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }

        .stat-card {
            background: white;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 20px;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .stat-icon {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 15px;
            font-size: 20px;
            color: white;
        }

        .stat-present .stat-icon {
            background-color: #28a745;
        }

        .stat-absent .stat-icon {
            background-color: #dc3545;
        }

        .stat-late .stat-icon {
            background-color: #ffc107;
            color: #212529;
        }

        .stat-number {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .stat-present .stat-number {
            color: #28a745;
        }

        .stat-absent .stat-number {
            color: #dc3545;
        }

        .stat-late .stat-number {
            color: #ffc107;
        }

        .stat-label {
            font-size: 14px;
            color: #7f8c8d;
            margin-bottom: 10px;
        }

        .stat-percent {
            font-size: 18px;
            font-weight: bold;
        }

        .stat-present .stat-percent {
            color: #28a745;
        }

        .stat-absent .stat-percent {
            color: #dc3545;
        }

        .stat-late .stat-percent {
            color: #ffc107;
        }

        @media (max-width: 768px) {
            .main-content {
                margin-left: 0;
                padding: 10px;
            }

            .header-section {
                flex-direction: column;
                align-items: flex-start;
                gap: 15px;
            }

            .filter-section {
                flex-direction: column;
                align-items: flex-start;
                width: 100%;
            }

            .attendance-grid {
                grid-template-columns: 1fr;
            }

            .stats-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<jsp:include page="student-sidebar.jsp" />

<div class="main-content">
    <div class="container">
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

        <form method="GET" action="${pageContext.request.contextPath}/StudentAttendanceReport" style="display: flex; gap: 10px;">
            <label for="class">Lọc theo lớp:</label>
            <select name="class" id="class" onchange="this.form.submit()">
                <option value="">Tất cả lớp</option>
                <%
                    List<String> classNames = (List<String>) request.getAttribute("classNames");
                    String selectedClass = (String) request.getAttribute("selectedClass");
                    for (String className : classNames) {
                %>
                <option value="<%=className%>" <%= className.equals(selectedClass) ? "selected" : "" %>><%=className%></option>
                <% } %>
            </select>

            <label for="year">Năm:</label>
            <select name="year">
                <%
                    int currentYear = LocalDate.now().getYear();
                    for (int y = currentYear - 2; y <= currentYear + 1; y++) {
                %>
                <option value="<%=y%>" <%= y == selectedYear ? "selected" : "" %>><%=y%></option>
                <% } %>
            </select>

            <label for="week">Tuần:</label>
            <select name="week">
                <%
                    WeekFields weekFields = WeekFields.of(Locale.getDefault());
                    LocalDate firstDayOfYear = LocalDate.of(selectedYear, 1, 1);

                    for (int i = 1; i <= totalWeeksInYear; i++) {
                        try {
                            LocalDate weekStart = firstDayOfYear
                                    .with(weekFields.weekOfYear(), i)
                                    .with(DayOfWeek.MONDAY);
                            LocalDate weekEnd = weekStart.plusDays(6);

                            if (weekStart.getYear() == selectedYear || weekEnd.getYear() == selectedYear) {
                                String weekLabel = "Tuần " + i + ": " + weekStart.format(DateTimeFormatter.ofPattern("dd/MM")) +
                                        " đến " + weekEnd.format(DateTimeFormatter.ofPattern("dd/MM"));
                %>
                <option value="<%=i%>" <%= i == selectedWeekNumber ? "selected" : "" %>><%=weekLabel%></option>
                <%
                            }
                        } catch (Exception e) {
                        }
                    }
                %>

            </select>

            <button type="submit">Lọc</button>
            <a href="${pageContext.request.contextPath}/StudentAttendanceReport"
               style="padding: 6px 12px; background: #ccc; border: none; text-decoration: none; color: #000; border-radius: 4px;">
                Bỏ lọc
            </a>
        </form>
<br>
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
    // Toggle view options
    document.querySelectorAll('.view-option').forEach(option => {
        option.addEventListener('click', function() {
            document.querySelectorAll('.view-option').forEach(opt => opt.classList.remove('active'));
            this.classList.add('active');

            // Toggle between grid and list view
            const grid = document.querySelector('.attendance-grid');
            if (this.querySelector('i').classList.contains('fa-list')) {
                grid.style.gridTemplateColumns = '1fr';
            } else {
                grid.style.gridTemplateColumns = 'repeat(auto-fit, minmax(280px, 1fr))';
            }
        });
    });
</script>

</body>
</html>