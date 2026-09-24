package com.lasyankali.erp.mapper;

import org.springframework.stereotype.Component;

import com.lasyankali.erp.dto.FeeResponseDTO;
import com.lasyankali.erp.entity.Fee;

@Component
public class FeeMapper {
	public FeeResponseDTO mapToFeeResponse(Fee fee) {
		FeeResponseDTO feeResponse = new FeeResponseDTO();
		feeResponse.setFeeId(fee.getFeeId());
		feeResponse.setStudentId(fee.getStudent().getStudentId());
		feeResponse.setStudentName(fee.getStudent().getUser().getFirstName());
		feeResponse.setBatchId(fee.getBatch().getBatchId());
		feeResponse.setBatchName(fee.getBatch().getBatchName());
		feeResponse.setBatchLevel(fee.getBatch().getLevel());
		feeResponse.setDueDate(fee.getDueDate());
		feeResponse.setFeeStatus(fee.getFeeStatus());
		feeResponse.setRemarks(fee.getRemarks());
		feeResponse.setFeeAmount(fee.getFeeAmount());
		feeResponse.setBillingMonth(fee.getBillingMonth());
		return feeResponse;
	}
}
