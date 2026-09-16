UPDATE batch_students
SET status = 'ACTIVE'
WHERE status IS NULL;