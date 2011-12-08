package net.imyapps.gwt.server.dao;

import net.imyapps.common.Item;
import com.lm.keyrow.KR;
import com.lm.keyrow.KeyRowException;

public class ItemKeyRowImpl implements ItemAdapter {
	public Item get(String platform, String itemId) throws Exception {
		return (Item) KR.get(Item.class, Item.getKey(platform, itemId));
	}
	
	public void put(Item item) throws KeyRowException {
		KR.put(item);
	}
}
