package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SettingDto {
	private Integer id;
	private String name;
	private Double value;
	private String description;
	private String code;
}
