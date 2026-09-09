use lasyankali_erp;

CREATE TABLE fees (
    fee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    fee_amount DECIMAL(10,2) NOT NULL,
    due_date DATE,
    fee_status ENUM(
        'PENDING',
        'PAID',
        'OVERDUE'
    ) DEFAULT 'PENDING',
    remarks VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_fee_student
        FOREIGN KEY (student_id)
        REFERENCES students(student_id),
    CONSTRAINT fk_fee_batch
        FOREIGN KEY (batch_id)
        REFERENCES batches(batch_id)
);

CREATE TABLE payments (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fee_id BIGINT NOT NULL UNIQUE,
    amount_paid DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method ENUM(
        'CASH',
        'UPI'
    ) NOT NULL,
    transaction_reference VARCHAR(100),
    payment_status ENUM(
        'SUCCESS',
        'FAILED'
    ) DEFAULT 'SUCCESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_fee
        FOREIGN KEY (fee_id)
        REFERENCES fees(fee_id)

);

CREATE TABLE fee_rates (
    fee_rate_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED')
        NOT NULL UNIQUE,
    monthly_amount DECIMAL(10,2) NOT NULL,
    currency CHAR(3) NOT NULL DEFAULT 'INR'
);

INSERT INTO fee_rates (batch_level, monthly_amount)
VALUES
    ('BEGINNER', 500.00),
    ('INTERMEDIATE', 900.00),
    ('ADVANCED', 1200.00);
    
SELECT COUNT(*) AS existing_fees FROM fees;

ALTER TABLE fees
ADD COLUMN billing_month DATE NOT NULL,
ADD CONSTRAINT uk_fee_student_batch_month
    UNIQUE (student_id, batch_id, billing_month);