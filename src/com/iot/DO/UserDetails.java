package com.iot.DO;

public class UserDetails {

	private String userId;
	private String password;
	private String firstName;
	private String middleName;
	private String lastName;
	private String emailId;
	private Integer imageId;
	private Double walletBalance;
	
	public UserDetails() {
		super();

	}
	
	public UserDetails(String userId, String password, String firstName, String middleName,
			String lastName, String emailId, Integer imageId, double walletBalance) {
		super();
		this.userId = userId;
		this.password = password;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.imageId = imageId;
		this.walletBalance = walletBalance;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Integer getImagePath() {
		return imageId;
	}
	public void setImagePath(Integer imageId) {
		this.imageId = imageId;
	}
	public double getWalletBalance() {
		return walletBalance;
	}
	public void setWalletBalance(double walletBalance) {
		this.walletBalance = walletBalance;
	}	

	@Override
	public String toString() {
		return "UserDetails [user_id=" + userId + ", password=" + password + ", first_name=" + firstName
				+ ", middle_name=" + middleName + ", last_name=" + lastName + ", email_id=" + emailId
				+ ", image_path=" + imageId + ", wallet_balance=" + walletBalance + "]";
	}	
}
