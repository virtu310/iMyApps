package com.lm.utils;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
	public static DateFormat dateFormat_normal = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static DateFormat dateFormat_normal2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	public static DateFormat dateFormat_date = new SimpleDateFormat("yyyy/MM/dd");
	public static DateFormat dateFormat_graph = new SimpleDateFormat("MM/dd/yyyy");
	public 	static DateFormat dateFormat = new SimpleDateFormat("EE-MM-dd-yyyy");
	public static DateFormat dateFormat_twymd = new SimpleDateFormat("yyyy年MM月dd日");
	public static DateFormat dateFormat_twmd = new SimpleDateFormat("MM月dd日");
	
	public static Date from(String date) {
		if (date == null)
			return null;
		
		try {
			return dateFormat_normal.parse(date);
		} catch (ParseException e) {
			try {
				return dateFormat_normal2.parse(date);
			} catch (ParseException e1) {
				try {
					return dateFormat_date.parse(date);
				} catch (ParseException e2) {
					return null;
				}
			}
		}
	}
	
	public static String to(Date date) {
		try {
			return dateFormat_normal.format(date);
		} catch (Exception e) {
			try {
				return dateFormat_normal2.format(date);
			}
			catch (Exception e1) {
				try {
					return dateFormat_date.format(date);
				} catch (Exception e2) {
					return null;
				}
			}
		}
	}
	
	public static Date parse(String date) {
		if (date == null)
			return null;
		
		try {
			return dateFormat_date.parse(date);
		} catch (Exception e0) {
			try {
				return dateFormat_twymd.parse(date);
			} catch (ParseException e) {
				try {
					Date d = dateFormat_twmd.parse(date);
					d.setYear(new Date().getYear());
					return d;
				} catch (ParseException e1) {
					return null;
				}
			}
		}
	}
	
	public static String showText(Date date) {
		if (date == null)
			return "";
		
		if (date.getYear() > 0)
			return dateFormat_twymd.format(date);
		else
			return dateFormat_twmd.format(date);
	}
}
