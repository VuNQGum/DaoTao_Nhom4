package com.hust.daotao.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hust.daotao.entity.Course;
import com.hust.daotao.service.CourseEvaluateService;
import com.hust.daotao.service.CourseService;
import com.hust.daotao.service.CriteriaTypeService;
import com.hust.daotao.service.UserService;

@Controller

public class StatisticController {
	@Autowired
	private UserService userSerivce;
	@Autowired
	private CourseService courseSerivce;
	@Autowired
	private CriteriaTypeService criteriaTypeSerivce;
	@Autowired
	private CourseEvaluateService evaluateService;

	@GetMapping("/administrator/statistic")
	public String index(Model model) {
		model.addAttribute("title", "Thống kê");
		model.addAttribute("isStatistic", true);
		model.addAttribute("countStudent", userSerivce.countStudent());
		model.addAttribute("countTeacher", userSerivce.countTeacher());
		model.addAttribute("countCourse", courseSerivce.countCourse());
		model.addAttribute("countCriteriaType", criteriaTypeSerivce.countCriteriaType());
		model.addAttribute("ranks_ict", courseSerivce.getRankCousreByCriteriaType(1));
		model.addAttribute("ranks_hust", courseSerivce.getRankCousreByCriteriaType(2));

		return "admin/statistic/index";
	}

	@GetMapping("/administrator/statistic/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("title", "Thống kê chi tiết");
		Course course = courseSerivce.findById(id);
		model.addAttribute("countLesson", course.getLessonOfCourses().size());
		model.addAttribute("countStudent", course.getStudentOfCourses().size());
		model.addAttribute("avg_ict", evaluateService.averageByCriteriaTypeAndCourse(1, id));
		model.addAttribute("avg_hust", evaluateService.averageByCriteriaTypeAndCourse(2, id));
		model.addAttribute("course", course);
		// model.addAttribute("ranks_ict",
		// courseSerivce.getRankCousreByCriteriaType(1));
		// model.addAttribute("ranks_hust",
		// courseSerivce.getRankCousreByCriteriaType(2));

		return "admin/statistic/detail";
	}
}
