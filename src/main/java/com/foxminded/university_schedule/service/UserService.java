package com.foxminded.university_schedule.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foxminded.university_schedule.model.Role;
import com.foxminded.university_schedule.model.User;
import com.foxminded.university_schedule.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService extends BaseService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	private Logger logger = LoggerFactory.getLogger(UserService.class);

	private static final Integer MIN_PASSWORD_LENGTH = 5;
	private static final Integer MIN_USERNAME_LENGTH = 3;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		logger.info("Saved user with username: {}", user.getUsername());
	}

	public Optional<User> getById(Integer userId) {
		return userRepository.findById(userId);
	}

	public Optional<User> getByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public Boolean isUsernameAlreadyUsed(String username) {
		return userRepository.findByUsername(username).isPresent();
	}

	@Transactional
	public void update(User user, Integer userId) {
		user.setId(userId);
		userRepository.save(user);
		logger.info("Updated user with id: {}", user.getId());
	}

	@Transactional
	public void deleteById(Integer userId) {
		userRepository.deleteById(userId);
		logger.info("Deleted user with id: {}", userId);
	}

	public Boolean isUsernameCorrect(String username) {
		if (username.equalsIgnoreCase("admin")) {
			return false;
		}
		return username.length() > MIN_USERNAME_LENGTH;
	}

	public Boolean isPasswordCorrect(String password) {
		return password.length() > MIN_PASSWORD_LENGTH;
	}

	public Boolean isStudent(User user) {
		return user.getRole().equals(Role.STUDENT);
	}

	public Boolean isTeacher(User user) {
		return user.getRole().equals(Role.TEACHER);
	}
}
