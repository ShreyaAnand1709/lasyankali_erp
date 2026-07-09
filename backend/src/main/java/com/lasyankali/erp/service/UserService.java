package com.lasyankali.erp.service;


import com.lasyankali.erp.dto.CreateUserDTO;
import com.lasyankali.erp.entity.User;

public interface UserService {
	User createUser(CreateUserDTO createUserDto);
}
