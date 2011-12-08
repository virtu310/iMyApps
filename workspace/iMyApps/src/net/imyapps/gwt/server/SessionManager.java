package net.imyapps.gwt.server;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


import net.imyapps.common.Account;
import net.imyapps.common.SessionRecord;
import net.imyapps.utils.SmartCache;
import net.imyapps.gwt.server.dao.SessionAdapter;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SessionManager {
	static Logger logger = LoggerFactory.getLogger(SessionManager.class);
	
	static int SESSION_KEEP_HOURS = 2 * 7 * 24;
	static int CACHE_KEEP_TIME = 6 * 60 * 60 * 1000;
	
	static SmartCache<SessionItem> cache = new SmartCache<SessionItem>();
	static SessionAdapter sessionAdapter;
	
	public static void setSessionAdapter(SessionAdapter sa) {
		sessionAdapter = sa;
	}
	
	public static boolean isSessionExpire(long createTime) {
		Calendar createTM = Calendar.getInstance();
		createTM.setTime(new Date(createTime));
		createTM.add(Calendar.HOUR_OF_DAY, SESSION_KEEP_HOURS);
		return createTM.before(Calendar.getInstance());
	}
	
	public static String retrieveSID(Request request) {
		Cookie sid = request.getCookies().getFirst("_SESSION_ID_");
		if (sid != null) {
			return sid.getValue();
		}
		return null;
	}
	
	public static Account getLoginUser(ServerResource resource) {
		SessionItem si = findSession(resource);
		if (si != null)
			return si.getAccount();
		
		return null;
	}

	@Deprecated
	public static SessionItem findSession(Request request) {
		return findSession(retrieveSID(request));
	}
	
	public static SessionItem findSession(ServerResource resource) {
		return findSession(retrieveSID(resource.getRequest()));
	}
	
	public static SessionItem findSession(CharSequence sid) {
		if (sid == null) {
			return null;
		}
		
		SessionItem si = cache.get(sid);
		if (si != null)
			return si;
		
		try {
			SessionRecord sr = sessionAdapter.get(sid);
			if (sr != null) {
				logger.info("found session \"" + sid + "\" in DB!");
				
				if (isSessionExpire(sr.getCreateTime())) {
					Date createTime = new Date(sr.getCreateTime());
					Calendar expireTime = Calendar.getInstance();
					expireTime.setTime(createTime);
					expireTime.add(Calendar.HOUR_OF_DAY, SESSION_KEEP_HOURS);
					logger.info("session \"" + sid + "\" was timeout! " + 
									createTime + " -> " + expireTime.getTime());
					sessionAdapter.delete(sid);
					sessionAdapter.purge(sr.getUid());
					return null;
				}
				Account account = AccountManager.getAccountByUid(sr.getUid());
				account.setPassword(null);
				account.setQuestion(null);
				account.setAnswer(null);
				
				si = new SessionItem();
				si.setCreateTime(sr.getCreateTime());
				si.setSid(sr.getSid());
				si.setAccount(account);
				cache.put(si.getSid(), si, CACHE_KEEP_TIME);
				return si;
			}
		}
		catch (Exception e) {
			logger.error("Occur exception:" + e);
		}
		
		return null;
	}
	
	public static SessionItem createSession(ServerResource resource, 
											Account account) {
		boolean keep = false;
		Cookie sid = resource.getRequest().getCookies().getFirst("RememberLogin");
		if (sid != null && Boolean.parseBoolean(sid.getValue())) {
			keep = true;
		}
		return createSession(resource.getResponse(), account, keep);
	}
	
	/**
	 * 
	 * @param response
	 * @param account
	 * @param keep if this value is true, it will store session record to DB
	 * @return
	 */
	public static SessionItem createSession(Response response, 
											Account account, boolean keep) {
		SessionRecord sr = new SessionRecord();
		sr.setCreateTime(System.currentTimeMillis());
		sr.setUid(account.getUid());
		sr.setSid(UUID.randomUUID().toString());
		
		if (keep) {
			try {
				sessionAdapter.purge(sr.getUid());
				sessionAdapter.put(sr);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Remove privacy information of account
		account.setPassword(null);
		account.setQuestion(null);
		account.setAnswer(null);
		
		SessionItem si = new SessionItem();
		si.setCreateTime(sr.getCreateTime());
		si.setSid(sr.getSid());
		si.setAccount(account);
		cache.put(si.getSid(), si, CACHE_KEEP_TIME);
		
		//response.getCookieSettings().set("_SESSION_ID_", si.getSid());
		CookieSetting cookie = new CookieSetting("_SESSION_ID_", si.getSid());
		cookie.setMaxAge(SESSION_KEEP_HOURS * 60 * 60);
		response.getCookieSettings().removeAll("_SESSION_ID_");
		response.getCookieSettings().add(cookie);
		return si;
	}
	
	public static boolean deleteSession(ServerResource resource) {
		return deleteSession(retrieveSID(resource.getRequest()));
	}
	
	public static boolean deleteSession(CharSequence sid) {
		try {
			sessionAdapter.delete(sid);
			return cache.delete(sid);
		}
		catch (Exception e) {
			return false;
		}
	}
}
