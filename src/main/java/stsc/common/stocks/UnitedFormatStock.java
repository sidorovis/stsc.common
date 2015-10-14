package stsc.common.stocks;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

	private static final String LOCAL_DATE_VERSION_MARKER = "__LocalDateVersion__";

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	// This is static and that's OK. Because we use it only for debug.
	private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder(). //
			appendPattern("yyyy-MM-dd"). //
			toFormatter(Locale.US). //
			withZone(ZoneOffset.UTC);

	private final String instrumentName;
	private final UnitedFormatFilename fileName;
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

	public static UnitedFormatStock readFromUniteFormatFile(final InputStream is) throws IOException {
		try (DataInputStream dis = new DataInputStream(new BufferedInputStream(is))) {
			return readFromUniteFormatFile(dis);
		}
	}

	private static UnitedFormatStock readFromUniteFormatFile(final DataInputStream is) throws IOException {
		UnitedFormatStock s = null;
		final String instrumentName = is.readUTF();
		if (instrumentName.equals(LOCAL_DATE_VERSION_MARKER)) {
			return readFromLocalDateUniteFormatFile(is);
		}
		s = new UnitedFormatStock(instrumentName);
		int daysLength = is.readInt();
		for (int i = 0; i < daysLength; ++i) {
			final long epochLongTime = is.readLong();
			final LocalDate dayTime = new Date(epochLongTime).toInstant().atZone(ZoneOffset.UTC).toLocalDate();
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

	private static UnitedFormatStock readFromLocalDateUniteFormatFile(DataInputStream is) throws IOException {
		final String instrumentName = is.readUTF();
		final UnitedFormatStock s = new UnitedFormatStock(instrumentName);
		int daysLength = is.readInt();
		for (int i = 0; i < daysLength; ++i) {
			final long epochLongTime = is.readLong();
			final LocalDate dayTime = LocalDate.ofEpochDay(epochLongTime);
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
			final LocalDate date = LocalDate.parse(lineDate, dateTimeFormatter);
			final String[] tokens = line.split(",");
			final double volume = Double.parseDouble(tokens[5]);
			final double adjClose = Double.parseDouble(tokens[6]);
			final Day newDay = new Day(date, Prices.fromTokens(tokens, adjClose), volume, adjClose);
			stock.addDay(newDay);
		} catch (DateTimeParseException | NumberFormatException e) {
			throw new ParseException(e.toString() + " while parsing data: '" + line + "' ", 1);
		}
	}

	private UnitedFormatStock(final String instrumentName) {
		this.instrumentName = instrumentName.toLowerCase();
		this.fileName = UnitedFormatHelper.toFilesystem(instrumentName);
	}

	@Override
	public String getInstrumentName() {
		return instrumentName;
	}

	public UnitedFormatFilename getFilesystemName() {
		return fileName;
	}

	public void storeUniteFormatToFolder(final Path folderPath) throws IOException {
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream(folderPath.resolve(fileName.getFilename()).toFile()))) {
			storeUniteFormat(os);
		}
	}

	private void storeUniteFormat(final DataOutputStream os) throws IOException {
		os.writeUTF(LOCAL_DATE_VERSION_MARKER);
		os.writeUTF(instrumentName);
		os.writeInt(days.size());
		for (Day day : days) {
			os.writeLong(day.getDate().toEpochDay());
			os.writeDouble(day.getPrices().getOpen());
			os.writeDouble(day.getPrices().getHigh());
			os.writeDouble(day.getPrices().getLow());
			os.writeDouble(day.getPrices().getClose());
			os.writeDouble(day.getVolume());
			os.writeDouble(day.getAdjClose());
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

}
