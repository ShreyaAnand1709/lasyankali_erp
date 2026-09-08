package com.lasyankali.erp.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}$")
	private String userName;
	
	public EmailServiceImpl(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendMail(String receiverMail, String name, String userName, String password, String role) {
		SimpleMailMessage message = new SimpleMailMessage();
		String body = "welcome to Lasyankali " + name + "\n" + 
						"Your user Id is " + userName + "\n" +
				"Your password is " + password + "\n" +
						"Role: " + role + "\n" +
						"Shreya Anand";
		message.setFrom(userName);
		message.setTo(receiverMail);
		message.setSubject("Welcome to Lasyankali");
		message.setText(body);
		
		javaMailSender.send(message);
		
	}

}
