package com.lasyankali.erp.dto;

import java.time.LocalDate;

import com.lasyankali.erp.entity.enums.Gender;
import com.lasyankali.erp.entity.enums.Status;

public class StudentResponseDTO {
	private Long studentId;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String mobileNumber;
	private String admissionNumber;
	private Gender gender;
	private LocalDate joiningDate;
	private String photoUrl;
	private Status status;
	private LocalDate dateOfBirth;
	public StudentResponseDTO() {
		super();
	}
	
	public StudentResponseDTO(Long studentId, String firstName, String lastName, String username, String email,
			String mobileNumber, String admissionNumber, Gender gender, LocalDate joiningDate, String photoUrl,
			Status status, LocalDate dateOfBirth) {
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
		this.dateOfBirth = dateOfBirth;
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
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
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
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public LocalDate getDateOfBirth() {
	    return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
	    this.dateOfBirth = dateOfBirth;
	}
}
