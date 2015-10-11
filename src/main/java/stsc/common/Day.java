package stsc.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.LocalDate;
import stsc.common.stocks.Prices;

/**
 * Day data - is a collection of information about smallest unit of input data
 * type. <br/>
 * For market trading, this smallest unit (input data type) contain next fields:
 * <br/>
 * 1. {@link Date} (original date/time of the unit); <br/>
 * 2. {@link Prices} - Open-High-Low-Close prices for market data day; <br/>
 * 3. {@link #volume} - double value of traded shares amount; <br/>
 * 4. {@link #adjClose} - double value of adjective close price.
 */
public final class Day implements Comparable<Day> {

	// This is static and that's OK. Because we use it only for debug.
	private static final DateFormat df;

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		df = new SimpleDateFormat("dd-MM-yyyy");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public final Date date;
	public final Prices prices;
	public final double volume;
	public final double adjClose;

	public static Date createDate(final LocalDate date) {
		return nullableTime(date.toDate());
	}

	public static Date createDate() {
		return nullableTime(new Date());
	}

	public static Date createDate(String dateRepresentation) throws ParseException {
		return df.parse(dateRepresentation);
	}

	static public Date nullableTime(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		final Date result = cal.getTime();
		return result;
	}

	public Day(Date d) {
		date = d;
		prices = null;
		volume = 0.0;
		adjClose = 0.0;
	}

	public Day(Date d, Prices p, double v, double ac) {
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

	public Date getDate() {
		return date;
	}

	public Prices getPrices() {
		return prices;
	}

	@Override
	public int compareTo(Day o) {
		return date.compareTo(o.date);
	}

	@Override
	public String toString() {
		return "Day:" + df.format(date) + "(" + getPrices() + ")";
	}
}