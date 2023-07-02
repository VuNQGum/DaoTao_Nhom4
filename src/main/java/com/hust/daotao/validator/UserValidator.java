package com.hust.daotao.validator;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserValidator extends BaseValidator {
	public static boolean isEmpty(Integer subjectId, String name, String studentCode, String email, String password,
			String rePassword) {
		if (subjectId == null || subjectId == 0 || name.isEmpty() || email.isEmpty() || studentCode.isEmpty()
				|| password.isEmpty() || rePassword.isEmpty())
			return true;
		return false;
	}

	public static boolean isEmpty(String str1, String str2, String str3) {
		if (str1.isEmpty() || str2.isEmpty() || str3.isEmpty())
			return true;
		return false;
	}

	public static boolean isEmail(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	public static boolean isPhone(String phoneNo) {
		// validate phone numbers of format "1234567890"
		if (phoneNo.matches("\\d{10}"))
			return true;
		// validating phone number with -, . or spaces
		else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
			return true;
		// validating phone number with extension length from 3 to 5
		else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
			return true;
		// validating phone number where area code is in braces ()
		else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
			return true;
		// return false if nothing matches the input
		else
			return false;
	}

	public static boolean checkLengthPassword(String password) {
		if (password.length() < 6)
			return false;
		return true;
	}

	public static boolean confirmPassword(String password, String rePassword) {
		if (!password.equals(rePassword)) {
			return false;
		}
		return true;
	}

	public static boolean checkCurrentPassword(String password, String currentPassword) {
		PasswordEncoder passwordEnocder = new BCryptPasswordEncoder();
		return passwordEnocder.matches(password, currentPassword);

	}

	public static void main(String[] args) {
	}
}
