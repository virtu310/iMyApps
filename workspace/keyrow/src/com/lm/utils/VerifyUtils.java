package com.lm.utils;

public class VerifyUtils {
	public static boolean isEmail(String email) {
		try {
			String v[] = email.split("@");
			if (v.length != 2)
				return false;
			String domain[] = v[1].split("\\.");
			if (domain.length < 2 || domain[0].length() <= 2 || domain[1].length() < 2)
				return false;
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isMobile(String mobile) {
		return mobile.length() >= 10;
	}
}
