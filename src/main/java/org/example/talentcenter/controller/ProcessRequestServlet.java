/*
 *  Copyright (C) 2025 <Group 1>
 *  All rights reserved.
 *
 *  This file is part of the <Talent Center Management> project.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *  Proprietary and confidential.
 *
 *  Created on:        2025-07-04
 *  Author:            Cù Thị Huyền Trang
 *
 *  ========================== Change History ==========================
 *  Date        | Author               | Description
 *  ------------|----------------------|--------------------------------
 *  2025-07-04  | Cù Thị Huyền Trang   | Initial creation
 */


package org.example.talentcenter.controller;

import org.example.talentcenter.dao.*;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.talentcenter.model.Schedule;
import org.example.talentcenter.service.NotificationService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "ProcessRequestServlet", value = "/ProcessRequest")
public class ProcessRequestServlet extends HttpServlet {
    private RequestDAO requestDAO = new RequestDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private TeacherScheduleDAO scheduleDAO = new TeacherScheduleDAO();
private TeacherDAO teacherDAO = new TeacherDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }

        ArrayList<Request> requestTypes = requestDAO.getStudentRequestType();
        request.setAttribute("requestTypes", requestTypes);

        try {
            String requestIdParam = request.getParameter("id");
            if (requestIdParam != null && !requestIdParam.trim().isEmpty()) {
                int requestId = Integer.parseInt(requestIdParam);
                Request requestDetail = requestDAO.getRequestDetailById(requestId);
                if (requestDetail == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn");
                    return;
                }
                request.setAttribute("requestDetail", requestDetail);
                String type = requestDetail.getTypeName();
                String fullReason = requestDetail.getReason();

                if ("Đơn xin nghỉ phép".equals(type)) {
                    if (fullReason != null && fullReason.contains("|")) {
                        String[] parts = fullReason.split("\\|");
                        if (parts.length >= 2) {
                            requestDetail.setReason(parts[1]);
                            try {
                                requestDetail.setOffDate(java.time.LocalDate.parse(parts[0]));
                            } catch (Exception e) {
                                System.out.println("Lỗi parse ngày nghỉ: " + e.getMessage());
                            }// Cập nhật lại lý do
                        }
                    }
                }

                if ("Đơn xin đổi lịch dạy".equals(type) && requestDetail.getScheduleId() > 0) {
                    try {
                        Schedule schedule = scheduleDAO.getScheduleById(requestDetail.getScheduleId());
                        if (schedule != null) {
                            request.setAttribute("oldSchedule", schedule);
                        }
                    } catch (Exception e) {
                        System.out.println("Lỗi khi lấy schedule: " + e.getMessage());
                    }
                }

                request.getRequestDispatcher("/View/process-request-detail.jsp").forward(request, response);
                return;
            }

            // Nếu không có id thì xử lý theo action
            String action = request.getParameter("action");
            if (action == null || action.equals("list")) {
                int page = 1;
                int recordsPerPage = 10;
                try {
                    String pageParam = request.getParameter("page");
                    if (pageParam != null) page = Integer.parseInt(pageParam);
                } catch (NumberFormatException ignored) {
                }

                int offset = (page - 1) * recordsPerPage;
                ArrayList<Request> requestList = requestDAO.getAllRequestWithPaging(offset, recordsPerPage);
                int totalRecords = requestDAO.getTotalRequestCount();
                int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

                request.setAttribute("requestList", requestList);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);

            } else if (action.equals("search")) {
                String keyword = request.getParameter("keyword");
                if (keyword == null || keyword.trim().isEmpty()) {
                    response.sendRedirect("ProcessRequest?action=list");
                    return;
                }
                ArrayList<Request> requestList = requestDAO.searchRequests(keyword.trim());
                request.setAttribute("requestList", requestList);
                request.setAttribute("keyword", keyword);
                request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);

            } else if (action.equals("filterByType")) {
                String typeFilter = request.getParameter("typeFilter");
                if (typeFilter == null || typeFilter.trim().isEmpty()) {
                    response.sendRedirect("ProcessRequest?action=list");
                    return;
                }
                ArrayList<Request> requestList = requestDAO.filterRequestsByType(typeFilter.trim());
                request.setAttribute("requestList", requestList);
                request.setAttribute("typeFilter", typeFilter);
                request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);

            } else if (action.equals("filterByStatus")) {
                String statusFilter = request.getParameter("statusFilter");
                if (statusFilter == null || statusFilter.trim().isEmpty()) {
                    response.sendRedirect("ProcessRequest?action=list");
                    return;
                }
                ArrayList<Request> requestList = requestDAO.filterRequestsByStatus(statusFilter.trim());
                request.setAttribute("requestList", requestList);
                request.setAttribute("statusFilter", statusFilter);
                request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đơn không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }

        try {
            String requestIdParam = request.getParameter("requestId");
            String managerNote = request.getParameter("managerNote");
            String action = request.getParameter("action");

            if (requestIdParam == null || managerNote == null || action == null
                    || requestIdParam.trim().isEmpty() || managerNote.trim().isEmpty() || action.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Thiếu thông tin xử lý đơn.");
                request.getRequestDispatcher("View/error.jsp").forward(request, response);
                return;
            }

            int requestId = Integer.parseInt(requestIdParam);
            Request requestDetail = requestDAO.getRequestDetailById(requestId);
            String reason = requestDetail.getReason();
            if (reason != null && reason.contains("|")) {
                String[] parts = reason.split("\\|");
                if (parts.length >= 4) {
                    try {
                        int scheduleId = Integer.parseInt(parts[3]);
                        Schedule schedule = scheduleDAO.getScheduleById(scheduleId);
                        request.setAttribute("oldSchedule", schedule);  // Gửi qua JSP
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            if (requestDetail == null) {
                request.setAttribute("errorMessage", "Không tìm thấy đơn cần xử lý.");
                request.getRequestDispatcher("View/error.jsp").forward(request, response);
                return;
            }

            String status;
            if ("approve".equals(action)) {
                status = "Đã duyệt";
                if ("Đơn xin chuyển lớp".equals(requestDetail.getTypeName())) {
                    if (reason != null && reason.contains("TARGET_CLASS:")) {
                        String targetClassName = reason.split("\\|TARGET_CLASS:")[1];
                        studentDAO.transferStudentToClass(requestDetail.getSenderID(), targetClassName);
                    }
                }
                if ("Đơn xin đổi lịch dạy".equals(requestDetail.getTypeName())) {
                    int scheduleId = requestDetail.getScheduleId();
                    LocalDate toDate = requestDetail.getToDate();
                    int toSlot = requestDetail.getSlot();

                    if (scheduleId > 0 && toDate != null && toSlot > 0) {
                        boolean updated = scheduleDAO.updateScheduleDateAndSlot(scheduleId, toDate.toString(), toSlot);
                        if (!updated) {
                            request.setAttribute("errorMessage", "Không thể cập nhật lịch học.");
                            request.getRequestDispatcher("View/error.jsp").forward(request, response);
                            return;
                        }
                        Schedule newSchedule = scheduleDAO.getScheduleById(scheduleId);
                        if (newSchedule != null) {
                            // Lấy danh sách học sinh của lớp này
                            ArrayList<Account> students = studentDAO.getStudentsByClassId(newSchedule.getClassRoomId());
                            for (Account student : students) {
                                try {
                                    NotificationService.notifyStudentScheduleChanged(
                                            student.getId(),
                                            requestDetail.getSenderName(),
                                            newSchedule.getClassName(),
                                            toDate,
                                            newSchedule.getSlotStartTime(),
                                            newSchedule.getSlotEndTime(),
                                            newSchedule.getId()
                                    );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            request.setAttribute("errorMessage", "Dữ liệu lịch không hợp lệ!");
                            request.getRequestDispatcher("View/error.jsp").forward(request, response);
                            return;
                        }
                    }
                    if ("Đơn xin nghỉ phép".equals(requestDetail.getTypeName())) {
                        LocalDate offDate = requestDetail.getOffDate();
                        int teacherId = teacherDAO.getTeacherByAccountId(requestDetail.getSenderID()).getId();

                        if (offDate != null) {
                            ArrayList<Schedule> schedules = scheduleDAO.getScheduleByTeacherIdAndDate(teacherId, offDate);
                            System.out.println("OffDate: " + offDate);
                            System.out.println("Số lịch dạy trong ngày nghỉ: " + schedules.size());

                            for (Schedule schedule : schedules) {
                                ArrayList<Account> students = studentDAO.getStudentsByClassId(schedule.getClassRoomId());
                                for (Account student : students) {

                                    try {
                                        NotificationService.notifyStudentAbsence(
                                                student.getId(),
                                                requestDetail.getSenderName(),
                                                schedule.getClassName(),
                                                offDate,
                                                schedule.getSlotStartTime(),
                                                schedule.getSlotEndTime(),
                                                schedule.getId()
                                        );
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }

            } else if ("reject".equals(action)) {
                status = "Từ chối";
            } else {
                request.setAttribute("errorMessage", "Hành động không hợp lệ.");
                request.getRequestDispatcher("View/error.jsp").forward(request, response);
                return;
            }

            boolean success = requestDAO.processRequest(requestId, status, managerNote, new Timestamp(new Date().getTime()));

            if (success) {
                try {
                    NotificationService.notifyRequestProcessed(
                            requestId,
                            requestDetail.getSenderName(),
                            requestDetail.getTypeName(),
                            status,
                            managerNote,
                            requestDetail.getSenderID()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.sendRedirect("ProcessRequest?action=list&success=1");
            } else {
                request.setAttribute("errorMessage", "Không thể cập nhật trạng thái đơn.");
                request.getRequestDispatcher("View/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý đơn.");
            request.getRequestDispatcher("View/error.jsp").forward(request, response);
        }
    }

}
