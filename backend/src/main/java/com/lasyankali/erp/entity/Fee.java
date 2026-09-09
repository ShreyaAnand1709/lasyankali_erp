package com.lasyankali.erp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.lasyankali.erp.entity.enums.FeeStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="fees")
public class Fee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fee_id")
	private Long feeId;
	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;
	@ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;
	@Column(name="fee_amount", precision = 10, scale = 2, nullable = false)
	private BigDecimal feeAmount;
	@Column(name="due_date")
	private LocalDate dueDate;
	@Enumerated(EnumType.STRING)
	@Column(name="fee_status")
	private FeeStatus feeStatus;
	@Column(name="remarks")
	private String remarks;
	@Column(name = "billing_month", nullable = false)
	private LocalDate billingMonth;
	@Column(name="created_at")
	private LocalDateTime createdAt;
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
	public Fee(Long feeId, Student student, Batch batch, BigDecimal feeAmount, LocalDate dueDate, FeeStatus feeStatus,
			String remarks, LocalDate billingMonth, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.feeId = feeId;
		this.student = student;
		this.batch = batch;
		this.feeAmount = feeAmount;
		this.dueDate = dueDate;
		this.feeStatus = feeStatus;
		this.remarks = remarks;
		this.billingMonth = billingMonth;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public Fee() {
		super();
	}
	public Long getFeeId() {
		return feeId;
	}
	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Batch getBatch() {
		return batch;
	}
	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	public BigDecimal getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
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
	public LocalDate getBillingMonth() {
		return billingMonth;
	}
	public void setBillingMonth(LocalDate billingMonth) {
		this.billingMonth = billingMonth;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
