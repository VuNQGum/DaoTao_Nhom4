package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.ProfileValueDto;
import com.hust.daotao.response.ProfileValueResponse;
import com.hust.daotao.service.ProfileValueService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import com.hust.daotao.validator.ProfileValueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/profile/values")

public class ProfileValueRestController {
	@Autowired
	private ProfileValueService profileValueService;

	@GetMapping("")
	public ResponseEntity<ProfileValueResponse> index(@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "status", defaultValue = "") String statusValue,
			@RequestParam(name = "length", required = false) Integer length,
			@RequestParam(name = "start", required = false) Integer start,
			@RequestParam(name = "profile_id", required = false) Integer profileId) {
		if (profileId == null) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
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
		Long count = profileValueService.countList(name, code, status, profileId);
		ProfileValueResponse profileValueResponse = profileValueService.getList(page, name, code, status, profileId);
		profileValueResponse.setiTotalDisplayRecords(count);
		profileValueResponse.setiTotalRecords(count);
		return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProfileValueResponse> show(@PathVariable("id") Integer id) {
		if (id == null) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);

		}
		ProfileValueDto profileValueDto = profileValueService.findByIdDto(id);
		if (profileValueDto == null) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_ENTITY_NOT_EXIST,
					Status.STATUS_ENTITY_NOT_EXIST);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);

		}
		ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
				Status.STATUS_GET_ENTITY_SUCCESS, profileValueDto);
		return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
	}

	@PostMapping(value = "")
	public ResponseEntity<ProfileValueResponse> save(@RequestBody ProfileValueDto profileValueDto) {
		if (ProfileValueValidator.isEmpty(profileValueDto.getName(), profileValueDto.getCode(),
				profileValueDto.getValue(), profileValueDto.getProfileId(), profileValueDto.getNameDisplayStudentSide(), profileValueDto.getNameDisplayTeacherSide())) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
		}
		ProfileValueDto resultDto = profileValueService.findByCodeAndProfile(profileValueDto.getCode(),
				profileValueDto.getProfileId());
		if (resultDto != null) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
		}
		ProfileValueDto resultValueDto = profileValueService.findByValueAndProfile(profileValueDto.getValue(),
				profileValueDto.getProfileId());
		if (resultValueDto != null) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_EXIST_VALUE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
		}
		ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_ADD_SUCCESS,
				Status.STATUS_ADD_SUCCESS);
		profileValueResponse.setElement(profileValueService.save(profileValueDto));
		return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
	}

	@PutMapping("")
	public ResponseEntity<ProfileValueResponse> update(@RequestBody ProfileValueDto profileValueDto) {
		if (ProfileValueValidator.isEmpty(profileValueDto.getName(), profileValueDto.getCode(),
				profileValueDto.getValue(), profileValueDto.getProfileId(), profileValueDto.getNameDisplayStudentSide(), profileValueDto.getNameDisplayTeacherSide())) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
		}
		ProfileValueDto resultDto = profileValueService.findByCodeAndProfile(profileValueDto.getCode(),
				profileValueDto.getProfileId());
		if (resultDto != null && !profileValueDto.getId().equals(resultDto.getId())) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
		}
		ProfileValueDto resultValueDto = profileValueService.findByValueAndProfile(profileValueDto.getValue(),
				profileValueDto.getProfileId());
		if (resultValueDto != null && !profileValueDto.getId().equals(profileValueDto.getId())) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_EXIST_VALUE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
		}
		ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_UPDATE_SUCCESS,
				Status.STATUS_UPDATE_SUCCESS);
		profileValueResponse.setElement(profileValueService.update(profileValueDto));
		return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ProfileValueResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);

		} else if (!profileValueService.delete(id)) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_DELETE_FAIL,
					Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
		}
		ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<ProfileValueResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);

		} else if (!profileValueService.changeStatus(id)) {
			ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_CHANGE_STATUS_FAIL,
					Status.STATUS_CHANGE_STATUS_FAIL);
			return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
		}
		ProfileValueResponse profileValueResponse = new ProfileValueResponse(Message.MESSAGE_CHANGE_STATUS_SUCCESS,
				Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<ProfileValueResponse>(profileValueResponse, HttpStatus.OK);
	}
}
