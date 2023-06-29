package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResultSurveyDto {
	private String fullName;
	HashMap<Integer, Double> results;
}
