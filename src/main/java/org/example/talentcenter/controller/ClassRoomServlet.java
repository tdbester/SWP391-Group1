package org.example.talentcenter.controller;

import org.example.talentcenter.dao.ClassRoomsDAO;
import org.example.talentcenter.dao.CourseDAO;
import org.example.talentcenter.dao.TeacherDAO;
import org.example.talentcenter.dao.SlotDAO;
import org.example.talentcenter.dao.RoomDAO;
import org.example.talentcenter.model.ClassRooms;
import org.example.talentcenter.model.Course;
import org.example.talentcenter.model.Teacher;
import org.example.talentcenter.model.Slot;
import org.example.talentcenter.model.Room;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ClassRoomServlet", urlPatterns = {
        "/training-manager-view-class",
        "/training-manager-delete-classroom",
        "/training-manager-update-classroom"
})
public class ClassRoomServlet extends HttpServlet {

    private ClassRoomsDAO classRoomsDAO;
    private CourseDAO courseDAO;
    private TeacherDAO teacherDAO;
    private SlotDAO slotDAO;
    private RoomDAO roomDAO;
    private static final int PAGE_SIZE = 10;

    @Override
    public void init() throws ServletException {
        classRoomsDAO = new ClassRoomsDAO();
        courseDAO = new CourseDAO();
        teacherDAO = new TeacherDAO();
        slotDAO = new SlotDAO();
        roomDAO = new RoomDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = getActionFromPath(request.getRequestURI());

        try {
            switch (action) {
                case "view":
                    handleViewClassrooms(request, response);
                    break;
                default:
                    handleViewClassrooms(request, response);
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, e, "Có lỗi xảy ra khi xử lý yêu cầu");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = getActionFromPath(request.getRequestURI());

        try {
            switch (action) {
                case "delete":
                    handleDeleteClassroom(request, response);
                    break;
                case "update":
                    handleUpdateClassroom(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/training-manager-view-class");
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, e, "Có lỗi xảy ra khi xử lý yêu cầu");
        }
    }

    /**
     * Xác định action từ URL path
     */
    private String getActionFromPath(String requestURI) {
        if (requestURI.contains("delete-classroom")) {
            return "delete";
        } else if (requestURI.contains("update-classroom")) {
            return "update";
        } else {
            return "view";
        }
    }

    /**
     * Xử lý hiển thị danh sách lớp học
     */
    private void handleViewClassrooms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy tham số tìm kiếm
        String courseSearch = request.getParameter("courseSearch");
        String classSearch = request.getParameter("classSearch");

        // Làm sạch tham số
        courseSearch = (courseSearch != null) ? courseSearch.trim() : "";
        classSearch = (classSearch != null) ? classSearch.trim() : "";

        // Xử lý phân trang
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // Lấy dữ liệu lớp học
        List<ClassRooms> classrooms = classRoomsDAO.getClassRoomsWithDetails(
                courseSearch.isEmpty() ? null : courseSearch,
                classSearch.isEmpty() ? null : classSearch,
                currentPage,
                PAGE_SIZE
        );

        int totalClassrooms = classRoomsDAO.getTotalClassRooms(
                courseSearch.isEmpty() ? null : courseSearch,
                classSearch.isEmpty() ? null : classSearch
        );

        int totalPages = (int) Math.ceil((double) totalClassrooms / PAGE_SIZE);

        try {
            // Lấy dữ liệu cho dropdown trong modal edit
            List<Course> courses = courseDAO.getAllCourses();
            List<Teacher> teachers = teacherDAO.getAll();
            List<Slot> slots = slotDAO.getAll();
            List<Room> rooms = roomDAO.getAllRooms();

            // Set attributes cho dropdown
            request.setAttribute("courses", courses);
            request.setAttribute("teachers", teachers);
            request.setAttribute("slots", slots);
            request.setAttribute("rooms", rooms);

        } catch (SQLException e) {
            System.err.println("Error loading dropdown data: " + e.getMessage());
            e.printStackTrace();
        }

        // Set attributes cho view
        request.setAttribute("classrooms", classrooms);
        request.setAttribute("totalClassrooms", totalClassrooms);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("courseSearch", courseSearch);
        request.setAttribute("classSearch", classSearch);

        // Forward to JSP
        request.getRequestDispatcher("/View/training-manager-view-class.jsp")
                .forward(request, response);
    }

    /**
     * Xử lý cập nhật lớp học
     */
    private void handleUpdateClassroom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String classroomIdParam = request.getParameter("classroomId");
        String name = request.getParameter("name");
        String courseIdParam = request.getParameter("courseId");
        String teacherIdParam = request.getParameter("teacherId");
        String slotIdParam = request.getParameter("slotId");
        String roomIdParam = request.getParameter("roomId");

        // Validation cơ bản
        if (classroomIdParam == null || name == null || name.trim().isEmpty() ||
                courseIdParam == null || teacherIdParam == null || slotIdParam == null ||
                roomIdParam == null ) {

            request.getSession().setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            response.sendRedirect(request.getContextPath() + "/training-manager-view-class");
            return;
        }

        try {
            int classroomId = Integer.parseInt(classroomIdParam);
            int courseId = Integer.parseInt(courseIdParam);
            int teacherId = Integer.parseInt(teacherIdParam);
            int slotId = Integer.parseInt(slotIdParam);
            int roomId = Integer.parseInt(roomIdParam);


            // Kiểm tra tên lớp học đã tồn tại (trừ lớp học hiện tại)
            if (classRoomsDAO.isClassNameExists(name.trim(), classroomId)) {
                request.getSession().setAttribute("error", "Tên lớp học đã tồn tại");
                response.sendRedirect(request.getContextPath() + "/training-manager-view-class");
                return;
            }

            // Tạo object để update
            ClassRooms classroom = new ClassRooms();
            classroom.setId(classroomId);
            classroom.setName(name.trim());
            classroom.setCourseId(courseId);
            classroom.setTeacherId(teacherId);
            classroom.setSlotId(slotId);
            classroom.setRoomId(roomId);

            // Thực hiện update
            boolean success = classRoomsDAO.updateClassRoom(classroom);

            if (success) {
                request.getSession().setAttribute("message", "Cập nhật lớp học thành công");
                System.out.println("Successfully updated classroom: " + name + " (ID: " + classroomId + ")");
            } else {
                request.getSession().setAttribute("error", "Không thể cập nhật lớp học");
                System.out.println("Failed to update classroom: " + name + " (ID: " + classroomId + ")");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ");
            System.err.println("Invalid data format: " + e.getMessage());
        }

        // Preserve search parameters
        String redirectUrl = buildRedirectUrl(request, "/training-manager-view-class");
        response.sendRedirect(redirectUrl);
    }

    /**
     * Xử lý xóa lớp học
     */
    private void handleDeleteClassroom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String classroomIdParam = request.getParameter("classroomId");

        if (classroomIdParam == null || classroomIdParam.trim().isEmpty()) {
            request.getSession().setAttribute("error", "ID lớp học không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/training-manager-view-class");
            return;
        }

        try {
            int classroomId = Integer.parseInt(classroomIdParam);

            // Lấy thông tin lớp học trước khi xóa
            ClassRooms classroom = classRoomsDAO.getClassRoomById(classroomId);
            if (classroom == null) {
                request.getSession().setAttribute("error", "Không tìm thấy lớp học để xóa");
                response.sendRedirect(request.getContextPath() + "/training-manager-view-class");
                return;
            }

            // Thực hiện xóa
            boolean success = classRoomsDAO.deleteClassRoom(classroomId);

            if (success) {
                request.getSession().setAttribute("message",
                        "Đã xóa thành công lớp học: " + classroom.getName());
                System.out.println("Successfully deleted classroom: " + classroom.getName() +
                        " (ID: " + classroomId + ")");
            } else {
                request.getSession().setAttribute("error",
                        "Không thể xóa lớp học " + classroom.getName() +
                                ". Lớp học có thể đang có học sinh hoặc có ràng buộc dữ liệu khác.");
                System.out.println("Failed to delete classroom: " + classroom.getName() +
                        " (ID: " + classroomId + ")");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID lớp học không hợp lệ");
            System.err.println("Invalid classroom ID: " + classroomIdParam);
        }

        // Redirect về trang danh sách với các tham số tìm kiếm
        String redirectUrl = buildRedirectUrl(request, "/training-manager-view-class");
        response.sendRedirect(redirectUrl);
    }

    /**
     * Xây dựng URL redirect với các tham số tìm kiếm
     */
    private String buildRedirectUrl(HttpServletRequest request, String basePath) {
        String courseSearch = request.getParameter("courseSearch");
        String classSearch = request.getParameter("classSearch");
        String page = request.getParameter("page");

        StringBuilder urlBuilder = new StringBuilder(request.getContextPath() + basePath);
        boolean hasParams = false;

        if (courseSearch != null && !courseSearch.trim().isEmpty()) {
            urlBuilder.append("?courseSearch=").append(courseSearch);
            hasParams = true;
        }

        if (classSearch != null && !classSearch.trim().isEmpty()) {
            urlBuilder.append(hasParams ? "&" : "?").append("classSearch=").append(classSearch);
            hasParams = true;
        }

        if (page != null && !page.trim().isEmpty()) {
            urlBuilder.append(hasParams ? "&" : "?").append("page=").append(page);
        }

        return urlBuilder.toString();
    }

    /**
     * Xử lý lỗi chung
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             Exception e, String userMessage) throws ServletException, IOException {

        System.err.println("Error in ClassRoomServlet: " + e.getMessage());
        e.printStackTrace();

        request.getSession().setAttribute("error", userMessage + ": " + e.getMessage());

        // Redirect về trang chính
        response.sendRedirect(request.getContextPath() + "/training-manager-view-class");
    }
}