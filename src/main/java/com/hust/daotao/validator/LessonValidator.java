package com.hust.daotao.validator;

public class LessonValidator extends BaseValidator {
	public static boolean isEmpty(String name, String code, Boolean status) {
		if (name.isEmpty() || code.isEmpty() || status == null)
			return true;
		return false;
	}

	public static boolean isEmpty(String name, String code, Boolean status, Integer lessonId) {
		if (name.isEmpty() || code.isEmpty() || status == null || lessonId == null)
			return true;
		return false;
	}
}
