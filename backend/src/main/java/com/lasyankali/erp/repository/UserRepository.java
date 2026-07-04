package com.lasyankali.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
	Optional<User> findByEmail(String email);
	
	Optional<User> findByUserName(String userName);
}
