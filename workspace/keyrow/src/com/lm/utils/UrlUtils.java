package com.lm.utils;

public class UrlUtils {
	public static String appendQuery(String url, String append) {
		if (url.contains("?"))
			return url + '&' + append;
		else
			return url + '?' + append;
	}
}
