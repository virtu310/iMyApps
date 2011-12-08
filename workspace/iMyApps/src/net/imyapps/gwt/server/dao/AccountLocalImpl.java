package net.imyapps.gwt.server.dao;

import java.util.HashMap;
import java.util.Map;

import net.imyapps.common.Account;

import org.apache.commons.beanutils.BeanUtils;

import com.lm.keyrow.KeyRowException;
import com.lm.keyrow.KeyRowUtils;

public class AccountLocalImpl implements AccountAdapter {

	private Map<String, Account> accountMap = new HashMap<String, Account>();
	private Map<String, Account> accountMapByLoginName = 
											new HashMap<String, Account>();
	
	{
		Account account = new Account();
		account.setCreateTime(System.currentTimeMillis());
		account.setUpdateTime(account.getCreateTime());
		account.setUid("uid0");
		account.setLoginName("admin");
		account.setPassword("aamin");
		account.setQuestion("hello");
		account.setAnswer("world");
		account.setStatus(Account.STATUS_NORMAL);
		accountMap.put(account.getUid(), account);
		accountMapByLoginName.put(account.getLoginName(), account);
	}
	
	@Override
	public Account get(String uid) throws Exception {
		Account account = accountMap.get(Account.getKey(uid));
		if (account != null) {
			Account retAccount = new Account();
			BeanUtils.copyProperties(retAccount, account);
			return retAccount;
		}
		
		return null;
	}
	
	@Override
	public void put(Account account) throws KeyRowException {
		accountMap.put(KeyRowUtils.getKeyValue(account), account);
		accountMapByLoginName.put(account.getLoginName().toLowerCase(), account);
	}

	@Override
	public void update(Account account) throws KeyRowException {
		accountMap.put(KeyRowUtils.getKeyValue(account), account);
		accountMapByLoginName.put(account.getLoginName().toLowerCase(), account);
	}
	
	@Override
	public Account getByLoginName(String loginName) throws Exception {
		Account account = accountMapByLoginName.get(loginName.toLowerCase());
		if (account != null) {
			Account retAccount = new Account();
			BeanUtils.copyProperties(retAccount, account);
			return retAccount;
		}
		
		return null;
	}
}
