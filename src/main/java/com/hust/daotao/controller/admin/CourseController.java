package com.hust.thesis.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hust.thesis.dto.LessonOfCourseDto;
import com.hust.thesis.dto.ProfileDto;
import com.hust.thesis.entity.Course;
import com.hust.thesis.entity.CourseProfile;
import com.hust.thesis.entity.User;
import com.hust.thesis.service.CategoryService;
import com.hust.thesis.service.CourseProfileService;
import com.hust.thesis.service.CourseService;
import com.hust.thesis.service.CriteriaService;
import com.hust.thesis.service.LessonOfCourseService;
import com.hust.thesis.service.ProfileService;
import com.hust.thesis.service.UserService;
import com.hust.thesis.util.Constants;

@Controller
@RequestMapping("/administrator/courses")
public class CourseController {
	public static final String ROOT_COURSE = "admin/course";
	public static final String ROOT_LESSON = "admin/lesson";

	@Autowired
	private ProfileService profileService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private LessonOfCourseService lessonService;
	@Autowired
	private CourseProfileService courseProfileService;
	@Autowired
	private CriteriaService criteriaService;

	@GetMapping("")
	public String course(Model model) {
		model.addAttribute("title", "Quản lý khóa học");
		model.addAttribute("isCourses", true);
		model.addAttribute("categories", categoryService.getCategoryIsShow());
		User userLogin = userService.getUserCurrentLogin();
		if (userLogin.getRole().getId() != Constants.ROLE_TEACHER) {
			model.addAttribute("teachers", userService.getListTeacher());
			model.addAttribute("isAdmin", true);
		}
		model.addAttribute("listCriteria", criteriaService.getListCriteriaByType());
		model.addAttribute("roleId", userLogin.getRole().getId());
		return ROOT_COURSE + "/index";
	}

	@GetMapping("/create")
	public String newCourse(Model model) {
		model.addAttribute("title", "Quản lý khóa học");
		model.addAttribute("isCourses", true);
		model.addAttribute("categories", categoryService.getCategoryIsShow());
		User userLogin = userService.getUserCurrentLogin();
		if (userLogin.getRole().getId() != Constants.ROLE_TEACHER) {
			model.addAttribute("teachers", userService.getListTeacher());
			model.addAttribute("isAdmin", true);
			model.addAttribute("roleName", "administrator");
		}
		List<ProfileDto> profileDtos = profileService.getListProfileOfCourse();
		model.addAttribute("profileDtos", profileDtos);
		return ROOT_COURSE + "/new";
	}

	@GetMapping("/{id}/edit")
	public String editCourse(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("title", "Quản lý khóa học");
		model.addAttribute("isCourses", true);
		model.addAttribute("categories", categoryService.getCategoryIsShow());
		User userLogin = userService.getUserCurrentLogin();
		if (userLogin.getRole().getId() != Constants.ROLE_TEACHER) {
			model.addAttribute("teachers", userService.getListTeacher());
			model.addAttribute("isAdmin", true);
		}
		Course course = courseService.findById(id);
		List<CourseProfile> courseProfiles = courseProfileService.findByCourse(course);
		List<ProfileDto> profileDtos = profileService.getByStatusAndProfile();
		List<ProfileDto> newProfiles = new ArrayList<ProfileDto>();
		for (ProfileDto profileDto : profileDtos) {
			if (!checkProfileId(profileDto.getId(), courseProfiles))
				newProfiles.add(profileDto);
		}
//		List<ProfileDto> profileDtos = profileService.getByStatusAndProfile();
//		model.addAttribute("profileDtos", profileDtos);
		model.addAttribute("course", course);
		model.addAttribute("profileDtos", newProfiles);
		model.addAttribute("courseProfiles", courseProfiles);
		model.addAttribute("roleName", "administrator");
		return ROOT_COURSE + "/edit";
	}

	@GetMapping("/{id}/lessons")
	public String lesssons(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("title", "Quản lý bài giảng");
		model.addAttribute("isCourses", true);
		model.addAttribute("course", courseService.findById(id));
		User userLogin = userService.getUserCurrentLogin();
		model.addAttribute("roleId", userLogin.getRole().getId());
		model.addAttribute("roleName", "administrator");
		return ROOT_LESSON + "/index";
	}

	@GetMapping("/{id}/lessons/create")
	public String createLesson(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("title", "Quản lý bài giảng");
		model.addAttribute("isCourses", true);
		model.addAttribute("roleName", "administrator");
		model.addAttribute("course", courseService.findById(id));
		return ROOT_LESSON + "/new";
	}

	@GetMapping("/{id}/lessons/{lesson_id}/edit")
	public String editLesson(Model model, @PathVariable("id") Integer courseId,
			@PathVariable("lesson_id") Integer LessonId) {
		model.addAttribute("title", "Chỉnh sửa bài giảng");
		model.addAttribute("isCourses", true);
		model.addAttribute("roleName", "administrator");
		LessonOfCourseDto lessonDto = lessonService.findByIdDto(LessonId);
		if (lessonDto == null) {
			return "redirect:/admin/courses/" + courseId + "/lessons";
		}
		model.addAttribute("lesson", lessonDto);
//		model.addAttribute("course", courseService.findById(courseId));

		return ROOT_LESSON + "/edit";
	}

	@GetMapping("/{course_id}/class")
	public String classCourse(Model model, @PathVariable("course_id") Integer courseId) {
		model.addAttribute("title", "Quản lý lớp học");
		model.addAttribute("isCourses", true);
		List<User> listStudent = userService.getListEmailStudent();
		model.addAttribute("students", listStudent);
//		model.addAttribute("categories", categoryService.getCategoryIsShow());
//		User userLogin = userService.getUserCurrentLogin();
//		if (userLogin.getRole().getId() != Constants.ROLE_TEACHER) {
//			model.addAttribute("teachers", userService.getListTeacher());
//			model.addAttribute("isAdmin", true);
//		}
		model.addAttribute("roleName", "administrator");
		model.addAttribute("course_id", courseId);
		return ROOT_COURSE + "/class";
	}

	public boolean checkProfileId(Integer id, List<CourseProfile> courseProfiles) {
		for (CourseProfile courseProfile : courseProfiles) {
			if (courseProfile.getProfile().getId() == id)
				return true;
		}
		return false;
	}
}
