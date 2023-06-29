package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonOfCourseDto implements Comparable<LessonOfCourseDto> {

	private Integer id;
	private String name;
	private String code;
	private String description;
	private String url;
	private Boolean status;
	private String updatedAt;
	private String deletedAt;
	private CourseDto courseOfLessonDto;
	private Integer courseOfLessonId;

	public LessonOfCourseDto(String name, String code, String description, String url, Boolean status,
			Integer courseId) {
		super();
		this.name = name;
		this.code = code;
		this.description = description;
		this.url = url;
		this.status = status;
		this.courseOfLessonId = courseId;
	}

	public LessonOfCourseDto(Integer id, String name, String code, String description, String url, Boolean status,
			Integer courseId) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.url = url;
		this.status = status;
		this.courseOfLessonId = courseId;
	}

	@Override
	public int compareTo(LessonOfCourseDto dto) {
		if (id == dto.getId())
			return 0;
		else if (id > dto.getId())
			return 1;
		else
			return -1;
	}

}
