package com.lasyankali.erp.service;

public interface EmailService {
	public void sendMail(String receiverMail, String name, String userId, String password, String role);
}
