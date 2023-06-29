package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

	private Integer id;
	private String name;
	private String code;
	private String description;
	private Boolean status;
	private String updatedAt;
	private String deletedAt;
	private String nameDisplayTeacherSide;
	private String nameDisplayStudentSide;
	private Boolean isMultiple;
	private Double weight;
	private List<ProfileValueDto> profileValueDtos;
	private Integer show;
}
