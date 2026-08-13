package com.lasyankali.erp.dto;

import java.time.LocalDate;

public class UpdateTeacherDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;
	private String employeeCode;
	private String specialization;
	private String qualification;
	private Integer yearsOfExperience;
	private LocalDate joiningDate;
	private String status;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public Integer getYearsOfExperience() {
		return yearsOfExperience;
	}
	public void setYearsOfExperience(Integer yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}
	public LocalDate getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UpdateTeacherDTO(String firstName, String lastName, String email, String mobileNumber, String employeeCode,
			String specialization, String qualification, Integer yearsOfExperience, LocalDate joiningDate,
			String status) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.employeeCode = employeeCode;
		this.specialization = specialization;
		this.qualification = qualification;
		this.yearsOfExperience = yearsOfExperience;
		this.joiningDate = joiningDate;
		this.status = status;
	}
	public UpdateTeacherDTO() {
		super();
	}
}
