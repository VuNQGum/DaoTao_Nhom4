package com.hust.daotao.rest.client;

import com.hust.daotao.dto.StudentCourseDto;
import com.hust.daotao.entity.User;
import com.hust.daotao.response.Response;
import com.hust.daotao.service.CourseService;
import com.hust.daotao.service.StudentCourseService;
import com.hust.daotao.service.UserService;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/courses/")
public class CourseClientRestController {
	@Autowired
	private CourseService courseSerivce;
	@Autowired
	private UserService userService;
	@Autowired
	private StudentCourseService studentCourseService;

	@GetMapping("list-name")
	public ResponseEntity<List<String>> getListNameCourse() {
		return new ResponseEntity<List<String>>(courseSerivce.getListNameCourse(), HttpStatus.OK);
	}

	@PostMapping(value = "register")
	public ResponseEntity<Response> register(@RequestParam("course_id") Integer courseId) {
		User user = userService.getUserCurrentLogin();

		if (user == null) {
			Response res = new Response(Message.MESSAGE_NOT_LOGIN, Status.STATUS_NOT_LOGIN);
			return new ResponseEntity<Response>(res, HttpStatus.OK);

		}
		if (user.getResultTest() == null) {
			Response res = new Response("Chưa làm bài khảo sát.", 300);
			return new ResponseEntity<Response>(res, HttpStatus.OK);
		}
		StudentCourseDto studentCourseDto = new StudentCourseDto(courseId, user.getId(), true);
		StudentCourseDto result = studentCourseService.registerCourse(studentCourseDto);
		if (result == null) {
			Response res = new Response(Message.MESSAGE_REGIST_COURSE_ERROR, Status.STATUS_FAIL);
			return new ResponseEntity<Response>(res, HttpStatus.BAD_REQUEST);

		}
		Response res = new Response("Đăng ký khóa học thành công.", Status.STATUS_SUCCESS);
		return new ResponseEntity<Response>(res, HttpStatus.OK);
	}

}
