package com.lasyankali.erp.service.Impl;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.config.RazorpayConfig;
import com.lasyankali.erp.dto.PaymentOrderRespnseDTO;
import com.lasyankali.erp.entity.Fee;
import com.lasyankali.erp.entity.Payment;
import com.lasyankali.erp.entity.enums.FeeStatus;
import com.lasyankali.erp.entity.enums.PaymentStatus;
import com.lasyankali.erp.repository.FeeRepository;
import com.lasyankali.erp.repository.PaymentRepository;
import com.lasyankali.erp.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService{
	private final FeeRepository feeRepository;
	private final PaymentRepository paymentRepository;
	private final RazorpayClient razorpayClient;
	private final RazorpayConfig razorpayConfig;
	
	public PaymentServiceImpl(FeeRepository feeRepository, PaymentRepository paymentRepository,
			RazorpayClient razorpayClient, RazorpayConfig razorpayConfig) {
		super();
		this.feeRepository = feeRepository;
		this.paymentRepository = paymentRepository;
		this.razorpayClient = razorpayClient;
		this.razorpayConfig = razorpayConfig;
	}

	@Override
	@Transactional
	public PaymentOrderRespnseDTO createPaymentOrder(Long feeId, String username) {
		// TODO Auto-generated method stub
		
		if(feeId==null) {
			throw new IllegalArgumentException("Fee Id cannot be null");
		}
		if(username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be empty");
		}
		
		Fee fee = feeRepository.findById(feeId).
				orElseThrow(
						()-> new RuntimeException("Fee does not exist"));
		
		boolean studentOwnsFee = username.equals(
		        fee.getStudent().getUser().getUserName()
		);
		
		boolean parentOwnsFee =
		        fee.getStudent().getParent() != null
		        && fee.getStudent().getParent().getUser() != null
		        && username.equals(
		                fee.getStudent()
		                        .getParent()
		                        .getUser()
		                        .getUserName()
		        );
		
		if (!studentOwnsFee && !parentOwnsFee) {
		    throw new AccessDeniedException(
		            "You are not allowed to pay this fee"
		    );
		}
		
		if (fee.getFeeStatus() == FeeStatus.PAID) {
		    throw new IllegalStateException(
		            "This fee has already been paid"
		    );
		}
		boolean successfulPaymentExists =
		        paymentRepository.existsByFee_FeeIdAndPaymentStatus(
		                feeId,
		                PaymentStatus.SUCCESS
		        );

		if (successfulPaymentExists) {
		    throw new IllegalStateException(
		            "A successful payment already exists for this fee"
		    );
		}
		
		if (fee.getFeeAmount() == null
		        || fee.getFeeAmount().signum() <= 0) {
		    throw new IllegalStateException(
		            "Fee amount must be greater than zero"
		    );
		}
		
		long amountInPaise = fee.getFeeAmount()
		        .movePointRight(2)
		        .longValueExact();
		
		JSONObject orderRequest = new JSONObject();

		orderRequest.put("amount", amountInPaise);
		orderRequest.put("currency", "INR");
		orderRequest.put(
		        "receipt",
		        "fee_" + feeId + "_" + System.currentTimeMillis()
		);
		orderRequest.put("partial_payment", false);
		
		Order razorpayOrder;
		try {
		    razorpayOrder =
		            razorpayClient.orders.create(orderRequest);
		} catch (RazorpayException exception) {
		    throw new RuntimeException(
		            "Unable to create Razorpay payment order",
		            exception
		    );
		}
		
		String providerOrderId = razorpayOrder.get("id");
		if (providerOrderId == null
		        || providerOrderId.isBlank()) {
		    throw new IllegalStateException(
		            "Razorpay did not return an order ID"
		    );
		}
		
		Payment payment = new Payment();

		payment.setFee(fee);
		payment.setAmount(fee.getFeeAmount());
		payment.setCurrency("INR");
		payment.setProviderOrderId(providerOrderId);
		payment.setPaymentStatus(PaymentStatus.CREATED);

		payment.setProviderPaymentId(null);
		payment.setPaymentDate(null);
		payment.setPaymentMethod(null);
		payment.setFailureReason(null);

		LocalDateTime now = LocalDateTime.now();
		payment.setCreatedAt(now);
		payment.setUpdatedAt(now);
		Payment savedPayment = paymentRepository.save(payment);
		
		PaymentOrderRespnseDTO response =
		        new PaymentOrderRespnseDTO();

		response.setPaymentId(savedPayment.getPaymentId());
		response.setFeeId(fee.getFeeId());
		response.setProviderOrderId(providerOrderId);
		response.setAmountInPaise(amountInPaise);
		response.setCurrency("INR");
		response.setKeyId(razorpayConfig.getKeyId());

		return response;
	}
}
