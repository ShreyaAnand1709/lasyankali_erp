package com.lasyankali.erp.service.Impl;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.dto.LoginRequestDTO;
import com.lasyankali.erp.dto.LoginResponseDTO;
import com.lasyankali.erp.dto.RegisterUserDTO;
import com.lasyankali.erp.entity.Role;
import com.lasyankali.erp.entity.User;
import com.lasyankali.erp.repository.RoleRepository;
import com.lasyankali.erp.repository.UserRepository;
import com.lasyankali.erp.security.JwtService;
import com.lasyankali.erp.service.*;

@Service
public class AuthServiceImpl implements AuthService{
	private final UserRepository userRepository;
	private final RoleRepository roleRepository; 
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,JwtService jwtService) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@Override
	public void registeredUser(RegisterUserDTO registerUserDTO) {
		Optional<User> existingUsername = userRepository.findByUserName(
				registerUserDTO.getUsername());
		if(existingUsername.isPresent()) {
			throw new RuntimeException("Username already exists");
		}
		
		Optional<User> exisitngEmail = userRepository.findByEmail(
				registerUserDTO.getEmail());
		if(exisitngEmail.isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		
		Role role = roleRepository.
				findByRoleName(registerUserDTO.getRoleName())
				.orElseThrow(
						()-> new RuntimeException("Role Not found"));
		
		User user = new User();
		user.setFirstName(registerUserDTO.getFirstName());
		user.setLastName(registerUserDTO.getLastname());
		user.setUserName(registerUserDTO.getUsername());
		user.setEmail(registerUserDTO.getEmail());
		user.setPasswordHash(
				passwordEncoder.encode(registerUserDTO.getPassword()));
		user.setMobileNumber(registerUserDTO.getMobileNumber());
		user.setRole(role);
		user.setIsActive(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		
		User savedUser = userRepository.save(user);
		
	}
	// building the login response now 
	@Override
	public LoginResponseDTO loginResponseDTO(LoginRequestDTO loginRequestDTO) {
		User user = userRepository.findByUserName(
				loginRequestDTO.getUsername()).
				orElseThrow( () -> 
				new RuntimeException("Invalid Username"));
		
		if(!passwordEncoder.matches(
				loginRequestDTO.getPassword(), user.getPasswordHash())) {
			throw new RuntimeException("Invalid Password");
		}
	   LoginResponseDTO response = new LoginResponseDTO();
	   response.setToken(jwtService.generateToken(user.getUserName()));
	   response.setUsername(user.getUserName());
	   response.setRole(user.getRole().getRoleName());
	   response.setMessage("Login Successful");
	   return response;
	}

}
