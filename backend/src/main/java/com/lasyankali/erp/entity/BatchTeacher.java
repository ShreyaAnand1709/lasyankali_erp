package com.lasyankali.erp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "batch_teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchTeacher {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_teacher_id")
    private Long batchTeacherId;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "assignemd_date")
    private LocalDate assignedDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

	public BatchTeacher() {
		super();
	}

	public BatchTeacher(Long batchTeacherId, Batch batch, Teacher teacher, LocalDate assignedDate,
			LocalDateTime createdAt) {
		super();
		this.batchTeacherId = batchTeacherId;
		this.batch = batch;
		this.teacher = teacher;
		this.assignedDate = assignedDate;
		this.createdAt = createdAt;
	}

	public LocalDate getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(LocalDate assignedDate) {
		this.assignedDate = assignedDate;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Long getBatchTeacherId() {
		return batchTeacherId;
	}

	public void setBatchTeacherId(Long batchTeacherId) {
		this.batchTeacherId = batchTeacherId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
    
}
