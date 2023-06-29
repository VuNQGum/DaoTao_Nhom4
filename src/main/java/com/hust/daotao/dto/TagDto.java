package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TagDto {
	private Integer id;
	private String name;
	private Date updatedAt;
	private Date deletedAt;
}
