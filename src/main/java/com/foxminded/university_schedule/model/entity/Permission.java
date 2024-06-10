package com.foxminded.university_schedule.model.entity;

public enum Permission {
	EDIT_USERS("EDIT_USERS"),
	WRITE("WRITE"),
	READ("READ");

	private String name;

	Permission(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
