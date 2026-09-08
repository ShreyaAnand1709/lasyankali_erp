package com.lasyankali.erp.service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.lasyankali.erp.dto.EventRegistrationDTO;
import com.lasyankali.erp.dto.UpdateParticipationStatusDTO;
import com.lasyankali.erp.entity.Event;
import com.lasyankali.erp.entity.EventRegister;
import com.lasyankali.erp.entity.Student;
import com.lasyankali.erp.entity.enums.ParticipationStatus;
import com.lasyankali.erp.mapper.EventRegistrationMapper;
import com.lasyankali.erp.repository.EventRegistrationRepository;
import com.lasyankali.erp.repository.EventRepository;
import com.lasyankali.erp.repository.StudentRepository;
import com.lasyankali.erp.service.EventRegistrationService;

@Service
public class EventRegistrationServiceImpl implements EventRegistrationService{
	private final EventRepository eventRepository;
	private final StudentRepository studentRepository;
	private final EventRegistrationRepository eventRegRepo;
	private final EventRegistrationMapper eventRegMapper;
	
	public EventRegistrationServiceImpl(EventRepository eventRepository, StudentRepository studentRepository,
			EventRegistrationRepository eventRegRepo,EventRegistrationMapper eventRegMapper) {
		super();
		this.eventRepository = eventRepository;
		this.studentRepository = studentRepository;
		this.eventRegRepo = eventRegRepo;
		this.eventRegMapper=eventRegMapper;
	}

	@Override
	public EventRegistrationDTO registerEvent(Long eventId, Long studentId) {
		// TODO Auto-generated method stub
		Event event = eventRepository.findById(eventId).
				orElseThrow( () -> 
						new RuntimeException("Event doesnt exist"));
		Student student = studentRepository.findById(studentId).
				orElseThrow(
						()-> new RuntimeException("Student doesnt exist"));
		
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
		EventRegistrationDTO eventReg = eventRegMapper.mapToEventRegister(eventRegister);
		return eventReg;
	}

	@Override
	public void deregisterEvent(Long eventId, Long studentId) {
		// TODO Auto-generated method stub
		Event event = eventRepository.findById(eventId).
				orElseThrow( () -> 
						new RuntimeException("Event doesnt exist"));
		Student student = studentRepository.findById(studentId).
				orElseThrow(
						()-> new RuntimeException("Student doesnt exist"));
		EventRegister register = eventRegRepo.findByEvent_EventIdAndStudent_StudentId(eventId, studentId).
				orElseThrow(()
						-> new RuntimeException("Student and the event doesnt exist"));
		register.setParticipationStatus(ParticipationStatus.ABSENT);
		eventRegRepo.save(register);
	}

	@Override
	public EventRegistrationDTO updateParticipationStatus(Long eventId, Long studentId, UpdateParticipationStatusDTO status) {
		// TODO Auto-generated method stub
		Event event = eventRepository.findById(eventId).
				orElseThrow( () -> 
						new RuntimeException("Event doesnt exist"));
		Student student = studentRepository.findById(studentId).
				orElseThrow(
						()-> new RuntimeException("Student doesnt exist"));
		EventRegister register = eventRegRepo.findByEvent_EventIdAndStudent_StudentId(eventId, studentId).
				orElseThrow(()
						-> new RuntimeException("Student and the event doesnt exist"));
		register.setParticipationStatus(status.getStatus());
		eventRegRepo.save(register);
		EventRegistrationDTO response = eventRegMapper.mapToEventRegister(register);
		return response;
	}

}
