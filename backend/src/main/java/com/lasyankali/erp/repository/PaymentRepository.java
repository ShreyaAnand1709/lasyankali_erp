package com.lasyankali.erp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.Payment;
import com.lasyankali.erp.entity.enums.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	Optional<Payment> findByProviderOrderId(String providerOrderId);
	Optional<Payment> findByProviderPaymentId(String providerPaymentId);
	List<Payment> findByFee_FeeIdOrderByCreatedAtDesc(Long feeId);
	boolean existsByFee_FeeIdAndPaymentStatus(
	        Long feeId,
	        PaymentStatus paymentStatus
	);
}
