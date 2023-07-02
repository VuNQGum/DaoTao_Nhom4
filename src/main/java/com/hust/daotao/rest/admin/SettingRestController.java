package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.SettingDto;
import com.hust.daotao.entity.Setting;
import com.hust.daotao.response.SettingResponse;
import com.hust.daotao.service.SettingService;
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
@RequestMapping("/api/admin/settings")
public class SettingRestController {
	@Autowired
	private SettingService settingService;

	@GetMapping("")
	public ResponseEntity<SettingResponse> index(@RequestParam(name = "code", defaultValue = "") String code,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "length", required = false) Integer length,
			@RequestParam(name = "start", required = false) Integer start) {

		Integer pageDisplayLength = 10;
		if (length != null)
			pageDisplayLength = length;
		Constants.pageSize = pageDisplayLength;
		Integer iDisplayStart = 1;
		if (start != null)
			iDisplayStart = start;
		Integer page = (iDisplayStart / pageDisplayLength);
		Long count = settingService.countList(name, code);
		SettingResponse settingResponse = settingService.getList(page, name, code);
		settingResponse.setiTotalDisplayRecords(count);
		settingResponse.setiTotalRecords(count);
		return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SettingResponse> show(@PathVariable("id") Integer id) {
		if (id == null) {
			SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);

		}
		SettingDto dto = settingService.findByIdDto(id);
		if (dto == null) {
			SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_ENTITY_NOT_EXIST,
					Status.STATUS_ENTITY_NOT_EXIST);
			return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);

		}
		SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_GET_ENTITY_SUCCESS,
				Status.STATUS_GET_ENTITY_SUCCESS, dto);
		return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SettingResponse> save(@RequestBody SettingDto settingDto) {
		if (BaseValidator.isEmpty(settingDto.getName(), settingDto.getCode(), settingDto.getValue())) {
			SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
		}
		if (settingService.findByCode(settingDto.getCode()) != null) {
			SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
		}
		SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_ADD_SUCCESS, Status.STATUS_ADD_SUCCESS);
		settingResponse.setElement(settingService.save(settingDto));
		return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
	}

	@PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SettingResponse> update(@RequestBody SettingDto dto) {
		
		if (BaseValidator.isEmpty(dto.getName(), dto.getCode(),dto.getValue())) {
			SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_REQUEST_EMPTY,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
		}
		Setting result = settingService.findByCode(dto.getCode());
		if (result != null && !dto.getId().equals(result.getId())) {
			SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_EXIST_CODE,
					Status.STATUS_REQUEST_ERROR);
			return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
		}
		SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_UPDATE_SUCCESS, Status.STATUS_UPDATE_SUCCESS);
		settingResponse.setElement(settingService.update(dto));
		return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SettingResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_NULL, Status.STATUS_NULL);
			return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);

		} else if (!settingService.delete(id)) {
			SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_DELETE_FAIL,
					Status.STATUS_DELETE_FAIL);
			return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
		}
		SettingResponse settingResponse = new SettingResponse(Message.MESSAGE_DELETE_SUCCESS,
				Status.STATUS_DELETE_SUCCESS);
		return new ResponseEntity<SettingResponse>(settingResponse, HttpStatus.OK);
	}
}