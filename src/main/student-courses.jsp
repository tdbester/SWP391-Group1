<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Khóa Học - Trung Tâm Năng Khiếu</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet"/>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: monospace;
        }

        body {
            background-color: #fdfdfd;
            background: linear-gradient(180deg, #fff7f0, #e6f0fa);
        }

        .navbar {
            background: linear-gradient(90deg, #e63946, #1d3557);
            padding: 1rem;
            color: white;
            position: fixed;
            width: 100%;
            top: 0;
            z-index: 1000;
            height: 60px;
        }

        .container {
            width: 90%;
            max-width: 1200px;
            margin: 0 auto;
        }

        .nav-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 1.5rem;
            font-weight: bold;
            font-family: 'Georgia', serif;
        }

        .user-name-display {
            font-size: 1rem;
        }

        .side-nav {
            background-color: #ffffff;
            width: 250px;
            position: fixed;
            left: 0;
            top: 60px;
            bottom: 0;
            box-shadow: 2px 0 5px rgba(0,0,0,0.1);
            display: flex;
            flex-direction: column;
        }

        .side-nav-user {
            padding: 1.5rem;
            border-bottom: 1px solid #eee;
            display: flex;
            align-items: center;
            gap: 1rem;
            background: linear-gradient(180deg, #f1c40f10, #ffffff);
        }

        .user-avatar-large {
            width: 48px;
            height: 48px;
            border-radius: 50%;
            background-color: #e63946;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
        }

        .nav-section {
            flex: 1;
            padding: 1rem 0;
        }

        .nav-button {
            display: flex;
            align-items: center;
            padding: 1rem 1.5rem;
            width: 100%;
            border: none;
            background: none;
            color: #1d3557;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 1rem;
        }

        .nav-button:hover {
            background-color: #f1c40f20;
            transform: scale(1.02);
        }

        .nav-button.active {
            color: #e63946;
            background-color: #f1c40f10;
            border-left: 4px solid #f1c40f;
            font-weight: bold;
        }

        .nav-button i {
            margin-right: 10px;
            width: 24px;
            color: #e63946;
        }

        .main-content {
            margin-left: 250px;
            padding: 2.5rem;
            margin-top: 60px;
            background: url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzMCIgaGVpZ2h0PSIzMCI+PHBhdGggZmlsbD0iI2YxYzQwZjMwIiBkPSJNMTAgMTBhMiAyIDAgMTEtNCAwIDIgMiAwIDAxNCAwem0xMCAxMGEyIDIgMCAxMS00IDAgMiAyIDAgMDE0IDB6Ii8+PC9zdmc+') repeat;
        }

        .filter-bar {
            margin-bottom: 2rem;
            display: flex;
            gap: 1rem;
        }

        .filter-button {
            padding: 0.5rem 1rem;
            border: none;
            background-color: #f1c40f;
            color: #1d3557;
            border-radius: 6px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .filter-button:hover {
            background-color: #e63946;
            color: white;
        }

        .filter-button.active {
            background-color: #e63946;
            color: white;
        }

        .course-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
        }

        .card {
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }

        .card:hover {
            transform: translateY(-5px);
        }

        .card-header {
            padding: 1rem;
            border-bottom: 1px solid #f1c40f20;
            font-weight: bold;
            font-family: 'Georgia', serif;
            font-size: 1.2rem;
            color: #1d3557;
            background: linear-gradient(90deg, #f1c40f10, #ffffff);
            border-radius: 12px 12px 0 0;
        }

        .card-header::before {
            content: '\f001';
            font-family: 'Font Awesome 6 Free';
            font-weight: 900;
            margin-right: 8px;
            color: #e63946;
        }

        .card-body {
            padding: 1.5rem;
        }

        .course-item {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .item-title {
            font-weight: bold;
            color: #e63946;
            font-size: 1.1rem;
        }

        .item-description {
            color: #1d3557;
            font-size: 0.9rem;
            line-height: 1.5;
        }

        .item-details p {
            color: #1d3557;
            font-size: 0.9rem;
            margin: 0.3rem 0;
        }

        .tag {
            display: inline-block;
            padding: 0.3rem 0.6rem;
            background-color: #f1c40f;
            color: #1d3557;
            border-radius: 6px;
            font-size: 0.8rem;
            margin-top: 0.5rem;
            transition: background 0.3s ease;
        }

        .tag:hover {
            background-color: #e63946;
            color: white;
        }

        @media (max-width: 768px) {
            .side-nav {
                transform: translateX(-100%);
                transition: transform 0.3s ease;
            }

            .side-nav.active {
                transform: translateX(0);
            }

            .main-content {
                margin-left: 0;
                padding: 1.5rem;
            }

            .course-grid {
                grid-template-columns: 1fr;
            }

            .navbar {
                height: 50px;
            }

            .logo {
                font-size: 1.2rem;
            }

            .filter-bar {
                flex-wrap: wrap;
            }
        }
    </style>
</head>
<body>
<nav class="navbar">
    <div class="container">
        <div class="nav-content">
            <div class="mobile-menu-toggle">
                <i class="fas fa-bars"></i>
            </div>
            <div class="logo">Trung Tâm Năng Khiếu</div>
            <div class="user-name-display">Chào mừng, Nguyễn Văn A</div>
        </div>
    </div>
</nav>

<nav class="side-nav">
    <div class="side-nav-user">
        <div class="user-avatar-large">
            A
        </div>
        <div class="user-info-side">
            <div class="user-name">Nguyễn Văn A</div>
            <div class="user-email">a.nguyen@example.com</div>
        </div>
    </div>

    <div class="nav-section">
        <button class="nav-button">
            <i class="fas fa-th-large"></i>
            Tổng Quan
        </button>
        <button class="nav-button">
            <i class="fas fa-calendar-alt"></i>
            Lịch Học
        </button>
        <button class="nav-button active">
            <i class="fas fa-book"></i>
            Khóa Học
        </button>
        <button class="nav-button">
            <i class="fas fa-blog"></i>
            Blog
        </button>
    </div>

    <div class="nav-section-bottom">
        <button class="nav-button">
            <i class="fas fa-user"></i>
            Hồ Sơ
        </button>
        <button class="nav-button nav-button-logout">
            <i class="fas fa-sign-out-alt"></i>
            Đăng Xuất
        </button>
    </div>
</nav>

<div class="main-content">
    <div class="filter-bar">
        <button class="filter-button active">Tất Cả</button>
        <button class="filter-button">Piano</button>
        <button class="filter-button">Violin</button>
        <button class="filter-button">Thanh Nhạc</button>
        <button class="filter-button">Guitar</button>
    </div>

    <div class="course-grid">
        <div class="card">
            <div class="card-header">Piano Cơ Bản</div>
            <div class="card-body">
                <div class="course-item">
                    <div class="item-title">Piano Cơ Bản</div>
                    <div class="item-description">Khóa học giới thiệu các kỹ thuật cơ bản để chơi piano, phù hợp cho người mới bắt đầu.</div>
                    <div class="item-details">
                        <p>Lịch học: Thứ Hai, Thứ Tư 18:00-19:30</p>
                        <p>Giảng viên: Lê Thị Hồng</p>
                        <p>Phòng: Nhạc Phòng 1</p>
                        <span class="tag">Đang Học</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">Violin Nâng Cao</div>
            <div class="card-body">
                <div class="course-item">
                    <div class="item-title">Violin Nâng Cao</div>
                    <div class="item-description">Khóa học nâng cao kỹ thuật kéo đàn violin, tập trung vào biểu cảm và phong cách.</div>
                    <div class="item-details">
                        <p>Lịch học: Thứ Ba, Thứ Năm 17:00-18:30</p>
                        <p>Giảng viên: Trần Văn Minh</p>
                        <p>Phòng: Nhạc Phòng 3</p>
                        <span class="tag">Đang Học</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">Thanh Nhạc Cơ Bản</div>
            <div class="card-body">
                <div class="course-item">
                    <div class="item-title">Thanh Nhạc Cơ Bản</div>
                    <div class="item-description">Học cách kiểm soát hơi thở và phát âm để hát đúng giai điệu và cảm xúc.</div>
                    <div class="item-details">
                        <p>Lịch học: Thứ Tư, Thứ Sáu 19:00-20:30</p>
                        <p>Giảng viên: Nguyễn Thị Lan</p>
                        <p>Phòng: Phòng Thanh Nhạc 1</p>
                        <span class="tag">Sắp Khai Giảng</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">Guitar Nhập Môn</div>
            <div class="card-body">
                <div class="course-item">
                    <div class="item-title">Guitar Nhập Môn</div>
                    <div class="item-description">Khóa học cơ bản về cách chơi guitar, từ hợp âm đến kỹ thuật gảy đàn.</div>
                    <div class="item-details">
                        <p>Lịch học: Thứ Hai, Thứ Năm 16:00-17:30</p>
                        <p>Giảng viên: Phạm Văn Hùng</p>
                        <p>Phòng: Nhạc Phòng 2</p>
                        <span class="tag">Sắp Khai Giảng</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    document.querySelectorAll('.nav-button').forEach(button => {
        button.addEventListener('click', () => {
            if (button.classList.contains('nav-button-logout')) {
                window.location.href = 'logout';
                return;
            }

            document.querySelectorAll('.nav-button').forEach(btn => {
                btn.classList.remove('active');
            });
            button.classList.add('active');
        });
    });

    document.querySelector('.mobile-menu-toggle')?.addEventListener('click', () => {
        document.querySelector('.side-nav').classList.toggle('active');
    });

    document.querySelectorAll('.filter-button').forEach(button => {
        button.addEventListener('click', () => {
            document.querySelectorAll('.filter-button').forEach(btn => {
                btn.classList.remove('active');
            });
            button.classList.add('active');
        });
    });
</script>
</body>
</html>