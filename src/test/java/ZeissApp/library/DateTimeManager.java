package ZeissApp.library;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeManager {

	private DateTimeManager() {
	}
	private static ThreadLocal<String> dateTime = new ThreadLocal<String>();
	public static String getDateTime() {
		timeStamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
		return timeStamp;
	}
	public static void setDateTime(String dateTime2) {
		dateTime.set(dateTime2);
	}
	public static void unload() {
		dateTime.remove();
	}
	public static String timeStamp;
}
