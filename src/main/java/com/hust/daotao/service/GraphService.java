package com.hust.daotao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.daotao.dto.GraphDto;
import com.hust.daotao.entity.CourseEvaluate;
import com.hust.daotao.entity.Criteria;
import com.hust.daotao.entity.SurveyResult;

@Service
public class GraphService {
	@Autowired
	private SurveyResultService surveyService;
	@Autowired
	private CourseEvaluateService evaluateService;
	@Autowired
	private CriteriaService criteriaService;

	public GraphDto averageSurveyStatistics(Integer typeId) {
		List<SurveyResult> surveyResults = surveyService.getByCriteriaType(typeId);
		GraphDto graph = new GraphDto();
		int count = 1;
		ArrayList<String> titles = new ArrayList<String>();
		HashMap<Integer, GraphDto> map = new HashMap<Integer, GraphDto>();
		for (SurveyResult surveyResult : surveyResults) {
			if (!map.containsKey(surveyResult.getCriteria().getId())) {
				GraphDto dto = new GraphDto();
				dto.setCount(1);
				dto.setValueDouble(surveyResult.getValue());
				titles.add("Tiêu chí " + count);
				count++;
				map.put(surveyResult.getCriteria().getId(), dto);
			} else {
				GraphDto dto = map.get(surveyResult.getCriteria().getId());
				dto.setCount(dto.getCount() + 1);
				dto.setValueDouble(dto.getValueDouble() + surveyResult.getValue());
				map.replace(surveyResult.getCriteria().getId(), dto);
			}
		}
		List<Double> valuesDouble = new ArrayList<Double>();
		for (int key : map.keySet()) {
			GraphDto dto = map.get(key);
			double val = (double) Math.round(dto.getValueDouble() / dto.getCount() * 100) / 100;
			valuesDouble.add(val);
		}

		graph.setTitles(titles);
		graph.setValuesDouble(valuesDouble);
		return graph;
	}

	public GraphDto averageEvaluateStatistics(Integer typeId) {
		List<CourseEvaluate> evaluateResults = evaluateService.getByCriteriaType(typeId);
		GraphDto graph = new GraphDto();
		int count = 1;
		ArrayList<String> titles = new ArrayList<String>();
		HashMap<Integer, GraphDto> map = new HashMap<Integer, GraphDto>();
		for (CourseEvaluate evaluateResult : evaluateResults) {
			if (!map.containsKey(evaluateResult.getCriteria().getId())) {
				GraphDto dto = new GraphDto();
				dto.setCount(1);
				dto.setValueDouble(evaluateResult.getValue());
				titles.add("Tiêu chí " + count);
				count++;
				map.put(evaluateResult.getCriteria().getId(), dto);
			} else {
				GraphDto dto = map.get(evaluateResult.getCriteria().getId());
				dto.setCount(dto.getCount() + 1);
				dto.setValueDouble(dto.getValueDouble() + evaluateResult.getValue());
				map.replace(evaluateResult.getCriteria().getId(), dto);
			}
		}
		List<Double> valuesDouble = new ArrayList<Double>();
		for (int key : map.keySet()) {
			GraphDto dto = map.get(key);
			double val = (double) Math.round(dto.getValueDouble() / dto.getCount() * 100) / 100;
			valuesDouble.add(val);
		}

		graph.setTitles(titles);
		graph.setValuesDouble(valuesDouble);
		return graph;
	}

	public GraphDto avgByCourse(Integer typeId, Integer courseId) {
		List<Criteria> listCriteria = criteriaService.listCriteriaByType(typeId);
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<Double> valuesDouble = new ArrayList<Double>();
		int count = 1;
		for (Criteria criteria : listCriteria) {
			double avg = evaluateService.averageByCriteriaAndCourse(criteria.getId(), courseId);
			titles.add("Tiêu chí " + (count++));
			valuesDouble.add(avg);
		}
		GraphDto graph = new GraphDto();
		graph.setTitles(titles);
		graph.setValuesDouble(valuesDouble);
		return graph;
	}

}
