package com.lasyankali.erp.mapper;

import org.springframework.stereotype.Component;

import com.lasyankali.erp.dto.BatchResponseDTO;
import com.lasyankali.erp.entity.Batch;

@Component
public class BatchMapper {
 public BatchResponseDTO mapToBatchResponse(Batch batch) {
	 BatchResponseDTO batchDTO = new BatchResponseDTO();
	 batchDTO.setBatchId(batch.getBatchId());
	 batchDTO.setBatchCode(batch.getBatchCode());
	 batchDTO.setBatchName(batch.getBatchName());
	 batchDTO.setDiscipline(batch.getDiscipline());
	 batchDTO.setLevel(batch.getLevel());
	 batchDTO.setStartTime(batch.getStartTime());
	 batchDTO.setEndTime(batch.getEndTime());
	 batchDTO.setCapacity(batch.getCapacity());
	 batchDTO.setStatus(batch.getStatus());
	 return batchDTO;
	 
 }
}
