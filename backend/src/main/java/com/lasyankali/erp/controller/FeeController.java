package com.lasyankali.erp.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.FeeResponseDTO;
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
	
	@PostMapping("/generate-monthly")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Integer> generateMonthy(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate billingMonth) {
		Integer val = feeService.generateMonthlyFees(billingMonth);
		return ResponseEntity.ok(val);
	}
	
	@PostMapping("/mark-overdue")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Integer> markOverdue() {
		LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));
		int val = feeService.markOverDueFees(currentDate);
		return ResponseEntity.ok(val);	
	}
	
	@GetMapping("/feeMe")
	@PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
	public ResponseEntity<List<FeeResponseDTO>> getMyFees(Authentication auth) {
		String username = auth.getName();
		List<FeeResponseDTO> response = feeService.getMyFees(username);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<List<FeeResponseDTO>> allFees() {
		List<FeeResponseDTO> response = feeService.getAllFees();
		return ResponseEntity.ok(response);
	} 
	
	@GetMapping("/my-children")
	@PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
	public ResponseEntity<List<FeeResponseDTO>> getChildFee(Authentication auth) {
		String username = auth.getName();
		List<FeeResponseDTO> response = feeService.getMyFees(username);
		return ResponseEntity.ok(response);
	}
}
