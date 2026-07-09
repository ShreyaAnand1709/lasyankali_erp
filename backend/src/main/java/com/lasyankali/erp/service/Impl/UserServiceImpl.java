package com.lasyankali.erp.service.Impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.dto.CreateUserDTO;
import com.lasyankali.erp.entity.Role;
import com.lasyankali.erp.entity.User;
import com.lasyankali.erp.repository.RoleRepository;
import com.lasyankali.erp.repository.UserRepository;
import com.lasyankali.erp.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	private final RoleRepository roleRepository; 
	private final PasswordEncoder passwordEncoder;
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public User createUser(CreateUserDTO createUserDto) {
		Optional<User> existingUsername = userRepository.findByUserName(
				createUserDto.getUsername());
		if(existingUsername.isPresent()) {
			throw new RuntimeException("Username already exists");
		}
		
		Optional<User> exisitngEmail = userRepository.findByEmail(
				createUserDto.getEmail());
		if(exisitngEmail.isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		
		Role role = roleRepository.
				findByRoleName("STUDENT")
				.orElseThrow(
						()-> new RuntimeException("Role Not found"));
		
		User user = new User();
		user.setFirstName(createUserDto.getFirstName());
		user.setLastName(createUserDto.getLastName());
		user.setUserName(createUserDto.getUsername());
		user.setEmail(createUserDto.getEmail());
		user.setPasswordHash(
				passwordEncoder.encode(createUserDto.getPassword()));
		user.setMobileNumber(createUserDto.getMobileNumber());
		user.setRole(role);
		user.setIsActive(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		
		User savedUser = userRepository.save(user);
		return savedUser;
	}
	
	
}
