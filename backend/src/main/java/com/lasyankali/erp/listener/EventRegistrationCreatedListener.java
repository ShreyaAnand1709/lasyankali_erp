package com.lasyankali.erp.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import com.lasyankali.erp.events.EventRegistrationCreatedEvent;
import com.lasyankali.erp.service.EmailService;

@Component
public class EventRegistrationCreatedListener {
	private final EmailService emailService;

	public EventRegistrationCreatedListener(EmailService emailService) {
		super();
		this.emailService = emailService;
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void eventRegisterHandler(EventRegistrationCreatedEvent event) {
	    emailService.sendEventRegistrationMail(
	            event.getReceiverMail(),
	            event.getName(),
	            event.getEventName(),
	            event.getEventDate());
	}
}
