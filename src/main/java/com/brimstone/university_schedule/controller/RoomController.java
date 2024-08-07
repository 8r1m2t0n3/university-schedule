package com.brimstone.university_schedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brimstone.university_schedule.service.RoomService;

@Controller
@RequestMapping("/room")
public class RoomController {

	private final RoomService roomService;
	
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}
	
	@GetMapping
	public String getRoomPage(Model model) {
		model.addAttribute("rooms", roomService.getAll());
		return "admin/model/room";
	}
}
