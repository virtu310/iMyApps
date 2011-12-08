package net.imyapps.gwt.server;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
	public static ResourceBundle msg;
	
	static {
		msg = ResourceBundle.getBundle("net.imyapps.gwt.server.Messages", Locale.TAIWAN);
	}
	
}
