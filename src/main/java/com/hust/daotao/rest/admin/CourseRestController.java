package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.CourseDto;
import com.hust.daotao.dto.ProfileDto;
import com.hust.daotao.entity.Course;
import com.hust.daotao.entity.CourseProfile;
import com.hust.daotao.entity.User;
import com.hust.daotao.response.CourseResponse;
import com.hust.daotao.service.*;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import com.hust.daotao.validator.CourseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/courses")
public class CourseRestController {
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private CourseProfileService courseProfileService;
	@Autowired
	private AmazonUploadService amazonUploadService;

	@GetMapping("")
	public ResponseEntity<CourseResponse> index(@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "status", defaultValue = "") String statusValue,
			@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "category_id", defaultValue = "") String category,
			@RequestParam(name = "user_id", defaultValue = "") String user,
			@RequestParam(name = "length", required = false) Integer length,
			@RequestParam(name = "start", required = false) Integer start) {

		Boolean status = null;
		if (!statusValue.equals("")) {
			if (Integer.parseInt(statusValue) == 1) {
				status = true;
			} else if (Integer.parseInt(statusValue) == 0) {
				status = false;
			}
		}
		Integer categoryId = null;
		if (!category.equals("")) {
			categoryId = Integer.parseInt(category);
		}
		Integer userId = null;
		User userLogin = userService.getUserCurrentLogin();
		if (userLogin.getRole().getId() == Constants.ROLE_TEACHER) {
			userId = userLogin.getId();
		} else if (!user.equals("")) {
			userId = Integer.parseInt(user);
		}
		Integer pageDisplayLength = 10;
		if (length != null)
			pageDisplayLength = length;
		Constants.pageSize = pageDisplayLength;
		Integer iDisplayStart = 1;
		if (start != null)
			iDisplayStart = start;
		Integer page = (iDisplayStart / pageDisplayLength);
		Long count = courseService.countList(name, code, status, categoryId, userId);
		CourseResponse courseResponse = courseService.getList(page, name, code, categoryId, userId, status);
		courseResponse.setiTotalDisplayRecords(count);
		courseResponse.setiTotalRecords(count);
		return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);
	}

//	@GetMapping("/{id}")
//	public ResponseEntity<CourseResponse> show(@PathVariable("id") Integer id) {
//		if (id == null) {
//			CourseResponse CourseResponse = new CourseResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
//			return new ResponseEntity<CourseResponse>(CourseResponse, HttpStatus.OK);
//
//		}
//		CategoryDto topicDto = CourseService.findByIdDto(id);
//		if (topicDto == null) {
//			CourseResponse CourseResponse = new CourseResponse(Message.MESSAGE_ENTITY_NOT_EXIST,
//					Status.STATUS_ENTITY_NOT_EXIST);
//			return new ResponseEntity<CourseResponse>(CourseResponse, HttpStatus.OK);
//
//		}
//		CourseResponse CourseResponse = new CourseResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
//				Status.STATUS_GET_ENTITY_SUCCESS, topicDto);
//		return new ResponseEntity<CourseResponse>(CourseResponse, HttpStatus.OK);
//	}

	@PostMapping("")
	public ResponseEntity<CourseResponse> save(@RequestParam("name") String name,
			@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "status", defaultValue = "1") Integer statusValue,
			@RequestParam(name = "category", defaultValue = "0") Integer category,
			@RequestParam(name = "user_id", defaultValue = "0") Integer userId,
			@RequestParam(name = "image", required = false) MultipartFile image,
			@RequestParam(name = "link", defaultValue = "") String link,
			@RequestParam(name = "description", defaultValue = "") String description, HttpServletRequest request) {
		User userLogin = userService.getUserCurrentLogin();
		if (userLogin.getRole().getId() == Constants.ROLE_TEACHER)
			userId = userLogin.getId();
		List<ProfileDto> profileDtos = profileService.getByStatusAndProfile();
		if (CourseValidator.isEmpty(name, code, statusValue, description, category, userId) || CourseValidator.isEmpty(profileDtos, request)) {
//			|| CourseValidator.isEmpty(profileDtos, request)
			CourseResponse CourseResponse = new CourseResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CourseResponse>(CourseResponse, HttpStatus.OK);
		}

		if (courseService.findByCode(code) != null) {
			CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_EXIST_CODE, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);
		}
		String urlImage = (image != null ? this.amazonUploadService.uploadFile(image) : Constants.IMAGE_DEFAULT);
		Boolean status = statusValue > 0 ? true : false;
		CourseDto courseDto = new CourseDto(name, code, status, description, urlImage, userId, category, 0.0, link);
		List<CourseProfile> courseProfiles = new ArrayList<CourseProfile>();
		for (ProfileDto profileDto : profileDtos) {
			String value = request.getParameter(profileDto.getCode());
			CourseProfile courseProfile = courseProfileService.setProfile(profileDto.getCode(),
					new CourseProfile(value));
			courseProfiles.add(courseProfile);
		}
		CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_ADD_SUCCESS, Status.STATUS_ADD_SUCCESS);
		courseResponse.setElement(courseService.save(courseDto, courseProfiles));
		return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);
	}

	@PutMapping("")
	public ResponseEntity<CourseResponse> update(@RequestParam(name = "id", defaultValue = "0") Integer id,
			@RequestParam("name") String name, @RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "status", defaultValue = "1") Integer statusValue,
			@RequestParam(name = "category", defaultValue = "0") Integer category,
			@RequestParam(name = "user_id", defaultValue = "0") Integer userId,
			@RequestParam(name = "image", required = false) MultipartFile image,
			@RequestParam(name = "current_image", defaultValue = "") String imageCurrent,
			@RequestParam(name = "description", defaultValue = "") String description,
			@RequestParam(name = "link", defaultValue = "") String link, HttpServletRequest request,
			@RequestParam(name = "resultEvaluate", defaultValue = "0") double resultEvaluate) {
		User userLogin = userService.getUserCurrentLogin();
		if (userLogin.getRole().getId() == Constants.ROLE_TEACHER)
			userId = userLogin.getId();
		List<ProfileDto> profileDtos = profileService.getByStatusAndProfile();
		if (CourseValidator.isEmpty(name, code, statusValue, description, category, userId)|| CourseValidator.isEmpty(profileDtos, request)) {
			CourseResponse CourseResponse = new CourseResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CourseResponse>(CourseResponse, HttpStatus.OK);
		}
		Course courseByCode = courseService.findByCode(code);
		if (courseByCode != null && courseByCode.getId() != id) {
			CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_EXIST_CODE, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);
		}
		String urlImage = (image != null ? this.amazonUploadService.uploadFile(image) : imageCurrent);
		Boolean status = statusValue > 0 ? true : false;
		CourseDto courseDto = new CourseDto(name, code, status, description, urlImage, userId, category, resultEvaluate,
				link);
		courseDto.setId(id);
		List<CourseProfile> courseProfiles = new ArrayList<CourseProfile>();
		for (ProfileDto profileDto : profileDtos) {
			String value = request.getParameter(profileDto.getCode());
			if (value == null)
				value = "";
			CourseProfile courseProfile = courseProfileService.setProfile(profileDto.getCode(),
					new CourseProfile(value));
			courseProfiles.add(courseProfile);
		}
		CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_ADD_SUCCESS, Status.STATUS_ADD_SUCCESS);
		courseResponse.setElement(courseService.update(courseDto, courseProfiles));
		return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);
	}

//	@PutMapping("")
//	public ResponseEntity<CourseResponse> update(@RequestParam(name = "id", required = false) Integer id,
//			@RequestParam(name = "name", required = false) String name, @RequestParam(name = "code") String code,
//			@RequestParam(name = "status") Boolean status, @RequestParam(name = "description") String description,
//			@RequestParam(name = "image", required = false) MultipartFile image,
//			@RequestParam(name = "imageUrlCurrent") String imageUrlCurrent) {
//		name = UtilMethod.setValueIsNull(name);
//		code = UtilMethod.setValueIsNull(code);
//		description = UtilMethod.setValueIsNull(description);
//		String urlImage = (image != null ? UtilMethod.uploadFile(image, Constants.TYPE_IMAGE) : imageUrlCurrent);
//		if (CategoryValidator.isEmpty(name, code)) {
//			CourseResponse CourseResponse = new CourseResponse(Message.MESSAGE_REQUEST_EMPTY, Status.STATUS_REQUEST_ERROR);
//			return new ResponseEntity<CourseResponse>(CourseResponse, HttpStatus.OK);
//		}
//		CategoryDto resulTopicDto = CourseService.findByCode(code);
//		if (resulTopicDto != null && !id.equals(resulTopicDto.getId())) {
//			CourseResponse CourseResponse = new CourseResponse(Message.MESSAGE_EXIST_CODE, Status.STATUS_REQUEST_ERROR);
//			return new ResponseEntity<CourseResponse>(CourseResponse, HttpStatus.OK);
//		}
//		CategoryDto topicDto = new CategoryDto(name, code, description, urlImage, status);
//		topicDto.setId(id);
//		CourseResponse CourseResponse = new CourseResponse(Message.MESSAGE_UPDATE_SUCCESS, Status.STATUS_UPDATE_SUCCESS);
//		CourseResponse.setElement(CourseService.update(topicDto));
//		return new ResponseEntity<CourseResponse>(CourseResponse, HttpStatus.OK);
//	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CourseResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);

		} else if (!courseService.delete(id)) {
			CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_DELETE_FAIL, Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);
		}
		CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<CourseResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);

		} else if (!courseService.changeStatus(id)) {
			CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_CHANGE_STATUS_FAIL,
					Status.STATUS_CHANGE_STATUS_FAIL);
			return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);
		}
		CourseResponse courseResponse = new CourseResponse(Message.MESSAGE_CHANGE_STATUS_SUCCESS,
				Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<CourseResponse>(courseResponse, HttpStatus.OK);
	}
}
