package com.hust.daotao.validator;

public class ProfileValidator extends BaseValidator{
	public static boolean isEmpty(String name, String nameStudentSide, String nameTeacherSide, String code,
			Double weight) {
		if (name.isEmpty() || code.isEmpty() || name == null || code == null || nameStudentSide.isEmpty()
				|| nameTeacherSide.isEmpty() || nameStudentSide == null || nameTeacherSide == null || weight == null) {
			return true;
		}
		return false;
	}
}
