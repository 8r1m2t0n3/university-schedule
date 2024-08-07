package com.brimstone.university_schedule.controller;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class LoginControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void getLoginPage_shouldAccess_whenUserHasAnyAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/login"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void getLoginPage_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/login"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void getLoginPage_shouldAccess_whenUserHasWriteAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/login"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getLoginPage_shouldAccess_whenUserHasEditUsersAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/login"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	void getLoginErrorPage_shouldAccess_whenUserHasAnyAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/login-error"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void getLoginErrorPage_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/login-error"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void getLoginErrorPage_shouldAccess_whenUserHasWriteAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/login-error"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getLoginErrorPage_shouldAccess_whenUserHasEditUsersAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/login-error"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void loginErrorTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/auth/login-error"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("loginError", equalTo(true)));
	}
}
