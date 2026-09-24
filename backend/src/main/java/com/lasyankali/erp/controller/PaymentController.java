package com.lasyankali.erp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.PaymentOrderRespnseDTO;
import com.lasyankali.erp.service.PaymentService;

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
}
