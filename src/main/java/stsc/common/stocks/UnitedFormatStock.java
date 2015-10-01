package stsc.common.stocks;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stsc.common.Day;

/**
 * United Format Stock class is main data for stock (financial instrument) data.
 * By default could be used with many different use cases. <br/>
 * <u>1. Creating mechanisms: </u><br/>
 * 1.a. creating from CSV file (see Yahoo Stock Market Datafeed format
 * https://code.google.com/p/yahoo-finance-managed/wiki/csvHistQuotesDownload ).
 * <br/>
 * 1.b. creating from CSV like string.<br/>
 * 1.c. creating from binary file (internal format (will be described later in
 * this file). <br/>
 * <b> 1.c.1. remember that Stock could be extended with data using, (make sure
 * that you don't call addDaysFromString method during processing). </b> <br/>
 * <u>2. Storing mechanism is method storeUniteFormatToFolder(). </u> <br/>
 * Use it for storing {@link UnitedFormatStock} in binary representation. <br/>
 * Make sure that result file contain only lowercase letters / digits / '_'
 * underscore character / '^' pow character.
 * <hr/>
 * <b>Internal Format</b><br/>
 * Internal (United) stock format is an binary format that was created to easily
 * store / load data from the filesystem files. <br/>
 * Format is next: <br/>
 * [UTF name] - stock name<br/>
 * [Int daysAmount] - amount of stored days<br/>
 * <i>:: daysAmount size loop ::</i> <br/>
 * -- [Long dateTime] - value for one of the days<br/>
 * -- [Double open] - open value for {@link Day} data;<br/>
 * -- [Double high] - high value for {@link Day} data;<br/>
 * -- [Double low] - low value for {@link Day} data;<br/>
 * -- [Double close] - close value for {@link Day} data;<br/>
 * -- [Double volume] - volume value for {@link Day} data;<br/>
 * -- [Double adjClose] - adjective close value for {@link Day} data;<br/>
 */
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
	private final ArrayList<Day> days = new ArrayList<Day>();

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

	public static UnitedFormatStock readFromUniteFormatFile(final InputStream is) throws IOException {
		try (DataInputStream dis = new DataInputStream(is)) {
			return readFromUniteFormatFile(dis);
		}
	}

	public static UnitedFormatStock readFromUniteFormatFile(DataInputStream is) throws IOException {
		UnitedFormatStock s = null;
		final String instrumentName = is.readUTF();
		s = new UnitedFormatStock(instrumentName);
		int daysLength = is.readInt();
		for (int i = 0; i < daysLength; ++i) {
			final Date dayTime = Day.nullableTime(new Date(is.readLong()));
			final double open = is.readDouble();
			final double high = is.readDouble();
			final double low = is.readDouble();
			final double close = is.readDouble();
			final double volume = is.readDouble();
			final double adjClose = is.readDouble();
			final Day newDay = new Day(dayTime, Prices.calculatePrices(open, high, low, close, adjClose), volume, adjClose);
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

	private UnitedFormatStock(final String instrumentName) {
		this.instrumentName = instrumentName.toLowerCase();
		this.fileName = UnitedFormatStock.toFilesystem(instrumentName.toLowerCase());
	}

	@Override
	public String getInstrumentName() {
		return instrumentName;
	}

	public void storeUniteFormatToFolder(final String folderPath) throws IOException {
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream(generatePath(folderPath, fileName)))) {
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

	/**
	 * Please make sure that you use this method only when you are sure that
	 * there is no parallel thread using this Stock data.
	 */
	public boolean addDaysFromString(String newData) throws ParseException {
		String[] lines = newData.split("\n");
		Collections.reverse(Arrays.asList(lines));
		for (int i = 0; i < lines.length - 1; ++i)
			if (!lines[i].isEmpty())
				storeDataLine(this, lines[i]);
		return lines.length > 1;
	}

	/**
	 * TODO think how to delete this method from this class.<br/>
	 * Days Content, very dangerous method. Please use it carefully.
	 */
	@Override
	public ArrayList<Day> getDays() {
		return days;
	}

	public static String generatePath(String folderPath, String fileName) {
		final Path filePath = FileSystems.getDefault().getPath(folderPath).resolve(fileName + EXTENSION);
		return filePath.toString();
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
