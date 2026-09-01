package com.lasyankali.erp.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.lasyankali.erp.events.AccountCreationEvent;
import com.lasyankali.erp.service.EmailService;

@Component
public class AccountCreatedListener {
	private final EmailService emailService;

	public AccountCreatedListener(EmailService emailService) {
		super();
		this.emailService = emailService;
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleAccountCreation(AccountCreationEvent event) {
		emailService.sendMail(event.getReceiverMail(),
				event.getName(), event.getUsername(), 
				event.getPassword(), event.getRole());
	}
}
