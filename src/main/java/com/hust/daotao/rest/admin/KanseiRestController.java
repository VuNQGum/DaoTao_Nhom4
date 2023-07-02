package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.KanseiWordDto;
import com.hust.daotao.response.KanseiWordResponse;
import com.hust.daotao.service.KanseiWordService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/kansei-words")
public class KanseiRestController {
	@Autowired
	private KanseiWordService kanseiService;

	@GetMapping("")
	public ResponseEntity<KanseiWordResponse> index(@RequestParam(name = "positive", defaultValue = "") String positive,
			@RequestParam(name = "negative", defaultValue = "") String negative,
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
		Long count = kanseiService.countList(negative, positive, status);
		KanseiWordResponse kainseiResponse = kanseiService.getList(page, negative, positive, status);
		kainseiResponse.setiTotalDisplayRecords(count);
		kainseiResponse.setiTotalRecords(count);
		return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<KanseiWordResponse> show(@PathVariable("id") Integer id) {
		if (id == null) {
			KanseiWordResponse kainseiResponse = new KanseiWordResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);

		}
		KanseiWordDto kanseiDto = kanseiService.findByIdDto(id);
		if (kanseiDto == null) {
			KanseiWordResponse kainseiResponse = new KanseiWordResponse(
					Message.MESSAGE_ENTITY_NOT_EXIST, Status.STATUS_ENTITY_NOT_EXIST);
			return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);

		}
		KanseiWordResponse kainseiResponse = new KanseiWordResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
				Status.STATUS_GET_ENTITY_SUCCESS, kanseiDto);
		return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<KanseiWordResponse> save(@RequestBody KanseiWordDto kanseiDto) {
//		if (BaseValidator.isEmpty(kanseiDto.getName(), kanseiDto.getCode())) {
//			CriteriaKanseiWordResponse kainseiResponse = new CriteriaKanseiWordResponse(Message.MESSAGE_REQUEST_EMPTY,
//					Status.STATUS_REQUEST_ERROR);
//			return new ResponseEntity<CriteriaKanseiWordResponse>(kainseiResponse, HttpStatus.OK);
//		}
//		if (kanseiService.findByCode(CriteriaKanseiWordDto.getCode()) != null) {
//			CriteriaKanseiWordResponse kainseiResponse = new CriteriaKanseiWordResponse(Message.MESSAGE_EXIST_CODE,
//					Status.STATUS_REQUEST_ERROR);
//			return new ResponseEntity<CriteriaKanseiWordResponse>(kainseiResponse, HttpStatus.OK);
//		}
		KanseiWordResponse kainseiResponse = new KanseiWordResponse(Message.MESSAGE_ADD_SUCCESS,
				Status.STATUS_ADD_SUCCESS);
		kainseiResponse.setElement(kanseiService.save(kanseiDto));
		return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);
	}

	@PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<KanseiWordResponse> update(@RequestBody KanseiWordDto kanseiDto) {

//		if (BaseValidator.isEmpty(CriteriaKanseiWordDto.getName(), CriteriaKanseiWordDto.getCode())) {
//			CriteriaKanseiWordResponse kainseiResponse = new CriteriaKanseiWordResponse(Message.MESSAGE_REQUEST_EMPTY,
//					Status.STATUS_REQUEST_ERROR);
//			return new ResponseEntity<kainseiResponse>(kainseiResponse, HttpStatus.OK);
//		}
//		CriteriaKanseiWordDto resultDto = kanseiService.findByCode(CriteriaKanseiWordDto.getCode());
//		if (resultDto != null && !resultDto.getId().equals(CriteriaKanseiWordDto.getId())) {
//			CriteriaKanseiWordResponse kainseiResponse = new CriteriaKanseiWordResponse(Message.MESSAGE_EXIST_CODE,
//					Status.STATUS_REQUEST_ERROR);
//			return new ResponseEntity<CriteriaKanseiWordResponse>(kainseiResponse, HttpStatus.OK);
//		}
		KanseiWordResponse kainseiResponse = new KanseiWordResponse(Message.MESSAGE_UPDATE_SUCCESS,
				Status.STATUS_UPDATE_SUCCESS);
		kainseiResponse.setElement(kanseiService.update(kanseiDto));
		return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<KanseiWordResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			KanseiWordResponse kainseiResponse = new KanseiWordResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);

		} else if (!kanseiService.delete(id)) {
			KanseiWordResponse kainseiResponse = new KanseiWordResponse(Message.MESSAGE_DELETE_FAIL,
					Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);
		}
		KanseiWordResponse kainseiResponse = new KanseiWordResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<KanseiWordResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			KanseiWordResponse kainseiResponse = new KanseiWordResponse(Message.MESSAGE_NULL,
					Status.STATUS_NULL);
			return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);

		}
		if (!kanseiService.changeStatus(id)) {
			KanseiWordResponse kainseiResponse = new KanseiWordResponse(
					Message.MESSAGE_CHANGE_STATUS_FAIL, Status.STATUS_CHANGE_STATUS_FAIL);
			return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);
		}
		KanseiWordResponse kainseiResponse = new KanseiWordResponse(
				Message.MESSAGE_CHANGE_STATUS_SUCCESS, Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<KanseiWordResponse>(kainseiResponse, HttpStatus.OK);
	}
}