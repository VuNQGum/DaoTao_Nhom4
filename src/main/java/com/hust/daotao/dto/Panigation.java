package com.hust.daotao.dto;

import lombok.Data;

@Data
public class Panigation {
	private Long totalPage;
	private Long totalSection;
	private Integer currentSection;
	private Integer pageStart;
	private Integer currentPage;
	private Boolean isBackSection;
	private Boolean isNextSection;
	private Integer pageEnd;
	private Integer quantityShowing;

	public Panigation(Integer page, Long totalPage) {
		this.currentPage = page;
		this.totalPage = totalPage;
		this.totalSection = totalPage % 3 == 0 ? totalPage / 3 : totalPage / 3 + 1;
		this.currentSection = page % 3 == 0 ? page / 3 : page / 3 + 1;
		this.pageStart = this.currentSection * 3 - 2;
		this.pageEnd = (int) (this.currentSection * 3 < totalPage ? this.currentSection * 3 : totalPage);

		if (this.currentSection != 1)
			this.isBackSection = true;
		if ((long) this.currentSection != totalSection)
			this.isNextSection = true;
	}

}
