package gcalendartest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.TokenResponseException;

@SuppressWarnings("serial")
public class LoginCallbackServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(LoginCallbackServlet.class.getName());
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		LoginInfo loginInfo = (LoginInfo)request.getSession().getAttribute(Constants.LOGIN_INFO);
		if(loginInfo == null) {
			log.warning("Reached callback page but login object not in session. Redirect to login again.");
			ServletUtils.sendRedirect(this, response, Constants.LOGIN_SERVLET);
			return;
		}
		
	    StringBuffer fullUrlBuf = request.getRequestURL();
	    if (request.getQueryString() != null) {
	      fullUrlBuf.append('?').append(request.getQueryString());
	    }
	    AuthorizationCodeResponseUrl loginResponse =
	        new AuthorizationCodeResponseUrl(fullUrlBuf.toString());
	    		
		if(loginResponse.getState() == null
				|| false == loginResponse.getState().equals(loginInfo.getStateAndReset())) {
			redoLogin("Invalid state", request, response);
			return;
		}
		
		if(loginResponse.getError() != null) {
			String msg = loginResponse.getErrorDescription() != null ? loginResponse.getErrorDescription() : loginResponse.getError();
			redoLogin(msg, request, response);
			return;
		}
		
		try {
			loginInfo.getCredential(loginResponse.getCode());
			
			if(loginInfo.isLoggedIn() == false) {
				redoLogin("Failed to get credential from Google", request, response);
				return;
			}
			log.info("Logged in");

			ServletUtils.sendRedirect(this, response, Constants.MAIN_SERVLET);
		} catch (TokenResponseException e) {
			log.log(Level.SEVERE, "Log in failed", e);
			redoLogin("Failed to upgrade the authorization code.", request, response);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Log in failed", e);
			redoLogin("Failed to read token data from Google. ", request, response);
		}
	}
	
	private void redoLogin(String message, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.warning("redo login: " + message);
		request.setAttribute("loginStatus", "Login error: " + message);
		request.getRequestDispatcher(Constants.LOGIN_SERVLET).forward(request, response);
	}
}
