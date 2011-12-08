package net.imyapps.gwt.server.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lm.keyrow.EqualCondition;
import com.lm.keyrow.KR;

import net.imyapps.common.AppItem;
import net.imyapps.common.Item;
import net.imyapps.common.PurchasedItem;
import net.imyapps.gwt.server.CacheManager;
import net.imyapps.gwt.server.ItemManager;
import net.imyapps.gwt.server.PurchasedItemManager;

public class AppItemKeyRowImpl implements AppItemAdapter {
	static Logger logger = LoggerFactory.getLogger(AppItemKeyRowImpl.class);
	
	public AppItem[] list(String uid, int begin, int size) throws Exception {
		String cacheKey = CacheManager.getCacheId(uid) + "?" + begin + ":" + size;
		List<AppItem> results = null;
		
		results = CacheManager.appItemsCache.get(cacheKey);
		if (results != null) {
			return results.toArray(new AppItem[results.size()]);
		}
		
		PurchasedItem eq = new PurchasedItem();
		eq.setUid(uid);
		
		EqualCondition<PurchasedItem> ec = new EqualCondition<PurchasedItem>(eq, begin, size);
		ec.setSort("PurchaseDate");
		ec.setDirection(EqualCondition.DESCENDING);
		
		List<PurchasedItem> purchasedItems = KR.queryT(eq, ec);
		Iterator<PurchasedItem> iter = purchasedItems.iterator();
		if (results == null) {
			results = new ArrayList<AppItem>();
		}
		
		while (iter.hasNext()) {
			PurchasedItem pi = iter.next();
			
			String itemCacheKey = CacheManager.getItemCacheId(pi.getItemId());
			Item item = CacheManager.itemCache.get(itemCacheKey);
			if (item == null) {
				item = (Item) KR.get(Item.class, 
					Item.getKey(pi.getPlatform(), pi.getItemId()));

				if (item == null) {
					continue;
				}
				
				CacheManager.itemCache.put(itemCacheKey, item, 604800000);
			}
			
			AppItem appItem = new AppItem();
			appItem.copyFrom(item);
			appItem.copyFrom(pi);
			/*
			try {
				BeanUtils.copyProperties(appItem, item);
				BeanUtils.copyProperties(appItem, pi);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			*/
			results.add(appItem);
		}
		
//		if (begin + lst.size() >= itemMap.size())
//			lst.add(new AppItem());
		
		CacheManager.appItemsCache.put(cacheKey, results, size < 0 ? 1209600000 : 1800000);
		return results.toArray(new AppItem[results.size()]);
	}

	@Override
	public boolean add(String uid, AppItem appItem) throws Exception {
		long curr = System.currentTimeMillis();
		Item item = ItemManager.getItem(appItem.getPlatform(), appItem.getItemId());
		
		if (item == null) {
			item = new Item();
			BeanUtils.copyProperties(item, appItem);
			item.setCreateTime(curr);
			item.setUpdateTime(curr);
			if (ItemManager.putItem(item) == false)
				return false;
			CacheManager.itemCache.put(
					CacheManager.getItemCacheId(item.getItemId()), item, 604800000);
		}
		else {
			boolean modified = false;
			
			if (item.getSoftwareSupportedDeviceIds().equals(
					appItem.getSoftwareSupportedDeviceIds()) == false) {
				item.setSoftwareSupportedDeviceIds(
						appItem.getSoftwareSupportedDeviceIds());
				modified = true;
			}
			
			if (item.getSoftwareVersionExternalIdentifiers().equals(
					appItem.getSoftwareVersionExternalIdentifiers()) == false) {
				item.setSoftwareVersionExternalIdentifiers(
						appItem.getSoftwareVersionExternalIdentifiers());
				modified = true;
			}
			

			if (StringUtils.isNotEmpty(appItem.getPriceDisplay()) &&
				(StringUtils.isEmpty(item.getPriceDisplay()) ||
				item.getPriceDisplay().equals(appItem.getPriceDisplay()) == false)) {
				item.setPriceDisplay(appItem.getPriceDisplay());
				modified = true;
			}
			
			if (StringUtils.isNotEmpty(appItem.getPrice()) &&
				(StringUtils.isEmpty(item.getPrice()) ||
				item.getPrice().equals(appItem.getPrice()) == false)) {
				item.setPrice(appItem.getPrice());
				modified = true;
			}
			/*
			if (StringUtils.isNotEmpty(appItem.getPlaylistName()) &&
				(StringUtils.isEmpty(item.getPlaylistName()) ||
				item.getPlaylistName().equals(appItem.getPlaylistName()) == false)) {
				item.setPlaylistName(appItem.getPlaylistName());
				modified = true;
			}
			
			if (StringUtils.isNotEmpty(appItem.getItemName()) &&
				(StringUtils.isEmpty(item.getItemName()) ||
				item.getItemName().equals(appItem.getItemName()) == false)) {
				item.setItemName(appItem.getItemName());
				modified = true;
			}
			*/
			if (modified) {
				item.setUpdateTime(System.currentTimeMillis());
				//CacheManager.itemCache.delete(
				//		CacheManager.getItemCacheId(appItem.getItemId()));
				if (ItemManager.updateItem(item) == false)
					return false;
				CacheManager.itemCache.put(
						CacheManager.getItemCacheId(item.getItemId()), item, 604800000);
			}
		}

		String cacheKey = CacheManager.getCacheId(uid) + "?0:-1";
		List<AppItem> appItems = CacheManager.appItemsCache.get(cacheKey);
		
		PurchasedItem pi = PurchasedItemManager.getPurchasedItem(
				uid, 
				appItem.getPlatform(), 
				appItem.getBuyerId(), 
				appItem.getItemId());
		
		if (pi == null) {
			pi = new PurchasedItem();
			BeanUtils.copyProperties(pi, appItem);
			pi.setUid(uid);
			pi.setCreateTime(curr);
			pi.setUpdateTime(curr);
			
			// update cache
			if (appItems != null) {
				appItems.add(0, appItem);
				CacheManager.appItemsCache.put(cacheKey, appItems, 1209600000);
			}
			return PurchasedItemManager.putItem(pi);
		}
		else {
			boolean modified = false;
			if (pi.getSoftwareVersionExternalIdentifier().equals(
				appItem.getSoftwareVersionExternalIdentifier()) == false) {
				pi.setSoftwareVersionExternalIdentifier(
						appItem.getSoftwareVersionExternalIdentifier());
				modified = true;
			}
			if (StringUtils.isNotEmpty(appItem.getNote()) &&
				(StringUtils.isEmpty(pi.getNote()) ||
				pi.getNote().equals(appItem.getNote()) == false)) {
				if (appItem.getNote().length() > 80)
					pi.setNote(appItem.getNote().substring(0, 80));
				else
					pi.setNote(appItem.getNote());
				modified = true;
			}
			if (StringUtils.isNotEmpty(appItem.getBuyPrice()) &&
				(StringUtils.isEmpty(pi.getBuyPrice()) ||
				pi.getBuyPrice().equals(appItem.getBuyPrice()) == false)) {
				if (appItem.getBuyPrice().length() > 20)
					pi.setBuyPrice(appItem.getBuyPrice().substring(0, 20));
				else
					pi.setBuyPrice(appItem.getBuyPrice());
				modified = true;
			}
			if (appItem.getGood() != null &&
				(pi.getGood() == null ||
				appItem.getGood() != pi.getGood())) {
				pi.setGood(appItem.getGood());
				modified = true;
			}
			if (modified) {
				pi.setUpdateTime(System.currentTimeMillis());
				
				// update cache
				if (appItems != null) {
					Iterator<AppItem> iter = appItems.iterator();
					while (iter.hasNext()) {
						AppItem aitem = iter.next();
						if (aitem.getItemId().equals(pi.getItemId())) {
							iter.remove();
							break;
						}
					}
					appItems.add(0, appItem);
					CacheManager.appItemsCache.put(cacheKey, appItems, 1209600000);
				}
				return PurchasedItemManager.updateItem(pi);
			}
		}
		
		return false;
	}

	@Override
	public long totalSize(String uid) throws Exception {
		PurchasedItem eq = new PurchasedItem();
		eq.setUid(uid);
		return KR.size(PurchasedItem.class, new EqualCondition<PurchasedItem>(eq));
	}
}
