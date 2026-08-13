package com.lasyankali.erp.mapper;

import org.springframework.stereotype.Component;

import com.lasyankali.erp.dto.TeacherResponseDTO;
import com.lasyankali.erp.entity.Teacher;

@Component
public class TeacherMapper {
	public TeacherResponseDTO mapToResponse(Teacher teacher) {
	TeacherResponseDTO teacherDto = new TeacherResponseDTO();

    teacherDto.setTeacherId(teacher.getTeacherId());

    teacherDto.setFirstName(teacher.getUser().getFirstName());
    teacherDto.setLastName(teacher.getUser().getLastName());

    teacherDto.setUsername(teacher.getUser().getUserName());
    teacherDto.setEmail(teacher.getUser().getEmail());
    teacherDto.setMobileNumber(teacher.getUser().getMobileNumber());

    teacherDto.setEmployeeCode(teacher.getEmployeeCode());
    teacherDto.setSpecialization(teacher.getSpecialization());
    teacherDto.setQualification(teacher.getQualification());
    teacherDto.setYearsOfExperience(teacher.getYearsOfExperience());
    teacherDto.setJoiningDate(teacher.getJoiningDate());

    teacherDto.setStatus(teacher.getStatus());

    return teacherDto;
	}
}
