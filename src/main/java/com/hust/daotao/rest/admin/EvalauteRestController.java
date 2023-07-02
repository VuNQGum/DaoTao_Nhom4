package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.CourseEvaluateDto;
import com.hust.daotao.response.CriteriaResponse;
import com.hust.daotao.service.CourseEvaluateService;
import com.hust.daotao.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class EvalauteRestController {
	@Autowired
	private CourseEvaluateService evaluateService;

	@PostMapping("evaluate")
	public ResponseEntity<CriteriaResponse> survey(@RequestBody ArrayList<CourseEvaluateDto> evaluates) {
		if (evaluateService.validateForm(evaluates) != -1) {
			int id = evaluateService.validateForm(evaluates);
			CriteriaResponse res = new CriteriaResponse("OK", Status.STATUS_FAIL);
			res.setId(id);
			return new ResponseEntity<CriteriaResponse>(res, HttpStatus.OK);
		}
		CriteriaResponse res = new CriteriaResponse("OK", Status.STATUS_SUCCESS);
		evaluateService.saveEvaluate(evaluates);
		return new ResponseEntity<CriteriaResponse>(res, HttpStatus.OK);

	}
}
