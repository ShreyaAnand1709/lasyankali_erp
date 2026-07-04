package com.lasyankali.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
	Optional<Student> findByAdmissionNumber(String admissionNumber);
}
