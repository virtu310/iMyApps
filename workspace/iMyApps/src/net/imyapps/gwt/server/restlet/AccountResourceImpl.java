package net.imyapps.gwt.server.restlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

import net.imyapps.common.Account;
import net.imyapps.common.AccountResource;
import net.imyapps.gwt.client.Base64Coder;
import net.imyapps.gwt.server.AccountManager;
import net.imyapps.gwt.server.SessionItem;
import net.imyapps.gwt.server.SessionManager;

import org.apache.commons.lang.StringUtils;
import org.restlet.client.resource.Post;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;


/**
 * 
 * @author Cyril
 *
 */
public class AccountResourceImpl extends ServerResource 
								 implements AccountResource {
	static Logger logger = LoggerFactory.getLogger(AccountResourceImpl.class);
	static Set<String> reservedSet = new HashSet<String>();
	
	static {
		reservedSet.add("admin");
		reservedSet.add("administrator");
		reservedSet.add("manager");
		reservedSet.add("webmaster");
		reservedSet.add("webadm");
		reservedSet.add("webadmin");
		reservedSet.add("webadministrator");
		reservedSet.add("web_master");
		reservedSet.add("webmanager");
	}
	
	@Override
	protected void doInit() throws ResourceException {
	}

	@Get
	public Account retrieve() {
		try {
			String uid = (String) getRequest().getAttributes().get("uid");
			
			if (uid != null) {
				Account account = AccountManager.getAccountByUid(uid);
				if (account.getStatus() != Account.STATUS_DELETED) {
					account.setPassword(null);
					account.setAnswer(null);
					
					SessionItem si = SessionManager.findSession(this);
					if (si == null ||
						si.getAccount().getUid().equals(uid) == false) {
						account.setCreateTime(null);
						account.setQuestion(null);
						account.setStatus(null);
					}
					
					return account;
				}
			}
			
			String loginName = (String) getRequest().getAttributes().get("loginname");
			if (loginName != null) {
				Account account = AccountManager.getAccountByLoginName(loginName);
				
				if (account == null)
					return null;
				
				if (account.getStatus() != Account.STATUS_DELETED) {
					Account ret = new Account();
					ret.setQuestion(account.getQuestion());
					return ret;
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

	@Post
	public void create(Account account) {
		if (StringUtils.isEmpty(account.getLoginName()) ||
			account.getLoginName().length() < 5 || 
			account.getLoginName().length() > 32 ||
			StringUtils.isEmpty(account.getPassword()) ||
			account.getPassword().length() < 4 ||
			account.getPassword().length() > 32) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
			return;
		}
		
		if (reservedSet.contains(account.getLoginName().toLowerCase())) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
			return;
		}
		
		try {
			Account accountInSystem = 
					AccountManager.getAccountByLoginName(account.getLoginName());
			
			if (accountInSystem == null) {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(account.getPassword().getBytes());
				String md5Pass = Base64Coder.encodeLines(md5.digest());
				account.setPassword(md5Pass);
				AccountManager.putAccount(account);
				if (AccountManager.sendAuthMail(account.getUid())) {
					logger.info(account.getUid() + " / " + 
							account.getLoginName() + 
							" has been inserted!");
				}
				else {
					getResponse().setStatus(Status.SERVER_ERROR_SERVICE_UNAVAILABLE);	
				}
			}
			else {
				logger.warn(account.getLoginName() + " already exists!");
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
			}
		}
		catch (Exception e) {
			logger.error("Occur exception:", e);
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
		}
	}

	@Put
	public void update(Account account) {
		try {
			SessionItem si = SessionManager.findSession(this);
			if (si == null) {
				if (StringUtils.isEmpty(account.getLoginName()) ||
					StringUtils.isEmpty(account.getAnswer())) {
					getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				}
				else {
					// 重寄身分認證信
					Account accountInSystem = 
						AccountManager.getAccountByLoginName(account.getLoginName());
					if (accountInSystem == null ||
						accountInSystem.getEmail().equals(account.getEmail()) == false ||
						accountInSystem.getAnswer().equals(account.getAnswer()) == false) {
						getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);	
					}
					else {
						if (AccountManager.sendAuthMail(accountInSystem.getUid()) == false) {
							getResponse().setStatus(Status.SERVER_ERROR_SERVICE_UNAVAILABLE);
						}
					}
				}
				return;
			}
			
			if (si.getAccount().getUid().equals(account.getUid()) == false) {
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				return;
			}
			
			Account accountInSystem = 
				AccountManager.getAccountByUid(account.getUid());
			
			if (accountInSystem != null) {
				/*
				String seed = getQuery().getFirstValue("seed");
				String verify = getQuery().getFirstValue("verify");
				
				String key = accountInSystem.getPassword() + '+' + seed;
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(key.getBytes());
				String verifyKey = Base64Coder.encodeLines(md5.digest());
				if (verifyKey.equals(verify) == false) {
					
					logger.warn(account.getUid() + " / " + account.getLoginName() + 
								" want to update account, but password was invalid!");
					
					getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
					return;
				}
				*/
				boolean modified = false;
				
				if (StringUtils.isNotEmpty(account.getAnswer())) {
					accountInSystem.setAnswer(account.getAnswer());
					modified = true;
				}

				if (StringUtils.isNotEmpty(account.getQuestion())) {
					accountInSystem.setQuestion(account.getQuestion());
					modified = true;
				}

				if (StringUtils.isNotEmpty(account.getPassword())) {
					MessageDigest md5 = MessageDigest.getInstance("MD5");
					md5.update(account.getPassword().getBytes());
					String md5Pass = Base64Coder.encodeLines(md5.digest());
					accountInSystem.setPassword(md5Pass);
					modified = true;
				}
				
				if (modified) {
					AccountManager.putAccount(accountInSystem);
				}
			}
			else {
				logger.error(account.getUid() + 
						" did not exists!");
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
			}
		}
		catch (Exception e) {
			logger.error("Occur exception:", e);
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
		}
	}
	
	@Delete
	public void remove() {
		SessionItem si = SessionManager.findSession(this);
		if (si == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return;
		}
		
		String uid = (String) getRequest().getAttributes().get("uid");
		
		if (uid != null) {
			if (si.getAccount().getUid().equals(uid) == false) {
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				return;
			}

			Account accountInSystem = 
					AccountManager.getAccountByUid(uid);
			
			if (accountInSystem != null) {
				accountInSystem.setStatus(Account.STATUS_DELETED);
				AccountManager.putAccount(accountInSystem);
			}
		}
	}

}
