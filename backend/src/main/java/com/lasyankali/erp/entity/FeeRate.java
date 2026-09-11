package com.lasyankali.erp.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.lasyankali.erp.entity.enums.BatchLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="fee_rates")
public class FeeRate {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fee_Rate_id")
	private Long feeRateId;
	@Column(name="batch_level",nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private BatchLevel batchLevel;
	@Column(name="monthly_amount", nullable=false, precision=10, scale=2)
	private BigDecimal monthlyAmount;
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(name = "currency", length = 3, nullable = false)
	private String currency;
	public FeeRate(Long feeRateId, BatchLevel batchLevel, BigDecimal monthlyAmount, String currency) {
		super();
		this.feeRateId = feeRateId;
		this.batchLevel = batchLevel;
		this.monthlyAmount = monthlyAmount;
		this.currency = currency;
	}
	public FeeRate() {
		super();
	}
	public Long getFeeRateId() {
		return feeRateId;
	}
	public void setFeeRateId(Long feeRateId) {
		this.feeRateId = feeRateId;
	}
	public BatchLevel getBatchLevel() {
		return batchLevel;
	}
	public void setBatchLevel(BatchLevel batchLevel) {
		this.batchLevel = batchLevel;
	}
	public BigDecimal getMonthlyAmount() {
		return monthlyAmount;
	}
	public void setMonthlyAmount(BigDecimal monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
