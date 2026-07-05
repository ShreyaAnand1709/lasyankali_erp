package com.lasyankali.erp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.LoginRequestDTO;
import com.lasyankali.erp.dto.LoginResponseDTO;
import com.lasyankali.erp.dto.RegisterUserDTO;
import com.lasyankali.erp.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authservice;

	public AuthController(AuthService authservice) {
		super();
		this.authservice = authservice;
	}
	// for registering 
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(
			@RequestBody RegisterUserDTO registerUserDTO) {
		authservice.registeredUser(registerUserDTO);
		return ResponseEntity.ok("Successfully registered");	
	}
	
	//for logging in as a user
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(
			@RequestBody LoginRequestDTO loginRequestDTO) {
		LoginResponseDTO response =authservice.loginResponseDTO(loginRequestDTO);
		return ResponseEntity.ok(response);
	}
}
