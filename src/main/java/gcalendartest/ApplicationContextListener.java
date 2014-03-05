package gcalendartest;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class ApplicationContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// define global objects
		
		ServletContext context = sce.getServletContext();
		JsonFactory jsonFactory = new JacksonFactory();
		HttpTransport transport = new NetHttpTransport();
		
		context.setAttribute(Constants.LOGIN_INFO_FACTORY, 
				new LoginInfoFactory(
						new LoginHelperImpl(context, jsonFactory),
						transport, jsonFactory));
		context.setAttribute(Constants.CALENDAR_HELPER,
				new CalendarHelper(transport, jsonFactory));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
