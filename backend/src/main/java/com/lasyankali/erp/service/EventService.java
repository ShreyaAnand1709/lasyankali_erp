package com.lasyankali.erp.service;

import java.time.LocalDate;
import java.util.List;

import com.lasyankali.erp.dto.CreateEventDTO;
import com.lasyankali.erp.dto.EventResponseDTO;
import com.lasyankali.erp.dto.UpdateEventDTO;

public interface EventService {
	void createEvent(CreateEventDTO createEventDTO);
	List<EventResponseDTO> getAllEvents();
	EventResponseDTO getEventById(Long eventId);
	EventResponseDTO updateEventById(Long eventId, UpdateEventDTO
			updateEventDTO); 
	void deleteEvent(Long eventId);
	List<EventResponseDTO> searchEvent(String keyword);
	List<EventResponseDTO> getEventByDate(LocalDate date);
}
