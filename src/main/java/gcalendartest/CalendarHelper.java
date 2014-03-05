package gcalendartest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;

public class CalendarHelper {
	
	private HttpTransport transport;
	private JsonFactory jsonFactory;
	
	public CalendarHelper(HttpTransport transport, JsonFactory jfac) {
		this.transport = transport;
		this.jsonFactory = jfac;
	}

	public CalendarEventDisplayData getData(LoginInfo loginInfo, String appName) throws IOException {
		Calendar service = new Calendar.Builder(
							transport, jsonFactory, loginInfo.getCredential())
							.setApplicationName(appName).build();
		com.google.api.services.calendar.model.CalendarList list = 
				service.calendarList().list().execute();

		CalendarListEntry cle = list.getItems().get(0);
		String calId = cle.getId();


		final long twoWeeksTime = 1000 * 60 * 60 * 24 * 14;
		Date timeNow = new Date();
		Date timeAfter2Weeks = new Date(timeNow.getTime() + twoWeeksTime);

		com.google.api.services.calendar.model.Events events = 
				service.events().list(calId)
				.setTimeMin(new DateTime(timeNow))
				.setTimeMax(new DateTime(timeAfter2Weeks)).execute();
	
		return new CalendarEventDisplayData(events);
	}
}
