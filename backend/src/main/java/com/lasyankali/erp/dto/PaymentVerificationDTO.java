package com.lasyankali.erp.dto;

import jakarta.validation.constraints.NotNull;

public class PaymentVerificationDTO {
	@NotNull(message = "Provider order ID cannot be empty")
	private String providerOrderId;
	@NotNull(message = "Provider order ID cannot be empty")
	private String providerPaymentId;
	@NotNull(message = "Provider order ID cannot be empty")
	private String providerSignature;
	
	public PaymentVerificationDTO() {
		super();
	}
	public PaymentVerificationDTO(@NotNull String providerOrderId, @NotNull String providerPaymentId,
			@NotNull String providerSignature) {
		super();
		this.providerOrderId = providerOrderId;
		this.providerPaymentId = providerPaymentId;
		this.providerSignature = providerSignature;
	}
	public String getProviderOrderId() {
		return providerOrderId;
	}
	public void setProviderOrderId(String providerOrderId) {
		this.providerOrderId = providerOrderId;
	}
	public String getProviderPaymentId() {
		return providerPaymentId;
	}
	public void setProviderPaymentId(String providerPaymentId) {
		this.providerPaymentId = providerPaymentId;
	}
	public String getProviderSignature() {
		return providerSignature;
	}
	public void setProviderSignature(String providerSignature) {
		this.providerSignature = providerSignature;
	}
	
}
