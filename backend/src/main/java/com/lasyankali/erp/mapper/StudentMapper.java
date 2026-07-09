package com.lasyankali.erp.mapper;

import org.springframework.stereotype.Component;

import com.lasyankali.erp.dto.StudentResponseDTO;
import com.lasyankali.erp.entity.Student;

@Component
public class StudentMapper {
	public StudentResponseDTO mapToResponse(Student student) {
		StudentResponseDTO studentDto = new StudentResponseDTO();
		studentDto.setStudentId(student.getStudentId());
		studentDto.setFirstName(student.getUser().getFirstName());
		studentDto.setLastName(student.getUser().getLastName());
		studentDto.setUsername(student.getUser().getUserName());
		studentDto.setEmail(student.getUser().getEmail());
		studentDto.setMobileNumber(student.getUser().getMobileNumber());
		studentDto.setAdmissionNumber(student.getAdmissionNumber());
		studentDto.setGender(student.getGender());
		studentDto.setJoiningDate(student.getJoiningDate());
		studentDto.setPhotoUrl(student.getPhotoUrl());
		studentDto.setStatus(student.getStatus());
		return studentDto;
	}
}
