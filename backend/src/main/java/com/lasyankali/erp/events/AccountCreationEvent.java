package com.lasyankali.erp.events;

public class AccountCreationEvent {
	private String receiverMail;
	private String name;
	private String username;
	private String password;
	private String role;
	public String getReceiverMail() {
		return receiverMail;
	}
	public String getName() {
		return name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getRole() {
		return role;
	}
	
	public AccountCreationEvent() {
		super();
	}
	public AccountCreationEvent(String receiverMail, String name, String username, String password, String role) {
		super();
		this.receiverMail = receiverMail;
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
	}
}
