package com.hust.daotao.controller.teacher;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hust.daotao.dto.LessonOfCourseDto;
import com.hust.daotao.dto.ProfileDto;
import com.hust.daotao.entity.Course;
import com.hust.daotao.entity.CourseProfile;
import com.hust.daotao.entity.User;
import com.hust.daotao.service.CategoryService;
import com.hust.daotao.service.CourseProfileService;
import com.hust.daotao.service.CourseService;
import com.hust.daotao.service.LessonOfCourseService;
import com.hust.daotao.service.ProfileService;
import com.hust.daotao.service.UserService;
import com.hust.daotao.util.Constants;
@Controller
@RequestMapping("/teacher/courses")
public class CourseTeacherController {
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
		model.addAttribute("roleName", "teacher");
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
		}
		List<ProfileDto> profileDtos = profileService.getByStatusAndProfile();
		model.addAttribute("profileDtos", profileDtos);
		model.addAttribute("roleName", "teacher");
		model.addAttribute("roleId", userLogin.getRole().getId());
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
		model.addAttribute("roleName", "teacher");
		model.addAttribute("roleId", userLogin.getRole().getId());
		return ROOT_COURSE + "/edit";
	}

	@GetMapping("/{id}/lessons")
	public String lesssons(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("title", "Quản lý bài giảng");
		model.addAttribute("isCourses", true);
		model.addAttribute("course", courseService.findById(id));
		model.addAttribute("roleName", "teacher");
		
		return ROOT_LESSON + "/index";
	}

	@GetMapping("/{id}/lessons/create")
	public String createLesson(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("title", "Quản lý bài giảng");
		model.addAttribute("isCourses", true);
		model.addAttribute("course", courseService.findById(id));
		model.addAttribute("roleName", "teacher");
		model.addAttribute("roleId", Constants.ROLE_TEACHER);
		return ROOT_LESSON + "/new";
	}

	@GetMapping("/{id}/lessons/{lesson_id}/edit")
	public String editLesson(Model model, @PathVariable("id") Integer courseId,
			@PathVariable("lesson_id") Integer LessonId) {
		model.addAttribute("title", "Chỉnh sửa bài giảng");
		model.addAttribute("isCourses", true);
		LessonOfCourseDto lessonDto = lessonService.findByIdDto(LessonId);
		if (lessonDto == null) {
			return "redirect:/admin/courses/" + courseId + "/lessons";
		}
		model.addAttribute("lesson", lessonDto);
//		model.addAttribute("course", courseService.findById(courseId));
		model.addAttribute("roleName", "teacher");
		model.addAttribute("roleId", Constants.ROLE_TEACHER);
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
		model.addAttribute("roleName", "teacher");

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
