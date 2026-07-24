package com.lasyankali.erp.service;

import java.util.List;

import com.lasyankali.erp.dto.CreateStudentDTO;
import com.lasyankali.erp.dto.StudentResponseDTO;
import com.lasyankali.erp.dto.UpdateStudentDTO;

public interface StudentService {
	void createStudent(CreateStudentDTO createStudentDto);
	List<StudentResponseDTO> getAllStudents();
	StudentResponseDTO getStudentById(Long studentId);
	StudentResponseDTO updateStudent(Long studentId,
            UpdateStudentDTO updateStudentDTO);
	void deleteStudent(Long studentId);
	List<StudentResponseDTO> searchStudents(String keyword);
}
