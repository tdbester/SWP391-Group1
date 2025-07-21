package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.example.talentcenter.dao.ClassroomDAO;
import org.example.talentcenter.dao.StudentDAO;
import org.example.talentcenter.dao.StudentClassDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Classroom;
import org.example.talentcenter.model.Student;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet for handling Training Manager classroom operations
 * Manages classroom listing and student assignment functionality
 * 
 * @author Training Manager Feature
 */
@WebServlet("/TrainingManagerClassroom")
public class TrainingManagerClassroomServlet extends HttpServlet {
    
    private final ClassroomDAO classroomDAO = new ClassroomDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final StudentClassDAO studentClassDAO = new StudentClassDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listAllClassrooms(request, response);
                break;
            case "addStudents":
                showAddStudentsPage(request, response);
                break;
            default:
                listAllClassrooms(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("addStudentsToClass".equals(action)) {
            addStudentsToClass(request, response);
        } else {
            response.sendRedirect("TrainingManagerClassroom");
        }
    }

    /**
     * Lấy danh sách tất cả các lớp học cho training manager
     */
    private void listAllClassrooms(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            ArrayList<Classroom> classrooms = classroomDAO.getAllClassroomsForTrainingManager();
            request.setAttribute("classrooms", classrooms);
            request.getRequestDispatcher("/View/training-manager-classrooms.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách lớp học: " + e.getMessage());
            request.getRequestDispatcher("/View/training-manager-dashboard.jsp").forward(request, response);
        }
    }

    /**
     * Hiển thị trang thêm học sinh vào lớp học
     */
    private void showAddStudentsPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String classroomIdStr = request.getParameter("classroomId");
            if (classroomIdStr == null || classroomIdStr.trim().isEmpty()) {
                request.setAttribute("error", "ID lớp học không hợp lệ");
                request.getRequestDispatcher("/View/training-manager-classrooms.jsp").forward(request, response);
                return;
            }

            int classroomId = Integer.parseInt(classroomIdStr);

            // Lấy thông tin lớp học
            ArrayList<Classroom> allClassrooms = classroomDAO.getAllClassroomsForTrainingManager();
            Classroom selectedClassroom = null;
            for (Classroom classroom : allClassrooms) {
                if (classroom.getClassroomID() == classroomId) {
                    selectedClassroom = classroom;
                    break;
                }
            }

            if (selectedClassroom == null) {
                request.setAttribute("error", "Không tìm thấy lớp học");
                request.getRequestDispatcher("/View/training-manager-classrooms.jsp").forward(request, response);
                return;
            }

            // Lấy danh sách học sinh có thể thêm vào lớp
            ArrayList<Student> eligibleStudents = studentDAO.getEligibleStudentsForClassroom(classroomId);

            request.setAttribute("classroom", selectedClassroom);
            request.setAttribute("eligibleStudents", eligibleStudents);
            request.getRequestDispatcher("/View/add-students-to-classroom.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID lớp học không hợp lệ");
            request.getRequestDispatcher("/View/training-manager-classrooms.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách học sinh: " + e.getMessage());
            request.getRequestDispatcher("/View/training-manager-classrooms.jsp").forward(request, response);
        }
    }

    /**
     * Thêm nhiều học sinh vào lớp học (Form submission)
     */
    private void addStudentsToClass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String classroomIdStr = request.getParameter("classroomId");
            String[] selectedStudentIds = request.getParameterValues("selectedStudents");

            if (classroomIdStr == null || classroomIdStr.trim().isEmpty()) {
                request.setAttribute("error", "ID lớp học không hợp lệ");
                response.sendRedirect("TrainingManagerClassroom?error=" +
                    java.net.URLEncoder.encode("ID lớp học không hợp lệ", "UTF-8"));
                return;
            }

            if (selectedStudentIds == null || selectedStudentIds.length == 0) {
                response.sendRedirect("TrainingManagerClassroom?action=addStudents&classroomId=" +
                    classroomIdStr + "&error=" +
                    java.net.URLEncoder.encode("Vui lòng chọn ít nhất một học sinh", "UTF-8"));
                return;
            }

            int classroomId = Integer.parseInt(classroomIdStr);

            // Parse student IDs from form array
            ArrayList<Integer> studentIds = new ArrayList<>();

            for (String idStr : selectedStudentIds) {
                try {
                    studentIds.add(Integer.parseInt(idStr.trim()));
                } catch (NumberFormatException e) {
                    response.sendRedirect("TrainingManagerClassroom?action=addStudents&classroomId=" +
                        classroomIdStr + "&error=" +
                        java.net.URLEncoder.encode("ID học sinh không hợp lệ: " + idStr, "UTF-8"));
                    return;
                }
            }

            // Validate that students are not already in the class
            for (Integer studentId : studentIds) {
                if (studentClassDAO.isStudentInClass(studentId, classroomId)) {
                    response.sendRedirect("TrainingManagerClassroom?action=addStudents&classroomId=" +
                        classroomIdStr + "&error=" +
                        java.net.URLEncoder.encode("Một số học sinh đã có trong lớp học này", "UTF-8"));
                    return;
                }
            }

            // Add students to class
            boolean success = studentClassDAO.addMultipleStudentsToClass(studentIds, classroomId);

            if (success) {
                response.sendRedirect("TrainingManagerClassroom?success=" +
                    java.net.URLEncoder.encode("Đã thêm " + studentIds.size() + " học sinh vào lớp thành công", "UTF-8"));
            } else {
                response.sendRedirect("TrainingManagerClassroom?error=" +
                    java.net.URLEncoder.encode("Lỗi khi thêm học sinh vào lớp", "UTF-8"));
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("TrainingManagerClassroom?error=" +
                java.net.URLEncoder.encode("Dữ liệu đầu vào không hợp lệ", "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TrainingManagerClassroom?error=" +
                java.net.URLEncoder.encode("Lỗi hệ thống: " + e.getMessage(), "UTF-8"));
        }
    }

}
