package com.hust.daotao.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrator/test")
public class TestAdminController {
	public static final String ROOT_TEST = "admin/test";

	@GetMapping("")
	public String profiles(Model model) {
		model.addAttribute("title", "Quản lý bộ câu hỏi kiểm tra");
		model.addAttribute("isTest", true);
		return ROOT_TEST + "/index";
	}
}
