package com.lasyankali.erp.service;

import com.lasyankali.erp.dto.PaymentOrderRespnseDTO;

public interface PaymentService {
	PaymentOrderRespnseDTO createPaymentOrder(
	        Long feeId,
	        String username
	);
}
