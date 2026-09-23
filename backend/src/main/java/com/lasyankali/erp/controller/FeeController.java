package com.lasyankali.erp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.GenerateMonthlyFeeRequestDTO;
import com.lasyankali.erp.service.FeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fees")
public class FeeController {
	private final FeeService feeService;

	public FeeController(FeeService feeService) {
		super();
		this.feeService = feeService;
	}
	
	@PostMapping("/generate")
	@PreAuthorize ("hasRole('ADMIN')")
	public ResponseEntity<String> generateFee(@Valid @RequestBody GenerateMonthlyFeeRequestDTO fee) {
		feeService.generateMonthlyFee(fee.getStudentId(), fee.getBatchId(), fee.getBillingMonth());
		return ResponseEntity.ok("The generation reqauest proceed");	
	}
}
