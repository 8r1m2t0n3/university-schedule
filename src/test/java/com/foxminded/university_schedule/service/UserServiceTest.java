package com.foxminded.university_schedule.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.foxminded.university_schedule.model.entity.Role;
import com.foxminded.university_schedule.model.entity.User;
import com.foxminded.university_schedule.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	UserRepository userRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	UserService userService;

	@Test
	void saveTest() {
		User user = new User();
		when(passwordEncoder.encode(user.getPassword())).thenReturn("1");
		when(userRepository.save(user)).thenReturn(user);

		userService.save(user);

		verify(passwordEncoder).encode(Mockito.any());
		verify(userRepository).save(user);
	}

	@Test
	void getByIdTest() {
		User user = new User();
		user.setId(1);
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		assertEquals(1, userService.getById(user.getId()).get().getId());

		verify(userRepository).findById(1);
	}

	@Test
	void getByUsernameTest() {
		User user = new User();
		user.setId(1);
		when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

		assertEquals(1, userService.getByUsername(user.getUsername()).get().getId());

		verify(userRepository).findByUsername(user.getUsername());
	}

	@Test
	void getAllTest() {
		when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
		
		assertEquals(2, userService.getAll().size());
		
		verify(userRepository).findAll();
	}

	@Test
	void isUsernameAlreadyUsed_shouldReturlFalse_whenUsernameIsNotAlreadyUsed() {
		String username = "username";
		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

		assertFalse(userService.isUsernameAlreadyUsed(username));

		verify(userRepository).findByUsername(username);
	}

	@Test
	void isUsernameAlreadyUsed_shouldReturlTrue_whenUsernameIsAlreadyUsed() {
		String username = "username";
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

		assertTrue(userService.isUsernameAlreadyUsed(username));

		verify(userRepository).findByUsername(username);
	}

	@Test
	void updateTest() {
		User user = new User();
		when(userRepository.save(user)).thenReturn(user);

		userService.update(user, 1);
		assertEquals(1, user.getId());

		verify(userRepository).save(user);
	}

	@Test
	void deleteByIdTest() {
		doNothing().when(userRepository).deleteById(1);

		userService.deleteById(1);

		verify(userRepository).deleteById(1);
	}
	
	@Test
	void isUsernameCorrect_shouldReturnFalse_whenUsernameIsAdmin() {
		assertEquals(false, userService.isUsernameCorrect("admin"));
	}
	
	@Test
	void isUsernameCorrect_shouldReturnFalse_whenUsernameIsShorterThanMinimalUsernameLength() {
		assertEquals(false, userService.isUsernameCorrect("123"));
	}
	
	@Test
	void isUsernameCorrect_shouldReturnTrue_whenUsernameIsLongerThanMinimalUsernameLength() {
		assertEquals(true, userService.isUsernameCorrect("1234"));
	}
	
	@Test
	void isPasswordCorrect_shouldReturnFalse_whenPasswordIsShorterThanMinimalPasswordLength() {
		assertEquals(false, userService.isPasswordCorrect("12345"));
	}
	
	@Test
	void isPasswordCorrect_shouldReturnTrue_whenPasswordIsLongerThanMinimalPasswordLength() {
		assertEquals(true, userService.isPasswordCorrect("123456"));
	}
	
	@Test
	void isStudent_shouldReturnFalse_whenUserIsNotStudent() {
		User user = new User();
		user.setRole(Role.TEACHER);
		assertEquals(false, userService.isStudent(user));
	}
	
	@Test
	void isStudent_shouldReturnTrue_whenUserIsStudent() {
		User user = new User();
		user.setRole(Role.STUDENT);
		assertEquals(true, userService.isStudent(user));
	}
	
	@Test
	void isTeacher_shouldReturnFalse_whenUserIsNotTeacher() {
		User user = new User();
		user.setRole(Role.STUDENT);
		assertEquals(false, userService.isTeacher(user));
	}
	
	@Test
	void isTeacher_shouldReturnTrue_whenUserIsTeacher() {
		User user = new User();
		user.setRole(Role.TEACHER);
		assertEquals(true, userService.isTeacher(user));
	}
}
