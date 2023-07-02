package com.hust.daotao.response;

import com.hust.daotao.dto.CapacityTestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacityTestResponse {
	private String message;
	private Integer code;
	private Long iTotalRecords;
	private Long iTotalDisplayRecords;
	private String sEcho;
	private List<CapacityTestDto> aaData;
	private CapacityTestDto element;
	private Long iTotalPage;
	private int resultTest;
	public CapacityTestResponse(Integer code, String message, CapacityTestDto element) {
		super();
		this.message = message;
		this.code = code;
		this.element = element;
	}
	public CapacityTestResponse(Long iTotalRecords, Long iTotalPage, List<CapacityTestDto> aaData) {
		super();
		this.iTotalRecords = iTotalRecords;
		this.aaData = aaData;
		this.iTotalPage = iTotalPage;
	}
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
	public CapacityTestResponse(Integer code, String message) {
		super();
		this.message = message;
		this.code = code;
	}
	public CapacityTestResponse(Integer code, String message, int result) {
		super();
		this.message = message;
		this.code = code;
		this.resultTest = result;
	}

}
