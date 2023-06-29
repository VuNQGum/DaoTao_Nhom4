package com.hust.daotao.rest.client;

import com.hust.daotao.dto.ProfileDto;
import com.hust.daotao.dto.UserDto;
import com.hust.daotao.entity.User;
import com.hust.daotao.entity.UserProfile;
import com.hust.daotao.response.UserResponse;
import com.hust.daotao.service.AmazonUploadService;
import com.hust.daotao.service.ProfileService;
import com.hust.daotao.service.UserProfileService;
import com.hust.daotao.service.UserService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import com.hust.daotao.util.UtilMethod;
import com.hust.daotao.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	@Autowired
	private ProfileService profileService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserProfileService userProfileSerivce;
	@Autowired
	private AmazonUploadService amazonUploadService;

	@PostMapping("")
	public ResponseEntity<UserResponse> save(@RequestParam(name = "name", defaultValue = "") String fullName,
			@RequestParam(name = "subject_id", defaultValue = "0") Integer subjectId,
			@RequestParam(name = "student_code", defaultValue = "") String studentCode,
			@RequestParam(name = "password", defaultValue = "") String password,
			@RequestParam(name = "repassword", defaultValue = "") String rePassword,
			@RequestParam(name = "birthday", defaultValue = "") String birthday,
			@RequestParam(name = "phone", defaultValue = "") String phone,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "image", required = false) MultipartFile image,
			@RequestParam(name = "description", defaultValue = "") String description, HttpServletRequest request) {
		if (UserValidator.isEmpty(subjectId, fullName, studentCode, email, password, rePassword)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_REQUEST_EMPTY, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}

		List<ProfileDto> profileDtos = profileService.getListProfileOfStudent();
		for (ProfileDto profileDto : profileDtos) {
			if (request.getParameter(profileDto.getCode()) == null
					|| request.getParameter(profileDto.getCode()).isEmpty()) {
				UserResponse userResponse = new UserResponse(Message.MESSAGE_REQUEST_EMPTY,
						Status.STATUS_REQUEST_ERROR);
				return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
			}
		}

		if (!UserValidator.isEmail(email)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_NOT_EMAIL, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		if (userService.findByEmail(email) != null) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_EXIST_EMAIL, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		if (!UserValidator.isPhone(phone) && !"".equals(phone.trim())) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_NOT_PHONE, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		if (!UserValidator.checkLengthPassword(password)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_LENGTH_PASSWORD, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		if (!UserValidator.confirmPassword(password, rePassword)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_LENGTH_PASSWORD, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		String urlImage = (image != null ? amazonUploadService.uploadFile(image) : Constants.IMAGE_DEFAULT);
		UserDto resultDto = new UserDto();
		UserDto userDto = new UserDto(fullName, subjectId, studentCode, email, UtilMethod.encode(password), phone,
				urlImage, birthday, true, Constants.ROLE_STUDENT, description);
		List<UserProfile> userProfiles = new ArrayList<UserProfile>();
		for (ProfileDto profileDto : profileDtos) {
			String value = request.getParameter(profileDto.getCode());
			UserProfile userProfile = userProfileSerivce.setProfile(profileDto.getCode(), new UserProfile(value));
			userProfiles.add(userProfile);
		}
		resultDto = userService.save(userDto, userProfiles);

		UserResponse userResponse = new UserResponse(Message.MESSAGE_REGISTER_SUCCESS, Status.STATUS_REGISTER_SUCCESS);
		userResponse.setElement(resultDto);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@PutMapping("/change-password")
	public ResponseEntity<UserResponse> changePassword(@RequestParam("current_password") String currentPassword,
			@RequestParam("password") String password, @RequestParam("repassword") String rePassword) {
		if (UserValidator.isEmpty(currentPassword, password, rePassword)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_REQUEST_EMPTY, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		User userLogin = userService.getUserCurrentLogin();
		if (!UserValidator.checkCurrentPassword(currentPassword, userLogin.getPassword())) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_ERROR_CURRENT_PASSWORD_,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		if (!UserValidator.checkLengthPassword(password)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_LENGTH_PASSWORD, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		if (!UserValidator.confirmPassword(password, rePassword)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_CONFIRM_PASSWORD, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		UserResponse userResponse = new UserResponse(Message.MESSAGE_UPDATE_PASSWORD_SUCCESS, Status.STATUS_SUCCESS);
		userLogin.setPassword(UtilMethod.encode(password));
		UserDto resultDto = userService.updatePassword(userLogin);
		userResponse.setElement(resultDto);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@GetMapping("/user-login")
	public ResponseEntity<UserDto> getUserLogin() {
		User userLogin = userService.getUserCurrentLogin();
		return new ResponseEntity<UserDto>(userService.convertBoToDto(userLogin), HttpStatus.OK);

	}

	@PutMapping("")
	public ResponseEntity<UserResponse> update(@RequestParam("name") String fullName,
			@RequestParam(name = "student_code", defaultValue = "") String studentCode,
			@RequestParam(name = "subject_id", required = false) Integer subjectId,
			@RequestParam("birthday") String birthday, @RequestParam("phone") String phone,
			@RequestParam("email") String email, @RequestParam(name = "image", required = false) MultipartFile image,
			@RequestParam(name = "description", defaultValue = "") String description, HttpServletRequest request) {
		User userLogin = userService.getUserCurrentLogin();
		if (userLogin.getRole().getId() == Constants.ROLE_STUDENT) {
			if (UserValidator.isEmpty(subjectId, fullName, studentCode, email, userLogin.getPassword(),
					userLogin.getPassword())) {
				UserResponse userResponse = new UserResponse(Message.MESSAGE_REQUEST_EMPTY,
						Status.STATUS_REQUEST_ERROR);
				return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
			}
		} else {
			if (UserValidator.isEmpty(fullName, email)) {
				UserResponse userResponse = new UserResponse(Message.MESSAGE_REQUEST_EMPTY,
						Status.STATUS_REQUEST_ERROR);
				return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
			}
		}
		List<ProfileDto> profileDtos = profileService.getListProfileOfStudent();

		if (userLogin.getRole().getId() == Constants.ROLE_STUDENT) {
			for (ProfileDto profileDto : profileDtos) {
				if (request.getParameter(profileDto.getCode()) == null
						|| request.getParameter(profileDto.getCode()).isEmpty()) {
					UserResponse userResponse = new UserResponse(Message.MESSAGE_REQUEST_EMPTY,
							Status.STATUS_REQUEST_ERROR);
					return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
				}
			}
		}

		if (!UserValidator.isEmail(email)) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_NOT_EMAIL, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		User resultUser = userService.findByEmail(email);
		if (resultUser != null && resultUser.getId() != userLogin.getId()) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_EXIST_EMAIL, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		if (!UserValidator.isPhone(phone) && !"".equals(phone.trim())) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_NOT_PHONE, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		String urlImage = (image != null ? amazonUploadService.uploadFile(image) : userLogin.getImage());

		UserDto resultDto = new UserDto();
		UserDto userDto = new UserDto(fullName, subjectId, studentCode, email, userLogin.getPassword(), phone, urlImage,
				birthday, true, userLogin.getRole().getId(), description);
		userDto.setId(userLogin.getId());
		List<UserProfile> userProfiles = new ArrayList<UserProfile>();
		for (ProfileDto profileDto : profileDtos) {
			String value = request.getParameter(profileDto.getCode());
			UserProfile userProfile = userProfileSerivce.setProfile(profileDto.getCode(), new UserProfile(value));
			userProfiles.add(userProfile);
		}
		resultDto = userService.update(userDto, userProfiles);

		UserResponse userResponse = new UserResponse(Message.MESSAGE_UPDATE_SUCCESS, Status.STATUS_UPDATE_SUCCESS);
		userResponse.setElement(resultDto);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@PutMapping("/forgot-password")
	public ResponseEntity<UserResponse> forgotPassword(@RequestParam("email") String email) {
		if (email.isEmpty() || email == null) {
			UserResponse userResponse = new UserResponse("Bạn chưa nhập email đã đăng ký", Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		User user = userService.findByEmail(email);
		if (user == null) {
			UserResponse userResponse = new UserResponse(Message.MESSAGE_NOT_EXIST_EMAIL, Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		}
		UserResponse userResponse = new UserResponse(Message.MESSAGE_FORGOT_PASSWORD_SUCCESS, Status.STATUS_SUCCESS);
		UserDto resultDto = userService.forgotPassword(user);
		userResponse.setElement(resultDto);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

}
