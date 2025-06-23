package org.example.talentcenter.controller;

import org.example.talentcenter.dao.StudentDAO;
import org.example.talentcenter.dao.CourseDAO;
import org.example.talentcenter.dao.RequestDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Course;
import org.example.talentcenter.model.Request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.talentcenter.model.Student;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@WebServlet(name = "StudentApplicationServlet", value = "/StudentApplication")
public class StudentApplicationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer studentId = (Integer) session.getAttribute("accountId");
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            // Hiển thị danh sách đơn
            RequestDAO requestDAO = new RequestDAO();
            ArrayList<Request> requestList = requestDAO.getRequestBySenderId(studentId);

            request.setAttribute("requestList", requestList);
            request.getRequestDispatcher("/View/student-application-list.jsp").forward(request, response);
        } else {
            StudentDAO studentDAO = new StudentDAO();
            CourseDAO courseDAO = new CourseDAO();

            Student student = studentDAO.getStudentById(studentId);
            ArrayList<Course> courseList = courseDAO.getAllCourses();

            request.setAttribute("student", student);
            request.setAttribute("courseList", courseList);

            request.getRequestDispatcher("/View/student-application.jsp").forward(request, response);
        }  }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            HttpSession session = request.getSession(false);
            Integer senderId = (Integer) session.getAttribute("accountId");
            if (senderId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            String phoneNumber = request.getParameter("phoneNumber");
            String currentClass = request.getParameter("currentClass");
            String parentPhone = request.getParameter("parentPhone");
            String detailedReason = request.getParameter("detailedReason");
            String requestDateStr = request.getParameter("requestDate");  // yyyy-MM-dd format expected

            // Chuyển từ String sang java.util.Date rồi sang Timestamp
            java.util.Date utilDate;
            try {
                utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(requestDateStr);
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Định dạng ngày không hợp lệ!");
                response.sendRedirect("TransferRequest");
                return;
            }

            Request transferRequest = new Request();
            transferRequest.setSenderID(senderId);
            transferRequest.setPhoneNumber(phoneNumber);
            transferRequest.setCourseName(currentClass);
            transferRequest.setParentPhone(parentPhone);
            transferRequest.setReason(detailedReason);
            transferRequest.setCreatedAt(utilDate);  // kiểu java.util.Date

            RequestDAO dao = new RequestDAO();
            dao.insert(transferRequest);

            session.setAttribute("message", "Đơn xin chuyển lớp đã được gửi thành công!");
            response.sendRedirect("StudentApplication");
        }
    }
}
