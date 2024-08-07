package com.brimstone.university_schedule.model.entity;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
	ADMIN(List.of(Permission.EDIT_USERS, Permission.READ, Permission.WRITE)),
	STUDENT(List.of(Permission.READ)),
	TEACHER(List.of(Permission.READ, Permission.WRITE));

	private List<Permission> permissions;

	Role(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public List<SimpleGrantedAuthority> getAuthorities() {
		return getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getName())).toList();
	}
}
