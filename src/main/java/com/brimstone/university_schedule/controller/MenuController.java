package com.brimstone.university_schedule.controller;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brimstone.university_schedule.model.entity.Role;
import com.brimstone.university_schedule.model.entity.User;
import com.brimstone.university_schedule.service.UserService;

@Controller
@RequestMapping("/menu")
public class MenuController {

	private final UserService userService;

	public MenuController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String getMenuPage(@AuthenticationPrincipal UserDetails currentUser, Model model) {
		model.addAttribute("userAdmin", false);
		model.addAttribute("userStudent", false);
		model.addAttribute("userTeacher", false);
		if (currentUser == null) {
			model.addAttribute("userLogged", false);
		} else {
			model.addAttribute("userLogged", true);
			Optional<User> user = userService.getByUsername(currentUser.getUsername());
			if (user.isEmpty()) {
				return "menu";
			}
			else if (user.get().getRole().equals(Role.STUDENT)) {
				model.addAttribute("userStudent", true);
			}
			else if (user.get().getRole().equals(Role.TEACHER)) {
				model.addAttribute("userTeacher", true);
			}
			else if (user.get().getRole().equals(Role.ADMIN)) {
				model.addAttribute("userAdmin", true);
			}
		}
		return "menu";
	}
}
