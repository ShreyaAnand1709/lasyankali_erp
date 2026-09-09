package com.lasyankali.erp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.lasyankali.erp.entity.enums.EventStatus;
import com.lasyankali.erp.entity.enums.EventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
	private Long eventId;
	
	@Column(name="event_code", unique = true, nullable = false)
	private String eventCode;
	
	@Column(name="event_name")
	private String eventName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="event_type")
	private EventType eventType;
	
	@Column(name="event_date")
	private LocalDate eventDate;
	
	@Column(name="venue")
	private String venue;
	
	@Column(name="description")
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private EventStatus status; 
	
	@Column(name="created_at")
	private LocalDateTime createdAt;
	
	@Column(name="updated_at")
	private LocalDateTime updatedAt;

	public Event(Long eventId, String eventCode, String eventName, EventType eventType, LocalDate eventDate, String venue,
			String description, EventStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.eventId = eventId;
		this.eventCode = eventCode;
		this.eventName = eventName;
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.venue = venue;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Event() {
		super();
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
