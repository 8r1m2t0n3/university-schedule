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

import com.foxminded.university_schedule.model.Course;
import com.foxminded.university_schedule.model.Group;
import com.foxminded.university_schedule.model.Specialization;
import com.foxminded.university_schedule.model.Student;
import com.foxminded.university_schedule.model.Teacher;
import com.foxminded.university_schedule.service.CourseService;
import com.foxminded.university_schedule.service.GroupService;
import com.foxminded.university_schedule.service.SpecializationService;
import com.foxminded.university_schedule.service.StudentService;
import com.foxminded.university_schedule.service.TeacherService;

@Controller
@RequestMapping("/course")
public class CourseController {

	private StudentService studentService;
	private GroupService groupService;
	private CourseService courseService;
	private SpecializationService specializationService;
	private TeacherService teacherService;

	public CourseController(GroupService groupService, StudentService studentService, CourseService courseService,
			SpecializationService specializationService, TeacherService teacherService) {
		this.groupService = groupService;
		this.studentService = studentService;
		this.courseService = courseService;
		this.specializationService = specializationService;
		this.teacherService = teacherService;
	}

	@GetMapping("/view")
	public String getCourseViewPage(Model model) {
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("students", studentService.getAll());
		model.addAttribute("teachers", teacherService.getAll());

		return "course-view";
	}

	@PostMapping("/select-group")
	public String selectGroup(@ModelAttribute("groupId") int groupId, Model model) {
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("students", studentService.getAll());
		model.addAttribute("teachers", teacherService.getAll());

		Optional<Group> group = groupService.getById(groupId);

		group.ifPresent(g -> {
			if (g.getSpecialization() != null) {
				List<Course> courses = g.getSpecialization().getCourses();
				model.addAttribute("courses", courses);
			}
		});

		return "course-view";
	}

	@PostMapping("/select-student")
	public String selectStudent(@ModelAttribute("studentId") int studentId, Model model) {
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("students", studentService.getAll());
		model.addAttribute("teachers", teacherService.getAll());
		
		Optional<Student> student = studentService.getById(studentId);

		student.ifPresent(s -> {
			if (s.getGroup() != null && s.getGroup().getSpecialization() != null) {
				List<Course> courses = s.getGroup().getSpecialization().getCourses();
				model.addAttribute("courses", courses);
			}
		});

		return "course-view";
	}

	@PostMapping("/select-teacher")
	public String selectTeacher(@ModelAttribute("teacherId") int teacherId, Model model) {
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("students", studentService.getAll());
		model.addAttribute("teachers", teacherService.getAll());

		Optional<Teacher> teacher = teacherService.getById(teacherId);

		teacher.ifPresent(t -> {
			List<Course> courses = t.getCourses();
			model.addAttribute("courses", courses);
		});

		return "course-view";
	}

	@GetMapping
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String getCoursePage(Model model) {
		model.addAttribute("courses", courseService.sortListByIdFromMoreToLess(courseService.getAll()));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("teachers", teacherService.getAll());

		return "admin/model/course";
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String createCourse(Model model) {
		courseService.save(new Course());
		model.addAttribute("courses", courseService.sortListByIdFromMoreToLess(courseService.getAll()));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("teachers", teacherService.getAll());

		return "admin/model/course";
	}

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String updateCourse(Model model, @ModelAttribute("course") Course courseNewInfo) {
		List<Course> courses = courseService.getAll();

		Optional<Course> course = courseService.findInListById(courses, courseNewInfo.getId());

		course.ifPresent(c -> {
			c.setName(courseNewInfo.getName());
			c.setDescription(courseNewInfo.getDescription());
			courseService.update(c, c.getId());
		});

		model.addAttribute("courses", courseService.sortListByIdFromMoreToLess(courses));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("teachers", teacherService.getAll());

		return "admin/model/course";
	}

	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteCourse(Model model, @ModelAttribute("courseId") int courseId) {
		courseService.deleteById(courseId);

		model.addAttribute("courses", courseService.sortListByIdFromMoreToLess(courseService.getAll()));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("teachers", teacherService.getAll());

		return "admin/model/course";
	}

	@PostMapping("/add-specialization")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String addSpecialization(Model model, 
			@ModelAttribute("specializationId") int specializationId,
			@ModelAttribute("courseId") int courseId) {
		List<Course> courses = courseService.getAll();
		List<Specialization> specializations = specializationService.getAll();

		Optional<Course> course = courseService.findInListById(courses, courseId);
		Optional<Specialization> specialization = specializationService.findInListById(specializations,
				specializationId);

		course.ifPresent(c -> specialization.ifPresent(s -> {
			if (!s.isContainCourseId(c.getId())) {
				s.addCourse(c);
				specializationService.update(s, specializationId);
			}
		}));

		model.addAttribute("courses", courseService.sortListByIdFromMoreToLess(courses));
		model.addAttribute("specializations", specializations);
		model.addAttribute("teachers", teacherService.getAll());

		return "admin/model/course";
	}

	@PostMapping("/delete-specialization")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteSpecialization(Model model, 
			@ModelAttribute("specializationId") int specializationId,
			@ModelAttribute("courseId") int courseId) {
		List<Course> courses = courseService.getAll();
		List<Specialization> specializations = specializationService.getAll();

		Optional<Course> course = courseService.findInListById(courses, courseId);
		Optional<Specialization> specialization = specializationService.findInListById(specializations,
				specializationId);

		specialization.ifPresent(s -> course.ifPresent(c -> {
			if (s.isContainCourseId(c.getId())) {
				s.removeCourse(course.get());
				specializationService.update(s, specializationId);
			}
		}));

		model.addAttribute("courses", courseService.sortListByIdFromMoreToLess(courses));
		model.addAttribute("specializations", specializations);
		model.addAttribute("teachers", teacherService.getAll());

		return "admin/model/course";
	}

	@PostMapping("/add-teacher")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String addTeacher(Model model, 
			@ModelAttribute("teacherId") int teacherId,
			@ModelAttribute("courseId") int courseId) {
		List<Course> courses = courseService.getAll();
		List<Teacher> teachers = teacherService.getAll();

		Optional<Course> course = courseService.findInListById(courses, courseId);
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherId);

		course.ifPresent(c -> teacher.ifPresent(t -> {
			if (!t.isContainCourseId(c.getId())) {
				t.addCourse(c);
				teacherService.update(t, teacherId);
			}
		}));
		
		model.addAttribute("courses", courseService.sortListByIdFromMoreToLess(courses));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("teachers", teachers);

		return "admin/model/course";
	}

	@PostMapping("/delete-teacher")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteTeacher(Model model, 
			@ModelAttribute("teacherId") int teacherId,
			@ModelAttribute("courseId") int courseId) {
		List<Course> courses = courseService.getAll();
		List<Teacher> teachers = teacherService.getAll();

		Optional<Course> course = courseService.findInListById(courses, courseId);
		Optional<Teacher> teacher = teacherService.findInListById(teachers, teacherId);

		teacher.ifPresent(t -> course.ifPresent(c -> {
			if (t.isContainCourseId(c.getId())) {
				t.removeCourse(c);
				teacherService.update(t, teacherId);
			}
		}));
		
		model.addAttribute("courses", courseService.sortListByIdFromMoreToLess(courses));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("teachers", teachers);

		return "admin/model/course";
	}
}
