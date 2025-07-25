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
        RequestDAO requestDAO = new RequestDAO();
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (role == null || !"học sinh".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        int accountId = account.getId();
        String action = request.getParameter("action");
        // lấy danh sách loa đơn của hs
        ArrayList<Request> requestTypeList = requestDAO.getStudentRequestType();
        request.setAttribute("requestTypeList", requestTypeList);

        if ("list".equals(action)) {
            int page = 1;
            int recordsPerPage = 10;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            String keyword = request.getParameter("keyword");
            String statusFilter = request.getParameter("statusFilter");
            String filterTypeIdParam = request.getParameter("filterTypeId");
            Integer typeId = null;
            if (filterTypeIdParam != null && !filterTypeIdParam.isEmpty()) {
                try {
                    typeId = Integer.parseInt(filterTypeIdParam);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            // đếm tổng số bản ghi
            int totalRecords = requestDAO.countStudentRequestsFiltered(accountId, keyword, typeId, statusFilter);

            ArrayList<Request> requestList = requestDAO.getStudentRequestsFiltered(accountId, keyword, typeId, statusFilter, (page - 1) * recordsPerPage, recordsPerPage);
            
            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
            
            request.setAttribute("requestList", requestList);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);
            request.getRequestDispatcher("/View/student-request-list.jsp").forward(request, response);
        } else {
            ClassroomDAO classroomDAO = new ClassroomDAO();
            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.getStudentById(accountId);
            int studentId = student.getId();
            // lấy ra danh sách các lớp của hs
            ArrayList<Classroom> classroomList = classroomDAO.getAllStudentClassByStudentId(studentId);
            // lấy ra loại đơn dc chọn
            String selectedRequestType = request.getParameter("requestType");
            request.setAttribute("requestTypeId", selectedRequestType);

            // Xử lý riêng cho đơn chuyển lớp
            if ("1".equals(selectedRequestType)) {
                request.setAttribute("isTransferRequest", true);
                request.setAttribute("selectedRequestType", selectedRequestType);

                // load danh sách lớp hiện có
                ArrayList<Classroom> availableClasses = classroomDAO.getAvailableClassrooms();
                request.setAttribute("availableClasses", availableClasses);

                // load lịch sau khi chọn lớp
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
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (role == null || !"học sinh".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("create".equals(action)) {
            account = (Account) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            // lấy dữ liệu từ form
            Integer senderId = account.getId();
            String phoneNumber = request.getParameter("phoneNumber");
            String currentClass = request.getParameter("currentClass");
            String parentPhone = request.getParameter("parentPhone");
            String detailedReason = request.getParameter("detailedReason");
            String requestTypeIdStr = request.getParameter("requestType");

            //lưu lại giữ liệu để trả về phòng lỗi
            request.setAttribute("preservePhoneNumber", request.getParameter("phoneNumber"));
            request.setAttribute("preserveCurrentClass", request.getParameter("currentClass"));
            request.setAttribute("preserveParentPhone", request.getParameter("parentPhone"));
            request.setAttribute("preserveDetailedReason", request.getParameter("detailedReason"));
            request.setAttribute("preserveSelectedClass", request.getParameter("selectedClass"));

            //validate thông tin
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

            // lưu tất cả thông tin vào reason
            String combinedReason = currentClass + "|" + parentPhone + "|" + phoneNumber + "|" + detailedReason;

            // thêm lớp mốn chuyển đến nếu là đơn xin chuyển lớp
            if (requestTypeId == 1) {
                String selectedClass = request.getParameter("selectedClass");
                if (selectedClass != null && !selectedClass.trim().isEmpty()) {
                    combinedReason += "|TRANSFER_TO_CLASS_ID:" + selectedClass;
                }
            }

            // lưu dữ liệu
            Request studentRequest = new Request();
            studentRequest.setSenderID(senderId);
            studentRequest.setPhoneNumber(phoneNumber);
            studentRequest.setCourseName(currentClass);
            studentRequest.setParentPhone(parentPhone);
            studentRequest.setReason(combinedReason);
            studentRequest.setCreatedAt(utilDate);
            studentRequest.setTypeId(requestTypeId);
            studentRequest.setStatus("Chờ xử lý");

            RequestDAO dao = new RequestDAO();
            boolean success = dao.insert(studentRequest);

            if (success) {
                String requestTypeName = getRequestTypeName(requestTypeId);
                NotificationService.notifyStudentRequestSubmitted(
                        account.getFullName(),
                        requestTypeName,
                        studentRequest.getId(),
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

    /**
     * Tìm và trả về đối tượng Classroom có ID trùng với classId trong danh sách classrooms.
     *
     * @param classId     ID của lớp cần tìm.
     * @param classrooms  Danh sách các Classroom để tìm kiếm.
     * @return            Đối tượng Classroom có ID trùng với classId, hoặc null nếu không tìm thấy.
     *
     * @author Huyen Trang
     */
    private Classroom getClassroomById(int classId, ArrayList<Classroom> classrooms) {
        for (Classroom classroom : classrooms) {
            if (classroom.getClassroomID() == classId) {
                return classroom;
            }
        }
        return null;
    }

    /**
     * Chuyển hướng (redirect) về trang StudentApplication đồng thời giữ lại dữ liệu đã nhập trên form
     * bằng cách thêm các tham số không rỗng từ request vào URL dưới dạng query string.
     * Thường dùng khi form có lỗi, cần giữ dữ liệu để người dùng không phải nhập lại.
     *
     * @param request  Đối tượng HttpServletRequest chứa dữ liệu form
     * @param response Đối tượng HttpServletResponse để gửi lệnh chuyển hướng
     * @throws IOException Nếu có lỗi xảy ra trong quá trình chuyển hướng
     *
     * @author Huyen Trang
     */
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

        String selectedClass = request.getParameter("selectedClass");
        if (selectedClass != null && !selectedClass.trim().isEmpty()) {
            redirectUrl.append("selectedClass=").append(URLEncoder.encode(selectedClass, "UTF-8")).append("&");
        }

        response.sendRedirect(redirectUrl.toString());
    }

    /**
     * Lấy tên đơn theo id
     *
     * @param typeId ID của đơn
     * @return tên của đơn
     * @author Huyen Trang
     */
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