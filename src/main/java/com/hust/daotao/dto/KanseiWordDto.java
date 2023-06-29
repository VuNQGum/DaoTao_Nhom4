package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KanseiWordDto {
	private Integer id;
	private String positiveWord;
	private String negativeWord;
	private Boolean status;
	private String updatedAt;
	private String deletedAt;
	private Integer criteriaId;
}
