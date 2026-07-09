package com.lasyankali.erp.dto;

import java.time.LocalDate;

public class StudentResponseDTO {
	private Long studentId;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String mobileNumber;
	private String admissionNumber;
	private String gender;
	private LocalDate joiningDate;
	private String photoUrl;
	private String status;
	public StudentResponseDTO() {
		super();
	}
	
	public StudentResponseDTO(Long studentId, String firstName, String lastName, String username, String email,
			String mobileNumber, String admissionNumber, String gender, LocalDate joiningDate, String photoUrl,
			String status) {
		super();
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.admissionNumber = admissionNumber;
		this.gender = gender;
		this.joiningDate = joiningDate;
		this.photoUrl = photoUrl;
		this.status = status;
	}

	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
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
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getAdmissionNumber() {
		return admissionNumber;
	}
	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public LocalDate getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
}
