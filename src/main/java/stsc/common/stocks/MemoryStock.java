package stsc.common.stocks;

import java.util.ArrayList;

import stsc.common.Day;

/**
 * In random access memory stock (not thread safe), it's better to use for tests
 * or some very internal data storage.
 */
public final class MemoryStock extends Stock {

	private final String name;
	private final ArrayList<Day> days = new ArrayList<Day>();

	public MemoryStock(String name) {
		this.name = name;
	}

	@Override
	public String getInstrumentName() {
		return name;
	}

	@Override
	public ArrayList<Day> getDays() {
		return days;
	}

}
