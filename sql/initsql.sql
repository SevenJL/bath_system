CREATE DATABASE IF NOT EXISTS `bath_system`;
USE bath_system;

CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table if not exists student
(
    student_id     int auto_increment
        primary key,
    student_number varchar(20) not null,
    name           varchar(50) not null,
    grade          varchar(50) not null,
    student_class  varchar(50) not null
);




CREATE TABLE reservations (
                              reservation_id INT AUTO_INCREMENT PRIMARY KEY,
                              user_id INT NOT NULL,
                              start_time DATETIME NOT NULL,
                              end_time DATETIME NOT NULL,
                              status ENUM('pending', 'confirmed', 'cancelled') DEFAULT 'pending',
                              confirmation_sent_at DATETIME,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE sign_ins (
                          sign_in_id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT NOT NULL,
                          sign_in_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                          status ENUM('active', 'completed') DEFAULT 'active',
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
);



CREATE TABLE feedback (
                          feedback_id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT,
                          feedback_text TEXT NOT NULL,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE captchas (
                          captcha_id INT AUTO_INCREMENT PRIMARY KEY,
                          captcha_code VARCHAR(10) NOT NULL,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- 插入用户
INSERT INTO users (username, email, password_hash) VALUES
                                                       ('john_doe', 'john@example.com', 'hashed_password_1'),
                                                       ('jane_smith', 'jane@example.com', 'hashed_password_2');

-- 插入预约
INSERT INTO reservations (user_id, start_time, end_time, status) VALUES
                                                                     (1, '2025-01-10 10:00:00', '2025-01-10 11:00:00', 'confirmed'),
                                                                     (2, '2025-01-10 10:30:00', '2025-01-10 11:30:00', 'pending');

-- 插入签到
INSERT INTO sign_ins (user_id, status) VALUES
                                           (1, 'active'),
                                           (2, 'completed');

-- 插入反馈
INSERT INTO feedback (user_id, feedback_text) VALUES
                                                  (1, '服务很好，环境舒适。'),
                                                  (NULL, '希望增加更多的沐浴用品。');
