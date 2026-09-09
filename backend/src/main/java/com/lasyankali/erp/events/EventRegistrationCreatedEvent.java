package com.lasyankali.erp.events;

import java.time.LocalDate;

public class EventRegistrationCreatedEvent {
	private String name;
	private String receiverMail;
	private String eventName;
	private LocalDate eventDate;
	private String venue;
	public EventRegistrationCreatedEvent(String name, String receiverMail, String eventName, LocalDate eventDate,
			String venue) {
		super();
		this.name = name;
		this.receiverMail = receiverMail;
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.venue = venue;
	}
	public EventRegistrationCreatedEvent() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReceiverMail() {
		return receiverMail;
	}
	public void setReceiverMail(String receiverMail) {
		this.receiverMail = receiverMail;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
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
}
