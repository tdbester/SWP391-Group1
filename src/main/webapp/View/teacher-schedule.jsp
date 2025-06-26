<%@ page import="java.util.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.talentcenter.model.Schedule" %>
<%
    List<Schedule> schedules = (List<Schedule>) request.getAttribute("schedulesTeacher");
    if (schedules == null) {
        schedules = new ArrayList<>();
    }

    // Các ngày trong tuần (theo DayOfWeek)
    String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    int totalSlots = 10;

    // Map ngày -> slotId -> danh sách lớp
    Map<String, Map<Integer, List<Schedule>>> weekSchedule = new HashMap<>();
    for (String day : days) {
        weekSchedule.put(day, new HashMap<>());
        for (int slot = 1; slot <= totalSlots; slot++) {
            weekSchedule.get(day).put(slot, new ArrayList<>());
        }
    }

    // Đưa lịch vào map theo ngày và slotId
    for (Schedule schedule : schedules) {
        LocalDate date = schedule.getDate();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String dayKey = dayOfWeek.toString();
        int slotId = schedule.getSlotId();

        if (weekSchedule.containsKey(dayKey) && weekSchedule.get(dayKey).containsKey(slotId)) {
            weekSchedule.get(dayKey).get(slotId).add(schedule);
        }
    }

    // Tính ngày bắt đầu tuần
    LocalDate today = LocalDate.now();
    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

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
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thời Khóa Biểu Tuần - TALENT01</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/schedule.css">

</head>
<body>
<jsp:include page="header.jsp" />
<jsp:include page="teacher-sidebar.jsp" />

<!-- Main Content -->
<div class="main-content">
    <div class="container">
        <h1>Thời Khóa Biểu</h1>

        <!-- Filter Container -->
        <div class="filter-container">
            <form class="filter-form" method="GET" action="${pageContext.request.contextPath}/TeacherSchedule">
                <div class="filter-group">
                    <label for="week">Chọn tuần:</label>
                    <input type="date" name="week" id="week"
                           value="<%= request.getAttribute("selectedWeek") != null ? request.getAttribute("selectedWeek").toString() : request.getAttribute("defaultWeek") %>">
                </div>

                <div class="filter-buttons">
                    <button type="submit" class="btn-filter">
                        <i class="fas fa-search"></i> Lọc
                    </button>
                    <button type="button" class="btn-reset" onclick="resetFilter()">
                        <i class="fas fa-undo"></i> Đặt lại
                    </button>
                </div>
            </form>
        </div>

        <!-- Current Filter Display -->
        <div class="current-filter">
            <strong>Đang hiển thị: </strong>
            Tuần từ <%= startOfWeek.format(dateTimeFormatter) %> đến <%= startOfWeek.plusDays(6).format(dateTimeFormatter) %>

            <span style="margin-left: 20px;">
                <strong>Tổng số lịch: </strong><%= schedules.size() %> buổi học
            </span>
        </div>

        <!-- View Toggle Buttons -->
        <div style="margin-bottom: 20px;">
            <button type="button" class="btn-filter" id="weekViewBtn" onclick="showWeekView()">
                <i class="fas fa-calendar-week"></i> Xem dạng tuần
            </button>
            <button type="button" class="btn-filter" onclick="showListView()">
                <i class="fas fa-list"></i> Xem dạng danh sách
            </button>
        </div>

        <!-- Week View (Table Format) -->
        <div class="schedule-table" id="weekView">
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
                            Optional<Schedule> sample = schedules.stream()
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
                            List<Schedule> daySlotSchedules = weekSchedule.get(dayKey).get(slotId);
                    %>
                    <td class="schedule-cell">
                        <%
                            if (daySlotSchedules.isEmpty()) {
                        %>
                        -
                        <%
                        } else {
                            for (Schedule schedule : daySlotSchedules) {
                        %>
                        <div class="class-item">
                            <div class="class-code"><%=schedule.getCourseTitle()%></div>
                            <div class="class-info">
                                Lớp: <%=schedule.getClassName()%><br>
                                Phòng: <%=schedule.getRoomCode()%><br>
                                <span class="time-slot">Slot <%=schedule.getSlotId()%></span>
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

        <!-- List View -->
        <div class="list-view" id="listView" style="display:none;">
            <ul class="schedule-list">
                <%
                    Map<LocalDate, List<Schedule>> schedulesByDate = new TreeMap<>();
                    for (Schedule schedule : schedules) {
                        schedulesByDate.computeIfAbsent(schedule.getDate(), k -> new ArrayList<>()).add(schedule);
                    }

                    for (Map.Entry<LocalDate, List<Schedule>> entry : schedulesByDate.entrySet()) {
                        LocalDate date = entry.getKey();
                        List<Schedule> daySchedules = entry.getValue();

                        // Sắp xếp lịch trong ngày theo slot
                        daySchedules.sort((s1, s2) -> Integer.compare(s1.getSlotId(), s2.getSlotId()));
                %>
                <li class="schedule-item">
                    <div class="schedule-date">
                        <i class="fas fa-calendar-day"></i>
                        <%= date.format(dateTimeFormatter) %>
                        (<%= date.getDayOfWeek().toString().equals("MONDAY") ? "Thứ 2" :
                            date.getDayOfWeek().toString().equals("TUESDAY") ? "Thứ 3" :
                                    date.getDayOfWeek().toString().equals("WEDNESDAY") ? "Thứ 4" :
                                            date.getDayOfWeek().toString().equals("THURSDAY") ? "Thứ 5" :
                                                    date.getDayOfWeek().toString().equals("FRIDAY") ? "Thứ 6" :
                                                            date.getDayOfWeek().toString().equals("SATURDAY") ? "Thứ 7" : "Chủ nhật" %>)
                    </div>
                    <div class="schedule-details">
                        <% for (Schedule schedule : daySchedules) {
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        %>
                        <div class="class-item">
                            <div class="class-code">
                                <i class="fas fa-book"></i>
                                <%= schedule.getCourseTitle() %>
                            </div>
                            <div class="class-info">
                                <div><i class="fas fa-users"></i> Lớp: <%= schedule.getClassName() %></div>
                                <div><i class="fas fa-door-open"></i> Phòng: <%= schedule.getRoomCode() %></div>
                                <div><i class="fas fa-clock"></i>
                                    Slot <%= schedule.getSlotId() %>
                                    (<%= schedule.getSlotStartTime().format(timeFormatter) %>-<%= schedule.getSlotEndTime().format(timeFormatter) %>)
                                </div>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </li>
                <% } %>

                <% if (schedules.isEmpty()) { %>
                <li class="schedule-item" style="text-align: center; color: #666;">
                    <i class="fas fa-calendar-times" style="font-size: 48px; margin-bottom: 10px; display: block;"></i>
                    <p>Không có lịch học trong khoảng thời gian đã chọn</p>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</div>

<script>
    function resetFilter() {
        // Set về tuần hiện tại
        const today = new Date();
        const monday = new Date(today);
        monday.setDate(today.getDate() - today.getDay() + 1);

        const year = monday.getFullYear();
        const month = String(monday.getMonth() + 1).padStart(2, '0');
        const day = String(monday.getDate()).padStart(2, '0');

        document.getElementById('week').value = `${year}-${month}-${day}`;

        // Submit form
        document.querySelector('.filter-form').submit();
    }

    function showWeekView() {
        document.getElementById('weekView').style.display = 'block';
        document.getElementById('listView').style.display = 'none';
    }

    function showListView() {
        document.getElementById('weekView').style.display = 'none';
        document.getElementById('listView').style.display = 'block';
    }

    // Khởi tạo khi trang load
    document.addEventListener('DOMContentLoaded', function() {
        // Set default week if not set
        const weekInput = document.getElementById('week');
        if (weekInput && !weekInput.value) {
            const today = new Date();
            const monday = new Date(today);
            monday.setDate(today.getDate() - today.getDay() + 1);

            const year = monday.getFullYear();
            const month = String(monday.getMonth() + 1).padStart(2, '0');
            const day = String(monday.getDate()).padStart(2, '0');

            weekInput.value = `${year}-${month}-${day}`;
        }
    });
</script>

<jsp:include page="footer.jsp" />
</body>
</html>