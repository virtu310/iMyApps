package net.imyapps.gwt.client;

import net.imyapps.common.Account;
import net.imyapps.gwt.shared.FieldVerifier;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Preference;
import org.restlet.client.resource.Result;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UpdateAccountPage {
	static UpdateAccountPage instance;

	Panels panels;
	Panel mainPanel;

	public static void destroy() {
		if (instance != null && instance.mainPanel != null)
			instance.mainPanel.clear();
		
		instance = null;
	}
	
	public static void switchTo(Panels panels) {
		if (instance == null) {
			instance = new UpdateAccountPage(panels);
		}
		
		panels.getMain().clear();
		panels.getMain().add(instance.mainPanel);
		instance.renew();
	}

	public UpdateAccountPage(Panels panels) {
		this.panels = panels;
		initPage();
	}

	public void initPage() {
		mainPanel = new VerticalPanel();
		renew();
	}

	public void renew() {
		final PasswordTextBox password = new PasswordTextBox();
		final PasswordTextBox vpassword = new PasswordTextBox();
		final TextBox question = new TextBox();
		final TextBox answer = new TextBox();
		
		VerticalPanel vp = new VerticalPanel();
		FlexTable flexTable = new FlexTable();

		flexTable.setWidget(0, 0, new Label(IMyApps.msgs.lb_loginName()));
		flexTable.setWidget(0, 1, new Label(IMyApps.loginAccount.getLoginName()));

		flexTable.setWidget(1, 0, new Label(IMyApps.msgs.lb_password()));
		flexTable.setWidget(1, 1, password);

		flexTable.setWidget(2, 0, new Label(IMyApps.msgs.lb_verify()));
		flexTable.setWidget(2, 1, vpassword);

		flexTable.setWidget(3, 0, new Label(IMyApps.msgs.lb_question()));
		flexTable.setWidget(3, 1, question);
		
		flexTable.setWidget(4, 0, new Label(IMyApps.msgs.lb_answer()));
		flexTable.setWidget(4, 1, answer);
		
		Button bnCancel = new Button(IMyApps.msgs.bn_cancel());
		Button bnUpdate = new Button(IMyApps.msgs.bn_ok());
		HorizontalPanel pnBtn = new HorizontalPanel();
		pnBtn.add(bnUpdate);
		pnBtn.add(bnCancel);
		flexTable.setWidget(5, 0, pnBtn);
		flexTable.getFlexCellFormatter().setColSpan(5, 0, 2);
		
		class UpdateHandler implements ClickHandler, KeyUpHandler {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					doUpdate();
				}
			}

			@Override
			public void onClick(ClickEvent event) {
				doUpdate();
			}
			
			
			public void doUpdate() {
				panels.clearMessage();
				
				boolean updatePassword = false;
				if (vpassword.getText().length() > 0 &&
					password.getText().equals(vpassword.getText()) == false) {
					panels.appendError(IMyApps.msgs.err_verify_fail());
					return;
				}
				
				Account account = new Account();
				account.setUid(IMyApps.loginAccount.getUid());
				account.setPassword(vpassword.getText());
				account.setQuestion(question.getText());
				account.setAnswer(answer.getText());
				
				if (!FieldVerifier.isValidUpdateAccount(account)) {
					panels.appendError(IMyApps.msgs.err_info_incorrupt());
					return;
				}
				
				/*
				String seed = String.valueOf(System.currentTimeMillis());
				String verify;
				try {
					String key = IMyApps.loginAccount.getPassword() + '+' + seed;
					MessageDigest md5 = MessageDigest.getInstance("MD5");
					md5.update(key.getBytes());
					verify = Base64Coder.encodeLines(md5.digest());
				}
				catch (Exception e) {
					Window.alert(e.getMessage());
					panels.appendError(IMyApps.msgs.err_client());
					return;
				}
				
				IMyApps.signupResource.getClientResource().setReference(API.account(seed, verify));
				*/
				
				IMyApps.signupResource.getClientResource().setReference(API.account());
				IMyApps.signupResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
						Preference<MediaType>(MediaType.APPLICATION_JSON));
						//Preference<MediaType>(MediaType.APPLICATION_JAVA_OBJECT_GWT));
				
				IMyApps.signupResource.update(account, new Result<Void>() {

					public void onFailure(Throwable caught) {
						// Handle the error
						Window.alert(caught.getMessage());
					}

					public void onSuccess(Void result) {
						Window.alert(IMyApps.msgs.msg_successful());
						ListPage.switchTo(panels);
					}
					
				});
			}
		}
		
		bnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				panels.clearMessage();
				ListPage.switchTo(panels);
			}
		});
		
		vp.add(flexTable);
		UpdateHandler updateHandler = new UpdateHandler();
		bnUpdate.addClickHandler(updateHandler);
		password.addKeyUpHandler(updateHandler);
		vpassword.addKeyUpHandler(updateHandler);
		question.addKeyUpHandler(updateHandler);
		answer.addKeyUpHandler(updateHandler);
		
		panels.getMain().add(vp);
	}
}
