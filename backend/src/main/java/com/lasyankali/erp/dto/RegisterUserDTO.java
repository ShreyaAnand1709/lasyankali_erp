package com.lasyankali.erp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
	private String firstName;
	private String lastname;
	private String username;
	private String email;
	private String password;
	private String mobileNumber;
	private String roleName;
	
	public RegisterUserDTO() {
		super();
	}
	public RegisterUserDTO(String firstName, String lastname, String username, String email, String password,
			String mobileNumber, String roleName) {
		super();
		this.firstName = firstName;
		this.lastname = lastname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.mobileNumber = mobileNumber;
		this.roleName = roleName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
