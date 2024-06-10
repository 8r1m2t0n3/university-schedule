package com.foxminded.university_schedule.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university_schedule.model.entity.Course;
import com.foxminded.university_schedule.model.entity.Department;
import com.foxminded.university_schedule.model.entity.Lesson;
import com.foxminded.university_schedule.model.entity.Teacher;
import com.foxminded.university_schedule.service.CourseService;
import com.foxminded.university_schedule.service.DepartmentService;
import com.foxminded.university_schedule.service.LessonService;
import com.foxminded.university_schedule.service.TeacherService;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

	private TeacherService teacherService;
	private DepartmentService departmentService;
	private CourseService courseService;
	private LessonService lessonService;

	public TeacherController(TeacherService teacherService, DepartmentService departmentService, CourseService courseService, LessonService lessonService) {
		this.teacherService = teacherService;
		this.departmentService = departmentService;
		this.courseService = courseService;
		this.lessonService = lessonService;
	}

	@GetMapping("/view")
	public String getTeacherViewPage(Model model) {
		model.addAttribute("teachers", teacherService.getAll());
		model.addAttribute("departments", departmentService.getAll());
		model.addAttribute("courses", courseService.getAll());
		return "teacher-view";
	}
	
	@PostMapping("/select-course")
	public String selectCourse(@ModelAttribute("courseName") String courseName, Model model) {
		Optional<Course> course = courseService.getByName(courseName);

		course.ifPresent(c -> {
			List<Teacher> teachers = c.getTeachers();
			model.addAttribute("teachers", teachers);
		});
		
		model.addAttribute("departments", departmentService.getAll());
		model.addAttribute("courses", courseService.getAll());
		return "teacher-view";
	}
	
	@PostMapping("/select-department")
	public String selectDepartment(@ModelAttribute("departmentName") String departmentName, Model model) {
		Optional<Department> department = departmentService.getByName(departmentName);

		department.ifPresent(d -> {
			List<Teacher> teachers = d.getTeachers();
			model.addAttribute("teachers", teachers);
		});
		
		model.addAttribute("departments", departmentService.getAll());
		model.addAttribute("courses", courseService.getAll());
		return "teacher-view";
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String getTeacherPage(Model model) {
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teacherService.getAll()));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departmentService.getAll()));
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessonService.getAll()));
		
		return "admin/model/teacher";
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String createTeacher(Model model) {
		teacherService.save(new Teacher());
		
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teacherService.getAll()));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departmentService.getAll()));
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessonService.getAll()));
		
		return "admin/model/teacher";
	}
	
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String updateTeacher(@ModelAttribute("teacher") Teacher teacherNewInfo, Model model) {
		List<Teacher> teachers = teacherService.getAll();
		
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherNewInfo.getId());
		
		teacher.ifPresent(t -> {
			t.setFirstName(teacherNewInfo.getFirstName());
			t.setLastName(teacherNewInfo.getLastName());
			t.setDateOfBirth(teacherNewInfo.getDateOfBirth());
			teacherService.update(t, t.getId());
		});
		
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teachers));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departmentService.getAll()));
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessonService.getAll()));
		
		return "admin/model/teacher";
	}
	
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteTeacher(@ModelAttribute("teacherId") Integer teacherId, Model model) {
		teacherService.deleteById(teacherId);
		
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teacherService.getAll()));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departmentService.getAll()));
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessonService.getAll()));
		
		return "admin/model/teacher";
	}
	
	@PostMapping("/add-course")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String addCourse(Model model, 
			@ModelAttribute("courseId") Integer courseId,
			@ModelAttribute("teacherId") Integer teacherId) {
		List<Teacher> teachers = teacherService.getAll();
		List<Course> courses = courseService.getAll();
		
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherId);
		Optional<Course> course = courseService.findInListById(courses, courseId);
		
		teacher.ifPresent(t -> course.ifPresent(c -> {
			if (!t.isContainCourseId(c.getId())) {
				t.addCourse(c);
				teacherService.update(t, teacherId);
			}
		}));
		
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teachers));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departmentService.getAll()));
		model.addAttribute("courses", courses);
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessonService.getAll()));
		
		return "admin/model/teacher";
	}
	
	@PostMapping("/delete-course")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteCourse(Model model,
			@ModelAttribute("courseId") Integer courseId,
			@ModelAttribute("teacherId") Integer teacherId) {
		List<Teacher> teachers = teacherService.getAll();
		List<Course> courses = courseService.getAll();
		
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherId);
		Optional<Course> course = courseService.findInListById(courses, courseId);
		
		teacher.ifPresent(t -> course.ifPresent(c -> {
			if (t.isContainCourseId(c.getId())) {
				t.removeCourse(c);
				teacherService.update(t, teacherId);
			}
		}));
		
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teachers));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departmentService.getAll()));
		model.addAttribute("courses", courses);
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessonService.getAll()));
		
		return "admin/model/teacher";
	}
	
	@PostMapping("/add-lesson")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String addLesson(Model model, 
			@ModelAttribute("lessonId") Integer lessonId,
			@ModelAttribute("teacherId") Integer teacherId) {
		List<Teacher> teachers = teacherService.getAll();
		List<Lesson> lessons = lessonService.getAll();
		
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherId);
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		
		teacher.ifPresent(t -> lesson.ifPresent(l -> {
			if (!t.isContainLessonId(l.getId())) {
				t.addLesson(l);
				teacherService.update(t, teacherId);
			}
		}));
		
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teachers));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departmentService.getAll()));
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessons));
		
		return "admin/model/teacher";
	}
	
	@PostMapping("/delete-lesson")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteLesson(Model model, 
			@ModelAttribute("lessonId") Integer lessonId,
			@ModelAttribute("teacherId") Integer teacherId) {
		List<Teacher> teachers = teacherService.getAll();
		List<Lesson> lessons = lessonService.getAll();
		
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherId);
		Optional<Lesson> lesson = lessonService.findInListById(lessons, lessonId);
		
		teacher.ifPresent(t -> lesson.ifPresent(l -> {
			if (t.isContainLessonId(l.getId())) {
				t.removeLesson(l);
				teacherService.update(t, teacherId);
			}
		}));
		
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teachers));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departmentService.getAll()));
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessons));
		
		return "admin/model/teacher";
	}
	
	@PostMapping("/add-department")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String addDepartment(Model model, 
			@ModelAttribute("departmentId") Integer departmentId,
			@ModelAttribute("teacherId") Integer teacherId) {
		List<Teacher> teachers = teacherService.getAll();
		List<Department> departments = departmentService.getAll();
		
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherId);
		Optional<Department> department = departmentService.findInListById(departments, departmentId);
		
		teacher.ifPresent(t -> department.ifPresent(d -> {
			if (!d.isContainTeacherId(teacherId)) {
				d.addTeacher(t);
				departmentService.update(d, departmentId);
			}
		}));
		
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teachers));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departments));
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessonService.getAll()));
		
		return "admin/model/teacher";
	}
	
	@PostMapping("/delete-department")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteDepartment(Model model, 
			@ModelAttribute("departmentId") Integer departmentId,
			@ModelAttribute("teacherId") Integer teacherId) {
		List<Teacher> teachers = teacherService.getAll();
		List<Department> departments = departmentService.getAll();
		
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherId);
		Optional<Department> department = departmentService.findInListById(departments, departmentId);
		
		teacher.ifPresent(t -> department.ifPresent(d -> {
			if (d.isContainTeacherId(teacherId)) {
				d.removeTeacher(t);
				departmentService.update(d, departmentId);
			}
		}));
		
		model.addAttribute("teachers", teacherService.sortListByIdFromMoreToLess(teachers));
		model.addAttribute("departments", departmentService.sortListByIdFromLessToMore(departments));
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("lessons", lessonService.sortListByIdFromLessToMore(lessonService.getAll()));
		
		return "admin/model/teacher";
	}
}
