use lasyankali_erp;

CREATE TABLE ai_chat_sessions (
    session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    session_title VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ai_session_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)

);

CREATE TABLE ai_chat_messages (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    sender ENUM(
        'USER',
        'AI'
    ) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ai_message_session
        FOREIGN KEY (session_id)
        REFERENCES ai_chat_sessions(session_id)

);

CREATE TABLE ai_choreographies (
    choreography_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    dance_style VARCHAR(100),
    duration_minutes INT,
    prompt TEXT NOT NULL,
    generated_content LONGTEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_choreography_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)

);