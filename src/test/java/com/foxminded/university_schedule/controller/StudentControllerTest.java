package com.foxminded.university_schedule.controller;

import static org.hamcrest.Matchers.equalTo;
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
class StudentControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	StudentService studentService;
	
	@MockBean
    GroupService groupService;

	@MockBean
    SpecializationService specializationService;

	@Test
	@WithMockUser(authorities = "READ")
	void getStudentViewPage_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/student/view"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getStudentViewPage_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/student/view"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getStudentViewPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/student/view"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void getStudentViewPage_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/student/view"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getStudentViewPageTest() throws Exception {
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/student/view"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(specializationService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-group")
				.flashAttr("groupName", "aa-00"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void selectGroup_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-group")
				.flashAttr("groupName", "aa-00"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-group")
				.flashAttr("groupName", "aa-00"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectGroup_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-group")
				.flashAttr("groupName", "aa-00"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldSelectGroup_whenGroupExist() throws Exception {
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		
		Group group = new Group();
		group.setName("aa-00");
		Student student = new Student();
		group.addStudent(student);
		
		when(groupService.getByName(group.getName())).thenReturn(Optional.of(group));

		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-group")
				.flashAttr("groupName", group.getName()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("students", equalTo(group.getStudents())));

		verify(groupService).getAll();
		verify(specializationService).getAll();
		verify(groupService).getByName("aa-00");
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldNotSelectGroup_whenGroupNotExist() throws Exception {
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-group")
				.flashAttr("groupName", "aa-00"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("students", equalTo(null)));
		
		verify(groupService).getAll();
		verify(specializationService).getAll();
		verify(groupService).getByName("aa-00");
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectSpecialization_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-specialization")
				.flashAttr("groupName", "aa-00"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void selectSpecialization_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-specialization")
				.flashAttr("groupName", "aa-00"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectSpecialization_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-specialization")
				.flashAttr("groupName", "aa-00"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	void selectSpecialization_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-specialization")
				.flashAttr("groupName", "aa-00"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectSpecialization_shouldSelectSpecialization_whenSpecializationExist() throws Exception {
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		
		Specialization specialization = new Specialization();
		specialization.setName("specialization");
		Group group1 = new Group();
		Student student1 = new Student();
		group1.addStudent(student1);
		Group group2 = new Group();
		Student student2 = new Student();
		group2.addStudent(student2);
		specialization.addGroup(group1);
		specialization.addGroup(group2);
		
		when(specializationService.getByName(specialization.getName())).thenReturn(Optional.of(specialization));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-specialization")
				.flashAttr("specializationName", specialization.getName()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("students", equalTo(List.of(student1, student2))));
		
		verify(groupService).getAll();
		verify(specializationService).getAll();
		verify(specializationService).getByName("specialization");
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectSpecialization_shouldNotSelectSpecialization_whenSpecializationNotExist() throws Exception {
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/select-specialization")
				.flashAttr("specializationName", "specialization"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("students", equalTo(null)));
		
		verify(groupService).getAll();
		verify(specializationService).getAll();
		verify(specializationService).getByName("specialization");
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getStudentPage_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/student"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void getStudentPage_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/student"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getStudentPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/student"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getStudentPageTest() throws Exception {
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/student"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(studentService).getAll();
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void createStudent_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void createStudent_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createStudent_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/create"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createStudentTest() throws Exception {
		Student student = new Student();
		doNothing().when(studentService).save(student);

		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/create"))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).save(student);
		verify(studentService).getAll();
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void updateStudent_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void updateStudent_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateStudent_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/update"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateStudent_shouldUpdateStudent_whenStudentExist() throws Exception {
		List<Student> students = new ArrayList<>();
		Student student = new Student();
		student.setId(1);
		students.add(student);
		
		when(studentService.getAll()).thenReturn(students);
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));
		
		doNothing().when(studentService).update(student, student.getId());
		
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		Student studentNewInfo = new Student();
		studentNewInfo.setId(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/update")
				.flashAttr("student", studentNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).getAll();
		verify(studentService).findInListById(students, 1);
		verify(studentService).update(student, student.getId());
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateStudent_shouldNotUpdateStudent_whenStudentNotExist() throws Exception {
		List<Student> students = new ArrayList<>();
		
		when(studentService.getAll()).thenReturn(students);
		when(studentService.findInListById(students, 1)).thenReturn(Optional.empty());
		
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		Student studentNewInfo = new Student();
		studentNewInfo.setId(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/update")
				.flashAttr("student", studentNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).getAll();
		verify(studentService).findInListById(students, 1);
		verify(studentService, never()).update(any(Student.class), anyInt());
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void deleteStudent_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete")
				.flashAttr("groupId", 1)
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteStudent_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete")
				.flashAttr("groupId", 1)
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteStudent_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete")
				.flashAttr("groupId", 1)
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteStudentTest() throws Exception {
		doNothing().when(studentService).deleteById(1);
		
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete")
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).deleteById(1);
		verify(studentService).getAll();
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void setGroup_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/set-group")
				.flashAttr("groupId", 1)
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void setGroup_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/set-group")
				.flashAttr("groupId", 1)
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/set-group")
				.flashAttr("groupId", 1)
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldSetGroupToStudent_whenStudentExistAndGroupExist() throws Exception {
		List<Student> students = new ArrayList<>();
		Student student = new Student();
		student.setId(1);
		students.add(student);
		
		List<Group> groups = new ArrayList<>();
		Group group = new Group();
		group.setId(1);
		groups.add(group);
		
		when(studentService.getAll()).thenReturn(students);
		when(groupService.getAll()).thenReturn(groups);
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));
		when(groupService.findInListById(groups, 1)).thenReturn(Optional.of(group));
		
		doNothing().when(studentService).update(student, student.getId());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/set-group")
				.flashAttr("groupId", 1)
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).getAll();
		verify(groupService).getAll();
		verify(studentService).findInListById(students, 1);
		verify(groupService).findInListById(groups, 1);
		verify(studentService).update(student, student.getId());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldNotSetGroupToStudent_whenStudentExistAndGroupNotExist() throws Exception {
		List<Student> students = new ArrayList<>();
		Student student = new Student();
		student.setId(1);
		students.add(student);
		
		List<Group> groups = new ArrayList<>();
		
		when(studentService.getAll()).thenReturn(students);
		when(groupService.getAll()).thenReturn(groups);
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));
		when(groupService.findInListById(groups, 1)).thenReturn(Optional.empty());
		
		doNothing().when(studentService).update(student, student.getId());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/set-group")
				.flashAttr("groupId", 1)
				.flashAttr("studentId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).getAll();
		verify(groupService).getAll();
		verify(studentService).findInListById(students, 1);
		verify(groupService).findInListById(groups, 1);
		verify(studentService, never()).update(student, student.getId());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldNotSetGroupToStudent_whenStudentNotExist() throws Exception {
		List<Student> students = new ArrayList<>();
		List<Group> groups = new ArrayList<>();
		
		Group group = new Group();
		group.setId(1);
		
		groups.add(group);
		
		when(studentService.getAll()).thenReturn(students);
		when(groupService.getAll()).thenReturn(groups);
		when(studentService.findInListById(students, 1)).thenReturn(Optional.empty());
		when(groupService.findInListById(groups, 1)).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/set-group")
				.flashAttr("groupId", 1)
				.flashAttr("studentId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).getAll();
		verify(groupService).getAll();
		verify(studentService).findInListById(students, 1);
		verify(groupService).findInListById(groups, 1);
		verify(studentService, never()).update(any(Student.class), anyInt());
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void deleteGroup_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete-group")
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteGroup_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete-group")
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete-group")
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteGroup_shouldRemoveGroupFromStudent_whenStudentExistAndStudentRelatedToAnyGroup() throws Exception {
		List<Student> students = new ArrayList<>();
		Student student = new Student();
		student.setId(1);
		Group group = new Group();
		group.setId(1);
		student.setGroup(group);
		students.add(student);
		
		when(studentService.getAll()).thenReturn(students);
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));
		
		doNothing().when(studentService).update(student, student.getId());
		
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete-group")
				.flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).getAll();
		verify(studentService).findInListById(students, 1);
		verify(studentService).update(student, student.getId());
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteGroup_shouldNotRemoveGroupFromStudent_whenStudentExistAndStudentNotRelatedToAnyGroup() throws Exception {
		List<Student> students = new ArrayList<>();
		Student student = new Student();
		student.setId(1);
		students.add(student);
		
		when(studentService.getAll()).thenReturn(students);
		when(studentService.findInListById(students, 1)).thenReturn(Optional.of(student));
		
		doNothing().when(studentService).update(student, student.getId());
		
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete-group")
				.flashAttr("studentId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).getAll();
		verify(studentService).findInListById(students, 1);
		verify(studentService, never()).update(student, student.getId());
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteGroup_shouldNotRemoveGroupFromStudent_whenStudentNotExist() throws Exception {
		List<Student> students = new ArrayList<>();
		
		when(studentService.getAll()).thenReturn(students);
		when(studentService.findInListById(students, 1)).thenReturn(Optional.empty());
		
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/student/delete-group")
				.flashAttr("studentId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentService).getAll();
		verify(studentService).findInListById(students, 1);
		verify(studentService, never()).update(any(Student.class), anyInt());
		verify(groupService).getAll();
	}
}
