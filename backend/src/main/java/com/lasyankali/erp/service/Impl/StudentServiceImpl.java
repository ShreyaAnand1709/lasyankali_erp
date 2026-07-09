package com.lasyankali.erp.service.Impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lasyankali.erp.dto.CreateStudentDTO;
import com.lasyankali.erp.dto.CreateUserDTO;
import com.lasyankali.erp.entity.Student;
import com.lasyankali.erp.entity.User;
import com.lasyankali.erp.repository.StudentRepository;
import com.lasyankali.erp.service.StudentService;
import com.lasyankali.erp.service.UserService;

@Service
public class StudentServiceImpl implements StudentService{
	private final StudentRepository studentRepository;
	private  final UserService userService;
	public StudentServiceImpl(StudentRepository studentRepository, UserService userService) {
		super();
		this.studentRepository = studentRepository;
		this.userService = userService;
	}
	@Transactional
	@Override
	public void createStudent(CreateStudentDTO createStudentDto) {
		CreateUserDTO createUserdto = new CreateUserDTO(); 
		createUserdto.setFirstName(createStudentDto.getFirstName());
		createUserdto.setLastName(createStudentDto.getLastName());
		createUserdto.setUsername(createStudentDto.getUsername());
		createUserdto.setEmail(createStudentDto.getEmail());
		createUserdto.setPassword(createStudentDto.getPassword());
		createUserdto.setMobileNumber(createStudentDto.getMobileNumber());
		createUserdto.setRoleName("STUDENT");
		User savedUser = userService.createUser(createUserdto);
		
		Student student = new Student();
		student.setUser(savedUser);
		student.setAdmissionNumber(createStudentDto.getAdmissionNumber());
		student.setGender(createStudentDto.getGender());
		student.setDateOfBirth(createStudentDto.getDateOfBirth());
		student.setJoiningDate(createStudentDto.getJoiningDate());
		student.setPhotoUrl(createStudentDto.getPhotoUrl());	
		student.setStatus("ACTIVE");
		student.setCreatedAt(LocalDateTime.now());
		student.setUpdatedAt(LocalDateTime.now());
		studentRepository.save(student);
	}

}
