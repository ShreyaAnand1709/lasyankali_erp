package com.lasyankali.erp.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.lasyankali.erp.dto.BatchResponseDTO;
import com.lasyankali.erp.dto.CreateBatchDTO;
import com.lasyankali.erp.dto.UpdateBatchDTO;
import com.lasyankali.erp.entity.Batch;
import com.lasyankali.erp.entity.enums.BatchStatus;
import com.lasyankali.erp.repository.BatchRepository;
import com.lasyankali.erp.service.BatchService;

import jakarta.transaction.Transactional;

public class BatchServiceImpl implements BatchService{
	
	private final BatchRepository batchRepository;
	
	public BatchServiceImpl(BatchRepository batchRepository) {
		super();
		this.batchRepository = batchRepository;
	}

	@Override
	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	public void createBatch(CreateBatchDTO createBatchDTO) {
		// 1. Check duplicate batch code
        if (batchRepository.existsByBatchCode(createBatchDTO.getBatchCode())) {
            throw new RuntimeException("Batch code already exists");
        }

        // 2. Validate capacity
        if (createBatchDTO.getCapacity() == null
                || createBatchDTO.getCapacity() <= 0) {

            throw new RuntimeException(
                    "Batch capacity must be greater than 0"
            );
        }

        // 3. Validate timings
        if (createBatchDTO.getStartTime() == null
                || createBatchDTO.getEndTime() == null) {

            throw new RuntimeException(
                    "Batch start time and end time are required"
            );
        }

        if (!createBatchDTO.getStartTime()
                .isBefore(createBatchDTO.getEndTime())) {

            throw new RuntimeException(
                    "Batch start time must be before end time"
            );
        }
		Batch batch = new Batch();
		batch.setBatchCode(createBatchDTO.getBatchCode());
		batch.setBatchName(createBatchDTO.getBatchName());
		batch.setDiscipline(createBatchDTO.getDiscipline());
		batch.setLevel(createBatchDTO.getLevel());
		batch.setStartTime(createBatchDTO.getStartTime());
		batch.setEndTime(createBatchDTO.getEndTime());
		batch.setCapacity(createBatchDTO.getCapacity());
		batch.setStatus(BatchStatus.ACTIVE);
		batch.setCreatedAt(LocalDateTime.now());
		batch.setUpdatedAt(LocalDateTime.now());
		batchRepository.save(batch);
		
	}

	@Override
	public List<BatchResponseDTO> getAllBatches() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BatchResponseDTO getBatchById(Long batchId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BatchResponseDTO updateBatch(Long studentId, UpdateBatchDTO updateStudentDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteStudent(Long studentId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BatchResponseDTO> searchStudents(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

}
