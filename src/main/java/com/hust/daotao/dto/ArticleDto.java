package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
	private Integer id;
	private String title;
	private String description;
	private String content;
	private Integer views;
	private Date updatedAt;
	private Date deletedAt;
	private List<Integer> tagIds;
	private List<TagDto> tags;
	private UserDto user;
}
