package com.foxminded.university_schedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university_schedule.service.DepartmentService;

@Controller
@RequestMapping("/department")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@GetMapping
	public String getDepartmentPage(Model model) {
		model.addAttribute("departments", departmentService.getAll());
		return "admin/model/department";
	}
}
