package gcalendartest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(LogoutServlet.class.getName());
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionLock lock = ServletUtils.getSessionLock(request);
		synchronized(lock) {
			LoginInfo loginInfo = (LoginInfo)request.getSession().getAttribute(Constants.LOGIN_INFO);
			if(loginInfo == null || false == loginInfo.isLoggedIn()) {
				log.warning("Already logged out. Redirect to login");
				ServletUtils.sendRedirect(this, response, Constants.LOGIN_SERVLET);
				return;
			}
			
			try {
				loginInfo.logout();
				log.info("Logged out");
			} catch(IOException e) {
				log.log(Level.SEVERE, "Log out failed", e);
			}

			request.getSession().removeAttribute(Constants.LOGIN_INFO);

			ServletUtils.sendRedirect(this, response, Constants.LOGIN_SERVLET);			
		}
	}
}
