package com.lasyankali.erp.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
	import jakarta.persistence.Column;
	import jakarta.persistence.Entity;
	import jakarta.persistence.FetchType;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.JoinColumn;
	import jakarta.persistence.ManyToOne;
	import jakarta.persistence.Table;
	@Entity
	@Table(name = "password_reset_tokens")
public class PasswordResetToken {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "reset_token_id")
	    private Long resetTokenId;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    @JdbcTypeCode(SqlTypes.CHAR)
	    @Column(name = "token_hash",nullable = false,unique = true,length = 64)
	    private String tokenHash;

	    @Column(name = "expires_at", nullable = false)
	    private LocalDateTime expiresAt;

	    @Column(name = "used_at")
	    private LocalDateTime usedAt;

	    @Column(name = "created_at", nullable = false)
	    private LocalDateTime createdAt;

	    public PasswordResetToken() {
	    }

	    public Long getResetTokenId() {
	        return resetTokenId;
	    }

	    public User getUser() {
	        return user;
	    }

	    public void setUser(User user) {
	        this.user = user;
	    }

	    public String getTokenHash() {
	        return tokenHash;
	    }

	    public void setTokenHash(String tokenHash) {
	        this.tokenHash = tokenHash;
	    }

	    public LocalDateTime getExpiresAt() {
	        return expiresAt;
	    }

	    public void setExpiresAt(LocalDateTime expiresAt) {
	        this.expiresAt = expiresAt;
	    }

	    public LocalDateTime getUsedAt() {
	        return usedAt;
	    }

	    public void setUsedAt(LocalDateTime usedAt) {
	        this.usedAt = usedAt;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }
}

