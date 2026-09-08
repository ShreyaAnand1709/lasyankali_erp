package com.lasyankali.erp.mapper;

import org.springframework.stereotype.Component;

import com.lasyankali.erp.dto.EventRegistrationDTO;
import com.lasyankali.erp.entity.EventRegister;

@Component
public class EventRegistrationMapper {
	public EventRegistrationDTO mapToEventRegister(EventRegister register) {
		EventRegistrationDTO registerEvent = new EventRegistrationDTO();
		registerEvent.setEventName(register.getEvent().getEventName());
		registerEvent.setStudentName(register.getStudent().
				getUser().
				getFirstName());
		registerEvent.setRegistrationDate(register.getRegistrationDate());
		registerEvent.setStatus(register.getParticipationStatus());
		return registerEvent;
		
	}
}
