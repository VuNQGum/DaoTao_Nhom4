package com.hust.daotao.controller.client;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hust.daotao.dto.CourseDto;
import com.hust.daotao.dto.DataCrawSoict;
import com.hust.daotao.dto.Panigation;
import com.hust.daotao.dto.StudentCourseDto;
import com.hust.daotao.entity.StudentCourse;
import com.hust.daotao.entity.User;
import com.hust.daotao.response.CourseResponse;
import com.hust.daotao.service.CategoryService;
import com.hust.daotao.service.ContextMatching;
import com.hust.daotao.service.CourseService;
import com.hust.daotao.service.StudentCourseService;
import com.hust.daotao.service.UserService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.GetDocumentFromURL;

@Controller
@RequestMapping("/courses")
public class CourseClientController {
	public static final String ROOT_CLIENT_COURSE = "client/courses";

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private StudentCourseService studentCourseService;
	@Autowired
	private ContextMatching contextMatching;

	@GetMapping("")
	public String courses(Model model, @RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "showing", defaultValue = "4") Integer quantityShowing,
			@RequestParam(name = "category", defaultValue = "0") Integer categoryId,
			@RequestParam(name = "teacher", defaultValue = "0") Integer teacherId,
			@RequestParam(name = "course_name", defaultValue = "") String courseName) {
		model.addAttribute("title", "Danh sách khóa học");
		model.addAttribute("isCourses", true);
		model.addAttribute("categories", categoryService.getCategoryIsShow());
		CourseResponse courseResponse = courseService.getList(page, quantityShowing, courseName, categoryId, teacherId);
		Panigation panigation = new Panigation(page, courseResponse.getITotalPage());
		model.addAttribute("response", courseResponse);
		model.addAttribute("panigation", panigation);
		model.addAttribute("teachers", userService.getListTeacher());
		model.addAttribute("category_id", categoryId);
		model.addAttribute("teacher_id", teacherId);
		model.addAttribute("course_name", courseName);
		List<DataCrawSoict> newsHome = GetDocumentFromURL.getNewsBySoictHome();

		model.addAttribute("newsHome", newsHome);
		List<CourseDto> yourCourses = new ArrayList<CourseDto>();
		User user = userService.getUserCurrentLogin();
		if (user != null && user.getRole().getId() == Constants.ROLE_STUDENT) {
			yourCourses = courseService.convertListBoToListDto(contextMatching.contextMatching(user, 3));
		}
		model.addAttribute("yourCourses", yourCourses);

		return ROOT_CLIENT_COURSE + "/courses";
	}

	@GetMapping("/{course_code}")
	public String detail(Model model, @PathVariable("course_code") String code, HttpServletRequest request) {

		CourseDto result = courseService.getCourseShow(code);
		model.addAttribute("title", "Khóa học " + result.getName());

		model.addAttribute("course", result);
		User userLogin = userService.getUserCurrentLogin();

		if (userLogin != null) {
			StudentCourse studentCourse = studentCourseService.findByCourseAndStudent(userLogin.getId(),
					result.getId());
			if (studentCourse == null) {
				model.addAttribute("course_register_status", "Chưa đăng ký");

			} else if (studentCourse.getStatus()) {
				model.addAttribute("course_register_status", "Đã đăng ký");
				model.addAttribute("status_regist_btn", true);

			} else if (!studentCourse.getStatus()) {
				model.addAttribute("course_register_status", "Đang gửi đăng ký");
			}
		} else {
			model.addAttribute("course_register_status", "Chưa đăng ký");
		}
		List<CourseDto> yourCourses = new ArrayList<CourseDto>();
		if (userLogin != null && userLogin.getRole().getId() == Constants.ROLE_STUDENT) {
			User user = userService.getUserCurrentLogin();
			yourCourses = courseService.convertListBoToListDto(contextMatching.contextMatching(user, 3));
		}
		String url = request.getRequestURL().toString();
		model.addAttribute("urlWeb", url);
		model.addAttribute("yourCourses", yourCourses);
		return ROOT_CLIENT_COURSE + "/course";

	}

	private String extracted() {
		return "redirect:/courses";
	}

	@GetMapping("/my-courses")
	public String getMyCourses(Model model) {
		List<StudentCourseDto> list = studentCourseService.getListCourseOfStudent();
		model.addAttribute("list", list);
		model.addAttribute("categories", categoryService.getCategoryIsShow());
		model.addAttribute("title", "Khóa học của tôi");
		model.addAttribute("isCourses", true);
		return ROOT_CLIENT_COURSE + "/my-courses";
	}

	@GetMapping("my-courses/{course_code}/learn")
	public String learning(Model model, @PathVariable("course_code") String code) {
		User userLogin = userService.getUserCurrentLogin();
		CourseDto result = courseService.getCourseShow(code);
		if (result == null)
			return extracted();
		if (studentCourseService.findByCourseAndStudentLearn(result.getId(), userLogin.getId()) == null) {
			return "redirect:/courses/my-courses";
		}
		model.addAttribute("title", "Khóa học " + result.getName());
		model.addAttribute("course", result);
		return ROOT_CLIENT_COURSE + "/learn-course";

	}
}
