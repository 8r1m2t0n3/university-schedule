package com.foxminded.university_schedule.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
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

import com.foxminded.university_schedule.model.entity.Role;
import com.foxminded.university_schedule.model.entity.User;
import com.foxminded.university_schedule.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class MenuControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;

	@Test
	void getMenuPage_shouldAccess_whenUserHasAnyAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getMenuPage_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getMenuPage_shouldAccess_whenUserHasWriteAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getMenuPage_shouldAccess_whenUserHasEditUsersAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void getMenuPage_shouldSetUserLoggedAndUserAdminToFalse_whenCurrentUserIsNull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("userLogged", equalTo(false)))
				.andExpect(MockMvcResultMatchers.model().attribute("userAdmin", equalTo(false)));
	}

	@Test
	@WithMockUser(username = "loggedUser")
	void getMenuPage_shouldSetUserLoggedToTrue_whenCurrsenUserIsLogged() throws Exception {
		User user = new User();
		user.setRole(Role.TEACHER);
		when(userService.getByUsername("loggedUser")).thenReturn(Optional.of(user));
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("userLogged", equalTo(true)));

		verify(userService).getByUsername("loggedUser");
	}

	@Test
	void getMenuPage_shouldSetUserLoggedToFalse_whenCurrsenUserIsNotLogged() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("userLogged", equalTo(false)));
	}

	@Test
	@WithMockUser(username = "student")
	void getMenuPage_shouldSetUserLoggedAndUserStudentToTrue_whenCurrentUserIsStudent() throws Exception {
		User user = new User();
		user.setRole(Role.STUDENT);
		when(userService.getByUsername("student")).thenReturn(Optional.of(user));
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("userLogged", equalTo(true)))
				.andExpect(MockMvcResultMatchers.model().attribute("userStudent", equalTo(true)));

		verify(userService).getByUsername("student");
	}

	@Test
	@WithMockUser(username = "teacher")
	void getMenuPage_shouldSetUserLoggedAndUserTeacherToTrue_whenCurrentUserIsTeacher() throws Exception {
		User user = new User();
		user.setRole(Role.TEACHER);
		when(userService.getByUsername("teacher")).thenReturn(Optional.of(user));
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("userLogged", equalTo(true)))
				.andExpect(MockMvcResultMatchers.model().attribute("userTeacher", equalTo(true)));

		verify(userService).getByUsername("teacher");
	}

	@Test
	@WithMockUser(username = "admin")
	void getMenuPage_shouldSetUserLoggedAndUserAdminToTrue_whenCurrentUserIsAdmin() throws Exception {
		User user = new User();
		user.setRole(Role.ADMIN);
		when(userService.getByUsername("admin")).thenReturn(Optional.of(user));
		mockMvc.perform(MockMvcRequestBuilders.get("/menu")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("userLogged", equalTo(true)))
				.andExpect(MockMvcResultMatchers.model().attribute("userAdmin", equalTo(true)));

		verify(userService).getByUsername("admin");
	}

}
