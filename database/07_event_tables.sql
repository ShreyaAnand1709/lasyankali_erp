use lasyankali_erp;

CREATE TABLE events (
    event_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_code VARCHAR(30) NOT NULL UNIQUE,
    event_name VARCHAR(150) NOT NULL,
    event_type ENUM(
        'ANNUAL_DAY',
        'ARANGETRAM',
        'COMPETITION',
        'WORKSHOP',
        'PERFORMANCE',
        'EXAM_EVENT'
    ) NOT NULL,
    event_date DATE NOT NULL,
    venue VARCHAR(200),
    description TEXT,
    status ENUM(
        'PLANNED',
        'ONGOING',
        'COMPLETED',
        'CANCELLED'
    ) DEFAULT 'PLANNED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE event_registrations (
    event_registration_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    registration_date DATE NOT NULL,
    participation_status ENUM(
        'REGISTERED',
        'PARTICIPATED',
        'ABSENT',
        'WINNER'
    ) DEFAULT 'REGISTERED',
    remarks VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_registration_event
        FOREIGN KEY (event_id)
        REFERENCES events(event_id),

    CONSTRAINT fk_event_registration_student
        FOREIGN KEY (student_id)
        REFERENCES students(student_id),

    CONSTRAINT uk_event_student
        UNIQUE(event_id, student_id)

);

