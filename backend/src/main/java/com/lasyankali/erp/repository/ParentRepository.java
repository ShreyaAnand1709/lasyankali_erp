package com.lasyankali.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lasyankali.erp.entity.Parents;

public interface ParentRepository extends JpaRepository<Parents,Long>{
	@Query("""
	        SELECT p
	        FROM Parents p
	        WHERE
	              LOWER(p.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
	           OR LOWER(p.user.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
	           OR LOWER(p.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
	           OR p.user.mobileNumber LIKE CONCAT('%', :keyword, '%')
	           OR LOWER(p.occupation) LIKE LOWER(CONCAT('%', :keyword, '%'))
	           OR LOWER(p.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
	    """)
	    List<Parents> searchParents(@Param("keyword") String keyword);
}
