<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thời Khóa Biểu Tuần - TALENT01</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/schedule.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/teacher-sidebar.css">
</head>
<body>
<!-- Header -->
<div class="header">
    <div class="logo">TALENT01</div>
    <div class="nav">
        <a href="${pageContext.request.contextPath}/View/home.jsp"">Trang Chủ</a>
        <a href="#">Dịch Vụ</a>
        <a href="#">Khóa Học</a>
        <a href="#">Sự Kiện</a>
        <a href="${pageContext.request.contextPath}/View/profile.jsp">Trang cá nhân</a>
        <a href="/logout">Đăng Xuất</a>
    </div>
</div>

<!-- Sidebar -->
<%@ include file="teacher-sidebar.jsp" %>

<!-- Main Content -->
<div class="main-content">
    <div class="container">
        <h1>Thời Khóa Biểu</h1>

        <!-- Controls -->
        <div class="controls">
            <div class="control-group">
                <label for="year">Năm:</label>
                <select name="year" id="year">
                    <option value="2024">2024</option>
                    <option value="2025" selected>2025</option>
                    <option value="2026">2026</option>
                </select>
            </div>

            <div class="control-group">
                <label for="week">Tuần:</label>
                <select name="week" id="week">
                    <option value="1">Week 1 (Jan 1-7)</option>
                    <option value="2">Week 2 (Jan 8-14)</option>
                    <option value="3" selected>Week 3 (Jan 15-21)</option>
                    <option value="4">Week 4 (Jan 22-28)</option>
                </select>
            </div>
        </div>

        <!-- Schedule Table -->
        <div class="schedule-table">
            <table>
                <thead>
                <tr>
                    <th class="slot-header">SLOT</th>
                    <th class="day-header">Thứ 2<br><span class="date">15/01</span></th>
                    <th class="day-header">Thứ 3<br><span class="date">16/01</span></th>
                    <th class="day-header">Thứ 4<br><span class="date">17/01</span></th>
                    <th class="day-header">Thứ 5<br><span class="date">18/01</span></th>
                    <th class="day-header">Thứ 6<br><span class="date">19/01</span></th>
                    <th class="day-header">Thứ 7<br><span class="date">20/01</span></th>
                    <th class="day-header">Chủ nhật<br><span class="date">21/01</span></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="slot-cell">Slot 1</td>
                    <td class="schedule-cell">
                        <div class="class-item">
                            <div class="class-code">SWP391</div>
                            <div class="class-info">
                                <a href="#" class="btn view-materials">View Materials</a><br>
                                at Room 101 -
                                <a href="#" class="btn meet-url">Meet URL</a>
                                <a href="#" class="btn edu-next">EduNext</a><br>
                                <span class="attendance-status">(attended)</span><br>
                                <span class="time-slot">(07:30-09:00)</span>
                            </div>
                        </div>
                    </td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">
                        <div class="class-item">
                            <div class="class-code">PRN231</div>
                            <div class="class-info">
                                <a href="#" class="btn view-materials">View Materials</a><br>
                                at Room 203 -
                                <a href="#" class="btn meet-url">Meet URL</a>
                                <a href="#" class="btn edu-next">EduNext</a><br>
                                <span class="attendance-status">(attended)</span><br>
                                <span class="time-slot">(07:30-09:00)</span>
                            </div>
                        </div>
                    </td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">
                        <div class="class-item">
                            <div class="class-code">SWE201c</div>
                            <div class="class-info">
                                <a href="#" class="btn view-materials">View Materials</a><br>
                                at Room 105 -
                                <a href="#" class="btn meet-url">Meet URL</a>
                                <a href="#" class="btn edu-next">EduNext</a><br>
                                <span class="attendance-status">(attended)</span><br>
                                <span class="time-slot">(07:30-09:00)</span>
                            </div>
                        </div>
                    </td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                </tr>
                <tr>
                    <td class="slot-cell">Slot 2</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">
                        <div class="class-item">
                            <div class="class-code">SWP391</div>
                            <div class="class-info">
                                <a href="#" class="btn view-materials">View Materials</a><br>
                                at Room 101 -
                                <a href="#" class="btn meet-url">Meet URL</a>
                                <a href="#" class="btn edu-next">EduNext</a><br>
                                <span class="attendance-status">(attended)</span><br>
                                <span class="time-slot">(09:10-10:40)</span>
                            </div>
                        </div>
                    </td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                </tr>
                <tr>
                    <td class="slot-cell">Slot 3</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                </tr>
                <tr>
                    <td class="slot-cell">Slot 4</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                </tr>
                <tr>
                    <td class="slot-cell">Slot 5</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                </tr>
                <tr>
                    <td class="slot-cell">Slot 6</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                    <td class="schedule-cell">-</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Footer -->
<div class="footer">
    <p>Copyright © 2025 Talent Center Management. SWP391-Group 01.</p>
</div>

<script>
    // Form submission handling
    document.getElementById('year').addEventListener('change', function() {
        // Simulate form submission
        console.log('Year changed to:', this.value);
    });

    document.getElementById('week').addEventListener('change', function() {
        // Simulate form submission
        console.log('Week changed to:', this.value);
    });

    // Button interactions
    document.querySelectorAll('.view-materials').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            alert('Xem tài liệu học tập');
        });
    });

    document.querySelectorAll('.meet-url').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            alert('Tham gia lớp học online');
        });
    });

    document.querySelectorAll('.edu-next').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            alert('Mở EduNext');
        });
    });
</script>
</body>
</html>