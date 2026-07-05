package com.lasyankali.erp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor 
@NoArgsConstructor
@Builder
public class LoginRequestDTO {
	private String username;
	private String password;
	public LoginRequestDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public LoginRequestDTO() {
		super();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
