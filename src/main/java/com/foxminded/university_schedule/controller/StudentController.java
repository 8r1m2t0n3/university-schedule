package com.foxminded.university_schedule.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university_schedule.model.entity.Group;
import com.foxminded.university_schedule.model.entity.Specialization;
import com.foxminded.university_schedule.model.entity.Student;
import com.foxminded.university_schedule.service.GroupService;
import com.foxminded.university_schedule.service.SpecializationService;
import com.foxminded.university_schedule.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {

	private final StudentService studentService;
	private final GroupService groupService;
	private final SpecializationService specializationService;

	public StudentController(StudentService studentService, GroupService groupService,
			SpecializationService specializationService) {
		this.studentService = studentService;
		this.groupService = groupService;
		this.specializationService = specializationService;
	}

	@GetMapping("/view")
	public String getStudentViewPage(Model model) {
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("students", studentService.getAll());

		return "student-view";
	}
	
	@PostMapping("/select-group")
	public String selectGroup(@ModelAttribute("groupName") String groupName, Model model) {
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("specializations", specializationService.getAll());

		Optional<Group> group = groupService.getByName(groupName);

		group.ifPresent(g -> {
			List<Student> students = g.getStudents();
			model.addAttribute("students", students);
		});

		return "student-view";
	}

	@PostMapping("/select-specialization")
	public String selectSpecialization(@ModelAttribute("specializationName") String specializationName, Model model) {
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("specializations", specializationService.getAll());

		Optional<Specialization> specialization = specializationService.getByName(specializationName);

		specialization.ifPresent(s -> {
			List<Group> groups = s.getGroups();
			List<Student> students = new ArrayList<>();
			for (Group group : groups) {
				students.addAll(group.getStudents());
			}
			model.addAttribute("students", students);
		});

		return "student-view";
	}

	@GetMapping
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String getStudentPage(Model model) {
		model.addAttribute("students", studentService.sortListByIdFromMoreToLess(studentService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "admin/model/student";
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String createStudent(Model model) {
		studentService.save(new Student());

		model.addAttribute("students", studentService.sortListByIdFromMoreToLess(studentService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "admin/model/student";
	}

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String updateStudent(Model model, @ModelAttribute("student") Student studentNewInfo) {
		List<Student> students = studentService.getAll();

		Optional<Student> student = studentService.findInListById(students, studentNewInfo.getId());

		student.ifPresent(s -> {
			s.setFirstName(studentNewInfo.getFirstName());
			s.setLastName(studentNewInfo.getLastName());
			s.setDateOfBirth(studentNewInfo.getDateOfBirth());
			studentService.update(s, s.getId());
		});

		model.addAttribute("students", studentService.sortListByIdFromMoreToLess(students));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "admin/model/student";
	}

	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteStudent(Model model, @ModelAttribute("studentId") int studentId) {
		studentService.deleteById(studentId);

		model.addAttribute("students", studentService.sortListByIdFromMoreToLess(studentService.getAll()));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "admin/model/student";
	}

	@PostMapping("/set-group")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String setGroup(Model model, 
			@ModelAttribute("groupId") int groupId,
			@ModelAttribute("studentId") int studentId) {
		List<Student> students = studentService.getAll();
		List<Group> groups = groupService.getAll();

		Optional<Student> student = studentService.findInListById(students, studentId);
		Optional<Group> group = groupService.findInListById(groups, groupId);

		student.ifPresent(s -> group.ifPresent(g -> {
			s.setGroup(g);
			studentService.update(s, studentId);
		}));

		model.addAttribute("students", studentService.sortListByIdFromMoreToLess(students));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groups));

		return "admin/model/student";
	}

	@PostMapping("/delete-group")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteGroup(Model model, @ModelAttribute("studentId") int studentId) {
		List<Student> students = studentService.getAll();

		Optional<Student> student = studentService.findInListById(students, studentId);

		student.ifPresent(s -> {
			if (s.getGroup() != null) {
				s.removeGroup();
				studentService.update(s, studentId);
			}
		});

		model.addAttribute("students", studentService.sortListByIdFromMoreToLess(students));
		model.addAttribute("groups", groupService.sortListByIdFromLessToMore(groupService.getAll()));

		return "admin/model/student";
	}
}
