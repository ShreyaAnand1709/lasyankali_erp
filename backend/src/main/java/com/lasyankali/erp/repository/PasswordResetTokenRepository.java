package com.lasyankali.erp.repository;

import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lasyankali.erp.entity.PasswordResetToken;

import jakarta.persistence.LockModeType;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT token
            FROM PasswordResetToken token
            WHERE token.tokenHash = :tokenHash
            """)
    Optional<PasswordResetToken> findByTokenHashForUpdate(
            @Param("tokenHash") String tokenHash);
	
	@Modifying(flushAutomatically = true)
	@Query("""
	        UPDATE PasswordResetToken token
	        SET token.usedAt = :usedAt
	        WHERE token.user.userId = :userId
	          AND token.usedAt IS NULL
	        """)
	int invalidateUnusedTokensForUser(
	        @Param("userId") Long userId,
	        @Param("usedAt") LocalDateTime usedAt);
	@Query("""
	        SELECT token.user.userId
	        FROM PasswordResetToken token
	        WHERE token.tokenHash = :tokenHash
	        """)
	Optional<Long> findUserIdByTokenHash(
	        @Param("tokenHash") String tokenHash);
}
