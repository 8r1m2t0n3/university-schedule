package com.foxminded.university_schedule.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.foxminded.university_schedule.model.dto.UserRegistrationDTO;
import com.foxminded.university_schedule.model.entity.Role;
import com.foxminded.university_schedule.model.entity.Student;
import com.foxminded.university_schedule.model.entity.Teacher;
import com.foxminded.university_schedule.model.entity.User;
import com.foxminded.university_schedule.service.StudentService;
import com.foxminded.university_schedule.service.TeacherService;
import com.foxminded.university_schedule.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RegistrationControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;

	@MockBean
	StudentService studentService;

	@MockBean
	TeacherService teacherService;

	@MockBean
	ModelMapper mapper;

	@Test
	void getRegistrationPage_shouldAccess_whenUserHasAnyAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/registration"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getRegistrationPage_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/registration"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getRegistrationPage_shouldAccess_whenUserHasWriteAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/registration"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getRegistrationPage_shouldAccess_whenUserHasEditUsersAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/registration"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void registrationSubmit_shouldSetUsernameErrorToTrue_whenUsernameIsCorrectAndUsernameIsUniqueAndPasswordCorrectAndRoleNoteSetAndUserAsStudentExist()
			throws Exception {
		UserRegistrationDTO userDto = new UserRegistrationDTO();
		userDto.setUsername("username");
		userDto.setFirstName("A");
		userDto.setLastName("Aa");
		userDto.setPassword("123456");
		userDto.setRoleNote(Role.STUDENT);

		when(userService.isUsernameCorrect(userDto.getUsername())).thenReturn(true);
		when(userService.isUsernameAlreadyUsed(userDto.getUsername())).thenReturn(false);
		when(userService.isPasswordCorrect(userDto.getPassword())).thenReturn(true);
		when(studentService.getByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName()))
				.thenReturn(Optional.of(new Student()));

		User user = mapper.map(userDto, User.class);

		doNothing().when(userService).save(user);

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration").flashAttr("user", userDto))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("incorrectUsernameError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("duplicatedUsernameError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("weakPasswordError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("noRoleNoteError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("noStudentError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("noTeacherError", equalTo(null)));

		verify(userService).isUsernameCorrect("username");
		verify(userService).isUsernameAlreadyUsed("username");
		verify(userService).isPasswordCorrect("123456");
		verify(studentService).getByFirstNameAndLastName("A", "Aa");
		verify(userService).save(any(User.class));
	}

	@Test
	void registrationSubmit_shouldSetUsernameErrorToTrue_whenUsernameIsCorrectAndUsernameIsUniqueAndPasswordCorrectAndRoleNoteSetAndUserAsTeacherExist()
			throws Exception {
		UserRegistrationDTO userDto = new UserRegistrationDTO();
		userDto.setUsername("username");
		userDto.setFirstName("AA");
		userDto.setLastName("AAa");
		userDto.setPassword("123456");
		userDto.setRoleNote(Role.TEACHER);

		when(userService.isUsernameCorrect(userDto.getUsername())).thenReturn(true);
		when(userService.isUsernameAlreadyUsed(userDto.getUsername())).thenReturn(false);
		when(userService.isPasswordCorrect(userDto.getPassword())).thenReturn(true);
		when(teacherService.getByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName()))
				.thenReturn(Optional.of(new Teacher()));

		User user = mapper.map(userDto, User.class);

		doNothing().when(userService).save(user);

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration").flashAttr("user", userDto))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("incorrectUsernameError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("duplicatedUsernameError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("weakPasswordError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("noRoleNoteError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("noStudentError", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("noTeacherError", equalTo(null)));

		verify(userService).isUsernameCorrect("username");
		verify(userService).isUsernameAlreadyUsed("username");
		verify(userService).isPasswordCorrect("123456");
		verify(teacherService).getByFirstNameAndLastName("AA", "AAa");
		verify(userService).save(any(User.class));
	}
	
	@Test
	void registrationSubmit_shouldSetUsernameErrorToFalse_whenUsernameIsCorrectAndUsernameIsUniqueAndPasswordCorrectAndRoleNoteSetAndUserAsStudentNotExist()
			throws Exception {
		UserRegistrationDTO userDto = new UserRegistrationDTO();
		userDto.setUsername("username");
		userDto.setFirstName("AA");
		userDto.setLastName("AAa");
		userDto.setPassword("123456");
		userDto.setRoleNote(Role.STUDENT);
		
		when(userService.isUsernameCorrect(userDto.getUsername())).thenReturn(true);
		when(userService.isUsernameAlreadyUsed(userDto.getUsername())).thenReturn(false);
		when(userService.isPasswordCorrect(userDto.getPassword())).thenReturn(true);
		when(studentService.getByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName()))
		.thenReturn(Optional.empty());
		
		User user = mapper.map(userDto, User.class);
		
		doNothing().when(userService).save(user);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration").flashAttr("user", userDto))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("incorrectUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("duplicatedUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("weakPasswordError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noRoleNoteError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noStudentError", equalTo(true)))
		.andExpect(MockMvcResultMatchers.model().attribute("noTeacherError", equalTo(null)));
		
		verify(userService).isUsernameCorrect("username");
		verify(userService).isUsernameAlreadyUsed("username");
		verify(userService).isPasswordCorrect("123456");
		verify(studentService).getByFirstNameAndLastName("AA", "AAa");
		verify(userService, never()).save(any(User.class));
	}
	
	@Test
	void registrationSubmit_shouldSetUsernameErrorToFalse_whenUsernameIsCorrectAndUsernameIsUniqueAndPasswordCorrectAndRoleNoteSetAndUserAsTeacherNotExist()
			throws Exception {
		UserRegistrationDTO userDto = new UserRegistrationDTO();
		userDto.setUsername("username");
		userDto.setFirstName("AA");
		userDto.setLastName("AAa");
		userDto.setPassword("123456");
		userDto.setRoleNote(Role.TEACHER);
		
		when(userService.isUsernameCorrect(userDto.getUsername())).thenReturn(true);
		when(userService.isUsernameAlreadyUsed(userDto.getUsername())).thenReturn(false);
		when(userService.isPasswordCorrect(userDto.getPassword())).thenReturn(true);
		when(teacherService.getByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName()))
		.thenReturn(Optional.empty());
		
		User user = mapper.map(userDto, User.class);
		
		doNothing().when(userService).save(user);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration").flashAttr("user", userDto))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("incorrectUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("duplicatedUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("weakPasswordError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noRoleNoteError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noStudentError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noTeacherError", equalTo(true)));
		
		verify(userService).isUsernameCorrect("username");
		verify(userService).isUsernameAlreadyUsed("username");
		verify(userService).isPasswordCorrect("123456");
		verify(teacherService).getByFirstNameAndLastName("AA", "AAa");
		verify(userService, never()).save(any(User.class));
	}
	
	@Test
	void registrationSubmit_shouldSetUsernameErrorToFalse_whenUsernameIsCorrectAndUsernameIsUniqueAndPasswordCorrectAndRoleNoteNotSet()
			throws Exception {
		UserRegistrationDTO userDto = new UserRegistrationDTO();
		userDto.setUsername("username");
		userDto.setFirstName("AA");
		userDto.setLastName("AAa");
		userDto.setPassword("123456");
		
		when(userService.isUsernameCorrect(userDto.getUsername())).thenReturn(true);
		when(userService.isUsernameAlreadyUsed(userDto.getUsername())).thenReturn(false);
		when(userService.isPasswordCorrect(userDto.getPassword())).thenReturn(true);
		
		User user = mapper.map(userDto, User.class);
		
		doNothing().when(userService).save(user);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration").flashAttr("user", userDto))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("incorrectUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("duplicatedUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("weakPasswordError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noRoleNoteError", equalTo(true)))
		.andExpect(MockMvcResultMatchers.model().attribute("noStudentError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noTeacherError", equalTo(null)));
		
		verify(userService).isUsernameCorrect("username");
		verify(userService).isUsernameAlreadyUsed("username");
		verify(userService).isPasswordCorrect("123456");
		verify(userService, never()).save(any(User.class));
	}
	
	@Test
	void registrationSubmit_shouldSetUsernameErrorToFalse_whenUsernameIsCorrectAndUsernameIsUniqueAndPasswordNotCorrect()
			throws Exception {
		UserRegistrationDTO userDto = new UserRegistrationDTO();
		userDto.setUsername("username");
		userDto.setPassword("1");
		
		when(userService.isUsernameCorrect(userDto.getUsername())).thenReturn(true);
		when(userService.isUsernameAlreadyUsed(userDto.getUsername())).thenReturn(false);
		when(userService.isPasswordCorrect(userDto.getPassword())).thenReturn(false);
		
		User user = mapper.map(userDto, User.class);
		
		doNothing().when(userService).save(user);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration").flashAttr("user", userDto))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("incorrectUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("duplicatedUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("weakPasswordError", equalTo(true)))
		.andExpect(MockMvcResultMatchers.model().attribute("noRoleNoteError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noStudentError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noTeacherError", equalTo(null)));
		
		verify(userService).isUsernameCorrect("username");
		verify(userService).isUsernameAlreadyUsed("username");
		verify(userService).isPasswordCorrect("1");
		verify(userService, never()).save(any(User.class));
	}
	
	@Test
	void registrationSubmit_shouldSetUsernameErrorToFalse_whenUsernameIsCorrectAndUsernameIsNotUnique()
			throws Exception {
		UserRegistrationDTO userDto = new UserRegistrationDTO();
		userDto.setUsername("username");
		
		when(userService.isUsernameCorrect(userDto.getUsername())).thenReturn(true);
		when(userService.isUsernameAlreadyUsed(userDto.getUsername())).thenReturn(true);
		
		User user = mapper.map(userDto, User.class);
		
		doNothing().when(userService).save(user);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration").flashAttr("user", userDto))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("incorrectUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("duplicatedUsernameError", equalTo(true)))
		.andExpect(MockMvcResultMatchers.model().attribute("weakPasswordError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noRoleNoteError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noStudentError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noTeacherError", equalTo(null)));
		
		verify(userService).isUsernameCorrect("username");
		verify(userService).isUsernameAlreadyUsed("username");
		verify(userService, never()).save(any(User.class));
	}
	
	@Test
	void registrationSubmit_shouldSetUsernameErrorToFalse_whenUsernameIsNotCorrect()
			throws Exception {
		UserRegistrationDTO userDto = new UserRegistrationDTO();
		userDto.setUsername("username");
		
		when(userService.isUsernameCorrect(userDto.getUsername())).thenReturn(false);
		
		User user = mapper.map(userDto, User.class);
		
		doNothing().when(userService).save(user);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration").flashAttr("user", userDto))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("incorrectUsernameError", equalTo(true)))
		.andExpect(MockMvcResultMatchers.model().attribute("duplicatedUsernameError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("weakPasswordError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noRoleNoteError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noStudentError", equalTo(null)))
		.andExpect(MockMvcResultMatchers.model().attribute("noTeacherError", equalTo(null)));
		
		verify(userService).isUsernameCorrect("username");
		verify(userService, never()).save(any(User.class));
	}
}
