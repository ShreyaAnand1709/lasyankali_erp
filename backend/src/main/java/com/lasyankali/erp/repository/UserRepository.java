package com.lasyankali.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;

public interface UserRepository extends JpaRepository<User,Long>{
	Optional<User> findByEmail(String email);
	
	Optional<User> findByUserName(String userName);
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT u FROM User u WHERE u.userId = :userId")
	Optional<User> findByIdForUpdate(@Param("userId") Long userId);
	@Query("""
	        SELECT token.user.userId
	        FROM PasswordResetToken token
	        WHERE token.tokenHash = :tokenHash
	        """)
	Optional<Long> findUserIdByTokenHash(
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
}
