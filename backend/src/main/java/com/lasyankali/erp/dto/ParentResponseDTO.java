package com.lasyankali.erp.dto;

public class ParentResponseDTO {
	// User Details
	private Long parentId;
    private String firstName;
    private String username;
    private String lastName;
    private String email;
    private String mobileNumber;

    // Parent Details
    private String occupation;
    private String address;
	public ParentResponseDTO() {
		super();
	}
	public ParentResponseDTO(Long parentId,String firstName, String lastName,String username, String email, String mobileNumber, String occupation,
			String address) {
		super();
		this.parentId = parentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.occupation = occupation;
		this.address = address;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
