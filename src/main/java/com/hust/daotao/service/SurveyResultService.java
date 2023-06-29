package com.hust.daotao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.daotao.dto.SurveyResultDto;
import com.hust.daotao.dto.UserResultSurveyDto;
import com.hust.daotao.entity.Criteria;
import com.hust.daotao.entity.SurveyResult;
import com.hust.daotao.entity.User;
import com.hust.daotao.repository.SurveyResultRepository;

@Service
public class SurveyResultService {
	@Autowired
	private SurveyResultRepository surveyRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private CriteriaService criteriaService;

	public void saveSurveyResult(ArrayList<SurveyResultDto> surveyResults) {
		User user = userService.getUserCurrentLogin();
		for (int i = 0; i < surveyResults.size(); i++) {
			Criteria criteria = criteriaService.findById(surveyResults.get(i).getCriteriaId());
			SurveyResult sr = surveyRepository.getByUserIdAndCriteriaId(user.getId(), criteria.getId());
			if (sr == null)
				surveyRepository.save(new SurveyResult(user, criteria, surveyResults.get(i).getValue()));
			else {
				sr.setValue(surveyResults.get(i).getValue());
				surveyRepository.save(sr);
			}

		}

	}

	public List<SurveyResult> findByUserIdAndCriteriaType(Integer userId) {
		List<SurveyResult> results = surveyRepository.getByUserIdAndCriteriaType(userId);
		return results;
	}

	public Map<Integer, Double> getListSurveyOfUser(Integer userId) {
		List<SurveyResult> results = surveyRepository.getByUserIdAndCriteriaType(userId);
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		if (results != null && results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				map.put(results.get(i).getCriteria().getId(), results.get(i).getValue());
			}
		}
		return map;
	}

	public Integer validateForm(ArrayList<SurveyResultDto> surveyResults) {
		List<Integer> listCriteriaId = criteriaService.listIdCriteria();
		List<Integer> listIdResult = new ArrayList<Integer>();
		for (SurveyResultDto s : surveyResults) {
			listIdResult.add(s.getCriteriaId());
		}
		for (int i = 0; i < listCriteriaId.size(); i++) {
			if (listIdResult.indexOf(listCriteriaId.get(i)) < 0)
				return i;
		}
		return -1;
	}

	public List<SurveyResult> getByCriteriaType(Integer typeId) {
		return surveyRepository.getByCriteriaType(typeId);
	}

	public HashMap<Integer, UserResultSurveyDto> getByCriteriaTypeOrderByUser(Integer typeId) {
		List<SurveyResult> results = surveyRepository.getByCriteriaTypeOrderByUser(typeId);
		HashMap<Integer, UserResultSurveyDto> map = new HashMap<Integer, UserResultSurveyDto>();
		for (SurveyResult surveyResult : results) {
			if (!map.containsKey(surveyResult.getUser().getId())) {
				UserResultSurveyDto surveyDto = new UserResultSurveyDto();
				HashMap<Integer, Double> values = new HashMap<Integer, Double>();
				surveyDto.setFullName(surveyResult.getUser().getFullName());
				values.put(surveyResult.getCriteria().getId(), surveyResult.getValue());
				surveyDto.setResults(values);
				map.put(surveyResult.getUser().getId(), surveyDto);
			} else {
				UserResultSurveyDto surveyDto = map.get(surveyResult.getUser().getId());
				HashMap<Integer, Double> values = surveyDto.getResults();
				values.put(surveyResult.getCriteria().getId(), surveyResult.getValue());
				surveyDto.setResults(values);
				map.replace(surveyResult.getUser().getId(), surveyDto);
			}
		}
		
		return map;
	}

}
