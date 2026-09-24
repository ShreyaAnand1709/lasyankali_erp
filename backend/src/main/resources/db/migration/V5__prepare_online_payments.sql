ALTER TABLE payments
DROP FOREIGN KEY fk_payment_fee;

ALTER TABLE payments
DROP INDEX fee_id;

ALTER TABLE payments
ADD INDEX idx_payments_fee_id (fee_id);

ALTER TABLE payments
CHANGE COLUMN amount_paid amount DECIMAL(10,2) NOT NULL,
CHANGE COLUMN transaction_reference provider_payment_id VARCHAR(100) NULL,
MODIFY COLUMN payment_date DATETIME NULL,
MODIFY COLUMN payment_method VARCHAR(30) NULL,
MODIFY COLUMN payment_status
    ENUM('CREATED', 'SUCCESS', 'FAILED')
    NOT NULL DEFAULT 'CREATED',
ADD COLUMN currency CHAR(3) NOT NULL DEFAULT 'INR'
    AFTER amount,
ADD COLUMN provider_order_id VARCHAR(100) NOT NULL
    AFTER currency,
ADD COLUMN failure_reason VARCHAR(255) NULL
    AFTER payment_status,
ADD COLUMN updated_at TIMESTAMP NULL
    DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP
    AFTER created_at;

ALTER TABLE payments
ADD CONSTRAINT fk_payment_fee
FOREIGN KEY (fee_id)
REFERENCES fees(fee_id);

ALTER TABLE payments
ADD CONSTRAINT uk_payments_provider_order_id
    UNIQUE (provider_order_id),
ADD CONSTRAINT uk_payments_provider_payment_id
    UNIQUE (provider_payment_id);