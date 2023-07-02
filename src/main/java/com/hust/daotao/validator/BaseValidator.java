package com.hust.daotao.validator;

import com.hust.daotao.dto.ProfileDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BaseValidator {
	public static boolean isEmpty(String name, String nameStudentSide, String nameTeacherSide, String code) {
		if (name.isEmpty() || code.isEmpty() || name == null || code == null || nameStudentSide.isEmpty()
				|| nameTeacherSide.isEmpty() || nameStudentSide == null || nameTeacherSide == null) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String name, String code) {
		if (name.isEmpty() || code.isEmpty() || name == null || code == null) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String name, String code, Double value) {
		if (name.isEmpty() || code.isEmpty() || value == null || name == null || code == null) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(List<ProfileDto> profileDtos, HttpServletRequest request) {
		for (ProfileDto profileDto : profileDtos) {
			if (request.getParameter(profileDto.getCode()).isEmpty()
					|| request.getParameter(profileDto.getCode()) == null) {
				return true;
			}
		}
		return false;
	}
}
