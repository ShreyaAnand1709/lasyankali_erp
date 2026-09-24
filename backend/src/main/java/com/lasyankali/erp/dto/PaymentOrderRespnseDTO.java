package com.lasyankali.erp.dto;

public class PaymentOrderRespnseDTO {
	Long paymentId;
	Long feeId;
	String providerOrderId;
	Long amountInPaise;
	String currency;
	String keyId;
	public PaymentOrderRespnseDTO(Long paymentId, Long feeId, String providerOrderId, Long amountInPaise,
			String currency, String keyId) {
		super();
		this.paymentId = paymentId;
		this.feeId = feeId;
		this.providerOrderId = providerOrderId;
		this.amountInPaise = amountInPaise;
		this.currency = currency;
		this.keyId = keyId;
	}
	
	public PaymentOrderRespnseDTO() {
		super();
	}

	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	public Long getFeeId() {
		return feeId;
	}
	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}
	public String getProviderOrderId() {
		return providerOrderId;
	}
	public void setProviderOrderId(String providerOrderId) {
		this.providerOrderId = providerOrderId;
	}
	public Long getAmountInPaise() {
		return amountInPaise;
	}
	public void setAmountInPaise(Long amountInPaise) {
		this.amountInPaise = amountInPaise;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
}
