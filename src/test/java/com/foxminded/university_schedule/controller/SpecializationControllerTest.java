package com.foxminded.university_schedule.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.foxminded.university_schedule.model.entity.Specialization;
import com.foxminded.university_schedule.service.SpecializationService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class SpecializationControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	SpecializationService specializationService;

	@Test
	@WithMockUser(authorities = "READ")
	void getSpecializationPage_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialization"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getSpecializationPage_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialization"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getSpecializationPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialization"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getSpecializationPageTest() throws Exception {
		List<Specialization> specializations = new ArrayList<>();
		specializations.add(new Specialization());
		
		when(specializationService.getAll()).thenReturn(specializations);

		mockMvc.perform(MockMvcRequestBuilders.get("/specialization"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("specializations", equalTo(specializations)))
				.andExpect(MockMvcResultMatchers.model().attribute("specializations", hasSize(1)));

		verify(specializationService).getAll();
	}
}
