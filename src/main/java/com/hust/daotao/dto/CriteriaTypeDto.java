package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaTypeDto {

	private Integer id;
	private String name;
	private String code;
	private String description;
	private Boolean status;
	private String updatedAt;
	private String deletedAt;
	private List<CriteriaDto> criteriaDtos;

}
