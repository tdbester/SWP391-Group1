CREATE
DATABASE Final;
GO
USE Final;

-- Bảng Role: lưu thông tin vai trò của người dùng trong hệ thống
CREATE TABLE Role
(
    Id   INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    Name NVARCHAR(100)                  -- Tên vai trò (Admin, Student, Teacher, Accountant)
);

-- Bảng Account: thông tin đăng nhập & cá nhân của người dùng
CREATE TABLE Account
(
    Id          INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    FullName    NVARCHAR(255),                 -- Họ tên đầy đủ
    Email       VARCHAR(255) UNIQUE,           -- Email đăng nhập, duy nhất
    PhoneNumber VARCHAR(20),                   -- Số điện thoại
    Address     NVARCHAR(500),                 -- Địa chỉ
    Password    VARCHAR(50),
    Avartar     VARCHAR(500),
    RoleId      INT,                           -- FK tới bảng Role: vai trò người dùng
    FOREIGN KEY (RoleId) REFERENCES Role (Id)
);

-- Bảng lưu token
CREATE TABLE tokenForgetPassword
(
    id         int IDENTITY(1,1) PRIMARY KEY,
    token      VARCHAR(255) NOT NULL,
    expiryTime DATETIME     NOT NULL,
    isUsed     bit          NOT NULL,
    accountId  int          NOT NULL,
    FOREIGN KEY (accountId) REFERENCES [Account](Id)
);

-- Bảng Sale: thông tin Sale
CREATE TABLE Sale
(
    Id        INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    AccountId INT UNIQUE,                    -- FK: liên kết 1-1 với tài khoản
    Salary    DECIMAL(12, 2),                -- Mức Lương
    FOREIGN KEY (AccountId) REFERENCES Account (Id)
);

-- bảng lưu loại khoá học và blog
CREATE TABLE Category
(
    Id   INT PRIMARY KEY IDENTITY,
    name NVARCHAR(255),
    Type INT
)

--bảng lưu khoá học
CREATE TABLE Course
(
    Id          INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    Title       NVARCHAR(255),                 -- Tên khóa học
    Price       FLOAT,                         -- Giá tiền
    Information NVARCHAR(MAX),                 -- Thông tin mô tả chi tiết
    CreatedBy   INT,                           -- FK: người tạo khóa học (AccountId)
    [
    Image]
    NVARCHAR
(
    500
), [CategoryId] INT, [Level] NVARCHAR(20), -- [Type] NVARCHAR(20), [Status] INT, FOREIGN KEY (CreatedBy) REFERENCES Account (Id), FOREIGN KEY ([CategoryId]) REFERENCES Category (Id)
    );


CREATE TABLE Consultations
(
    Id                 INT PRIMARY KEY IDENTITY(1,1),                                   -- Khóa chính, tự tăng
    FullName           NVARCHAR(50),                                                    -- người gửi yêu cầu
    Email              VARCHAR(50),                                                     -- Email
    Phone              VARCHAR(50),
    Status             NVARCHAR(50) CONSTRAINT DF_Consult_Status DEFAULT N'Chưa xử lý', -- Trạng thái:
    CourseId           INT,                                                             -- Course
    CreatedAt          DATETIME DEFAULT GETDATE(),                                      -- Ngày tạo yêu cầu
    ResponseAt         DATETIME DEFAULT NULL,                                           -- Ngày phản hồi
    ProcessedBy        INT      DEFAULT NULL,                                           -- FK: người xử lý yêu cầu (AccountId)
    PaymentStatus      NVARCHAR(50) CONSTRAINT DF_Payment_Status DEFAULT N'Chưa thanh toán',
    AccountRequestSent BIT      DEFAULT 0,
    Note               NVarchar(MAX),
    FOREIGN KEY (CourseId) REFERENCES Course (Id),
    FOREIGN KEY (ProcessedBy) REFERENCES Sale (Id)
);

-- Bảng Student: thông tin chi tiết học sinh
CREATE TABLE Student
(
    Id             INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    parentPhone    VARCHAR(20),                   -- SĐT bố
    AccountId      INT UNIQUE,                    -- FK: liên kết 1-1 với tài khoản
    EnrollmentDate DATE,                          -- Ngày nhập học
    ConsultationId INT,
    FOREIGN KEY (AccountId) REFERENCES Account (Id),
    FOREIGN KEY (ConsultationId) REFERENCES Consultations (Id)
);

-- Bảng Teacher: thông tin giáo viên
CREATE TABLE Teacher
(
    Id         INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    AccountId  INT UNIQUE,                    -- FK: liên kết 1-1 với tài khoản
    Department NVARCHAR(255),                 -- Bộ môn phụ trách
    Salary     DECIMAL(12, 2),                -- Mức Lương
    FOREIGN KEY (AccountId) REFERENCES Account (Id)
);


-- Bảng Blog: chứa các bài viết/blog trong hệ thống
CREATE TABLE Blog
(
    Id          INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    Title       NVARCHAR(255),                 -- Tiêu đề bài viết
    Description NVARCHAR(MAX),                 -- Mô tả ngắn

    Image       NVARCHAR (500),
    Content     TEXT,                          -- Nội dung chi tiết
    AuthorId    INT,                           -- FK: người viết, tham chiếu Account(Id)
    CreatedAt   DATETIME DEFAULT GETDATE(),    -- Ngày tạo bài viết

    CategoryId
                INT,
    Status
                INT,
    FOREIGN KEY (AuthorId) REFERENCES Sale (Id),
    FOREIGN KEY (CategoryId) REFERENCES Category (Id)
);


-- Bảng Room: danh sách các phòng học
CREATE TABLE Room
(
    Id   INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    Code VARCHAR(20)                    -- Mã phòng học
);

--Bang Slot
CREATE TABLE Slot
(
    Id        INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    StartTime TIME,                          -- Giờ bắt đầu
    EndTime   TIME
);


-- Bảng ClassRooms: thông tin các lớp học
CREATE TABLE ClassRooms
(
    Id          INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    Name        NVARCHAR(255),                 -- Tên lớp
    CourseId    INT,                           -- FK: khóa học liên quan
    TeacherId   INT,                           -- FK: giáo viên phụ trách
    MaxCapicity int default 15,
    SlotId      int,
    FOREIGN KEY (CourseId) REFERENCES Course (Id),
    FOREIGN KEY (TeacherId) REFERENCES Teacher (Id),
    FOREIGN KEY (SlotId) REFERENCES Slot (Id)
);

-- Bảng Student_Class: danh sách học sinh thuộc từng lớp học
CREATE TABLE Student_Class
(
    Id          INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    ClassRoomId INT,                           -- FK: lớp học
    StudentId   INT,                           -- FK: học sinh
    JoinDate    DATE,                          -- Ngày vào lớp
    FOREIGN KEY (StudentId) REFERENCES Student (Id),
    FOREIGN KEY (ClassRoomId) REFERENCES ClassRooms (Id)
);
--khi tạo lịch học tạo schedule trong 1 tuần
--cần nhập các schedule (các slot bao gồm thông tin giờ bắt đầu giờ kết thúc buổi học)
--b1: chọn số slot trong 1 tuần
--b2: vd (3slot) điền thông tin 3 slot (start time, day)
--b3: chọn số tuần lặp lại (all, number) => tự động lặp (tính toán dựa trên ngày bắt đầu và kết thúc)
--b4: hệ thống tự tạo ra số schedule từ thông tin trên
-- Bảng Schedule: thời khóa biểu các lớp học

CREATE TABLE Schedule
(
    Id          INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    Date        DATE,                          -- Ngày học
    RoomId      INT,                           -- FK: phòng học
    ClassRoomId INT,                           -- FK: lớp học
    SlotId      Int,
    FOREIGN KEY (RoomId) REFERENCES Room (Id),
    FOREIGN KEY (ClassRoomId) REFERENCES ClassRooms (Id),
    FOREIGN KEY (SlotId) REFERENCES Slot (Id)
);

-- Bảng Attendance: điểm danh học sinh theo buổi học
CREATE TABLE Attendance
(
    Id         INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    ScheduleId INT,                           -- FK: buổi học
    StudentId  INT,                           -- FK: học sinh
    Status     VARCHAR(20),                   -- Trạng thái: Present, Absent
    Note       NVARCHAR(255),                 -- Ghi chú (nếu có)
    FOREIGN KEY (ScheduleId) REFERENCES Schedule (Id),
    FOREIGN KEY (StudentId) REFERENCES Student (Id)
);

-- Bảng RequestType: loại đơn
CREATE TABLE RequestType
(
    TypeID      int PRIMARY KEY IDENTITY(1,1),
    TypeName    nvarchar(100) NOT NULL,
    Description nvarchar(500)
);
INSERT INTO RequestType (TypeName, Description)
VALUES (N'Đơn xin chuyển lớp', N'Yêu cầu chuyển sang lớp khác phù hợp với học viên.'),
       (N'Đơn xin bảo lưu', N'Đề nghị bảo lưu kết quả hoặc dừng học tạm thời vì lý do cá nhân.'),
       (N'Đơn xin nghỉ học', N'Đơn xin phép nghỉ học trong một buổi vì lý do đặc biệt.'),
       (N'Đơn khiếu nại về giảng viên', N'Phản ánh, kiến nghị về giảng viên trong quá trình học tập.'),
       (N'Đơn khác', N'Các loại đơn không thuộc những nhóm liệt kê phía trên.'),
       (N'Đơn yêu cầu cấp tài khoản học viên',
        N'Đề nghị khởi tạo hoặc cấp lại tài khoản học viên để đăng nhập hệ thống.'),
       (N'Đơn xin nghỉ phép', N'Đơn xin nghỉ phép ngắn hạn với lý do cá nhân, sức khỏe...'),
       (N'Đơn xin đổi lịch dạy', N'Đề xuất thay đổi lịch dạy hoặc lịch học vì lý do phát sinh hợp lệ.');


-- Bảng Request: xử lý các yêu cầu trong hệ thống
CREATE TABLE Request
(
    Id          INT PRIMARY KEY IDENTITY(1,1), -- Khóa chính, tự tăng
    SenderId    INT,                           -- FK: người gửi yêu cầu (AccountId)
    Reason      NVARCHAR(1000),                -- Lý do gửi yêu cầu
    Status      NVARCHAR(50),                  -- Trạng thái: Pending, Approved, Rejected
    Response    NVARCHAR(1000),                -- Phản hồi từ người xử lý
    CreatedAt   DATETIME DEFAULT GETDATE(),    -- Ngày tạo yêu cầu
    ResponseAt  DATETIME DEFAULT NULL,         -- Ngày phản hồi
    ProcessedBy INT      DEFAULT NULL,         -- FK: người xử lý yêu cầu (AccountId)
    TypeID      int,
    FOREIGN KEY (SenderId) REFERENCES Account (Id),
    FOREIGN KEY (ProcessedBy) REFERENCES Account (Id),
    FOREIGN KEY (TypeID) REFERENCES RequestType (TypeID)
);

--Bảng thông báo--
CREATE TABLE Notification
(
    Id                 INT PRIMARY KEY IDENTITY(1,1),
    Title              NVARCHAR(255) NOT NULL,
    Content            NTEXT    NOT NULL,
    SenderName         NVARCHAR(255) DEFAULT 'SYSTEM',
    RecipientRole      NVARCHAR(50) NOT NULL,
    RecipientAccountId INT NULL,
    NotificationType   NVARCHAR(50) NOT NULL,
    RelatedEntityId    INT NULL,
    RelatedEntityType  NVARCHAR(50) NULL,
    CreatedAt          DATETIME NOT NULL DEFAULT GETDATE(),
    IsRead             BIT               DEFAULT 0,
    FOREIGN KEY (RecipientAccountId) REFERENCES Account (Id)
);

--bảng tạo lớp học
CREATE TABLE ClassSchedulePattern
(
    Id          INT PRIMARY KEY IDENTITY(1,1),
    ClassRoomId INT  NOT NULL,
    StartDate   DATE NOT NULL,
    EndDate     DATE NOT NULL,
    SlotId      INT  NOT NULL,
    DayOfWeek   INT  NOT NULL, -- 1=Monday, ..., 7=Sunday
    FOREIGN KEY (ClassRoomId) REFERENCES ClassRooms (Id),
    FOREIGN KEY (SlotId) REFERENCES Slot (Id)
);

-- Bảng lưu thông tin lương giáo viên theo tháng
CREATE TABLE TeacherSalary
(
    Id            INT PRIMARY KEY IDENTITY(1,1),
    TeacherId     INT NOT NULL,
    Month         INT NOT NULL,
    Year          INT NOT NULL,
    TotalSessions INT            DEFAULT 0,
    BaseSalary    DECIMAL(12, 2) DEFAULT 0,
    TotalSalary   DECIMAL(12, 2) DEFAULT 0,
    Adjustment    DECIMAL(12, 2) DEFAULT 0, -- Thưởng (+) hoặc phạt (-)
    FinalSalary   DECIMAL(12, 2) DEFAULT 0, -- Lương cuối cùng sau điều chỉnh
    PaymentDate   DATETIME NULL,
    Note          NVARCHAR(500),
    ProcessedBy   INT,                      -- Admin xử lý
    CreatedAt     DATETIME       DEFAULT GETDATE(),
    UpdatedAt     DATETIME       DEFAULT GETDATE(),
    FOREIGN KEY (TeacherId) REFERENCES Teacher (Id),
    FOREIGN KEY (ProcessedBy) REFERENCES Account (Id),
    UNIQUE (TeacherId, Month, Year)
);


INSERT INTO Slot (StartTime, EndTime)
VALUES ('8:00', '9:00'),
       ('9:00', '10:00'),
       ('10:00', '11:00'),
       ('14:00', '15:00'),
       ('15:00', '16:00'),
       ('16:00', '17:00'),
       ('17:00', '18:00'),
       ('18:00', '19:00'),
       ('19:00', '20:00'),
       ('20:00', '21:00');