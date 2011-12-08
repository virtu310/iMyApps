package net.imyapps.gwt.server;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.ResourceBundle;

import net.imyapps.Configure;
import net.imyapps.common.Account;
import net.imyapps.gwt.client.Base64Coder;
import net.imyapps.gwt.server.dao.AccountAdapter;
import net.imyapps.utils.InstanceUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lm.utils.MailUtils;

public class AccountManager {
	static Logger logger = LoggerFactory.getLogger(AccountManager.class);
	
	private static AccountAdapter adapter;
	
	protected static AccountAdapter setAdapter(AccountAdapter newAdapter) {
		AccountAdapter oldAdapter = adapter;
		adapter = newAdapter;
		return oldAdapter;
	}
	
	protected static AccountAdapter getAdapter() {
		if (adapter == null)
			throw new RuntimeException("AccountAdapter must be setup!");
		
		return adapter;
	}
	
	public static Account getAccountByLoginName(String loginName) {
		try {
			return getAdapter().getByLoginName(loginName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Account getAccountByUid(String uid) {
		try {
			return getAdapter().get(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean putAccount(Account account) {
		try {
			if (StringUtils.isEmpty(account.getUid())) {
				account.setCreateTime(System.currentTimeMillis());
				account.setUpdateTime(account.getCreateTime());
				account.setRole(Account.ROLE_USER);
				account.setStatus(Account.STATUS_CREATED);
				
				for (int i=0; i<10; i++) {
					String uid;
					
					uid = String.valueOf(System.currentTimeMillis()) +
						  String.valueOf(Math.abs(InstanceUtils.random.nextInt(1000)));
					
					synchronized (Runtime.getRuntime()) {
						if (getAdapter().get(uid) != null)
							continue;
						account.setUid(uid);
						getAdapter().put(account);
						break;
					}
				}
			}
			else {
				account.setUpdateTime(System.currentTimeMillis());
				getAdapter().update(account);
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean setLastLoginTime(String uid) {
		try {
			Account account = new Account();
			account.setUid(uid);
			account.setLastLoginTime(System.currentTimeMillis());
			getAdapter().update(account);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getAuthCode(String uid) {
		try {
			Account account = getAccountByUid(uid);
			if (account != null) {
				String hashStr = account.getEmail() + '+' + account.getUpdateTime();
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(hashStr.getBytes());
				return Base64Coder.encodeLines(md5.digest());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	public static boolean authMe(String uid) {
		try {
			Account account = new Account();
			account.setUid(uid);
			account.setStatus(Account.STATUS_NORMAL);
			account.setUpdateTime(System.currentTimeMillis());
			getAdapter().update(account);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean sendAuthMail(String uid) {
		try {
			Account account = getAccountByUid(uid);
			
			if (account == null) {
				return false;
			}
			
			String authCode = getAuthCode(uid);
			String authUrl = Configure.HomeURL + "/authme?uid=" + uid + 
							 "&authcode=" + URLEncoder.encode(authCode, "UTF-8");
			StringBuilder sb = new StringBuilder();
			sb.append("Hello ").append(account.getLoginName()).append(",\n\n" +
					"Welcome to register iMyApps account.\n" +
					"If you had registered iMyApps account, please click below link:\n\n");
			sb.append(authUrl).append("\n\n" +
					"If you didn't register iMyApps account, please do not click the link.\n");
			sb.append("Thank you!\n\n" +
					"iMyApps\n\n" +
					"Website: ").append(Configure.HomeURL).append("\n");
			
			MailUtils.mailTo(Configure.MailFrom, account.getEmail(), 
					Messages.msg.getString("authmail_title"), sb.toString());
			logger.info("Send auth mail to " + account.getUid() + " / " + 
					account.getLoginName() + " / " + account.getEmail() + 
					" - " + authUrl);
			//logger.info(sb.toString());
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.info("Send auth mail to " + uid + " failed!");
			return false;
		}
	}
}
