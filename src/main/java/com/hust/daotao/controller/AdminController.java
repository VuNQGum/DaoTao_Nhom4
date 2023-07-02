package com.hust.daotao.controller;

import com.hust.daotao.service.CourseService;
import com.hust.daotao.service.CriteriaTypeService;
import com.hust.daotao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrator")
public class AdminController {
	@Autowired
	private UserService userSerivce;
	@Autowired
	private CourseService courseSerivce;
	@Autowired
	private CriteriaTypeService criteriaTypeSerivce;
	@GetMapping("")
	public String dashboard(Model model) {
		model.addAttribute("title", "Trang quản lý");
		model.addAttribute("isHome", true);
		model.addAttribute("countStudent", userSerivce.countStudent());
		model.addAttribute("countTeacher", userSerivce.countTeacher());
		model.addAttribute("countCourse", courseSerivce.countCourse());
		model.addAttribute("countCriteriaType", criteriaTypeSerivce.countCriteriaType());

		return "admin/index";
	}

}
