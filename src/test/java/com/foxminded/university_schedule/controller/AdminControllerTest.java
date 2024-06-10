package com.foxminded.university_schedule.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

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

import com.foxminded.university_schedule.model.entity.User;
import com.foxminded.university_schedule.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class AdminControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;

	@Test
	@WithMockUser(authorities = "READ")
	void getAdminPanelPage_shouldNotAccess_whenUserHasReadAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getAdminPanelPage_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getAdminPanelPage_shouldAccess_whenUserHasEditUsersAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getDBModelPage_shouldNotAccess_whenUserHasReadAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel/model-menu"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getDBModelPage_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel/model-menu"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getDBModelPage_shouldAccess_whenUserHasEditUsersAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel/model-menu"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getUserAdministrationPage_shouldNotAccess_whenUserHasReadAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel/user-editor"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getUserAdministrationPage_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel/user-editor"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getUserAdministrationPage_shouldAccess_whenUserHasEditUsersAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel/user-editor"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getUserAdministrationPageTest() throws Exception {
		when(userService.getAll()).thenReturn(List.of(new User()));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/admin-panel/user-editor"))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(userService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void updateUser_shouldNotAccess_whenUserHasReadAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin-panel/user-editor/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void updateUser_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin-panel/user-editor/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateUser_shouldAccess_whenUserHasEditUsersAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin-panel/user-editor/update"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = { "READ", "WRITE" })
	void deleteUser_shouldNotAccess_whenUserHasReadOrWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin-panel/user-editor/delete"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteUser_shouldAccess_whenUserHasEditUsersAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin-panel/user-editor/delete"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateUserTest() throws Exception {
		User user = new User();
		doNothing().when(userService).update(user, user.getId());
		when(userService.getAll()).thenReturn(List.of(user));

		mockMvc.perform(MockMvcRequestBuilders.post("/admin-panel/user-editor/update").flashAttr("user", user))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(userService).update(user, user.getId());
		verify(userService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteUserTest() throws Exception {
		User user = new User();
		user.setId(1);
		doNothing().when(userService).deleteById(user.getId());
		when(userService.getAll()).thenReturn(List.of(user));

		mockMvc.perform(MockMvcRequestBuilders.post("/admin-panel/user-editor/delete").flashAttr("user", user))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(userService).deleteById(user.getId());
		verify(userService).getAll();
	}
}
