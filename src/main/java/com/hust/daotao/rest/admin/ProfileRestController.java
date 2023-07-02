package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.ProfileDto;
import com.hust.daotao.response.ProfileResponse;
import com.hust.daotao.service.ProfileService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import com.hust.daotao.validator.ProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/profiles")
public class ProfileRestController {
	@Autowired
	private ProfileService profileService;

	@GetMapping("")
	public ResponseEntity<ProfileResponse> index(@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "status", defaultValue = "") String statusValue,
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
		Long count = profileService.countList(name, code, status);
		ProfileResponse profileResponse = profileService.getList(page, name, code, status);
		profileResponse.setiTotalDisplayRecords(count);
		profileResponse.setiTotalRecords(count);
		return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProfileResponse> show(@PathVariable("id") Integer id) {
		if (id == null) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);

		}
		ProfileDto profileDto = profileService.findByIdDto(id);
		if (profileDto == null) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_ENTITY_NOT_EXIST,
					Status.STATUS_ENTITY_NOT_EXIST);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);

		}
		ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
				Status.STATUS_GET_ENTITY_SUCCESS, profileDto);
		return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfileResponse> save(@RequestBody ProfileDto profileDto) {
		if (ProfileValidator.isEmpty(profileDto.getName(), profileDto.getNameDisplayStudentSide(),
				profileDto.getNameDisplayTeacherSide(), profileDto.getCode(), profileDto.getWeight())) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
		}
		if (profileService.findByCode(profileDto.getCode()) != null) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
		}
		ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_ADD_SUCCESS, Status.STATUS_ADD_SUCCESS);
		profileResponse.setElement(profileService.save(profileDto));
		return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
	}

	@PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfileResponse> update(@RequestBody ProfileDto profileDto) {

		if (ProfileValidator.isEmpty(profileDto.getName(), profileDto.getNameDisplayStudentSide(),
				profileDto.getNameDisplayTeacherSide(), profileDto.getCode(), profileDto.getWeight())) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
		}
		ProfileDto resulProfileDto = profileService.findByCode(profileDto.getCode());
		if (resulProfileDto != null && !profileDto.getId().equals(resulProfileDto.getId())) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
		}
		ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_UPDATE_SUCCESS,
				Status.STATUS_UPDATE_SUCCESS);
		profileResponse.setElement(profileService.update(profileDto));
		return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ProfileResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);

		} else if (!profileService.delete(id)) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_DELETE_FAIL,
					Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
		}
		ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<ProfileResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);

		} else if (!profileService.changeStatus(id)) {
			ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_CHANGE_STATUS_FAIL,
					Status.STATUS_CHANGE_STATUS_FAIL);
			return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
		}
		ProfileResponse profileResponse = new ProfileResponse(Message.MESSAGE_CHANGE_STATUS_SUCCESS,
				Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<ProfileResponse>(profileResponse, HttpStatus.OK);
	}

}
