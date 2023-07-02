package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.CapacityTestDto;
import com.hust.daotao.entity.CapacityTest;
import com.hust.daotao.response.CapacityTestResponse;
import com.hust.daotao.service.CapacityTestService;
import com.hust.daotao.util.Constants;
import com.hust.daotao.util.Message;
import com.hust.daotao.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/test")

public class TestRestController {
	@Autowired
	private CapacityTestService testService;

	@GetMapping("")
	public ResponseEntity<CapacityTestResponse> index(
			@RequestParam(name = "question", defaultValue = "") String question,
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
		Long count = testService.countList(question, status);
		CapacityTestResponse capacityTestResponse = testService.getList(page, question, status);
		capacityTestResponse.setiTotalDisplayRecords(count);
		capacityTestResponse.setiTotalRecords(count);
		return new ResponseEntity<CapacityTestResponse>(capacityTestResponse, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<CapacityTestResponse> save(@RequestBody CapacityTestDto dto) {
		if (dto.getQuestion().isEmpty() || dto.getAnswerA().isEmpty() || dto.getAnswerB().isEmpty()
				|| dto.getAnswerC().isEmpty() || dto.getAnswerD().isEmpty() || dto.getAnswerTrue().isEmpty()) {
			CapacityTestResponse capacityTestResponse = new CapacityTestResponse(Status.STATUS_REQUEST_ERROR,
					Message.MESSAGE_REQUEST_EMPTY);
			return new ResponseEntity<CapacityTestResponse>(capacityTestResponse, HttpStatus.OK);
		}
		CapacityTest result = testService.save(dto);
		if (result == null) {
			CapacityTestResponse capacityTestResponse = new CapacityTestResponse(Status.STATUS_REQUEST_ERROR,
					"Không thể thêm mới");
			return new ResponseEntity<CapacityTestResponse>(capacityTestResponse, HttpStatus.OK);
		}

		CapacityTestResponse capacityTestResponse = new CapacityTestResponse(Status.STATUS_ADD_SUCCESS,
				Message.MESSAGE_ADD_SUCCESS);
		return new ResponseEntity<CapacityTestResponse>(capacityTestResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")

	public ResponseEntity<CapacityTestResponse> delete(@PathVariable("id") Integer id) {
		if (id == null) {
			CapacityTestResponse res = new CapacityTestResponse(Status.STATUS_NULL, Message.MESSAGE_NULL);
			return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);

		} else if (testService.delete(id) == null) {
			CapacityTestResponse res = new CapacityTestResponse(Status.STATUS_DELETE_FAIL, Message.MESSAGE_DELETE_FAIL);
			return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);
		}
		CapacityTestResponse res = new CapacityTestResponse(Status.STATUS_DELETE_SUCCESS,
				Message.MESSAGE_DELETE_SUCCESS);
		return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);
	}

	@PutMapping("/{id}/change-status")
	public ResponseEntity<CapacityTestResponse> changeStatus(@PathVariable("id") Integer id) {
		if (id == null) {
			CapacityTestResponse res = new CapacityTestResponse(Status.STATUS_NULL, Message.MESSAGE_NULL);
			return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);

		} else if (testService.changeStatus(id) == null) {
			CapacityTestResponse res = new CapacityTestResponse(Status.STATUS_CHANGE_STATUS_FAIL,
					Message.MESSAGE_CHANGE_STATUS_FAIL);
			return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);
		}
		CapacityTestResponse res = new CapacityTestResponse(Status.STATUS_CHANGE_STATUS_SUCCESS,
				Message.MESSAGE_CHANGE_STATUS_SUCCESS);
		return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CapacityTestResponse> show(@PathVariable("id") Integer id) {
		if (id == null) {
			CapacityTestResponse res = new CapacityTestResponse(Status.STATUS_NULL, Message.MESSAGE_NULL);
			return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);

		}
		CapacityTestDto dto = testService.findByIdDto(id);
		if (dto == null) {
			CapacityTestResponse res = new CapacityTestResponse(Status.STATUS_ENTITY_NOT_EXIST,
					Message.MESSAGE_ENTITY_NOT_EXIST);
			return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);

		}
		CapacityTestResponse res = new CapacityTestResponse(Status.STATUS_GET_ENTITY_SUCCESS,
				Message.MESSAGE_GET_ENTITY_SUCCESS, dto);
		return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);
	}

	@PutMapping("")
	public ResponseEntity<CapacityTestResponse> update(@RequestBody CapacityTestDto dto) {
		if (dto.getQuestion().isEmpty() || dto.getAnswerA().isEmpty() || dto.getAnswerB().isEmpty()
				|| dto.getAnswerC().isEmpty() || dto.getAnswerD().isEmpty() || dto.getAnswerTrue().isEmpty()) {
			CapacityTestResponse capacityTestResponse = new CapacityTestResponse(Status.STATUS_REQUEST_ERROR,
					Message.MESSAGE_REQUEST_EMPTY);
			return new ResponseEntity<CapacityTestResponse>(capacityTestResponse, HttpStatus.OK);
		}
		CapacityTest result = testService.update(dto);
		if (result == null) {
			CapacityTestResponse capacityTestResponse = new CapacityTestResponse(Status.STATUS_REQUEST_ERROR,
					"Không thể cập nhật");
			return new ResponseEntity<CapacityTestResponse>(capacityTestResponse, HttpStatus.OK);
		}

		CapacityTestResponse capacityTestResponse = new CapacityTestResponse(Status.STATUS_UPDATE_SUCCESS,
				Message.MESSAGE_ADD_SUCCESS);
		return new ResponseEntity<CapacityTestResponse>(capacityTestResponse, HttpStatus.OK);
	}

}
