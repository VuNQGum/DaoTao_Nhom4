package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thymeleaf.context.Context;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
	private String emailTo;
	private String subject;
	private String templateName;
	private  Context context;
}
