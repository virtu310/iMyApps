package net.imyapps.gwt.server.restlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.imyapps.common.Account;
import net.imyapps.common.Statistic;
import net.imyapps.common.StatisticResource;
import net.imyapps.gwt.server.AppItemManager;
import net.imyapps.gwt.server.SessionManager;

import org.apache.commons.lang.StringUtils;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


/**
 * 
 * @author Cyril
 *
 */
public class StatisticResourceImpl extends ServerResource 
								 implements StatisticResource {
	static Logger logger = LoggerFactory.getLogger(StatisticResourceImpl.class);

	@Get
	public Statistic getStatistic() {
		Account account = SessionManager.getLoginUser(this);
		if (account == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		
		String begin = getQuery().getFirstValue("b");
		String end = getQuery().getFirstValue("e");
		
		if (StringUtils.isEmpty(begin))
			begin = getQuery().getFirstValue("begin");
		
		if (StringUtils.isEmpty(end))
			end = getQuery().getFirstValue("end");
		
		if (StringUtils.isEmpty(begin) || StringUtils.isEmpty(end)) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
			return null;
		}
		
		return AppItemManager.statistic(account.getUid(), 
							Long.parseLong(begin), 
							Long.parseLong(end));
	}
	
	
}
