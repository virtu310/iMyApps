package net.imyapps.gwt.server.dao;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import net.imyapps.common.Account;
import com.lm.keyrow.EqualCondition;
import com.lm.keyrow.KR;
import com.lm.keyrow.KeyRowException;

public class AccountKeyRowImpl implements AccountAdapter {
	/*
	static Account def_account = new Account();

	{
		def_account.setCreateTime(new Date(0).getTime());
		def_account.setUpdateTime(def_account.getCreateTime());
		def_account.setUid("uid0");
		def_account.setEmail(Configure.MailFrom);
		def_account.setLoginName("admin");
		def_account.setPassword("aamin");
		def_account.setQuestion("hello");
		def_account.setAnswer("world");
		def_account.setStatus(Account.STATUS_NORMAL);
	}
	*/
	@Override
	public Account get(String uid) throws Exception {
		/*
		if (uid.equals(def_account.getUid())) {
			Account account = new Account();
			BeanUtils.copyProperties(account, def_account);
			return account;
		}
		*/
		return (Account) KR.get(Account.class, uid);
	}
	
	@Override
	public void put(Account account) throws KeyRowException {
		if (StringUtils.isNotEmpty(account.getLoginName()))
			account.setLoginName(account.getLoginName().toLowerCase());
		KR.put(account);
	}

	@Override
	public void update(Account account) throws KeyRowException {
		if (StringUtils.isNotEmpty(account.getLoginName()))
			account.setLoginName(account.getLoginName().toLowerCase());
		KR.update(account);
	}
	
	@Override
	public Account getByLoginName(String loginName) throws Exception {
		Account eq = new Account();
		eq.setLoginName(loginName.toLowerCase());
		List<Account> accounts = KR.queryT(eq, new EqualCondition<Account>(eq));
		if (accounts != null && accounts.size() == 1) {
			return accounts.get(0);
		}
		/*
		if (loginName.equalsIgnoreCase(def_account.getLoginName())) {
			Account account = new Account();
			BeanUtils.copyProperties(account, def_account);
			return account;
		}
		*/
		return null;
	}
}
