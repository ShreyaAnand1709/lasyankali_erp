package com.lasyankali.erp.entity;

import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.lasyankali.erp.entity.enums.EnrollmentStatus;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "batch_students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchStudent {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_student_id")
    private Long batchStudentId;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

	public BatchStudent() {
		super();
	}

	public BatchStudent(Long batchStudentId, Batch batch, Student student, LocalDate enrollmentDate,
			EnrollmentStatus status, LocalDateTime createdAt) {
		super();
		this.batchStudentId = batchStudentId;
		this.batch = batch;
		this.student = student;
		this.enrollmentDate = enrollmentDate;
		this.status = status;
		this.createdAt = createdAt;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Long getBatchStudentId() {
		return batchStudentId;
	}

	public void setBatchStudentId(Long batchStudentId) {
		this.batchStudentId = batchStudentId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(LocalDate enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public EnrollmentStatus getStatus() {
		return status;
	}

	public void setStatus(EnrollmentStatus status) {
		this.status = status;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
    
}
