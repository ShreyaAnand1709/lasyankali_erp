package com.lasyankali.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{
	Optional<Role> findByRoleName(String roleName);
}
