package com.lasyankali.erp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.lasyankali.erp.entity.Batch;
import com.lasyankali.erp.entity.enums.BatchStatus;

public interface BatchRepository extends JpaRepository<Batch, Long>{
	Optional<Batch> findByBatchCode(String batchCode);
	boolean existsByBatchCode(String batchCode);
	List<Batch> findByStatus(BatchStatus status);
	@Query("""
			SELECT b 
			from Batch b
			where b.status = com.lasyankali.erp.entity.enums.BatchStatus.ACTIVE
			AND (
			LOWER(b.batchCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
	         OR LOWER(b.batchName) LIKE LOWER(CONCAT('%', :keyword, '%'))
	         )
			""")
	List<Batch> searchBatch(@Param("keyword") String keyword);
}
