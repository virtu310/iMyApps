package net.imyapps.gwt.server.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import net.imyapps.common.AppItem;
import net.imyapps.common.Item;
import net.imyapps.common.PurchasedItem;
import net.imyapps.gwt.server.ItemManager;
import net.imyapps.gwt.server.PurchasedItemManager;

public class AppItemLocalImpl implements AppItemAdapter {
	
	public AppItem[] list(String uid, int begin, int size) {
		Map<String, PurchasedItem> itemMap = 
			((PurchasedItemLocalImpl)PurchasedItemManager.getAdapter()).itemMap;
		Iterator<PurchasedItem> iter = itemMap.values().iterator();
		ArrayList<AppItem> lst = new ArrayList<AppItem>();
		int index = 0;
		
		while (iter.hasNext() && (size == -1 || lst.size() < size)) {
			PurchasedItem pi = iter.next();
			if (pi.getUid().equals(uid)) {
				if (index++ < begin)
					continue;
				
				Item item = ((ItemLocalImpl)ItemManager.getAdapter()).
					itemMap.get(Item.getKey(pi.getPlatform(), pi.getItemId()));
				
				if (item == null) {
					continue;
				}
				
				AppItem appItem = new AppItem();
				try {
					BeanUtils.copyProperties(appItem, item);
					BeanUtils.copyProperties(appItem, pi);
					lst.add(appItem);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
//		if (begin + lst.size() >= itemMap.size())
//			lst.add(new AppItem());
		
		return lst.toArray(new AppItem[lst.size()]);
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
				(StringUtils.isEmpty(appItem.getPriceDisplay()) ||
				appItem.getPriceDisplay().equals(appItem.getPriceDisplay()) == false)) {
				appItem.setPriceDisplay(appItem.getPriceDisplay());
				modified = true;
			}
			
			if (StringUtils.isNotEmpty(appItem.getPrice()) &&
				(StringUtils.isEmpty(appItem.getPrice()) ||
				appItem.getPrice().equals(appItem.getPrice()) == false)) {
				appItem.setPrice(appItem.getPrice());
				modified = true;
			}

			if (modified) {
				item.setUpdateTime(System.currentTimeMillis());
				if (ItemManager.updateItem(item) == false)
					return false;
			}
		}
		
		PurchasedItem pi = PurchasedItemManager.getPurchasedItem(
				uid, 
				appItem.getPlatform(), 
				appItem.getBuyerId(), 
				appItem.getItemId());
		
		if (pi == null) {
			pi = new PurchasedItem();
			BeanUtils.copyProperties(pi, appItem);
			pi.setUid(uid);
			pi.setBuyPrice(appItem.getPrice());
			pi.setCreateTime(curr);
			pi.setUpdateTime(curr);
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
				pi.setNote(appItem.getNote());
				modified = true;
			}
			if (StringUtils.isNotEmpty(appItem.getBuyPrice()) &&
				(StringUtils.isEmpty(pi.getBuyPrice()) ||
				pi.getBuyPrice().equals(appItem.getBuyPrice()) == false)) {
				pi.setBuyPrice(appItem.getBuyPrice());
				modified = true;
			}
			if (modified) {
				pi.setUpdateTime(System.currentTimeMillis());
				return PurchasedItemManager.updateItem(pi);
			}
		}
		
		return false;
	}

	@Override
	public long totalSize(String uid) throws Exception {
		Map<String, PurchasedItem> itemMap = 
			((PurchasedItemLocalImpl)PurchasedItemManager.getAdapter()).itemMap;
		Iterator<PurchasedItem> iter = itemMap.values().iterator();
		long total = 0;
		
		while (iter.hasNext()) {
			PurchasedItem pi = iter.next();
			if (pi.getUid().equals(uid)) {
				Item item = ((ItemLocalImpl)ItemManager.getAdapter()).
					itemMap.get(Item.getKey(pi.getPlatform(), pi.getItemId()));
				
				if (item == null) {
					continue;
				}
				
				total++;
			}
		}
		
		return total;
	}
}
