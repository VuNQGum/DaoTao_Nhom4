package com.hust.daotao.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrator/settings")
public class SettingController {
	public static final String ROOT = "admin/settings";

	@GetMapping("")
	public String profiles(Model model) {
		model.addAttribute("title", "Quản lý setting");
		model.addAttribute("isSettings", true);
		return ROOT + "/index";
	}
}