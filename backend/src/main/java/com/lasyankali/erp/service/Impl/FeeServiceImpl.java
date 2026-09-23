package com.lasyankali.erp.service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.lasyankali.erp.entity.BatchStudent;
import com.lasyankali.erp.entity.Fee;
import com.lasyankali.erp.entity.FeeRate;
import com.lasyankali.erp.entity.enums.EnrollmentStatus;
import com.lasyankali.erp.entity.enums.FeeStatus;
import com.lasyankali.erp.repository.BatchStudentRepository;
import com.lasyankali.erp.repository.FeeRateRepository;
import com.lasyankali.erp.repository.FeeRepository;
import com.lasyankali.erp.service.FeeService;

import jakarta.transaction.Transactional;

@Service
public class FeeServiceImpl implements FeeService{
	private final BatchStudentRepository batchStudRepo;
	private final FeeRepository feeRepo;
	private final FeeRateRepository feeRateRepo;
	
	public FeeServiceImpl(BatchStudentRepository batchStudRepo, FeeRepository feeRepo, FeeRateRepository feeRateRepo) {
		super();
		this.batchStudRepo = batchStudRepo;
		this.feeRepo = feeRepo;
		this.feeRateRepo = feeRateRepo;
	}

	@Override
	@Transactional
	public boolean generateMonthlyFee(Long studentId, Long batchId, LocalDate billingMonth) {
		// TODO Auto-generated method stub
		if(studentId == null || batchId == null || billingMonth == null) {
			throw new IllegalArgumentException("Student, batch or the month cannot be null");
		}
		
		LocalDate normalizeBillingMonth = billingMonth.withDayOfMonth(1);
		BatchStudent batchStudent = batchStudRepo.findByBatch_BatchIdAndStudent_StudentId(batchId, studentId).
				orElseThrow(()->
				new RuntimeException("Student and the batch not found"));
		EnrollmentStatus status = batchStudent.getStatus();
		if(status != EnrollmentStatus.ACTIVE) {
			return false;
		}
		LocalDate enrollmentDate = batchStudent.getEnrollmentDate();
		if(enrollmentDate == null) {
			throw new IllegalArgumentException("Enrollment Date is missing");
		}
		LocalDate firstChargeMonth = enrollmentDate.withDayOfMonth(1);
		if(enrollmentDate.getDayOfMonth() > 15) {
			firstChargeMonth = firstChargeMonth.plusMonths(1);
		} 
		if(normalizeBillingMonth.isBefore(firstChargeMonth)) {
			return false;
		}
		
		boolean feeExists = feeRepo.existsByStudent_StudentIdAndBatch_BatchIdAndBillingMonth(studentId, batchId, normalizeBillingMonth);
		if(feeExists) {
			return false; 
		} 
		FeeRate batchRate = feeRateRepo.findByBatchLevel(batchStudent.getBatch()
				.getLevel()).
				orElseThrow(
						()-> new RuntimeException("Fee rate is not configured for this batch"));
		LocalDate dueDate = normalizeBillingMonth.withDayOfMonth(15);
		Fee feeEntity = new Fee();
		feeEntity.setStudent(batchStudent.getStudent());
		feeEntity.setBatch(batchStudent.getBatch());
		feeEntity.setFeeAmount(batchRate.getMonthlyAmount());
		feeEntity.setBillingMonth(normalizeBillingMonth);
		feeEntity.setDueDate(dueDate);
		feeEntity.setFeeStatus(FeeStatus.PENDING);
		feeEntity.setRemarks(null);
		feeEntity.setCreatedAt(LocalDateTime.now());
		feeEntity.setUpdatedAt(LocalDateTime.now());
		feeRepo.save(feeEntity);
		return true;
	}

}
