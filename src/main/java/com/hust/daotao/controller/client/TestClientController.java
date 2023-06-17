package com.hust.daotao.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hust.daotao.service.CapacityTestService;

@Controller
public class TestClientController {
	@Autowired
	private CapacityTestService testService;
	
	@GetMapping("/test")
	public String test(Model model) {
		model.addAttribute("title", "Kiếm tra năng lực");
		model.addAttribute("questions", testService.getListQuestion());
		return "client/test/index";
	}
}
