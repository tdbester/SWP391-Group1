<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo lớp học - TALENT01</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/training-manager-add-classroom.css">
</head>
<body>
<!-- Include Header -->
<%@ include file="../View/header.jsp" %>

<!-- Main Dashboard Container -->
<div class="dashboard">
    <!-- Include Training Manager Sidebar -->
    <%@ include file="../View/training-manager-sidebar.jsp" %>

    <!-- Main Content Area -->
    <div class="main-content">
        <div class="content-wrapper">
            <!-- Form Container -->
            <div class="form-container">
                <div class="form-header">
                    <h1><i class="fas fa-plus-circle"></i> Tạo Lớp Học Mới</h1>
                    <p>Điền thông tin để tạo lớp học</p>
                </div>

                <!-- Display error/success messages -->
                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>

                <c:if test="${not empty success}">
                    <div class="alert alert-success">
                        <i class="fas fa-check-circle"></i> ${success}
                    </div>
                </c:if>

                <form action="CreateClassRoomServlet" method="post" class="classroom-form">
                    <!-- Form Row 1: Tên lớp học và Khóa học -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="className">Tên lớp học:</label>
                            <input type="text" id="className" name="className" required>
                        </div>

                        <div class="form-group">
                            <label for="courseId">Khóa học:</label>
                            <select id="courseId" name="courseId" required>
                                <option value="">-- Chọn khóa học --</option>
                                <c:forEach var="course" items="${courses}">
                                    <option value="${course.id}">${course.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <!-- Form Row 2: Giáo viên và Slot học -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="teacherId">Giáo viên:</label>
                            <select id="teacherId" name="teacherId" required>
                                <option value="">-- Chọn giáo viên --</option>
                                <c:forEach var="teacher" items="${teachers}">
                                    <option value="${teacher.id}">${teacher.fullName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="slotId">Slot học:</label>
                            <select id="slotId" name="slotId" required>
                                <option value="">-- Chọn slot --</option>
                                <c:forEach var="slot" items="${slots}">
                                    <option value="${slot.id}">
                                            ${slot.timeRange}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <!-- Form Row 3: Phòng học -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="roomId">Phòng học:</label>
                            <select id="roomId" name="roomId" required>
                                <option value="">-- Chọn phòng --</option>
                                <c:forEach var="room" items="${rooms}">
                                    <option value="${room.id}">${room.code}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <!-- Form Row 4: Ngày bắt đầu và kết thúc -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="startDate">Ngày bắt đầu:</label>
                            <input type="date" id="startDate" name="startDate" required>
                        </div>

                        <div class="form-group">
                            <label for="endDate">Ngày kết thúc:</label>
                            <input type="date" id="endDate" name="endDate" required>
                        </div>
                    </div>

                    <!-- Ngày trong tuần -->
                    <div class="checkbox-group">
                        <label>Ngày học trong tuần:</label>
                        <div class="days-container">
                            <div class="checkbox-item">
                                <input type="checkbox" name="daysOfWeek" value="2" id="monday">
                                <label for="monday">Thứ 2</label>
                            </div>

                            <div class="checkbox-item">
                                <input type="checkbox" name="daysOfWeek" value="3" id="tuesday">
                                <label for="tuesday">Thứ 3</label>
                            </div>

                            <div class="checkbox-item">
                                <input type="checkbox" name="daysOfWeek" value="4" id="wednesday">
                                <label for="wednesday">Thứ 4</label>
                            </div>

                            <div class="checkbox-item">
                                <input type="checkbox" name="daysOfWeek" value="5" id="thursday">
                                <label for="thursday">Thứ 5</label>
                            </div>

                            <div class="checkbox-item">
                                <input type="checkbox" name="daysOfWeek" value="6" id="friday">
                                <label for="friday">Thứ 6</label>
                            </div>

                            <div class="checkbox-item">
                                <input type="checkbox" name="daysOfWeek" value="7" id="saturday">
                                <label for="saturday">Thứ 7</label>
                            </div>

                            <div class="checkbox-item">
                                <input type="checkbox" name="daysOfWeek" value="1" id="sunday">
                                <label for="sunday">Chủ nhật</label>
                            </div>
                        </div>
                    </div>

                    <!-- Form Actions -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Tạo lớp học
                        </button>
                        <a href="${pageContext.request.contextPath}/CreateClassRoomServlet?action=list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Quay lại
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Include Footer -->
<%@ include file="../View/footer.jsp" %>

<script>
    // Validate form before submit
    document.querySelector('form').addEventListener('submit', function(e) {
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;
        const daysChecked = document.querySelectorAll('input[name="daysOfWeek"]:checked').length;

        if (startDate && endDate && new Date(startDate) >= new Date(endDate)) {
            alert('Ngày bắt đầu phải trước ngày kết thúc!');
            e.preventDefault();
            return;
        }

        if (daysChecked === 0) {
            alert('Vui lòng chọn ít nhất một ngày trong tuần!');
            e.preventDefault();
            return;
        }
    });

    // Add animation to form groups
    document.addEventListener('DOMContentLoaded', function() {
        const formGroups = document.querySelectorAll('.form-group');
        formGroups.forEach((group, index) => {
            group.style.animationDelay = `${index * 0.1}s`;
            group.classList.add('fade-in');
        });
    });
</script>
</body>
</html>