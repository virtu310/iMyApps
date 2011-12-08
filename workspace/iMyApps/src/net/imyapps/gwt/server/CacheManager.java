package net.imyapps.gwt.server;

import java.util.List;

import com.lm.cache.JRECache;
import net.imyapps.common.AppItem;
import net.imyapps.common.Item;
import net.imyapps.common.Statistic;
import net.imyapps.utils.SmartCache;


public class CacheManager {
	public static JRECache<Item> itemCache = new JRECache<Item>();
	public static SmartCache<List<AppItem>> appItemsCache = new SmartCache<List<AppItem>>("appItemsCache");
	public static SmartCache<Statistic> statisticCache = new SmartCache<Statistic>("statisticCache");
	
	public static String getCacheId(String uid) {
		return "PIL#" + uid;
	}
	
	public static String getItemCacheId(String itemId) {
		return "Item#" + itemId;
	}
}
