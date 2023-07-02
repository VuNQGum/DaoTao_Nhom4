package com.hust.daotao.controller;

import com.hust.daotao.entity.SurveyResult;
import com.hust.daotao.entity.User;
import com.hust.daotao.service.CriteriaService;
import com.hust.daotao.service.SurveyResultService;
import com.hust.daotao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class StaticPageController {
	@Autowired
	private UserService userService;
	@Autowired
	private CriteriaService criteriaService;
	@Autowired
	private SurveyResultService surveyService;

	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("title", "Đăng nhập");
		model.addAttribute("isLogin", true);
		return "login";
	}

	@GetMapping("/success-login")
	public String successLogin(Authentication authentication) {

		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("TEACHER"))) {
			return "redirect:/teacher/courses";
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
			return "redirect:/administrator";
		else {
			User userLogin = userService.getUserCurrentLogin();
			List<SurveyResult> surveys = surveyService.findByUserIdAndCriteriaType(userLogin.getId());
			if (surveys == null || surveys.size() <= 0) {
				return "redirect:/criteria";
			}
			return "redirect:/";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

	@GetMapping("/criteria")
	public String survey(Model model, HttpSession session) {
		model.addAttribute("title", "Khảo sát nguyện vọng");
		model.addAttribute("listCriteria", criteriaService.getListCriteriaByType());
		model.addAttribute("isSurvey", true);
		return "criteria";
	}
}