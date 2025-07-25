<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:choose><c:when test="${not empty teacher}">Chỉnh sửa giáo viên</c:when><c:otherwise>Thêm giáo viên mới</c:otherwise></c:choose></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .form-container {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .btn-primary {
            background-color: #7a6ad8;
            border-color: #7a6ad8;
        }
        .btn-primary:hover {
            background-color: #6a5acd;
            border-color: #6a5acd;
        }
        .invalid-feedback {
            display: none;
        }

        .was-validated .form-control:invalid ~ .invalid-feedback {
            display: block;
        }

        .was-validated .form-control:valid ~ .invalid-feedback {
            display: none;
        }
    </style>
</head>

<body>
<jsp:include page="header.jsp" />

<div class="">
    <%
        String userRole = (String) session.getAttribute("userRole");
        if ("admin".equalsIgnoreCase(userRole)) {
    %>
    <jsp:include page="admin-sidebar.jsp" />
    <%
    } else {
    %>
    <jsp:include page="training-manager-sidebar.jsp" />
    <%
        }
    %>

    <div class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>
                <c:choose>
                    <c:when test="${not empty teacher}">Chỉnh sửa giáo viên</c:when>
                    <c:otherwise>Thêm giáo viên mới</c:otherwise>
                </c:choose>
            </h2>
            <a href="teachers" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Quay lại danh sách
            </a>
        </div>

        <!-- Error Message -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="form-container">
            <form action="teachers" method="post" id="teacherForm" novalidate>
                <input type="hidden" name="action" value="${not empty teacher ? 'update' : 'insert'}" />
                <c:if test="${not empty teacher}">
                    <input type="hidden" name="teacherId" value="${teacher.id}" />
                    <input type="hidden" name="accountId" value="${teacher.accountId}" />
                </c:if>

                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="fullName" class="form-label">Họ tên <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="fullName" name="fullName"
                                   value="${not empty teacher ? teacher.account.fullName : ''}" required>
                            <div class="invalid-feedback">
                                Vui lòng nhập họ tên.
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="${not empty teacher ? teacher.account.email : ''}"
                            ${not empty teacher ? 'readonly' : ''} required>
                            <c:if test="${not empty teacher}">
                                <div class="form-text">Email không thể thay đổi sau khi tạo tài khoản.</div>
                            </c:if>
                            <div class="invalid-feedback">
                                Vui lòng nhập email hợp lệ.
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="phoneNumber" class="form-label">Số điện thoại <span class="text-danger">*</span></label>
                            <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber"
                                   value="${not empty teacher ? teacher.account.phoneNumber : ''}" required>
                            <div class="invalid-feedback">
                                Vui lòng nhập số điện thoại.
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="department" class="form-label">Khoa <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="department" name="department"
                                   value="${not empty teacher ? teacher.department : ''}" required>
                            <div class="invalid-feedback">
                                Vui lòng nhập tên khoa.
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="salary" class="form-label">Đơn giá/slot (VNĐ) <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" id="salary" name="salary"
                                   value="<fmt:formatNumber value='${not empty teacher ? teacher.salary : 0}' groupingUsed='false' pattern='#'/>"
                                   step="1" required min="1001" placeholder="Nhập đơn giá/slot (tối thiểu 1.001 VNĐ)">
                            <div class="invalid-feedback" id="salaryError">
                                Đơn giá/slot phải lớn hơn 1.000 VNĐ.
                            </div>
                            <div class="form-text">Đơn giá/slot tối thiểu là 1.001 VNĐ</div>
                        </div>

                        <div class="mb-3">
                            <label for="address" class="form-label">Địa chỉ</label>
                            <textarea class="form-control" id="address" name="address" rows="3"
                                      placeholder="Nhập địa chỉ">${not empty teacher ? teacher.account.address : ''}</textarea>
                        </div>
                    </div>
                </div>

                <c:if test="${empty teacher}">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i>
                        <strong>Lưu ý:</strong> Mật khẩu mặc định cho tài khoản mới là <strong>123456</strong>.
                        Giáo viên có thể thay đổi mật khẩu sau khi đăng nhập.
                    </div>
                </c:if>

                <div class="d-flex justify-content-end gap-2">
                    <a href="teachers" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Hủy
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i>
                        <c:choose>
                            <c:when test="${not empty teacher}">Cập nhật</c:when>
                            <c:otherwise>Thêm giáo viên</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Form validation
    (function() {
        'use strict';
        window.addEventListener('load', function() {
            var form = document.getElementById('teacherForm');
            var salaryInput = document.getElementById('salary');
            var salaryError = document.getElementById('salaryError');

            // Custom salary validation
            function validateSalary() {
                var salaryValue = parseFloat(salaryInput.value);

                // Only validate if user has entered something
                if (salaryInput.value.trim() === '') {
                    salaryInput.setCustomValidity('');
                    return true; // Let the required attribute handle empty validation
                }

                if (isNaN(salaryValue) || salaryValue <= 1000) {
                    salaryInput.setCustomValidity('Đơn giá/slot phải lớn hơn 1.000 VNĐ');
                    salaryError.textContent = 'Đơn giá/slot phải lớn hơn 1.000 VNĐ.';
                    return false;
                } else {
                    salaryInput.setCustomValidity('');
                    return true;
                }
            }

            // Only validate salary on input if form has been submitted before
            salaryInput.addEventListener('input', function() {
                if (form.classList.contains('was-validated')) {
                    validateSalary();
                }
            });

            // Validate salary on blur only if there's a value
            salaryInput.addEventListener('blur', function() {
                if (salaryInput.value.trim() !== '' && form.classList.contains('was-validated')) {
                    validateSalary();
                }
            });

            form.addEventListener('submit', function(event) {
                var isValid = true;

                // Validate salary before submission
                if (!validateSalary()) {
                    isValid = false;
                }

                // Check form validity
                if (!form.checkValidity() || !isValid) {
                    event.preventDefault();
                    event.stopPropagation();
                }

                // Only add was-validated class after first submit attempt
                form.classList.add('was-validated');
            }, false);
        }, false);
    })();
</script>
</body>
</html>