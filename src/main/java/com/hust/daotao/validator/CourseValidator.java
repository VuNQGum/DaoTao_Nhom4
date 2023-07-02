package com.hust.daotao.validator;

public class CourseValidator extends BaseValidator {
	public static boolean isEmpty(String name, String code, Integer status, String description, Integer categoryId,
			Integer userId) {
		if (name == null || name.isEmpty() || code == null || code.isEmpty() || status == null || categoryId == null
				|| categoryId == 0 || description.isEmpty() || userId == 0 || userId == null) {
			return true;
		}
		return false;
	}
}
