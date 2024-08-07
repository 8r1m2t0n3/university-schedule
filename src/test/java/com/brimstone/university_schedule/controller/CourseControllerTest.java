package com.brimstone.university_schedule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brimstone.university_schedule.model.entity.Course;
import com.brimstone.university_schedule.model.entity.Group;
import com.brimstone.university_schedule.model.entity.Specialization;
import com.brimstone.university_schedule.model.entity.Student;
import com.brimstone.university_schedule.model.entity.Teacher;
import com.brimstone.university_schedule.service.CourseService;
import com.brimstone.university_schedule.service.GroupService;
import com.brimstone.university_schedule.service.SpecializationService;
import com.brimstone.university_schedule.service.StudentService;
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
class CourseControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
  GroupService groupService;

	@MockBean
  StudentService studentService;

	@MockBean
  CourseService courseService;

	@MockBean
  SpecializationService specializationService;

	@MockBean
  TeacherService teacherService;

	@Test
	@WithMockUser(authorities = "READ")
	void getCourseViewPage_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/course/view")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "WRITE")
	void getCourseViewPage_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/course/view")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getCourseViewPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/course/view")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void getCourseViewPage_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/course/view")).andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getCourseViewPageTest() throws Exception {
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/course/view"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-group").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void selectGroup_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-group").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-group").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectGroup_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-group"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldSelectGroup_whenGroupExistAndGroupRelatedToSpecialization() throws Exception {
		Group group = new Group();
		group.setId(1);
		Specialization specialization = new Specialization();
		specialization.addGroup(group);

		when(groupService.getById(group.getId())).thenReturn(Optional.of(group));

		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-group").flashAttr("groupId", group.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getById(1);
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldNotSelectGroup_whenGroupNotExist() throws Exception {
		when(groupService.getById(1)).thenReturn(Optional.empty());

		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-group")
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getById(1);
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldNotSelectGroup_whenGroupNotRelatedToSpecialization() throws Exception {
		Group group = new Group();
		group.setId(1);

		when(groupService.getById(group.getId())).thenReturn(Optional.of(group));

		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-group").flashAttr("groupId", group.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(groupService).getById(1);
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectTeacher_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void selectTeacher_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectTeacher_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectTeacher_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-teacher"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectTeacher_shouldSelectTeacher_whenTeacherExist() throws Exception {
		Teacher teacher = new Teacher();
		teacher.setId(1);

		when(teacherService.getById(teacher.getId())).thenReturn(Optional.of(teacher));

		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getById(1);
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(teacherService).getAll();
		verify(teacherService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectTeacher_shouldNotSelectTeacher_whenTeacherNotExist() throws Exception {
		when(teacherService.getById(1)).thenReturn(Optional.empty());

		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService).getById(1);
		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(teacherService).getAll();
		verify(teacherService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectStudent_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void selectStudent_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectStudent_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectStudent_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectStudent_shouldSelectStudent_whenStudentExistAndStudentRelatedToGroupAndGroupRelatedToSpecialization()
			throws Exception {
		Student student = new Student();
		student.setId(1);
		Group group = new Group();
		Specialization specialization = new Specialization();
		specialization.addGroup(group);
		group.addStudent(student);

		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		when(studentService.getById(student.getId())).thenReturn(Optional.of(student));

		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model()
				.attribute("courses", equalTo(student.getGroup().getSpecialization().getCourses())));

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(teacherService).getAll();
		verify(studentService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectStudent_shouldSelectStudent_whenStudentExistAndStudentNotRelatedToGroup()
			throws Exception {
		Student student = new Student();
		student.setId(1);

		when(studentService.getById(student.getId())).thenReturn(Optional.of(student));

		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("courses", equalTo(null)));

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(teacherService).getAll();
		verify(studentService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectStudent_shouldSelectStudent_whenStudentExistAndStudentRelatedToGroupAndGroupNotRelatedToSpecialization()
			throws Exception {
		Student student = new Student();
		student.setId(1);
		Group group = new Group();
		group.addStudent(student);

		when(studentService.getById(student.getId())).thenReturn(Optional.of(student));

		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(studentService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/select-student").flashAttr("studentId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("courses", equalTo(null)));

		verify(groupService).getAll();
		verify(studentService).getAll();
		verify(teacherService).getAll();
		verify(studentService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void createCourse_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void createCourse_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createCourse_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/create")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createCourseTest() throws Exception {
		Course course = new Course();
		doNothing().when(courseService).save(course);

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/create")).andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).save(course);
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void updateCourse_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void updateCourse_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/update"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateCourse_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/update")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateCourse_shouldUpdateCourse_whenCourseExist() throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		when(courseService.getAll()).thenReturn(courses);
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));

		doNothing().when(courseService).update(course, 1);

		Course courseNewInfo = new Course();
		courseNewInfo.setId(1);

		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/update").flashAttr("course", courseNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).findInListById(courses, 1);
		verify(courseService).getAll();
		verify(courseService).update(course, 1);
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateCourse_shouldNotUpdateCourse_whenCourseNotExist() throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		when(courseService.getAll()).thenReturn(courses);

		Course courseNewInfo = new Course();
		courseNewInfo.setId(2);

		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/update").flashAttr("course", courseNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService, never()).update(any(Course.class), anyInt());
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void deleteCourse_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete").flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteCourse_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete").flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteCourse_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete").flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteCourseTest() throws Exception {
		doNothing().when(courseService).deleteById(1);

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete").flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).deleteById(anyInt());
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getCoursePage_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/course")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getCoursePage_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/course")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getCoursePage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/course")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getCoursePageTest() throws Exception {
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/course"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void addSpecialization_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void addSpecialization_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addSpecialization_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addSpecialization_shouldAddCourseToSpecialization_whenCourseExistAndSpecializationExistAndSpecializationDoNotAlreadyContainCourse()
			throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		List<Specialization> specializations = new ArrayList<>();
		Specialization specialization = new Specialization();
		specialization.setId(1);
		specializations.add(specialization);

		when(courseService.getAll()).thenReturn(courses);
		when(specializationService.getAll()).thenReturn(specializations);

		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));
		when(specializationService.findInListById(specializations, 1)).thenReturn(Optional.of(specialization));

		doNothing().when(specializationService).update(specialization, specialization.getId());

		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-specialization")
				.flashAttr("specializationId", specialization.getId()).flashAttr("courseId", course.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).findInListById(courses, 1);
		verify(specializationService).findInListById(specializations, 1);
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(specializationService).update(specialization, specialization.getId());
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addSpecialization_shouldNotAddCourseToSpecialization_whenCourseNotExist() throws Exception {
		List<Specialization> specializations = new ArrayList<>();
		Specialization specialization = new Specialization();
		specialization.setId(1);
		specializations.add(specialization);

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(specializations);
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-specialization")
				.flashAttr("specializationId", specialization.getId()).flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService, never()).update(specialization, specialization.getId());
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addSpecialization_shouldNotAddCourseToSpecialization_whenCourseExistAndSpecializationNotExist()
			throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		when(courseService.getAll()).thenReturn(courses);
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService, never()).update(any(Specialization.class), anyInt());
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addSpecialization_shouldNotAddCourseToSpecialization_whenCourseExistAndSpecializationExistAndSpecializationAlreadyContainCourse()
			throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		List<Specialization> specializations = new ArrayList<>();
		Specialization specialization = new Specialization();
		specialization.setId(1);
		specialization.addCourse(course);
		specializations.add(specialization);

		when(courseService.getAll()).thenReturn(courses);
		when(specializationService.getAll()).thenReturn(specializations);
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService, never()).update(specialization, specialization.getId());
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void deleteSpecialization_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteSpecialization_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteSpecialization_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteSpecialization_shouldDeleteCourseFromSpecialization_whenCourseExistAndSpecializationExistAndSpecializationContainCourse()
			throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		List<Specialization> specializations = new ArrayList<>();
		Specialization specialization = new Specialization();
		specialization.setId(1);
		specialization.addCourse(course);
		specializations.add(specialization);

		when(courseService.getAll()).thenReturn(courses);
		when(specializationService.getAll()).thenReturn(specializations);

		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));
		when(specializationService.findInListById(specializations, 1)).thenReturn(Optional.of(specialization));

		doNothing().when(specializationService).update(specialization, specialization.getId());

		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-specialization")
				.flashAttr("specializationId", specialization.getId()).flashAttr("courseId", course.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(courseService).findInListById(courses, 1);
		verify(specializationService).findInListById(specializations, 1);
		verify(specializationService).update(specialization, specialization.getId());
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteSpecialization_shouldNotDeleteCourseFromSpecialization_whenCourseNotExist() throws Exception {
		List<Specialization> specializations = new ArrayList<>();
		Specialization specialization = new Specialization();
		specialization.setId(1);
		specializations.add(specialization);

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(specializations);
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-specialization")
				.flashAttr("specializationId", specialization.getId()).flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService, never()).update(specialization, specialization.getId());
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteSpecialization_shouldNotDeleteCourseFromSpecialization_whenCourseExistAndSpecializationNotExist()
			throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		when(courseService.getAll()).thenReturn(courses);
		when(specializationService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService, never()).update(any(Specialization.class), anyInt());
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteSpecialization_shouldDeleteCourseFromSpecialization_whenCourseExistAndSpecializationExistAndSpecializationNotContainCourse()
			throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		List<Specialization> specializations = new ArrayList<>();
		Specialization specialization = new Specialization();
		specialization.setId(1);
		specializations.add(specialization);

		when(courseService.getAll()).thenReturn(courses);
		when(specializationService.getAll()).thenReturn(specializations);
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-specialization").flashAttr("specializationId", 1)
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(specializationService, never()).update(specialization, specialization.getId());
		verify(courseService).getAll();
		verify(specializationService).getAll();
		verify(teacherService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void addTeacher_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/course/add-teacher").flashAttr("teacherId", 1).flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void addTeacher_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/course/add-teacher").flashAttr("teacherId", 1).flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addTeacher_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/course/add-teacher").flashAttr("teacherId", 1).flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addTeacher_shouldAddCourseToTeacher_whenCourseExistAndTeacherExistAndTeacherDoNotAlreadyContainCourse()
			throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teachers.add(teacher);

		when(courseService.getAll()).thenReturn(courses);
		when(teacherService.getAll()).thenReturn(teachers);

		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));

		doNothing().when(teacherService).update(teacher, teacher.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-teacher").flashAttr("teacherId", teacher.getId())
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).findInListById(courses, 1);
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(teacherService).update(teacher, teacher.getId());
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addTeacher_shouldNotAddCourseToTeacher_whenCourseNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teachers.add(teacher);

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(teachers);

		doNothing().when(teacherService).update(teacher, teacher.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-teacher").flashAttr("teacherId", teacher.getId())
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService, never()).update(teacher, teacher.getId());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addTeacher_shouldNotAddCourseToTeacher_whenCourseExistAndTeacherNotExist() throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		when(courseService.getAll()).thenReturn(courses);
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-teacher").flashAttr("teacherId", 1)
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService, never()).update(any(Teacher.class), anyInt());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void addTeacher_shouldAddCourseToTeacher_whenCourseExistAndTeacherExistAndTeacherAlreadyContainCourse()
			throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teacher.addCourse(course);
		teachers.add(teacher);

		when(courseService.getAll()).thenReturn(courses);
		when(teacherService.getAll()).thenReturn(teachers);

		doNothing().when(teacherService).update(teacher, teacher.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/add-teacher").flashAttr("teacherId", teacher.getId())
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService, never()).update(teacher, teacher.getId());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void deleteTeacher_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-teacher").flashAttr("teacherId", 1)
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteTeacher_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-teacher").flashAttr("teacherId", 1)
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteTeacher_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-teacher").flashAttr("teacherId", 1)
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteTeacher_shouldDeleteTeacher_whenCourseExistAndTeacherExistAndTeacherContainCourse() throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teacher.addCourse(course);
		teachers.add(teacher);

		when(courseService.getAll()).thenReturn(courses);
		when(teacherService.getAll()).thenReturn(teachers);

		when(courseService.findInListById(courses, 1)).thenReturn(Optional.of(course));
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.of(teacher));

		doNothing().when(teacherService).update(teacher, teacher.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-teacher").flashAttr("teacherId", teacher.getId())
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).findInListById(courses, 1);
		verify(teacherService).findInListById(teachers, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(teacherService).update(teacher, teacher.getId());
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteTeacher_shouldNotDeleteTeacher_whenCourseNotExist() throws Exception {
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teachers.add(teacher);

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(teachers);

		doNothing().when(teacherService).update(teacher, teacher.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-teacher").flashAttr("teacherId", teacher.getId())
				.flashAttr("courseId", 1)).andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService, never()).update(teacher, teacher.getId());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteTeacher_shouldNotDeleteTeacher_whenCourseExistAndTeacherNotExist() throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		when(courseService.getAll()).thenReturn(courses);
		when(teacherService.getAll()).thenReturn(new ArrayList<>());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-teacher").flashAttr("teacherId", 1)
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService, never()).update(any(Teacher.class), anyInt());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(specializationService).getAll();
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteTeacher_shouldNotDeleteTeacher_whenCourseExistAndTeacherExistAndTeacherNotContainCourse()
			throws Exception {
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);

		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teachers.add(teacher);

		when(courseService.getAll()).thenReturn(courses);
		when(teacherService.getAll()).thenReturn(teachers);
		doNothing().when(teacherService).update(teacher, teacher.getId());

		when(specializationService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/course/delete-teacher").flashAttr("teacherId", teacher.getId())
				.flashAttr("courseId", course.getId())).andExpect(MockMvcResultMatchers.status().isOk());

		verify(teacherService, never()).update(teacher, teacher.getId());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(specializationService).getAll();
	}
}
