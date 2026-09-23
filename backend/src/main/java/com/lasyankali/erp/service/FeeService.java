package com.lasyankali.erp.service;

import java.time.LocalDate;

public interface FeeService {
	boolean generateMonthlyFee(Long studentId, Long batchId, LocalDate billingMonth);
}
