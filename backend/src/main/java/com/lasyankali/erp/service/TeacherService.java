package com.lasyankali.erp.service;

import java.util.List;

import com.lasyankali.erp.dto.CreateTeacherDTO;
import com.lasyankali.erp.dto.TeacherResponseDTO;
import com.lasyankali.erp.dto.UpdateTeacherDTO;

public interface TeacherService {
	void createTeacher(CreateTeacherDTO createTeacherDTO);

    List<TeacherResponseDTO> getAllTeachers();

    TeacherResponseDTO getTeacherById(Long teacherId);

    TeacherResponseDTO updateTeacher(Long teacherId,
                                     UpdateTeacherDTO updateTeacherDTO);

    void deleteTeacher(Long teacherId);

    List<TeacherResponseDTO> searchTeachers(String keyword);
}
