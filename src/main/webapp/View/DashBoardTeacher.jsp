<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách công ty</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
    <!-- Kế thừa hoặc nhúng lại CSS từ trang chính -->
    
    <style>
        

          /* HEADER STYLES */
        .main-content {
            flex: 1;
            margin-left: 280px;
            padding: 30px;
            overflow-y: auto;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            background: rgba(255, 255, 255, 0.95);
            padding: 20px 30px;
            border-radius: 15px;
            backdrop-filter: blur(10px);
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }

        .header h1 {
            color: #333;
            font-size: 2rem;
            font-weight: 700;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .user-avatar {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            background: linear-gradient(135deg, #4CAF50, #45a049);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: bold;
        }

        /* RESPONSIVE */
        @media (max-width: 768px) {
            .sidebar {
                width: 100%;
                height: auto;
                position: relative;
            }
            
            .main-content {
                margin-left: 0;
            }
        }

        /* Sample content area */
        .content-area {
            background: rgba(255, 255, 255, 0.95);
            padding: 40px;
            border-radius: 15px;
            backdrop-filter: blur(10px);
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        
       
    
        
        
        /* Stats Cards */
.stats-grid {
    display: flex;
    gap: 20px;
    justify-content: space-between;
    margin-bottom: 30px;
    flex-wrap: wrap;
}

.stat-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    flex: 1;
    min-width: 220px;
    text-align: center;
    box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.stat-icon {
    font-size: 2rem;
    margin-bottom: 10px;
}

.stat-number {
    font-size: 2rem;
    font-weight: bold;
}

.stat-label {
    font-weight: 500;
    color: #555;
    margin-top: 8px;
}

.stat-status {
    margin-top: 5px;
    font-size: 0.9rem;
}

.status-info {
    color: #3498db;
}

.status-warning {
    color: #f39c12;
}

.status-good {
    color: #2ecc71;
}

/* Content Grid */
.content-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 30px;
    margin-top: 30px;
}

.card {
    background: #ffffff;
    padding: 25px;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.05);
}

.card-title {
    font-size: 1.5rem;
    font-weight: bold;
    margin-bottom: 20px;
    color: #2c3e50;
}

/* Schedule Items */
.schedule-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px 0;
    border-bottom: 1px solid #eaeaea;
}

.schedule-time {
    font-weight: bold;
    font-size: 1.2rem;
    color: #2c3e50;
    min-width: 60px;
}

.schedule-info {
    flex: 1;
    margin-left: 20px;
}

.schedule-title {
    font-weight: bold;
    font-size: 1.1rem;
    color: #333;
}

.schedule-details {
    font-size: 0.9rem;
    color: #666;
    margin-top: 4px;
}

.schedule-actions button {
    margin-left: 10px;
    padding: 6px 12px;
    border: none;
    border-radius: 5px;
    font-size: 0.9rem;
    cursor: pointer;
}

.btn-success {
    background-color: #2ecc71;
    color: white;
}

.btn-primary {
    background-color: #3498db;
    color: white;
}

.btn-warning {
    background-color: #f39c12;
    color: white;
}

/* Request Items */
.request-item {
    margin-top: 15px;
    padding: 15px;
    border-radius: 8px;
    background: #f8f9fa;
    box-shadow: 0 2px 6px rgba(0,0,0,0.05);
}

.request-title {
    font-weight: bold;
    margin-bottom: 5px;
}

.request-date {
    color: #666;
    font-size: 0.9rem;
    margin-bottom: 5px;
}

.request-status {
    font-weight: bold;
    padding: 4px 10px;
    border-radius: 20px;
    font-size: 0.8rem;
}

.request-status.pending {
    background-color: #fff3cd;
    color: #856404;
}

.request-status.approved {
    background-color: #d4edda;
    color: #155724;
}

/* Class Items */
.class-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #eee;
}

.class-info {
    display: flex;
    align-items: center;
    gap: 15px;
}

.class-icon {
    font-size: 2rem;
}

.class-details h4 {
    margin: 0;
    font-size: 1.1rem;
    font-weight: bold;
}

.class-details p {
    margin: 4px 0 0;
    color: #777;
    font-size: 0.9rem;
}

.class-actions button {
    margin-left: 10px;
    padding: 6px 12px;
    border: none;
    border-radius: 5px;
    font-size: 0.9rem;
    cursor: pointer;
}

        
        
        
        
        
    </style>
</head>
<body>
    <jsp:include page="navbar.jsp" />
    
        <!-- SIDEBAR -->
         <!-- Tách sidebar thành file riêng để tái sử dụng -->

       <!-- MAIN CONTENT WITH HEADER -->
        <div class="main-content">
            <!-- HEADER BAR -->
            <div class="header">

                <div class="user-info">
                    <span>Xin chào, <strong>Giao viên</strong></span>
                    <div class="user-avatar">A</div>
                </div>
            </div>

            <!-- Sample content area -->
            <div class="content-area">
                 <!-- Stats Cards -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">👥</div>
                <div class="stat-number" style="color: #3498db;">5</div>
                <div class="stat-label">Lớp cần điểm danh</div>
                <div class="stat-status status-info">Còn 2 tiết chưa điểm danh</div>
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