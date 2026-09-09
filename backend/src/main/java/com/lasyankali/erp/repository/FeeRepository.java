package com.lasyankali.erp.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.Fee;

public interface FeeRepository extends JpaRepository<Fee,Long>{
	boolean existsByStudent_StudentIdAndBatch_BatchIdAndBillingMonth(
	        Long studentId,
	        Long batchId,
	        LocalDate billingMonth);
}
