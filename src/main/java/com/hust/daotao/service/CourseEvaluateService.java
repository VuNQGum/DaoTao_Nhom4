package com.hust.thesis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.thesis.dto.CourseEvaluateDto;
import com.hust.thesis.dto.CourseResultEvaluateDto;
import com.hust.thesis.entity.Course;
import com.hust.thesis.entity.CourseEvaluate;
import com.hust.thesis.entity.Criteria;
import com.hust.thesis.repository.CourseEvaluateRepository;

@Service
public class CourseEvaluateService {
	@Autowired
	private CourseEvaluateRepository evaluateRepository;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CriteriaService criteriaService;

	public void saveEvaluate(ArrayList<CourseEvaluateDto> evaluateDtos) {
		Course course = courseService.findById(evaluateDtos.get(0).getCourseId());
		for (int i = 0; i < evaluateDtos.size(); i++) {
			Criteria criteria = criteriaService.findById(evaluateDtos.get(i).getCriteriaId());
			CourseEvaluate sr = evaluateRepository.findByCourseIdAndCriteriaId(evaluateDtos.get(0).getCourseId(),
					criteria.getId());
			if (sr == null)
				evaluateRepository.save(new CourseEvaluate(course, criteria, evaluateDtos.get(i).getValue()));
			else {
				sr.setValue(evaluateDtos.get(i).getValue());
				evaluateRepository.save(sr);
			}

		}
		double result = resultEvaluate(course);
		course.setResultEvaluate(result);
		courseService.save(course);

	}

	public double resultEvaluate(Course course) {
		double result = 0.0;
		List<CourseEvaluate> listEvaluate = evaluateRepository.findByCourseIdAndCriteriaType(course.getId());
		double total = 0.0;
		if (listEvaluate != null && listEvaluate.size() > 0) {
			for (CourseEvaluate courseEvaluate : listEvaluate) {
				total = total + courseEvaluate.getValue();
			}
			result = total / listEvaluate.size();
		}

		return result;
	}

	public Integer validateForm(ArrayList<CourseEvaluateDto> evaluateDtos) {
		List<Integer> listCriteriaId = criteriaService.listIdCriteria();
		List<Integer> listIdResult = new ArrayList<Integer>();
		for (CourseEvaluateDto s : evaluateDtos) {
			listIdResult.add(s.getCriteriaId());
		}
		for (int i = 0; i < listCriteriaId.size(); i++) {
			if (listIdResult.indexOf(listCriteriaId.get(i)) < 0)
				return i;
		}
		return -1;
	}

	public Map<Integer, Double> getListEvaluateOfCourse(Integer courseId) {
		List<CourseEvaluate> results = evaluateRepository.findByCourseIdAndCriteriaType(courseId);
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		for (int i = 0; i < results.size(); i++) {
			map.put(results.get(i).getCriteria().getId(), results.get(i).getValue());
		}
		return map;
	}

	public Double getAverage(Integer courseId, Integer criteriaType) {
		return evaluateRepository.getAverage(courseId, criteriaType);
	}

	public List<CourseEvaluate> getByCriteriaType(Integer typeId) {
		return evaluateRepository.getByCriteriaType(typeId);
	}

	public Double averageByCriteriaTypeAndCourse(Integer typeId, Integer courseId) {
		Double value = evaluateRepository.avgByCriteriaTypeAndCourse(typeId, courseId);
		if (value != null)
			return (double) Math.round(value * 100) / 100;
		else
			return 0.0d;
	}

	public Double averageByCriteriaAndCourse(Integer criteriaId, Integer courseId) {
		Double value = evaluateRepository.avgByCourseAndCriteria(criteriaId, courseId);
		if (value != null)
			return (double) Math.round(value * 100) / 100;
		else
			return 0.0d;
	}

//	public List<CourseEvaluate> getByCriteriaTypeOrderByCourse(Integer typeId) {
//		return evaluateRepository.getByCriteriaTypeOrderByCourse(typeId);
//	}

	public HashMap<Integer, CourseResultEvaluateDto> getByCriteriaTypeOrderByCourse(Integer typeId) {
		List<CourseEvaluate> results = evaluateRepository.getByCriteriaTypeOrderByCourse(typeId);
		HashMap<Integer, CourseResultEvaluateDto> map = new HashMap<Integer, CourseResultEvaluateDto>();
		for (CourseEvaluate evaluate : results) {
			if (!map.containsKey(evaluate.getCourse().getId())) {
				CourseResultEvaluateDto evaluateCourse = new CourseResultEvaluateDto();
				HashMap<Integer, Double> values = new HashMap<Integer, Double>();
				evaluateCourse.setCourseName(evaluate.getCourse().getName());
				values.put(evaluate.getCriteria().getId(), evaluate.getValue());
				evaluateCourse.setResults(values);
				map.put(evaluate.getCourse().getId(), evaluateCourse);
			} else {
				CourseResultEvaluateDto evaluateCourse = map.get(evaluate.getCourse().getId());
				HashMap<Integer, Double> values = evaluateCourse.getResults();
				values.put(evaluate.getCriteria().getId(), evaluate.getValue());
				evaluateCourse.setResults(values);
				map.replace(evaluate.getCourse().getId(), evaluateCourse);
			}
		}

		return map;
	}
}
