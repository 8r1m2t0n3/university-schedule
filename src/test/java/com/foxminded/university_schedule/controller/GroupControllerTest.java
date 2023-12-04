package com.foxminded.university_schedule.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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

import com.foxminded.university_schedule.model.Group;
import com.foxminded.university_schedule.model.Specialization;
import com.foxminded.university_schedule.model.Student;
import com.foxminded.university_schedule.service.GroupService;
import com.foxminded.university_schedule.service.SpecializationService;
import com.foxminded.university_schedule.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class GroupControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	GroupService groupService;

	@MockBean
	StudentService studentService;

	@MockBean
	SpecializationService specializationService;

	@Test
	@WithMockUser(authorities = "READ")
	void getGroupViewPage_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/group/view")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getGroupViewPage_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/group/view")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getGroupViewPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/group/view")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void getGroupViewPage_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/group/view")).andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getGroupViewPageTest() throws Exception {
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/group/view"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectStudent_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void selectStudent_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectStudent_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectStudent_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectStudent_shouldSelectStudent_whenStudentExistAndStudentRelatedToGroup() throws Exception {
		Student student = new Student();
		student.setId(1);
		Group group = new Group();
		student.setGroup(group);

		when(studentService.getById(student.getId())).thenReturn(Optional.of(student));

		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService).getAll();
		verify(studentService).getAll();
		verify(studentService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectStudent_shouldNotSelectStudent_whenStudentNotExist() throws Exception {
		when(studentService.getById(1))
				.thenReturn(Optional.empty());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-student")
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(specializationService).getAll();
		verify(studentService).getAll();
		verify(studentService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectStudent_shouldSelectStudent_whenStudentExistAndStudentNotRelatedToGroup() throws Exception {
		Student student = new Student();
		student.setId(1);

		when(studentService.getById(student.getId())).thenReturn(Optional.of(student));

		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService).getAll();
		verify(studentService).getAll();
		verify(studentService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectSpecialization_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/select-specialization").flashAttr("specializationId", anyInt()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void selectSpecialization_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/select-specialization").flashAttr("specializationId", anyInt()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectSpecialization_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/select-specialization").flashAttr("specializationId", anyInt()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectSpecialization_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-specialization"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectSpecialization_shouldSelectSpecialization_whenSpecializationExist() throws Exception {
		Specialization specialization = new Specialization();
		specialization.setId(1);
		Group group = new Group();
		specialization.addGroup(group);

		when(specializationService.getById(specialization.getId())).thenReturn(Optional.of(specialization));
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-specialization").flashAttr("specializationId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService).getById(1);
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectSpecialization_shouldNotSelectSpecialization_whenSpecializationNotExist() throws Exception {
		when(specializationService.getById(1)).thenReturn(Optional.empty());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/select-specialization").flashAttr("specializationId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService).getById(1);
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getGroupPage_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/group")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getGroupPage_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/group")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getGroupPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/group")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getGroupPageTest() throws Exception {
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/group"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void createGroup_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void createGroup_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/create")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createGroupTest() throws Exception {
		Group group = new Group();
		doNothing().when(groupService).save(group);

		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/create")).andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).save(group);
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void updateGroup_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void updateGroup_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/update")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateGroup_shouldUpdateGroupNameAndGroupGrade_whenGroupExistAndGradeIsNaturalNumber() throws Exception {
		Group group = new Group();
		group.setId(1);

		List<Group> groups = new ArrayList<>();
		groups.add(group);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(groups);

		Group groupNewInfo = new Group();
		groupNewInfo.setId(1);
		groupNewInfo.setGrade(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/group/update").flashAttr("group", groupNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		assertEquals(groupNewInfo.getGrade(), group.getGrade());

		verify(groupService).update(group, 1);
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateGroup_shouldUpdateOnlyGroupName_whenGroupExistAndGradeIsNotNaturalNumber() throws Exception {
		Group group = new Group();
		group.setId(1);

		List<Group> groups = new ArrayList<>();
		groups.add(group);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(groups);

		Group groupNewInfo = new Group();
		groupNewInfo.setId(1);
		groupNewInfo.setGrade(-1);
		mockMvc.perform(MockMvcRequestBuilders.post("/group/update").flashAttr("group", groupNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		assertEquals(null, group.getGrade());

		verify(groupService).update(group, 1);
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateGroup_shouldNotUpdateGroup_whenGroupNotExist() throws Exception {
		List<Group> groups = new ArrayList<>();

		Group groupNewInfo = new Group();
		groupNewInfo.setId(1);

		when(groupService.findInListById(groups, groupNewInfo.getId())).thenReturn(Optional.empty());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(groups);

		mockMvc.perform(MockMvcRequestBuilders.post("/group/update").flashAttr("group", groupNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService, never()).update(any(Group.class), anyInt());
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void deleteGroup_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteGroup_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteGroupTest() throws Exception {
		doNothing().when(groupService).deleteById(1);

		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).deleteById(1);
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void setSpecialization_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/set-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void setSpecialization_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/set-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setSpecialization_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/set-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setSpecialization_shouldSetSpecializationToGroup_whenGroupExistAndSpecializationExistAndSpecializationNotAlreadyContainGroup()
			throws Exception {
		doNothing().when(groupService).deleteById(1);

		List<Group> groups = new ArrayList<>();
		List<Specialization> specializations = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(specializationService.getAll()).thenReturn(specializations);

		Group group = new Group();
		group.setId(1);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));

		Specialization specialization = new Specialization();
		specialization.setId(1);

		when(specializationService.findInListById(specializations, 1)).thenReturn(Optional.of(specialization));

		doNothing().when(groupService).update(group, group.getId());

		when(studentService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/set-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(specializationService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(specializationService).findInListById(specializations, 1);
		verify(groupService).update(group, 1);
		verify(studentService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setSpecialization_shouldNotSetSpecializationToGroup_whenGroupExistAndSpecializationExistAndSpecializationAlreadyContainGroup()
			throws Exception {
		doNothing().when(groupService).deleteById(1);

		List<Group> groups = new ArrayList<>();
		List<Specialization> specializations = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(specializationService.getAll()).thenReturn(specializations);

		Group group = new Group();
		group.setId(1);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));

		Specialization specialization = new Specialization();
		specialization.addGroup(group);
		specialization.setId(1);

		when(specializationService.findInListById(specializations, 1)).thenReturn(Optional.of(specialization));

		doNothing().when(groupService).update(group, group.getId());

		when(studentService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/set-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(specializationService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(specializationService).findInListById(specializations, 1);
		verify(groupService, never()).update(group, 1);
		verify(studentService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setSpecialization_shouldNotSetSpecializationToGroup_whenGroupExistAndSpecializationNotExist()
			throws Exception {
		doNothing().when(groupService).deleteById(1);

		List<Group> groups = new ArrayList<>();
		List<Specialization> specializations = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(specializationService.getAll()).thenReturn(specializations);

		Group group = new Group();
		group.setId(1);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));
		when(specializationService.findInListById(specializations, 1)).thenReturn(Optional.empty());

		doNothing().when(groupService).update(group, group.getId());

		when(studentService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/set-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(specializationService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(specializationService).findInListById(specializations, 1);
		verify(groupService, never()).update(group, 1);
		verify(studentService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setSpecialization_shouldNotSetSpecializationToGroup_whenGroupNotExist() throws Exception {
		doNothing().when(groupService).deleteById(1);

		List<Group> groups = new ArrayList<>();
		List<Specialization> specializations = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(specializationService.getAll()).thenReturn(specializations);
		when(groupService.findInListById(groups, 1)).thenReturn(Optional.empty());

		Specialization specialization = new Specialization();
		specialization.setId(1);

		when(specializationService.findInListById(specializations, 1)).thenReturn(Optional.of(specialization));
		when(studentService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/set-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(specializationService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(specializationService).findInListById(specializations, 1);
		verify(groupService, never()).update(any(Group.class), anyInt());
		verify(studentService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void deleteSpecialization_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteSpecialization_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteSpecialization_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteSpecialization_shouldRemoveSpecializationFromGroup_whenGroupExistAndGroupRelatedToSpecialization()
			throws Exception {
		List<Group> groups = new ArrayList<>();
		Group group = new Group();
		group.setId(1);
		Specialization specialization = new Specialization();
		specialization.setId(1);
		group.setSpecialization(specialization);
		groups.add(group);

		when(groupService.getAll()).thenReturn(groups);
		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(groupService).update(group, 1);
		verify(specializationService).getAll();
		verify(studentService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteSpecialization_shouldNotRemoveSpecializationFromGroup_whenGroupExistAndGroupNotRelatedToSpecialization()
			throws Exception {
		List<Group> groups = new ArrayList<>();
		Group group = new Group();
		group.setId(1);
		groups.add(group);

		when(groupService.getAll()).thenReturn(groups);
		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(groupService, never()).update(group, 1);
		verify(specializationService).getAll();
		verify(studentService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteSpecialization_shouldNotRemoveSpecializationFromGroup_whenGroupNotExist() throws Exception {
		List<Group> groups = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(groupService.findInListById(groups, 1)).thenReturn(Optional.empty());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/group/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("groupId", 1)).andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(groupService, never()).update(any(Group.class), anyInt());
		verify(specializationService).getAll();
		verify(studentService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void addStudent_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/add-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void addStudent_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/add-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addStudent_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/add-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addStudent_shouldAddStudentToGroup_whenGroupExistAndStudentExistAndStudentNotRelatedToThatGroup()
			throws Exception {
		List<Group> groups = new ArrayList<>();
		List<Student> students = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(studentService.getAll()).thenReturn(students);

		Group group = new Group();
		Student student = new Student();

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/add-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(studentService).findInListById(students, 1);
		verify(groupService).update(group, 1);
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addStudent_shouldAddStudentToGroup_whenGroupExistAndStudentExistAndStudentRelatedToAnotherGroup()
			throws Exception {
		List<Group> groups = new ArrayList<>();
		List<Student> students = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(studentService.getAll()).thenReturn(students);

		Group group1 = new Group();
		group1.setId(1);

		Group group2 = new Group();
		group2.setId(2);
		Student student = new Student();
		student.setGroup(group2);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group1));
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));

		doNothing().when(groupService).update(group1, group1.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/add-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(studentService).findInListById(students, 1);
		verify(groupService).update(group1, 1);
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addStudent_shouldNotAddStudentToGroup_whenGroupExistAndStudentExistAndStudentAlreadyRelatedToThatGroup()
			throws Exception {
		List<Group> groups = new ArrayList<>();
		List<Student> students = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(studentService.getAll()).thenReturn(students);

		Group group = new Group();
		group.setId(1);
		Student student = new Student();
		student.setGroup(group);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/add-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(studentService).findInListById(students, 1);
		verify(groupService, never()).update(group, 1);
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addStudent_shouldNotAddStudentToGroup_whenGroupExistAndStudentNotExist() throws Exception {
		List<Group> groups = new ArrayList<>();
		List<Student> students = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(studentService.getAll()).thenReturn(students);

		Group group = new Group();
		group.setId(1);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));
		when(studentService.findInListById(students, 1)).thenReturn(Optional.empty());

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/add-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(studentService).findInListById(students, 1);
		verify(groupService, never()).update(group, 1);
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addStudent_shouldNotAddStudentToGroup_whenGroupNotExist() throws Exception {
		List<Group> groups = new ArrayList<>();
		List<Student> students = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(studentService.getAll()).thenReturn(students);

		Student student = new Student();

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.empty());
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/add-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(studentService).findInListById(students, 1);
		verify(groupService, never()).update(any(Group.class), anyInt());
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void deleteStudent_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/delete-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteStudent_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/delete-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteStudent_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/delete-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteStudent_shouldRemoveStudentFromGroup_whenGroupExistAndStudentExistAndGroupContainStudent()
			throws Exception {
		List<Group> groups = new ArrayList<>();
		List<Student> students = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(studentService.getAll()).thenReturn(students);

		Group group = new Group();
		Student student = new Student();
		student.setId(1);
		group.addStudent(student);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/delete-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(studentService).findInListById(students, 1);
		verify(groupService).update(group, 1);
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteStudent_shouldNotRemoveStudentFromGroup_whenGroupExistAndStudentExistAndGroupNotContainStudent()
			throws Exception {
		List<Group> groups = new ArrayList<>();
		List<Student> students = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(studentService.getAll()).thenReturn(students);

		Group group = new Group();
		Student student = new Student();
		student.setId(1);

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/delete-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(studentService).findInListById(students, 1);
		verify(groupService, never()).update(group, 1);
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteStudent_shouldNotRemoveStudentFromGroup_whenGroupExistAndStudentNotExist() throws Exception {
		List<Group> groups = new ArrayList<>();
		List<Student> students = new ArrayList<>();

		when(groupService.getAll()).thenReturn(groups);
		when(studentService.getAll()).thenReturn(students);

		Group group = new Group();

		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));
		when(studentService.findInListById(students, 1)).thenReturn(Optional.empty());

		doNothing().when(groupService).update(group, group.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/group/delete-student").flashAttr("studentId", 1).flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(groupService).findInListById(groups, 1);
		verify(studentService).findInListById(students, 1);
		verify(groupService, never()).update(group, 1);
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}
}
