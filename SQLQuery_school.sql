CREATE DATABASE SchoolManagement1;
GO
USE SchoolManagement1;

-- Bảng Role: lưu thông tin vai trò của người dùng trong hệ thống
CREATE TABLE Role (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  Name VARCHAR(100)                        -- Tên vai trò (Admin, Student, Teacher, Accountant)
);

-- Bảng Account: thông tin đăng nhập & cá nhân của người dùng
CREATE TABLE Account (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  FullName NVARCHAR(255),                  -- Họ tên đầy đủ
  Email VARCHAR(255) UNIQUE,               -- Email đăng nhập, duy nhất
  Password VARCHAR(255),
  PhoneNumber VARCHAR(20),                 -- Số điện thoại
  Address NVARCHAR(500),                   -- Địa chỉ
  RoleId INT,                              -- FK tới bảng Role: vai trò người dùng
  FOREIGN KEY (RoleId) REFERENCES Role(Id)
);
-- Bảng lưu token
CREATE TABLE tokenForgetPassword (
    id int IDENTITY(1,1) PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expiryTime DATETIME NOT NULL,
    isUsed bit NOT NULL,
    accountId int NOT NULL,
    FOREIGN KEY (accountId) REFERENCES [Account](Id)
);



-- Bảng Course: danh sách các khóa học
CREATE TABLE Course (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  Title NVARCHAR(255),                     -- Tên khóa học
  Price FLOAT,                             -- Giá tiền
  Information TEXT,                        -- Thông tin mô tả chi tiết
  CreatedBy INT,                           -- FK: người tạo khóa học (AccountId)
  FOREIGN KEY (CreatedBy) REFERENCES Account(Id)
);

-- Bảng Student: thông tin chi tiết học sinh
CREATE TABLE Student (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  parentPhone VARCHAR(20),                 -- SĐT bố
  motherPhone VARCHAR(20),                 -- SĐT mẹ
  AccountId INT UNIQUE,                    -- FK: liên kết 1-1 với tài khoản
  EnrollmentDate DATE,                     -- Ngày nhập học
  FOREIGN KEY (AccountId) REFERENCES Account(Id)
);

-- Bảng Teacher: thông tin giáo viên
CREATE TABLE Teacher (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  AccountId INT UNIQUE,                    -- FK: liên kết 1-1 với tài khoản
  Department NVARCHAR(255),                -- Bộ môn phụ trách
  Salary DECIMAL(12, 2),                   -- Mức Lương 
  FOREIGN KEY (AccountId) REFERENCES Account(Id)
);
-- Bảng Sale: thông tin Sale
CREATE TABLE Sale (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  AccountId INT UNIQUE,                    -- FK: liên kết 1-1 với tài khoản
  Salary DECIMAL(12, 2),                   -- Mức Lương 
  FOREIGN KEY (AccountId) REFERENCES Account(Id)
);

-- Bảng Blog: chứa các bài viết/blog trong hệ thống
CREATE TABLE Blog (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  Title NVARCHAR(255),                     -- Tiêu đề bài viết
  Description NVARCHAR(1000),              -- Mô tả ngắn
  Content TEXT,                            -- Nội dung chi tiết
  AuthorId INT,                            -- FK: người viết, tham chiếu Account(Id)
  CreatedAt DATETIME DEFAULT  GETDATE(), -- Ngày tạo bài viết
  FOREIGN KEY (AuthorId) REFERENCES Sale(Id)
);

-- Bảng Room: danh sách các phòng học
CREATE TABLE Room (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  Code VARCHAR(20)                         -- Mã phòng học
);

--Bang Slot
CREATE TABLE Slot (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  StartTime TIME,                         -- Giờ bắt đầu
  EndTime TIME
);
--Bảng Slot
INSERT INTO Slot (StartTime,EndTime) VALUES
('8:00','9:00'),
('9:00','10:00'),
('10:00','11:00'),
('14:00','15:00'),
('15:00','16:00'),
('16:00','17:00'),
('17:00','18:00'),
('18:00','19:00'),
('19:00','20:00'),
('20:00','21:00');



-- Bảng ClassRooms: thông tin các lớp học
CREATE TABLE ClassRooms (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  Name NVARCHAR(255),                      -- Tên lớp
  CourseId INT,                            -- FK: khóa học liên quan
  TeacherId INT,                           -- FK: giáo viên phụ trách
  FOREIGN KEY (CourseId) REFERENCES Course(Id),
  FOREIGN KEY (TeacherId) REFERENCES Teacher(Id)
);
ALTER TABLE ClassRooms
ADD FOREIGN KEY (SlotId) REFERENCES Slot(Id);


-- Bảng Student_Class: danh sách học sinh thuộc từng lớp học
CREATE TABLE Student_Class (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  ClassRoomId INT,                         -- FK: lớp học
  StudentId INT,                           -- FK: học sinh
  JoinDate DATE,                           -- Ngày vào lớp
  FOREIGN KEY (StudentId) REFERENCES Student(Id),
  FOREIGN KEY (ClassRoomId) REFERENCES ClassRooms(Id)
);
--khi tạo lịch học tạo schedule trong 1 tuần
--cần nhập các schedule (các slot bao gồm thông tin giờ bắt đầu giờ kết thúc buổi học) 
--b1: chọn số slot trong 1 tuần
--b2: vd (3slot) điền thông tin 3 slot (start time, day)
--b3: chọn số tuần lặp lại (all, number) => tự động lặp (tính toán dựa trên ngày bắt đầu và kết thúc)
--b4: hệ thống tự tạo ra số schedule từ thông tin trên
-- Bảng Schedule: thời khóa biểu các lớp học
CREATE TABLE Schedule (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  Date DATE,                               -- Ngày học
  RoomId INT,                              -- FK: phòng học
  ClassRoomId INT,                         -- FK: lớp học
  FOREIGN KEY (RoomId) REFERENCES Room(Id),
  FOREIGN KEY (ClassRoomId) REFERENCES ClassRooms(Id)
);
ALTER TABLE Schedule DROP COLUMN StartTime,EndTime;
ALTER TABLE Schedule
ADD SlotId INT;
ALTER TABLE Schedule
ADD FOREIGN KEY (SlotId) REFERENCES Slot(Id);




-- Bảng Attendance: điểm danh học sinh theo buổi học
CREATE TABLE Attendance (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  ScheduleId INT,                          -- FK: buổi học
  StudentId INT,                           -- FK: học sinh
  Status VARCHAR(20),                      -- Trạng thái: Present, Absent
  Note NVARCHAR(255),                      -- Ghi chú (nếu có)
  FOREIGN KEY (ScheduleId) REFERENCES Schedule(Id),
  FOREIGN KEY (StudentId) REFERENCES Student(Id)
);


-- Bảng Request: xử lý các yêu cầu trong hệ thống
CREATE TABLE Request (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  Type VARCHAR(100),                       -- Loại yêu cầu: Chuyển lớp, Thưởng/phạt, v.v.
  SenderId INT,                            -- FK: người gửi yêu cầu (AccountId)
  Reason NVARCHAR(1000),                   -- Lý do gửi yêu cầu
  Status VARCHAR(50),                      -- Trạng thái: Pending, Approved, Rejected
  Response NVARCHAR(1000),                 -- Phản hồi từ người xử lý
  CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo yêu cầu
  ResponseAt DATETIME DEFAULT NULL,        -- Ngày phản hồi
  ProcessedBy INT DEFAULT NULL,            -- FK: người xử lý yêu cầu (AccountId)
  FOREIGN KEY (SenderId) REFERENCES Account(Id),
  FOREIGN KEY (ProcessedBy) REFERENCES Account(Id)
);
-- Bảng Request Tư vấn: xử lý các yêu cầu tư vấn từ guest
CREATE TABLE Consultations (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  FullName NVARCHAR(50),                   -- người gửi yêu cầu
  Email VARCHAR(50),                       -- Email
  Status VARCHAR(50),                      -- Trạng thái: 
  CourseId INT,                            -- Course
  CreatedAt DATETIME DEFAULT GETDATE(),    -- Ngày tạo yêu cầu
  ResponseAt DATETIME DEFAULT NULL,        -- Ngày phản hồi
  ProcessedBy INT DEFAULT NULL,            -- FK: người xử lý yêu cầu (AccountId)
  FOREIGN KEY (CourseId) REFERENCES Course(Id),
  FOREIGN KEY (ProcessedBy) REFERENCES Sale(Id)
);

-- Bảng Accountant: kế toán viên (liên kết 1-1 với Account)
CREATE TABLE Accountant (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  HireDate DATE,                           -- Ngày bắt đầu làm việc
  OutDate Date default null,               -- Ngày rời việc
  Note NVARCHAR(255),
  AccountId INT UNIQUE,                    -- FK: tài khoản kế toán
  Salary DECIMAL(12, 2),                   -- Mức Lương 
  FOREIGN KEY (AccountId) REFERENCES Account(Id)
);

-- Bảng Invoice: hóa đơn thanh toán của học sinh
CREATE TABLE Invoice (
  Id INT PRIMARY KEY IDENTITY(1,1),        -- Khóa chính, tự tăng
  StudentId INT NOT NULL,                  -- FK: học sinh thanh toán
  Amount FLOAT NOT NULL CHECK (Amount >= 0), -- Số tiền, không âm
  ExportAt DATETIME DEFAULT GETDATE(),     -- Ngày xuất hóa đơn
  ExportBy INT NOT NULL,                   -- FK: kế toán xuất hóa đơn
  PaidAt DATETIME NULL,                    -- Ngày thanh toán (có thể null)
  Note NVARCHAR(500),                      -- Ghi chú thêm
  FOREIGN KEY (StudentId) REFERENCES Student(Id),
  FOREIGN KEY (ExportBy) REFERENCES Accountant(Id)
);

CREATE TABLE SalaryPaymentHistory (
    Id INT PRIMARY KEY IDENTITY,
    AccountId INT NOT NULL,           -- Nhân viên được trả
    Amount DECIMAL(12, 2) NOT NULL,   -- Số tiền (dương hoặc âm nếu là phạt)
    PaymentDate DATETIME DEFAULT GETDATE(),
    Reason NVARCHAR(255),             -- Ví dụ: Lương tháng 6, Thưởng xuất sắc, Phạt đi trễ
    ProcessedBy INT,                  -- Người (Admin) đã xác nhận chi trả
    FOREIGN KEY (AccountId) REFERENCES Account(Id),
    FOREIGN KEY (ProcessedBy) REFERENCES Account(Id)
);



-- 1. Bảng Role
INSERT INTO Role (Name) VALUES
('Admin'),
('Student'),
('Teacher'),
('Accountant'),
('Sale');

-- 2. Bảng Account
INSERT INTO Account (FullName, Email, Password, PhoneNumber, Address, RoleId) VALUES
(N'Nguyễn Văn A', 'nguyenvana@example.com', '1234','0909123456', N'123 Đường A, Quận 1', 1),  -- Admin
(N'Lê Thị B', 'lethib@example.com','12345', '0909234567', N'456 Đường B, Quận 2', 2),         -- Student
(N'Lê Thị C', 'lethic@example.com','123456', '0909234568', N'457 Đường B, Quận 3', 2),         -- Student
(N'Lê Thị C', 'lethic@gmail.com','123456', '0909234568', N'Nội Bài, Hà Nội', 2),         -- Student
(N'Lê Thị D', 'lethid@example.com','1234567', '0909234569', N'458 Đường B, Quận 4', 2),         -- Student

(N'Phạm Văn C', 'phamvanc@example.com','1234', '0909345678', N'789 Đường C, Quận 3', 3),      -- Teacher
(N'Phạm Văn D', 'phamvand@example.com','1234', '0909345678', N'789 Đường C, Quận 3', 3),      -- Teacher
(N'Phạm Văn E', 'phamvane@example.com','1234', '0909345678', N'789 Đường C, Quận 3', 3),      -- Teacher
(N'Phạm Văn F', 'phamvanf@example.com','1234', '0909345678', N'789 Đường C, Quận 3', 3),      -- Teacher

(N'Trần Thị D', 'tranthid@example.com','1234', '0909456789', N'321 Đường D, Quận 4', 4),     -- Accountant
(N'Nguyễn Văn E', 'nguyenvane@example.com','1234', '0909567890', N'654 Đường E, Quận 5', 5),    -- Sale
(N'Nguyễn Văn F', 'nguyenvanf@example.com','1234', '0909567891', N'655 Đường E, Quận 6', 5);    -- Sale

-- 3. Bảng Course
INSERT INTO Course (Title, Price, Information, CreatedBy) VALUES
(N'Tiếng Anh Cơ Bản', 200.0, N'Khóa học tiếng Anh cho người mới bắt đầu', 1),
(N'Máy Tính Văn Phòng', 300.0, N'Khóa học kỹ năng sử dụng máy tính văn phòng', 3),
(N'Toán Cao Cấp', 250.0, N'Khóa học toán cho sinh viên đại học', 1);

-- 4. Bảng Student
INSERT INTO Student (parentPhone, motherPhone, AccountId, EnrollmentDate) VALUES
('0912345678', '0987654321', 2, '2024-01-10'),
('0911122233', '0988776655', 3, '2024-01-15'), -- Chưa liên kết AccountId
('0909988776', '0977665544', 4, '2024-01-20');

-- 5. Bảng Teacher
INSERT INTO Teacher (AccountId, Department, Salary) VALUES
(5, N'Ngữ văn', 12000000.00),
(6, N'Toán', 13000000.00),
(7, N'Vật lý', 11000000.00),
(8, N'Toán', 11000000.00);

-- 6. Bảng Sale
INSERT INTO Sale (AccountId, Salary) VALUES
(10, 9000000.00),
(11, 9200000.00);

-- 7. Bảng Blog
INSERT INTO Blog (Title, Description, Content, AuthorId, CreatedAt) VALUES
(N'Bí quyết học tiếng Anh', N'Hướng dẫn phương pháp học hiệu quả', N'Chi tiết các phương pháp...', 1, GETDATE()),
(N'Cách sử dụng máy tính', N'Máy tính văn phòng cơ bản', N'Hướng dẫn chi tiết...', 1, GETDATE()),
(N'Phương pháp giảng dạy hiệu quả', N'Kinh nghiệm từ giáo viên', N'Nội dung chia sẻ...', 1, GETDATE());

-- 8. Bảng Room
INSERT INTO Room (Code) VALUES
('R101'),
('R102'),
('R103'),
('R104');

-- 9. Bảng ClassRooms
INSERT INTO ClassRooms (Name, CourseId, TeacherId, SlotId) VALUES
(N'Lớp Piano 1', 1, 1, 3),
(N'Lớp Guitar', 2, 1, 4),
(N'Lớp Violin', 3, 1, 6);

-- 10. Bảng Student_Class
INSERT INTO Student_Class (ClassRoomId, StudentId, JoinDate) VALUES
(2, 1, '2024-02-01'),
(2, 3, '2024-02-05'),
(2, 4, '2024-02-03'),
(2, 5, '2024-02-03'),
(2, 6, '2024-02-03');

-- 11. Bảng Schedule
INSERT INTO Schedule (Date, StartTime, EndTime, RoomId, ClassRoomId) VALUES
('2025-06-23', '08:00', '10:00', 1, 4),
('2025-06-23', '10:00', '12:00', 2, 2),
('2025-06-23', '13:00', '15:00', 3, 3),
('2025-06-23', '18:00', '21:00', 3, 3),
('2025-06-24', '08:00', '10:00', 1, 4),
('2025-06-25', '10:00', '12:00', 2, 2),
('2025-06-26', '13:00', '15:00', 3, 3);


-- 12. Bảng Attendance
INSERT INTO Attendance (ScheduleId, StudentId, Status, Note) VALUES
(1, 1, 'Present', N'Đi học đầy đủ'),
(1, 2, 'Absent', N'Bị ốm'),
(2, 1, 'Present', NULL);

-- 14. Bảng Request
INSERT INTO Request (Type, SenderId, Reason, Status, Response, CreatedAt, ResponseAt, ProcessedBy) VALUES
(N'Chuyển lớp', 2, N'Muốn chuyển sang lớp cao hơn', N'Pending', NULL, GETDATE(), NULL, NULL),
(N'Thưởng', 3, N'Hoàn thành xuất sắc nhiệm vụ', N'Approved', N'Được thưởng 1 triệu', GETDATE(), GETDATE(), 1);

-- 15. Bảng Accountant
INSERT INTO Accountant (HireDate, OutDate, Note, AccountId, Salary) VALUES
('2023-01-01', NULL, N'Kế toán chính', 9, 15000000);

-- 16. Bảng Invoice
INSERT INTO Invoice (StudentId, Amount, ExportAt, ExportBy, PaidAt, Note) VALUES
(1, 2000, GETDATE(), 1, NULL, N'Học phí tháng 6'),
(1, 3000, GETDATE(), 1, GETDATE(), N'Học phí tháng 7 đã thanh toán');

-- 17. Bảng SalaryPaymentHistory
INSERT INTO SalaryPaymentHistory (AccountId, Amount, PaymentDate, Reason, ProcessedBy) VALUES
(3, 12000000, GETDATE(), N'Lương tháng 5', 1),
(5, 9000000, GETDATE(), N'Lương tháng 5', 1),
(4, 15000000, GETDATE(), N'Lương tháng 5', 1);


CREATE TABLE RequestType (
    TypeID int PRIMARY KEY IDENTITY(1,1),
    TypeName nvarchar(100) NOT NULL,
    Description nvarchar(500)
);
ALTER TABLE Request
DROP COLUMN Type;

-- 2. Thêm cột TypeID
ALTER TABLE Request
ADD TypeID INT;

-- 3. Thêm ràng buộc khóa ngoại
ALTER TABLE Request
ADD CONSTRAINT FK_Request_RequestType
FOREIGN KEY (TypeID) REFERENCES RequestType(TypeID);
