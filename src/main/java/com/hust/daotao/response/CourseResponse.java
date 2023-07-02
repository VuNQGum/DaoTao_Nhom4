package com.hust.daotao.response;

import com.hust.daotao.dto.CourseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
	private String message;
	private Integer code;
	private Long iTotalRecords;
	private Long iTotalDisplayRecords;
	private String sEcho;
	private List<CourseDto> aaData;
	private CourseDto element;
	private Long iTotalPage;
	public Long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(Long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public Long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(Long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public CourseResponse(String message, Integer code) {
		this.message = message;
		this.code = code;
	}

	public CourseResponse(String message, Integer code, CourseDto element) {
		super();
		this.message = message;
		this.code = code;
		this.element = element;
	}

	public CourseResponse(Long iTotalRecords, Long iTotalPage, List<CourseDto> aaData) {
		super();
		this.iTotalRecords = iTotalRecords;
		this.aaData = aaData;
		this.iTotalPage = iTotalPage;
	}


}
