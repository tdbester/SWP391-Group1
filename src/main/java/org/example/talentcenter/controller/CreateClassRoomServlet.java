package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/CreateClassRoomServlet")
public class CreateClassRoomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Load necessary data for form (slots, teachers, rooms)
        loadFormData(request);

        // Debug: In ra số lượng dữ liệu đã load
        System.out.println("Courses count: " +
                (request.getAttribute("courses") != null ?
                        ((List<?>)request.getAttribute("courses")).size() : "null"));
        System.out.println("Teachers count: " +
                (request.getAttribute("teachers") != null ?
                        ((List<?>)request.getAttribute("teachers")).size() : "null"));
        System.out.println("Slots count: " +
                (request.getAttribute("slots") != null ?
                        ((List<?>)request.getAttribute("slots")).size() : "null"));
        System.out.println("Rooms count: " +
                (request.getAttribute("rooms") != null ?
                        ((List<?>)request.getAttribute("rooms")).size() : "null"));

        request.getRequestDispatcher("/View/training-manager-add-classroom.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Validate and parse input
            String className = request.getParameter("className");
            if (className == null || className.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên lớp không được để trống");
            }

            int courseId = parseIntParameter(request, "courseId", "Khóa học không hợp lệ");
            int teacherId = parseIntParameter(request, "teacherId", "Giáo viên không hợp lệ");
            int slotId = parseIntParameter(request, "slotId", "Slot học không hợp lệ");
            int roomId = parseIntParameter(request, "roomId", "Phòng học không hợp lệ");

            LocalDate startDate = parseLocalDateParameter(request, "startDate", "Ngày bắt đầu không hợp lệ");
            LocalDate endDate = parseLocalDateParameter(request, "endDate", "Ngày kết thúc không hợp lệ");

            // Validate date logic
            if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
                throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
            }

            // Validate days of week
            String[] days = request.getParameterValues("daysOfWeek");
            if (days == null || days.length == 0) {
                throw new IllegalArgumentException("Vui lòng chọn ít nhất một ngày trong tuần");
            }

            List<Integer> daysOfWeek = Arrays.stream(days)
                    .map(day -> {
                        try {
                            int dayInt = Integer.parseInt(day);
                            if (dayInt < 1 || dayInt > 7) {
                                throw new IllegalArgumentException("Ngày trong tuần không hợp lệ");
                            }
                            return dayInt;
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Ngày trong tuần không hợp lệ");
                        }
                    })
                    .collect(Collectors.toList());

            // Create DAO instances
            ClassRoomsDAO classRoomsDAO = new ClassRoomsDAO();
            ClassSchedulePatternDAO classSchedulePatternDAO = new ClassSchedulePatternDAO();
            TeacherScheduleDAO teacherScheduleDAO = new TeacherScheduleDAO();

            // Create classroom
            ClassRooms classRoom = new ClassRooms();
            classRoom.setName(className.trim());
            classRoom.setCourseId(courseId);
            classRoom.setTeacherId(teacherId);
            classRoom.setSlotId(slotId);

            int classRoomId = classRoomsDAO.insertClassRoomAndReturnId(classRoom);
            if (classRoomId == -1) {
                throw new RuntimeException("Không thể tạo lớp học");
            }

            // Create schedule patterns for each day of week
            for (Integer dayOfWeek : daysOfWeek) {
                ClassSchedulePattern pattern = new ClassSchedulePattern();
                pattern.setClassRoomId(classRoomId);
                pattern.setStartDate(startDate);
                pattern.setEndDate(endDate);
                pattern.setSlotId(slotId);
                pattern.setDayOfWeek(dayOfWeek);
                classSchedulePatternDAO.insertPattern(pattern);
            }

            // Get all patterns for the classroom
            List<ClassSchedulePattern> patterns = classSchedulePatternDAO.getPatternsByClassRoom(classRoomId);

            // Generate specific schedules
            teacherScheduleDAO.generateSchedulesFromPattern(patterns, classRoomId, roomId);

            // Success response
            request.setAttribute("success", "Tạo lớp học và thành công!");
            request.getRequestDispatcher("/View/training-manager-add-classroom.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            // Validation errors
            request.setAttribute("error", e.getMessage());
            loadFormData(request);
            request.getRequestDispatcher("/View/training-manager-add-classroom.jsp").forward(request, response);

        } catch (Exception e) {
            // System errors
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại!");
            loadFormData(request);
            request.getRequestDispatcher("/View/training-manager-add-classroom.jsp").forward(request, response);
        }
    }

    private int parseIntParameter(HttpServletRequest request, String paramName, String errorMessage) {
        String paramValue = request.getParameter(paramName);
        if (paramValue == null || paramValue.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        try {
            return Integer.parseInt(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private LocalDate parseLocalDateParameter(HttpServletRequest request, String paramName, String errorMessage) {
        String paramValue = request.getParameter(paramName);
        if (paramValue == null || paramValue.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        try {
            return LocalDate.parse(paramValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void loadFormData(HttpServletRequest request) {
        try {
            // Load slots
            SlotDAO slotDAO = new SlotDAO();
            List<Slot> slots = slotDAO.getAll();
            System.out.println("Loaded " + slots.size() + " slots");
            request.setAttribute("slots", slots);

            // Load teachers
            TeacherDAO teacherDAO = new TeacherDAO();
            List<Teacher> teachers = teacherDAO.getAll();
            System.out.println("Loaded " + teachers.size() + " teachers");
            request.setAttribute("teachers", teachers);

            // Load rooms
            RoomDAO roomDAO = new RoomDAO();
            ArrayList<Room> rooms = roomDAO.getAllRooms();
            System.out.println("Loaded " + rooms.size() + " rooms");
            request.setAttribute("rooms", rooms);

            // Load courses
            CourseDAO courseDAO = new CourseDAO();
            List<Course> courses = courseDAO.getAllCourses();
            System.out.println("Loaded " + courses.size() + " courses");
            request.setAttribute("courses", courses);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải dữ liệu từ database: " + e.getMessage());
        }
    }
}