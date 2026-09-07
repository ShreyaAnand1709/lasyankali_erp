package com.lasyankali.erp.dto;

import java.time.LocalDate;

import com.lasyankali.erp.entity.enums.EventStatus;
import com.lasyankali.erp.entity.enums.EventType;

public class UpdateEventDTO {
	private String eventName;
	private EventType eventType;
	private LocalDate eventDate;
	private String venue;
	private String description;
	private EventStatus status;
	public UpdateEventDTO(String eventName, EventType eventType, LocalDate eventDate, String venue, String description,
			EventStatus status) {
		super();
		this.eventName = eventName;
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.venue = venue;
		this.description = description;
		this.status = status;
	}
	public UpdateEventDTO() {
		super();
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public LocalDate getEventDate() {
		return eventDate;
	}
	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public EventStatus getStatus() {
		return status;
	}
	public void setStatus(EventStatus status) {
		this.status = status;
	}
	
}
