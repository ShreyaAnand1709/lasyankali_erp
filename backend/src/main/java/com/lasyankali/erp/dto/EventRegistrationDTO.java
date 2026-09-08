package com.lasyankali.erp.dto;

import java.time.LocalDate;

import com.lasyankali.erp.entity.enums.ParticipationStatus;

public class EventRegistrationDTO {
	private String studentName;
	private String eventName;
	private LocalDate registrationDate;
	private ParticipationStatus status;
	public EventRegistrationDTO(String studentName, String eventName, LocalDate registrationDate,
			ParticipationStatus status) {
		super();
		this.studentName = studentName;
		this.eventName = eventName;
		this.registrationDate = registrationDate;
		this.status = status;
	}
	public EventRegistrationDTO() {
		super();
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public LocalDate getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}
	public ParticipationStatus getStatus() {
		return status;
	}
	public void setStatus(ParticipationStatus status) {
		this.status = status;
	}
}
