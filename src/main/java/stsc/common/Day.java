package stsc.common;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.TimeZone;

import stsc.common.stocks.Prices;

/**
 * Day data - is a collection of information about smallest unit of input data
 * type. <br/>
 * For market trading, this smallest unit (input data type) contain next fields:
 * <br/>
 * 1. {@link LocalDate} (original date/time of the unit); <br/>
 * 2. {@link Prices} - Open-High-Low-Close prices for market data day; <br/>
 * 3. {@link #volume} - double value of traded shares amount; <br/>
 * 4. {@link #adjClose} - double value of adjective close price.
 */
public final class Day implements Comparable<Day> {

	// This is static and that's OK. Because we use it only for debug.
	private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder(). //
			appendPattern("dd-MM-yyyy"). //
			toFormatter(Locale.US). //
			withZone(ZoneOffset.UTC);

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	private final LocalDate date;
	private final Prices prices;
	private final double volume;
	private final double adjClose;

	public static LocalDate createDate(final String dateRepresentation) throws DateTimeParseException {
		return LocalDate.parse(dateRepresentation, dateTimeFormatter);
	}

	public static Day createForSearch(final LocalDate d) {
		return new Day(d, null, 0.0, 0.0);
	}

	public Day(final LocalDate d, final Prices p, final double v, final double ac) {
		date = d;
		prices = p;
		volume = v;
		adjClose = ac;
	}

	public double getVolume() {
		return volume;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public LocalDate getDate() {
		return date;
	}

	public Prices getPrices() {
		return prices;
	}

	@Override
	public int compareTo(final Day day) {
		return date.compareTo(day.date);
	}

	@Override
	public String toString() {
		return "Day:" + date.toString() + "(" + getPrices() + ")";
	}
}