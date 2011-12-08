package net.imyapps.gwt.server;

import net.imyapps.common.Item;
import net.imyapps.gwt.server.dao.ItemAdapter;

public class ItemManager {
	private static ItemAdapter adapter;
	
	public static ItemAdapter setAdapter(ItemAdapter newAdapter) {
		ItemAdapter oldAdapter = adapter;
		adapter = newAdapter;
		return oldAdapter;
	}
	
	public static ItemAdapter getAdapter() {
		if (adapter == null)
			throw new RuntimeException("ItemAdapter must be setup!");
		
		return adapter;
	}
	
	public static Item getItem(String platform, String itemId) {
		try {
			return getAdapter().get(platform, itemId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean putItem(Item item) {
		try {
			if (getAdapter().get(item.getPlatform(), item.getItemId()) != null){
				return false;
			}
			
			getAdapter().put(item);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean updateItem(Item item) {
		try {
			getAdapter().put(item);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
