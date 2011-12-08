package net.imyapps.gwt.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.imyapps.Configure;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthMeServlet extends HttpServlet {
	private static final long serialVersionUID = 93130511911837095L;
	
	static Logger logger = LoggerFactory.getLogger(AuthMeServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String authcode = req.getParameter("authcode");
		String uid = req.getParameter("uid");
		if (StringUtils.isEmpty(authcode) || StringUtils.isEmpty(uid)) {
			/*
			StringBuilder sb = new StringBuilder();
			sb.append("<script>top.location=\"");
			sb.append("http://imyapps.appspot.com");
			sb.append("\";</script>");
			resp.getOutputStream().write(sb.toString().getBytes());
			*/
			resp.sendRedirect(Configure.HomeURL);
			return;
		}
		
		String realAuthcode = AccountManager.getAuthCode(uid);
		if (realAuthcode != null && realAuthcode.equals(authcode)) {
			AccountManager.authMe(uid);
			resp.getWriter().println("<html><head>" +
					"<META HTTP-EQUIV='refresh' CONTENT='3;URL=" + 
					Configure.HomeURL + "'>" +
					"</head><body>Auth successful!</body></html>");
		}
		else {
			resp.getWriter().println("<html><head>" +
					"<META HTTP-EQUIV='refresh' CONTENT='3;URL=" + 
					Configure.HomeURL + "'>" +
					"</head><body>Auth failed!</body></html>");
		}
		
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
