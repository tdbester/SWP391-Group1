/*--%>
<%--*  Copyright (C) 2025 <Group 1>--%>
<%--    *  All rights reserved.--%>
<%--    *--%>
<%--    *  This file is part of the <Talent Center Management> project.--%>
<%--    *  Unauthorized copying of this file, via any medium is strictly prohibited.--%>
<%--    *  Proprietary and confidential.--%>
<%--    *--%>
<%--    *  Created on:        2025-06-13--%>
<%--    *  Author:            Cù Thị Huyền Trang--%>
<%--    *--%>
<%--    *  ========================== Change History ==========================--%>
<%--    *  Date        | Author               | Description--%>
<%--    *  ------------|----------------------|----------------------------------%>
<%--    *  2025-06-13  | Cù Thị Huyền Trang   | Initial creation--%>
<%--    */

package org.example.talentcenter.controller;

import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.talentcenter.model.Classroom;
import org.example.talentcenter.model.Student;
import org.example.talentcenter.service.NotificationService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "StudentApplicationServlet", value = "/StudentApplication")
public class StudentRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        RequestDAO requestDAO = new RequestDAO();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int accountId = account.getId();
        String action = request.getParameter("action");
        ArrayList<Request> requestTypeList = requestDAO.getStudentRequestType();
        request.setAttribute("requestTypeList", requestTypeList);

        if ("list".equals(action)) {
            String filterTypeIdParam = request.getParameter("filterTypeId");
            ArrayList<Request> requestList;
            if (filterTypeIdParam != null && !filterTypeIdParam.isEmpty()) {
                int filterTypeId = Integer.parseInt(filterTypeIdParam);
                requestList = requestDAO.getRequestBySenderIdAndType(accountId, filterTypeId);
            } else {
                requestList = requestDAO.getRequestBySenderId(accountId);
            }
            request.setAttribute("requestList", requestList);
            request.getRequestDispatcher("/View/student-request-list.jsp").forward(request, response);
        } else {
            ClassroomDAO classroomDAO = new ClassroomDAO();
            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.getStudentById(accountId);
            int studentId = student.getId();
            ArrayList<Classroom> classroomList = classroomDAO.getAllStudentClassByStudentId(studentId);
            String selectedRequestType = request.getParameter("requestType");
            request.setAttribute("requestTypeId", selectedRequestType);

            // Xử lý riêng cho đơn chuyển lớp
            if ("1".equals(selectedRequestType)) {
                request.setAttribute("isTransferRequest", true);
                request.setAttribute("selectedRequestType", selectedRequestType);

                // Load danh sách lớp hiện có
                ArrayList<Classroom> availableClasses = classroomDAO.getAvailableClassrooms();
                request.setAttribute("availableClasses", availableClasses);

                // Load lịch sau khi chọn lớp
                String selectedClassId = request.getParameter("selectedClass");
                if (selectedClassId != null && !selectedClassId.trim().isEmpty()) {
                    try {
                        int classId = Integer.parseInt(selectedClassId);
                        ArrayList<StudentSchedule> classSchedules = classroomDAO.getClassSchedule(classId);
                        request.setAttribute("classSchedules", classSchedules);
                        request.setAttribute("selectedClassId", selectedClassId);

                        Classroom selectedClass = getClassroomById(classId, availableClasses);
                        if (selectedClass != null) {
                            request.setAttribute("selectedClassInfo", selectedClass);
                        }
                    } catch (NumberFormatException e) {
                        // Ignore invalid class ID
                    }
                }
            }

            request.setAttribute("student", student);
            request.setAttribute("classList", classroomList);
            request.setAttribute("studentName", account.getFullName());
            request.setAttribute("phoneNumber", account.getPhoneNumber());
            request.getRequestDispatcher("/View/student-request.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            HttpSession session = request.getSession(false);
            Account account = (Account) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            Integer senderId = account.getId();
            String phoneNumber = request.getParameter("phoneNumber");
            String currentClass = request.getParameter("currentClass");
            String parentPhone = request.getParameter("parentPhone");
            String detailedReason = request.getParameter("detailedReason");
            String requestTypeIdStr = request.getParameter("requestType");

            // Thêm hidden inputs để preserve data khi POST
            request.setAttribute("preservePhoneNumber", request.getParameter("phoneNumber"));
            request.setAttribute("preserveCurrentClass", request.getParameter("currentClass"));
            request.setAttribute("preserveParentPhone", request.getParameter("parentPhone"));
            request.setAttribute("preserveDetailedReason", request.getParameter("detailedReason"));
            request.setAttribute("preserveSelectedClass", request.getParameter("selectedClass"));

            // Validate form data
            if (requestTypeIdStr == null || requestTypeIdStr.trim().isEmpty()) {
                session.setAttribute("error", "Vui lòng chọn loại đơn!");
                redirectWithFormData(request, response);
                return;
            }

            if (detailedReason == null || detailedReason.trim().length() < 20) {
                session.setAttribute("error", "Mô tả lý do phải có ít nhất 20 ký tự!");
                redirectWithFormData(request, response);
                return;
            }

            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                session.setAttribute("error", "Vui lòng nhập số điện thoại!");
                redirectWithFormData(request, response);
                return;
            }

            if (currentClass == null || currentClass.trim().isEmpty()) {
                session.setAttribute("error", "Vui lòng chọn lớp hiện tại!");
                redirectWithFormData(request, response);
                return;
            }

            if (parentPhone == null || parentPhone.trim().isEmpty()) {
                session.setAttribute("error", "Vui lòng nhập số điện thoại phụ huynh!");
                redirectWithFormData(request, response);
                return;
            }

            int requestTypeId = Integer.parseInt(requestTypeIdStr);

            // Validate cho đơn chuyển lớp
            if (requestTypeId == 1) {
                String selectedClass = request.getParameter("selectedClass");
                if (selectedClass == null || selectedClass.trim().isEmpty()) {
                    session.setAttribute("error", "Vui lòng chọn lớp muốn chuyển tới!");
                    redirectWithFormData(request, response);
                    return;
                }
            }

            // Tạo ngày hiện tại
            Date utilDate = new Date();

            // Tạo combined reason
            String combinedReason = currentClass + "|" + parentPhone + "|" + phoneNumber + "|" + detailedReason;

            if (requestTypeId == 1) {
                String selectedClass = request.getParameter("selectedClass");
                if (selectedClass != null && !selectedClass.trim().isEmpty()) {
                    combinedReason += "|TRANSFER_TO_CLASS_ID:" + selectedClass;
                }
            }

            // Tạo request object
            Request studentRequest = new Request();
            studentRequest.setSenderID(senderId);
            studentRequest.setPhoneNumber(phoneNumber);
            studentRequest.setCourseName(currentClass);
            studentRequest.setParentPhone(parentPhone);
            studentRequest.setReason(combinedReason);
            studentRequest.setCreatedAt(utilDate);
            studentRequest.setTypeId(requestTypeId);

            // Lưu vào database
            RequestDAO dao = new RequestDAO();
            boolean success = dao.insert(studentRequest);

            if (success) {
                String requestTypeName = getRequestTypeName(requestTypeId); // Tạo method helper này
                NotificationService.notifyStudentRequestSubmitted(
                        account.getFullName(),
                        requestTypeName,
                        studentRequest.getId(), // Cần get ID sau khi insert
                        account.getId()
                );

                session.setAttribute("message", "Đơn đã được gửi thành công!");
                response.sendRedirect("StudentApplication");
            } else {
                session.setAttribute("error", "Gửi đơn thất bại! Vui lòng thử lại.");
                redirectWithFormData(request, response);
            }
        }
    }

    private Classroom getClassroomById(int classId, ArrayList<Classroom> classrooms) {
        for (Classroom classroom : classrooms) {
            if (classroom.getClassroomID() == classId) {
                return classroom;
            }
        }
        return null;
    }

    private void redirectWithFormData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder redirectUrl = new StringBuilder("StudentApplication?");

        String requestType = request.getParameter("requestTypeId");
        if (requestType != null && !requestType.trim().isEmpty()) {
            redirectUrl.append("requestType=").append(URLEncoder.encode(requestType, "UTF-8")).append("&");
        }

        String phoneNumber = request.getParameter("phoneNumber");
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            redirectUrl.append("phoneNumber=").append(URLEncoder.encode(phoneNumber, "UTF-8")).append("&");
        }

        String currentClass = request.getParameter("currentClass");
        if (currentClass != null && !currentClass.trim().isEmpty()) {
            redirectUrl.append("currentClass=").append(URLEncoder.encode(currentClass, "UTF-8")).append("&");
        }

        String parentPhone = request.getParameter("parentPhone");
        if (parentPhone != null && !parentPhone.trim().isEmpty()) {
            redirectUrl.append("parentPhone=").append(URLEncoder.encode(parentPhone, "UTF-8")).append("&");
        }

        String detailedReason = request.getParameter("detailedReason");
        if (detailedReason != null && !detailedReason.trim().isEmpty()) {
            redirectUrl.append("detailedReason=").append(URLEncoder.encode(detailedReason, "UTF-8")).append("&");
        }

        // Sửa từ targetClass thành selectedClass
        String selectedClass = request.getParameter("selectedClass");
        if (selectedClass != null && !selectedClass.trim().isEmpty()) {
            redirectUrl.append("selectedClass=").append(URLEncoder.encode(selectedClass, "UTF-8")).append("&");
        }

        response.sendRedirect(redirectUrl.toString());
    }
    private String getRequestTypeName(int typeId) {
        switch (typeId) {
            case 1:
                return "Đơn xin chuyển lớp";
            case 3:
                return "Đơn xin nghỉ học";
            case 4:
                return "Đơn khiếu nại về giảng viên";
            default:
                return "Đơn khác";
        }
    }
}