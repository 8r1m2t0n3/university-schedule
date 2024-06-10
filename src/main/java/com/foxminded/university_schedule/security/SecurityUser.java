package com.foxminded.university_schedule.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.foxminded.university_schedule.model.entity.User;

public class SecurityUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private List<SimpleGrantedAuthority> authorities;
	private Boolean isActive;

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isActive;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isActive;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isActive;
	}

	@Override
	public boolean isEnabled() {
		return isActive;
	}

	public static UserDetails fromUser(User user) {
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				true, true, true, true, user.getRole().getAuthorities());
	}
}
