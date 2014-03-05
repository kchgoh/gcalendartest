package gcalendartest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.http.HttpResponseException;


@SuppressWarnings("serial")
public class MainServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(MainServlet.class.getName());
	private CalendarHelper calendarHelper;
	
	@Override
	public void init() {
		calendarHelper = (CalendarHelper)getServletContext().getAttribute(Constants.CALENDAR_HELPER);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoginInfo loginInfo = (LoginInfo)request.getSession().getAttribute(Constants.LOGIN_INFO);
		if(loginInfo == null || false == loginInfo.isLoggedIn()) {
			ServletUtils.sendRedirect(this, response, Constants.LOGIN_SERVLET);
			return;
		}

		response.setContentType("text/html");

		try {
			CalendarEventDisplayData data = calendarHelper.getData(loginInfo, getServletContext().getContextPath());

			request.setAttribute("events", data.getItems());
			String emailUrl = data.getEmailUrl();
			// HTTP GET URL has size limit. check for 2KB to be conservative and warn
			if(emailUrl.getBytes(Charset.forName("UTF-8")).length > 2000)
				request.setAttribute("emailWarning", "Calendar data may be too big to send as email");
			request.setAttribute("emailUrl", emailUrl);
			
		} catch(HttpResponseException e) {
			if(e.getStatusCode() == 401) {
				request.setAttribute("status", "Unauthroized request. Try <a href='Login'>login</a> again");
			} else {
				request.setAttribute("status", "Error occurred");
			}
			log.log(Level.SEVERE, "Error getting calendar data", e);
		}

		request.getRequestDispatcher("/main.jsp").forward(request, response);
	}
}

