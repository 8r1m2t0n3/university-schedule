package com.brimstone.university_schedule.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brimstone.university_schedule.model.entity.Course;
import com.brimstone.university_schedule.model.entity.Department;
import com.brimstone.university_schedule.model.entity.Lesson;
import com.brimstone.university_schedule.model.entity.Teacher;
import com.brimstone.university_schedule.service.CourseService;
import com.brimstone.university_schedule.service.DepartmentService;
import com.brimstone.university_schedule.service.LessonService;
import com.brimstone.university_schedule.service.TeacherService;
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

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TeacherControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
  TeacherService teacherService;
	
	@MockBean
  DepartmentService departmentService;
	
	@MockBean
  CourseService courseService;
	
	@MockBean
  LessonService lessonService;
	
	@Test
	@WithMockUser(authorities = "READ")
	void getTeacherViewPage_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/view"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void getTeacherViewPage_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/view"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getTeacherViewPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/view"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void getTeacherViewPage_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/view"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void getTeacherViewPageTest() throws Exception {
		when(teacherService.getAll()).thenReturn(new ArrayList<Teacher>());
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());

		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/view")).andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectCourse_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-course"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void selectCourse_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-course"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectCourse_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-course"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectCourse_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/select-course"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectCourse_shouldSelectCourse_whenCourseExist() throws Exception {
		Course course = new Course();
		Teacher teacher = new Teacher();
		teacher.addCourse(course);
		when(courseService.getByName("math")).thenReturn(Optional.of(course));
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-course")
				.flashAttr("courseName", "math"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("teachers", equalTo(List.of(teacher))));
		
		verify(courseService).getByName("math");
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectCourse_shouldNotSelectCourse_whenCourseNotExist() throws Exception {
		when(courseService.getByName("math")).thenReturn(Optional.empty());
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-course")
				.flashAttr("courseName", "math"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("teachers", equalTo(null)));
		
		verify(courseService).getByName("math");
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectDepartment_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-department"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void selectDepartment_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-department"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectDepartment_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-department"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectDepartment_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-department"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectDepartment_shouldSelectDepartment_whenDepartmentExist() throws Exception {
		Department department = new Department();
		Teacher teacher = new Teacher();
		department.addTeacher(teacher);
		when(departmentService.getByName("department")).thenReturn(Optional.of(department));

		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-department")
				.flashAttr("departmentName", "department"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("teachers", equalTo(List.of(teacher))));
		
		verify(departmentService).getByName("department");
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void selectDepartment_shouldNotSelectDepartment_whenDepartmentNotExist() throws Exception {
		when(departmentService.getByName("department")).thenReturn(Optional.empty());
	
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/select-department")
				.flashAttr("departmentName", "department"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("teachers", equalTo(null)));
		
		verify(departmentService).getByName("department");
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void getTeacherPage_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/teacher"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void getTeacherPage_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/teacher"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getTeacherPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/teacher"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getTeacherPageTest() throws Exception {
		when(teacherService.getAll()).thenReturn(new ArrayList<Teacher>());
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());

		mockMvc.perform(MockMvcRequestBuilders.get("/teacher"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(courseService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void createTeacher_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void createTeacher_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createTeacher_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/create"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createTeacherTest() throws Exception {
		Teacher teacher = new Teacher();
		doNothing().when(teacherService).save(teacher);
		
		when(teacherService.getAll()).thenReturn(new ArrayList<Teacher>());
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/create"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).save(teacher);
		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(courseService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void updateTeacher_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void updateTeacher_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateTeacher_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/update"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateTeacher_shouldUpdateTeacher_whenTeacherExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		
		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());

		Teacher teacherNewInfo = new Teacher();
		teacherNewInfo.setId(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/update")
				.flashAttr("teacher", teacherNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(teacherService).update(teacher, 1);
		verify(departmentService).getAll();
		verify(courseService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateTeacher_shouldNotUpdateTeacher_whenTeacherNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.empty());
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		
		Teacher teacherNewInfo = new Teacher();
		teacherNewInfo.setId(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/update")
				.flashAttr("teacher", teacherNewInfo))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(teacherService, never()).update(any(Teacher.class), anyInt());
		verify(departmentService).getAll();
		verify(courseService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void deleteTeacher_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete")
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteTeacher_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete")
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteTeacher_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete")
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteTeacherTest() throws Exception {
		doNothing().when(teacherService).deleteById(1);
		
		when(teacherService.getAll()).thenReturn(new ArrayList<Teacher>());
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete")
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).deleteById(1);
		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(courseService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void addCourse_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void addCourse_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addCourse_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addCourse_shouldAddCourseToTeacher_whenTeacherExistAndCourseExistAndTeacherNotContainCourse() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Course> courses = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(courseService.getAll()).thenReturn(courses);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Course course = new Course();
		course.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));

		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).findInListById(courses, 1);
		verify(teacherService).update(teacher, 1);
		verify(departmentService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addCourse_shouldNotAddCourseToTeacher_whenTeacherExistAndCourseExistAndTeacherContainCourse() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Course> courses = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(courseService.getAll()).thenReturn(courses);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Course course = new Course();
		course.setId(1);
		teacher.addCourse(course);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));
		
		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).findInListById(courses, 1);
		verify(teacherService, never()).update(teacher, 1);
		verify(departmentService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addCourse_shouldNotAddCourseToTeacher_whenTeacherExistAndCourseNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Course> courses = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(courseService.getAll()).thenReturn(courses);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.empty());
		
		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).findInListById(courses, 1);
		verify(teacherService, never()).update(teacher, 1);
		verify(departmentService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addCourse_shouldNotAddCourseToTeacher_whenTeacherNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Course> courses = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(courseService.getAll()).thenReturn(courses);
		
		Course course = new Course();
		course.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.empty());
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).findInListById(courses, 1);
		verify(teacherService, never()).update(any(Teacher.class), anyInt());
		verify(departmentService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void deleteCourse_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteCourse_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteCourse_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteCourse_shouldDeleteCourseFromTeacher_whenTeacherExistAndCourseExistAndTeacherContainCourse() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Course> courses = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(courseService.getAll()).thenReturn(courses);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Course course = new Course();
		course.setId(1);
		teacher.addCourse(course);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));

		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).findInListById(courses, 1);
		verify(teacherService).update(teacher, 1);
		verify(departmentService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteCourse_shouldNotDeleteCourseFromTeacher_whenTeacherExistAndCourseExistAndTeacherNotContainCourse() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Course> courses = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(courseService.getAll()).thenReturn(courses);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Course course = new Course();
		course.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));
		
		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).findInListById(courses, 1);
		verify(teacherService, never()).update(teacher, 1);
		verify(departmentService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteCourse_shouldNotDeleteCourseFromTeacher_whenTeacherExistAndCourseNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Course> courses = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(courseService.getAll()).thenReturn(courses);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.empty());
		
		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).findInListById(courses, 1);
		verify(teacherService, never()).update(teacher, 1);
		verify(departmentService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteCourse_shouldNotDeleteCourseFromTeacher_whenTeacherNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Course> courses = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(courseService.getAll()).thenReturn(courses);
		
		Course course = new Course();
		course.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.empty());
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-course")
				.flashAttr("courseId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).findInListById(courses, 1);
		verify(teacherService, never()).update(any(Teacher.class), anyInt());
		verify(departmentService).getAll();
		verify(lessonService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void addLesson_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void addLesson_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addLesson_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addLesson_shouldAddLessonToTeacher_whenTeacherExistAndLessonExistAndTeacherNotContainLesson() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Lesson> lessons = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(lessonService.getAll()).thenReturn(lessons);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Lesson lesson = new Lesson();
		lesson.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.of(lesson));

		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getAll();
		verify(lessonService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService).update(teacher, 1);
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addLesson_shouldNotAddLessonToTeacher_whenTeacherExistAndLessonExistAndTeacherContainLesson() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Lesson> lessons = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(lessonService.getAll()).thenReturn(lessons);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Lesson lesson = new Lesson();
		lesson.setId(1);
		teacher.addLesson(lesson);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.of(lesson));
		
		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(lessonService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService, never()).update(teacher, 1);
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addLesson_shouldNotAddLessonToTeacher_whenTeacherExistAndLessonNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Lesson> lessons = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(lessonService.getAll()).thenReturn(lessons);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		
		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(lessonService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService, never()).update(teacher, 1);
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addLesson_shouldNotAddLessonToTeacher_whenTeacherNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Lesson> lessons = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(lessonService.getAll()).thenReturn(lessons);

		Lesson lesson = new Lesson();
		lesson.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.empty());
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.of(lesson));
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(lessonService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService, never()).update(any(Teacher.class), anyInt());
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void deleteLesson_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteLesson_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteLesson_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteLesson_shouldDeleteLessonFromTeacher_whenTeacherExistAndLessonExistAndTeacherContainLesson() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Lesson> lessons = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(lessonService.getAll()).thenReturn(lessons);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Lesson lesson = new Lesson();
		lesson.setId(1);
		teacher.addLesson(lesson);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.of(lesson));

		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getAll();
		verify(lessonService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService).update(teacher, 1);
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteLesson_shouldNotDeleteLessonFromTeacher_whenTeacherExistAndLessonExistAndTeacherNotContainLesson() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Lesson> lessons = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(lessonService.getAll()).thenReturn(lessons);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Lesson lesson = new Lesson();
		lesson.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.of(lesson));
		
		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(lessonService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService, never()).update(teacher, 1);
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteLesson_shouldNotDeleteLessonFromTeacher_whenTeacherExistAndLessonNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Lesson> lessons = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(lessonService.getAll()).thenReturn(lessons);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		
		doNothing().when(teacherService).update(teacher, 1);
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(lessonService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService, never()).update(teacher, 1);
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteLesson_shouldNotDeleteLessonFromTeacher_whenTeacherNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Lesson> lessons = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(lessonService.getAll()).thenReturn(lessons);
		
		Lesson lesson = new Lesson();
		lesson.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.empty());
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.of(lesson));
		
		when(departmentService.getAll()).thenReturn(new ArrayList<Department>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-lesson")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(lessonService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService, never()).update(any(Teacher.class), anyInt());
		verify(departmentService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void addDepartment_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void addDepartment_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addDepartment_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addDepartment_shouldAddTeacherToDepartment_whenTeacherExistAndDepartmentExistAndDepartmentNotContainTeacher() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Department> departments = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(departmentService.getAll()).thenReturn(departments);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Department department = new Department();
		department.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(departmentService.findInListById(departments, 1)).thenReturn(Optional.of(department));

		doNothing().when(departmentService).update(department, 1);
		
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(departmentService).findInListById(departments, 1);
		verify(departmentService).update(department, 1);
		verify(lessonService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addDepartment_shouldNotAddTeacherToDepartment_whenTeacherExistAndDepartmentExistAndDepartmentContainTeacher() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Department> departments = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(departmentService.getAll()).thenReturn(departments);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Department department = new Department();
		department.setId(1);
		department.addTeacher(teacher);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(departmentService.findInListById(departments, 1)).thenReturn(Optional.of(department));
		
		doNothing().when(departmentService).update(department, 1);
		
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(departmentService).findInListById(departments, 1);
		verify(departmentService, never()).update(department, 1);
		verify(lessonService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addDepartment_shouldNotAddTeacherToDepartment_whenTeacherExistAndDepartmentNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Department> departments = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(departmentService.getAll()).thenReturn(departments);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(departmentService.findInListById(departments, 1)).thenReturn(Optional.empty());
		
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(departmentService).findInListById(departments, 1);
		verify(departmentService, never()).update(any(Department.class), anyInt());
		verify(lessonService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addDepartment_shouldNotAddTeacherToDepartment_whenTeacherNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Department> departments = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(departmentService.getAll()).thenReturn(departments);
		
		Department department = new Department();
		department.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.empty());
		when(departmentService.findInListById(departments, 1)).thenReturn(Optional.of(department));
		
		doNothing().when(departmentService).update(department, 1);
		
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/add-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(departmentService).findInListById(departments, 1);
		verify(departmentService, never()).update(department, 1);
		verify(lessonService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void deleteDepartment_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteDepartment_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteDepartment_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteDepartment_shouldDeleteTeacherFromDepartment_whenTeacherExistAndDepartmentExistAndDepartmentContainTeacher() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Department> departments = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(departmentService.getAll()).thenReturn(departments);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Department department = new Department();
		department.setId(1);
		department.addTeacher(teacher);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(departmentService.findInListById(departments, 1)).thenReturn(Optional.of(department));

		doNothing().when(departmentService).update(department, 1);
		
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(departmentService).findInListById(departments, 1);
		verify(departmentService).update(department, 1);
		verify(lessonService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteDepartment_shouldNotDeleteTeacherFromDepartment_whenTeacherExistAndDepartmentExistAndDepartmentNotContainTeacher() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Department> departments = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(departmentService.getAll()).thenReturn(departments);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		Department department = new Department();
		department.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(departmentService.findInListById(departments, 1)).thenReturn(Optional.of(department));
		
		doNothing().when(departmentService).update(department, 1);
		
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(departmentService).findInListById(departments, 1);
		verify(departmentService, never()).update(department, 1);
		verify(lessonService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteDepartment_shouldNotDeleteTeacherFromDepartment_whenTeacherExistAndDepartmentNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Department> departments = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(departmentService.getAll()).thenReturn(departments);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));
		when(departmentService.findInListById(departments, 1)).thenReturn(Optional.empty());
		
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(departmentService).findInListById(departments, 1);
		verify(departmentService, never()).update(any(Department.class), anyInt());
		verify(lessonService).getAll();
		verify(courseService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteDepartment_shouldNotDeleteTeacherFromDepartment_whenTeacherNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		List<Department> departments = new ArrayList<>();
		
		when(teacherService.getAll()).thenReturn(teachers);
		when(departmentService.getAll()).thenReturn(departments);
		
		Department department = new Department();
		department.setId(1);
		
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.empty());
		when(departmentService.findInListById(departments, 1)).thenReturn(Optional.of(department));
		
		doNothing().when(departmentService).update(department, 1);
		
		when(lessonService.getAll()).thenReturn(new ArrayList<Lesson>());
		when(courseService.getAll()).thenReturn(new ArrayList<Course>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/delete-department")
				.flashAttr("departmentId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(teacherService).getAll();
		verify(departmentService).getAll();
		verify(teacherService).findInListById(teachers, 1);
		verify(departmentService).findInListById(departments, 1);
		verify(departmentService, never()).update(department, 1);
		verify(lessonService).getAll();
		verify(courseService).getAll();
	}
}
