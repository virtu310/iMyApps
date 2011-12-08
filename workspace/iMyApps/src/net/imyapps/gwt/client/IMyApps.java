package net.imyapps.gwt.client;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Preference;
import org.restlet.client.resource.Result;

import net.imyapps.common.Account;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class IMyApps implements EntryPoint {
	static ResponseMessages msgs = GWT.create(ResponseMessages.class);
	static Account loginAccount;
	
	static AccountResourceProxy signupResource = 
		GWT.create(AccountResourceProxy.class);
	static LoginResourceProxy loginResource = 
		GWT.create(LoginResourceProxy.class);
	static AppItemResourceProxy appItemResource = 
		GWT.create(AppItemResourceProxy.class);
	static SearchAppItemResourceProxy searchAppItemResource = 
		GWT.create(SearchAppItemResourceProxy.class);
	static StatisticResourceProxy statisticResource = 
		GWT.create(StatisticResourceProxy.class);
	static ListAppResourceProxy listAppResource = 
		GWT.create(ListAppResourceProxy.class);
	static ListAppResourceProxy preloadResource = 
		GWT.create(ListAppResourceProxy.class);

	@Override
	public void onModuleLoad() {
		final Panels panels = new Panels();
		panels.setTop(RootPanel.get("topContainer"));
		panels.setLeft(RootPanel.get("leftContainer"));
		panels.setRight(RootPanel.get("rightContainer"));
		panels.setBottom(RootPanel.get("bottomContainer"));
		panels.setMessage(RootPanel.get("messageLabelContainer"));
		
		Label errorLabel = new Label();
		errorLabel.setStyleName("labelError");
		panels.addError(errorLabel);
		
		Label infoLabel = new Label();
		infoLabel.setStyleName("labelInfo");
		panels.addInfo(infoLabel);
		
		String page = Window.Location.getParameter("page");
		if (page != null) {
			if (page.equalsIgnoreCase("Signup"))
				SignupPage.initPage(panels);
			else
				LoginPage.initPage(panels);
		}
		else {
			// check is remember login
			String rememberLogin = Cookies.getCookie("RememberLogin");
			if (rememberLogin != null && Boolean.parseBoolean(rememberLogin)) {
				IMyApps.loginResource.getClientResource().setReference(API.remember_login());
				IMyApps.loginResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
						Preference<MediaType>(MediaType.APPLICATION_JSON));
				
				IMyApps.loginResource.login(new Result<Account>() {
					
					@Override
					public void onSuccess(Account result) {
						loginAccount = result;
						ListPage.switchTo(panels);
						MenuPage.switchTo(panels);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						LoginPage.initPage(panels);						
					}
				});
			}
			else {
				LoginPage.initPage(panels);
			}
		}
	}

}
