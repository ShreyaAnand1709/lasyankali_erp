package com.lasyankali.erp.dto;

import com.lasyankali.erp.entity.enums.ParticipationStatus;

public class UpdateParticipationStatusDTO {
	private ParticipationStatus status;

	public UpdateParticipationStatusDTO(ParticipationStatus status) {
		super();
		this.status = status;
	}

	public ParticipationStatus getStatus() {
		return status;
	}

	public void setStatus(ParticipationStatus status) {
		this.status = status;
	}

	public UpdateParticipationStatusDTO() {
		super();
	}
	
}
