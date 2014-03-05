package gcalendartest;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class ServletUtils {
	public static void sendRedirect(HttpServlet servlet, HttpServletResponse response, String contextUrl) throws IOException {
		response.sendRedirect(servlet.getServletContext().getContextPath() + contextUrl);
	}
	public static void pageForward(PageContext pageContext, String contextUrl) throws JspException {
		try {
			pageContext.forward(Constants.LOGIN_SERVLET);
		} catch (ServletException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}
	}
	
	private static final Object GLOBAL_LOCK = new Object();
	public static SessionLock getSessionLock(HttpServletRequest request) {
		SessionLock lock = (SessionLock)request.getSession().getAttribute(Constants.SESSION_LOCK);
		if(lock == null) {
			synchronized (GLOBAL_LOCK) {
				lock = (SessionLock)request.getSession().getAttribute(Constants.SESSION_LOCK);
				if(lock == null) {
					lock = new SessionLock();
					request.getSession().setAttribute(Constants.SESSION_LOCK, lock);
				}
			}
		}
		return lock;
	}
}
// immutable class ensures double-check locking works
final class SessionLock {
}
