package net.imyapps.gwt.server.restlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.imyapps.common.Account;
import net.imyapps.common.AppItems;
import net.imyapps.common.SearchAppItemResource;
import net.imyapps.gwt.server.AppItemManager;
import net.imyapps.gwt.server.SessionManager;

import org.apache.commons.lang.StringUtils;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class SearchAppItemResourceImpl extends ServerResource implements SearchAppItemResource {
	static Logger logger = LoggerFactory.getLogger(SearchAppItemResourceImpl.class);

	@Get
	public AppItems search() {
		Account account = SessionManager.getLoginUser(this);
		if (account == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		
		String q = getQuery().getFirstValue("q");
		if (StringUtils.isEmpty(q))
			q = getQuery().getFirstValue("query");
		
		if (StringUtils.isEmpty(q)) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}
		
		AppItems result = new AppItems();
		result.setAppItems(AppItemManager.search(account.getUid(), q));
		result.setTotalSize((long) result.getAppItems().length);
		result.setSize(result.getAppItems().length);
		
		return result;
	}

}
