package com.lasyankali.erp.service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lasyankali.erp.dto.CreateStudentDTO;
import com.lasyankali.erp.dto.CreateUserDTO;
import com.lasyankali.erp.dto.StudentResponseDTO;
import com.lasyankali.erp.dto.UpdateStudentDTO;
import com.lasyankali.erp.entity.Student;
import com.lasyankali.erp.entity.User;
import com.lasyankali.erp.mapper.StudentMapper;
import com.lasyankali.erp.repository.StudentRepository;
import com.lasyankali.erp.service.StudentService;
import com.lasyankali.erp.service.UserService;

@Service
public class StudentServiceImpl implements StudentService{
	private final StudentRepository studentRepository;
	private  final UserService userService;
	private final StudentMapper mapStudent;
	public StudentServiceImpl(StudentRepository studentRepository, UserService userService, StudentMapper mapStudent) {
		super();
		this.studentRepository = studentRepository;
		this.userService = userService;
		this.mapStudent = mapStudent;
	}
	
	// creating a student and a user 
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

	// retreiving all the student details
	@Override
	public List<StudentResponseDTO> getAllStudents() {
		List<Student> studentsList = studentRepository.findByStatus("ACTIVE");
		List<StudentResponseDTO> response = new ArrayList<>();
		for(Student student : studentsList ) {
			StudentResponseDTO studentDto = mapStudent.mapToResponse(student);
			response.add(studentDto);
		}
		return response;
	}

	@Override
	public StudentResponseDTO getStudentById(Long studentId) {
		Student student = studentRepository.findById(studentId).
										orElseThrow(
												()-> new RuntimeException("Student not found"));
		StudentResponseDTO studentDto = mapStudent.mapToResponse(student);
		return studentDto;
	}

	@Transactional
	@Override
	public StudentResponseDTO updateStudent(Long studentId, UpdateStudentDTO updateStudentDTO) {
		Student student = studentRepository.findById(studentId).
				orElseThrow(()-> 
				new RuntimeException("Student not found"));
		User user = student.getUser();
		user.setFirstName(updateStudentDTO.getFirstName());
		user.setLastName(updateStudentDTO.getLastName());
		user.setEmail(updateStudentDTO.getEmail());
		user.setMobileNumber(updateStudentDTO.getMobileNumber());
		
		student.setAdmissionNumber(updateStudentDTO.getAdmissionNumber());
		student.setGender(updateStudentDTO.getGender());
		student.setDateOfBirth(updateStudentDTO.getDateOfBirth());
		student.setJoiningDate(updateStudentDTO.getJoiningDate());
		student.setPhotoUrl(updateStudentDTO.getPhotoUrl());
		student.setStatus(updateStudentDTO.getStatus());
		student.setUpdatedAt(LocalDateTime.now());
		
		studentRepository.save(student);
		return mapStudent.mapToResponse(student);
	}

	@Override
	@Transactional
	public void deleteStudent(Long studentId) {
		Student student = studentRepository.findById(studentId).
				orElseThrow(() -> new RuntimeException("Student not found"));
		student.setStatus("INACTIVE");
		student.setUpdatedAt(LocalDateTime.now());
		studentRepository.save(student);	
	}	
}
