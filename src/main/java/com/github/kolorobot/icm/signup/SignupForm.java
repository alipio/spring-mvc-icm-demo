package com.github.kolorobot.icm.signup;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.*;

import com.github.kolorobot.icm.account.Account;

class SignupForm {

	public enum Role {
		ROLE_USER, ROLE_ADMIN;
	}

	@NotBlank
	private String name;
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String confirmedPassword;
	@NotNull
	private Role role = Role.ROLE_USER;

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Account createAccount() {
		return new Account(getName(), getEmail(), getPassword(), getRole().toString());
	}

}
