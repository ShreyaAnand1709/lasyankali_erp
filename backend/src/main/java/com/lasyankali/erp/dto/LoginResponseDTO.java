package com.lasyankali.erp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
	private String token;
	private String username;
	private String role;
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public LoginResponseDTO(String token, String username, String role, String message) {
		super();
		this.token = token;
		this.username = username;
		this.role = role;
		this.message = message;
	}
	public LoginResponseDTO() {
		super();
	}
	
}
