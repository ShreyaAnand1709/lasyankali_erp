package com.lasyankali.erp.service;

import java.time.LocalDate;

public interface EmailService {
	public void sendMail(String receiverMail, String name, String userId, String password, String role);
	void sendEventRegistrationMail(String receiverMail, String name, String eventName, LocalDate eventDate);
}
