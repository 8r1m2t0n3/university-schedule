package com.foxminded.university_schedule.controller;

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

import com.foxminded.university_schedule.model.entity.Course;
import com.foxminded.university_schedule.model.entity.Group;
import com.foxminded.university_schedule.model.entity.Lesson;
import com.foxminded.university_schedule.model.entity.Room;
import com.foxminded.university_schedule.model.entity.Teacher;
import com.foxminded.university_schedule.service.CourseService;
import com.foxminded.university_schedule.service.GroupService;
import com.foxminded.university_schedule.service.LessonService;
import com.foxminded.university_schedule.service.RoomService;
import com.foxminded.university_schedule.service.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD, classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class LessonControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	LessonService lessonService;

	@MockBean
	CourseService courseService;

	@MockBean
	TeacherService teacherService;

	@MockBean
	GroupService groupService;

	@MockBean
	RoomService roomService;

	@Test
	@WithMockUser(authorities = "READ")
	void getLessonPage_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/lesson")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void getLessonPage_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/lesson")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getLessonPage_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/lesson")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void getLessonPageTest() throws Exception {
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		when(lessonService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.get("/lesson"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
		verify(lessonService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void createLesson_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void createLesson_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/create"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createLesson_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/create")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void createLessonTest() throws Exception {
		Lesson lesson = new Lesson();
		doNothing().when(lessonService).save(lesson);

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		when(lessonService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/create")).andExpect(MockMvcResultMatchers.status().isOk());

		verify(lessonService).save(lesson);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
		verify(lessonService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void updateLesson_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/update").flashAttr("lesson", new Lesson()))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void updateLesson_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/update").flashAttr("lesson", new Lesson()))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateLesson_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/update").flashAttr("lesson", new Lesson()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateLesson_shouldUpdateLesson_whenLessonExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);

		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));

		doNothing().when(lessonService).update(lesson, lesson.getId());

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		Lesson lessonNewInfo = new Lesson();
		lessonNewInfo.setId(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/update").flashAttr("lesson", lessonNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();

	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void updateLesson_shouldNotUpdateLesson_whenLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();

		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		Lesson lessonNewInfo = new Lesson();
		lessonNewInfo.setId(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/update").flashAttr("lesson", lessonNewInfo))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();

	}

	@Test
	@WithMockUser(authorities = "READ")
	void deleteLesson_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/delete").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void deleteLesson_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/delete").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteLesson_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/delete").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void deleteLessonTest() throws Exception {
		doNothing().when(lessonService).deleteById(1);

		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		when(lessonService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/delete").flashAttr("lessonId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());

		verify(lessonService).deleteById(1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
		verify(lessonService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void setCourse_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void setCourse_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setCourse_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setCourse_shouldSetCourseToLesson_whenLessonExistAndCourseExistAndLessonContainNoCourse() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(courseService.getAll()).thenReturn(courses);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(courseService.findInListById(courses, course.getId())).thenReturn(Optional.of(course));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(courseService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(courseService).findInListById(courses, 1);
		verify(lessonService).update(lesson, 1);
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setCourse_shouldSetCourseToLesson_whenLessonExistAndCourseExistAndLessonNotContainThisCourse() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		Course course1 = new Course();
		course1.setId(2);
		lesson.setCourse(course1);
		lessons.add(lesson);
		
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(courseService.getAll()).thenReturn(courses);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(courseService.findInListById(courses, course.getId())).thenReturn(Optional.of(course));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(courseService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(courseService).findInListById(courses, 1);
		verify(lessonService).update(lesson, 1);
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setCourse_shouldNotSetCourseToLesson_whenLessonExistAndCourseExistAndLessonContainThisCourse() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);
		
		lesson.setCourse(course);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(courseService.getAll()).thenReturn(courses);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(courseService.findInListById(courses, course.getId())).thenReturn(Optional.of(course));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(courseService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(courseService).findInListById(courses, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setCourse_shouldNotSetCourseToLesson_whenLessonExistAndCourseNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Course> courses = new ArrayList<>();
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(courseService.getAll()).thenReturn(courses);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(courseService.findInListById(courses, 1)).thenReturn(Optional.empty());
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(courseService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(courseService).findInListById(courses, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setCourse_shouldNotSetCourseToLesson_whenLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		
		List<Course> courses = new ArrayList<>();
		Course course = new Course();
		course.setId(1);
		courses.add(course);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(courseService.getAll()).thenReturn(courses);
		
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		when(courseService.findInListById(courses, course.getId())).thenReturn(Optional.of(course));
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(courseService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(courseService).findInListById(courses, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void removeCourse_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void removeCourse_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeCourse_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeCourse_shouldRemoveCourseFromLesson_whenLessonExistAndLessonContainSpecifiedCourse() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		Course course = new Course();
		course.setId(1);
		
		lesson.setCourse(course);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeCourse_shouldNotRemoveCourseFromLesson_whenLessonExistAndLessonContainNoCourse() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeCourse_shouldNotRemoveCourseFromLesson_whenLessonExistAndLessonNotContainSpecifiedCourse() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		Course course = new Course();
		course.setId(2);
		
		lesson.setCourse(course);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeCourse_shouldNotRemoveCourseFromLesson_whenLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-course")
				.flashAttr("lessonId", 1)
				.flashAttr("courseId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void setTeacher_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void setTeacher_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setTeacher_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setTeacher_shouldSetCourseToLesson_whenLessonExistAndTeacherExistAndLessonContainNoTeacher() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teachers.add(teacher);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(teacherService.getAll()).thenReturn(teachers);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(teacherService.findInListById(teachers, teacher.getId())).thenReturn(Optional.of(teacher));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(teacherService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).update(lesson, 1);
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setTeacher_shouldSetCourseToLesson_whenLessonExistAndTeacherExistAndLessonNotContainThisTeacher() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		Teacher teacher1 = new Teacher();
		teacher1.setId(2);
		lesson.setTeacher(teacher1);
		lessons.add(lesson);
		
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teachers.add(teacher);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(teacherService.getAll()).thenReturn(teachers);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(teacherService.findInListById(teachers, teacher.getId())).thenReturn(Optional.of(teacher));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(teacherService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService).update(lesson, 1);
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setTeacher_shouldNotSetCourseToLesson_whenLessonExistAndTeacherExistAndLessonContainThisTeacher() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teachers.add(teacher);
		
		lesson.setTeacher(teacher);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(teacherService.getAll()).thenReturn(teachers);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(teacherService.findInListById(teachers, teacher.getId())).thenReturn(Optional.of(teacher));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(teacherService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setTeacher_shouldNotSetCourseToLesson_whenLessonExistAndTeacherNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Teacher> teachers = new ArrayList<>();
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(teacherService.getAll()).thenReturn(teachers);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.empty());
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(teacherService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setTeacher_shouldNotSetCourseToLesson_whenLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();

		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		teachers.add(teacher);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(teacherService.getAll()).thenReturn(teachers);
		
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		when(teacherService.findInListById(teachers, 1)).thenReturn(Optional.empty());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(teacherService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(teacherService).findInListById(teachers, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void removeTeacher_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void removeTeacher_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeTeacher_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeTeacher_shouldRemoveCourseFromLesson_whenLessonExistAndLessonContainSpecifiedTeacher() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		Teacher teacher = new Teacher();
		teacher.setId(1);
		
		lesson.setTeacher(teacher);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService).update(lesson, 1);
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeTeacher_shouldNotRemoveCourseFromLesson_whenLessonExistAndLessonContainNoTeacher() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeTeacher_shouldNotRemoveCourseFromLesson_whenLessonExistAndLessonNotContainSpecifiedTeacher() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		Teacher teacher = new Teacher();
		teacher.setId(2);
		
		lesson.setTeacher(teacher);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeTeacher_shouldNotRemoveCourseFromLesson_whenLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-teacher")
				.flashAttr("lessonId", 1)
				.flashAttr("teacherId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void setGroup_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void setGroup_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldSetGroupToLesson_whenLessonExistAndGroupExistAndLessonContainNoGroup() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Group> groups = new ArrayList<>();
		Group group = new Group();
		group.setId(1);
		groups.add(group);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(groupService.getAll()).thenReturn(groups);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(groupService.findInListById(groups, group.getId())).thenReturn(Optional.of(group));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(groupService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(groupService).findInListById(groups, 1);
		verify(lessonService).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldSetGroupToLesson_whenLessonExistAndGroupExistAndLessonNotContainThisGroup() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		Group group1 = new Group();
		group1.setId(2);
		lesson.setGroup(group1);
		lessons.add(lesson);
		
		List<Group> groups = new ArrayList<>();
		Group group = new Group();
		group.setId(1);
		groups.add(group);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(groupService.getAll()).thenReturn(groups);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(groupService.findInListById(groups, group.getId())).thenReturn(Optional.of(group));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(groupService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(groupService).findInListById(groups, 1);
		verify(lessonService).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldSetGroupToLesson_whenLessonExistAndGroupExistAndLessonContainThisGroup() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Group> groups = new ArrayList<>();
		Group group = new Group();
		group.setId(1);
		groups.add(group);
		
		lesson.setGroup(group);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(groupService.getAll()).thenReturn(groups);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(groupService.findInListById(groups, group.getId())).thenReturn(Optional.of(group));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(groupService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(groupService).findInListById(groups, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldSetGroupToLesson_whenLessonExistAndGroupNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Group> groups = new ArrayList<>();
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(groupService.getAll()).thenReturn(groups);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(groupService.findInListById(groups, 1)).thenReturn(Optional.empty());
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(groupService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(groupService).findInListById(groups, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setGroup_shouldSetGroupToLesson_whenLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		
		List<Group> groups = new ArrayList<>();
		Group group = new Group();
		group.setId(1);
		groups.add(group);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(groupService.getAll()).thenReturn(groups);
		
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		when(groupService.findInListById(groups, group.getId())).thenReturn(Optional.of(group));
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(groupService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(groupService).findInListById(groups, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void removeGroup_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void removeGroup_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeGroup_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeGroup_shouldRemoveGroupFromLesson_whenLessonExistAndLessonContainSpecifiedGroup() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		Group group = new Group();
		group.setId(1);
		
		lesson.setGroup(group);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService).update(lesson, 1);
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeGroup_shouldNotRemoveGroupFromLesson_whenLessonExistAndLessonContainNoGroup() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeGroup_shouldNotRemoveGroupFromLesson_whenLessonExistAndLessonNotContainSpecifiedGroup() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		Group group = new Group();
		group.setId(2);
		
		lesson.setGroup(group);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeGroup_shouldNotRemoveGroupFromLesson_whenLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-group")
				.flashAttr("lessonId", 1)
				.flashAttr("groupId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "READ")
	void setRoom_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void setRoom_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setRoom_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setRoom_shouldSetGroupToLesson_whenLessonExistAndRoomExistAndLessonContainNoRoom() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Room> rooms = new ArrayList<>();
		Room room = new Room();
		room.setId(1);
		rooms.add(room);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(roomService.getAll()).thenReturn(rooms);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(roomService.findInListById(rooms, room.getId())).thenReturn(Optional.of(room));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(roomService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(roomService).findInListById(rooms, 1);
		verify(lessonService).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setRoom_shouldSetGroupToLesson_whenLessonExistAndRoomExistAndLessonNotContainThisRoom() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		Room room1 = new Room();
		room1.setId(2);
		lesson.setRoom(room1);
		lessons.add(lesson);
		
		List<Room> rooms = new ArrayList<>();
		Room room = new Room();
		room.setId(1);
		rooms.add(room);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(roomService.getAll()).thenReturn(rooms);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(roomService.findInListById(rooms, room.getId())).thenReturn(Optional.of(room));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(roomService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(roomService).findInListById(rooms, 1);
		verify(lessonService).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setRoom_shouldNotSetGroupToLesson_whenLessonExistAndRoomExistAndLessonContainThisRoom() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Room> rooms = new ArrayList<>();
		Room room = new Room();
		room.setId(1);
		rooms.add(room);
		
		lesson.setRoom(room);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(roomService.getAll()).thenReturn(rooms);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(roomService.findInListById(rooms, room.getId())).thenReturn(Optional.of(room));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(roomService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(roomService).findInListById(rooms, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setRoom_shouldNotSetGroupToLesson_whenLessonExistAndRoomNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		List<Room> rooms = new ArrayList<>();
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(roomService.getAll()).thenReturn(rooms);
		
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		when(roomService.findInListById(rooms, 1)).thenReturn(Optional.empty());
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(roomService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(roomService).findInListById(rooms, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void setRoom_shouldNotSetGroupToLesson_whenLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		
		List<Room> rooms = new ArrayList<>();
		Room room = new Room();
		room.setId(1);
		rooms.add(room);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(roomService.getAll()).thenReturn(rooms);
		
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		when(roomService.findInListById(rooms, room.getId())).thenReturn(Optional.of(room));
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/set-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(roomService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(roomService).findInListById(rooms, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(courseService).getAll();
		verify(teacherService).getAll();
		verify(groupService).getAll();
	}

	@Test
	@WithMockUser(authorities = "READ")
	void removeRoom_shouldNotAccess_whenUserHasReadAuthority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "WRITE")
	void removeRoom_shoulNotdAccess_whenUserHasWriteAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeRoom_shouldAccess_whenUserHasEditUsersAuthorities() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeRoom_shouldRemoveRoomFromLesson_whenLessonExistAndLessonContainSpecifiedRoom() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		Room room = new Room();
		room.setId(1);
		
		lesson.setRoom(room);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService).update(lesson, 1);
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeRoom_shouldNotRemoveRoomFromLesson_whenLessonExistAndLessonContainNoRoom() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeRoom_shouldNotRemoveRoomFromLesson_whenLessonExistAndLessonNotContainSpecifiedRoom() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		Lesson lesson = new Lesson();
		lesson.setId(1);
		lessons.add(lesson);
		
		Room room = new Room();
		room.setId(2);
		
		lesson.setRoom(room);
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, lesson.getId())).thenReturn(Optional.of(lesson));
		
		doNothing().when(lessonService).update(lesson, lesson.getId());
		
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(lesson, 1);
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
	
	@Test
	@WithMockUser(authorities = "EDIT_USERS")
	void removeRoom_shouldNotRemoveRoomFromLesson_whenLessonNotExist() throws Exception {
		List<Lesson> lessons = new ArrayList<>();
		
		when(lessonService.getAll()).thenReturn(lessons);
		when(lessonService.findInListById(lessons, 1)).thenReturn(Optional.empty());
		when(teacherService.getAll()).thenReturn(new ArrayList<>());
		when(courseService.getAll()).thenReturn(new ArrayList<>());
		when(groupService.getAll()).thenReturn(new ArrayList<>());
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/lesson/remove-room")
				.flashAttr("lessonId", 1)
				.flashAttr("roomId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(lessonService).getAll();
		verify(lessonService).findInListById(lessons, 1);
		verify(lessonService, never()).update(any(Lesson.class), anyInt());
		verify(teacherService).getAll();
		verify(courseService).getAll();
		verify(groupService).getAll();
		verify(roomService).getAll();
	}
}
