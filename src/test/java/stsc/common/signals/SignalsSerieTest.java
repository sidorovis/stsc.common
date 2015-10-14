package stsc.common.signals;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import stsc.common.BadSignalException;

public class SignalsSerieTest {

	private static class SignalsSerieHelper extends SignalsSerie<Integer, LocalDate> {

		private final Integer dateSignal = 15;
		private Integer indexSignal = 24;

		public SignalsSerieHelper(Class<Integer> signalClass) {
			super(signalClass);
		}

		@Override
		public SignalContainer<Integer, LocalDate> getSignal(LocalDate date) {
			return new SignalContainer<Integer, LocalDate>(0, date, dateSignal);
		}

		@Override
		public SignalContainer<Integer, LocalDate> getSignal(int index) {
			return new SignalContainer<Integer, LocalDate>(index, LocalDate.now(), indexSignal);
		}

		@Override
		public void addSignal(LocalDate date, Integer signal) throws BadSignalException {
			indexSignal = signal;
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public String toString() {
			return "test string";
		}

	}

	@Test
	public void testSignalsSerie() throws BadSignalException {
		final SignalsSerieHelper helper = new SignalsSerieHelper(Integer.class);
		Assert.assertEquals(15, helper.getSignal(LocalDate.now()).getContent().intValue());
		Assert.assertEquals(24, helper.getSignal(45).getContent().intValue());
		helper.addSignal(LocalDate.now(), new Integer(56));
		Assert.assertEquals(56, helper.getSignal(99).getContent().intValue());
	}
}
