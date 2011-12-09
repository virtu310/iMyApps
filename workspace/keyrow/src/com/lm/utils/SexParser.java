package com.lm.utils;

import com.lm.keyrow.rows.PersonInformation;

public class SexParser {
	public static Integer parse(String sex) {
		if (sex == null || sex.length() == 0)
			return PersonInformation.SEX_UNKNOW;
		if (sex.equals("男性"))
			return PersonInformation.SEX_MALE;
		if (sex.equals("女性"))
			return PersonInformation.SEX_FEMALE;
		return PersonInformation.SEX_UNKNOW;
	}
	
	public static String showText(String sex) {
		try {
			double s = Double.parseDouble(sex);
			if (s == 1.0f)
				return "男性";
			else if (s == 2.0f)
				return "女性";
		}
		catch (Exception e) {
		}
		return "未知";
	}
}
