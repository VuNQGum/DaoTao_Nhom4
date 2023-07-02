package com.hust.daotao.controller;

import com.hust.daotao.dto.CategoryDto;
import com.hust.daotao.dto.CourseDto;
import com.hust.daotao.dto.DataCrawSoict;
import com.hust.daotao.dto.ProfileDto;
import com.hust.daotao.entity.User;
import com.hust.daotao.entity.UserProfile;
import com.hust.daotao.service.*;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.GetDocumentFromURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
public class ClientController {
	@Autowired
	private UserService userSerivce;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private UserProfileService userProfileService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ContextMatching contextMatching;
	@Autowired
	private ArticleService articleService;

	@GetMapping("")
	public String home(Model model) {
		List<CourseDto> yourCourses = new ArrayList<CourseDto>();
		List<CourseDto> coursesEvaluateHigh = courseService.getListCourseByEvaluate();
		User user = userSerivce.getUserCurrentLogin();
		if (user != null && user.getRole().getId() == Constants.ROLE_STUDENT) {
			yourCourses = courseService.convertListBoToListDto(contextMatching.contextMatching(user, 3));
		}
		List<DataCrawSoict> newsHome = GetDocumentFromURL.getNewsBySoictHome();
		model.addAttribute("yourCourses", yourCourses);
		model.addAttribute("coursesEvaluateHigh", coursesEvaluateHigh);
		model.addAttribute("title", "Trang chủ - Học trực tuyến");
		model.addAttribute("isHome", true);
		model.addAttribute("newsHome", newsHome);
		model.addAttribute("listArticle", articleService.getListArticleShowClient());
		
		return "client/index";
	}

	@GetMapping("/users")
	public String edit(Model model) {
		User userLogin = userSerivce.getUserCurrentLogin();
		List<ProfileDto> profileDtos = profileService.getListProfileOfStudent();
		List<UserProfile> userProfiles = userProfileService.findByUser(userLogin);
		List<ProfileDto> newProfiles = new ArrayList<ProfileDto>();
		for (ProfileDto profileDto : profileDtos) {
			if (!checkProfileId(profileDto.getId(), userProfiles))
				newProfiles.add(profileDto);
		}
		List<CategoryDto> listSubject = categoryService.getCategoryIct();
		model.addAttribute("subjects", listSubject);
		model.addAttribute("profileDtos", newProfiles);
		model.addAttribute("userLogin", userLogin);
		model.addAttribute("userProfiles", userProfiles);
		model.addAttribute("title", "Thông tin cá nhân");
		return "client/user/edit";
	}

	@GetMapping("/change-password")
	public String showFormChangePasswordString() {
		return "client/user/change-password";
	}

	@GetMapping("/register")
	public String register(Model model) {
		List<ProfileDto> profileDtos = profileService.getListProfileOfStudent();
		List<CategoryDto> listSubject = categoryService.getCategoryIct();
		model.addAttribute("profileDtos", profileDtos);
		model.addAttribute("subjects", listSubject);
		model.addAttribute("isRegister", true);
		model.addAttribute("title", "Đăng ký");
		return "register";
	}

	public boolean checkProfileId(Integer id, List<UserProfile> userProfiles) {
		for (UserProfile userProfile : userProfiles) {
			if (userProfile.getProfile().getId() == id)
				return true;
		}
		return false;
	}

}
