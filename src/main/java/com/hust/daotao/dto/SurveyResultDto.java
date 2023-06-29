package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SurveyResultDto {
	private Integer userId;
	private Integer criteriaId;
	private Double value;
	private Date deletedAt;
	private Date updatedAt;

}
