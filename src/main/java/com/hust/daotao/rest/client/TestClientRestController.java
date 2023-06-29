package com.hust.daotao.rest.client;

import com.hust.daotao.dto.CapacityTestDto;
import com.hust.daotao.response.CapacityTestResponse;
import com.hust.daotao.service.CapacityTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestClientRestController {
	@Autowired
	private CapacityTestService testService;
	@PostMapping("/api/client/test")
	public ResponseEntity<CapacityTestResponse> test(@RequestBody List<CapacityTestDto> answersOfUser) {
		int result = testService.resultTest(answersOfUser);
		CapacityTestResponse res = new CapacityTestResponse(200, "OK", result);
		return new ResponseEntity<CapacityTestResponse>(res, HttpStatus.OK);
	}
}
