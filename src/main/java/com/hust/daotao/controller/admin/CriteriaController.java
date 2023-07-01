package com.hust.daotao.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hust.daotao.service.CriteriaTypeService;
import com.hust.daotao.service.KanseiWordService;

@Controller
@RequestMapping("/administrator/criterias")
public class CriteriaController {
	public static final String ROOT_CRITERIA = "admin/criteria";
	@Autowired
	public CriteriaTypeService criteriaTypeService;
	@Autowired
	public KanseiWordService kanseiService;

	@GetMapping("/{type_code}")
	public String criterias(Model model, @PathVariable("type_code") String typeCode) {
		model.addAttribute("title", "Quản lý tiêu chí đánh giá");
		model.addAttribute("isCriteria", true);
		model.addAttribute("criteriaType", criteriaTypeService.findByCode(typeCode));
		model.addAttribute("kansei", kanseiService.getListKanseiShow());
		return ROOT_CRITERIA + "/criterias";
	}

	@GetMapping("")
	public String criteriaType(Model model) {
		model.addAttribute("title", "Quản lý các bộ tiêu chí đánh giá");
		model.addAttribute("isCriteria", true);
		return ROOT_CRITERIA + "/criteria-type";
	}
}
