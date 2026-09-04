package com.lasyankali.erp.service;

import java.util.List;

import com.lasyankali.erp.dto.BatchResponseDTO;
import com.lasyankali.erp.dto.CreateBatchDTO;
import com.lasyankali.erp.dto.UpdateBatchDTO;


public interface BatchService {
	void createBatch(CreateBatchDTO createBatchDTo);
	List<BatchResponseDTO> getAllBatches();
	BatchResponseDTO getBatchById(Long batchId);
	BatchResponseDTO updateBatch(Long batchId,
            UpdateBatchDTO updateBatchDTO);
	void deleteBatch(Long batchId);
	List<BatchResponseDTO> searchBatch(String keyword);
}
