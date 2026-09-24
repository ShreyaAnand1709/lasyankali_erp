package com.lasyankali.erp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.lasyankali.erp.entity.enums.BatchLevel;
import com.lasyankali.erp.entity.enums.FeeStatus;

public class FeeResponseDTO {
	Long feeId;
	Long studentId;
	String studentName;
	Long batchId;
	String batchName;
	BatchLevel batchLevel;
	BigDecimal feeAmount;
	LocalDate billingMonth;
	LocalDate dueDate;
	FeeStatus feeStatus;
	String remarks;
	public Long getFeeId() {
		return feeId;
	}
	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public BatchLevel getBatchLevel() {
		return batchLevel;
	}
	public void setBatchLevel(BatchLevel batchLevel) {
		this.batchLevel = batchLevel;
	}
	public BigDecimal getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
	public LocalDate getBillingMonth() {
		return billingMonth;
	}
	public void setBillingMonth(LocalDate billingMonth) {
		this.billingMonth = billingMonth;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public FeeStatus getFeeStatus() {
		return feeStatus;
	}
	public void setFeeStatus(FeeStatus feeStatus) {
		this.feeStatus = feeStatus;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public FeeResponseDTO() {
		super();
	}
	public FeeResponseDTO(Long feeId, Long studentId, String studentName, Long batchId, String batchName,
			BatchLevel batchLevel, BigDecimal feeAmount, LocalDate billingMonth, LocalDate dueDate, FeeStatus feeStatus,
			String remarks) {
		super();
		this.feeId = feeId;
		this.studentId = studentId;
		this.studentName = studentName;
		this.batchId = batchId;
		this.batchName = batchName;
		this.batchLevel = batchLevel;
		this.feeAmount = feeAmount;
		this.billingMonth = billingMonth;
		this.dueDate = dueDate;
		this.feeStatus = feeStatus;
		this.remarks = remarks;
	}
}
