-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: lasyankali_erp_flyway_test
-- ------------------------------------------------------
-- Server version	8.0.46

CREATE TABLE `roles` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(25) NOT NULL,
  `description` varchar(25) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



--
-- Table structure for table `users`
--



CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `mobile_number` varchar(20) DEFAULT NULL,
  `first_name` varchar(25) DEFAULT NULL,
  `last_name` varchar(25) DEFAULT NULL,
  `role_id` bigint NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `last_login` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_user_role` (`role_id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `parents`
--

CREATE TABLE `parents` (
  `parent_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `occupation` varchar(255) DEFAULT NULL,
  `address` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`parent_id`),
  KEY `fk_parent_user` (`user_id`),
  CONSTRAINT `fk_parent_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `students`
--



CREATE TABLE `students` (
  `student_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  `admission_number` varchar(50) NOT NULL,
  `gender` enum('MALE','FEMALE','OTHER') NOT NULL,
  `date_of_birth` date NOT NULL,
  `joining_date` date NOT NULL,
  `photo_url` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `addmission_number` (`admission_number`),
  KEY `fk_student_parent` (`parent_id`),
  CONSTRAINT `fk_student_parent` FOREIGN KEY (`parent_id`) REFERENCES `parents` (`parent_id`),
  CONSTRAINT `fk_student_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




--
-- Table structure for table `teachers`
--



CREATE TABLE `teachers` (
  `teacher_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `employee_code` varchar(50) NOT NULL,
  `specialization` varchar(100) NOT NULL,
  `qualification` varchar(200) DEFAULT NULL,
  `years_of_experience` int DEFAULT '0',
  `joining_date` date NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`teacher_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `employee_code` (`employee_code`),
  CONSTRAINT `fk_teacher_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




--
-- Table structure for table `batches`
--



CREATE TABLE `batches` (
  `batch_id` bigint NOT NULL AUTO_INCREMENT,
  `batch_code` varchar(50) NOT NULL,
  `batch_name` varchar(100) NOT NULL,
  `discipline` enum('BHARATANATYAM','VOCAL','TABALA','YOGA') DEFAULT NULL,
  `level` enum('BEGINNER','INTERMEDIATE','ADVANCED') NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `capacity` int DEFAULT '10',
  `status` enum('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`batch_id`),
  UNIQUE KEY `batch_code` (`batch_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




--
-- Table structure for table `batch_students`
--



CREATE TABLE `batch_students` (
  `batch_student_id` bigint NOT NULL AUTO_INCREMENT,
  `batch_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `enrollment_date` date NOT NULL,
  `status` enum('ACTIVE','COMPLETED','DROPPED') DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`batch_student_id`),
  UNIQUE KEY `uk_batch_student` (`batch_id`,`student_id`),
  KEY `fk_batch_student_student` (`student_id`),
  CONSTRAINT `fk_batch_student_batch` FOREIGN KEY (`batch_id`) REFERENCES `batches` (`batch_id`),
  CONSTRAINT `fk_batch_student_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




--
-- Table structure for table `batch_teacher`
--



CREATE TABLE `batch_teacher` (
  `batch_teacher_id` bigint NOT NULL AUTO_INCREMENT,
  `batch_id` bigint NOT NULL,
  `teacher_id` bigint NOT NULL,
  `assignemd_date` date NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`batch_teacher_id`),
  UNIQUE KEY `uk_batch_teacher` (`batch_id`,`teacher_id`),
  KEY `fk_batch_teacher_teacher` (`teacher_id`),
  CONSTRAINT `fk_batch_teacher_batch` FOREIGN KEY (`batch_id`) REFERENCES `batches` (`batch_id`),
  CONSTRAINT `fk_batch_teacher_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




--
-- Table structure for table `events`
--



CREATE TABLE `events` (
  `event_id` bigint NOT NULL AUTO_INCREMENT,
  `event_code` varchar(30) NOT NULL,
  `event_name` varchar(150) NOT NULL,
  `event_type` enum('ANNUAL_DAY','ARANGETRAM','COMPETITION','WORKSHOP','PERFORMANCE','EXAM_EVENT') NOT NULL,
  `event_date` date NOT NULL,
  `venue` varchar(200) DEFAULT NULL,
  `description` text,
  `status` enum('PLANNED','ONGOING','COMPLETED','CANCELLED') DEFAULT 'PLANNED',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`event_id`),
  UNIQUE KEY `event_code` (`event_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




--
-- Table structure for table `event_registrations`
--



CREATE TABLE `event_registrations` (
  `event_registration_id` bigint NOT NULL AUTO_INCREMENT,
  `event_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `registration_date` date NOT NULL,
  `participation_status` enum('REGISTERED','PARTICIPATED','ABSENT','WINNER') DEFAULT 'REGISTERED',
  `remarks` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`event_registration_id`),
  UNIQUE KEY `uk_event_student` (`event_id`,`student_id`),
  KEY `fk_event_registration_student` (`student_id`),
  CONSTRAINT `fk_event_registration_event` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `fk_event_registration_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



--
-- Table structure for table `attendance_sessions`
--


CREATE TABLE `attendance_sessions` (
  `session_id` bigint NOT NULL AUTO_INCREMENT,
  `batch_id` bigint NOT NULL,
  `attendance_date` date NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_id`),
  KEY `fk_attendance_sessions_batch` (`batch_id`),
  CONSTRAINT `fk_attendance_sessions_batch` FOREIGN KEY (`batch_id`) REFERENCES `batches` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `attendance_records`
--



CREATE TABLE `attendance_records` (
  `record_id` bigint NOT NULL AUTO_INCREMENT,
  `session_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `status` enum('PRESENT','ABSENT') DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  KEY `fk_attendance_record_session` (`session_id`),
  KEY `fk_attendance_record_student` (`student_id`),
  CONSTRAINT `fk_attendance_record_session` FOREIGN KEY (`session_id`) REFERENCES `attendance_sessions` (`session_id`),
  CONSTRAINT `fk_attendance_record_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



--
-- Table structure for table `universities`
--



CREATE TABLE `universities` (
  `university_id` bigint NOT NULL AUTO_INCREMENT,
  `university_code` varchar(20) NOT NULL,
  `university_name` varchar(150) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`university_id`),
  UNIQUE KEY `univerity_code` (`university_code`),
  UNIQUE KEY `university_name` (`university_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `exam_levels`
--



CREATE TABLE `exam_levels` (
  `level_id` bigint NOT NULL AUTO_INCREMENT,
  `university_id` bigint NOT NULL,
  `level_code` varchar(20) NOT NULL,
  `level_name` varchar(100) NOT NULL,
  `level_order` int NOT NULL,
  `description` varchar(150) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`level_id`),
  UNIQUE KEY `uk_university_level` (`university_id`,`level_code`),
  CONSTRAINT `fk_exam_levels_universities` FOREIGN KEY (`university_id`) REFERENCES `universities` (`university_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



--
-- Table structure for table `exam_registrations`
--



CREATE TABLE `exam_registrations` (
  `registration_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `level_id` bigint NOT NULL,
  `exam_year` year NOT NULL,
  `registration_date` date NOT NULL,
  `registration_status` enum('REGISTERED','APPROVED','REJECTED','COMPLETED') DEFAULT 'REGISTERED',
  `remarks` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`registration_id`),
  KEY `fk_exam_registrations_student` (`student_id`),
  KEY `fk_exam_registrations_level` (`level_id`),
  CONSTRAINT `fk_exam_registrations_level` FOREIGN KEY (`level_id`) REFERENCES `exam_levels` (`level_id`),
  CONSTRAINT `fk_exam_registrations_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `exam_results`
--



CREATE TABLE `exam_results` (
  `result_id` bigint NOT NULL AUTO_INCREMENT,
  `registration_id` bigint NOT NULL,
  `marks` decimal(5,2) DEFAULT NULL,
  `grade` enum('A','B','C','F') DEFAULT NULL,
  `result_status` enum('PASSED','FAILED') NOT NULL,
  `result_date` date DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`result_id`),
  UNIQUE KEY `registration_id` (`registration_id`),
  CONSTRAINT `fk_exam_results_registrations` FOREIGN KEY (`registration_id`) REFERENCES `exam_registrations` (`registration_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `fee_rates`
--



CREATE TABLE `fee_rates` (
  `fee_rate_id` bigint NOT NULL AUTO_INCREMENT,
  `batch_level` enum('BEGINNER','INTERMEDIATE','ADVANCED') NOT NULL,
  `monthly_amount` decimal(10,2) NOT NULL,
  `currency` char(3) NOT NULL DEFAULT 'INR',
  PRIMARY KEY (`fee_rate_id`),
  UNIQUE KEY `batch_level` (`batch_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `fees`
--

CREATE TABLE `fees` (
  `fee_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `batch_id` bigint NOT NULL,
  `fee_amount` decimal(10,2) NOT NULL,
  `due_date` date DEFAULT NULL,
  `fee_status` enum('PENDING','PAID','OVERDUE') DEFAULT 'PENDING',
  `remarks` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `billing_month` date NOT NULL,
  PRIMARY KEY (`fee_id`),
  UNIQUE KEY `uk_fee_student_batch_month` (`student_id`,`batch_id`,`billing_month`),
  KEY `fk_fee_batch` (`batch_id`),
  CONSTRAINT `fk_fee_batch` FOREIGN KEY (`batch_id`) REFERENCES `batches` (`batch_id`),
  CONSTRAINT `fk_fee_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `payments`
--


CREATE TABLE `payments` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT,
  `fee_id` bigint NOT NULL,
  `amount_paid` decimal(10,2) NOT NULL,
  `payment_date` date NOT NULL,
  `payment_method` enum('CASH','UPI') NOT NULL,
  `transaction_reference` varchar(100) DEFAULT NULL,
  `payment_status` enum('SUCCESS','FAILED') DEFAULT 'SUCCESS',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`payment_id`),
  UNIQUE KEY `fee_id` (`fee_id`),
  CONSTRAINT `fk_payment_fee` FOREIGN KEY (`fee_id`) REFERENCES `fees` (`fee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `ai_chat_sessions`
--



CREATE TABLE `ai_chat_sessions` (
  `session_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `session_title` varchar(200) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_id`),
  KEY `fk_ai_session_user` (`user_id`),
  CONSTRAINT `fk_ai_session_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




CREATE TABLE `ai_chat_messages` (
  `message_id` bigint NOT NULL AUTO_INCREMENT,
  `session_id` bigint NOT NULL,
  `sender` enum('USER','AI') NOT NULL,
  `message` text NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`),
  KEY `fk_ai_message_session` (`session_id`),
  CONSTRAINT `fk_ai_message_session` FOREIGN KEY (`session_id`) REFERENCES `ai_chat_sessions` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



--
-- Table structure for table `ai_choreographies`
--



CREATE TABLE `ai_choreographies` (
  `choreography_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(200) NOT NULL,
  `dance_style` varchar(100) DEFAULT NULL,
  `duration_minutes` int DEFAULT NULL,
  `prompt` text NOT NULL,
  `generated_content` longtext NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`choreography_id`),
  KEY `fk_choreography_user` (`user_id`),
  CONSTRAINT `fk_choreography_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Dump completed on 2026-09-10 18:36:50
