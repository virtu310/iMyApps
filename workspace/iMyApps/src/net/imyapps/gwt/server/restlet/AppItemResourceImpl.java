package net.imyapps.gwt.server.restlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import net.imyapps.common.Account;
import net.imyapps.common.AppItem;
import net.imyapps.common.AppItemResource;
import net.imyapps.common.AppItems;
import net.imyapps.gwt.server.AppItemManager;
import net.imyapps.gwt.server.CacheManager;
import net.imyapps.gwt.server.SessionManager;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class AppItemResourceImpl extends ServerResource implements AppItemResource {
	static Logger logger = LoggerFactory.getLogger(AppItemResourceImpl.class);

	@Get
	public AppItems list() {
		Account account = SessionManager.getLoginUser(this);
		if (account == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		
		int begin = 0;
		int size = 20;
		
		try {
			begin = Integer.parseInt((getQuery().getFirstValue("begin")));
		}
		catch (Exception e) {
		}
		
		try {
			size = Integer.parseInt((getQuery().getFirstValue("size")));
		}
		catch (Exception e) {
		}
		
		AppItem itemArray[] = AppItemManager.list(account.getUid(), begin, size);
		if (itemArray == null) {
			return null;
		}
		
		AppItems result = new AppItems();
		result.setAppItems(itemArray);
		result.setTotalSize(AppItemManager.totalSize(account.getUid()));
		result.setBegin(begin);
		result.setSize(result.getAppItems().length);
		
		return result;
	}

	@Post
	public String[] add(AppItems appItems) {
		Account account = SessionManager.getLoginUser(this);
		if (account == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		
		List<String> respString = new ArrayList<String>();
		
		String cacheKey = CacheManager.getCacheId(account.getUid());
		CacheManager.appItemsCache.delete(cacheKey + "?0:20");
		CacheManager.appItemsCache.delete(cacheKey + "?20:20");
		CacheManager.statisticCache.delete(cacheKey);
		
		for (AppItem appItem : appItems.getAppItems()) {
			// 目前只支援 iOS
			appItem.setPlatform(AppItem.PLATFORM_IOS);
			
			if (AppItemManager.add(account.getUid(), appItem)) {
				StringBuilder sb = new StringBuilder();
				sb.append(appItem.getPlatform());
				sb.append(",").append(appItem.getBuyerId());
				sb.append(',').append(appItem.getItemId());
				respString.add(sb.toString());
			}
		}
		
		return respString.toArray(new String[respString.size()]);
	}

}
