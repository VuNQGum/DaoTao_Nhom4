package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.CriteriaTypeDto;
import com.hust.daotao.response.CriteriaTypeResponse;
import com.hust.daotao.service.CriteriaTypeService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import com.hust.daotao.validator.BaseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/criteria-type")
public class CriteriaTypeRestController {
	@Autowired
	private CriteriaTypeService criteriaTypeService;

	@GetMapping("")
	public ResponseEntity<CriteriaTypeResponse> index(@RequestParam(name = "code", defaultValue = "") String code,
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
		Long count = criteriaTypeService.countList(name, code, status);
		CriteriaTypeResponse criteriaTypeResponse = criteriaTypeService.getList(page, name, code, status);
		criteriaTypeResponse.setiTotalDisplayRecords(count);
		criteriaTypeResponse.setiTotalRecords(count);
		return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CriteriaTypeResponse> show(@PathVariable("id") Integer id) {
		if (id == null) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);

		}
		CriteriaTypeDto criteriaTypeDto = criteriaTypeService.findByIdDto(id);
		if (criteriaTypeDto == null) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_ENTITY_NOT_EXIST,
					Status.STATUS_ENTITY_NOT_EXIST);
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);

		}
		CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
				Status.STATUS_GET_ENTITY_SUCCESS, criteriaTypeDto);
		return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CriteriaTypeResponse> save(@RequestBody CriteriaTypeDto criteriaTypeDto) {
		if (BaseValidator.isEmpty(criteriaTypeDto.getName(), criteriaTypeDto.getCode())) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
		}
		if (criteriaTypeService.findByCode(criteriaTypeDto.getCode()) != null) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
		}
		CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_ADD_SUCCESS, Status.STATUS_ADD_SUCCESS);
		criteriaTypeResponse.setElement(criteriaTypeService.save(criteriaTypeDto));
		return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
	}

	@PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CriteriaTypeResponse> update(@RequestBody CriteriaTypeDto criteriaTypeDto) {
		
		if (BaseValidator.isEmpty(criteriaTypeDto.getName(), criteriaTypeDto.getCode())) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
		}
		CriteriaTypeDto resulDto = criteriaTypeService.findByCode(criteriaTypeDto.getCode());
		if (resulDto != null && !criteriaTypeDto.getId().equals(resulDto.getId())) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
		}
		CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_UPDATE_SUCCESS, Status.STATUS_UPDATE_SUCCESS);
		criteriaTypeResponse.setElement(criteriaTypeService.update(criteriaTypeDto));
		return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CriteriaTypeResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);

		} else if (!criteriaTypeService.delete(id)) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_DELETE_FAIL,
					Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
		}
		CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<CriteriaTypeResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);

		}
		if (!criteriaTypeService.changeStatus(id)) {
			CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_CHANGE_STATUS_FAIL,
					Status.STATUS_CHANGE_STATUS_FAIL);	
			return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
		}
		CriteriaTypeResponse criteriaTypeResponse = new CriteriaTypeResponse(Message.MESSAGE_CHANGE_STATUS_SUCCESS,
				Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<CriteriaTypeResponse>(criteriaTypeResponse, HttpStatus.OK);
	}
}
