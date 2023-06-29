package com.hust.daotao.rest;

import com.hust.daotao.entity.CriteriaType;
import com.hust.daotao.service.CriteriaTypeService;
import com.hust.daotao.service.WriteDataToCSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class RestControllerApi {
	@Autowired
	private CriteriaTypeService criteriaTypeService;
	@Autowired
	private WriteDataToCSVService csvService;

	@GetMapping("/set-session")
	public void setSession(HttpSession session) {
		if (session.getAttribute("criteriaType") == null) {
			CriteriaType criteriaType = criteriaTypeService.getCriteriaTypeUse();
			if (criteriaType != null) {
				session.setAttribute("criteriaType", criteriaType);
			}
		}
	}

	@GetMapping("/csv/report-result-survey.csv")
	public void downloadCSVReportResultSurvey(HttpServletResponse response) throws IOException {
		csvService.writeObjectToCSVResultSurvey("report-result-survey.csv", response);

	}

	@GetMapping("/csv/report-result-evaluate.csv")
	public void downloadCSVReportCourseEvaluate(HttpServletResponse response) throws IOException {
		csvService.writeObjectToCSVResultEvaluate("report-course-evaluate.csv", response);

	}
	
	@GetMapping("/csv/report-student.csv")
	public void downloadCSVReportStudent(HttpServletResponse response) throws IOException {
		csvService.writeObjectToCSVStudent("report-student.csv", response);
	}
	
	@GetMapping("/csv/report-course.csv")
	public void downloadCSVReportCourse(HttpServletResponse response) throws IOException {
		csvService.writeObjectToCSVCourse("report-course.csv", response);
	}
}
