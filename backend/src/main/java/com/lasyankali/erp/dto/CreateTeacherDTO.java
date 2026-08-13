package com.lasyankali.erp.dto;

import java.time.LocalDate;

public class CreateTeacherDTO {
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String mobileNumber;
	private String employeeCode;
	private String specialization;
	private String qualification;
	private Integer yearsOfExperience;
	private LocalDate joiningDate;
	public CreateTeacherDTO() {
		super();
	}
	public CreateTeacherDTO(String firstName, String lastName, String username, String email, String password,
			String mobileNumber, String employeeCode, String specialization, String qualification,
			Integer yearsOfExperience, LocalDate joiningDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.mobileNumber = mobileNumber;
		this.employeeCode = employeeCode;
		this.specialization = specialization;
		this.qualification = qualification;
		this.yearsOfExperience = yearsOfExperience;
		this.joiningDate = joiningDate;
	}
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public void setSpecification(String specialization) {
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
}
