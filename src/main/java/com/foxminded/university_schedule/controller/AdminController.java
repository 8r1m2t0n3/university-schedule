package com.foxminded.university_schedule.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.foxminded.university_schedule.model.User;
import com.foxminded.university_schedule.service.UserService;

@Controller
@RequestMapping("/admin-panel")
public class AdminController {

	private UserService userService;

	public AdminController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String getAdminPanelPage(Model model) {
		return "admin/admin-panel";
	}
	
	@GetMapping("/model-menu")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String getDBModelPage(Model model) {
		return "admin/model-menu";
	}
	
	@GetMapping("/user-editor")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String getUserAdministrationPage(Model model) {
		model.addAttribute("users", userService.sortListByIdFromMoreToLess(userService.getAll()));
		return "admin/user-editor";
	}

	@PostMapping("/user-editor/update")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String updateUser(@ModelAttribute("user") User user, Model model) {
		userService.update(user, user.getId());
		model.addAttribute("users", userService.sortListByIdFromMoreToLess(userService.getAll()));
		return "admin/user-editor";
	}

	@PostMapping("/user-editor/delete")
	@PreAuthorize("hasAuthority('EDIT_USERS')")
	public String deleteUser(@ModelAttribute("user") User user, Model model) {
		userService.deleteById(user.getId());
		model.addAttribute("users", userService.sortListByIdFromMoreToLess(userService.getAll()));
		return "admin/user-editor";
	}
}
