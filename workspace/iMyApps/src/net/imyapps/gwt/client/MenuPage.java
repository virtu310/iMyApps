package net.imyapps.gwt.client;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Preference;
import org.restlet.client.resource.Result;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;

public class MenuPage {
	static MenuPage instance;
	
	Panels panels;
	Panel mainPanel;
	
	public static void destroy() {
		if (instance != null && instance.mainPanel != null)
			instance.mainPanel.clear();
		
		instance = null;
	}
	
	public static void switchTo(Panels panels) {
		if (instance == null) {
			instance = new MenuPage(panels);
		}
		
		panels.getTop().add(instance.mainPanel);
	}
	
	public MenuPage(Panels panels) {
		this.panels = panels;
		initPage();
	}
	
	public void initPage() {
		Button bnList = new Button(IMyApps.msgs.bn_list());
		Button bnSearch = new Button(IMyApps.msgs.bn_search());
		Button bnStatistic = new Button(IMyApps.msgs.bn_statistic());
		Button bnProfile = new Button(IMyApps.msgs.bn_profile());
		Button bnLogout = new Button(IMyApps.msgs.bn_logout());
		
		bnList.addStyleName("menuButton");
		bnSearch.addStyleName("menuButton");
		bnStatistic.addStyleName("menuButton");
		bnProfile.addStyleName("menuButton");
		bnLogout.addStyleName("menuButton");
		
		bnList.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ListPage.switchTo(panels);
			}
		});
		
		bnSearch.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SearchPage.switchTo(panels);
			}
		});

		bnStatistic.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StatisticPage.switchTo(panels);
			}
		});
		
		bnProfile.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UpdateAccountPage.switchTo(panels);
			}
		});
		
		bnLogout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Window.confirm(IMyApps.msgs.q_sure_to_logout()) == false) {
					return;
				}
				
				IMyApps.loginResource.getClientResource().setReference(API.logout());
				IMyApps.loginResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
						Preference<MediaType>(MediaType.APPLICATION_JSON));
				
				IMyApps.loginResource.logout(new Result<Void>() {
					
					public void onFailure(Throwable caught) {
						// Handle the error
						Window.alert(caught.getMessage());
					}
					
					public void onSuccess(Void v) {
						ListPage.destroy();
						SearchPage.destroy();
						StatisticPage.destroy();
						UpdateAccountPage.destroy();
						panels.clear();
						panels.appendInfo(IMyApps.msgs.pop_byebye());
						LoginPage.initPage(panels);
						//Window.Location.assign("http://imyapps.appspot.com/");
					}
					
				});
			}
		});
		
		mainPanel = new HorizontalPanel();
		mainPanel.add(bnList);
		mainPanel.add(bnSearch);
		mainPanel.add(bnStatistic);
		mainPanel.add(bnProfile);
		mainPanel.add(bnLogout);
	}
}
