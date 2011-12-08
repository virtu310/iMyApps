package net.imyapps.gwt.server.restlet;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.imyapps.common.Account;
import net.imyapps.common.AppItem;
import net.imyapps.common.AppleiTunes;
import net.imyapps.common.AppleiTunesResource;
import net.imyapps.gwt.server.AppItemManager;
import net.imyapps.gwt.server.SessionManager;
import net.imyapps.utils.IpaXMLParser;

import org.restlet.resource.Post;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

public class AppleiTunesResourceImpl extends ServerResource implements AppleiTunesResource {
	static Logger logger = LoggerFactory.getLogger(AppleiTunesResourceImpl.class);
	
	@Post
	public String[] upload(AppleiTunes appleiTunes) {
		try {
			Account account = SessionManager.getLoginUser(this);
			if (account == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				return null;
			}
			
			/*
			Form form = new Form(getRequest().getEntity());
			String xml = form.getFirstValue("xml");
			*/
			IpaXMLParser parser = new IpaXMLParser();
			parser.parse(appleiTunes.getXml(), "UTF-8");
			
			ArrayList<String> lst = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();

			for (AppItem appItem : parser.getItems()) {
				if (AppItemManager.add(account.getUid(), appItem)) {
					sb.setLength(0);
					sb.append(appItem.getBuyerId());
					sb.append(',').append(appItem.getItemId());
					lst.add(sb.toString());
				}
			}
			
			return lst.toArray(new String[lst.size()]);
		}
		catch (Exception e) {
			logger.error("Occur exception:", e);
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
			return null;
		}
	}

}
