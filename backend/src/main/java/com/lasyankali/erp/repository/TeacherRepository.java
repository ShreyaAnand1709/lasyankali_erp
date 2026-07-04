package com.lasyankali.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher,Long>{
	Optional<Teacher> findByEmployeeCode(String employeeCode);
}
