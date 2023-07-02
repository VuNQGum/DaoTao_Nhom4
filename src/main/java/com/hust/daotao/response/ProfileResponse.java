package com.hust.daotao.response;

import com.hust.daotao.dto.ProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
	private String message;
	private Integer code;
	private Long iTotalRecords;
	private Long iTotalDisplayRecords;
	private String sEcho;
	private List<ProfileDto> aaData;
	private ProfileDto element;

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

	public ProfileResponse(String message, Integer code) {
		this.message = message;
		this.code = code;
	}

	public ProfileResponse(String message, Integer code, ProfileDto element) {
		super();
		this.message = message;
		this.code = code;
		this.element = element;
	}

}

