/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-06-13
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-06-13  | Cù Thị Huyền Trang   | Initial creation
 */

package org.example.talentcenter.controller;

import org.example.talentcenter.dao.StudentClassDAO;
import org.example.talentcenter.dao.StudentDAO;
import org.example.talentcenter.dao.CourseDAO;
import org.example.talentcenter.dao.RequestDAO;
import org.example.talentcenter.model.*;

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
        RequestDAO requestDAO = new RequestDAO();
        Integer accountId = (Integer) session.getAttribute("accountId");
        Account account = (Account) session.getAttribute("account");
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
            request.getRequestDispatcher("/View/student-application-list.jsp").forward(request, response);
        } else {
            StudentClassDAO classDAO = new StudentClassDAO();
            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.getStudentById(accountId);
            int studentId = student.getId();
            ArrayList<StudentClass> classList = classDAO.getAllStudentClassByStudentId(studentId);
            request.setAttribute("student", student);
            request.setAttribute("classList", classList);
            request.setAttribute("studentName", account.getFullName());
            request.setAttribute("phoneNumber", account.getPhoneNumber());
            request.getRequestDispatcher("/View/student-application.jsp").forward(request, response);
        }
    }

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
            String requestDateStr = request.getParameter("requestDate");
            String requestTypeIdStr = request.getParameter("requestTypeId");
            if (requestTypeIdStr == null || requestTypeIdStr.trim().isEmpty()) {
                session.setAttribute("error", "Vui lòng chọn loại đơn!");
                response.sendRedirect("StudentApplication");
                return;
            }
            int requestTypeId = Integer.parseInt(requestTypeIdStr);
            if (detailedReason == null || detailedReason.trim().length() < 20) {
                session.setAttribute("error", "Mô tả lý do phải có ít nhất 20 ký tự!");
                response.sendRedirect("StudentApplication");
                return;
            }

            java.util.Date utilDate;
            try {
                utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(requestDateStr);
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Định dạng ngày không hợp lệ!");
                response.sendRedirect("StudentApplication");
                return;
            }

            Request studentRequest = new Request();
            studentRequest.setSenderID(senderId);
            studentRequest.setPhoneNumber(phoneNumber);
            studentRequest.setCourseName(currentClass);
            studentRequest.setParentPhone(parentPhone);
            studentRequest.setReason(detailedReason);
            studentRequest.setCreatedAt(utilDate);
            studentRequest.setTypeId(requestTypeId);

            RequestDAO dao = new RequestDAO();
            boolean success = dao.insert(studentRequest);
            System.out.println("Insert result: " + success);
            if (success) {
                session.setAttribute("message", "Đơn đã được gửi thành công!");
            } else {
                session.setAttribute("error", "Gửi đơn thất bại! Vui lòng thử lại.");
            }
            response.sendRedirect("StudentApplication");
        }
    }
}
