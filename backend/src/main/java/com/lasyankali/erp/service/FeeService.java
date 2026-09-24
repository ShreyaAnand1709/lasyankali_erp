package com.lasyankali.erp.service;

import java.time.LocalDate;
import java.util.List;

import com.lasyankali.erp.dto.FeeResponseDTO;

public interface FeeService {
	boolean generateMonthlyFee(Long studentId, Long batchId, LocalDate billingMonth);
	int generateMonthlyFees(LocalDate billingMonth);
	int markOverDueFees(LocalDate currentDate);
	List<FeeResponseDTO> getMyFees(String username);
	List<FeeResponseDTO> getAllFees();
	List<FeeResponseDTO> getMyChildrenFees(String parentUsername);
}
