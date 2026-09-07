package com.lasyankali.erp.service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.lasyankali.erp.dto.BatchResponseDTO;
import com.lasyankali.erp.dto.CreateBatchDTO;
import com.lasyankali.erp.dto.UpdateBatchDTO;
import com.lasyankali.erp.entity.Batch;
import com.lasyankali.erp.entity.BatchStudent;
import com.lasyankali.erp.entity.BatchTeacher;
import com.lasyankali.erp.entity.Student;
import com.lasyankali.erp.entity.Teacher;
import com.lasyankali.erp.entity.enums.BatchStatus;
import com.lasyankali.erp.entity.enums.Status;
import com.lasyankali.erp.mapper.BatchMapper;
import com.lasyankali.erp.repository.BatchRepository;
import com.lasyankali.erp.repository.BatchStudentRepository;
import com.lasyankali.erp.repository.BatchTeacherRepository;
import com.lasyankali.erp.repository.StudentRepository;
import com.lasyankali.erp.repository.TeacherRepository;
import com.lasyankali.erp.service.BatchService;
import jakarta.transaction.Transactional;

@Service
public class BatchServiceImpl implements BatchService{
	
	private final BatchRepository batchRepository;
	private final BatchMapper batchmapper;
	private final TeacherRepository teacherRepository;
	private final BatchTeacherRepository batchTeacherRepository;
	private final StudentRepository studentRepository;
	private final BatchStudentRepository batchStudentRepository;
	
	public BatchServiceImpl(BatchRepository batchRepository, BatchMapper batchmapper,
			TeacherRepository teacherRepository,BatchTeacherRepository batchTeacherRepository,
			StudentRepository studentRepository,BatchStudentRepository batchStudentRepository) {
		super();
		this.batchRepository = batchRepository;
		this.batchmapper = batchmapper;
		this.teacherRepository = teacherRepository;
		this.batchTeacherRepository = batchTeacherRepository;
		this.studentRepository = studentRepository;
		this.batchStudentRepository = batchStudentRepository;
	}

	@Override
	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	public void createBatch(CreateBatchDTO createBatchDTO) {
		// 1. Check duplicate batch code
        if (batchRepository.existsByBatchCode(createBatchDTO.getBatchCode())) {
            throw new RuntimeException("Batch code already exists");
        }

        // 2. Validate capacity
        if (createBatchDTO.getCapacity() == null
                || createBatchDTO.getCapacity() <= 0) {

            throw new RuntimeException(
                    "Batch capacity must be greater than 0"
            );
        }

        // 3. Validate timings
        if (createBatchDTO.getStartTime() == null
                || createBatchDTO.getEndTime() == null) {

            throw new RuntimeException(
                    "Batch start time and end time are required"
            );
        }

        if (!createBatchDTO.getStartTime()
                .isBefore(createBatchDTO.getEndTime())) {

            throw new RuntimeException(
                    "Batch start time must be before end time"
            );
        }
		Batch batch = new Batch();
		batch.setBatchCode(createBatchDTO.getBatchCode());
		batch.setBatchName(createBatchDTO.getBatchName());
		batch.setDiscipline(createBatchDTO.getDiscipline());
		batch.setLevel(createBatchDTO.getLevel());
		batch.setStartTime(createBatchDTO.getStartTime());
		batch.setEndTime(createBatchDTO.getEndTime());
		batch.setCapacity(createBatchDTO.getCapacity());
		batch.setStatus(BatchStatus.ACTIVE);
		batch.setCreatedAt(LocalDateTime.now());
		batch.setUpdatedAt(LocalDateTime.now());
		batchRepository.save(batch);
		
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public List<BatchResponseDTO> getAllBatches() {
		// TODO Auto-generated method stub
		List<Batch> batchList = batchRepository.findByStatus(BatchStatus.ACTIVE);
		List<BatchResponseDTO> response = new ArrayList<>();
		for(Batch batch : batchList ) {
			BatchResponseDTO batchDto = batchmapper.mapToBatchResponse(batch);
			response.add(batchDto);
		}
		return response;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public BatchResponseDTO getBatchById(Long batchId) {
		Batch batch = batchRepository.findById(batchId).
				orElseThrow(
						()-> new RuntimeException("Batch not found"));
		BatchResponseDTO response = batchmapper.mapToBatchResponse(batch);
		return response;
	}

	@Override
	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	public BatchResponseDTO updateBatch(Long batchId, UpdateBatchDTO updateBatchDTO) {
		// TODO Auto-generated method stub
		Batch batch = batchRepository.findById(batchId).
				orElseThrow(
						()-> new RuntimeException("Batch not found"));
		if (updateBatchDTO.getCapacity() == null
                || updateBatchDTO.getCapacity() <= 0) {

            throw new RuntimeException(
                    "Batch capacity must be greater than 0"
            );
        }

        // 3. Validate timings
        if (updateBatchDTO.getStartTime() == null
                || updateBatchDTO.getEndTime() == null) {

            throw new RuntimeException(
                    "Batch start time and end time are required"
            );
        }

        if (!updateBatchDTO.getStartTime()
                .isBefore(updateBatchDTO.getEndTime())) {

            throw new RuntimeException(
                    "Batch start time must be before end time"
            );
        }
		batch.setBatchName(updateBatchDTO.getBatchName());
		batch.setDiscipline(updateBatchDTO.getDisciplines());
		batch.setLevel(updateBatchDTO.getLevel());
		batch.setStartTime(updateBatchDTO.getStartTime());
		batch.setEndTime(updateBatchDTO.getEndTime());
		batch.setCapacity(updateBatchDTO.getCapacity());
		if (updateBatchDTO.getStatus() != null) {
		    batch.setStatus(updateBatchDTO.getStatus());
		}
		batch.setUpdatedAt(LocalDateTime.now());
		batchRepository.save(batch);
		BatchResponseDTO response = batchmapper.mapToBatchResponse(batch);
		return response;
	}

	@Override
	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	public void deleteBatch(Long batchId) {
		// TODO Auto-generated method stub
		Batch batch = batchRepository.findById(batchId).
				orElseThrow(
						()-> new RuntimeException("Batch not found"));
		batch.setStatus(BatchStatus.INACTIVE);
		batch.setUpdatedAt(LocalDateTime.now());
		batchRepository.save(batch);
		
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public List<BatchResponseDTO> searchBatch(String keyword) {
		// TODO Auto-generated method stub
		List<Batch> batches = batchRepository.searchBatch(keyword);
	    return batches.stream()
	            .map(batchmapper::mapToBatchResponse)
	            .toList();
	}

	@Override
	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	public void assignTeacherToBatch(Long batchId, Long teacherId) {
		// TODO Auto-generated method stub
	Batch batch = batchRepository.findById(batchId).
			orElseThrow(
					()-> new RuntimeException("Batch doesnt exist"));
	Teacher teacher = teacherRepository.findById(teacherId).
			orElseThrow(
					()-> new RuntimeException("Teacher not found"));
	BatchTeacher batchTeacher = new BatchTeacher();
	if(batch.getStatus() != BatchStatus.ACTIVE) {
		throw new RuntimeException("Batch is inactive");
	}
	if(teacher.getStatus() != Status.ACTIVE) {
		throw new RuntimeException("Teacher is inactive");
	}
	if(batchTeacherRepository.existsByBatch_BatchIdAndTeacher_TeacherId(batchId, teacherId)) {
		throw new RuntimeException("Teacher and batch are already assigned");
	}
	batchTeacher.setTeacher(teacher);
	batchTeacher.setBatch(batch);
	batchTeacher.setAssignedDate(LocalDate.now());
	batchTeacher.setCreatedAt(LocalDateTime.now());
	batchTeacherRepository.save(batchTeacher);	
	}
	
	@Override
	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	public void deleteTeacher(Long batchId, Long teacherId) {
		BatchTeacher assignment = batchTeacherRepository.findByBatch_BatchIdAndTeacher_TeacherId(batchId, teacherId).
				orElseThrow(
						()-> new RuntimeException("Teacher is not assigned to the batch"));
		batchTeacherRepository.delete(assignment);
	}

	@Override
	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	public void assignStudentToBatch(Long batchId, Long studentId) {
		// TODO Auto-generated method stub
		Batch batch = batchRepository.findById(batchId).
				orElseThrow(
						()-> new RuntimeException("Batch doesnt exist"));
		Student student = studentRepository.findById(studentId).
				orElseThrow(
						()-> new RuntimeException("Student not found"));
		BatchStudent batchStudent = new BatchStudent();
		if(batch.getStatus() != BatchStatus.ACTIVE) {
			throw new RuntimeException("Batch is inactive");
		}
		if(student.getStatus() != Status.ACTIVE) {
			throw new RuntimeException("Student is inactive");
		}
		if(batchStudentRepository.existsByBatch_BatchIdAndStudent_StudentId(batchId, studentId)) {
			throw new RuntimeException("Student and batch are already assigned");
		}
		batchStudent.setBatch(batch);
		batchStudent.setStudent(student);
		batchStudent.setEnrollmentDate(LocalDate.now());
		batchStudent.setCreatedAt(LocalDateTime.now());
		batchStudentRepository.save(batchStudent);	
	}

	@Override
	@Transactional
    @PreAuthorize("hasRole('ADMIN')")
	public void deleteStudent(Long batchId, Long studentId) {
		// TODO Auto-generated method stub
		BatchStudent assignment = batchStudentRepository.findByBatch_BatchIdAndStudent_StudentId(batchId, studentId).
				orElseThrow(
						()-> new RuntimeException("Student is not assigned to the batch"));
		batchStudentRepository.delete(assignment);
	}

}
