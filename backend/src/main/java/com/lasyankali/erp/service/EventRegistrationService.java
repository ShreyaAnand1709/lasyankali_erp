package com.lasyankali.erp.service;

import com.lasyankali.erp.dto.EventRegistrationDTO;
import com.lasyankali.erp.dto.UpdateParticipationStatusDTO;
import com.lasyankali.erp.entity.enums.ParticipationStatus;

public interface EventRegistrationService {
	EventRegistrationDTO registerEvent(Long eventId, Long studentId);
	void deregisterEvent(Long eventId, Long studentId);
	EventRegistrationDTO updateParticipationStatus(Long eventId, Long studentId, UpdateParticipationStatusDTO status);
}
