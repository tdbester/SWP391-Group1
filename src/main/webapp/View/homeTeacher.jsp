<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 5/29/2025
  Time: 3:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Trung Tâm Năng Khiếu</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Arial', sans-serif;
            background-color: #f8f9fa;
            color: #333;
            line-height: 1.5;
        }

        .container {
            display: flex;
            min-height: 100vh;
        }

        /* Sidebar */
        .sidebar {
            width: 250px;
            background: #2c3e50;
            color: white;
            padding: 20px 0;
            position: fixed;
            height: 100vh;
            overflow-y: auto;
        }

        .logo {
            text-align: center;
            padding: 0 20px 30px;
            border-bottom: 1px solid #34495e;
            margin-bottom: 30px;
        }

        .logo h1 {
            font-size: 20px;
            font-weight: bold;
            color: #3498db;
        }

        .menu-item {
            display: block;
            padding: 15px 20px;
            color: #bdc3c7;
            text-decoration: none;
            transition: all 0.3s;
            border-left: 3px solid transparent;
        }

        .menu-item:hover,
        .menu-item.active {
            background: #34495e;
            color: white;
            border-left-color: #3498db;
        }

        .menu-item i {
            margin-right: 10px;
            width: 20px;
        }

        /* Main Content */
        .main-content {
            flex: 1;
            margin-left: 250px;
            padding: 30px;
        }

        .header {
            margin-bottom: 30px;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        .page-title {
            font-size: 28px;
            color: #2c3e50;
            margin-bottom: 5px;
        }

        .breadcrumb {
            color: #7f8c8d;
            font-size: 14px;
        }

        /* Stats Cards */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.3s;
        }

        .stat-card:hover {
            transform: translateY(-3px);
        }

        .stat-icon {
            font-size: 40px;
            margin-bottom: 15px;
        }

        .stat-number {
            font-size: 36px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .stat-label {
            color: #7f8c8d;
            font-size: 14px;
            margin-bottom: 10px;
        }

        .stat-status {
            font-size: 12px;
            padding: 5px 10px;
            border-radius: 15px;
            font-weight: bold;
        }

        .status-good {
            background: #d4edda;
            color: #155724;
        }

        .status-warning {
            background: #fff3cd;
            color: #856404;
        }

        .status-info {
            background: #cce5ff;
            color: #0066cc;
        }

        /* Content Grid */
        .content-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 30px;
        }

        .card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            height: fit-content;
        }

        .card-title {
            font-size: 20px;
            color: #2c3e50;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #ecf0f1;
        }

        /* Schedule Items */
        .schedule-item {
            display: flex;
            align-items: center;
            padding: 20px;
            margin-bottom: 15px;
            background: #f8f9fa;
            border-radius: 8px;
            border-left: 4px solid #3498db;
        }

        .schedule-time {
            font-size: 18px;
            font-weight: bold;
            color: #3498db;
            width: 80px;
        }

        .schedule-info {
            flex: 1;
            margin-left: 20px;
        }

        .schedule-title {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .schedule-details {
            color: #7f8c8d;
            font-size: 14px;
        }

        .schedule-actions {
            display: flex;
            gap: 10px;
        }

        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            font-size: 12px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
        }

        .btn-primary {
            background: #3498db;
            color: white;
        }

        .btn-success {
            background: #27ae60;
            color: white;
        }

        .btn-warning {
            background: #f39c12;
            color: white;
        }

        .btn:hover {
            opacity: 0.8;
            transform: translateY(-1px);
        }

        /* Class List */
        .class-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 15px;
            margin-bottom: 15px;
            background: #f8f9fa;
            border-radius: 8px;
            transition: background 0.3s;
        }

        .class-item:hover {
            background: #e9ecef;
        }

        .class-info {
            display: flex;
            align-items: center;
        }

        .class-icon {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            color: white;
            margin-right: 15px;
        }

        .class-icon.music {
            background: #9b59b6;
        }

        .class-icon.art {
            background: #e91e63;
        }

        .class-icon.dance {
            background: #00bcd4;
        }

        .class-details h4 {
            margin-bottom: 5px;
            color: #2c3e50;
        }

        .class-details p {
            color: #7f8c8d;
            font-size: 14px;
        }

        .class-actions {
            display: flex;
            gap: 8px;
        }

        /* Requests */
        .request-item {
            padding: 15px;
            margin-bottom: 15px;
            border-radius: 8px;
            border-left: 4px solid;
        }

        .request-item.pending {
            background: #fff8e1;
            border-left-color: #ffc107;
        }

        .request-item.approved {
            background: #e8f5e8;
            border-left-color: #28a745;
        }

        .request-item.rejected {
            background: #ffeaea;
            border-left-color: #dc3545;
        }

        .request-title {
            font-weight: bold;
            margin-bottom: 5px;
        }

        .request-date {
            color: #7f8c8d;
            font-size: 14px;
            margin-bottom: 10px;
        }

        .request-status {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: bold;
        }

        .request-status.pending {
            background: #ffc107;
            color: white;
        }

        .request-status.approved {
            background: #28a745;
            color: white;
        }

        .request-status.rejected {
            background: #dc3545;
            color: white;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .sidebar {
                width: 200px;
            }

            .main-content {
                margin-left: 200px;
                padding: 20px;
            }

            .content-grid {
                grid-template-columns: 1fr;
            }

            .stats-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 600px) {
            .sidebar {
                transform: translateX(-100%);
                transition: transform 0.3s;
            }

            .main-content {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="logo">
            <h1>🎭 Trung Tâm Năng Khiếu</h1>
        </div>
        <a href="DashBoardTeacher.jsp" class="menu-item active">
            📊 Dashboard
        </a>

        <a href="#" class="menu-item">
            📅 Lịch dạy của tôi
        </a>
        <a href="#" class="menu-item">
            👥 Điểm danh
        </a>
        <a href="#" class="menu-item">
            📁 Tài liệu
        </a>
        <a href="#" class="menu-item">
            📝 Đơn xin nghỉ
        </a>
        <a href="#" class="menu-item">
            🔄 Phân công dạy thay
        </a>
        <a href="#" class="menu-item">
            👨‍👩‍👧‍👦 Danh sách lớp
        </a>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Header -->
        <div class="header">
            <h1 class="page-title">Dashboard</h1>
            <div class="breadcrumb">Trang chủ › Dashboard </div>
        </div>

        <!-- Stats Cards -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">👥</div>
                <div class="stat-number" style="color: #3498db;">5</div> --<!-- số lượng lớp mà giáo viên phải dạy -->
                <div class="stat-label">Lớp cần điểm danh</div>
                <div class="stat-status status-info">Còn 2 tiết chưa điểm danh</div>  --<!-- nếu còn lớp nào chưa được save điểm danh sẽ được up load dữ liệu lên -->
            </div>

            <div class="stat-card">
                <div class="stat-icon">📝</div>
                <div class="stat-number" style="color: #f39c12;">3</div>
                <div class="stat-label">Đơn xin nghỉ</div>
                <div class="stat-status status-warning">Chờ phê duyệt</div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">🔄</div>
                <div class="stat-number" style="color: #9b59b6;">2</div>
                <div class="stat-label">Lịch dạy thay hôm nay</div>
                <div class="stat-status status-good">Tuần này</div>
            </div>
        </div>

        <!-- Content Grid -->
        <div class="content-grid">
            <!-- Schedule -->
            <div class="card">
                <h2 class="card-title">Lịch dạy hôm nay</h2>

                <div class="schedule-item">
                    <div class="schedule-time">7:30</div>
                    <div class="schedule-info">
                        <div class="schedule-title">Âm nhạc - Lớp Violin A1</div>
                        <div class="schedule-details">Phòng 201 • 15 học sinh • Chưa điểm danh</div>
                    </div>
                    <div class="schedule-actions">
                        <button class="btn btn-success">Điểm danh</button>
                        <button class="btn btn-primary">Tài liệu</button>
                    </div>
                </div>

                <div class="schedule-item">
                    <div class="schedule-time">10:00</div>
                    <div class="schedule-info">
                        <div class="schedule-title">Hội họa - Lớp Vẽ B2</div>
                        <div class="schedule-details">Phòng 305 • 14 học sinh • Đã điểm danh</div>
                    </div>
                    <div class="schedule-actions">
                        <button class="btn btn-success">Hoàn thành</button>
                        <button class="btn btn-primary">Tài liệu</button>
                    </div>
                </div>

                <div class="schedule-item">
                    <div class="schedule-time">12:50</div>
                    <div class="schedule-info">
                        <div class="schedule-title">Khiêu vũ - Lớp Múa A3</div>
                        <div class="schedule-details">Phòng 201 • 18 học sinh • Dạy thay GV Minh</div>
                    </div>
                    <div class="schedule-actions">
                        <button class="btn btn-warning">Dạy thay</button>
                        <button class="btn btn-primary">Tài liệu</button>
                    </div>
                </div>

                <!-- Recent Requests -->
                <h3 style="margin: 30px 0 20px 0; color: #2c3e50;">Đơn xin nghỉ gần đây</h3>

                <div class="request-item pending">
                    <div class="request-title">Xin nghỉ phép cá nhân</div>
                    <div class="request-date">Ngày 02/06/2025 • Lý do: Khám sức khỏe</div>
                    <span class="request-status pending">Chờ duyệt</span>
                </div>

                <div class="request-item approved">
                    <div class="request-title">Xin nghỉ ốm</div>
                    <div class="request-date">Ngày 28/05/2025 • Lý do: Bệnh cảm cúm</div>
                    <span class="request-status approved">Đã duyệt</span>
                </div>
            </div>

            <!-- Classes & Materials -->
            <div class="card">
                <h2 class="card-title">Danh sách lớp</h2>

                <div class="class-item">
                    <div class="class-info">
                        <div class="class-icon music">🎵</div>
                        <div class="class-details">
                            <h4>Lớp Violin A1</h4>
                            <p>15 học sinh • 12 tài liệu</p>
                        </div>
                    </div>
                    <div class="class-actions">
                        <button class="btn btn-primary">Xem lớp</button>
                        <button class="btn btn-success">Tài liệu</button>
                    </div>
                </div>

                <div class="class-item">
                    <div class="class-info">
                        <div class="class-icon art">🎨</div>
                        <div class="class-details">
                            <h4>Lớp Vẽ B2</h4>
                            <p>14 học sinh • 8 tài liệu</p>
                        </div>
                    </div>
                    <div class="class-actions">
                        <button class="btn btn-primary">Xem lớp</button>
                        <button class="btn btn-success">Tài liệu</button>
                    </div>
                </div>

                <div class="class-item">
                    <div class="class-info">
                        <div class="class-icon dance">💃</div>
                        <div class="class-details">
                            <h4>Lớp Múa A3</h4>
                            <p>18 học sinh • 15 tài liệu</p>
                        </div>
                    </div>
                    <div class="class-actions">
                        <button class="btn btn-primary">Xem lớp</button>
                        <button class="btn btn-success">Tài liệu</button>
                    </div>
                </div>

                <!-- Substitute Teaching -->
                <h3 style="margin: 30px 0 20px 0; color: #2c3e50;">Lịch dạy thay</h3>
                <p style="color: #7f8c8d; margin-bottom: 15px;">Tuần này: 2/4 tiết</p>

                <div style="background: #fff3cd; padding: 15px; border-radius: 8px; margin-bottom: 10px;">
                    <div style="font-weight: bold;">Thay GV Minh - Lớp Múa A3</div>
                    <div style="color: #856404; font-size: 14px;">Hôm nay 12:50 • Môn: Khiêu vũ</div>
                </div>

                <div style="background: #cce5ff; padding: 15px; border-radius: 8px;">
                    <div style="font-weight: bold;">Thay GV Lan - Lớp Piano A2</div>
                    <div style="color: #0066cc; font-size: 14px;">Ngày mai 7:30 • Môn: Âm nhạc</div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>