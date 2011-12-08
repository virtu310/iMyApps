package net.imyapps.utils;

public class PriceUtils {
	public static int to(char c, String value) {
		return to(c, Integer.parseInt(value));
	}
	
	public static int to(char c, int value) {
		switch (c) {
		case '$':
			return value;
		case '¥':
			return value / 80;
		case '€':
			return (int)(((double) value * 1.437f));
		}
		
		return value;
	}
	
	public static int to(String value) {
		if (value.length() == 0) {
			return -1;
		}
		
		char c = value.charAt(0);
		
		try {
			if (c >= '0' && c <= '9') {
				return (int) (Double.parseDouble(value) * 1000.0f);
			}
			
			if (value.equalsIgnoreCase("free")) {
				return 0;
			}
			else if (value.equalsIgnoreCase("none")) {
				return 0;
			}
			
			return to(c, (int) (Double.parseDouble(value.substring(1)) * 1000.0f));
		}
		catch (Exception e) {
			return -1;
		}
	}
}
