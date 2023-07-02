package com.hust.daotao.validator;

public class ProfileValueValidator extends BaseValidator {
	public static boolean isEmpty(String name, String code, Integer value, Integer profileId, String nameStudentSide, String nameTeacherSide) {
		if (name.isEmpty() || code.isEmpty() || value == null || profileId == null || nameStudentSide.isEmpty() || nameTeacherSide.isEmpty()) {
			return true;
		}
		return false;
	}
}
