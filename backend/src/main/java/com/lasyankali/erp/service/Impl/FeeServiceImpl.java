package com.lasyankali.erp.service.Impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.lasyankali.erp.entity.BatchStudent;
import com.lasyankali.erp.entity.FeeRate;
import com.lasyankali.erp.entity.enums.EnrollmentStatus;
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
	public void generateMonthlyFee(Long studentId, Long batchId, LocalDate billingMonth) {
		// TODO Auto-generated method stub
		if(studentId == null || batchId == null || billingMonth == null) {
			throw new IllegalArgumentException("Student, batch or the month cannot be null");
		}
		
		LocalDate normalizeBillingMonth = billingMonth.withDayOfMonth(1);
		BatchStudent batchStudent = batchStudRepo.findByBatch_BatchIdAndStudent_StudentId(batchId, studentId).
				orElseThrow(()->
				new RuntimeException("Student and the batch not found"));
		EnrollmentStatus status = batchStudent.getStatus();
		if(!(status == EnrollmentStatus.ACTIVE)) {
			return;
		}
		LocalDate enrollmentDate = batchStudent.getEnrollmentDate();
		if(enrollmentDate == null) {
			throw new IllegalArgumentException("Enrollment Date not found");
		}
		LocalDate firstChargeMonth = enrollmentDate.withDayOfMonth(1);
		if(enrollmentDate.getDayOfMonth() > 15) {
			firstChargeMonth = firstChargeMonth.plusMonths(1);
		} 
		if(normalizeBillingMonth.isBefore(firstChargeMonth)) {
			return;
		}
		
		boolean feeExists = feeRepo.existsByStudent_StudentIdAndBatch_BatchIdAndBillingMonth(studentId, batchId, normalizeBillingMonth);
		if(feeExists) {
			return; 
		} 
		FeeRate batchRate = feeRateRepo.findByBatchLevel(batchStudent.getBatch()
				.getLevel()).
				orElseThrow(
						()-> new RuntimeException("Batch doesnt exist"));
	}

}
