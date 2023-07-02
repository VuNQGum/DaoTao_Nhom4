package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.LessonOfCourseDto;
import com.hust.daotao.entity.LessonOfCourse;
import com.hust.daotao.response.LessonOfCourseResponse;
import com.hust.daotao.service.AmazonUploadService;
import com.hust.daotao.service.LessonOfCourseService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import com.hust.daotao.validator.LessonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/lessons")
public class LessonOfCourseRestController {
	@Autowired
	private LessonOfCourseService lessonService;
	@Autowired
	private AmazonUploadService amazonUploadService;

	@GetMapping("")
	public ResponseEntity<LessonOfCourseResponse> index(@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "status", defaultValue = "") String statusValue,
			@RequestParam(name = "length", required = false) Integer length,
			@RequestParam(name = "start", required = false) Integer start,
			@RequestParam("course_id") Integer courseId) {

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
		Long count = lessonService.countList(name, code, status, courseId);
		LessonOfCourseResponse lessonResponse = lessonService.getList(page, name, code, status, courseId);
		lessonResponse.setiTotalDisplayRecords(count);
		lessonResponse.setiTotalRecords(count);
		return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
	}

	@PostMapping(value = "")
	public ResponseEntity<LessonOfCourseResponse> save(@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "course_id", defaultValue = "0") Integer courseId,
			@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "description", defaultValue = "") String description,
			@RequestParam(name = "link_other", defaultValue = "linkOther") String linkOther,
			@RequestParam(name = "status", defaultValue = "true") Boolean status,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		if (LessonValidator.isEmpty(name, code, status)) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
		}

		if (file == null && linkOther.isEmpty()) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
		}

		if (lessonService.findByCode(code) != null) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
		}
		String url = (file != null ? this.amazonUploadService.uploadFile(file) : linkOther);
		LessonOfCourseDto lessonDto = new LessonOfCourseDto(name, code, description, url, status, courseId);
		LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_ADD_SUCCESS,
				Status.STATUS_ADD_SUCCESS);
		lessonResponse.setElement(lessonService.save(lessonDto));
		return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
	}

	@PutMapping(value = "")
	public ResponseEntity<LessonOfCourseResponse> update(@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "course_id", defaultValue = "0") Integer courseId,
			@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "description", defaultValue = "") String description,
			@RequestParam(name = "link_other", defaultValue = "") String linkOther,
			@RequestParam(name = "status", defaultValue = "true") Boolean status,
			@RequestParam(name = "file", required = false) MultipartFile file,
			@RequestParam(name = "lesson_id", defaultValue = "0") Integer lessonId,
			@RequestParam(name = "current_url", defaultValue = "") String currentUrl) {
		if (LessonValidator.isEmpty(name, code, status, lessonId)) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
		}
		LessonOfCourse lessonByCode = lessonService.findByCode(code);
		if (lessonByCode != null && lessonByCode.getId() != lessonId) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
		}
		if (!linkOther.isEmpty())
			currentUrl = linkOther;
		String url = (file != null ? this.amazonUploadService.uploadFile(file) : currentUrl);
		LessonOfCourseDto lessonDto = new LessonOfCourseDto(lessonId, name, code, description, url, status, courseId);
		LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_UPDATE_SUCCESS,
				Status.STATUS_ADD_SUCCESS);
		lessonResponse.setElement(lessonService.save(lessonDto));
		return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<LessonOfCourseResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);

		} else if (!lessonService.changeStatus(id)) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_CHANGE_STATUS_FAIL,
					Status.STATUS_CHANGE_STATUS_FAIL);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
		}
		LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_CHANGE_STATUS_SUCCESS,
				Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<LessonOfCourseResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);

		} else if (!lessonService.delete(id)) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_DELETE_FAIL,
					Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
		}
		LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LessonOfCourseResponse> show(@PathVariable("id") Integer id) {
		if (id == null) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);

		}
		LessonOfCourseDto lessonDto = lessonService.findByIdDto(id);
		if (lessonDto == null) {
			LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_ENTITY_NOT_EXIST,
					Status.STATUS_ENTITY_NOT_EXIST);
			return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);

		}
		LessonOfCourseResponse lessonResponse = new LessonOfCourseResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
				Status.STATUS_GET_ENTITY_SUCCESS, lessonDto);
		return new ResponseEntity<LessonOfCourseResponse>(lessonResponse, HttpStatus.OK);
	}

}
