package com.foxminded.university_schedule.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university_schedule.model.Course;
import com.foxminded.university_schedule.model.Group;
import com.foxminded.university_schedule.model.Lesson;
import com.foxminded.university_schedule.model.Role;
import com.foxminded.university_schedule.model.Student;
import com.foxminded.university_schedule.model.Teacher;
import com.foxminded.university_schedule.model.User;
import com.foxminded.university_schedule.service.CourseService;
import com.foxminded.university_schedule.service.GroupService;
import com.foxminded.university_schedule.service.LessonService;
import com.foxminded.university_schedule.service.RoomService;
import com.foxminded.university_schedule.service.StudentService;
import com.foxminded.university_schedule.service.TeacherService;
import com.foxminded.university_schedule.service.UserService;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

	private TeacherService teacherService;
	private GroupService groupService;
	private LessonService lessonService;
	private UserService userService;
	private CourseService courseService;
	private StudentService studentService;
	private RoomService roomService;

	private static Map<User, Group> selectedGroups = new HashMap<>();
	private static Map<User, Teacher> selectedTeachers = new HashMap<>();
	private static Map<User, LocalDate> selectedDates = new HashMap<>();

	public ScheduleController(TeacherService teacherService, GroupService groupService, LessonService lessonService,
			UserService userService, CourseService courseService, StudentService studentService,
			RoomService roomService) {
		this.teacherService = teacherService;
		this.groupService = groupService;
		this.lessonService = lessonService;
		this.userService = userService;
		this.courseService = courseService;
		this.studentService = studentService;
		this.roomService = roomService;
	}

	private List<String> getCoursesNamesUserHaveAbilityToReschedule(User user) {
		List<String> courses = new ArrayList<>();

		if (user.getRole().equals(Role.ADMIN)) {
			courses = courseService.getAll().stream().map(Course::getName).toList();
		} else if (user.getRole().equals(Role.TEACHER)) {
			Optional<Teacher> teacher = teacherService.getByFirstNameAndLastName(user.getFirstName(),
					user.getLastName());
			if (teacher.isPresent()) {
				courses = teacher.get().getCourses().stream().map(Course::getName).toList();
			}
		}
		return courses;
	}

	private List<Lesson> getLessonsOfUserForDate(User user) {
		if (selectedGroups.get(user) != null) {
			return lessonService.getLessonsOfGroupForDate(selectedGroups.get(user), selectedDates.get(user));
		} else if (selectedTeachers.get(user) != null) {
			return lessonService.getLessonsOfTeacherForDate(selectedTeachers.get(user), selectedDates.get(user));
		} else
			return lessonService.getListByPeriod(selectedDates.get(user), selectedDates.get(user));
	}

	@GetMapping("/view")
	public String getScheduleViewPage(@AuthenticationPrincipal UserDetails currentUser, Model model) {
		Optional<User> user = userService.getByUsername(currentUser.getUsername());
		user.ifPresent(u -> {
			selectedTeachers.remove(u);
			selectedTeachers.put(u, null);

			selectedGroups.remove(u);
			selectedGroups.put(u, null);

			selectedDates.remove(u);
			selectedDates.put(u, LocalDate.now());

			if (u.getRole().equals(Role.STUDENT)) {
				Optional<Student> student = studentService.getByFirstNameAndLastName(u.getFirstName(), u.getLastName());
				student.ifPresent(s -> {
					selectedGroups.remove(u);
					selectedGroups.put(u, s.getGroup());
				});
			} else if (u.getRole().equals(Role.TEACHER)) {
				Optional<Teacher> teacher = teacherService.getByFirstNameAndLastName(u.getFirstName(), u.getLastName());
				teacher.ifPresent(t -> {
					selectedTeachers.remove(u);
					selectedTeachers.put(u, t);
				});
			}

			model.addAttribute("selectedGroup", selectedGroups.get(u));
			model.addAttribute("selectedTeacher", selectedTeachers.get(u));
			model.addAttribute("selectedDate", selectedDates.get(u));

			model.addAttribute("lessons", getLessonsOfUserForDate(u));
			model.addAttribute("coursesOfUser", getCoursesNamesUserHaveAbilityToReschedule(u));

			model.addAttribute("rescheduleButton", true);
			if (u.getRole().equals(Role.ADMIN)) {
				model.addAttribute("deleteButton", true);
			}
		});

		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "schedule-view";

	}

	@PostMapping("/select-group")
	public String selectGroup(Model model, @AuthenticationPrincipal UserDetails currentUser,
			@ModelAttribute("groupId") int groupId) {
		Optional<User> user = userService.getByUsername(currentUser.getUsername());
		user.ifPresent(u -> {
			Optional<Group> group = groupService.getById(groupId);
			selectedGroups.remove(u);
			selectedGroups.put(u, group.isPresent() ? group.get() : null);

			selectedTeachers.remove(u);
			selectedTeachers.put(u, null);

			model.addAttribute("selectedDate", selectedDates.get(u));
			model.addAttribute("selectedGroup", selectedGroups.get(u));
			model.addAttribute("selectedTeacher", selectedTeachers.get(u));

			model.addAttribute("lessons", getLessonsOfUserForDate(u));
			model.addAttribute("coursesOfUser", getCoursesNamesUserHaveAbilityToReschedule(u));

			model.addAttribute("rescheduleButton", true);
			if (u.getRole().equals(Role.ADMIN)) {
				model.addAttribute("deleteButton", true);
			}
		});

		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "schedule-view";
	}

	@PostMapping("/select-teacher")
	public String selectTeacher(Model model, @AuthenticationPrincipal UserDetails currentUser,
			@ModelAttribute("teacherId") int teacherId) {
		Optional<User> user = userService.getByUsername(currentUser.getUsername());
		user.ifPresent(u -> {
			Optional<Teacher> teacher = teacherService.getById(teacherId);
			selectedTeachers.remove(u);
			selectedTeachers.put(u, teacher.isPresent() ? teacher.get() : null);

			selectedGroups.remove(u);
			selectedGroups.put(u, null);

			model.addAttribute("selectedDate", selectedDates.get(u));
			model.addAttribute("selectedGroup", selectedGroups.get(u));
			model.addAttribute("selectedTeacher", selectedTeachers.get(u));

			model.addAttribute("lessons", getLessonsOfUserForDate(u));
			model.addAttribute("coursesOfUser", getCoursesNamesUserHaveAbilityToReschedule(u));

			model.addAttribute("rescheduleButton", true);
			if (u.getRole().equals(Role.ADMIN)) {
				model.addAttribute("deleteButton", true);
			}
		});

		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "schedule-view";
	}

	@PostMapping("/select-date")
	public String selectDate(Model model, @AuthenticationPrincipal UserDetails currentUser,
			@ModelAttribute("date") LocalDate date) {
		Optional<User> user = userService.getByUsername(currentUser.getUsername());
		user.ifPresent(u -> {
			selectedDates.remove(u);
			selectedDates.put(u, date);

			model.addAttribute("selectedDate", selectedDates.get(u));
			model.addAttribute("selectedGroup", selectedGroups.get(u));
			model.addAttribute("selectedTeacher", selectedTeachers.get(u));

			model.addAttribute("lessons", getLessonsOfUserForDate(u));
			model.addAttribute("coursesOfUser", getCoursesNamesUserHaveAbilityToReschedule(u));

			model.addAttribute("rescheduleButton", true);
			if (u.getRole().equals(Role.ADMIN)) {
				model.addAttribute("deleteButton", true);
			}
		});

		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "schedule-view";
	}

	@PostMapping("/reschedule-lesson-mode")
	@PreAuthorize("hasAnyAuthority('WRITE', 'EDIT_USERS')")
	public String switchToRescheduleLessonMode(Model model, @AuthenticationPrincipal UserDetails currentUser,
			@ModelAttribute("lessonId") int lessonId) {
		model.addAttribute("updateMode", false);
		model.addAttribute("rescheduleMode", true);
		model.addAttribute("lessonIdToRenew", lessonId);

		Optional<User> user = userService.getByUsername(currentUser.getUsername());
		user.ifPresent(u -> {
			model.addAttribute("selectedDate", selectedDates.get(u));
			model.addAttribute("selectedGroup", selectedGroups.get(u));
			model.addAttribute("selectedTeacher", selectedTeachers.get(u));

			model.addAttribute("lessons", getLessonsOfUserForDate(u));
			model.addAttribute("coursesOfUser", getCoursesNamesUserHaveAbilityToReschedule(u));
		});

		model.addAttribute("rooms", roomService.getAll());

		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "schedule-view";
	}

	@PostMapping("/reschedule-lesson")
	@PreAuthorize("hasAnyAuthority('WRITE', 'EDIT_USERS')")
	public String rescheduleLesson(Model model, @AuthenticationPrincipal UserDetails currentUser,
			@ModelAttribute("lesson") Lesson lessonNewInfo) {
		List<Lesson> allLessons = lessonService.getAll();

		Optional<Lesson> lesson = lessonService.findInListById(allLessons, lessonNewInfo.getId());

		lesson.ifPresent(l -> {
			l.setDate(lessonNewInfo.getDate());
			l.setStartTime(lessonNewInfo.getStartTime());
			l.setEndTime(lessonNewInfo.getEndTime());
			l.setRoom(lessonNewInfo.getRoom());
			lessonService.update(l, lessonNewInfo.getId());
		});

		Optional<User> user = userService.getByUsername(currentUser.getUsername());
		user.ifPresent(u -> {
			model.addAttribute("selectedDate", selectedDates.get(u));
			model.addAttribute("selectedGroup", selectedGroups.get(u));
			model.addAttribute("selectedTeacher", selectedTeachers.get(u));

			model.addAttribute("lessons", getLessonsOfUserForDate(u));
			model.addAttribute("coursesOfUser", getCoursesNamesUserHaveAbilityToReschedule(u));

			model.addAttribute("rescheduleButton", true);
			if (u.getRole().equals(Role.ADMIN)) {
				model.addAttribute("deleteButton", true);
			}
		});

		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "schedule-view";
	}

	@PostMapping("/delete-lesson")
	@PreAuthorize("hasAnyAuthority('EDIT_USERS')")
	public String deleteLesson(Model model, @AuthenticationPrincipal UserDetails currentUser,
			@ModelAttribute("lessonId") int lessonId) {
		lessonService.deleteById(lessonId);

		Optional<User> user = userService.getByUsername(currentUser.getUsername());
		user.ifPresent(u -> {
			model.addAttribute("selectedDate", selectedDates.get(u));
			model.addAttribute("selectedGroup", selectedGroups.get(u));
			model.addAttribute("selectedTeacher", selectedTeachers.get(u));

			model.addAttribute("lessons", getLessonsOfUserForDate(u));
			model.addAttribute("coursesOfUser", getCoursesNamesUserHaveAbilityToReschedule(u));

			model.addAttribute("rescheduleButton", true);
			if (u.getRole().equals(Role.ADMIN)) {
				model.addAttribute("deleteButton", true);
			}
		});

		model.addAttribute("teachers", teacherService.sortListByIdFromLessToMore(teacherService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "schedule-view";
	}
}
