<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.talentcenter.model.Schedule" %>
<%
    // Lấy danh sách schedule từ request
    @SuppressWarnings("unchecked")
    List<Schedule> schedules = (List<Schedule>) request.getAttribute("schedules");
    if (schedules == null) {
        schedules = new ArrayList<>();
    }

    // Tạo map để nhóm các schedule theo ngày trong tuần và slot
    Map<String, Map<String, List<Schedule>>> weekSchedule = new HashMap<>();

    // Khởi tạo structure cho 7 ngày trong tuần
    String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    String[] slots = {"Sáng", "Chiều", "Tối"};

    for (String day : days) {
        weekSchedule.put(day, new HashMap<>());
        for (String slot : slots) {
            weekSchedule.get(day).put(slot, new ArrayList<>());
        }
    }

    // Phân loại schedule theo ngày và slot
    for (Schedule schedule : schedules) {
        LocalDate date = schedule.getDate();
        LocalTime startTime = schedule.getStartTime();

        // Xác định ngày trong tuần
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String dayKey = dayOfWeek.toString();

        // Xác định slot dựa trên thời gian
        String slot;
        int hour = startTime.getHour();
        if (hour >= 6 && hour < 12) {
            slot = "Sáng";
        } else if (hour >= 12 && hour < 18) {
            slot = "Chiều";
        } else {
            slot = "Tối";
        }

        // Thêm vào map
        if (weekSchedule.containsKey(dayKey)) {
            weekSchedule.get(dayKey).get(slot).add(schedule);
        }
    }

    // Sắp xếp các lớp trong cùng slot theo thời gian bắt đầu
    for (String day : days) {
        for (String slot : slots) {
            List<Schedule> slotSchedules = weekSchedule.get(day).get(slot);
            slotSchedules.sort((s1, s2) -> s1.getStartTime().compareTo(s2.getStartTime()));
        }
    }

    // Tính toán ngày trong tuần hiện tại
    LocalDate today = LocalDate.now();
    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
%>
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
        <a href="${pageContext.request.contextPath}/View/home.jsp">Trang Chủ</a>
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
        <!-- Schedule Table -->
        <div class="schedule-table">
            <table>
                <thead>
                <tr>
                    <th class="slot-header">SLOT</th>
                    <th class="day-header">Thứ 2<br><span class="date"><%=startOfWeek.format(dateFormatter)%></span></th>
                    <th class="day-header">Thứ 3<br><span class="date"><%=startOfWeek.plusDays(1).format(dateFormatter)%></span></th>
                    <th class="day-header">Thứ 4<br><span class="date"><%=startOfWeek.plusDays(2).format(dateFormatter)%></span></th>
                    <th class="day-header">Thứ 5<br><span class="date"><%=startOfWeek.plusDays(3).format(dateFormatter)%></span></th>
                    <th class="day-header">Thứ 6<br><span class="date"><%=startOfWeek.plusDays(4).format(dateFormatter)%></span></th>
                    <th class="day-header">Thứ 7<br><span class="date"><%=startOfWeek.plusDays(5).format(dateFormatter)%></span></th>
                    <th class="day-header">Chủ nhật<br><span class="date"><%=startOfWeek.plusDays(6).format(dateFormatter)%></span></th>
                </tr>
                </thead>
                <tbody>
                <%
                    String[] slotNames = {"Sáng", "Chiều", "Tối"};
                    String[] dayKeys = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

                    for (String slotName : slotNames) {
                %>
                <tr>
                    <td class="slot-cell"><%=slotName%></td>
                    <%
                        for (String dayKey : dayKeys) {
                            List<Schedule> daySlotSchedules = weekSchedule.get(dayKey).get(slotName);
                    %>
                    <td class="schedule-cell">
                        <%
                            if (daySlotSchedules.isEmpty()) {
                        %>
                        -
                        <%
                        } else {
                            for (Schedule schedule : daySlotSchedules) {
                                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                                String startTimeStr = schedule.getStartTime().format(timeFormatter);
                                String endTimeStr = schedule.getEndTime().format(timeFormatter);
                        %>
                        <div class="class-item">
                            <div class="class-code"><%=schedule.getCourseTitle()%></div>
                            <div class="class-info">
                                Lớp: <%=schedule.getClassName()%><br>
                                Phòng: <%=schedule.getRoomCode()%><br>
                                <span class="time-slot">(<%=startTimeStr%>-<%=endTimeStr%>)</span>
                            </div>
                        </div>
                        <%
                                }
                            }
                        %>
                    </td>
                    <%
                        }
                    %>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Footer -->
<div class="footer">
    <p>Copyright © 2025 Talent Center Management. SWP391-Group 01.</p>
</div>

</body>
</html>