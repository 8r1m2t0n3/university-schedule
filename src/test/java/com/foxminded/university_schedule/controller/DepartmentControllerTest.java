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

import com.foxminded.university_schedule.model.entity.Department;
import com.foxminded.university_schedule.service.DepartmentService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class DepartmentControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	DepartmentService departmentService;
	
	@Test
	@WithMockUser(authorities = "READ")
	void getDepartmentPage_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/department"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void getDepartmentPage_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/department"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getDepartmentPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/department"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getDepartmentPageTest() throws Exception {
		List<Department> departments = new ArrayList<>();
		departments.add(new Department());
		
		when(departmentService.getAll()).thenReturn(departments);

		mockMvc.perform(MockMvcRequestBuilders.get("/department"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("departments", equalTo(departments)))
				.andExpect(MockMvcResultMatchers.model().attribute("departments", hasSize(1)));

		verify(departmentService).getAll();
	}
}
