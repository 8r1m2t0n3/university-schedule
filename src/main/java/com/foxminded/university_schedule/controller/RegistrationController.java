package com.foxminded.university_schedule.controller;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university_schedule.model.dto.UserRegistrationDTO;
import com.foxminded.university_schedule.model.entity.Role;
import com.foxminded.university_schedule.model.entity.User;
import com.foxminded.university_schedule.service.StudentService;
import com.foxminded.university_schedule.service.TeacherService;
import com.foxminded.university_schedule.service.UserService;

@Controller
@RequestMapping("/auth")
public class RegistrationController {

	private UserService userService;
	private StudentService studentService;
	private TeacherService teacherService;
	private ModelMapper mapper;

	public RegistrationController(UserService userService, StudentService studentService,
			TeacherService teacherService) {
		this.userService = userService;
		this.studentService = studentService;
		this.teacherService = teacherService;
		this.mapper = new ModelMapper();
	}

	@GetMapping("/registration")
	public String getRregistrationPage(Model model) {
		model.addAttribute("user", new UserRegistrationDTO());

		return "auth/registration";
	}

	@PostMapping("/registration")
	public String registrationSubmit(@ModelAttribute("user") UserRegistrationDTO userDto, Model model) {
		if (!userService.isUsernameCorrect(userDto.getUsername())) {
			model.addAttribute("incorrectUsernameError", true);
			return "auth/registration";
		}
		if (userService.isUsernameAlreadyUsed(userDto.getUsername())) {
			model.addAttribute("duplicatedUsernameError", true);
			return "auth/registration";
		}
		if (!userService.isPasswordCorrect(userDto.getPassword())) {
			model.addAttribute("weakPasswordError", true);
			return "auth/registration";
		}
		if (userDto.getRoleNote() == null) {
			model.addAttribute("noRoleNoteError", true);
			return "auth/registration";
		}
		if (userDto.getRoleNote().equals(Role.STUDENT) && studentService
				.getByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName()).isEmpty()) {
			model.addAttribute("noStudentError", true);
			return "auth/registration";
		}
		if (userDto.getRoleNote().equals(Role.TEACHER) && teacherService
				.getByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName()).isEmpty()) {
			model.addAttribute("noTeacherError", true);
			return "auth/registration";
		}
		userService.save(mapper.map(userDto, User.class));

		return "auth/login";
	}
}
