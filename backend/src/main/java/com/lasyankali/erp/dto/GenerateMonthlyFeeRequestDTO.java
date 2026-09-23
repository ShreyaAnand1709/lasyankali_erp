package com.lasyankali.erp.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;

@Component
public class GenerateMonthlyFeeRequestDTO {
	@NotNull
	private Long studentId;
	@NotNull
	private Long batchId;
	@NotNull
	private LocalDate billingMonth;
	public GenerateMonthlyFeeRequestDTO(Long studentId, Long batchId, LocalDate billingMonth) {
		super();
		this.studentId = studentId;
		this.batchId = batchId;
		this.billingMonth = billingMonth;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public LocalDate getBillingMonth() {
		return billingMonth;
	}
	public void setBillingMonth(LocalDate billingMonth) {
		this.billingMonth = billingMonth;
	}
	public GenerateMonthlyFeeRequestDTO() {
		super();
	}
}
