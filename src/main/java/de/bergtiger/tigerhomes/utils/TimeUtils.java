package de.bergtiger.tigerhomes.utils;

import de.bergtiger.tigerhomes.utils.lang.Lang;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

public class TimeUtils {

	/**
	 * LocalDateTime as String formatted with default time format.
	 * @param t to format
	 * @return String
	 */
	public static String formatted(LocalDateTime t) {
		return formatted(t, Lang.FORMAT_TIME.get());
	}

	/**
	 * LocalDateTime as String formatted with given time format.
	 * @param t to format
	 * @param format String representing how t should be represented
	 * @return String
	 */
	public static String formatted(LocalDateTime t, String format) {
		if (t != null) {
			try {
				return DateTimeFormatter.ofPattern(format).format(t);
			} catch (Exception e) {
				TigerLogger.log(Level.SEVERE, String.format("formatted: could not format LocalDateTime(%s) with format(%s)", t, format), e);
			}
		} else {
			TigerLogger.log(Level.WARNING, "formatted: t is null");
		}
		return "-";
	}

}
