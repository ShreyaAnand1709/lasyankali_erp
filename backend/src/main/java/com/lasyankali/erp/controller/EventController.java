package com.lasyankali.erp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lasyankali.erp.dto.CreateEventDTO;
import com.lasyankali.erp.dto.EventRegistrationDTO;
import com.lasyankali.erp.dto.EventResponseDTO;
import com.lasyankali.erp.dto.UpdateEventDTO;
import com.lasyankali.erp.dto.UpdateParticipationStatusDTO;
import com.lasyankali.erp.service.EventRegistrationService;
import com.lasyankali.erp.service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {
	private final EventService eventService;
	private final EventRegistrationService eventRegService;
	public EventController(EventService eventService, EventRegistrationService eventRegService) {
		super();
		this.eventService = eventService;
		this.eventRegService = eventRegService;
	}
	
	//creating an event 
	@PostMapping
	public ResponseEntity<String> createEvent(@RequestBody CreateEventDTO createEventDto) {
		eventService.createEvent(createEventDto);
		return ResponseEntity.ok("Event has been created");
	}
	
	@GetMapping("/{eventId}")
	public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long eventId) {
		EventResponseDTO response = eventService.getEventById(eventId);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<List<EventResponseDTO>> getEvents() {
		List<EventResponseDTO> response = eventService.getAllEvents();
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{eventId}")
	public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long eventId,@RequestBody UpdateEventDTO updateEventDTO) {
		EventResponseDTO response = eventService.updateEventById(eventId, updateEventDTO);
		return ResponseEntity.ok(response);	
	}
	
	@DeleteMapping("/{eventId}")
	public ResponseEntity deleteEvent(@PathVariable Long eventId) {
		eventService.deleteEvent(eventId);
		return ResponseEntity.ok("Event is deleted");
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<EventResponseDTO>> searchBatches(
	        @RequestParam String keyword) {
	    List<EventResponseDTO> response = eventService.searchEvent(keyword);
	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("{eventId}/student/{studentId}")
	public ResponseEntity<EventRegistrationDTO> registerForEvent(@PathVariable Long eventId, @PathVariable Long studentId) {
		EventRegistrationDTO response = eventRegService.registerEvent(eventId, studentId);
		return ResponseEntity.ok(response);
		
	}
	
	@DeleteMapping("{eventId}/student/{studentId}")
	public ResponseEntity deRegisterForEvent(@PathVariable Long eventId, @PathVariable Long studentId) {
		eventRegService.deregisterEvent(eventId, studentId);
		return ResponseEntity.ok("De registered");
	}
	
	@PatchMapping("{eventId}/student/{studentId}")
	public ResponseEntity<EventRegistrationDTO> updateParticipation(@PathVariable Long eventId, @PathVariable Long studentId,@RequestBody UpdateParticipationStatusDTO status) {
		EventRegistrationDTO response = eventRegService.updateParticipationStatus(eventId, studentId, status);	
		return ResponseEntity.ok(response);
	}

}
