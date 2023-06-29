package com.hust.daotao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphDto {
	private List<String> titles;
	private List<Integer> valuesInteger;
	private List<Double> valuesDouble;
	private double valueDouble;
	private double valueInteger;
	private int count;

}
