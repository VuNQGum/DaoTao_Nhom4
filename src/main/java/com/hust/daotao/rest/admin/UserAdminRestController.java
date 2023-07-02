package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.UserDto;
import com.hust.daotao.entity.User;
import com.hust.daotao.response.UserResponse;
import com.hust.daotao.service.UserService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import com.hust.daotao.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserAdminRestController {
	@Autowired
	private UserService userService;

	@GetMapping("")
	public ResponseEntity<UserResponse> index(@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "status", defaultValue = "") String statusValue,
			@RequestParam(name = "role_id", defaultValue = "3") int role,
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
		
		Integer pageDisplayLength = 10;
		if (length != null)
			pageDisplayLength = length;
		Constants.pageSize = pageDisplayLength;
		Integer iDisplayStart = 1;
		if (start != null)
			iDisplayStart = start;
		Integer page = (iDisplayStart / pageDisplayLength);
		Long count = userService.countList(name, email, status, role);
		UserResponse userResponse = userService.getList(page, name, email, status, role);
		userResponse.setiTotalDisplayRecords(count);
		userResponse.setiTotalRecords(count);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

//	@GetMapping("/{id}")
//	public ResponseEntity<UserResponse> show(@PathVariable("id") Integer id) {
//		if (id == null) {
//			UserResponse UserResponse = new UserResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
//			return new ResponseEntity<UserResponse>(UserResponse, HttpStatus.OK);
//
//		}
//		CategoryDto topicDto = userService.findByIdDto(id);
//		if (topicDto == null) {
//			UserResponse UserResponse = new UserResponse(Message.MESSAGE_ENTITY_NOT_EXIST,
//					Status.STATUS_ENTITY_NOT_EXIST);
//			return new ResponseEntity<UserResponse>(UserResponse, HttpStatus.OK);
//
//		}
//		UserResponse UserResponse = new UserResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
//				Status.STATUS_GET_ENTITY_SUCCESS, topicDto);
//		return new ResponseEntity<UserResponse>(UserResponse, HttpStatus.OK);
//	}

	@PostMapping("")
	public ResponseEntity<UserResponse> save(@RequestBody UserDto userDto) {
		if (UserValidator.isEmpty(userDto.getFullName(), userDto.getEmail())) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_REQUEST_EMPTY, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		if (!UserValidator.isEmail(userDto.getEmail())) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_NOT_EMAIL, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		if (userService.findByEmail(userDto.getEmail()) != null) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_EXIST_EMAIL, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		UserResponse UserResponse = new UserResponse(Message.MESSAGE_ADD_SUCCESS, Status.STATUS_ADD_SUCCESS);
		UserResponse.setElement(userService.save(userDto));
		return new ResponseEntity<UserResponse>(UserResponse, HttpStatus.OK);
	}
//
//	@PutMapping("")
//	public ResponseEntity<UserResponse> update(@RequestParam(name = "id", required = false) Integer id,
//			@RequestParam(name = "name", required = false) String name, @RequestParam(name = "code") String code,
//			@RequestParam(name = "status") Boolean status, @RequestParam(name = "description") String description,
//			@RequestParam(name = "image", required = false) MultipartFile image,
//			@RequestParam(name = "imageUrlCurrent") String imageUrlCurrent) {
//		name = UtilMethod.setValueIsNull(name);
//		code = UtilMethod.setValueIsNull(code);
//		description = UtilMethod.setValueIsNull(description);
//		String urlImage = (image != null ? UtilMethod.uploadFile(image, Constants.TYPE_IMAGE) : imageUrlCurrent);
//		if (CategoryValidator.isEmpty(name, code)) {
//			UserResponse UserResponse = new UserResponse(Message.MESSAGE_REQUEST_EMPTY, Status.STATUS_REQUEST_ERROR);
//			return new ResponseEntity<UserResponse>(UserResponse, HttpStatus.OK);
//		}
//		CategoryDto resulTopicDto = userService.findByCode(code);
//		if (resulTopicDto != null && !id.equals(resulTopicDto.getId())) {
//			UserResponse UserResponse = new UserResponse(Message.MESSAGE_EXIST_CODE, Status.STATUS_REQUEST_ERROR);
//			return new ResponseEntity<UserResponse>(UserResponse, HttpStatus.OK);
//		}
//		CategoryDto topicDto = new CategoryDto(name, code, description, urlImage, status);
//		topicDto.setId(id);
//		UserResponse UserResponse = new UserResponse(Message.MESSAGE_UPDATE_SUCCESS, Status.STATUS_UPDATE_SUCCESS);
//		UserResponse.setElement(userService.update(topicDto));
//		return new ResponseEntity<UserResponse>(UserResponse, HttpStatus.OK);
//	}

	@DeleteMapping("/{id}")
	public ResponseEntity<UserResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);

		} else if (!userService.delete(id)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_DELETE_FAIL, Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		UserResponse userResponse = new UserResponse(Message.MESSAGE_DELETE_SUCCESS, Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<UserResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);

		} else if (!userService.changeStatus(id)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_CHANGE_STATUS_FAIL,
					Status.STATUS_CHANGE_STATUS_FAIL);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		UserResponse userResponse = new UserResponse(Message.MESSAGE_CHANGE_STATUS_SUCCESS,
				Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@GetMapping("/list-email")
	public ResponseEntity<List<User>> getListEmail() {
		List<User> listStudent = userService.getListEmailStudent();
		return new ResponseEntity<List<User>>(listStudent, HttpStatus.OK);
	}
	@GetMapping("/find-student-by-email")
	public ResponseEntity<UserDto> getListEmail(@RequestParam(name = "email", defaultValue = "") String email) {
		UserDto student = userService.getInfoStudentByEmail(email);
		return new ResponseEntity<UserDto>(student, HttpStatus.OK);
	}

}
