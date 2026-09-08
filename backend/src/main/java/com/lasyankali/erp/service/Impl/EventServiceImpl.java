package com.lasyankali.erp.service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lasyankali.erp.dto.CreateEventDTO;
import com.lasyankali.erp.dto.EventResponseDTO;
import com.lasyankali.erp.dto.UpdateEventDTO;
import com.lasyankali.erp.entity.Event;
import com.lasyankali.erp.entity.enums.EventStatus;
import com.lasyankali.erp.mapper.EventMapper;
import com.lasyankali.erp.repository.EventRepository;
import com.lasyankali.erp.service.EventService;

@Service
public class EventServiceImpl implements EventService{
	private final EventRepository eventRepository;
	private final EventMapper eventMapper;
	
	public EventServiceImpl(EventRepository eventRepository,EventMapper eventMapper) {
		super();
		this.eventRepository = eventRepository;
		this.eventMapper = eventMapper;
	}

	@Override
	public void createEvent(CreateEventDTO createEventDTO) {
		// TODO Auto-generated method stub
		if(eventRepository.existsByEventCode(createEventDTO.getEventCode())) {
			throw new RuntimeException("Event Code already exists");
		}
		Event event = new Event();
		event.setEventCode(createEventDTO.getEventCode());
		event.setEventName(createEventDTO.getEventName());
		event.setEventType(createEventDTO.getEventType());
		event.setEventDate(createEventDTO.getEventDate());
		event.setVenue(createEventDTO.getVenue());
		event.setDescription(createEventDTO.getDescription());
		event.setStatus(EventStatus.PLANNED);
		event.setCreatedAt(LocalDate.now());
		event.setUpdatedAt(LocalDate.now());
		eventRepository.save(event);
	}

	@Override
	public List<EventResponseDTO> getAllEvents() {
		List<Event> events = eventRepository.findAll();
		List<EventResponseDTO> response = new ArrayList<>();
		for(Event oneEvent : events) {
			EventResponseDTO eachEvent = eventMapper.eventMapper(oneEvent);
			response.add(eachEvent);
		}
		return response;
	}

	@Override
	public EventResponseDTO getEventById(Long eventId) {
		// TODO Auto-generated method stub
		Event event = eventRepository.findById(eventId).
				orElseThrow(
						()-> new RuntimeException("Event doesnt not exists"));
		EventResponseDTO response = eventMapper.eventMapper(event);
		
		return response;
	}

	@Override
	public EventResponseDTO updateEventById(Long eventId, UpdateEventDTO updateEventDTO) {
		Event event = eventRepository.findById(eventId).
				orElseThrow(
						() -> new RuntimeException("Event doesnt exists"));
		event.setEventName(updateEventDTO.getEventName());
		event.setEventType(updateEventDTO.getEventType());
		event.setEventDate(updateEventDTO.getEventDate());
		event.setVenue(updateEventDTO.getVenue());
		event.setDescription(updateEventDTO.getDescription());
		event.setStatus(updateEventDTO.getStatus());
		event.setUpdatedAt(LocalDate.now());
		eventRepository.save(event);
		EventResponseDTO response = eventMapper.eventMapper(event);
		return response;
	}

	@Override
	public void deleteEvent(Long eventId) {
		// TODO Auto-generated method stub
		Event event = eventRepository.findById(eventId).
				orElseThrow(
						()-> new RuntimeException("Event doesnt exist"));
		event.setStatus(EventStatus.CANCELLED);
		event.setUpdatedAt(LocalDate.now());
		eventRepository.save(event);
	}

	@Override
	public List<EventResponseDTO> searchEvent(String keyword) {
		List<Event> eventList = eventRepository.searchByNameOrVenue(keyword);
		List<EventResponseDTO> response = new ArrayList<>();
		for(Event oneEvent : eventList) {
			EventResponseDTO eachEvent = eventMapper.eventMapper(oneEvent);
			response.add(eachEvent);
		}
		return response;
	}

	@Override
	public List<EventResponseDTO> getEventByDate(LocalDate date) {
		// TODO Auto-generated method stub
		List<Event> eventList = eventRepository.findByEventDate(date);
		List<EventResponseDTO> response = new ArrayList<>();
		for(Event oneEvent : eventList) {
			EventResponseDTO eachEvent = eventMapper.eventMapper(oneEvent);
			response.add(eachEvent);
		}
		return response;
	}

}
