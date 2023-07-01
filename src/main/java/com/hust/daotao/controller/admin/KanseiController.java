package com.hust.daotao.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hust.daotao.service.CriteriaTypeService;

@Controller
@RequestMapping("/administrator/kansei-word")
public class KanseiController {
	public static final String ROOT_KANSEI = "admin/kansei";
	@Autowired
	public CriteriaTypeService criteriaTypeService;

	@GetMapping("")
	public String index(Model model) {
		model.addAttribute("title", "Quản lý các bộ từ Kansei");
		model.addAttribute("isKansei", true);
		return ROOT_KANSEI + "/kansei-word";
	}
}