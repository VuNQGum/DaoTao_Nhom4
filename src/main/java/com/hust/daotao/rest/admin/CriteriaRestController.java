package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.CriteriaDto;
import com.hust.daotao.entity.Criteria;
import com.hust.daotao.entity.KanseiWord;
import com.hust.daotao.response.CriteriaResponse;
import com.hust.daotao.response.KanseiWordResponse;
import com.hust.daotao.service.CriteriaService;
import com.hust.daotao.service.KanseiWordService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import com.hust.daotao.validator.BaseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/criterias")
public class CriteriaRestController {
	@Autowired
	private CriteriaService criteriaService;
	@Autowired
	private KanseiWordService kanseiService;
	@GetMapping("")
	public ResponseEntity<CriteriaResponse> index(@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "type_id", defaultValue = "0") Integer type,
			@RequestParam(name = "status", defaultValue = "") String statusValue,
			@RequestParam(name = "length", required = false) Integer length,
			@RequestParam(name = "start", required = false) Integer start) {

		Boolean status = null;
		if (!statusValue.equals("") && statusValue !=null) {
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
		Long count = criteriaService.countList(name, code, status, type);
		CriteriaResponse criteriaResponse = criteriaService.getList(page, name, code, status, type);
		criteriaResponse.setiTotalDisplayRecords(count);
		criteriaResponse.setiTotalRecords(count);
		return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CriteriaResponse> show(@PathVariable("id") Integer id) {
		if (id == null) {
			CriteriaResponse CriteriaResponse = new CriteriaResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CriteriaResponse>(CriteriaResponse, HttpStatus.OK);

		}
		CriteriaDto criteriaDto = criteriaService.findByIdDto(id);
		if (criteriaDto == null) {
			CriteriaResponse CriteriaResponse = new CriteriaResponse(Message.MESSAGE_ENTITY_NOT_EXIST,
					Status.STATUS_ENTITY_NOT_EXIST);
			return new ResponseEntity<CriteriaResponse>(CriteriaResponse, HttpStatus.OK);

		}
		CriteriaResponse CriteriaResponse = new CriteriaResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
				Status.STATUS_GET_ENTITY_SUCCESS, criteriaDto);
		return new ResponseEntity<CriteriaResponse>(CriteriaResponse, HttpStatus.OK);
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CriteriaResponse> save(@RequestBody CriteriaDto criteriaDto) {
		if (BaseValidator.isEmpty(criteriaDto.getName(), criteriaDto.getCode())) {
			CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
		}
		if (criteriaService.findByCode(criteriaDto.getCode()) != null) {
			CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
		}
		CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_ADD_SUCCESS,
				Status.STATUS_ADD_SUCCESS);
		criteriaResponse.setElement(criteriaService.save(criteriaDto));
		return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
	}

	@PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CriteriaResponse> update(@RequestBody CriteriaDto criteriaDto) {

		if (BaseValidator.isEmpty(criteriaDto.getName(), criteriaDto.getCode())) {
			CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
		}
		CriteriaDto resultDto = criteriaService.findByCode(criteriaDto.getCode());
		if (resultDto != null && !resultDto.getId().equals(criteriaDto.getId())) {
			CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
		}
		CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_UPDATE_SUCCESS,
				Status.STATUS_UPDATE_SUCCESS);
		criteriaResponse.setElement(criteriaService.update(criteriaDto));
		return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CriteriaResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);

		} else if (!criteriaService.delete(id)) {
			CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_DELETE_FAIL,
					Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
		}
		CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<CriteriaResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);

		}
		if (!criteriaService.changeStatus(id)) {
			CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_CHANGE_STATUS_FAIL,
					Status.STATUS_CHANGE_STATUS_FAIL);
			return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
		}
		CriteriaResponse criteriaResponse = new CriteriaResponse(Message.MESSAGE_CHANGE_STATUS_SUCCESS,
				Status.STATUS_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<CriteriaResponse>(criteriaResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}/kansei")
	public ResponseEntity<KanseiWordResponse> getKanseiWord(@PathVariable("id") Integer id) {
		Criteria criteria = criteriaService.findById(id);
		List<KanseiWord> kanseiWords = criteria.getKanseiWords();
		KanseiWordResponse res = new KanseiWordResponse();
		res.setAaData(kanseiService.convertListBoToListDto(kanseiWords));
		return new ResponseEntity<KanseiWordResponse>(res, HttpStatus.OK);
	}
	
	@PutMapping("/change-kansei-word")
	public ResponseEntity<KanseiWordResponse> changeKansei(@RequestParam("kansei_id") Integer kanseiId, @RequestParam("criteria_id") Integer criteriaId) {
		criteriaService.updateKanseiWord(criteriaId, kanseiId);
		KanseiWordResponse res = new KanseiWordResponse("Thành công", 200);
		return new ResponseEntity<KanseiWordResponse>(res, HttpStatus.OK);
	}

}
