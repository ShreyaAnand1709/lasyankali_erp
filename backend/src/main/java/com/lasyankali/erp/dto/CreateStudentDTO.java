package com.lasyankali.erp.dto;

import java.time.LocalDate;

import com.lasyankali.erp.entity.enums.Gender;

public class CreateStudentDTO {

    // =========================
    // STUDENT USER DETAILS
    // =========================

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String mobileNumber;


    // =========================
    // STUDENT DETAILS
    // =========================

    private String admissionNumber;
    private Gender gender;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    private String photoUrl;


    // =========================
    // PARENT USER DETAILS
    // =========================

    private String parentFirstName;
    private String parentLastName;
    private String parentUsername;
    private String parentEmail;
    private String parentPassword;
    private String parentMobileNumber;


    // =========================
    // PARENT DETAILS
    // =========================

    private String occupation;
    private String address;
	public CreateStudentDTO() {
		super();
	}
	public CreateStudentDTO(String firstName, String lastName, String username, String email, String password,
			String mobileNumber, String admissionNumber, Gender gender, LocalDate dateOfBirth, LocalDate joiningDate,
			String photoUrl, String parentFirstName, String parentLastName, String parentUsername, String parentEmail,
			String parentPassword, String parentMobileNumber, String occupation, String address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.mobileNumber = mobileNumber;
		this.admissionNumber = admissionNumber;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.joiningDate = joiningDate;
		this.photoUrl = photoUrl;
		this.parentFirstName = parentFirstName;
		this.parentLastName = parentLastName;
		this.parentUsername = parentUsername;
		this.parentEmail = parentEmail;
		this.parentPassword = parentPassword;
		this.parentMobileNumber = parentMobileNumber;
		this.occupation = occupation;
		this.address = address;
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
	public String getParentFirstName() {
		return parentFirstName;
	}
	public void setParentFirstName(String parentFirstName) {
		this.parentFirstName = parentFirstName;
	}
	public String getParentLastName() {
		return parentLastName;
	}
	public void setParentLastName(String parentLastName) {
		this.parentLastName = parentLastName;
	}
	public String getParentUsername() {
		return parentUsername;
	}
	public void setParentUsername(String parentUsername) {
		this.parentUsername = parentUsername;
	}
	public String getParentEmail() {
		return parentEmail;
	}
	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}
	public String getParentPassword() {
		return parentPassword;
	}
	public void setParentPassword(String parentPassword) {
		this.parentPassword = parentPassword;
	}
	public String getParentMobileNumber() {
		return parentMobileNumber;
	}
	public void setParentMobileNumber(String parentMobileNumber) {
		this.parentMobileNumber = parentMobileNumber;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
