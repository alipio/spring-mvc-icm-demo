package com.github.kolorobot.icm.signup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.github.kolorobot.icm.account.Account;

class SignupForm {

	public enum Role {
		ROLE_USER, ROLE_ADMIN, ROLE_EMPLOYEE;
	}

	@NotBlank
	@Size(max = 49)
	private String name;
	@NotBlank
	@Email
	private String email;

	private String phone;
	@NotBlank
	@Size(max = 50)
	private String password;
	@NotBlank
	@Size(max = 50)
	private String confirmedPassword;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public Account createUserAccount() {
		Account account = new Account(getName(), getEmail(), getPassword(), Role.ROLE_USER.name());
		account.setPhone(getPhone());
		return account;
	}

	public String getPhone() {
		if (phone != null && phone.isEmpty()) {
			return null;
		}
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
