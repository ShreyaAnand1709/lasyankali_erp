package com.lasyankali.erp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.lasyankali.erp.entity.enums.ParticipationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="event_registrations")
public class EventRegister {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="event_registration_id")
	private Long eventRegistrationId;
	
	@ManyToOne
	@JoinColumn(name="event_id",nullable = false)
	private Event event;
	
	@ManyToOne
	@JoinColumn(name="student_id",nullable = false)
	private Student student;
	
	@Column(name="registration_date",nullable = false)
	private LocalDate registrationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name="participation_status")
	private ParticipationStatus participationStatus;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="created_at")
	private LocalDateTime createdAt;

	public EventRegister() {
	}

	public EventRegister(Long eventRegistrationId, Event event, Student student, LocalDate registrationDate,
			ParticipationStatus participationStatus, String remarks, LocalDateTime createdAt) {
		super();
		this.eventRegistrationId = eventRegistrationId;
		this.event = event;
		this.student = student;
		this.registrationDate = registrationDate;
		this.participationStatus = participationStatus;
		this.remarks = remarks;
		this.createdAt = createdAt;
	}

	public Long getEventRegistrationId() {
		return eventRegistrationId;
	}

	public void setEventRegistrationId(Long eventRegistrationId) {
		this.eventRegistrationId = eventRegistrationId;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public ParticipationStatus getParticipationStatus() {
		return participationStatus;
	}

	public void setParticipationStatus(ParticipationStatus participationStatus) {
		this.participationStatus = participationStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
