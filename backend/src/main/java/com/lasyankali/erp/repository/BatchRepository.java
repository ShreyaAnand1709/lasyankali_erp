package com.lasyankali.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.Batch;

public interface BatchRepository extends JpaRepository<Batch, Long>{
	Optional<Batch> findByBatchCode(String batchCode);
}
