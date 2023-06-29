package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResultEvaluateDto {
	private String courseName;
	HashMap<Integer, Double> results;
}
