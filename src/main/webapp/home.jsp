<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <title>Scholar - Online School HTML5 Template</title>

    <!-- Bootstrap core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">


    <!-- Additional CSS Files -->
    <link rel="stylesheet" href="assets/css/fontawesome.css">
    <link rel="stylesheet" href="assets/css/templatemo-scholar.css">
    <link rel="stylesheet" href="assets/css/owl.css">
    <link rel="stylesheet" href="assets/css/animate.css">
    <link rel="stylesheet"href="https://unpkg.com/swiper@7/swiper-bundle.min.css"/>
    <!--

    TemplateMo 586 Scholar

    https://templatemo.com/tm-586-scholar

    -->
</head>

<body>

<!-- ***** Preloader Start ***** -->
<div id="js-preloader" class="js-preloader">
    <div class="preloader-inner">
        <span class="dot"></span>
        <div class="dots">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </div>
</div>
<!-- ***** Preloader End ***** -->

<!------------------------------------------------------------------- ***** Header Area Start *****------------------------------------------------------------------------->
<header class="header-area header-sticky">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <nav class="main-nav">
                    <!-- ***** Logo Start ***** -->
                    <a href="index.html" class="logo">
                        <h1>Talent01</h1>
                    </a>
                    <!-- ***** Logo End ***** -->
                    <!-- ***** Serach Start ***** -->
                    <div class="search-input">
                        <form id="search" action="#">
                            <input type="text" placeholder="Tìm kiếm" id='searchText' name="searchKeyword" onkeypress="handle" />
                            <i class="fa fa-search"></i>
                        </form>
                    </div>
                    <!-- ***** Serach Start ***** -->
                    <!-- ***** Menu Start ***** -->
                    <ul class="nav">
                        <li class="scroll-to-section"><a href="#top" class="active">Trang chủ</a></li>
                        <li class="scroll-to-section"><a href="#services">Dịch vụ</a></li>
                        <li class="scroll-to-section"><a href="#courses">Khóa học</a></li>
                        <li class="scroll-to-section"><a href="#events">Sự kiện</a></li>
                        <li class="scroll-to-section"><a href="login.jsp">Đăng nhập</a></li>
                        <li class="scroll-to-section"><a href="#contact">Đăng kí</a></li>
                    </ul>
                    <a class='menu-trigger'>
                        <span>Menu</span>
                    </a>
                    <!-- ***** Menu End ***** -->
                </nav>
            </div>
        </div>
    </div>
</header>
<!-------------------------------------------------- ***** banner course ***** -------------------------------------------------------------->

<div class="main-banner" id="top">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="owl-carousel owl-banner">
                    <div class="item item-1">
                        <div class="header-text">
                            <span class="category">Khóa học Piano</span>
                            <h2>Học Piano Dễ Dàng Cùng Giảng Viên Chuyên Nghiệp</h2>
                            <p>Khóa học phù hợp cho cả người mới bắt đầu và đã có nền tảng. Học nhanh, chơi hay với lộ trình cá nhân hóa và hướng dẫn tận tâm từ giảng viên giàu kinh nghiệm.</p>
                            <div class="buttons">
                                <div class="main-button">
                                    <a href="#">Đăng Ký Tư Vấn</a>
                                </div>
                                <div class="icon-button">
                                    <a href="#"><i class="fa fa-play"></i> Tìm Hiểu Khóa Học</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="item item-2">
                        <div class="header-text">
                            <span class="category">Khóa học Violin</span>
                            <h2>Chinh Phục Violin Dễ Dàng Cùng Hướng Dẫn Tận Tâm</h2>
                            <p>Từ những nốt nhạc đầu tiên đến bản nhạc hoàn chỉnh – khóa học phù hợp cho mọi trình độ, với phương pháp học thú vị và giảng viên dày dạn kinh nghiệm.</p>
                            <div class="buttons">
                                <div class="main-button">
                                    <a href="#">Nhận Tư Vấn</a>
                                </div>
                                <div class="icon-button">
                                    <a href="#"><i class="fa fa-play"></i> Khám Phá Khóa Học</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="item item-3">
                        <div class="header-text">
                            <span class="category">Khóa học Guitar</span>
                            <h2>Tự Tin Chơi Guitar Chỉ Sau Vài Buổi Học</h2>
                            <p>Khóa học từ cơ bản đến nâng cao, giúp bạn làm chủ cây đàn nhanh chóng với lộ trình rõ ràng và phong cách giảng dạy dễ hiểu, dễ nhớ.</p>
                            <div class="buttons">
                                <div class="main-button">
                                    <a href="#">Đăng Ký Tư Vấn</a>
                                </div>
                                <div class="icon-button">
                                    <a href="#"><i class="fa fa-play"></i> Tìm Hiểu Ngay</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-------------------------------------------------- *****  service ***** -------------------------------------------------------------->
<div class="services section" id="services">
    <div class="container">
        <div class="row">
            <div class="col-lg-4 col-md-6">
                <div class="service-item">
                    <div class="icon">
                        <img src="assets/images/service-01.png" alt="online degrees">
                    </div>
                    <div class="main-content">
                        <h4>Khóa học</h4>
                        <p>Khám phá các lớp học nhạc chuyên sâu – từ cơ bản đến nâng cao, phù hợp mọi lứa tuổi.</p>
                        <div class="main-button">
                            <a href="#">Read More</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="service-item">
                    <div class="icon">
                        <img src="assets/images/service-02.png" alt="short courses">
                    </div>
                    <div class="main-content">
                        <h4>Sự kiện</h4>
                        <p>Tham gia buổi biểu diễn, workshop và giao lưu âm nhạc định kỳ cùng giảng viên & học viên.</p>
                        <div class="main-button">
                            <a href="#">Read More</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="service-item">
                    <div class="icon">
                        <img src="assets/images/service-03.png" alt="web experts">
                    </div>
                    <div class="main-content">
                        <h4>Blog</h4>
                        <p>Chia sẻ kiến thức âm nhạc, mẹo luyện tập và cảm hứng học nhạc mỗi ngày từ chuyên gia.</p>
                        <div class="main-button">
                            <a href="#">Read More</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-------------------------------------------------- ***** about us ***** -------------------------------------------------------------->
<div class="section about-us">
    <div class="container">
        <div class="row">
            <div class="col-lg-6 offset-lg-1">
                <div class="accordion" id="accordionExample">
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingOne">
                            <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                Chúng ta bắt đầu từ đâu?
                            </button>
                        </h2>
                        <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
                            <div class="accordion-body">
                                Trước tiên, <strong>bạn chỉ cần để lại thông tin liên hệ</strong>. Đội ngũ tư vấn sẽ chủ động gọi điện để tìm hiểu nhu cầu, định hướng mục tiêu học tập và gợi ý khóa học phù hợp nhất cho bạn.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingTwo">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                Chúng ta sẽ đồng hành như thế nào?
                            </button>
                        </h2>
                        <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#accordionExample">
                            <div class="accordion-body">
                                Ngay sau khi đăng ký khóa học, bạn sẽ được cấp tài khoản học viên để quản lý lịch học, điểm danh và theo dõi tiến độ. Trung tâm luôn đồng hành và hỗ trợ bạn trong suốt quá trình học tập.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingThree">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                                Trung tâm có dạy online không?
                            </button>
                        </h2>
                        <div id="collapseThree" class="accordion-collapse collapse" aria-labelledby="headingThree" data-bs-parent="#accordionExample">
                            <div class="accordion-body">
                                Hiện tại, các lớp học tại trung tâm được tổ chức <strong>hoàn toàn trực tiếp (offline)</strong>, giúp học viên dễ dàng trao đổi với giảng viên, thực hành nhóm và nâng cao hiệu quả học tập thực tế.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingFour">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                                Nếu sau này tôi có thắc mắc thì sao?
                            </button>
                        </h2>
                        <div id="collapseFour" class="accordion-collapse collapse" aria-labelledby="headingFour" data-bs-parent="#accordionExample">
                            <div class="accordion-body">
                                Bạn có thể liên hệ với trung tâm bất cứ khi nào qua điện thoại, email hoặc đến trực tiếp. Bộ phận hỗ trợ học viên luôn sẵn sàng giải đáp mọi thắc mắc và đồng hành cùng bạn.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-5 align-self-center">
                <div class="section-heading">
                    <h6>Về chúng tôi</h6>
                    <h2>Tại sao chọn chúng tôi là trung tâm đào tạo đáng tin cậy?</h2>
                    <p>Chúng tôi là trung tâm đào tạo uy tín, chuyên cung cấp các khóa học offline chất lượng cao với đội ngũ giảng viên nhiều năm kinh nghiệm. Học viên được tư vấn lộ trình học tập rõ ràng, học thử miễn phí, và hỗ trợ cá nhân trong suốt quá trình học.
                        Môi trường học tập hiện đại, phương pháp giảng dạy thực tế và cam kết đầu ra rõ ràng là những giá trị cốt lõi giúp học viên phát triển toàn diện.</p>
                    <div class="main-button">
                        <a href="#">Khám phá thêm</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-------------------------------------------------- ***** Hot course ***** -------------------------------------------------------------->
<section class="section courses" id="courses" >
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <div class="section-heading">
                    <h6>Hottest Courses</h6>
                    <h2>Hottest Courses</h2>
                </div>
            </div>
        </div>
        <ul class="event_filter">
            <li>
                <a class="is_active" href="#!" data-filter="*">Tất cả</a>
            </li>
            <li>
                <a href="#!" data-filter=".design">Piano</a>
            </li>
            <li>
                <a href="#!" data-filter=".development">Violin</a>
            </li>
            <li>
                <a href="#!" data-filter=".wordpress">Guitar</a>
            </li>
        </ul>
        <div class="row event_box">
            <div class="col-lg-4 col-md-6 align-self-center mb-30 event_outer col-md-6 design">
                <div class="events_item">
                    <div class="thumb">
                        <a href="#"><img src="assets/images/course-01.jpg" alt=""></a>
                        <span class="category">Piano</span>
                        <span class="price"><h6><em>$</em>160</h6></span>
                    </div>
                    <div class="down-content">
                        <span class="author">Stella Blair</span>
                        <h4>Piano cơ bản</h4>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6 align-self-center mb-30 event_outer col-md-6  development">
                <div class="events_item">
                    <div class="thumb">
                        <a href="#"><img src="assets/images/course-02.jpg" alt=""></a>
                        <span class="category">Piano</span>
                        <span class="price"><h6><em>$</em>340</h6></span>
                    </div>
                    <div class="down-content">
                        <span class="author">Cindy Walker</span>
                        <h4>Piano nâng cao</h4>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6 align-self-center mb-30 event_outer col-md-6 design wordpress">
                <div class="events_item">
                    <div class="thumb">
                        <a href="#"><img src="assets/images/course-03.jpg" alt=""></a>
                        <span class="category">Violin</span>
                        <span class="price"><h6><em>$</em>640</h6></span>
                    </div>
                    <div class="down-content">
                        <span class="author">David Hutson</span>
                        <h4>Violin nâng cao</h4>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6 align-self-center mb-30 event_outer col-md-6 development">
                <div class="events_item">
                    <div class="thumb">
                        <a href="#"><img src="assets/images/course-04.jpg" alt=""></a>
                        <span class="category">Violin</span>
                        <span class="price"><h6><em>$</em>450</h6></span>
                    </div>
                    <div class="down-content">
                        <span class="author">Stella Blair</span>
                        <h4>Violin cơ bản</h4>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6 align-self-center mb-30 event_outer col-md-6 wordpress development">
                <div class="events_item">
                    <div class="thumb">
                        <a href="#"><img src="assets/images/course-05.jpg" alt=""></a>
                        <span class="category">Guitar</span>
                        <span class="price"><h6><em>$</em>320</h6></span>
                    </div>
                    <div class="down-content">
                        <span class="author">Sophia Rose</span>
                        <h4>Guitar nâng cao</h4>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6 align-self-center mb-30 event_outer col-md-6 wordpress design">
                <div class="events_item">
                    <div class="thumb">
                        <a href="#"><img src="assets/images/course-06.jpg" alt=""></a>
                        <span class="category">Guitar</span>
                        <span class="price"><h6><em>$</em>240</h6></span>
                    </div>
                    <div class="down-content">
                        <span class="author">David Hutson</span>
                        <h4>Guitar cơ bản</h4>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-------------------------------------------------- ***** facts ***** -------------------------------------------------------------->
<div class="section fun-facts">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="wrapper">
                    <div class="row">
                        <div class="col-lg-3 col-md-6">
                            <div class="counter">
                                <h2 class="timer count-title count-number" data-to="100" data-speed="1000"></h2>
                                <p class="count-text ">Học viên</p>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="counter">
                                <h2 class="timer count-title count-number" data-to="6" data-speed="1000"></h2>
                                <p class="count-text ">Khóa học</p>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="counter">
                                <h2 class="timer count-title count-number" data-to="20" data-speed="1000"></h2>
                                <p class="count-text ">Giảng viên</p>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="counter end">
                                <h2 class="timer count-title count-number" data-to="15" data-speed="1000"></h2>
                                <p class="count-text ">Năm kinh nghiệm</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-------------------------------------------------- ***** Feedback ***** -------------------------------------------------------------->
<div class="section testimonials">
    <div class="container">
        <div class="row">
            <div class="col-lg-7">
                <div class="owl-carousel owl-testimonials">
                    <div class="item">
                        <p>“Tôi thực sự ấn tượng với cách giảng dạy tại TALENT01 – vừa tận tâm, vừa bài bản. Nhờ chương trình học cá nhân hóa, tôi đã tiến bộ rõ rệt chỉ sau vài tuần luyện tập.”</p>
                        <div class="author">
                            <img src="assets/images/testimonial-author.jpg" alt="">
                            <span class="category">Piano cơ bản</span>
                            <h4>Claude David</h4>
                        </div>
                    </div>
                    <div class="item">
                        <p>“Sau khi thi xong bằng ABRSM Grade 5, mình muốn luyện thi lên Grade 8 và cần người hướng dẫn chuyên sâu. TALENT01 có giảng viên từng du học và thi quốc tế, nên lộ trình học rất bài bản và mình tiến bộ thấy rõ từng tuần.”</p>
                        <div class="author">
                            <img src="assets/images/testimonial-author.jpg" alt="">
                            <span class="category">Violin nâng cao</span>
                            <h4>Thomas Jefferson</h4>
                        </div>
                    </div>
                    <div class="item">
                        <p>“Mình từng học guitar qua mạng nhưng chơi vẫn rất vụng. Nhờ thầy cô tại TALENT01 chỉnh tay, sửa thế bấm, hướng dẫn luyện ngón chi tiết nên giờ mình có thể chơi fingerstyle phức tạp và biểu diễn tự tin hơn rất nhiều!”</p>
                        <div class="author">
                            <img src="assets/images/testimonial-author.jpg" alt="">
                            <span class="category">Guitar nâng cao</span>
                            <h4>Stella Blair</h4>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-5 align-self-center">
                <div class="section-heading">
                    <h6>CẢM NHẬN HỌC VIÊN</h6>
                    <h2>Họ Nói Gì Về TALENT01?</h2>
                    <p>Học viên của chúng tôi đến từ nhiều độ tuổi và trình độ khác nhau – tất cả đều tìm thấy niềm vui và tiến bộ rõ rệt sau từng buổi học. TALENT01 tự hào đồng hành cùng hành trình âm nhạc của bạn.</p>
                </div>
            </div>
        </div>
    </div>
</div>
<!-------------------------------------------------- ***** Upcoming Events ***** -------------------------------------------------------------->
<div class="section events" id="events">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <div class="section-heading">
                    <h6>Lịch khai giảng</h6>
                    <h2>Các khóa học sắp tới</h2>
                </div>
            </div>
            <div class="col-lg-12 col-md-6">
                <div class="item">
                    <div class="row">
                        <div class="col-lg-3">
                            <div class="image">
                                <img src="assets/images/event-01.jpg" alt="">
                            </div>
                        </div>
                        <div class="col-lg-9">
                            <ul>
                                <li>
                                    <span class="category">Piano</span>
                                    <h4>Piano cơ bản</h4>
                                </li>
                                <li>
                                    <span>Khai giảng</span>
                                    <h6>5/6/2025</h6>
                                </li>
                                <li>
                                    <span>Thời gian</span>
                                    <h6>30 buổi</h6>
                                </li>
                                <li>
                                    <span>Price:</span>
                                    <h6>$120</h6>
                                </li>
                            </ul>
                            <a href="#"><i class="fa fa-angle-right"></i></a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-12 col-md-6">
                <div class="item">
                    <div class="row">
                        <div class="col-lg-3">
                            <div class="image">
                                <img src="assets/images/event-02.jpg" alt="">
                            </div>
                        </div>
                        <div class="col-lg-9">
                            <ul>
                                <li>
                                    <span class="category">Piano</span>
                                    <h4>Piano nâng cao</h4>
                                </li>
                                <li>
                                    <span>Khai giảng</span>
                                    <h6>30/5/2025</h6>
                                </li>
                                <li>
                                    <span>Thời gian</span>
                                    <h6>60 buổi</h6>
                                </li>
                                <li>
                                    <span>Price:</span>
                                    <h6>$320</h6>
                                </li>
                            </ul>
                            <a href="#"><i class="fa fa-angle-right"></i></a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-12 col-md-6">
                <div class="item">
                    <div class="row">
                        <div class="col-lg-3">
                            <div class="image">
                                <img src="assets/images/event-03.jpg" alt="">
                            </div>
                        </div>
                        <div class="col-lg-9">
                            <ul>
                                <li>
                                    <span class="category">Violin</span>
                                    <h4>Violin cơ bản</h4>
                                </li>
                                <li>
                                    <span>Khai giảng</span>
                                    <h6>12/6/2025</h6>
                                </li>
                                <li>
                                    <span>Thời gian</span>
                                    <h6>40 buổi</h6>
                                </li>
                                <li>
                                    <span>Price:</span>
                                    <h6>$440</h6>
                                </li>
                            </ul>
                            <a href="#"><i class="fa fa-angle-right"></i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-------------------------------------------------- ***** contact us ***** -------------------------------------------------------------->
<div class="contact-us section" id="contact">
    <div class="container">
        <div class="row">
            <div class="col-lg-6  align-self-center">
                <div class="section-heading">
                    <h6>LIÊN HỆ VỚI CHÚNG TÔI</h6>
                    <h2>Cần hỗ trợ hoặc muốn đăng ký học?</h2>
                    <p>Đội ngũ TALENT01 luôn sẵn sàng hỗ trợ bạn. Hãy điền thông tin bên cạnh, chúng tôi sẽ phản hồi sớm nhất!</p>
                    <div class="special-offer">
                        <span class="offer">off<br><em>50%</em></span>
                        <h6>Valide: <em>30 May 2025</em></h6>
                        <h4>Đăng ký sớm – Nhận ưu đãi học phí lên đến <em>50%</em>!</h4>
                        <a href="#"><i class="fa fa-angle-right"></i></a>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="contact-us-content">
                    <form id="contact-form" action="" method="post">
                        <div class="row">
                            <div class="col-lg-12">
                                <fieldset>
                                    <input type="text" name="name" id="name" placeholder="Họ và tên..." autocomplete="on" required>
                                </fieldset>
                            </div>
                            <div class="col-lg-12">
                                <fieldset>
                                    <input type="text" name="phone" id="phone" pattern="^0\d{9}$" placeholder="Số điện thoại..." required="">
                                </fieldset>
                            </div>
                            <div class="col-lg-12">
                                <fieldset>
                                    <input type="text" name="email" id="email" pattern="[^ @]*@[^ @]*" placeholder="E-mail..." required="">
                                </fieldset>
                            </div>
                            <div class="col-lg-12">
                                <fieldset>
                                    <textarea name="message" id="message" placeholder="Tin nhắn"></textarea>
                                </fieldset>
                            </div>
                            <div class="col-lg-12">
                                <fieldset>
                                    <button type="submit" id="form-submit" class="orange-button">Gửi tư vấn ngay!</button>
                                </fieldset>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!-------------------------------------------------- ***** footer ***** -------------------------------------------------------------->
<footer>
    <div class="container">
        <div class="col-lg-12">
            <p>
                © 2025 TALENT01 Music Center. All rights reserved.
                &nbsp;&nbsp;&nbsp;
                Website by <a href="#">TALENT01 Team</a>
            </p>
        </div>
    </div>
</footer>


<!-- Scripts -->
<!-- Bootstrap core JavaScript -->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="assets/js/isotope.min.js"></script>
<script src="assets/js/owl-carousel.js"></script>
<script src="assets/js/counter.js"></script>
<script src="assets/js/custom.js"></script>

</body>
</html>