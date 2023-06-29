package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseProfileDto {
	private Integer id;
	private CourseDto courseDto;
	private ProfileDto profileDto;
	private Integer courseId;
	private Integer profileId;
	private String value;
}
