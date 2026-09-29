package com.lasyankali.erp.dto;

import com.lasyankali.erp.entity.enums.FeeStatus;
import com.lasyankali.erp.entity.enums.PaymentStatus;

public class PaymentVerificationResponseDTO {
	private Long paymentId;
	private Long feeId;
	private String providerOrderId;
	private String providerPaymentId;
	private PaymentStatus paymentStatus;
	private FeeStatus feeStatus;
	private String message;
	public PaymentVerificationResponseDTO(Long paymentId, Long feeId, String providerOrderId, String providerPaymentId,
			PaymentStatus paymentStatus, FeeStatus feeStatus, String message) {
		super();
		this.paymentId = paymentId;
		this.feeId = feeId;
		this.providerOrderId = providerOrderId;
		this.providerPaymentId = providerPaymentId;
		this.paymentStatus = paymentStatus;
		this.feeStatus = feeStatus;
		this.message = message;
	}
	public PaymentVerificationResponseDTO() {
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
	public String getProviderPaymentId() {
		return providerPaymentId;
	}
	public void setProviderPaymentId(String providerPaymentId) {
		this.providerPaymentId = providerPaymentId;
	}
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public FeeStatus getFeeStatus() {
		return feeStatus;
	}
	public void setFeeStatus(FeeStatus feeStatus) {
		this.feeStatus = feeStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
