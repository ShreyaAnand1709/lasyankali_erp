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
}
