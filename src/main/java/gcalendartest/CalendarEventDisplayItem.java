package gcalendartest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;

public class CalendarEventDisplayItem {
	private final static SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final com.google.api.services.calendar.model.Event event;
	
	public CalendarEventDisplayItem(com.google.api.services.calendar.model.Event event) {
		this.event = event;
	}
	
	public String getSummary() {
		return event.getSummary();
	}
	
	public String getTimeDescription() {
		EventDateTime startDT = event.getStart();
		EventDateTime endDT = event.getEnd();
		// this field is populated if event has time
		if(startDT.getDateTime() != null) {
			return String.format("Start: %s End: %s", formatDateTime(startDT.getDateTime()), formatDateTime(endDT.getDateTime()));
		}
		DateTime startDate = startDT.getDate();
		DateTime endDate = endDT.getDate();
		// special case for 1 full day event - just show the one date instead of start end
		final long oneDayTime = 1000 * 60 * 60 * 24;
		if(endDate.getValue() - startDate.getValue() == oneDayTime)
			return String.format("Date: %s", startDate);
		
		return String.format("Start: %s End: %s", startDate, endDate);
	}
	
	public long getStartTime() {
		EventDateTime startDT = event.getStart();
		if(startDT.getDateTime() != null)
			return startDT.getDateTime().getValue();
		return startDT.getDate().getValue();
	}
	
	private static String formatDateTime(DateTime googleDateTime) {
		return formatDateTime.format(new Date(googleDateTime.getValue()));
	}
	
	public String toPlainText() {
		return String.format("Summary: %s\nDate/Time: %s", getSummary(), getTimeDescription());
	}
}
class CalendarEventDisplayData {
	private final List<CalendarEventDisplayItem> items;
	
	public CalendarEventDisplayData(com.google.api.services.calendar.model.Events events) {
		items = new ArrayList<CalendarEventDisplayItem>(events.size());
		for(com.google.api.services.calendar.model.Event event : events.getItems()) {
			items.add(new CalendarEventDisplayItem(event));
		}
		Collections.sort(items, new Comparator<CalendarEventDisplayItem>() {
			@Override
			public int compare(CalendarEventDisplayItem o1,
					CalendarEventDisplayItem o2) {
				if(o1.getStartTime() < o2.getStartTime())
					return -1;
				if(o1.getStartTime() > o2.getStartTime())
					return 1;
				return 0;
			}
		});
	}
	
	public List<CalendarEventDisplayItem> getItems() {
		return items;
	}
	
	public String getEmailUrl() throws UnsupportedEncodingException {
		String template = "http://mail.google.com/mail/?view=cm&fs=1&to=&su=&body=%s&ui=1";
		StringBuilder builder = new StringBuilder();
		for(CalendarEventDisplayItem item : getItems()) {
			builder.append(item.toPlainText()).append("\n\n");
		}
		return String.format(template, URLEncoder.encode(builder.toString(), "UTF-8"));
	}
}