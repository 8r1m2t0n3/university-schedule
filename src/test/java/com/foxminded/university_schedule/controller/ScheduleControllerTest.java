package com.foxminded.university_schedule.controller;

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

import com.foxminded.university_schedule.model.entity.Course;
import com.foxminded.university_schedule.model.entity.Group;
import com.foxminded.university_schedule.model.entity.Lesson;
import com.foxminded.university_schedule.model.entity.Role;
import com.foxminded.university_schedule.model.entity.Student;
import com.foxminded.university_schedule.model.entity.Teacher;
import com.foxminded.university_schedule.model.entity.User;
import com.foxminded.university_schedule.service.CourseService;
import com.foxminded.university_schedule.service.GroupService;
import com.foxminded.university_schedule.service.LessonService;
import com.foxminded.university_schedule.service.RoomService;
import com.foxminded.university_schedule.service.StudentService;
import com.foxminded.university_schedule.service.TeacherService;
import com.foxminded.university_schedule.service.UserService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ScheduleControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	TeacherService teacherService;

	@MockBean
	GroupService groupService;

	@MockBean
	LessonService lessonService;

	@MockBean
	UserService userService;

	@MockBean
	CourseService courseService;

	@MockBean
	StudentService studentService;

	@MockBean
	RoomService roomService;

	@Test
	@WithMockUser(authorities = "READ")
	void getScheduleViewPage_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/schedule/view")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getScheduleViewPage_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/schedule/view")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getScheduleViewPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/schedule/view")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void getScheduleViewPage_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/schedule/view"))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getScheduleViewPage_shouldSubmitSchedulePageWithCurrentStudentUserScheduleForToday_whenCurrentUserExistAndCurrentUserIsStudentAndStudentExist()
			throws Exception {
		User user = new User();
		user.setFirstName("A");
		user.setLastName("Aa");
		user.setRole(Role.STUDENT);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		Student student = new Student();
		Group group = new Group();
		group.setId(1);
		student.setGroup(group);
		when(studentService.getByFirstNameAndLastName(user.getFirstName(), user.getLastName()))
				.thenReturn(Optional.of(student));

		when(lessonService.getLessonsOfGroupForDate(group, LocalDate.now())).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/schedule/view")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("selectedDate", equalTo(LocalDate.now())))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedTeacher", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedGroup", equalTo(group)));

		verify(userService).getByUsername(anyString());
		verify(studentService).getByFirstNameAndLastName("A", "Aa");
		verify(lessonService).getLessonsOfGroupForDate(group, LocalDate.now());
		verify(courseService, never()).getAll();
		verify(teacherService, never()).getByFirstNameAndLastName(anyString(), anyString());
		verify(teacherService).getAll();
		verify(groupService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getScheduleViewPage_shouldSubmitSchedulePageWithCurrentTeacherUserScheduleForToday_whenCurrentUserExistAndCurrentUserIsTeacherAndTeacherExist()
			throws Exception {
		User user = new User();
		user.setFirstName("AA");
		user.setLastName("AAa");
		user.setRole(Role.TEACHER);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		Teacher teacher = new Teacher();
		teacher.setFirstName(user.getFirstName());
		teacher.setLastName(user.getLastName());
		Course course = new Course();
		course.setId(1);
		course.setName("math");
		teacher.addCourse(course);

		when(teacherService.getByFirstNameAndLastName(user.getFirstName(), user.getLastName()))
				.thenReturn(Optional.of(teacher));

		Course course1 = new Course();
		course1.setName("misic");

		when(courseService.getAll()).thenReturn(List.of(course, course1));
		when(lessonService.getLessonsOfTeacherForDate(teacher, LocalDate.now())).thenReturn(new ArrayList<>());
		when(teacherService.getByFirstNameAndLastName(user.getFirstName(), user.getLastName()))
				.thenReturn(Optional.of(teacher));

		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/schedule/view")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("selectedDate", equalTo(LocalDate.now())))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedTeacher", equalTo(teacher)))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedGroup", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("coursesOfUser", equalTo(List.of("math"))));

		verify(userService).getByUsername(anyString());
		verify(teacherService, times(2)).getByFirstNameAndLastName("AA", "AAa");
		verify(lessonService).getLessonsOfTeacherForDate(teacher, LocalDate.now());
		verify(courseService, never()).getAll();
		verify(studentService, never()).getByFirstNameAndLastName(anyString(), anyString());
		verify(teacherService).getAll();
		verify(groupService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void getScheduleViewPage_shouldSubmitSchedulePageWithScheduleForToday_whenCurrentUserExistAndCurrentUserIsAdmin()
			throws Exception {
		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		when(lessonService.getListByPeriod(LocalDate.now(), LocalDate.now())).thenReturn(new ArrayList<>());

		Course course1 = new Course();
		course1.setName("math");
		Course course2 = new Course();
		course2.setName("music");

		when(courseService.getAll()).thenReturn(List.of(course1, course2));
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/schedule/view")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("selectedDate", equalTo(LocalDate.now())))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedTeacher", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedGroup", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("coursesOfUser", equalTo(List.of("math", "music"))))
				.andExpect(MockMvcResultMatchers.model().attribute("deleteButton", equalTo(true)));

		verify(userService).getByUsername(anyString());
		verify(lessonService).getListByPeriod(LocalDate.now(), LocalDate.now());
		verify(studentService, never()).getByFirstNameAndLastName(anyString(), anyString());
		verify(teacherService, never()).getByFirstNameAndLastName(anyString(), anyString());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-group").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void selectGroup_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-group").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-group").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectGroup_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-group").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldSelectGroup_whenCurrentUserExistAndGroupExist() throws Exception {
		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		Group group = new Group();
		group.setId(1);
		when(groupService.getById(1)).thenReturn(Optional.of(group));

		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-group").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("selectedDate", equalTo(LocalDate.now())))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedTeacher", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedGroup", equalTo(group)));

		verify(userService).getByUsername(anyString());
		verify(groupService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectGroup_shouldNotSelectGroup_whenCurrentUserExistAndGroupNotExist() throws Exception {
		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));
		when(groupService.getById(1)).thenReturn(Optional.empty());

		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-group").flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("selectedDate", equalTo(LocalDate.now())))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedTeacher", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedGroup", equalTo(null)));

		verify(userService).getByUsername(anyString());
		verify(groupService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectTeacher_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void selectTeacher_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectTeacher_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectTeacher_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectTeacher_shouldSelectTeacher_whenCurrentUserExistAndTeacherExist() throws Exception {
		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		Teacher teacher = new Teacher();
		teacher.setId(1);
		when(teacherService.getById(1)).thenReturn(Optional.of(teacher));

		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("selectedDate", equalTo(LocalDate.now())))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedTeacher", equalTo(teacher)))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedGroup", equalTo(null)));

		verify(userService).getByUsername(anyString());
		verify(teacherService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectTeacher_shouldNotSelectTeacher_whenCurrentUserExistAndTeacherNotExist() throws Exception {
		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));
		when(teacherService.getById(1)).thenReturn(Optional.empty());

		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-teacher").flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("selectedDate", equalTo(LocalDate.now())))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedTeacher", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedGroup", equalTo(null)));

		verify(userService).getByUsername(anyString());
		verify(teacherService).getById(1);
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectDate_shouldAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-date").flashAttr("date", LocalDate.now()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void selectDate_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-date").flashAttr("date", LocalDate.now()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void selectDate_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-date").flashAttr("date", LocalDate.now()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void selectDate_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/select-date").flashAttr("date", LocalDate.now()))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void selectDate_shouldSelectDate_whenCurrentUserExist() throws Exception {
		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		mockMvc.perform(
				MockMvcRequestBuilders.post("/schedule/select-date").flashAttr("date", LocalDate.of(2023, 6, 13)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("selectedDate", equalTo(LocalDate.of(2023, 6, 13))))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedTeacher", equalTo(null)))
				.andExpect(MockMvcResultMatchers.model().attribute("selectedGroup", equalTo(null)));

		verify(userService).getByUsername(anyString());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void switchToRescheduleLessonMode_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson-mode").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void switchToRescheduleLessonMode_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson-mode").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void switchToRescheduleLessonMode_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson-mode").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void switchToRescheduleLessonMode_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson-mode").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void switchToRescheduleLessonMode_shouldSwitchToRescheduleLessonMode_whenCurrentUserExist() throws Exception {
		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson-mode").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(userService).getByUsername(anyString());
	}

	@Test
	@WithMockUser(authorities = "READ")
	void rescheduleLesson_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void rescheduleLesson_shouldAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void rescheduleLesson_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void rescheduleLesson_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void rescheduleLesson_shouldRescheduleLesson_whenCurrentUserExistAndLessonExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);

		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));

		doNothing().when(lessonService).update(lesson, lesson.getId());

		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		Lesson lessonNewInfo = new Lesson();
		lessonNewInfo.setId(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson").flashAttr("lesson", lessonNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService).update(lesson, 1);
		verify(userService).getByUsername(anyString());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void rescheduleLesson_shouldNotRescheduleLesson_whenCurrentUserExistAndLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();

		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());

		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		Lesson lessonNewInfo = new Lesson();
		lessonNewInfo.setId(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/reschedule-lesson").flashAttr("lesson", lessonNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(userService).getByUsername(anyString());
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void deleteLesson_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/delete-lesson").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteLesson_shouldNotAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/delete-lesson").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteLesson_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/delete-lesson").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void deleteLesson_shouldNotAccess_whenUserUnauthenticated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/delete-lesson").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteLesson_shouldDeleteLesson_whenCurrentUserExist() throws Exception {
		doNothing().when(lessonService).deleteById(1);
		
		User user = new User();
		user.setRole(Role.ADMIN);

		when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));

		mockMvc.perform(MockMvcRequestBuilders.post("/schedule/delete-lesson").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(lessonService).deleteById(1);
		verify(userService).getByUsername(anyString());
	}
}
