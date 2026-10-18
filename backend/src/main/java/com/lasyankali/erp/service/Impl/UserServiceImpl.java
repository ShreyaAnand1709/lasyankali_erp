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
	    if (createUserDto == null) {
	        throw new IllegalArgumentException(
	                "User details cannot be null");
	    }

	    String roleName = createUserDto.getRoleName();

	    if (roleName == null || roleName.isBlank()) {
	        throw new IllegalArgumentException(
	                "Role name is required");
	    }

	    roleName = roleName.trim()
	            .toUpperCase(java.util.Locale.ROOT);

	    if (!java.util.Set.of("STUDENT", "TEACHER", "PARENT")
	            .contains(roleName)) {
	        throw new IllegalArgumentException(
	                "Unsupported role for this workflow");
	    }

	    if (userRepository.findByUserName(
	            createUserDto.getUsername()).isPresent()) {
	        throw new IllegalArgumentException(
	                "Username already exists");
	    }

	    if (userRepository.findByEmail(
	            createUserDto.getEmail()).isPresent()) {
	        throw new IllegalArgumentException(
	                "Email already exists");
	    }

	    Role role = roleRepository.findByRoleName(roleName)
	            .orElseThrow(() -> new IllegalStateException(
	                    "Requested role is not configured"));

	    LocalDateTime now = LocalDateTime.now();

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
	    user.setCreatedAt(now);
	    user.setUpdatedAt(now);

	    return userRepository.save(user);
	}
}
