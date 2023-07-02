package com.hust.daotao.rest.admin;

import com.hust.daotao.dto.GraphDto;
import com.hust.daotao.service.CourseService;
import com.hust.daotao.service.GraphService;
import com.hust.daotao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class StatisticRestController {
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private GraphService graphService;

	@GetMapping("/api/admin/statistic")
	public ResponseEntity<HashMap<String, GraphDto>> getData() {
		GraphDto countCapacityStudent = userService.countStudentByCapacity();
		GraphDto countCourseByQuantity = courseService.countCourseByCategory();
		HashMap<String, GraphDto> map = new HashMap<String, GraphDto>();
		map.put("capactity", countCapacityStudent);
		map.put("course", countCourseByQuantity);
		map.put("survey_ict", graphService.averageSurveyStatistics(1));
		map.put("survey_hust", graphService.averageSurveyStatistics(2));
		map.put("evaluate_ict", graphService.averageEvaluateStatistics(1));
		map.put("evaluate_hust", graphService.averageEvaluateStatistics(2));

		return new ResponseEntity<HashMap<String, GraphDto>>(map, HttpStatus.OK);
	}

	@GetMapping("/api/admin/statistic/{course_id}")
	public ResponseEntity<HashMap<String, GraphDto>> getData(@PathVariable("course_id") Integer courseId) {

		HashMap<String, GraphDto> map = new HashMap<String, GraphDto>();
		map.put("evaluate_ict", graphService.avgByCourse(1, courseId));
		map.put("evaluate_hust", graphService.avgByCourse(2, courseId));

		return new ResponseEntity<HashMap<String, GraphDto>>(map, HttpStatus.OK);
	}
}
