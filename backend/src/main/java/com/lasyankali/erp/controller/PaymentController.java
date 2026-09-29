package com.lasyankali.erp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.PaymentOrderRespnseDTO;
import com.lasyankali.erp.dto.PaymentVerificationDTO;
import com.lasyankali.erp.dto.PaymentVerificationResponseDTO;
import com.lasyankali.erp.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@PostMapping("/fees/{feeId}/order")
	@PreAuthorize("hasAnyRole('STUDENT', 'PARENT')")
	public ResponseEntity<PaymentOrderRespnseDTO> createPaymentOrder(
	        @PathVariable Long feeId,
	        Authentication authentication) {

	    String username = authentication.getName();

	    PaymentOrderRespnseDTO response =
	            paymentService.createPaymentOrder(feeId, username);

	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/verify")
	@PreAuthorize("hasAnyRole('STUDENT', 'PARENT')")
	public ResponseEntity<PaymentVerificationResponseDTO>
	        verifyPayment(
	                @Valid
	                @RequestBody
	                PaymentVerificationDTO verifyDTO,
	                Authentication authentication) {

	    String username =
	            authentication.getName();

	    PaymentVerificationResponseDTO response =
	    		paymentService.verifyPayment(
	                    verifyDTO,
	                    username
	            );

	    return ResponseEntity.ok(response);
	}
}
