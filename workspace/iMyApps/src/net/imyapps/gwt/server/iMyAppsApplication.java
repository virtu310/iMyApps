package net.imyapps.gwt.server;

import java.io.IOException;
import java.util.List;


import net.imyapps.gwt.server.dao.AccountKeyRowImpl;
import net.imyapps.gwt.server.dao.AppItemKeyRowImpl;
import net.imyapps.gwt.server.dao.ItemKeyRowImpl;
import net.imyapps.gwt.server.dao.PurchasedItemKeyRowImpl;
import net.imyapps.gwt.server.dao.SessionKeyRowImpl;
import net.imyapps.gwt.server.restlet.AccountResourceImpl;
import net.imyapps.gwt.server.restlet.AppItemResourceImpl;
import net.imyapps.gwt.server.restlet.AppleiTunesResourceImpl;
import net.imyapps.gwt.server.restlet.ListAppsResourceImpl;
import net.imyapps.gwt.server.restlet.LoginResourceImpl;
import net.imyapps.gwt.server.restlet.SearchAppItemResourceImpl;
import net.imyapps.gwt.server.restlet.StatisticResourceImpl;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.converter.DefaultConverter;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Directory;
import org.restlet.resource.UniformResource;
import org.restlet.routing.Router;
import org.restlet.security.Authenticator;

public class iMyAppsApplication extends Application {
	class MyAuthenticator extends Authenticator
	{
		public MyAuthenticator(Context context) {
			super(context);
		}

		@Override
		protected boolean authenticate(Request request, Response response) {
			String sid = SessionManager.retrieveSID(request);
			if (sid != null) {
				SessionItem si = SessionManager.findSession(sid);
				if (si != null)
					return true;
			}
			response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			//throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED);
			return false;
		}	
	}
	
	@Override
	public Restlet createInboundRoot() {
		init();
		
		// 設定認證機制
		/*
		DigestAuthenticator accountResource = 
			new DigestAuthenticator(null, 
									"iMyAppsRealm", 
									"iMyAppsSecretServerKey");
		
		accountResource.setWrappedVerifier(new LocalVerifier() {
			@Override
			public char[] getLocalSecret(String identifier) {
				Account account = 
					AccountManager.getAccountByLoginName(identifier);
				
				if (account != null)
					return account.getPassword().toCharArray();
				
				return null;
			}
		});
		*/
		Authenticator accountResource = new MyAuthenticator(null);
		accountResource.setNext(AppleiTunesResourceImpl.class);

		Authenticator appItemResource = new MyAuthenticator(null);
		appItemResource.setNext(AppItemResourceImpl.class);

		Router router = new Router(getContext());
		
		router.attach("/API/account", AccountResourceImpl.class);
		router.attach("/API/account/U/{uid}", AccountResourceImpl.class);
		router.attach("/API/account/N/{loginname}", AccountResourceImpl.class);		
		router.attach("/API/itunes", accountResource);
		router.attach("/API/appitem", appItemResource);
		router.attach("/API/search/appitem", SearchAppItemResourceImpl.class);
		router.attach("/API/login", LoginResourceImpl.class);
		router.attach("/API/statistic/appitem", StatisticResourceImpl.class);
		router.attach("/API/listapp", ListAppsResourceImpl.class);
		router.attach("/list/allapps", ListAppsResourceImpl.class);
		
		router.attach("/", new Directory(getContext(), "war:///"));
		
		return router;
	}

	public static void init() {
		// setup Adapter
		AccountManager.setAdapter(new AccountKeyRowImpl());
		ItemManager.setAdapter(new ItemKeyRowImpl());
		PurchasedItemManager.setAdapter(new PurchasedItemKeyRowImpl());
		AppItemManager.setAdapter(new AppItemKeyRowImpl());
		SessionManager.setSessionAdapter(new SessionKeyRowImpl());
		
		replaceDefaultConverter();
		//if (Configure.LocalMode)
		//	InstanceManager.getInstance().initTestDB();
	}
	
	public static void replaceDefaultConverter() {
		List<ConverterHelper> chs = 
			Engine.getInstance().getRegisteredConverters();
		
        DefaultConverter dch = null;
        for (ConverterHelper ch : chs) {
        	if (ch instanceof DefaultConverter) {
        		dch = (DefaultConverter) ch;
        		break;
        	}
        }
        if (dch != null) {
        	chs.remove(dch);
        	chs.add(new DefaultConverter() {
        		@Override
        		public Representation toRepresentation(Object source,
        				Variant target, UniformResource resource)
        				throws IOException {
        	        Representation result = null;

        	        if (source instanceof String) {
        	            result = new StringRepresentation(
        	            		(String) source, 
        	            		target
        	                    .getMediaType(), 
        	                    Language.DEFAULT, 
        	                    CharacterSet.UTF_8);
        	        } else {
        	        	result = 
        	        		super.toRepresentation(source, target, resource);
        	        }
        	        
        	        return result;
        		}
        	});
        }
	}

}
