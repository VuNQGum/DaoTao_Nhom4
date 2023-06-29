package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CategoryDto {
	private Integer id;
	private String name;
	private String code;
	private String description;
	private String image;
	private Boolean status;
	private Date updatedAt;
	private Date deletedAt;
	private Integer quantityCourse;

	public CategoryDto(String name, String code, String description, String image, Boolean status) {
		super();
		this.name = name;
		this.code = code;
		this.description = description;
		this.image = image;
		this.status = status;
		this.quantityCourse = 0;
	}

}
