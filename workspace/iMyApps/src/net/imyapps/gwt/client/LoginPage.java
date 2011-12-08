package net.imyapps.gwt.client;

import java.util.Date;

import net.imyapps.common.Account;
import net.imyapps.common.Login;
import net.imyapps.gwt.shared.FieldVerifier;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Preference;
import org.restlet.client.resource.Result;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPage {
	public static void initPage(final Panels panels) {
		final TextBox loginName = new TextBox();
		final PasswordTextBox password = new PasswordTextBox();
		final CheckBox rememberLogin = new CheckBox();
		
		VerticalPanel vp = new VerticalPanel();
		
		VerticalPanel vpHelp = new VerticalPanel();
		vpHelp.add(new Label(IMyApps.msgs.msg_help()));
		vpHelp.add(new HTML("<br /><br />"));
		vp.add(vpHelp);
		
		VerticalPanel pName = new VerticalPanel();
		pName.add(new Label(IMyApps.msgs.lb_loginName()));
		pName.add(loginName);
		vp.add(pName);
		
		VerticalPanel pPass = new VerticalPanel();
		pPass.add(new Label(IMyApps.msgs.lb_password()));
		pPass.add(password);
		vp.add(pPass);
		
		// remember login state
		String v_rememberLogin = Cookies.getCookie("RememberLogin");
		if (v_rememberLogin != null && Boolean.parseBoolean(v_rememberLogin)) {
			rememberLogin.setValue(true);
		}
		else {
			rememberLogin.setValue(false);
		}
		HorizontalPanel pRemember = new HorizontalPanel();
		pRemember.add(new Label(IMyApps.msgs.lb_rememberLogin()));
		pRemember.add(rememberLogin);
		vp.add(pRemember);
		
		rememberLogin.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Date now = new Date();
				long nowLong = now.getTime();
				nowLong = nowLong + (1000 * 60 * 60 * 24 * 14); //14 days
				now.setTime(nowLong);
				
				Cookies.setCookie("RememberLogin", 
								  String.valueOf(rememberLogin.getValue()), 
								  now);
			}
		});
		
		Button bnLogin = new Button(IMyApps.msgs.bn_login());
		Anchor forgetpass = new Anchor(IMyApps.msgs.bn_forget_passwd());
		forgetpass.addStyleName("gwt-label");
		
		Anchor signup = new Anchor(IMyApps.msgs.bn_register());
		signup.addStyleName("gwt-Label");
		
		vp.add(new HTML("&nbsp;"));
		vp.add(bnLogin);
		vp.add(new HTML("&nbsp;"));
		vp.add(signup);
		vp.add(forgetpass);
		
		class LoginHandler implements ClickHandler, KeyUpHandler {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					doLogin();
				}
			}

			@Override
			public void onClick(ClickEvent event) {
				doLogin();
			}
			
			
			public void doLogin() {
				panels.clearMessage();
				
				Login login = new Login();
				login.setLoginName(loginName.getText());
				login.setPassword(password.getText());
				
				if (!FieldVerifier.isValidLogin(login)) {
					panels.error.setText(IMyApps.msgs.err_name_pass_fail());
					return;
				}
				
				IMyApps.loginResource.getClientResource().setReference(API.login());
				IMyApps.loginResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
						Preference<MediaType>(MediaType.APPLICATION_JSON));
				
				IMyApps.loginResource.loginByPost(login, new Result<Account>() {
					
					public void onFailure(Throwable caught) {
						// Handle the error
						Window.alert(IMyApps.msgs.err_login_fail());
					}
					
					public void onSuccess(Account result) {
						// TODO Auto-generated method stub
						IMyApps.loginAccount = result;
						IMyApps.loginAccount.setPassword(password.getText());
						
						if (result != null) {
							panels.clear();
							ListPage.switchTo(panels);
							//StatisticPage.switchTo(panels);
							MenuPage.switchTo(panels);
							
							// for preload
							IMyApps.preloadResource.getClientResource().setReference(API.preload());
							IMyApps.preloadResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
									Preference<MediaType>(MediaType.APPLICATION_JSON));
							IMyApps.preloadResource.postHtml(new Result<String>() {
								@Override
								public void onSuccess(String result) {
								}
								@Override
								public void onFailure(Throwable caught) {
								}
							});
						}
						else
							Window.alert(IMyApps.msgs.err_login_fail());
					}
					
				});
			}
		}

		signup.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				panels.clear();
				SignupPage.initPage(panels);
			}
		});
		
		forgetpass.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				panels.clear();
				ForgetPasswordPage.initPage(panels);
			}
		});
		
		LoginHandler loginHandler = new LoginHandler();
		bnLogin.addClickHandler(loginHandler);
		loginName.addKeyUpHandler(loginHandler);
		password.addKeyUpHandler(loginHandler);
		
		panels.getMain().add(vp);
		loginName.setFocus(true);
	}
}
