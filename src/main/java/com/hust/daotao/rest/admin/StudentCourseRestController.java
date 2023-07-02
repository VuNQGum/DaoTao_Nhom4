package com.hust.daotao.rest.admin;

import com.hust.daotao.response.StudentCourseResponse;
import com.hust.daotao.service.CourseService;
import com.hust.daotao.service.StudentCourseService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/class")

public class StudentCourseRestController {
	@Autowired
	private StudentCourseService studentCourseService;
	@Autowired
	private CourseService courseService;
	@GetMapping("")
	public ResponseEntity<StudentCourseResponse> index(@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "status", defaultValue = "") String statusValue,
			@RequestParam(name = "length", required = false) Integer length,
			@RequestParam(name = "start", required = false) Integer start,
			@RequestParam(name = "course_id", required = false) Integer courseId) {
		if (courseId == null) {
			StudentCourseResponse studentCourseResponse = new StudentCourseResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);
		}
		Boolean status = null;
		if (!statusValue.equals("")) {
			if (Integer.parseInt(statusValue) == 1) {
				status = true;
			} else if (Integer.parseInt(statusValue) == 0) {
				status = false;
			}
		}
		Integer pageDisplayLength = 10;
		if (length != null)
			pageDisplayLength = length;
		Constants.pageSize = pageDisplayLength;
		Integer iDisplayStart = 1;
		if (start != null)
			iDisplayStart = start;
		Integer page = (iDisplayStart / pageDisplayLength);
		Long count = studentCourseService.countList(name, email, status, courseId);
		StudentCourseResponse studentCourseResponse = studentCourseService.getList(page, name, email, status, courseId);
		studentCourseResponse.setiTotalDisplayRecords(count);
		studentCourseResponse.setiTotalRecords(count);
		return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);
	}
	
	@PutMapping("/{id}/change-status")
	public ResponseEntity<StudentCourseResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			StudentCourseResponse studentCourseResponse = new StudentCourseResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);

		} else if (!studentCourseService.changeStatus(id)) {
			StudentCourseResponse studentCourseResponse = new StudentCourseResponse(Message.MESSAGE_CHANGE_STATUS_FAIL,
					Status.STATUS_CHANGE_STATUS_FAIL);
			return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);
		}
		StudentCourseResponse studentCourseResponse = new StudentCourseResponse("Thêm sinh viên vào khóa học thành công",
				Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<StudentCourseResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			StudentCourseResponse studentCourseResponse = new StudentCourseResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);

		} else if (!studentCourseService.delete(id)) {
			StudentCourseResponse studentCourseResponse = new StudentCourseResponse(Message.MESSAGE_DELETE_FAIL,
					Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);
		}
		StudentCourseResponse studentCourseResponse = new StudentCourseResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);
	}
	
	@PostMapping("")
	public ResponseEntity<StudentCourseResponse> addStudent(@RequestParam("course_id") Integer courseId, @RequestParam("list_student_id") String[] listStudentId){
		if(courseId == null || courseService.findById(courseId) == null) {
			StudentCourseResponse studentCourseResponse = new StudentCourseResponse(Message.MESSAGE_NOT_EXIST_COURSE,
					Status.STATUS_FAIL);
			return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);
		}
		if(listStudentId == null || listStudentId.length < 1) {
			StudentCourseResponse studentCourseResponse = new StudentCourseResponse("Bạn chưa chọn sinh viên nào.",
					Status.STATUS_FAIL);
			return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);
		}
		studentCourseService.addStudent(courseId, listStudentId);
		StudentCourseResponse studentCourseResponse = new StudentCourseResponse("Thêm sinh viên vào khóa học thành công",
				Status.STATUS_SUCCESS);
		return new ResponseEntity<StudentCourseResponse>(studentCourseResponse, HttpStatus.OK);
		
	}
}
