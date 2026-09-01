package com.lasyankali.erp.dto;

import java.time.LocalTime;

import com.lasyankali.erp.entity.enums.BatchLevel;
import com.lasyankali.erp.entity.enums.BatchStatus;
import com.lasyankali.erp.entity.enums.Disciplines;

public class CreateBatchDTO {
	private String batchCode;
	private String batchName;
	private Disciplines discipline;
	private BatchLevel level;
	private LocalTime startTime;
	private LocalTime endTime;
	private Integer capacity;
	private BatchStatus status;
	public CreateBatchDTO(String batchCode, String batchName, Disciplines discipline, BatchLevel level,
			LocalTime startTime, LocalTime endTime, Integer capacity, BatchStatus status) {
		super();
		this.batchCode = batchCode;
		this.batchName = batchName;
		this.discipline = discipline;
		this.level = level;
		this.startTime = startTime;
		this.endTime = endTime;
		this.capacity = capacity;
		this.status = status;
	}
	public CreateBatchDTO() {
		super();
	}
	public String getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public Disciplines getDiscipline() {
		return discipline;
	}
	public void setDiscipline(Disciplines discipline) {
		this.discipline = discipline;
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
