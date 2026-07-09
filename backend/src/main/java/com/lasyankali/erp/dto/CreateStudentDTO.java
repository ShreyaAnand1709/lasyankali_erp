package com.lasyankali.erp.dto;

import java.time.LocalDate;

public class CreateStudentDTO {
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String mobileNumber;
	private Long parentId;
	private String admissionNumber;
	private String gender;
	private LocalDate dateOfBirth;
	private LocalDate joiningDate;
	private String photoUrl;
	public CreateStudentDTO(String firstName, String lastName, String username, String email, String password,
			String mobileNumber, Long parentId, String admissionNumber, String gender, LocalDate dateOfBirth,
			LocalDate joiningDate, String photoUrl) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.mobileNumber = mobileNumber;
		this.parentId = parentId;
		this.admissionNumber = admissionNumber;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.joiningDate = joiningDate;
		this.photoUrl = photoUrl;
	}
	public CreateStudentDTO() {
		super();
	}
	public String getAdmissionNumber() {
		return admissionNumber;
	}
	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDate getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
