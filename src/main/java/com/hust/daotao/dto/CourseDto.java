package com.hust.daotao.dto;

import com.hust.daotao.entity.Category;
import com.hust.daotao.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
	private Integer id;
	private String name;
	private String code;
	private Boolean status;
	private String description;
	private String image;
	private Date updatedAt;
	private Date deletedAt;
	private UserDto teacherDto;
	private CategoryDto categoryDto;
	private Category category;
	private User teacher;
	private Integer userId;
	private Integer categoryId;
	private List<CourseProfileDto> courseProfileDtos;
	private List<LessonOfCourseDto> lessonOfCourseDtos;
	private Integer quantityLesson;
	private Integer quantityStudent;
	private Double resultEvaluate;
	private String link;

	public CourseDto(String name, String code, Boolean status, String description, String image, Integer userId,
			Integer categoryId, double resultEvaluate, String link) {
		super();
		this.name = name;
		this.code = code;
		this.status = status;
		this.description = description;
		this.image = image;
		this.userId = userId;
		this.categoryId = categoryId;
		this.quantityLesson = 0;
		this.quantityStudent = 0;
		this.resultEvaluate = resultEvaluate;
		this.link = link;
	}

}
