package com.lasyankali.erp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lasyankali.erp.dto.CreateTeacherDTO;
import com.lasyankali.erp.dto.TeacherResponseDTO;
import com.lasyankali.erp.dto.UpdateTeacherDTO;
import com.lasyankali.erp.service.TeacherService;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
	private final TeacherService teacherService;

	public TeacherController(TeacherService teacherService) {
		super();
		this.teacherService = teacherService;
	}
	
	@PostMapping
	public ResponseEntity<Void> createTeacher(
	        @RequestBody CreateTeacherDTO createTeacherDTO) {

	    teacherService.createTeacher(createTeacherDTO);

	    return ResponseEntity.ok().build();
	}
	
	@GetMapping
	public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {

	    List<TeacherResponseDTO> response =
	            teacherService.getAllTeachers();

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{teacherId}")
	public ResponseEntity<TeacherResponseDTO> getTeacherById(
	        @PathVariable Long teacherId) {

	    TeacherResponseDTO response =
	            teacherService.getTeacherById(teacherId);

	    return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{teacherId}")
	public ResponseEntity<TeacherResponseDTO> updateTeacher(
	        @PathVariable Long teacherId,
	        @RequestBody UpdateTeacherDTO updateTeacherDTO) {

	    TeacherResponseDTO response =
	            teacherService.updateTeacher(teacherId, updateTeacherDTO);

	    return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{teacherId}")
	public ResponseEntity<Void> deleteTeacher(
	        @PathVariable Long teacherId) {

	    teacherService.deleteTeacher(teacherId);

	    return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<TeacherResponseDTO>> searchTeachers(
	        @RequestParam String keyword) {

	    List<TeacherResponseDTO> response =
	            teacherService.searchTeachers(keyword);

	    return ResponseEntity.ok(response);
	}
}
