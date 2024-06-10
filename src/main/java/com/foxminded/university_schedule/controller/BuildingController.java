package com.foxminded.university_schedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university_schedule.service.BuildingService;

@Controller
@RequestMapping("/building")
public class BuildingController {

	private final BuildingService buildingService;

	public BuildingController(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@GetMapping
	public String getBuildingPage(Model model) {
		model.addAttribute("buildings", buildingService.getAll());
		return "admin/model/building";
	}
}
