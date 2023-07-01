package com.hust.daotao.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrator/categories")
public class CategoryController {
	public static final String ROOT_CATEGORY = "admin/category";

	@GetMapping("")
	public String categories(Model model) {
		model.addAttribute("title", "Quản lý chủ đề");
		model.addAttribute("isCategories", true);
		return ROOT_CATEGORY + "/index";
	}
}
