package com.lasyankali.erp.service.Impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
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

	@Override
	public void sendEventRegistrationMail(String receiverMail, String name, String eventName, LocalDate eventDate) {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
		String body = "Hello " + "\n" +
		name + " registered for the event " + eventName +
		" on " + eventDate; 
		message.setFrom(userName);
		message.setTo(receiverMail);
		message.setSubject("Registered for the event");
		message.setText(body);
		
		javaMailSender.send(message);
		
	}

	@Override
	public void sendPasswordResetMail(String receiverMail, String name, String resetLink) {
		// TODO Auto-generated method stub

    String displayName =
            name == null || name.isBlank()
                    ? "there"
                    : name;

    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(this.userName);
    message.setTo(receiverMail);
    message.setSubject("Reset your NrityaSutra password");

    message.setText(
            "Hello " + displayName + ",\n\n"
            + "We received a request to reset your "
            + "Lasyankali: NrityaSutra password.\n\n"
            + "Open this link to choose a new password:\n"
            + resetLink + "\n\n"
            + "This link expires in 15 minutes "
            + "and can only be used once.\n\n"
            + "If you did not request this reset, "
            + "you can ignore this email. "
            + "Your password has not been changed.\n\n"
            + "Lasyankali: NrityaSutra"
    );

    javaMailSender.send(message);
		
	}

}
