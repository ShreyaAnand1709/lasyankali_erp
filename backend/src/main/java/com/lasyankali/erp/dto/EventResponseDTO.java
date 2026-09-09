package com.lasyankali.erp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.lasyankali.erp.entity.enums.EventStatus;
import com.lasyankali.erp.entity.enums.EventType;

public class EventResponseDTO {
	private Long eventId;
	private String eventCode;
	private String eventName;
	private EventType eventType;
	private LocalDate eventDate;
	private String venue;
	private String description;
	private EventStatus status;
	public EventResponseDTO(Long eventId,String eventCode, String eventName, EventType eventType, LocalDate eventDate, String venue,
			String description, EventStatus status) {
		super();
		this.eventId = eventId;
		this.eventCode = eventCode;
		this.eventName = eventName;
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.venue = venue;
		this.description = description;
		this.status = status;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public EventResponseDTO() {
		super();
	}
	public String getEventCode() {
		return eventCode;
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
