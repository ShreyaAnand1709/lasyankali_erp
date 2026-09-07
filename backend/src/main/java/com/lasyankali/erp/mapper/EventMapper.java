package com.lasyankali.erp.mapper;

import org.springframework.stereotype.Component;

import com.lasyankali.erp.dto.EventResponseDTO;
import com.lasyankali.erp.entity.Event;

@Component
public class EventMapper {
	public EventResponseDTO eventMapper(Event event) {
		EventResponseDTO response = new EventResponseDTO();
		response.setEventId(event.getEventId());
		response.setEventCode(event.getEventCode());
		response.setEventName(event.getEventName());
		response.setEventType(event.getEventType());
		response.setEventDate(event.getEventDate());
		response.setVenue(event.getVenue());
		response.setDescription(event.getDescription());
		response.setStatus(event.getStatus());
		return response;
		
	}
}
