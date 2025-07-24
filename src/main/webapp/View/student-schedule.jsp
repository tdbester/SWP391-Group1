<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/15/2025
  Time: 10:59 PM
  To change this template use File | Settings | File Templates.
--%>

<%--/*--%>
<%--*  Copyright (C) 2025 <Group 1>--%>
<%--*  All rights reserved.--%>
<%--*--%>
<%--*  This file is part of the <Talent Center Management> project.--%>
<%--    *  Unauthorized copying of this file, via any medium is strictly prohibited.--%>
<%--    *  Proprietary and confidential.--%>
<%--    *--%>
<%--    *  Created on:        2025-06-15--%>
<%--    *  Author:            Nguyễn Minh Cao--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-06-15  | Nguyễn Minh Cao      | Schedule for teacher--%>
<%--    *  2025-06-18  | Cù Thị Huyền Trang   | Schedule for student--%>
<%--    */--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.talentcenter.model.StudentSchedule" %>
<%@ page import="java.util.Optional" %>
<%@ page import="java.time.temporal.WeekFields" %>
<%
    @SuppressWarnings("unchecked")
    List<StudentSchedule> schedules = (List<StudentSchedule>) request.getAttribute("schedules");
    if (schedules == null) {
        schedules = new ArrayList<>();
    }

    Integer selectedYearObj = (Integer) request.getAttribute("selectedYear");
    Integer selectedWeekNumberObj = (Integer) request.getAttribute("selectedWeekNumber");
    Integer totalWeeksInYear = (Integer) request.getAttribute("totalWeeksInYear");

    int selectedYear = selectedYearObj != null ? selectedYearObj : LocalDate.now().getYear();
    int selectedWeekNumber = selectedWeekNumberObj != null ? selectedWeekNumberObj : 1;
    if (totalWeeksInYear == null) {
        totalWeeksInYear = 52; // Mặc định 52 tuần
    }

    // Các ngày trong tuần (theo DayOfWeek)
    String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    int totalSlots = 10;

    // Map ngày -> slotId -> danh sách lớp
    Map<String, Map<Integer, List<StudentSchedule>>> weekSchedule = new HashMap<>();
    for (String day : days) {
        weekSchedule.put(day, new HashMap<>());
        for (int slot = 1; slot <= totalSlots; slot++) {
            weekSchedule.get(day).put(slot, new ArrayList<>());
        }
    }

    // Đưa lịch vào map theo ngày và slotId
    for (StudentSchedule schedule : schedules) {
        LocalDate date = schedule.getDate();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String dayKey = dayOfWeek.toString();
        int slotId = schedule.getSlotId();

        if (weekSchedule.containsKey(dayKey) && weekSchedule.get(dayKey).containsKey(slotId)) {
            weekSchedule.get(dayKey).get(slotId).add(schedule);
        }
    }

    // Tính ngày bắt đầu tuần
    LocalDate startOfWeek = (LocalDate) request.getAttribute("selectedWeek");
    if (startOfWeek == null) {
        startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
    }

    // Kiểm tra xem có tuần được chọn không
    LocalDate selectedWeek = (LocalDate) request.getAttribute("selectedWeek");
    if (selectedWeek != null) {
        startOfWeek = selectedWeek;
    }

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<html lang="vi">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thời Khóa Biểu Tuần - TALENT01</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/schedule.css">

</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <jsp:include page="student-sidebar.jsp"/>
    <div class="main-content">
        <a href="StudentDashboard" class="back-link">
            <i class="fas fa-arrow-left"></i>
            Quay lại Dashboard
        </a>
        <div class="filter-container">
            <form method="GET" action="${pageContext.request.contextPath}/StudentSchedule"
                  class="filter-form">
                <div class="filter-group">
                    <label for="year">YEAR:</label>
                    <select name="year" id="year" class="dropdown-filter">
                        <%
                            int currentYear = LocalDate.now().getYear();
                            for (int year = currentYear - 2; year <= currentYear + 2; year++) {
                        %>
                        <option value="<%=year%>" <%= (year == selectedYear) ? "selected" : "" %>><%=year%>
                        </option>
                        <%
                            }
                        %>
                    </select>
                </div>

                <div class="filter-group">
                    <label for="week">WEEK:</label>
                    <select name="week" id="week" class="dropdown-filter">
                        <%
                            WeekFields weekFields = WeekFields.of(Locale.getDefault());
                            LocalDate firstDayOfYear = LocalDate.of(selectedYear, 1, 1);

                            for (int week = 1; week <= totalWeeksInYear; week++) {
                                try {
                                    LocalDate weekStart = firstDayOfYear
                                            .with(weekFields.weekOfYear(), week)
                                            .with(DayOfWeek.MONDAY);
                                    LocalDate weekEnd = weekStart.plusDays(6);

                                    if (weekStart.getYear() == selectedYear || weekEnd.getYear() == selectedYear) {
                                        String weekRange = weekStart.format(DateTimeFormatter.ofPattern("dd/MM")) +
                                                " To " + weekEnd.format(DateTimeFormatter.ofPattern("dd/MM"));
                        %>
                        <option value="<%=week%>" <%= (week == selectedWeekNumber) ? "selected" : "" %>>
                            Tuần <%=week%>: <%=weekRange%>
                        </option>
                        <%
                                    }
                                } catch (Exception e) {
                                }
                            }
                        %>
                    </select>
                </div>

                <button type="submit" class="btn-filter">
                    <i class="fas fa-search"></i> Lọc
                </button>
            </form>
        </div>
        <h1>Thời Khóa Biểu</h1>
        <div class="schedule-table">
            <table>
                <thead>
                <tr>
                    <th class="slot-header">SLOT</th>
                    <% for (int i = 0; i < 7; i++) { %>
                    <th class="day-header">
                        Thứ <%= (i + 2 <= 7 ? i + 2 : "CN") %><br>
                        <span class="date"><%=startOfWeek.plusDays(i).format(dateFormatter)%></span>
                    </th>
                    <% } %>
                </tr>
                </thead>
                <tbody>
                <%
                    for (int slotId = 1; slotId <= totalSlots; slotId++) {
                        final int currentSlotId = slotId;
                %>
                <tr>
                    <td class="slot-cell">
                        Slot <%=slotId%><br>
                        <%
                            Optional<StudentSchedule> sample = schedules.stream()
                                    .filter(s -> s.getSlotId() == currentSlotId)
                                    .findFirst();
                            if (sample.isPresent()) {
                                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                                out.print("(" + sample.get().getSlotStartTime().format(timeFormatter)
                                        + "-" + sample.get().getSlotEndTime().format(timeFormatter) + ")");
                            }
                        %>
                    </td>

                    <%
                        for (String dayKey : days) {
                            List<StudentSchedule> daySlotSchedules = weekSchedule.get(dayKey).get(slotId);
                    %>
                    <td class="schedule-cell">
                        <%
                            if (daySlotSchedules.isEmpty()) {
                        %>
                        -
                        <%
                        } else {
                            for (StudentSchedule schedule : daySlotSchedules) {
                        %>
                        <div class="class-item">
                            <%
                                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                            %>
                            <div class="class-code"><%=schedule.getCourseTitle()%>
                            </div>
                            <div class="class-info">
    <span>Lớp: <a href="StudentClass?slotId=<%=schedule.getSlotId()%>&date=<%=schedule.getDate()%>">
        <%=schedule.getClassName()%>
    </a></span>
                                <span>Phòng: <%=schedule.getRoomCode()%></span>
                                <span>Giảng viên: <%=schedule.getTeacherName()%></span>
                                <span class="time-slot">Slot <%= schedule.getSlotId() %>
        (<%= schedule.getSlotStartTime().format(timeFormatter) %>-<%= schedule.getSlotEndTime().format(timeFormatter) %>)
    </span>
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

<jsp:include page="footer.jsp"/>


</body>
</html>
