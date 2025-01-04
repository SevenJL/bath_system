CREATE DATABASE IF NOT EXISTS `bath_system`;
USE bath_system;

CREATE TABLE users
(
    user_id       INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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



CREATE TABLE reservations
(
    reservation_id       INT AUTO_INCREMENT PRIMARY KEY,
    user_id              INT      NOT NULL,
    start_time           DATETIME NOT NULL,
    end_time             DATETIME NOT NULL,
    status               ENUM ('pending', 'confirmed', 'cancelled') DEFAULT 'pending',
    confirmation_sent_at DATETIME,
    created_at           DATETIME                                   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- 插入虚假数据到 reservations 表
INSERT INTO reservations (user_id, start_time, end_time, status, confirmation_sent_at)
VALUES (1, '2025-01-04 10:00:00', '2025-01-04 12:00:00', 'pending', NULL),
       (2, '2025-01-05 14:00:00', '2025-01-05 16:00:00', 'confirmed', '2025-01-04 10:00:00'),
       (3, '2025-01-06 09:00:00', '2025-01-06 10:30:00', 'cancelled', '2025-01-05 08:00:00'),
       (1, '2025-01-07 13:00:00', '2025-01-07 15:00:00', 'confirmed', '2025-01-06 15:00:00'),
       (4, '2025-01-08 11:00:00', '2025-01-08 12:30:00', 'pending', NULL),
       (2, '2025-01-09 16:00:00', '2025-01-09 18:00:00', 'pending', NULL),
       (5, '2025-01-10 08:30:00', '2025-01-10 09:30:00', 'confirmed', '2025-01-09 17:00:00'),
       (3, '2025-01-11 10:00:00', '2025-01-11 11:30:00', 'pending', NULL),
       (6, '2025-01-12 15:00:00', '2025-01-12 17:00:00', 'cancelled', '2025-01-10 10:00:00'),
       (4, '2025-01-13 09:00:00', '2025-01-13 11:00:00', 'confirmed', '2025-01-12 12:00:00');


CREATE TABLE sign_ins
(
    sign_in_id   INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    sign_in_time DATETIME                     DEFAULT CURRENT_TIMESTAMP,
    status       ENUM ('active', 'completed') DEFAULT 'active',
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);



# create table if not exists feedback
# (
#     feedback_id   int auto_increment
#         primary key,
#     user_id       int                                null,
#     feedback_text text                               not null,
#     created_time  datetime default CURRENT_TIMESTAMP null,
#     feedback_type varchar(10)                        null,
#     constraint feedback_ibfk_1
#         foreign key (user_id) references users (user_id)
# );
#
# create index user_id
#     on feedback (user_id);






CREATE TABLE captchas
(
    captcha_id   INT AUTO_INCREMENT PRIMARY KEY,
    captcha_code VARCHAR(10) NOT NULL,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- 插入用户
INSERT INTO users (username, email, password_hash)
VALUES ('john_doe', 'john@example.com', 'hashed_password_1'),
       ('jane_smith', 'jane@example.com', 'hashed_password_2');

-- 插入预约
INSERT INTO reservations (user_id, start_time, end_time, status)
VALUES (1, '2025-01-10 10:00:00', '2025-01-10 11:00:00', 'confirmed'),
       (2, '2025-01-10 10:30:00', '2025-01-10 11:30:00', 'pending');

-- 插入签到
INSERT INTO sign_ins (user_id, status)
VALUES (1, 'active'),
       (2, 'completed');

-- 插入反馈
INSERT INTO feedback (user_id, feedback_text)
VALUES (1, '服务很好，环境舒适。'),
       (NULL, '希望增加更多的沐浴用品。');
