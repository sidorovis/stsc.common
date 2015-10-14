package stsc.common;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Properties;

/**
 * Description for time period (from / to). <br/>
 * Used for simulation process to describe period (from first to last trading
 * dates).
 */
public final class FromToPeriod { // implements Externalizable {

	private final LocalDate from;
	private final LocalDate to;

	public FromToPeriod(final Properties p) throws ParseException {
		this(p.getProperty("Period.from"), p.getProperty("Period.to"));
	}

	public FromToPeriod(final String from, final String to) throws ParseException {
		this.from = Day.createDate(from);
		this.to = Day.createDate(to);
	}

	public FromToPeriod(final LocalDate from, final LocalDate to) {
		this.from = from;
		this.to = to;
	}

	public LocalDate getFrom() {
		return from;
	}

	public LocalDate getTo() {
		return to;
	}

	@Override
	public String toString() {
		return from.toString() + " -> " + to.toString();
	}
	//
	// public static FromToPeriod read(ObjectInput in) throws IOException {
	// final LocalDate from = LocalDate.ofEpochDay(in.readLong());
	// final LocalDate to = LocalDate.ofEpochDay(in.readLong());
	// return new FromToPeriod(from, to);
	// }
	//
	// @Override
	// public void readExternal(ObjectInput in) throws IOException,
	// ClassNotFoundException {
	// }
	//
	// @Override
	// public void writeExternal(ObjectOutput out) throws IOException {
	// out.writeLong(from.toEpochDay());
	// out.writeLong(to.toEpochDay());
	// }

}
