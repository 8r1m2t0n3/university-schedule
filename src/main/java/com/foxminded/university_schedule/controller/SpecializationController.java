package com.foxminded.university_schedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university_schedule.service.SpecializationService;

@Controller
@RequestMapping("/specialization")
public class SpecializationController {

	private final SpecializationService specializationService;
	
	public SpecializationController(SpecializationService specializationService) {
		this.specializationService = specializationService;
	}
	
	@GetMapping
	public String getSpecializationPage(Model model) {
		model.addAttribute("specializations", specializationService.getAll());
		return "admin/model/specialization";
	}
}
