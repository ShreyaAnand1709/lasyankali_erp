package com.lasyankali.erp.service;

import com.lasyankali.erp.dto.LoginRequestDTO;
import com.lasyankali.erp.dto.LoginResponseDTO;
import com.lasyankali.erp.dto.RegisterUserDTO;

public interface AuthService {
	void registeredUser(RegisterUserDTO registerUserDTO);
	LoginResponseDTO loginResponseDTO(LoginRequestDTO loginResponseDTO);
}
