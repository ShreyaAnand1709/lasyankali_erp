package com.lasyankali.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.BatchTeacher;

public interface BatchTeacherRepository extends JpaRepository<BatchTeacher, Long>{
	boolean existsByBatch_BatchIdAndTeacher_TeacherId(Long batchId, Long teacherId);
	Optional<BatchTeacher> findByBatch_BatchIdAndTeacher_TeacherId(Long batchId, Long teacherId);
}
