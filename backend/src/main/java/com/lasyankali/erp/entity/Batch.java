package com.lasyankali.erp.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.lasyankali.erp.entity.enums.BatchLevel;
import com.lasyankali.erp.entity.enums.BatchStatus;
import com.lasyankali.erp.entity.enums.Disciplines;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batch {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "batch_code", nullable = false, unique = true)
    private String batchCode;

    @Column(name = "batch_name", nullable = false)
    private String batchName;

    @Enumerated(EnumType.STRING)
    @Column(name = "discipline", nullable = false)
    private Disciplines discipline;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private BatchLevel level;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "capacity")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BatchStatus status;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

	public Batch() {
		super();
	}

	public Batch(Long batchId, String batchCode, String batchName, Disciplines discipline, BatchLevel level,
			LocalTime startTime, LocalTime endTime, Integer capacity, BatchStatus status, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.batchId = batchId;
		this.batchCode = batchCode;
		this.batchName = batchName;
		this.discipline = discipline;
		this.level = level;
		this.startTime = startTime;
		this.endTime = endTime;
		this.capacity = capacity;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Disciplines getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Disciplines discipline) {
		this.discipline = discipline;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public BatchLevel getLevel() {
		return level;
	}

	public void setLevel(BatchLevel level) {
		this.level = level;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public BatchStatus getStatus() {
		return status;
	}

	public void setStatus(BatchStatus status) {
		this.status = status;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    
    
}
