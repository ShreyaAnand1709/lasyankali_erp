package com.lasyankali.erp.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.entity.User;
import com.lasyankali.erp.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username)
				.orElseThrow(()->
				new UsernameNotFoundException("User not found"));
		
		List<SimpleGrantedAuthority> authorities = List.of(
				new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
		return new org.springframework.security.core.userdetails.User(
		        user.getUserName(),
		        user.getPasswordHash(),
		        authorities);
	}

}
