package net.imyapps.gwt.shared;

import net.imyapps.common.Account;
import net.imyapps.common.Login;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is note translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 * 
	 * In this example, we only require that the name is at least four
	 * characters. In your application, you can use more complex checks to ensure
	 * that usernames, passwords, email addresses, URLs, and other fields have the
	 * proper syntax.
	 * 
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidLogin(Login login) {
		if (login == null || 
			login.getLoginName() == null || 
			login.getLoginName().length() < 3 ||
			login.getPassword() == null || 
			login.getPassword().length() < 4) {
			return false;
		}
		return true;
	}
	
	public static boolean isValidSignupAccount(Account account) {
		if (account == null || 
			account.getLoginName() == null || 
			account.getLoginName().length() < 5 ||
			account.getLoginName().length() > 32 ||
			account.getEmail() == null ||
			account.getEmail().length() < 10 ||
			account.getEmail().indexOf("@") <= 0 ||
			account.getEmail().indexOf(".") <= 0 ||
			account.getQuestion() == null || 
			account.getQuestion().length() == 0 ||
			account.getQuestion().length() > 80 ||
			account.getAnswer() == null || 
			account.getAnswer().length() == 0 ||
			account.getAnswer().length() > 80 ||
			account.getPassword() == null || 
			account.getPassword().length() < 4 ||
			account.getPassword().length() > 32 ||
			account.getPassword().equals(account.getLoginName())) {
			return false;
		}
		return true;
	}
	
	public static boolean isValidUpdateAccount(Account account) {
		if (account == null || 
			account.getUid() == null || 
			account.getUid().length() < 3 ||
			(account.getPassword() != null && 
			account.getPassword().length() != 0 &&
			account.getPassword().length() < 4)) {
			return false;
		}
		return true;
	}
}
