INSERT INTO roles (role_name, description)
VALUES
    ('ADMIN', 'System Administration'),
    ('TEACHER', 'Dance Teacher'),
    ('STUDENT', 'Student User'),
    ('PARENT', 'Parent User')
AS incoming
ON DUPLICATE KEY UPDATE
    description = incoming.description;


INSERT INTO fee_rates (batch_level, monthly_amount, currency)
VALUES
    ('BEGINNER', 500.00, 'INR'),
    ('INTERMEDIATE', 900.00, 'INR'),
    ('ADVANCED', 1200.00, 'INR')
AS incoming
ON DUPLICATE KEY UPDATE
    monthly_amount = incoming.monthly_amount,
    currency = incoming.currency;