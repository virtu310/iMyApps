package net.imyapps.gwt.client;

public class API {
	public static String login() {
		return "/API/login";
	}
	
	public static String remember_login() {
		return "/API/login?r=true";
	}
	
	public static String logout() {
		return "/API/login?a=logout";
	}
	
	/*
	public static String login(String loginname, String password) {
		return "/API/login?l=" + loginname + "&p=" + password;
	}
	*/
	public static String account(String seed, String verify) {
		return "/API/account?seed=" + seed + "&verify=" + verify;
	}
	
	public static String account() {
		return "/API/account";
	}

	public static String getQuestion(String loginname) {
		return "/API/account/N/" + loginname;
	}

	public static String iTunes() {
		return "/API/itunes";
	}
	
	public static String appItem() {
		return "/API/appitem";
	}
	
	public static String appItem(int begin, int size) {
		return "/API/appitem?begin=" + begin + "&size=" + size;
	}
	
	public static String listApp() {
		return "/API/listapp";
	}
	
	public static String preload() {
		return "/API/listapp?preload";
	}
	
	public static String searchAppItem(String query) {
		return "/API/search/appitem?q=" + query;
	}
	
	public static String statisticAppItem(long begin, long end) {
		return "/API/statistic/appitem?b=" + begin + "&e=" + end;
	}
}
