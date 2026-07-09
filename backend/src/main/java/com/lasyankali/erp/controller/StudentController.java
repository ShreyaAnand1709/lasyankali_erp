package com.lasyankali.erp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.CreateStudentDTO;
import com.lasyankali.erp.dto.StudentResponseDTO;
import com.lasyankali.erp.service.StudentService;

@RestController
@RequestMapping("api/student")
public class StudentController {
	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}
	
	@PostMapping
	public ResponseEntity<String> createStudent(@RequestBody CreateStudentDTO createStudentDto) {
		studentService.createStudent(createStudentDto);
		return ResponseEntity.ok("Student and User created Successfully");
	}
	
	@GetMapping
	public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
		List<StudentResponseDTO> response = studentService.getAllStudents();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{studentId}")
	public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long studentId) {
		StudentResponseDTO response = studentService.getStudentById(studentId);
		return ResponseEntity.ok(response);
	}
	
}
