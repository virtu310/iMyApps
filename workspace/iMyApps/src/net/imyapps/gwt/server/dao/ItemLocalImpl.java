package net.imyapps.gwt.server.dao;

import java.util.HashMap;


import java.util.Map;

import net.imyapps.common.Item;

import org.apache.commons.beanutils.BeanUtils;

import com.lm.keyrow.KeyRowException;
import com.lm.keyrow.KeyRowUtils;

public class ItemLocalImpl implements ItemAdapter {
	Map<String, Item> itemMap = new HashMap<String, Item>();
	
	public Item get(String platform, String itemId) throws Exception {
		Item item = itemMap.get(Item.getKey(platform, itemId));
		if (item != null) {
			Item retItem = new Item();
			BeanUtils.copyProperties(retItem, item);
			return retItem;
		}
		
		return null;
	}
	
	public void put(Item item) throws KeyRowException {
		itemMap.put(KeyRowUtils.getKeyValue(item), item);
	}
}
