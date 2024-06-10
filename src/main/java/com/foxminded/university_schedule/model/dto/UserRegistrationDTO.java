package com.foxminded.university_schedule.model.dto;

import org.springframework.stereotype.Component;

import com.foxminded.university_schedule.model.entity.Role;

@Component
public class UserRegistrationDTO {

	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private Role roleNote;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRoleNote() {
		return roleNote;
	}

	public void setRoleNote(Role roleNote) {
		this.roleNote = roleNote;
	}

}
