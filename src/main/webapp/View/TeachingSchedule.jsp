<%-- 
    Document   : TeachingSchedule
    Created on : May 29, 2025, 10:29:09 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
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
        overflow-x: auto;
    }

    .content-area table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
        font-family: Arial, sans-serif;
        font-size: 16px;
    }

    .content-area th,
    .content-area td {
        padding: 15px;
        text-align: center;
        border: 1px solid #ccc;
    }

    .content-area th {
        background-color: #3498db;
        color: white;
        font-weight: bold;
    }

    .content-area td {
        background-color: #f9f9f9;
    }

    .content-area tr:nth-child(even) td {
        background-color: #eef7fb;
    }

    .content-area tr:hover td {
        background-color: #d6eaf8;
    }

    @media (max-width: 768px) {
        .content-area table {
            font-size: 14px;
        }

        .content-area th,
        .content-area td {
            padding: 10px;
        }
    }
    
    form select, form button {
    padding: 8px 12px;
    border-radius: 5px;
    border: 1px solid #ccc;
}

form label {
    margin-right: 5px;
    font-weight: bold;
}

    
        </style>
    </head>
    <body>
       <jsp:include page="navbar.jsp" />
        <!-- MAIN CONTENT WITH HEADER -->
        <div class="main-content">
            <!-- HEADER BAR -->
            <div class="header">
                <h1>Hệ thống Quản lý Tuyển dụng</h1>
                <div class="user-info">
                    <span>Xin chào, <strong>Administrator</strong></span>
                    <div class="user-avatar">A</div>
                </div>
            </div>

            <!-- Sample content area -->
            <div class="content-area">
                <form action="" method="GET"  style="margin-bottom: 20px; display: flex; justify-content: center; gap: 15px; flex-wrap: wrap;">
                    
                    <div>
                        <label for="day">Ngày:</label>
                        <select name="day" id="day">
                            <option value="">--</option>
                            <% for (int i = 1; i <= 31; i++) { %>
                            <option value="<%= i %>"><%= i %></option>
                            <% } %>
                        </select>
                    </div>

                    <div>
                        <label for="month">Tháng:</label>
                        <select name="month" id="month">
                            <option value="">--</option>
                            <% for (int i = 1; i <= 12; i++) { %>
                            <option value="<%= i %>"><%= i %></option>
                            <% } %>
                        </select>
                    </div>

                    <div>
                        <label for="year">Năm:</label>
                        <select name="year" id="year">
                            <option value="">--</option>
                            <% 
                                int currentYear = java.time.Year.now().getValue();
                                for (int i = currentYear - 5; i <= currentYear + 1; i++) {
                            %>
                            <option value="<%= i %>"><%= i %></option>
                            <% } %>
                        </select>
                    </div>

                    <div>
                        <button type="submit">Lọc</button>
                    </div>
                    
                    
                    <table border = "1">
                        <thead>
                            <tr>
                                <th>Thứ hai</th>
                                <th>Thứ ba</th>
                                <th>Thứ tư</th>
                                <th>Thứ năm</th>
                                <th>Thứ sáu</th>
                                <th>Thứ bảy</th>
                                <th>Chủ nhật</th>

                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Toán</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                            </tr>
                            <tr>
                                <td>Toán</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                            </tr>
                             <tr>
                                <td>Toán</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                            </tr>
                             <tr>
                                <td>Toán</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                                <td>Văn</td>
                            </tr>
                        </tbody>
                    </table>

                </form>
            </div>
        </div>
    </div>

    </body>
</html>
