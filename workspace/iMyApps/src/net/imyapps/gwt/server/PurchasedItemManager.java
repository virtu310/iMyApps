package net.imyapps.gwt.server;

import net.imyapps.common.PurchasedItem;
import net.imyapps.gwt.server.dao.PurchasedItemAdapter;


public class PurchasedItemManager {
	private static PurchasedItemAdapter adapter;
	
	public static 
	PurchasedItemAdapter setAdapter(PurchasedItemAdapter newAdapter) {
		PurchasedItemAdapter oldAdapter = adapter;
		adapter = newAdapter;
		return oldAdapter;
	}
	
	public static PurchasedItemAdapter getAdapter() {
		if (adapter == null)
			throw new RuntimeException("PurchasedItemAdapter must be setup!");
		
		return adapter;
	}
	
	public static PurchasedItem getPurchasedItem(String uid,
												 String platform, 
												 String buyer, 
												 String itemId) {
		try {
			return getAdapter().get(uid, platform, buyer, itemId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean putItem(PurchasedItem item) {
		try {
			if (getAdapter().get(item.getUid(), 
								 item.getPlatform(), 
								 item.getBuyerId(), 
								 item.getItemId()) != null) {
				return false;
			}
			
			getAdapter().put(item);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean updateItem(PurchasedItem item) {
		try {
			getAdapter().put(item);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
