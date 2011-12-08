package net.imyapps.gwt.server.dao;

import net.imyapps.common.Item;

public interface ItemAdapter {
	public Item get(String platform, String itemId) throws Exception;
	public void put(Item item) throws Exception;
}
