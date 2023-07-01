package com.hust.daotao.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hust.daotao.dto.ProfileDto;
import com.hust.daotao.service.ProfileService;

@Controller
@RequestMapping("/administrator/profiles")
public class ProfilesController {
	public static final String ROOT_PROFILE = "admin/profile";

	@Autowired
	private ProfileService profileService;

	@GetMapping("")
	public String profiles(Model model) {
		model.addAttribute("title", "Quản lý thuộc tính profile");
		model.addAttribute("isProfiles", true);
		return ROOT_PROFILE + "/index";
	}

	@GetMapping("/{id}/values")
	public String value(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("isProfiles", true);
		ProfileDto profileDto = profileService.findByIdDto(id);
		if (profileDto == null)
			return "admin/index";
		model.addAttribute("title", "Quản lý giá trị của thuộc tính " + profileDto.getName());
		model.addAttribute("profile", profileDto);
		return ROOT_PROFILE + "/values";
	}
}
