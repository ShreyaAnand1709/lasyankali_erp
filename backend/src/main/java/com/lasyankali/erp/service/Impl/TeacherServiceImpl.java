package com.lasyankali.erp.service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.dto.CreateTeacherDTO;
import com.lasyankali.erp.dto.CreateUserDTO;
import com.lasyankali.erp.dto.TeacherResponseDTO;
import com.lasyankali.erp.dto.UpdateTeacherDTO;
import com.lasyankali.erp.entity.Teacher;
import com.lasyankali.erp.entity.User;
import com.lasyankali.erp.entity.enums.Status;
import com.lasyankali.erp.mapper.TeacherMapper;
import com.lasyankali.erp.repository.TeacherRepository;
import com.lasyankali.erp.service.TeacherService;
import com.lasyankali.erp.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class TeacherServiceImpl implements TeacherService{
	
	private final TeacherRepository teacherRepository;
    private final UserService userService;
    private final TeacherMapper teacherMapper;
    
	
	public TeacherServiceImpl(TeacherRepository teacherRepository, UserService userService,
			TeacherMapper teacherMapper) {
		super();
		this.teacherRepository = teacherRepository;
		this.userService = userService;
		this.teacherMapper = teacherMapper;
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public void createTeacher(CreateTeacherDTO createTeacherDTO) {
		CreateUserDTO createUserDTO = new CreateUserDTO();

        createUserDTO.setFirstName(createTeacherDTO.getFirstName());
        createUserDTO.setLastName(createTeacherDTO.getLastName());
        createUserDTO.setUsername(createTeacherDTO.getUsername());
        createUserDTO.setEmail(createTeacherDTO.getEmail());
        createUserDTO.setPassword(createTeacherDTO.getPassword());
        createUserDTO.setMobileNumber(createTeacherDTO.getMobileNumber());
        createUserDTO.setRoleName("TEACHER");

        User savedUser = userService.createUser(createUserDTO);

        Teacher teacher = new Teacher();

        teacher.setUser(savedUser);
        teacher.setEmployeeCode(createTeacherDTO.getEmployeeCode());
        teacher.setSpecialization(createTeacherDTO.getSpecialization());
        teacher.setQualification(createTeacherDTO.getQualification());
        teacher.setYearsOfExperience(createTeacherDTO.getYearsOfExperience());
        teacher.setJoiningDate(createTeacherDTO.getJoiningDate());

        teacher.setStatus(Status.ACTIVE);
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        teacherRepository.save(teacher);
		
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public List<TeacherResponseDTO> getAllTeachers() {
		 List<Teacher> teacherList = teacherRepository.findByStatus(Status.ACTIVE);

	        List<TeacherResponseDTO> response = new ArrayList<>();

	        for (Teacher teacher : teacherList) {
	            response.add(teacherMapper.mapToResponse(teacher));
	        }

	        return response;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public TeacherResponseDTO getTeacherById(Long teacherId) {
		Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return teacherMapper.mapToResponse(teacher);
	}

	@Override
	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	public TeacherResponseDTO updateTeacher(Long teacherId, UpdateTeacherDTO updateTeacherDTO) {
		Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        User user = teacher.getUser();

        user.setFirstName(updateTeacherDTO.getFirstName());
        user.setLastName(updateTeacherDTO.getLastName());
        user.setEmail(updateTeacherDTO.getEmail());
        user.setMobileNumber(updateTeacherDTO.getMobileNumber());

        teacher.setEmployeeCode(updateTeacherDTO.getEmployeeCode());
        teacher.setSpecialization(updateTeacherDTO.getSpecialization());
        teacher.setQualification(updateTeacherDTO.getQualification());
        teacher.setYearsOfExperience(updateTeacherDTO.getYearsOfExperience());
        teacher.setJoiningDate(updateTeacherDTO.getJoiningDate());

        teacher.setUpdatedAt(LocalDateTime.now());

        teacherRepository.save(teacher);

        return teacherMapper.mapToResponse(teacher);
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteTeacher(Long teacherId) {
		Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        teacher.setStatus(Status.INACTIVE);
        teacher.setUpdatedAt(LocalDateTime.now());

        teacherRepository.save(teacher);
		
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")

	public List<TeacherResponseDTO> searchTeachers(String keyword) {
		 List<Teacher> teachers = teacherRepository.searchTeachers(keyword, Status.ACTIVE);

	        return teachers.stream()
	                .map(teacherMapper::mapToResponse)
	                .toList();
	}

}
