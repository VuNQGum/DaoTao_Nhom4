package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Integer id;
	private String fullName;
	private String email;
	private String password;
	private String phone;
	private String image;
	private String birthday;
	private Boolean status;
	private String description;
	private Date updatedAt;
	private Date deletedAt;
	private RoleDto roleDto;
	private String roleName;
	private Integer roleId;
	private String studentCode;
	private Integer subjectId;
	private CategoryDto subjectDto;
	private List<UserProfileDto> userProfileDtos;
	private Integer resultTest;


	public UserDto(String fullName, Integer subjectId, String studentCode, String email, String password, String phone,
			String image, String birthday, Boolean status, Integer roleId, String description) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.image = image;
		this.birthday = birthday;
		this.status = status;
		this.roleId = roleId;
		this.description = description;
		this.subjectId = subjectId;
		this.studentCode = studentCode;
	}

}
