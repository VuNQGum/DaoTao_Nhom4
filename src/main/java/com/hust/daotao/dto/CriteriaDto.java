package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaDto {
	
	private Integer id;
	private String name;
	private String code;
	private String description;
	private Boolean status;
	private String updatedAt;
	private String deletedAt;
	private Integer typeId;
	private Double weight;
}
