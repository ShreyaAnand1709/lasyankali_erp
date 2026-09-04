package com.lasyankali.erp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lasyankali.erp.dto.BatchResponseDTO;
import com.lasyankali.erp.dto.CreateBatchDTO;
import com.lasyankali.erp.dto.UpdateBatchDTO;
import com.lasyankali.erp.service.BatchService;

@RestController
@RequestMapping("api/batches")
public class BatchController {
	private final BatchService batchService;

	public BatchController(BatchService batchService) {
		super();
		this.batchService = batchService;
	}
	
	//creating batch
	@PostMapping
	public ResponseEntity<String> createBatch(@RequestBody CreateBatchDTO createBatchDto) {
		batchService.createBatch(createBatchDto);
		return ResponseEntity.ok("Batch created Successfully");
	}
	
	//getting all the batches
	@GetMapping()
	public ResponseEntity<List<BatchResponseDTO>> getAllBatches() {
		List<BatchResponseDTO> response = batchService.getAllBatches();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{batchId}")
	public ResponseEntity<BatchResponseDTO> getBatchById(@PathVariable Long batchId) {
		BatchResponseDTO response = batchService.getBatchById(batchId);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{batchId}")
	public ResponseEntity<BatchResponseDTO> updateBatch(@PathVariable Long batchId, @RequestBody UpdateBatchDTO updateBatchDTO) {
		BatchResponseDTO response = batchService.updateBatch(batchId, updateBatchDTO);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{batchId}")
	public ResponseEntity<Void> deleteBatch(@PathVariable Long batchId) {
		batchService.deleteBatch(batchId);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<BatchResponseDTO>> searchBatches(
	        @RequestParam String keyword) {

	    List<BatchResponseDTO> batch = batchService.searchBatch(keyword);

	    return ResponseEntity.ok(batch);
	}
}
