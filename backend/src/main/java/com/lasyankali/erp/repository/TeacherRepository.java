package com.lasyankali.erp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lasyankali.erp.entity.Teacher;
import com.lasyankali.erp.entity.enums.Status;

public interface TeacherRepository extends JpaRepository<Teacher,Long>{
	Optional<Teacher> findByEmployeeCode(String employeeCode);
	List<Teacher> findByStatus(Status status);
	@Query("""
		    SELECT t
		    FROM Teacher t
		    WHERE t.status = :status
		      AND (
		            LOWER(t.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
		         OR LOWER(t.user.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
		         OR LOWER(t.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
		         OR LOWER(t.user.mobileNumber) LIKE CONCAT('%', :keyword, '%')
		         OR LOWER(t.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
		      )
		    """)
		List<Teacher> searchTeachers(@Param("keyword") String keyword,
		                             @Param("status") Status status);
}
