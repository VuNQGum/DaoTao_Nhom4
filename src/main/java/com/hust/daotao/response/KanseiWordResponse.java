package com.hust.daotao.response;

import com.hust.daotao.dto.KanseiWordDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KanseiWordResponse {
	private String message;
	private Integer code;
	private Long iTotalRecords;
	private Long iTotalDisplayRecords;
	private String sEcho;
	private List<KanseiWordDto> aaData;
	private KanseiWordDto element;

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

	public KanseiWordResponse(String message, Integer code) {
		this.message = message;
		this.code = code;
	}

	public KanseiWordResponse(String message, Integer code, KanseiWordDto element) {
		super();
		this.message = message;
		this.code = code;
		this.element = element;
	}

}
