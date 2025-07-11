package org.example.talentcenter.controller;

import org.example.talentcenter.dao.AttendanceDAO;
import org.example.talentcenter.dao.TeacherDAO;
import org.example.talentcenter.model.Attendance;
import org.example.talentcenter.model.ClassRooms;
import org.example.talentcenter.model.Student;
import org.example.talentcenter.model.Teacher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/attendance")
public class AttendanceServlet extends HttpServlet {

    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private TeacherDAO teacherDAO = new TeacherDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "today";

        switch (action) {
            case "today":
                showTodayClasses(request, response);
                break;
            case "list":
                showAllClasses(request, response);
                break;
            case "take":
                showAttendanceForm(request, response);
                break;
            case "edit":
                showEditAttendance(request, response);
                break;
            case "history":
                showAttendanceHistory(request, response);
                break;
            default:
                showAllClasses(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {
            case "save":
                saveAttendance(request, response);
                break;
            case "update":
                updateAttendance(request, response);
                break;
            default:
                showAllClasses(request, response);
                break;
        }
    }

    // Hiển thị tất cả lớp học của giáo viên
    private void showAllClasses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer accountId = (Integer) session.getAttribute("accountId");

        if (accountId == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }

        Teacher teacher = teacherDAO.getTeacherByAccountId(accountId);
        if (teacher == null) {
            request.setAttribute("error", "Không tìm thấy thông tin giáo viên");
            request.getRequestDispatcher("View/attendance.jsp").forward(request, response);
            return;
        }

        List<ClassRooms> allClasses = attendanceDAO.getAllClassesByTeacherId(teacher.getId());

        request.setAttribute("allClasses", allClasses);
        request.setAttribute("currentDate", java.sql.Date.valueOf(LocalDate.now()));
        request.setAttribute("viewType", "all");
        request.getRequestDispatcher("View/attendance.jsp").forward(request, response);
    }

    // Hiển thị danh sách lớp hôm nay của giáo viên
    private void showTodayClasses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer accountId = (Integer) session.getAttribute("accountId");

        if (accountId == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }

        Teacher teacher = teacherDAO.getTeacherByAccountId(accountId);
        if (teacher == null) {
            request.setAttribute("error", "Không tìm thấy thông tin giáo viên");
            request.getRequestDispatcher("View/attendance.jsp").forward(request, response);
            return;
        }

        List<ClassRooms> todayClasses = attendanceDAO.getTodayClassesByTeacherId(teacher.getId());

        request.setAttribute("todayClasses", todayClasses);
        request.setAttribute("currentDate", java.sql.Date.valueOf(LocalDate.now()));
        request.setAttribute("viewType", "today");
        request.getRequestDispatcher("View/attendance.jsp").forward(request, response);
    }

    // Hiển thị form điểm danh cho lớp
    private void showAttendanceForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String classIdStr = request.getParameter("classId");
        if (classIdStr == null || classIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Thiếu tham số classId");
            showAllClasses(request, response);
            return;
        }

        int classRoomId;
        try {
            classRoomId = Integer.parseInt(classIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Tham số classId không hợp lệ");
            showAllClasses(request, response);
            return;
        }

        String dateParam = request.getParameter("date");
        LocalDate attendanceDate;

        if (dateParam != null && !dateParam.isEmpty()) {
            try {
                attendanceDate = LocalDate.parse(dateParam);
            } catch (Exception e) {
                attendanceDate = LocalDate.now();
            }
        } else {
            attendanceDate = LocalDate.now();
        }

        // Lấy scheduleId
        int scheduleId = attendanceDAO.getScheduleIdByClassAndDate(classRoomId, attendanceDate);
        if (scheduleId == -1) {
            request.setAttribute("error", "Không tìm thấy lịch học cho lớp này vào ngày " + attendanceDate);
            showAllClasses(request, response);
            return;
        }

        // Lấy thông tin lớp để truyền sang JSP
        ClassRooms classRoom = attendanceDAO.getClassRoomById(classRoomId);
        request.setAttribute("classRoom", classRoom);
        request.setAttribute("classRoomId", classRoomId);

        // Kiểm tra đã điểm danh chưa
        boolean isAttendanceExist = attendanceDAO.isAttendanceExist(scheduleId);

        if (isAttendanceExist) {
            boolean hasNewStudents = attendanceDAO.hasNewStudentsForAttendance(scheduleId, classRoomId);

            if (hasNewStudents) {
                showMixedAttendanceForm(request, response, classRoomId, scheduleId, attendanceDate);
                return;
            } else {
                response.sendRedirect("attendance?action=edit&scheduleId=" + scheduleId);
                return;
            }
        }

        List<Student> students = attendanceDAO.getStudentsByClassId(classRoomId);

        request.setAttribute("students", students);
        request.setAttribute("scheduleId", scheduleId);
        request.setAttribute("attendanceDate", attendanceDate);
        request.setAttribute("currentDate", java.sql.Date.valueOf(LocalDate.now()));
        request.setAttribute("isNewAttendance", true);
        request.getRequestDispatcher("View/attendance.jsp").forward(request, response);
    }

    // Hiển thị form điểm danh kết hợp (có cả học sinh đã điểm danh và chưa điểm danh)
    private void showMixedAttendanceForm(HttpServletRequest request, HttpServletResponse response,
                                         int classRoomId, int scheduleId, LocalDate attendanceDate)
            throws ServletException, IOException {

        List<Student> studentsWithAttendance = attendanceDAO.getStudentsWithAttendanceStatus(classRoomId, scheduleId);

        // Lấy thông tin lớp để truyền sang JSP
        ClassRooms classRoom = attendanceDAO.getClassRoomById(classRoomId);
        request.setAttribute("classRoom", classRoom);
        request.setAttribute("classRoomId", classRoomId);

        request.setAttribute("studentsWithAttendance", studentsWithAttendance);
        request.setAttribute("scheduleId", scheduleId);
        request.setAttribute("attendanceDate", attendanceDate);
        request.setAttribute("currentDate", java.sql.Date.valueOf(LocalDate.now()));
        request.setAttribute("isMixedAttendance", true);
        request.getRequestDispatcher("View/attendance.jsp").forward(request, response);
    }

    // Hiển thị form chỉnh sửa điểm danh
    private void showEditAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));

        // Lấy danh sách điểm danh hiện tại
        List<Attendance> attendances = attendanceDAO.getAttendanceByScheduleId(scheduleId);

        // Lấy classRoomId từ scheduleId (bạn cần có hàm này trong AttendanceDAO)
        int classRoomId = attendanceDAO.getClassIdByScheduleId(scheduleId);
        ClassRooms classRoom = attendanceDAO.getClassRoomById(classRoomId);
        request.setAttribute("classRoom", classRoom);
        request.setAttribute("classRoomId", classRoomId);

        request.setAttribute("attendances", attendances);
        request.setAttribute("scheduleId", scheduleId);
        request.setAttribute("currentDate", java.sql.Date.valueOf(LocalDate.now()));
        request.setAttribute("isEdit", true);
        request.getRequestDispatcher("View/attendance.jsp").forward(request, response);
    }

    // Hiển thị lịch sử điểm danh của một lớp
    private void showAttendanceHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String classIdStr = request.getParameter("classId");
        if (classIdStr == null || classIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Thiếu tham số classId");
            showAllClasses(request, response);
            return;
        }

        int classRoomId;
        try {
            classRoomId = Integer.parseInt(classIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Tham số classId không hợp lệ");
            showAllClasses(request, response);
            return;
        }

        // Lấy lịch sử điểm danh
        List<Attendance> attendanceHistory = attendanceDAO.getAttendanceHistoryByClassId(classRoomId);

        // Lấy thông tin lớp để truyền sang JSP
        ClassRooms classRoom = attendanceDAO.getClassRoomById(classRoomId);
        request.setAttribute("classRoom", classRoom);
        request.setAttribute("classRoomId", classRoomId);

        request.setAttribute("attendanceHistory", attendanceHistory);
        request.setAttribute("currentDate", java.sql.Date.valueOf(LocalDate.now()));
        request.setAttribute("viewType", "history");
        request.getRequestDispatcher("View/attendance.jsp").forward(request, response);
    }

    // Lưu điểm danh mới
    private void saveAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String scheduleIdStr = request.getParameter("scheduleId");
        if (scheduleIdStr == null || scheduleIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Thiếu thông tin scheduleId");
            showAttendanceForm(request, response);
            return;
        }

        int scheduleId;
        try {
            scheduleId = Integer.parseInt(scheduleIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "scheduleId không hợp lệ");
            showAttendanceForm(request, response);
            return;
        }

        String[] studentIds = request.getParameterValues("studentId");
        if (studentIds == null || studentIds.length == 0) {
            request.setAttribute("error", "Không có học sinh nào để điểm danh");
            showAttendanceForm(request, response);
            return;
        }

        List<Attendance> newAttendances = new ArrayList<>();
        List<Attendance> updateAttendances = new ArrayList<>();

        for (String studentIdStr : studentIds) {
            if (studentIdStr == null || studentIdStr.trim().isEmpty()) {
                continue;
            }

            int studentId;
            try {
                studentId = Integer.parseInt(studentIdStr);
            } catch (NumberFormatException e) {
                continue;
            }

            String status = request.getParameter("status_" + studentId);
            String note = request.getParameter("note_" + studentId);
            String attendanceIdStr = request.getParameter("attendanceId_" + studentId);

            if (status == null || status.trim().isEmpty()) {
                status = Attendance.ABSENT;
            }

            if (attendanceIdStr != null && !attendanceIdStr.trim().isEmpty()) {
                try {
                    int attendanceId = Integer.parseInt(attendanceIdStr);
                    Attendance attendance = new Attendance();
                    attendance.setId(attendanceId);
                    attendance.setStatus(status);
                    attendance.setNote(note);
                    updateAttendances.add(attendance);
                } catch (NumberFormatException e) {
                    // Ignore invalid attendance ID
                }
            } else {
                Attendance attendance = new Attendance(scheduleId, studentId, status, note);
                newAttendances.add(attendance);
            }
        }

        boolean success = true;
        String message = "";

        if (!newAttendances.isEmpty()) {
            boolean addSuccess = attendanceDAO.addBulkAttendance(scheduleId, newAttendances);
            if (!addSuccess) {
                success = false;
                message += "Lỗi khi thêm điểm danh cho học sinh mới. ";
            }
        }

        if (!updateAttendances.isEmpty()) {
            for (Attendance attendance : updateAttendances) {
                boolean updateSuccess = attendanceDAO.updateAttendance(attendance);
                if (!updateSuccess) {
                    success = false;
                    message += "Lỗi khi cập nhật điểm danh. ";
                    break;
                }
            }
        }

        if (success) {
            if (!newAttendances.isEmpty() && !updateAttendances.isEmpty()) {
                request.setAttribute("success", "Đã thêm điểm danh cho học sinh mới và cập nhật điểm danh thành công!");
            } else if (!newAttendances.isEmpty()) {
                request.setAttribute("success", "Đã thêm điểm danh cho học sinh mới thành công!");
            } else if (!updateAttendances.isEmpty()) {
                request.setAttribute("success", "Cập nhật điểm danh thành công!");
            } else {
                request.setAttribute("success", "Điểm danh thành công!");
            }
        } else {
            request.setAttribute("error", message.trim());
        }

        showAllClasses(request, response);
    }

    // Cập nhật điểm danh
    private void updateAttendance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
        String[] attendanceIds = request.getParameterValues("attendanceId");

        if (attendanceIds == null || attendanceIds.length == 0) {
            request.setAttribute("error", "Không có dữ liệu để cập nhật");
            showEditAttendance(request, response);
            return;
        }

        boolean allSuccess = true;

        for (String attendanceIdStr : attendanceIds) {
            int attendanceId = Integer.parseInt(attendanceIdStr);
            String status = request.getParameter("status_" + attendanceId);
            String note = request.getParameter("note_" + attendanceId);

            Attendance attendance = new Attendance();
            attendance.setId(attendanceId);
            attendance.setStatus(status);
            attendance.setNote(note);

            boolean success = attendanceDAO.updateAttendance(attendance);
            if (!success) {
                allSuccess = false;
            }
        }

        if (allSuccess) {
            request.setAttribute("success", "Cập nhật điểm danh thành công!");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi cập nhật một số điểm danh");
        }

        showAllClasses(request, response);
    }
}
