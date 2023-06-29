package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
	private Integer id;
	private UserDto userDto;
	private ProfileDto profileDto;
	private Integer userId;
	private Integer profileId;
	private String value;
}
