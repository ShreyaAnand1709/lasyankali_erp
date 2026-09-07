package com.lasyankali.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lasyankali.erp.entity.BatchStudent;

public interface BatchStudentRepository extends JpaRepository<BatchStudent, Long>{
	boolean existsByBatch_BatchIdAndStudent_StudentId(Long batchId, Long studentId);
	Optional<BatchStudent> findByBatch_BatchIdAndStudent_StudentId(Long batchId, Long StudentId);
}
