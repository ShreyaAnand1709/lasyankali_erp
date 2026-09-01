package com.lasyankali.erp.dto;

import java.time.LocalTime;

import com.lasyankali.erp.entity.enums.BatchLevel;
import com.lasyankali.erp.entity.enums.BatchStatus;
import com.lasyankali.erp.entity.enums.Disciplines;

public class UpdateBatchDTO {
	private String BatchName;
	private Disciplines disciplines;
	private BatchLevel level;
	private LocalTime startTime;
	private LocalTime endTime;
	private Integer capacity;
	private BatchStatus status;
	public UpdateBatchDTO() {
		super();
	}
	public UpdateBatchDTO(String batchName, Disciplines disciplines, BatchLevel level, LocalTime startTime,
			LocalTime endTime, Integer capacity, BatchStatus status) {
		super();
		BatchName = batchName;
		this.disciplines = disciplines;
		this.level = level;
		this.startTime = startTime;
		this.endTime = endTime;
		this.capacity = capacity;
		this.status = status;
	}
	public String getBatchName() {
		return BatchName;
	}
	public void setBatchName(String batchName) {
		BatchName = batchName;
	}
	public Disciplines getDisciplines() {
		return disciplines;
	}
	public void setDisciplines(Disciplines disciplines) {
		this.disciplines = disciplines;
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
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public BatchStatus getStatus() {
		return status;
	}
	public void setStatus(BatchStatus status) {
		this.status = status;
	}
	
	
}
