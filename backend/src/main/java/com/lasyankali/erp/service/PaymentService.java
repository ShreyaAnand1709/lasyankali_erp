package com.lasyankali.erp.service;

import com.lasyankali.erp.dto.PaymentOrderRespnseDTO;
import com.lasyankali.erp.dto.PaymentVerificationDTO;
import com.lasyankali.erp.dto.PaymentVerificationResponseDTO;

public interface PaymentService {
	PaymentOrderRespnseDTO createPaymentOrder(
	        Long feeId,
	        String username
	);
	PaymentVerificationResponseDTO verifyPayment(PaymentVerificationDTO verifyDTO, String username);
}
