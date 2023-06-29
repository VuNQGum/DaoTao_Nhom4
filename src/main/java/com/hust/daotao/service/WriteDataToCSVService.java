package com.hust.daotao.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.daotao.dto.CourseResultEvaluateDto;
import com.hust.daotao.dto.UserResultSurveyDto;
import com.hust.daotao.entity.Category;
import com.hust.daotao.entity.Course;
import com.hust.daotao.entity.Criteria;
import com.hust.daotao.entity.CriteriaType;
import com.hust.daotao.entity.User;

@Service
public class WriteDataToCSVService {
	@Autowired
	private SurveyResultService surveyService;
	@Autowired
	private CourseEvaluateService evaluateService;
	@Autowired
	private CriteriaService criteriaService;
	private Workbook wb;
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CriteriaTypeService criteriaTypeService;

	public void writeObjectToCSVResultSurvey(String fileName, HttpServletResponse response) {
		List<CriteriaType> criteriaTypes = criteriaTypeService.getCriterias();
		try {
			wb = new HSSFWorkbook();
			// Creating a sheet using predefined class provided by Apache POI
			for (CriteriaType criteriaType : criteriaTypes) {
				Sheet sheet = wb.createSheet(criteriaType.getName());
				Integer rowNum = 1;
				Integer cellNum = 0;
				Row rowHeader = sheet.createRow(0);
				Cell cellHeader = rowHeader.createCell(0);
				cellHeader.setCellValue("Họ và tên sinh viên");
				List<Criteria> criterias = criteriaService.listCriteriaByType(criteriaType.getId());
				for (int i = 0; i < criterias.size(); i++) {
					cellHeader = rowHeader.createCell(cellNum + 1);
					cellHeader.setCellValue("Tiêu chí " + (cellNum + 1));
					cellNum++;
				}
				HashMap<Integer, UserResultSurveyDto> surveys = surveyService
						.getByCriteriaTypeOrderByUser(criteriaType.getId());
				for (int key : surveys.keySet()) {
					cellNum = 1;
					Row row = sheet.createRow(rowNum);
					Cell cell = row.createCell(0);
					cell.setCellValue(surveys.get(key).getFullName());
					HashMap<Integer, Double> values = surveys.get(key).getResults();
					for (int k : values.keySet()) {
						cell = row.createCell(cellNum);
						cell.setCellValue(values.get(k));
						cellNum++;
					}
					rowNum++;
				}

			}
			response.setHeader("Content-Encoding", "UTF-8");
			response.setContentType("text/csv; charset=UTF-8");
			response.setHeader("content-disposition", "attachment; filename=" + fileName);
			OutputStream outObject = response.getOutputStream();
			wb.write(outObject);
			outObject.flush();
			outObject.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeObjectToCSVResultEvaluate(String fileName, HttpServletResponse response) {
		List<CriteriaType> criteriaTypes = criteriaTypeService.getCriterias();

		try {
			wb = new HSSFWorkbook();
			// Creating a sheet using predefined class provided by Apache POI
			for (CriteriaType criteriaType : criteriaTypes) {
				Sheet sheet = wb.createSheet(criteriaType.getName());
				Integer rowNum = 1;
				Integer cellNum = 0;
				Row rowHeader = sheet.createRow(0);
				Cell cellHeader = rowHeader.createCell(0);
				cellHeader.setCellValue("Tên khóa học");
				List<Criteria> criterias = criteriaService.listCriteriaByType(criteriaType.getId());
				for (int i = 0; i < criterias.size(); i++) {
					cellHeader = rowHeader.createCell(cellNum + 1);
					cellHeader.setCellValue("Tiêu chí " + (cellNum + 1));
					cellNum++;
				}
				HashMap<Integer, CourseResultEvaluateDto> evaluates = evaluateService
						.getByCriteriaTypeOrderByCourse(criteriaType.getId());
				for (int key : evaluates.keySet()) {
					cellNum = 1;
					Row row = sheet.createRow(rowNum);
					Cell cell = row.createCell(0);
					cell.setCellValue(evaluates.get(key).getCourseName());
					HashMap<Integer, Double> values = evaluates.get(key).getResults();
					for (int k : values.keySet()) {
						cell = row.createCell(cellNum);
						cell.setCellValue(values.get(k));
						cellNum++;
					}
					rowNum++;
				}
			}
			response.setHeader("Content-Encoding", "UTF-8");
			response.setContentType("text/csv; charset=UTF-8");
			response.setHeader("content-disposition", "attachment; filename=" + fileName);
			OutputStream outObject = response.getOutputStream();
			wb.write(outObject);
			outObject.flush();
			outObject.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeObjectToCSVStudent(String fileName, HttpServletResponse response) {

		List<User> students = userService.getListStudent();
		try {
			wb = new HSSFWorkbook();
			Sheet sheet = wb.createSheet("Danh sách sinh viên");
			Integer rowNum = 1;
			Row rowHeader = sheet.createRow(0);
			Cell cellHeader0 = rowHeader.createCell(0);
			Cell cellHeader1 = rowHeader.createCell(1);
			Cell cellHeader2 = rowHeader.createCell(2);
			Cell cellHeader3 = rowHeader.createCell(3);
			cellHeader0.setCellValue("Họ và tên");
			cellHeader1.setCellValue("Bộ môn");
			cellHeader2.setCellValue("Email");
			cellHeader3.setCellValue("Đánh giá năng lực");
			for (int i = 0; i < students.size(); i++) {
				Row row = sheet.createRow(rowNum);
				Cell cell = row.createCell(0);
				Cell cell1 = row.createCell(1);
				Cell cell2 = row.createCell(2);
				Cell cell3 = row.createCell(3);
				cell.setCellValue(students.get(i).getFullName());
				cell1.setCellValue(students.get(i).getSubject().getName());
				cell2.setCellValue(students.get(i).getEmail());
				cell3.setCellValue(userService.formatResultTest(students.get(i).getResultTest()));
				rowNum++;
			}
			response.setHeader("Content-Encoding", "UTF-8");
			response.setContentType("text/csv; charset=UTF-8");
			response.setHeader("content-disposition", "attachment; filename=" + fileName);
			OutputStream outObject = response.getOutputStream();
			wb.write(outObject);
			outObject.flush();
			outObject.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeObjectToCSVCourse(String fileName, HttpServletResponse response) {
		List<Category> categories = categoryService.getListCategory();
		try {
			wb = new HSSFWorkbook();
			for (Category category : categories) {
				Sheet sheet = wb.createSheet(category.getName());
				Integer rowNum = 1;
				Row rowHeader = sheet.createRow(0);
				Cell cellHeader0 = rowHeader.createCell(0);
				Cell cellHeader1 = rowHeader.createCell(1);
				Cell cellHeader2 = rowHeader.createCell(2);
				Cell cellHeader3 = rowHeader.createCell(3);
				cellHeader0.setCellValue("Tên khóa học");
				cellHeader1.setCellValue("Mã khóa học");
				cellHeader2.setCellValue("Số bài giảng");
				cellHeader3.setCellValue("Số sinh viên tham gia");
				List<Course> courses = courseService.getListCourseByCategory(category.getId());
				for (int i = 0; i < courses.size(); i++) {
					Row row = sheet.createRow(rowNum);
					Cell cell = row.createCell(0);
					Cell cell1 = row.createCell(1);
					Cell cell2 = row.createCell(2);
					Cell cell3 = row.createCell(3);
					cell.setCellValue(courses.get(i).getName());
					cell1.setCellValue(courses.get(i).getCode());
					cell2.setCellValue(courses.get(i).getLessonOfCourses().size());
					cell3.setCellValue(courses.get(i).getStudentOfCourses().size());
					rowNum++;
				}

			}

			response.setHeader("Content-Encoding", "UTF-8");
			response.setContentType("text/csv; charset=UTF-8");
			response.setHeader("content-disposition", "attachment; filename=" + fileName);
			OutputStream outObject = response.getOutputStream();
			wb.write(outObject);
			outObject.flush();
			outObject.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
