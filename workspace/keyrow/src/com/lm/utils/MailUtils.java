package com.lm.utils;

import java.io.IOException;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;

public class MailUtils {
	public static boolean mailTo(String from, String to, String subject, String content) {
		try {
			MailServiceFactory.getMailService().send(new MailService.Message(
					from, to, subject, content));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
