package com.lasyankali.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.FeeRate;
import com.lasyankali.erp.entity.enums.BatchLevel;

public interface FeeRateRepository extends JpaRepository<FeeRate,Long>{
	Optional<FeeRate> findByBatchLevel(BatchLevel batchLevel);

}
