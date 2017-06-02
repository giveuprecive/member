package member.utils;

import java.text.SimpleDateFormat;

public class DateUtils {
	public static String timeSwitch(String minute) {
		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd/ HH:mm");
		long time = Long.parseLong(minute);
		return format.format(time);
	}

}
