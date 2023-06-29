package com.hust.daotao.rest.client;

import com.hust.daotao.dto.SurveyResultDto;
import com.hust.daotao.response.CriteriaResponse;
import com.hust.daotao.service.SurveyResultService;
import com.hust.daotao.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class CriteriaClientRestController {
	@Autowired
	private SurveyResultService surveyResultService;

	@PostMapping("survey")
	public ResponseEntity<CriteriaResponse> survey(@RequestBody ArrayList<SurveyResultDto> surveyResults) {
		if(surveyResultService.validateForm(surveyResults) !=-1) {
			int id = surveyResultService.validateForm(surveyResults);
			CriteriaResponse res = new CriteriaResponse("OK", Status.STATUS_FAIL);
			res.setId(id);
			return new ResponseEntity<CriteriaResponse>(res, HttpStatus.OK);
		}
		CriteriaResponse res = new CriteriaResponse("OK", Status.STATUS_SUCCESS);
		surveyResultService.saveSurveyResult(surveyResults);
		return new ResponseEntity<CriteriaResponse>(res, HttpStatus.OK);

	}
}
