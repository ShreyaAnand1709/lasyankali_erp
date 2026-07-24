package com.lasyankali.erp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lasyankali.erp.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
	Optional<Student> findByAdmissionNumber(String admissionNumber);
	List<Student> findByStatus(String status);
	@Query("""
		    SELECT s
		    FROM Student s
		    WHERE s.status = 'ACTIVE'
		      AND (
		            LOWER(s.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
		         OR LOWER(s.user.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
		         OR LOWER(s.admissionNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
		         OR LOWER(s.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
		         OR s.user.mobileNumber LIKE CONCAT('%', :keyword, '%')
		      )
		""")
		List<Student> searchStudents(@Param("keyword") String keyword);
}
