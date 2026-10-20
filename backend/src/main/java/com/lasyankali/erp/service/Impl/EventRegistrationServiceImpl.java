package com.lasyankali.erp.service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.dto.EventRegistrationDTO;
import com.lasyankali.erp.dto.UpdateParticipationStatusDTO;
import com.lasyankali.erp.entity.Event;
import com.lasyankali.erp.entity.EventRegister;
import com.lasyankali.erp.entity.Student;
import com.lasyankali.erp.entity.enums.ParticipationStatus;
import com.lasyankali.erp.events.EventRegistrationCreatedEvent;
import com.lasyankali.erp.mapper.EventRegistrationMapper;
import com.lasyankali.erp.repository.EventRegistrationRepository;
import com.lasyankali.erp.repository.EventRepository;
import com.lasyankali.erp.repository.StudentRepository;
import com.lasyankali.erp.service.EventRegistrationService;

import jakarta.transaction.Transactional;

@Service
public class EventRegistrationServiceImpl implements EventRegistrationService{
	private final EventRepository eventRepository;
	private final StudentRepository studentRepository;
	private final EventRegistrationRepository eventRegRepo;
	private final EventRegistrationMapper eventRegMapper;
	private final ApplicationEventPublisher publisher;
	
	public EventRegistrationServiceImpl(EventRepository eventRepository, StudentRepository studentRepository,
			EventRegistrationRepository eventRegRepo,EventRegistrationMapper eventRegMapper, ApplicationEventPublisher publisher) {
		super();
		this.eventRepository = eventRepository;
		this.studentRepository = studentRepository;
		this.eventRegRepo = eventRegRepo;
		this.eventRegMapper=eventRegMapper;
		this.publisher=publisher;
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('STUDENT')")
	public EventRegistrationDTO registerEvent(Long eventId, String username) {
		// TODO Auto-generated method stub
		Event event = eventRepository.findById(eventId).
				orElseThrow( () -> 
						new RuntimeException("Event doesnt exist"));
		Student student = studentRepository.findByUser_UserName(username).
				orElseThrow(
						()-> new RuntimeException("Student doesnt exist"));
		Long studentId = student.getStudentId();
		if(eventRegRepo.existsByEvent_EventIdAndStudent_StudentId(eventId, studentId)) {
			throw new RuntimeException("Student is already registered for this event");
		}
		EventRegister register = new EventRegister();
		register.setEvent(event);
		register.setStudent(student);
		register.setParticipationStatus(ParticipationStatus.REGISTERED);
		register.setRegistrationDate(LocalDate.now());
		register.setCreatedAt(LocalDateTime.now());
		EventRegister eventRegister = eventRegRepo.save(register);
		EventRegistrationCreatedEvent registrationCreatedEvent =
		        new EventRegistrationCreatedEvent(
		                student.getUser().getFirstName(),
		                student.getUser().getEmail(),
		                event.getEventName(),
		                event.getEventDate(),
		                event.getVenue());
		publisher.publishEvent(registrationCreatedEvent);
		EventRegistrationDTO eventReg = eventRegMapper.mapToEventRegister(eventRegister);
		return eventReg;
	}

	@Override
	@PreAuthorize("hasRole('STUDENT')")
	public void deregisterEvent(Long eventId, String username) {
		Student student = studentRepository.findByUser_UserName(username).
				orElseThrow(
						()-> new RuntimeException("Student doesnt exist"));
		Long studentId = student.getStudentId();
		// TODO Auto-generated method stub
		EventRegister register = eventRegRepo.findByEvent_EventIdAndStudent_StudentId(eventId, studentId).
				orElseThrow(()
						-> new RuntimeException("Student and the event doesnt exist"));
		register.setParticipationStatus(ParticipationStatus.ABSENT);
		eventRegRepo.save(register);
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public EventRegistrationDTO updateParticipationStatus(Long eventId, Long studentId, UpdateParticipationStatusDTO status) {
		// TODO Auto-generated method stub
		if (eventId == null || studentId == null) {
	        throw new org.springframework.web.server.ResponseStatusException(
	                org.springframework.http.HttpStatus.BAD_REQUEST,
	                "Event ID and student ID are required");
	    }

	    if (status == null || status.getStatus() == null) {
	        throw new org.springframework.web.server.ResponseStatusException(
	                org.springframework.http.HttpStatus.BAD_REQUEST,
	                "Participation status is required");
	    }

	    EventRegister register = eventRegRepo
	            .findByEvent_EventIdAndStudent_StudentId(eventId, studentId)
	            .orElseThrow(() ->
	                    new org.springframework.web.server.ResponseStatusException(
	                            org.springframework.http.HttpStatus.NOT_FOUND,
	                            "Event registration not found"));

	    register.setParticipationStatus(status.getStatus());

	    EventRegister savedRegister = eventRegRepo.save(register);

	    return eventRegMapper.mapToEventRegister(savedRegister);
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public List<EventRegistrationDTO> getParticipants(Long eventId) {
		// TODO Auto-generated method stub
		if (eventId == null || eventId <= 0) {
	        throw new org.springframework.web.server.ResponseStatusException(
	                org.springframework.http.HttpStatus.BAD_REQUEST,
	                "A valid event ID is required");
	    }

	    if (!eventRepository.existsById(eventId)) {
	        throw new org.springframework.web.server.ResponseStatusException(
	                org.springframework.http.HttpStatus.NOT_FOUND,
	                "Event not found");
	    }

	    return eventRegRepo.findByEvent_EventId(eventId)
	            .stream()
	            .map(eventRegMapper::mapToEventRegister)
	            .toList();
	}

}
