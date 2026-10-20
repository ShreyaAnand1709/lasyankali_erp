package com.lasyankali.erp.service.Impl;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.config.RazorpayConfig;
import com.lasyankali.erp.dto.PaymentOrderRespnseDTO;
import com.lasyankali.erp.dto.PaymentVerificationDTO;
import com.lasyankali.erp.dto.PaymentVerificationResponseDTO;
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

	@Override
	@Transactional
	public PaymentVerificationResponseDTO verifyPayment(
	        PaymentVerificationDTO verifyDTO,
	        String username) {

	    if (verifyDTO == null) {
	        throw new IllegalArgumentException(
	                "Payment verification details cannot be null"
	        );
	    }

	    if (username == null || username.isBlank()) {
	        throw new IllegalArgumentException(
	                "Username cannot be empty"
	        );
	    }

	    String providerOrderId =
	            verifyDTO.getProviderOrderId();

	    String providerPaymentId =
	            verifyDTO.getProviderPaymentId();

	    String providerSignature =
	            verifyDTO.getProviderSignature();

	    if (providerOrderId == null
	            || providerOrderId.isBlank()
	            || providerPaymentId == null
	            || providerPaymentId.isBlank()
	            || providerSignature == null
	            || providerSignature.isBlank()) {

	        throw new IllegalArgumentException(
	                "Payment verification values cannot be empty"
	        );
	    }

	    Payment existingPayment =
	            paymentRepository
	                    .findByProviderOrderId(providerOrderId)
	                    .orElseThrow(() ->
	                            new RuntimeException(
	                                    "Payment order does not exist"
	                            )
	                    );

	    Fee fee = existingPayment.getFee();

	    if (fee == null) {
	        throw new IllegalStateException(
	                "The payment is not connected to a fee"
	        );
	    }

	    boolean studentOwnsFee =
	            fee.getStudent() != null
	            && fee.getStudent().getUser() != null
	            && username.equals(
	                    fee.getStudent()
	                            .getUser()
	                            .getUserName()
	            );

	    boolean parentOwnsFee =
	            fee.getStudent() != null
	            && fee.getStudent().getParent() != null
	            && fee.getStudent()
	                    .getParent()
	                    .getUser() != null
	            && username.equals(
	                    fee.getStudent()
	                            .getParent()
	                            .getUser()
	                            .getUserName()
	            );

	    if (!studentOwnsFee && !parentOwnsFee) {
	        throw new AccessDeniedException(
	                "You are not allowed to verify this payment"
	        );
	    }

	    if (existingPayment.getPaymentStatus()
	            == PaymentStatus.SUCCESS) {

	        if (providerPaymentId.equals(
	                existingPayment.getProviderPaymentId())) {

	            return buildVerificationResponse(
	                    existingPayment,
	                    fee,
	                    "Payment was already verified"
	            );
	        }

	        throw new IllegalStateException(
	                "This payment order was completed "
	                + "using another payment"
	        );
	    }

	    if (existingPayment.getPaymentStatus()
	            == PaymentStatus.FAILED) {

	        throw new IllegalStateException(
	                "This payment attempt has already failed"
	        );
	    }

	    boolean anotherSuccessfulPaymentExists =
	            paymentRepository
	                    .existsByFee_FeeIdAndPaymentStatus(
	                            fee.getFeeId(),
	                            PaymentStatus.SUCCESS
	                    );

	    if (fee.getFeeStatus() == FeeStatus.PAID
	            || anotherSuccessfulPaymentExists) {

	        throw new IllegalStateException(
	                "This fee has already been paid"
	        );
	    }

	    Payment paymentUsingSameProviderId =
	            paymentRepository
	                    .findByProviderPaymentId(
	                            providerPaymentId
	                    )
	                    .orElse(null);

	    if (paymentUsingSameProviderId != null
	            && !paymentUsingSameProviderId
	                    .getPaymentId()
	                    .equals(existingPayment.getPaymentId())) {

	        throw new IllegalStateException(
	                "This provider payment ID "
	                + "has already been used"
	        );
	    }

	    boolean signatureIsValid;

	    try {
	        signatureIsValid =
	                razorpayConfig.verifyPaymentSignature(
	                        existingPayment
	                                .getProviderOrderId(),
	                        providerPaymentId,
	                        providerSignature
	                );
	    } catch (RazorpayException exception) {
	        throw new RuntimeException(
	                "Unable to verify the "
	                + "Razorpay payment signature",
	                exception
	        );
	    }

	    if (!signatureIsValid) {
	        throw new AccessDeniedException(
	                "Invalid payment signature"
	        );
	    }

	    com.razorpay.Payment razorpayPayment;

	    try {
	        razorpayPayment =
	                razorpayClient.payments.fetch(
	                        providerPaymentId
	                );
	    } catch (RazorpayException exception) {
	        throw new RuntimeException(
	                "Unable to fetch payment details "
	                + "from Razorpay",
	                exception
	        );
	    }

	    String fetchedPaymentId =
	            razorpayPayment.get("id");

	    String fetchedOrderId =
	            razorpayPayment.get("order_id");

	    Number fetchedAmount =
	            razorpayPayment.get("amount");

	    String fetchedCurrency =
	            razorpayPayment.get("currency");

	    String fetchedStatus =
	            razorpayPayment.get("status");

	    String fetchedMethod =
	            razorpayPayment.get("method");

	    long expectedAmountInPaise =
	            existingPayment.getAmount()
	                    .movePointRight(2)
	                    .longValueExact();

	    if (!providerPaymentId.equals(
	            fetchedPaymentId)) {

	        throw new IllegalStateException(
	                "Razorpay returned a different payment ID"
	        );
	    }

	    if (!existingPayment
	            .getProviderOrderId()
	            .equals(fetchedOrderId)) {

	        throw new IllegalStateException(
	                "The Razorpay payment does not "
	                + "belong to this order"
	        );
	    }

	    if (fetchedAmount == null
	            || fetchedAmount.longValue()
	                    != expectedAmountInPaise) {

	        throw new IllegalStateException(
	                "The Razorpay payment amount does not match"
	        );
	    }

	    if (existingPayment.getCurrency() == null
	            || fetchedCurrency == null
	            || !existingPayment
	                    .getCurrency()
	                    .equalsIgnoreCase(fetchedCurrency)) {

	        throw new IllegalStateException(
	                "The Razorpay payment currency does not match"
	        );
	    }

	    if (!"captured".equalsIgnoreCase(
	            fetchedStatus)) {

	        throw new IllegalStateException(
	                "The Razorpay payment has not been captured"
	        );
	    }

	    LocalDateTime now = LocalDateTime.now();

	    existingPayment.setProviderPaymentId(
	            providerPaymentId
	    );

	    existingPayment.setPaymentMethod(
	            fetchedMethod
	    );

	    existingPayment.setPaymentDate(now);

	    existingPayment.setPaymentStatus(
	            PaymentStatus.SUCCESS
	    );

	    existingPayment.setFailureReason(null);
	    existingPayment.setUpdatedAt(now);

	    fee.setFeeStatus(FeeStatus.PAID);
	    fee.setUpdatedAt(now);

	    Payment savedPayment =
	            paymentRepository.save(
	                    existingPayment
	            );

	    feeRepository.save(fee);

	    return buildVerificationResponse(
	            savedPayment,
	            fee,
	            "Payment verified successfully"
	    );
	}

	private PaymentVerificationResponseDTO
    buildVerificationResponse(
            Payment payment,
            Fee fee,
            String message) {

			PaymentVerificationResponseDTO response =
			        new PaymentVerificationResponseDTO();
			
			response.setPaymentId(
			        payment.getPaymentId()
			);
			
			response.setFeeId(
			        fee.getFeeId()
			);
			
			response.setProviderOrderId(
			        payment.getProviderOrderId()
			);
			
			response.setProviderPaymentId(
			        payment.getProviderPaymentId()
			);
			
			response.setPaymentStatus(
			        payment.getPaymentStatus()
			);
			
			response.setFeeStatus(
			        fee.getFeeStatus()
			);
			
			response.setMessage(message);
			
			return response;
			}
			}
