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
@RequestMapping("/group")
public class GroupController {

	private final GroupService groupService;
	private final StudentService studentService;
	private final SpecializationService specializationService;

	public GroupController(GroupService groupService, StudentService studentService,
			SpecializationService specializationService) {
		this.groupService = groupService;
		this.studentService = studentService;
		this.specializationService = specializationService;
	}

	@GetMapping("/view")
	public String getGroupViewPage(Model model) {
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("students", studentService.getAll());
		model.addAttribute("specializations", specializationService.getAll());
		
		return "group-view";
	}
	
	@PostMapping("/select-student")
	public String selectStudent(@ModelAttribute("studentId") int studentId, Model model) {
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("students", studentService.getAll());

		Optional<Student> student = studentService.getById(studentId);

		student.ifPresent(s -> {
			if (s.getGroup() != null) {
				List<Group> groups = new ArrayList<>();
				groups.add(s.getGroup());
				model.addAttribute("groups", groups);
			}
		});

		return "group-view";
	}

	@PostMapping("/select-specialization")
	public String selectSpecialization(@ModelAttribute("specializationId") int specializationId, Model model) {
		Optional<Specialization> specialization = specializationService.getById(specializationId);

		specialization.ifPresent(s -> {
			List<Group> groups = s.getGroups();
			model.addAttribute("groups", groups);
		});

		model.addAttribute("students", studentService.getAll());
		model.addAttribute("specializations", specializationService.getAll());

		return "group-view";
	}

	@GetMapping
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String getGroupPage(Model model) {
		model.addAttribute("groups", groupService.sortListByIdFromMoreToLess(groupService.getAll()));
		model.addAttribute("students", studentService.sortListByIdFromLessToMore(studentService.getAll()));
		model.addAttribute("specializations", specializationService.getAll());

		return "admin/model/group";
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String createGroup(Model model) {
		groupService.save(new Group());

		model.addAttribute("groups", groupService.sortListByIdFromMoreToLess(groupService.getAll()));
		model.addAttribute("students", studentService.sortListByIdFromLessToMore(studentService.getAll()));
		model.addAttribute("specializations", specializationService.getAll());

		return "admin/model/group";
	}

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String updateGroup(Model model, @ModelAttribute("group") Group groupNewInfo) {
		List<Group> groups = groupService.getAll();

		Optional<Group> group = groupService.findInListById(groups, groupNewInfo.getId());

		group.ifPresent(g -> {
			g.setName(groupNewInfo.getName());
			if (groupNewInfo.getGrade() != null && groupNewInfo.getGrade() > 0) {
				g.setGrade(groupNewInfo.getGrade());
			}
			groupService.update(g, g.getId());
		});

		model.addAttribute("groups", groupService.sortListByIdFromMoreToLess(groups));
		model.addAttribute("students", studentService.sortListByIdFromLessToMore(studentService.getAll()));
		model.addAttribute("specializations", specializationService.getAll());

		return "admin/model/group";
	}

	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteGroup(Model model, @ModelAttribute("groupId") int groupId) {
		groupService.deleteById(groupId);

		model.addAttribute("groups", groupService.sortListByIdFromMoreToLess(groupService.getAll()));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("students", studentService.sortListByIdFromLessToMore(studentService.getAll()));

		return "admin/model/group";
	}

	@PostMapping("/set-specialization")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String setSpecialization(Model model, 
			@ModelAttribute("specializationId") int specializationId,
			@ModelAttribute("groupId") int groupId) {
		List<Group> groups = groupService.getAll();
		List<Specialization> specializations = specializationService.getAll();

		Optional<Group> group = groupService.findInListById(groups, groupId);
		Optional<Specialization> specialization = specializationService.findInListById(specializations, specializationId);

		group.ifPresent(g -> specialization.ifPresent(s -> {
			if (!s.isContainGroupId(g.getId())) {
				g.setSpecialization(s);
				groupService.update(g, groupId);
			}
		}));

		model.addAttribute("groups", groupService.sortListByIdFromMoreToLess(groups));
		model.addAttribute("specializations", specializations);
		model.addAttribute("students", studentService.sortListByIdFromLessToMore(studentService.getAll()));

		return "admin/model/group";
	}

	@PostMapping("/delete-specialization")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteSpecialization(Model model, @ModelAttribute("groupId") int groupId) {
		List<Group> groups = groupService.getAll();

		Optional<Group> group = groupService.findInListById(groups, groupId);

		group.ifPresent(g -> {
			if (g.getSpecialization() != null) {
				g.removeSpecialization();
				groupService.update(g, groupId);
			}
		});

		model.addAttribute("groups", groupService.sortListByIdFromMoreToLess(groups));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("students", studentService.sortListByIdFromLessToMore(studentService.getAll()));

		return "admin/model/group";
	}

	@PostMapping("/add-student")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String addStudent(Model model, 
			@ModelAttribute("studentId") int studentId,
			@ModelAttribute("groupId") int groupId) {
		List<Group> groups = groupService.getAll();
		List<Student> students = studentService.getAll();

		Optional<Group> group = groupService.findInListById(groups, groupId);
		Optional<Student> student = studentService.findInListById(students, studentId);

		group.ifPresent(g -> student.ifPresent(s -> {
			if (s.getGroup() == null || !s.getGroup().getId().equals(g.getId())) {
				g.addStudent(s);
				groupService.update(g, groupId);
			}
		}));

		model.addAttribute("groups", groupService.sortListByIdFromMoreToLess(groups));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("students", studentService.sortListByIdFromLessToMore(students));

		return "admin/model/group";
	}

	@PostMapping("/delete-student")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteStudent(Model model, 
			@ModelAttribute("studentId") int studentId,
			@ModelAttribute("groupId") int groupId) {
		List<Group> groups = groupService.getAll();
		List<Student> students = studentService.getAll();

		Optional<Group> group = groupService.findInListById(groups, groupId);
		Optional<Student> student = studentService.findInListById(students, studentId);

		group.ifPresent(g -> student.ifPresent(s -> {
			if (g.isContainStudentId(s.getId())) {
				g.removeStudent(s);
				groupService.update(g, groupId);
			}
		}));
		
		model.addAttribute("groups", groupService.sortListByIdFromMoreToLess(groups));
		model.addAttribute("specializations", specializationService.getAll());
		model.addAttribute("students", studentService.sortListByIdFromLessToMore(students));

		return "admin/model/group";
	}
}
