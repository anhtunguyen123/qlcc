-- 1. Bảng người dùng
CREATE TABLE users (
    user_id BIGINT IDENTITY(1,1) PRIMARY KEY,           
    full_name NVARCHAR(100) NOT NULL,                  
    email NVARCHAR(100) UNIQUE NOT NULL,               
    password_hash NVARCHAR(255) NOT NULL,              
    role NVARCHAR(20) DEFAULT N'TEACHER',              
    status NVARCHAR(20) DEFAULT N'ACTIVE',             
    created_at DATETIME2 DEFAULT GETDATE()             
);

-- 2. Bảng danh mục thiết bị
CREATE TABLE device_categories (
    category_id INT IDENTITY(1,1) PRIMARY KEY,          
    category_name NVARCHAR(100) NOT NULL,               
    description NVARCHAR(255),                          
    created_at DATETIME2 DEFAULT GETDATE()              
);

-- 3. Bảng phòng học
CREATE TABLE classrooms (
    room_id NVARCHAR(50) PRIMARY KEY,                   
    room_name NVARCHAR(100) NOT NULL,                   
    floor INT,                                          
    building NVARCHAR(50),                              
    capacity INT,                                       
    has_projector BIT DEFAULT 0,                        
    has_computer BIT DEFAULT 0,                         
    has_sound_system BIT DEFAULT 0,                     
    created_at DATETIME2 DEFAULT GETDATE()              
);

-- 4. Bảng thiết bị
CREATE TABLE devices (
    device_id VARCHAR(20) PRIMARY KEY,                  
    device_name NVARCHAR(100) NOT NULL,                 
    category_id INT,                                    
    room NVARCHAR(50),                                  
    description NVARCHAR(MAX),                          
    specifications NVARCHAR(MAX),                       
    is_portable BIT DEFAULT 0,                          -- ✅ Thêm để fix lỗi
    status NVARCHAR(20) DEFAULT N'AVAILABLE',           
    created_at DATETIME2 DEFAULT GETDATE(),             
    CONSTRAINT fk_device_category FOREIGN KEY (category_id) REFERENCES device_categories(category_id),
    CONSTRAINT fk_device_room FOREIGN KEY (room) REFERENCES classrooms(room_id)
);

-- 5. Bảng đặt chỗ
CREATE TABLE reservations (
    reservation_id BIGINT IDENTITY(1,1) PRIMARY KEY,    
    device_id VARCHAR(20) NOT NULL,                     
    user_id BIGINT NOT NULL,                            
    start_time DATETIME2 NOT NULL,                      
    end_time DATETIME2 NOT NULL,                        
    purpose NVARCHAR(255),                              
    status NVARCHAR(20) DEFAULT N'PENDING',             
    created_at DATETIME2 DEFAULT GETDATE(),              
    CONSTRAINT fk_reservation_device FOREIGN KEY (device_id) REFERENCES devices(device_id),
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 6. Bảng mượn trả
CREATE TABLE borrows (
    borrow_id BIGINT IDENTITY(1,1) PRIMARY KEY,         
    device_id VARCHAR(20) NOT NULL,                     
    user_id BIGINT NOT NULL,                            
    reservation_id BIGINT NULL,                         
    actual_start_time DATETIME2 NOT NULL,               
    actual_end_time DATETIME2 NOT NULL,                 
    borrow_date DATE NOT NULL,                          
    return_due DATE NOT NULL,                           
    return_date DATE,                                   
    purpose NVARCHAR(255),                              
    notes NVARCHAR(MAX),                                
    extension_count INT DEFAULT 0,                      
    status NVARCHAR(20) DEFAULT N'BORROWED',            
    created_at DATETIME2 DEFAULT GETDATE(),             
    CONSTRAINT fk_borrow_device FOREIGN KEY (device_id) REFERENCES devices(device_id),
    CONSTRAINT fk_borrow_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_borrow_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
);

-- 7. Bảng bảo trì
CREATE TABLE maintenance (
    maintenance_id BIGINT IDENTITY(1,1) PRIMARY KEY,    
    device_id VARCHAR(20) NOT NULL,                     
    reported_by BIGINT NOT NULL,                        
    description NVARCHAR(MAX) NOT NULL,                 
    report_date DATE NOT NULL,                          
    estimated_completion_date DATE,                     
    actual_completion_date DATE,                        
    cost DECIMAL(10,2),                                 
    technician_notes NVARCHAR(MAX),                     
    status NVARCHAR(20) DEFAULT N'PENDING',             
    updated_at DATETIME2 DEFAULT GETDATE(),             
    CONSTRAINT fk_maintenance_device FOREIGN KEY (device_id) REFERENCES devices(device_id),
    CONSTRAINT fk_maintenance_reporter FOREIGN KEY (reported_by) REFERENCES users(user_id)
);

-- 8. Bảng thông số thiết bị
CREATE TABLE device_specifications (
    spec_id BIGINT IDENTITY(1,1) PRIMARY KEY,           
    device_id VARCHAR(20) NOT NULL,                     
    spec_name NVARCHAR(100) NOT NULL,                   
    spec_value NVARCHAR(255) NOT NULL,                  
    created_at DATETIME2 DEFAULT GETDATE(),             
    CONSTRAINT fk_spec_device FOREIGN KEY (device_id) REFERENCES devices(device_id)
);

-- 9. Bảng lịch sử tìm kiếm
CREATE TABLE search_history (
    search_id BIGINT IDENTITY(1,1) PRIMARY KEY,         
    user_id BIGINT NOT NULL,                            
    search_query NVARCHAR(500) NOT NULL,                
    search_filters NVARCHAR(MAX),                       
    result_count INT,                                   
    created_at DATETIME2 DEFAULT GETDATE(),             
    CONSTRAINT fk_search_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 10. Bảng nhật ký hoạt động
CREATE TABLE activity_logs (
    log_id BIGINT IDENTITY(1,1) PRIMARY KEY,            
    user_id BIGINT NOT NULL,                            
    device_id VARCHAR(20) NULL,                         
    action NVARCHAR(255),                               
    action_type NVARCHAR(50),                           
    ip_address NVARCHAR(45),                            
    user_agent NVARCHAR(255),                           
    created_at DATETIME2 DEFAULT GETDATE(),             
    CONSTRAINT fk_log_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_log_device FOREIGN KEY (device_id) REFERENCES devices(device_id)
);

-- ✅ Các chỉ mục
CREATE INDEX idx_devices_room_status ON devices(room, status);
CREATE INDEX idx_borrows_dates ON borrows(borrow_date, return_due, status);
CREATE INDEX idx_reservations_times ON reservations(start_time, end_time, status);
CREATE INDEX idx_maintenance_status ON maintenance(status, report_date);

-- ✅ Dữ liệu mẫu
INSERT INTO device_categories (category_name, description) VALUES
(N'Máy chiếu', N'Thiết bị trình chiếu đa năng'),
(N'Laptop', N'Máy tính xách tay phục vụ giảng dạy');

INSERT INTO classrooms (room_id, room_name, floor, building, capacity, has_projector, has_computer, has_sound_system) VALUES
('P201', N'Phòng 201 - Giảng đường A', 2, N'Giảng đường A', 50, 1, 1, 1),
('LAB101', N'Phòng Lab Máy tính', 1, N'Giảng đường A', 30, 1, 1, 0);

INSERT INTO users (full_name, email, password_hash, role) VALUES
(N'Nguyễn Văn A', 'teacher_a@school.edu.vn', 'hashed_password_1', 'TEACHER'),
(N'Admin', 'admin@school.edu.vn', 'hashed_password_admin', 'ADMIN');

INSERT INTO devices (device_id, device_name, category_id, room, description, is_portable, status) VALUES
('MC-001', N'Máy chiếu Sony', 1, 'P201', N'Máy chiếu Sony', 0, 'AVAILABLE'),
('LT-015', N'Laptop Dell', 2, 'LAB101', N'Laptop Dell core i5', 1, 'AVAILABLE');
