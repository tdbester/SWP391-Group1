<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 5/29/2025
  Time: 3:58 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <title>Scholar - Contact Us</title>
    <link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../assets/css/templatemo-scholar.css">
    <link rel="stylesheet" href="../assets/css/fontawesome.css">
    <link rel="stylesheet" href="../assets/css/owl.css">
    <link rel="stylesheet" href="../assets/css/animate.css">
    <link rel="stylesheet" href="https://unpkg.com/swiper@7/swiper-bundle.min.css"/>
    <style>
        .contact-us-content select {
            width: 100%;
            height: 50px;
            border-radius: 25px;
            background-color: rgba(249, 235, 255, 0.15);
            border: none;
            outline: none;
            font-weight: 300;
            padding: 0px 20px;
            font-size: 14px;
            color: #fff;
            margin-bottom: 30px;
            -webkit-appearance: none;
            -moz-appearance: none;
            appearance: none;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='10' height='6' viewBox='0 0 10 6'%3E%3Cpath d='M0 0h10L5 6z' fill='%23fff'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 20px center;
            position: relative;
            z-index: 3;
        }
        .contact-us-content select:focus {
            outline: none;
            border: none;
        }
        .contact-us-content select option {
            background-color: #7a6ad8;
            color: #fff;
        }
        .contact-us-content select option[disabled] {
            color: #fff;
        }
        .contact-us-content select option:checked {
            background-color: rgba(249, 235, 255, 0.15);
        }
    </style>
</head>
<body>
<div class="contact-us section" id="contact">
    <div class="container">
        <div class="row">
            <div class="col-lg-6 align-self-center">
                <div class="section-heading">
                    <h6>Contact Us</h6>
                    <h2>Feel free to contact us anytime</h2>
                    <p>Thank you for choosing our templates. We provide you best CSS templates at absolutely 100% free of charge. You may support us by sharing our website to your friends.</p>
                    <div class="special-offer">
                        <span class="offer">off<br><em>50%</em></span>
                        <h6>Valide: <em>24 April 2036</em></h6>
                        <h4>Special Offer <em>50%</em> OFF!</h4>
                        <a href="#"><i class="fa fa-angle-right"></i></a>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="contact-us-content">
                    <c:if test="${not empty message}">
                        <div class="alert alert-info">${message}</div>
                    </c:if>
                    <form id="contact-form" action="Consultation" method="get">
                        <div class="row">
                            <div class="col-lg-12">
                                <fieldset>
                                    <input type="text" name="name" id="name" placeholder="Họ và Tên" autocomplete="on" required>
                                </fieldset>
                            </div>
                            <div class="col-lg-12">
                                <fieldset>
                                    <input type="text" name="email" id="email" pattern="[^ @]*@[^ @]*" placeholder="Email" required>
                                </fieldset>
                            </div>
                            <div class="col-lg-12">
                                <fieldset>
                                    <input type="number" name="phone" id="phone" placeholder="Số điện thoại" autocomplete="on" required>
                                </fieldset>
                            </div>
                            <div class="col-lg-12">
                                <fieldset>
                                    <select name="course_interest" id="course_interest" class="form-control" required>
                                        <option value="">Khoá học muốn tư vấn</option>
                                        <c:forEach var="subject" items="${subjects}">
                                            <option value="${subject.id}">${subject.name}</option>
                                        </c:forEach>
                                    </select>
                                </fieldset>
                            </div>
                            <div class="col-lg-12">
                                <fieldset>
                                    <button type="submit" id="form-submit" class="orange-button">Send Message Now</button>
                                </fieldset>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="../vendor/jquery/jquery.min.js"></script>
<script src="../vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="../assets/js/isotope.min.js"></script>
<script src="../assets/js/owl-carousel.js"></script>
<script src="../assets/js/counter.js"></script>
<script src="../assets/js/custom.js"></script>
</body>
</html>
