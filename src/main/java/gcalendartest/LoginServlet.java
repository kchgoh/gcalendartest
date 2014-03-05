package gcalendartest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(LoginServlet.class.getName());
	private LoginInfoFactory infoFactory;

	@Override
	public void init() {
		infoFactory = (LoginInfoFactory)getServletContext().getAttribute(Constants.LOGIN_INFO_FACTORY);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionLock lock = ServletUtils.getSessionLock(request);
		synchronized(lock) {
			LoginInfo loginInfo = (LoginInfo)request.getSession().getAttribute(Constants.LOGIN_INFO);
			if(loginInfo != null && loginInfo.isLoggedIn()) {
				ServletUtils.sendRedirect(this, response, Constants.MAIN_SERVLET);
				return;
			}
			loginInfo = infoFactory.newInstance();
			
			String url = loginInfo.getNewAuthorizationUrl();
			request.setAttribute("googleLoginUrl", url);
			log.info("Created login object " + url);
			request.getSession().setAttribute(Constants.LOGIN_INFO, loginInfo);
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
}
