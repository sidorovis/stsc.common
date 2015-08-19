package stsc.common.stocks;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;

import stsc.common.Day;

public final class UnitedFormatStock extends Stock {

	public final static String EXTENSION = ".uf";

	private static final TimeZone timeZone;
	private static final DateFormat dateFormat;

	private static final class Regexps {
		public static final Pattern stockNamePrefix = Pattern.compile("^_(\\d{3})(.+)$");
		public static final Pattern notSymbolPrefix = Pattern.compile("^([\\^$#\\.])(.+)$");
	}

	static {
		timeZone = TimeZone.getTimeZone("UTC");
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(timeZone);
	}

	private final String instrumentName;
	private final String fileName;
	ArrayList<Day> days = new ArrayList<Day>();

	public static UnitedFormatStock readFromCsvFile(String name, String filePath) throws IOException, ParseException {
		byte[] data = Files.readAllBytes(Paths.get(filePath));
		String content = new String(data);
		return UnitedFormatStock.newFromString(name, content);
	}

	public static UnitedFormatStock newFromString(String instrumentName, String content) throws ParseException {
		UnitedFormatStock stock = new UnitedFormatStock(instrumentName);
		String[] lines = content.split("\n");
		Collections.reverse(Arrays.asList(lines));
		for (int i = 0; i < lines.length - 1; ++i)
			if (!lines[i].isEmpty())
				storeDataLine(stock, lines[i]);
		return stock;
	}

	public static UnitedFormatStock readFromUniteFormatFile(String filePath) throws IOException {
		try (DataInputStream is = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)))) {
			return readFromUniteFormatFile(is);
		}
	}

	public static UnitedFormatStock readFromUniteFormatFile(DataInputStream is) throws IOException {
		UnitedFormatStock s = null;
		final String instrumentName = is.readUTF();
		s = new UnitedFormatStock(instrumentName);
		int daysLength = is.readInt();
		for (int i = 0; i < daysLength; ++i) {
			Date dayTime = Day.nullableTime(new Date(is.readLong()));
			double open = is.readDouble();
			double high = is.readDouble();
			double low = is.readDouble();
			double close = is.readDouble();
			double volume = is.readDouble();
			double adjClose = is.readDouble();
			Day newDay = new Day(dayTime, Prices.calculatePrices(open, high, low, close, adjClose), volume, adjClose);
			s.addDay(newDay);
		}
		return s;
	}

	static private void storeDataLine(UnitedFormatStock stock, String line) throws ParseException {
		final String lineDate = line.substring(0, 10);
		try {
			final Date date = Day.nullableTime(dateFormat.parse(lineDate));
			final String[] tokens = line.split(",");
			final double volume = Double.parseDouble(tokens[5]);
			final double adjClose = Double.parseDouble(tokens[6]);
			final Day newDay = new Day(date, Prices.fromTokens(tokens, adjClose), volume, adjClose);
			stock.addDay(newDay);
		} catch (ParseException e) {
			throw new ParseException(e.toString() + " while parsing data: " + lineDate, 1);
		} catch (NumberFormatException e) {
			throw new ParseException(e.toString() + " while parsing data: '" + line + "' ", 1);
		}
	}

	public UnitedFormatStock(final String instrumentName) {
		this.instrumentName = instrumentName.toLowerCase();
		this.fileName = UnitedFormatStock.toFilesystem(instrumentName.toLowerCase());
	}

	@Override
	public String getInstrumentName() {
		return instrumentName;
	}

	public void storeUniteFormatToFolder(String folderPath) throws IOException {
		final String filePath = folderPath + "/" + fileName + EXTENSION;
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream(filePath))) {
			storeUniteFormat(os);
		}
	}

	private void storeUniteFormat(DataOutputStream os) throws IOException {
		os.writeUTF(instrumentName);
		os.writeInt(days.size());
		for (Day day : days) {
			os.writeLong(Day.nullableTime(day.date).getTime());
			os.writeDouble(day.prices.open);
			os.writeDouble(day.prices.high);
			os.writeDouble(day.prices.low);
			os.writeDouble(day.prices.close);
			os.writeDouble(day.volume);
			os.writeDouble(day.adjClose);
		}
	}

	private void addDay(Day d) {
		days.add(d);
	}

	public boolean addDaysFromString(String newData) throws ParseException {
		String[] lines = newData.split("\n");
		Collections.reverse(Arrays.asList(lines));
		for (int i = 0; i < lines.length - 1; ++i)
			if (!lines[i].isEmpty())
				storeDataLine(this, lines[i]);
		return lines.length > 1;
	}

	/**
	 * Days Content.
	 */
	@Override
	public ArrayList<Day> getDays() {
		return days;
	}

	public String generatePartiallyDownloadLine() {
		final Date lastDate = days.get(days.size() - 1).date;
		final Calendar cal = Calendar.getInstance();
		cal.setTime(lastDate);
		if (new LocalDate(lastDate).equals(new LocalDate(new Date()))) {
			return "";
		}
		if (new LocalDate(lastDate).plusDays(1).equals(new LocalDate(new Date()))) {
			return "";
		}
		cal.add(Calendar.DATE, 1);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		return "http://ichart.yahoo.com/table.csv?s=" + instrumentName + "&a=" + month + "&b=" + day + "&c=" + year;
	}

	public static String generatePath(String dataFolder, String filesystemName) {
		return dataFolder + filesystemName + EXTENSION;
	}

	/*
	 * ^FTSE -> _094FTSE
	 */
	public final static String toFilesystem(String stockName) {
		final Matcher matcher = Regexps.notSymbolPrefix.matcher(stockName);
		if (matcher.matches()) {
			final String code = matcher.group(1);
			final String postfix = matcher.group(2);
			final int c = (int) (code.charAt(0));
			final String prefix = String.format("_%03d", c);
			return prefix + postfix;
		}
		return stockName;
	}

	/*
	 * _094FTSE -> ^FTSE
	 */
	public final static String fromFilesystem(String stockName) {
		final Matcher matcher = Regexps.stockNamePrefix.matcher(stockName);
		if (matcher.matches()) {
			final int code = Integer.valueOf(matcher.group(1));
			final String symbol = String.valueOf(Character.toChars(code));
			final String postfix = matcher.group(2);
			return symbol + postfix;
		}
		return stockName;
	}

}
