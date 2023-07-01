package com.hust.daotao.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hust.daotao.service.CategoryService;

@Controller
@RequestMapping("/administrator/users")
public class UserController {
	public static final String ROOT_USER = "admin/user";
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/{user_role}")
	public String users(Model model, @PathVariable("user_role") int role) {
		model.addAttribute("title", "Quản lý " + (role < 3 ? "giảng viên" : "sinh viên"));
		model.addAttribute("role", role);
		model.addAttribute("isUsers", true);
		model.addAttribute("categories", categoryService.getCategoryIct());
		model.addAttribute("roleName", role < 3 ? "giảng viên" : "sinh viên");
		return ROOT_USER + "/index";
	}
}
