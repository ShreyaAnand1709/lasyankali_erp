package com.lasyankali.erp.dto;

import java.time.LocalDate;

import com.lasyankali.erp.entity.enums.Gender;
import com.lasyankali.erp.entity.enums.Status;

public class UpdateStudentDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;
	private String admissionNumber;
	private Gender gender;
	private LocalDate dateOfBirth;
	private LocalDate joiningDate;
	private String photoUrl;
	private Status status;
	public UpdateStudentDTO(String firstName, String lastName, String email, String mobileNumber,
			String admissionNumber, Gender gender, LocalDate dateOfBirth, LocalDate joiningDate, String photoUrl,
			Status status) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.admissionNumber = admissionNumber;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.joiningDate = joiningDate;
		this.photoUrl = photoUrl;
		this.status = status;
	}
	public UpdateStudentDTO() {
		super();
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
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
	
}
