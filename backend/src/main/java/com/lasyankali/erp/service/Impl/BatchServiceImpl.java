package com.lasyankali.erp.service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.lasyankali.erp.dto.BatchMemberResponseDTO;
import com.lasyankali.erp.dto.BatchResponseDTO;
import com.lasyankali.erp.dto.CreateBatchDTO;
import com.lasyankali.erp.dto.UpdateBatchDTO;
import com.lasyankali.erp.entity.Batch;
import com.lasyankali.erp.entity.BatchStudent;
import com.lasyankali.erp.entity.BatchTeacher;
import com.lasyankali.erp.entity.Student;
import com.lasyankali.erp.entity.Teacher;
import com.lasyankali.erp.entity.enums.BatchLevel;
import com.lasyankali.erp.entity.enums.BatchStatus;
import com.lasyankali.erp.entity.enums.Disciplines;
import com.lasyankali.erp.entity.enums.EnrollmentStatus;
import com.lasyankali.erp.entity.enums.Status;
import com.lasyankali.erp.mapper.BatchMapper;
import com.lasyankali.erp.repository.BatchRepository;
import com.lasyankali.erp.repository.BatchStudentRepository;
import com.lasyankali.erp.repository.BatchTeacherRepository;
import com.lasyankali.erp.repository.StudentRepository;
import com.lasyankali.erp.repository.TeacherRepository;
import com.lasyankali.erp.service.BatchService;

@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final BatchMapper batchmapper;
    private final TeacherRepository teacherRepository;
    private final BatchTeacherRepository batchTeacherRepository;
    private final StudentRepository studentRepository;
    private final BatchStudentRepository batchStudentRepository;

    public BatchServiceImpl(
            BatchRepository batchRepository,
            BatchMapper batchmapper,
            TeacherRepository teacherRepository,
            BatchTeacherRepository batchTeacherRepository,
            StudentRepository studentRepository,
            BatchStudentRepository batchStudentRepository) {

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

        if (createBatchDTO == null) {
            throw badRequest("Batch details are required");
        }

        if (createBatchDTO.getBatchCode() == null
                || createBatchDTO.getBatchCode().isBlank()) {
            throw badRequest("Batch code is required");
        }

        validateBatchDetails(
                createBatchDTO.getBatchName(),
                createBatchDTO.getDiscipline(),
                createBatchDTO.getLevel(),
                createBatchDTO.getStartTime(),
                createBatchDTO.getEndTime(),
                createBatchDTO.getCapacity());

        String batchCode = createBatchDTO.getBatchCode().trim();

        if (batchRepository.existsByBatchCode(batchCode)) {
            throw conflict("Batch code already exists");
        }

        LocalDateTime now = LocalDateTime.now();

        Batch batch = new Batch();
        batch.setBatchCode(batchCode);
        batch.setBatchName(createBatchDTO.getBatchName().trim());
        batch.setDiscipline(createBatchDTO.getDiscipline());
        batch.setLevel(createBatchDTO.getLevel());
        batch.setStartTime(createBatchDTO.getStartTime());
        batch.setEndTime(createBatchDTO.getEndTime());
        batch.setCapacity(createBatchDTO.getCapacity());
        batch.setStatus(BatchStatus.ACTIVE);
        batch.setCreatedAt(now);
        batch.setUpdatedAt(now);

        batchRepository.save(batch);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<BatchResponseDTO> getAllBatches() {

        return batchRepository.findByStatus(BatchStatus.ACTIVE)
                .stream()
                .map(batchmapper::mapToBatchResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public BatchResponseDTO getBatchById(Long batchId) {
        return batchmapper.mapToBatchResponse(findBatch(batchId));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public BatchResponseDTO updateBatch(
            Long batchId,
            UpdateBatchDTO updateBatchDTO) {

        if (updateBatchDTO == null) {
            throw badRequest("Batch details are required");
        }

        validateBatchDetails(
                updateBatchDTO.getBatchName(),
                updateBatchDTO.getDisciplines(),
                updateBatchDTO.getLevel(),
                updateBatchDTO.getStartTime(),
                updateBatchDTO.getEndTime(),
                updateBatchDTO.getCapacity());

        Batch batch = findBatchForUpdate(batchId);

        long activeEnrollments = batchStudentRepository
                .countByBatch_BatchIdAndStatus(
                        batchId,
                        EnrollmentStatus.ACTIVE);

        if (updateBatchDTO.getCapacity() < activeEnrollments) {
            throw conflict(
                    "Capacity cannot be lower than current active enrollment");
        }

        batch.setBatchName(updateBatchDTO.getBatchName().trim());
        batch.setDiscipline(updateBatchDTO.getDisciplines());
        batch.setLevel(updateBatchDTO.getLevel());
        batch.setStartTime(updateBatchDTO.getStartTime());
        batch.setEndTime(updateBatchDTO.getEndTime());
        batch.setCapacity(updateBatchDTO.getCapacity());

        if (updateBatchDTO.getStatus() != null) {
            batch.setStatus(updateBatchDTO.getStatus());
        }

        batch.setUpdatedAt(LocalDateTime.now());

        return batchmapper.mapToBatchResponse(
                batchRepository.save(batch));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBatch(Long batchId) {

        Batch batch = findBatchForUpdate(batchId);

        batch.setStatus(BatchStatus.INACTIVE);
        batch.setUpdatedAt(LocalDateTime.now());

        batchRepository.save(batch);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<BatchResponseDTO> searchBatch(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return batchRepository.findByStatus(BatchStatus.ACTIVE)
                    .stream()
                    .map(batchmapper::mapToBatchResponse)
                    .toList();
        }

        return batchRepository.searchBatch(keyword.trim())
                .stream()
                .map(batchmapper::mapToBatchResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void assignTeacherToBatch(Long batchId, Long teacherId) {

        validateId(teacherId, "Teacher");

        Batch batch = findBatchForUpdate(batchId);
        requireActiveBatch(batch);

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> notFound("Teacher not found"));

        if (teacher.getStatus() != Status.ACTIVE) {
            throw conflict("Teacher profile is inactive");
        }

        if (batchTeacherRepository
                .existsByBatch_BatchIdAndTeacher_TeacherId(
                        batchId, teacherId)) {
            throw conflict("Teacher is already assigned to this batch");
        }

        BatchTeacher assignment = new BatchTeacher();
        assignment.setBatch(batch);
        assignment.setTeacher(teacher);
        assignment.setAssignedDate(LocalDate.now());
        assignment.setCreatedAt(LocalDateTime.now());

        batchTeacherRepository.save(assignment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTeacher(Long batchId, Long teacherId) {

        validateId(teacherId, "Teacher");
        findBatchForUpdate(batchId);

        BatchTeacher assignment = batchTeacherRepository
                .findByBatch_BatchIdAndTeacher_TeacherId(
                        batchId, teacherId)
                .orElseThrow(() ->
                        notFound("Teacher is not assigned to this batch"));

        batchTeacherRepository.delete(assignment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void assignStudentToBatch(Long batchId, Long studentId) {

        validateId(studentId, "Student");

        Batch batch = findBatchForUpdate(batchId);
        requireActiveBatch(batch);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> notFound("Student not found"));

        if (student.getStatus() != Status.ACTIVE) {
            throw conflict("Student profile is inactive");
        }

        if (batchStudentRepository
                .existsByBatch_BatchIdAndStudent_StudentId(
                        batchId, studentId)) {
            throw conflict("Student already has an enrollment in this batch");
        }

        if (batch.getCapacity() == null || batch.getCapacity() <= 0) {
            throw conflict("Batch capacity is not configured correctly");
        }

        long activeEnrollments = batchStudentRepository
                .countByBatch_BatchIdAndStatus(
                        batchId,
                        EnrollmentStatus.ACTIVE);

        if (activeEnrollments >= batch.getCapacity()) {
            throw conflict("Batch has reached its capacity");
        }

        BatchStudent assignment = new BatchStudent();
        assignment.setBatch(batch);
        assignment.setStudent(student);
        assignment.setEnrollmentDate(LocalDate.now());
        assignment.setStatus(EnrollmentStatus.ACTIVE);
        assignment.setCreatedAt(LocalDateTime.now());

        batchStudentRepository.save(assignment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudent(Long batchId, Long studentId) {

        validateId(studentId, "Student");
        findBatchForUpdate(batchId);

        BatchStudent assignment = batchStudentRepository
                .findByBatch_BatchIdAndStudent_StudentId(
                        batchId, studentId)
                .orElseThrow(() ->
                        notFound("Student is not assigned to this batch"));

        // Preserves the existing physical-removal behavior.
        batchStudentRepository.delete(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<BatchMemberResponseDTO> getBatchStudents(Long batchId) {

        findBatch(batchId);

        return batchStudentRepository.findByBatch_BatchId(batchId)
                .stream()
                .map(assignment -> {
                    Student student = assignment.getStudent();
                    var user = student.getUser();

                    return new BatchMemberResponseDTO(
                            student.getStudentId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getUserName(),
                            student.getStatus() == null
                                    ? null
                                    : student.getStatus().name(),
                            assignment.getStatus() == null
                                    ? null
                                    : assignment.getStatus().name(),
                            assignment.getEnrollmentDate());
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<BatchMemberResponseDTO> getBatchTeachers(Long batchId) {

        findBatch(batchId);

        return batchTeacherRepository.findByBatch_BatchId(batchId)
                .stream()
                .map(assignment -> {
                    Teacher teacher = assignment.getTeacher();
                    var user = teacher.getUser();

                    return new BatchMemberResponseDTO(
                            teacher.getTeacherId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getUserName(),
                            teacher.getStatus() == null
                                    ? null
                                    : teacher.getStatus().name(),
                            null,
                            assignment.getAssignedDate());
                })
                .toList();
    }

    private Batch findBatch(Long batchId) {
        validateId(batchId, "Batch");

        return batchRepository.findById(batchId)
                .orElseThrow(() -> notFound("Batch not found"));
    }

    private Batch findBatchForUpdate(Long batchId) {
        validateId(batchId, "Batch");

        return batchRepository.findForUpdate(batchId)
                .orElseThrow(() -> notFound("Batch not found"));
    }

    private void requireActiveBatch(Batch batch) {
        if (batch.getStatus() != BatchStatus.ACTIVE) {
            throw conflict("Batch is inactive");
        }
    }

    private void validateId(Long id, String resource) {
        if (id == null || id <= 0) {
            throw badRequest(resource + " ID must be greater than zero");
        }
    }

    private void validateBatchDetails(
            String batchName,
            Disciplines discipline,
            BatchLevel level,
            LocalTime startTime,
            LocalTime endTime,
            Integer capacity) {

        if (batchName == null || batchName.isBlank()) {
            throw badRequest("Batch name is required");
        }

        if (discipline == null || level == null) {
            throw badRequest("Discipline and level are required");
        }

        if (capacity == null || capacity <= 0) {
            throw badRequest("Batch capacity must be greater than zero");
        }

        if (startTime == null || endTime == null) {
            throw badRequest("Start time and end time are required");
        }

        if (!startTime.isBefore(endTime)) {
            throw badRequest("Start time must be before end time");
        }
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST, message);
    }

    private ResponseStatusException notFound(String message) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND, message);
    }

    private ResponseStatusException conflict(String message) {
        return new ResponseStatusException(
                HttpStatus.CONFLICT, message);
    }

	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('STUDENT') and #p0 == authentication.name")
	public List<BatchResponseDTO> getMyStudentBatches(String username) {
		Student student = studentRepository.findByUser_UserName(username)
	            .orElseThrow(() ->
	                    notFound("No student profile is linked to your account"));

	    if (student.getStatus() != Status.ACTIVE) {
	        throw new org.springframework.security.access.AccessDeniedException(
	                "Your student profile is inactive");
	    }

	    return batchStudentRepository
	            .findByStudent_User_UserNameAndStatus(
	                    username, EnrollmentStatus.ACTIVE)
	            .stream()
	            .map(BatchStudent::getBatch)
	            .filter(batch -> batch.getStatus() == BatchStatus.ACTIVE)
	            .map(batchmapper::mapToBatchResponse)
	            .toList();
	}
}