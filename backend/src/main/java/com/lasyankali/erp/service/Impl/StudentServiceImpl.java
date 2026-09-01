package com.lasyankali.erp.service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lasyankali.erp.dto.CreateStudentDTO;
import com.lasyankali.erp.dto.CreateUserDTO;
import com.lasyankali.erp.dto.StudentResponseDTO;
import com.lasyankali.erp.dto.UpdateStudentDTO;
import com.lasyankali.erp.entity.Parents;
import com.lasyankali.erp.entity.Student;
import com.lasyankali.erp.entity.User;
import com.lasyankali.erp.entity.enums.Status;
import com.lasyankali.erp.events.AccountCreationEvent;
import com.lasyankali.erp.mapper.StudentMapper;
import com.lasyankali.erp.repository.ParentRepository;
import com.lasyankali.erp.repository.StudentRepository;
import com.lasyankali.erp.service.StudentService;
import com.lasyankali.erp.service.UserService;

@Service
public class StudentServiceImpl implements StudentService{
	private final StudentRepository studentRepository;
	private  final UserService userService;
	private final StudentMapper mapStudent;
	private final ParentRepository parentRepository;
	private final ApplicationEventPublisher eventPublisher;
	public StudentServiceImpl(StudentRepository studentRepository, UserService userService, StudentMapper mapStudent, ParentRepository parentRepository, ApplicationEventPublisher eventPublisher) {
		super();
		this.studentRepository = studentRepository;
		this.userService = userService;
		this.mapStudent = mapStudent;
		this.parentRepository = parentRepository;
		this.eventPublisher = eventPublisher;
	}
	
	// creating a student and a user 
	@Transactional
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void createStudent(CreateStudentDTO createStudentDto) {
		// ==========================================
	    // 1. CREATE PARENT USER
	    // ==========================================

	    CreateUserDTO parentUserDTO = new CreateUserDTO();

	    parentUserDTO.setFirstName(createStudentDto.getParentFirstName());
	    parentUserDTO.setLastName(createStudentDto.getParentLastName());
	    parentUserDTO.setUsername(createStudentDto.getParentUsername());
	    parentUserDTO.setEmail(createStudentDto.getParentEmail());
	    parentUserDTO.setPassword(createStudentDto.getParentPassword());
	    parentUserDTO.setMobileNumber(createStudentDto.getParentMobileNumber());
	    parentUserDTO.setRoleName("PARENT");

	    User savedParentUser = userService.createUser(parentUserDTO);


	    // ==========================================
	    // 2. CREATE PARENT
	    // ==========================================

	    Parents parent = new Parents();

	    parent.setUser(savedParentUser);
	    parent.setOccupation(createStudentDto.getOccupation());
	    parent.setAddress(createStudentDto.getAddress());
	    parent.setCreatedAt(LocalDateTime.now());
	    parent.setUpdatedAt(LocalDateTime.now());

	    Parents savedParent = parentRepository.save(parent);
	    
	    eventPublisher.publishEvent(new AccountCreationEvent(
	    		parentUserDTO.getEmail(),
	    		parentUserDTO.getFirstName(),
	    		parentUserDTO.getUsername(),
	    		parentUserDTO.getPassword(),
	    		parentUserDTO.getRoleName()));


	    // ==========================================
	    // 3. CREATE STUDENT USER
	    // ==========================================

	    CreateUserDTO studentUserDTO = new CreateUserDTO();

	    studentUserDTO.setFirstName(createStudentDto.getFirstName());
	    studentUserDTO.setLastName(createStudentDto.getLastName());
	    studentUserDTO.setUsername(createStudentDto.getUsername());
	    studentUserDTO.setEmail(createStudentDto.getEmail());
	    studentUserDTO.setPassword(createStudentDto.getPassword());
	    studentUserDTO.setMobileNumber(createStudentDto.getMobileNumber());
	    studentUserDTO.setRoleName("STUDENT");

	    User savedStudentUser = userService.createUser(studentUserDTO);


	    // ==========================================
	    // 4. CREATE STUDENT
	    // ==========================================

	    Student student = new Student();

	    student.setUser(savedStudentUser);

	    // Link student to parent
	    student.setParent(savedParent);

	    student.setAdmissionNumber(createStudentDto.getAdmissionNumber());
	    student.setGender(createStudentDto.getGender());
	    student.setDateOfBirth(createStudentDto.getDateOfBirth());
	    student.setJoiningDate(createStudentDto.getJoiningDate());
	    student.setPhotoUrl(createStudentDto.getPhotoUrl());

	    student.setStatus(Status.ACTIVE);

	    student.setCreatedAt(LocalDateTime.now());
	    student.setUpdatedAt(LocalDateTime.now());

	    studentRepository.save(student);
	    
	    eventPublisher.publishEvent(new AccountCreationEvent(
	    		studentUserDTO.getEmail(),
	    		studentUserDTO.getFirstName(),
	    		studentUserDTO.getUsername(),
	    		studentUserDTO.getPassword(),
	    		studentUserDTO.getRoleName()));
	}

	// retreiving all the student details
	@Override
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public List<StudentResponseDTO> getAllStudents() {
		List<Student> studentsList = studentRepository.findByStatus(Status.ACTIVE);
		List<StudentResponseDTO> response = new ArrayList<>();
		for(Student student : studentsList ) {
			StudentResponseDTO studentDto = mapStudent.mapToResponse(student);
			response.add(studentDto);
		}
		return response;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public StudentResponseDTO getStudentById(Long studentId) {
		Student student = studentRepository.findById(studentId).
										orElseThrow(
												()-> new RuntimeException("Student not found"));
		StudentResponseDTO studentDto = mapStudent.mapToResponse(student);
		return studentDto;
	}

	@Transactional
	@Override
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteStudent(Long studentId) {
		Student student = studentRepository.findById(studentId).
				orElseThrow(() -> new RuntimeException("Student not found"));
		student.setStatus(Status.INACTIVE);
		student.setUpdatedAt(LocalDateTime.now());
		studentRepository.save(student);	
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public List<StudentResponseDTO> searchStudents(String keyword) {
		    List<Student> students = studentRepository.searchStudents(keyword);
		    return students.stream()
		            .map(mapStudent::mapToResponse)
		            .toList();
	}	
}
