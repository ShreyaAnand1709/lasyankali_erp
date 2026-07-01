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