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
}
