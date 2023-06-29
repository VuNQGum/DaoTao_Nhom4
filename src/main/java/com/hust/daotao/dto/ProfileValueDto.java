package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileValueDto {
	private Integer id;
	private String name;
	private String nameDisplayStudentSide;
	private String nameDisplayTeacherSide;
	private String code;
	private Integer value;
	private Boolean status;
	private String updatedAt;
	private String deletedAt;
	private ProfileDto profileDto;
	private Integer profileId;
}
