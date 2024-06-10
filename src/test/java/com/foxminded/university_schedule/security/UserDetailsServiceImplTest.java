package com.foxminded.university_schedule.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.foxminded.university_schedule.model.entity.Role;
import com.foxminded.university_schedule.model.entity.User;
import com.foxminded.university_schedule.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserDetailsServiceImpl userDetailsService;

	@Test
	void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUsernameDoesNotExist() {
		String username = "username";
		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> {
			userDetailsService.loadUserByUsername(username);
		});

		verify(userRepository).findByUsername(username);
	}

	@Test
	void loadUserByUsername_shouldReturnSecurityUser_whenUsernameExist() {
		String username = "username";
		User user = new User();
		user.setPassword("pass");
		user.setUsername(username);
		user.setRole(Role.ADMIN);

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		assertEquals(username, userDetailsService.loadUserByUsername(username).getUsername());

		verify(userRepository).findByUsername(username);
	}
}
