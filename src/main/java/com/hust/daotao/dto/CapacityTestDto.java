package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacityTestDto {
	private Integer id;
	private String question;
	private String answerA;
	private String answerB;
	private String answerC;
	private String answerD;
	private String answerTrue;
	private Boolean status;
	private Date updatedAt;
	private Date deletedAt;
	private String answerOfUser;
}
