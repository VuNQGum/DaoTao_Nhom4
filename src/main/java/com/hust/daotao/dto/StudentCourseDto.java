package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseDto {
	private Integer id;
	private CourseDto courseDto;
	private UserDto studentDto;
	private Integer courseOfStudentId;
	private Integer studentId;
	private Boolean status;
	public StudentCourseDto(Integer courseOfStudentId, Integer studentId, Boolean status) {
		super();
		this.courseOfStudentId = courseOfStudentId;
		this.studentId = studentId;
		this.status = status;
	}
	
}
