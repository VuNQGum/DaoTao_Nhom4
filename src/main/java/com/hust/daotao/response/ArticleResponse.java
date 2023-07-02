package com.hust.daotao.response;

import com.hust.daotao.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
	private String message;
	private Integer code;
	private Long iTotalRecords;
	private Long iTotalDisplayRecords;
	private String sEcho;
	private List<ArticleDto> aaData;
	private ArticleDto element;
	private Long iTotalPage;

	public ArticleResponse(Integer code, String message, ArticleDto element) {
		super();
		this.message = message;
		this.code = code;
		this.element = element;
	}

	public ArticleResponse(Long iTotalRecords, Long iTotalPage, List<ArticleDto> aaData) {
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

	public ArticleResponse(String message, Integer code) {
		super();
		this.message = message;
		this.code = code;
	}

}
