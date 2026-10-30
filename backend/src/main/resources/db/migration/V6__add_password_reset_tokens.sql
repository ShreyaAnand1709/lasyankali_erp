CREATE TABLE password_reset_tokens (
    reset_token_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token_hash CHAR(64)
        CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    used_at DATETIME(6) NULL,
    created_at DATETIME(6) NOT NULL,

    PRIMARY KEY (reset_token_id),

    CONSTRAINT uk_password_reset_token_hash
        UNIQUE (token_hash),

    INDEX idx_password_reset_user (user_id),
    INDEX idx_password_reset_expiry (expires_at),

    CONSTRAINT fk_password_reset_user
        FOREIGN KEY (user_id)
        REFERENCES users (user_id)
) ENGINE=InnoDB;