package com.lasyankali.erp.service;

import com.lasyankali.erp.dto.EventRegistrationDTO;
import com.lasyankali.erp.dto.UpdateParticipationStatusDTO;

public interface EventRegistrationService {
	EventRegistrationDTO registerEvent(Long eventId, String authName);
	void deregisterEvent(Long eventId, String username);
	EventRegistrationDTO updateParticipationStatus(Long eventId, Long studentId, UpdateParticipationStatusDTO status);
}
