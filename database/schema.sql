-- EduManager Database Schema
-- Optimized for high-throughput operations with proper indexes

CREATE DATABASE IF NOT EXISTS edumanager;
USE edumanager;

-- Students Table
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    date_of_birth DATE NOT NULL,
    address TEXT,
    enrollment_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_last_name (last_name),
    INDEX idx_first_name (first_name),
    INDEX idx_email (email),
    INDEX idx_status (status),
    INDEX idx_full_name (first_name, last_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Courses Table
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(255) NOT NULL,
    description TEXT,
    credits INT NOT NULL,
    department VARCHAR(100) NOT NULL,
    instructor_name VARCHAR(200),
    capacity INT NOT NULL DEFAULT 30,
    enrolled INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_course_code (course_code),
    INDEX idx_department (department),
    INDEX idx_course_name (course_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Enrollments Table
CREATE TABLE IF NOT EXISTS enrollments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrollment_date DATE NOT NULL,
    grade VARCHAR(5),
    status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, course_id),
    INDEX idx_student_id (student_id),
    INDEX idx_course_id (course_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample Data for Testing
INSERT INTO students (first_name, last_name, email, phone_number, date_of_birth, address, enrollment_date, status)
VALUES 
    ('John', 'Doe', 'john.doe@example.com', '555-0101', '2000-05-15', '123 Main St, City, State', '2023-09-01', 'ACTIVE'),
    ('Jane', 'Smith', 'jane.smith@example.com', '555-0102', '1999-08-22', '456 Oak Ave, City, State', '2023-09-01', 'ACTIVE'),
    ('Michael', 'Johnson', 'michael.j@example.com', '555-0103', '2001-03-10', '789 Pine Rd, City, State', '2023-09-01', 'ACTIVE'),
    ('Emily', 'Brown', 'emily.brown@example.com', '555-0104', '2000-11-30', '321 Elm St, City, State', '2023-09-01', 'ACTIVE'),
    ('David', 'Wilson', 'david.w@example.com', '555-0105', '1998-07-18', '654 Maple Dr, City, State', '2022-09-01', 'ACTIVE');

INSERT INTO courses (course_code, course_name, description, credits, department, instructor_name, capacity, enrolled)
VALUES 
    ('CS101', 'Introduction to Computer Science', 'Fundamentals of programming and computer science', 3, 'Computer Science', 'Dr. Alan Turing', 30, 5),
    ('MATH201', 'Calculus I', 'Differential and integral calculus', 4, 'Mathematics', 'Dr. Isaac Newton', 25, 3),
    ('ENG101', 'English Composition', 'Academic writing and composition', 3, 'English', 'Prof. Jane Austen', 20, 2),
    ('PHYS101', 'Physics I', 'Mechanics and thermodynamics', 4, 'Physics', 'Dr. Albert Einstein', 30, 4),
    ('HIST201', 'World History', 'Survey of world civilizations', 3, 'History', 'Prof. Howard Zinn', 25, 1);

INSERT INTO enrollments (student_id, course_id, enrollment_date, grade, status)
VALUES 
    (1, 1, '2023-09-01', NULL, 'ENROLLED'),
    (1, 2, '2023-09-01', NULL, 'ENROLLED'),
    (2, 1, '2023-09-01', NULL, 'ENROLLED'),
    (2, 3, '2023-09-01', NULL, 'ENROLLED'),
    (3, 1, '2023-09-01', NULL, 'ENROLLED'),
    (3, 4, '2023-09-01', NULL, 'ENROLLED'),
    (4, 2, '2023-09-01', NULL, 'ENROLLED'),
    (4, 5, '2023-09-01', NULL, 'ENROLLED'),
    (5, 1, '2023-09-01', NULL, 'ENROLLED'),
    (5, 4, '2023-09-01', NULL, 'ENROLLED');
