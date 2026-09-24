package com.lasyankali.erp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasyankali.erp.entity.Fee;
import com.lasyankali.erp.entity.enums.FeeStatus;

public interface FeeRepository extends JpaRepository<Fee,Long>{
	boolean existsByStudent_StudentIdAndBatch_BatchIdAndBillingMonth(
	        Long studentId,
	        Long batchId,
	        LocalDate billingMonth);
	List<Fee> findByFeeStatusAndDueDateBefore(FeeStatus feeStatus, LocalDate currentDate);
	List<Fee> findByStudent_User_UserNameOrderByBillingMonthDesc(
	        String username
	);
	List<Fee> findAllByOrderByBillingMonthDesc();
	List<Fee> findByStudent_Parent_User_UserNameOrderByBillingMonthDesc(
	        String username
	);
}
