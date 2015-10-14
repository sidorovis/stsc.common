package stsc.common.stocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import stsc.common.Day;
import stsc.common.DayComparator;

public abstract class Stock {

	public abstract String getInstrumentName();

	public abstract ArrayList<Day> getDays();

	public int findDayIndex(final LocalDate date) {
		ArrayList<Day> days = getDays();
		int index = Collections.binarySearch(days, Day.createForSearch(date), DayComparator.getInstance());
		if (index < 0)
			index = -index - 1;
		return index;
	}

	@Override
	public String toString() {
		String result = "Stock(" + getInstrumentName() + ")\n";
		int count = 0;
		final ArrayList<Day> days = new ArrayList<>(getDays());
		Collections.reverse(days);
		for (Day day : days) {
			result += day.toString() + "\n";
			if (count++ > 20)
				break;
		}
		return result;
	}
}
