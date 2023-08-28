package org.tfl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Users {
    private String userId;
    private String firstName;
    private String lastName;
    private String salt;
    private String password;
    private String otpSecret;
    private int isLocked;
    private int failLogin;
    private int label;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOtpSecret() {
		return otpSecret;
	}
	public void setOtpSecret(String otpSecret) {
		this.otpSecret = otpSecret;
	}
	public int getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(int isLocked) {
		this.isLocked = isLocked;
	}
	public int getFailLogin() {
		return failLogin;
	}
	public void setFailLogin(int failLogin) {
		this.failLogin = failLogin;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public Users(String userId, String firstName, String lastName, String salt, String password, String otpSecret) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salt = salt;
		this.password = password;
		this.otpSecret = otpSecret;
	}
	public Users() {
		super();
	}


}
