package com.hust.daotao.util;

import com.hust.daotao.dto.SurveyResultDto;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class UtilMethod {

	public static String uploadFile(MultipartFile multipartFile, Integer type) {
		String url = Constants.DIR_UPLOAD;
		if (type == Constants.TYPE_IMAGE)
			url += "/images/";
		else if (type == Constants.TYPE_LESSON)
			url += "/lessons/";
		try {
			String fileName = formatDate("yyyy_MM_dd_HH_mm_ss_") + multipartFile.getOriginalFilename();
			File file = new File(getFolderUpload(type), fileName);
			multipartFile.transferTo(file);
			url += fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	public static File getFolderUpload(Integer type) {
		String dir = Constants.URL_DIR_UPLOAD;
		if (type == Constants.TYPE_IMAGE)
			dir += "\\images";
		else if (type == Constants.TYPE_LESSON)
			dir += "\\lessons";

		File folderUpload = new File(System.getProperty("user.dir") + dir);
		if (!folderUpload.exists()) {
			folderUpload.mkdirs();
		}
		return folderUpload;
	}

	public static String setValueIsNull(String text) {
		return (text != null ? text : "");
	}

	public static String encode(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}

	public static String formatDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return sdf.format(timestamp);

	}

	public static String randomString(int n) {

		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

	public static Integer convertTestResult(int testResult) {
		if (testResult >= 95)
			return 4;
		if (testResult < 95 && testResult >= 85)
			return 3;
		if (testResult < 85 && testResult >= 70)
			return 2;
		if (testResult < 70 && testResult >= 50)
			return 1;
		return 0;
	}

	public static boolean validateSurvey(List<SurveyResultDto> listSurvey, List<Integer> listCriteriaIds) {
		for (SurveyResultDto surveyResultDto : listSurvey) {
			if (!listCriteriaIds.contains(surveyResultDto.getCriteriaId()))
				return false;
		}
		return true;
	}
}
